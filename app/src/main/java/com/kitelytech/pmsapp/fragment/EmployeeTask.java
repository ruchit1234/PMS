package com.kitelytech.pmsapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.MainActivity;
import com.kitelytech.pmsapp.pojo.Fetchempproj;
import com.kitelytech.pmsapp.pojo.FileUtils;
import com.kitelytech.pmsapp.pojo.Message;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Employeeprojlist;
import com.kitelytech.pmsapp.rest.Emptask;
import com.kitelytech.pmsapp.rest.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class EmployeeTask extends Fragment {

    RadioGroup type;
    EditText tname, tdesc;
    TextView esthrs;
    Spinner sp,spinner;
    String name,desc,esthr;
    public EmployeeTask() {
        // Required empty public constructor
    }


    public static taskhistory newInstance(String param1, String param2) {
        taskhistory fragment = new taskhistory();
        Bundle args = new Bundle();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emptask, container, false);
        Button submit, cancel;
        cancel=view.findViewById(R.id.empcancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(),MainActivity.class);
                startActivity(in);
            }
        });
        tname=view.findViewById(R.id.empettasknm);
        tdesc=view.findViewById(R.id.empettaskdesc);
        type = view.findViewById(R.id.rdgrp);
        esthrs=view.findViewById(R.id.empetesthour);
        esthrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour>4){
                            esthrs.setText("");
                            Toast.makeText(getContext(),"You Cannot Select More Than 4 Hours",Toast.LENGTH_LONG).show();
                        }else if (selectedHour<=4){
                        esthrs.setText( selectedHour + ":" + selectedMinute+":"+"00");
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        submit=view.findViewById(R.id.empsubmit);
        // find the radiobutton by returned id
            //get radio button view

// find the radioButton by returned id
        int id = type.getCheckedRadioButtonId();
// radioButton text
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= tname.getText().toString();
                desc=tdesc.getText().toString();
                esthr=esthrs.getText().toString();
               String item= sp.getSelectedItem().toString();
               String proitem=spinner.getSelectedItem().toString();
               if (name.isEmpty()) {
                   Toast.makeText(getContext(),"Task Name Cannot Be Empty!",Toast.LENGTH_LONG).show();
               }
               else if(desc.isEmpty()) {
                   Toast.makeText(getContext(),"Task Description Cannot Be Empty!",Toast.LENGTH_LONG).show();

               }
               else if (id != -1) {
                    Toast.makeText(getContext(),"Please Select A Task Type!",Toast.LENGTH_LONG).show();
                }
                else if (proitem.contains("Select A Project")){
                   Toast.makeText(getContext(),"Please Select A Project!",Toast.LENGTH_LONG).show();
               }
                else if (item.contains("Select Level")){
                   Toast.makeText(getContext(),"Please Select A Level!",Toast.LENGTH_LONG).show();
               }
               else if (esthr.isEmpty()) {
                   // Your code.
                   Toast.makeText(getContext(),"Please Enter Estimated Hours!",Toast.LENGTH_LONG).show();
                }
                else if (selectedfile==null){
                   addtask();
               }
               else{
                   addtask();
               }


            }

        });
        cancel = view.findViewById(R.id.cancel);
        fetchDepartmentName();
        CardView cv = view.findViewById(R.id.uploaddoc);
        cv.setOnClickListener(v -> {
            Intent intent = new Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
        });
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.add("Select Level");
        listSpinner.add("NP");
        listSpinner.add("Urgent");
        listSpinner.add("Highest");
        listSpinner.add("High");
        listSpinner.add("Normal");
        listSpinner.add("Low");
        listSpinner.add("Lowest");
        final ArrayAdapter a = new ArrayAdapter(getActivity(), R.layout.spinner_item_black, listSpinner);
        a.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp = view.findViewById(R.id.empspsellvl);
        sp.setAdapter(a);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String item=sp.getSelectedItem().toString();
                String tsklvl = null;
                if (item.equals("NP"))
                {
                    tsklvl="-1";
                }
                else if (item.equals("Urgent"))
                {
                    tsklvl="0";
                }
                else if (item.equals("Highest"))
                {
                    tsklvl="1";
                }
                else if (item.equals("High"))
                {
                    tsklvl="2";
                }
                else if (item.equals("Normal"))
                {
                    tsklvl="3";
                }
                else if (item.equals("Low"))
                {
                    tsklvl="4";
                }
                else if (item.equals("Lowest"))
                {
                    tsklvl="5";
                }
                if (tsklvl!=null) {
                    SharedPreferences settings = getActivity().getSharedPreferences("tasklvl", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("lvl", tsklvl);
                    editor.commit();

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

            }

        });
        return view;
    }

    public void addtask() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Emptask service = retrofit.create(Emptask.class);
        int id = type.getCheckedRadioButtonId();
        String selection = null;
        if (id != -1) {
            //get radio button view
            View radioButton = type.findViewById(id);
            // return index of selected radiobutton
            int radioId = type.indexOfChild(radioButton);
            // based on index getObject of radioButton
            RadioButton btn = (RadioButton) type.getChildAt(radioId);
            //After getting radiobutton you can now use all its methods
            selection = (String) btn.getText();
        }
        
        Intent localIntent = getActivity().getIntent();
        String tid = localIntent.getStringExtra("tid");
        SharedPreferences settings = getActivity().getSharedPreferences("PrefEID", 0);
        String eid = settings.getString("eid", "").toString();
        SharedPreferences set = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken= set.getString("email","").toString();
        SharedPreferences pid = getActivity().getSharedPreferences("pid", 0);
        String prid= pid.getString("pid","").toString();
        SharedPreferences lvl = getActivity().getSharedPreferences("tasklvl", 0);
        String tsklvl= lvl.getString("lvl","").toString();
        // create RequestBody instance from file

      String tskname=tname.getText().toString();
    String tskdesc=tdesc.getText().toString();
         String empesthrs=esthrs.getText().toString();

        // MultipartBody.Part is used to send also the actual file name
        java.io.File file = FileUtils.getFile(getActivity(), selectedfile);
        if (selectedfile==null){
            try {
                RequestBody taskname =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, tskname);
                RequestBody taskdesc =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, tskdesc);
                RequestBody taskcrtby =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, emailtoken);
                RequestBody projectid =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, prid);
                RequestBody assignto =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, eid);
                if (selection.equals("Dev")){
                    selection="0";

                }
                else if (selection.equals("QA")){
                    selection="1";
                }
                RequestBody tasktype =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, selection);
                RequestBody esthrs =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, empesthrs);
                RequestBody tasklvl =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, tsklvl);


                // finally, execute the request
                Call<ResponseBody> call = service.Task_Add_By_Employee_Without_File(taskname, taskdesc,taskcrtby,projectid,assignto,tasktype,esthrs,tasklvl);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        Toast.makeText(getContext(), "Task Created", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }        }
            else{
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getActivity().getContentResolver().getType(selectedfile)),
                        (java.io.File) file
                );
        if (fname==null&&requestFile==null){
            fname="";
        }

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", fname, requestFile);

        // add another part within the multipart request
        RequestBody filename =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, fname);


        RequestBody taskname =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, tskname);
        RequestBody taskdesc =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, tskdesc);
        RequestBody taskcrtby =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, emailtoken);
        RequestBody projectid =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, prid);
        RequestBody assignto =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, eid);
        if (selection.equals("Dev")){
            selection="0";

        }
        else if (selection.equals("QA")){
            selection="1";
        }
        RequestBody tasktype =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, selection);
        RequestBody esthrs =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, empesthrs);
        RequestBody tasklvl =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, tsklvl);


        // finally, execute the request
        Call<ResponseBody> call = service.Task_Add_By_Employee(body,filename,taskname, taskdesc,taskcrtby,projectid,assignto,tasktype,esthrs,tasklvl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Toast.makeText(getContext(), "Task Created", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }}
    private void fetchDepartmentName(){
//Creating a rest adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creating an object of our api interface
        Employeeprojlist service = retrofit.create(Employeeprojlist.class);
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken= settings.getString("email", "").toString();
        Call<Fetchempproj> call = service.Bind_Employee_Projects(emailtoken);
        call.enqueue(new Callback<Fetchempproj>() {
            @Override
            public void onResponse(Call<Fetchempproj> call, Response<Fetchempproj> response) {
                List<Message> listResponse =response.body().getMessage();
                List<String> listSpinner = new ArrayList<String>();
                listSpinner.add("Select A Project");
                for (int i = 0; i < listResponse.size(); i++){
                    String id=listResponse.get(i).getProjectId().toString();
                    String name=listResponse.get(i).getProjectName();
                    String fn=id+"."+name;
                    listSpinner.add(fn);
                }

                final ArrayAdapter a = new ArrayAdapter(getActivity(),R.layout.spinner_item_black,listSpinner);
                a.setDropDownViewResource(R.layout.spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner = getActivity().findViewById(R.id.empspproj);
                spinner.setAdapter(a);
                loading.dismiss();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String item=spinner.getSelectedItem().toString();
                        String a=item.replaceAll("([a-z])", "");
                        String b=a.replaceAll("([A-Z])", "");
                        String prid=b.replaceAll("([.])", "");
                        SharedPreferences settings = getActivity().getSharedPreferences("pid", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("pid", prid);
                        editor.commit();



                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here

                    }

                });
            }
            @Override
            public void onFailure(Call<Fetchempproj> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public void onDetach() {
        super.onDetach();

    }
Uri selectedfile;
    String fname;
    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
          try {
              selectedfile = data.getData(); //The uri with the location of the file
              fname = getFileName(selectedfile);
          }
          catch (NullPointerException e){
              e.printStackTrace();
          }
        }
    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
