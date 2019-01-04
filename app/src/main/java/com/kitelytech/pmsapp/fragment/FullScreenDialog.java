package com.kitelytech.pmsapp.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.MainActivity;
import com.kitelytech.pmsapp.pojo.Fetchempproj;
import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.Message;
import com.kitelytech.pmsapp.pojo.MessageItem;
import com.kitelytech.pmsapp.pojo.ResponseData;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Employeeprojlist;
import com.kitelytech.pmsapp.rest.ManagerTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Khyati Kachhiya on 22,October,2018
 * Cloudester Software
 */
public class FullScreenDialog extends DialogFragment {

    public static final String TAG = FullScreenDialog.class.getSimpleName();
Button btsubmit,btcancel;
EditText tname,tdesc;
TextView estdevhrs,estqahrs,estcharghrs;
String lvl;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

            }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_fullscreen_dialog, container, false);
btsubmit = view.findViewById(R.id.mansubmit);
tname = view.findViewById(R.id.ettasknm);
tdesc = view.findViewById(R.id.ettaskdesc);
estcharghrs=view.findViewById(R.id.etcharghours);
estdevhrs=view.findViewById(R.id.etestdevhour);
estqahrs=view.findViewById(R.id.etestqahour);
btcancel = view.findViewById(R.id.mancancel);
btsubmit.setOnClickListener(v -> {
    addtask();
});
estqahrs.setOnClickListener(v -> {
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            estqahrs.setText(selectedHour + ":" + selectedMinute+":"+"00");
        }
    }, hour, minute, true);//Yes 24 hour time
    mTimePicker.setTitle("Select Time");
    mTimePicker.show();
});
estdevhrs.setOnClickListener(v -> {
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            estdevhrs.setText(selectedHour + ":" + selectedMinute+":"+"00");
        }
    }, hour, minute, true);//Yes 24 hour time
    mTimePicker.setTitle("Select Time");
    mTimePicker.show();
});


        btcancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }
});
fetchprojects();
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.add("Select Level");
        listSpinner.add("NP");
        listSpinner.add("Urgent");
        listSpinner.add("Highest");
        listSpinner.add("High");
        listSpinner.add("Normal");
        listSpinner.add("Low");
        listSpinner.add("Lowest");
        final ArrayAdapter a = new ArrayAdapter(getActivity(),R.layout.spinner_item_black,listSpinner);
        a.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        Spinner sp=view.findViewById(R.id.spsellvl);
        sp.setAdapter(a);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=sp.getSelectedItem().toString();
                if (item.contains("NP")){
                    lvl = "-1";
                }
               else if (item.contains("Urgent")){
                    lvl = "0";
                }
                else if (item.contains("Highest")){
                    lvl = "1";
                }
                else if (item.contains("High")){
                    lvl = "2";
                }
               else if (item.contains("Normal")){
                    lvl = "3";
                }
               else if (item.contains("Low")){
                    lvl = "4";
                }
                else if (item.contains("Lowest")){
                    lvl = "5";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.layout_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                Toast.makeText(getActivity(), "Save New Project", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void cancelUpload(){
        Dialog dialog = getDialog();
        dialog.dismiss();
    }
    public  void addtask(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken= settings.getString("email", "").toString();
        SharedPreferences man = getActivity().getSharedPreferences("manid", 0);
        String pid= man.getString("pid", "").toString();
        String did= man.getString("did", "").toString();
        String qid= man.getString("qid", "").toString();
        //Creating an object of our api interface
        ManagerTask service = retrofit.create(ManagerTask.class);
        Call<Result> call = service.Task_Details_and_Dev_Qa(tname.getText().toString(),tdesc.getText().toString(),emailtoken,pid,"",did,qid,estdevhrs.getText().toString(),estqahrs.getText().toString(),lvl,estcharghrs.getText().toString());
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                String msg=response.body().getMessage();
                //if (msg.equals("Task Added Successfully..")){

                    Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();

                //}
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });

    }

    private void fetchprojects(){
//Creating a rest adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creating an object of our api interface
        ManagerTask service = retrofit.create(ManagerTask.class);
        //While the app fetched data we are displaying a progress dialog
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken= settings.getString("email", "").toString();
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);
        Call<Fetchempproj> call = service.Bind_CurrentManager_Project(emailtoken);
        call.enqueue(new Callback<Fetchempproj>() {
            @Override
            public void onResponse(Call<Fetchempproj> call, Response<Fetchempproj> response) {
                List<Message> listResponse =response.body().getMessage();
                List<String> listSpinner = new ArrayList<String>();
                listSpinner.add("Select Project");
                for (int i = 0; i < listResponse.size(); i++){
                    String id=listResponse.get(i).getProjectId().toString();
                    String name=listResponse.get(i).getProjectName();
                    String fn=id+"."+name;
                    listSpinner.add(fn);
                }

                final ArrayAdapter a = new ArrayAdapter(getActivity(),R.layout.spinner_item_black,listSpinner);
                a.setDropDownViewResource(R.layout.spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                Spinner spinner = getActivity().findViewById(R.id.spproj);
                spinner.setAdapter(a);
                loading.dismiss();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String item=spinner.getSelectedItem().toString();
                        String a=item.replaceAll("([a-z])", "");
                        String b=a.replaceAll("([A-Z])", "");
                        String prid=b.replaceAll("([.])", "");
                        SharedPreferences settings = getActivity().getSharedPreferences("manid", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("pid", prid);
                        editor.commit();
                       /* SharedPreferences settings = getActivity().getSharedPreferences("manager", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("pid", prid);
                        editor.commit();*/
                        if (item.contains("Select Project")){

                        }else {
                            fetchdevs();
                            fetchqa();
                        }

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
    private void fetchdevs(){
//Creating a rest adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creating an object of our api interface
        ManagerTask service = retrofit.create(ManagerTask.class);
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);
        SharedPreferences settings = getActivity().getSharedPreferences("manager", 0);
        String pid=settings.getString("pid","");
        Call<ResponseData> call = service.View_Project_Employees(pid);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                List<MessageItem> listResponse =response.body().getMessage();
                List<String> listSpinner = new ArrayList<String>();
                listSpinner.add("Select Developer");
                for (int i = 0; i < listResponse.size(); i++){
                    String id= String.valueOf(listResponse.get(i).getEmpid());
                    String fname=listResponse.get(i).getEmpFirstName();
                    String lname=listResponse.get(i).getEmpLastName();
                    String fn=id+"."+fname+" "+lname;
                    listSpinner.add(fn);
                }

                final ArrayAdapter a = new ArrayAdapter(getActivity(),R.layout.spinner_item_black,listSpinner);
                a.setDropDownViewResource(R.layout.spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                Spinner spinner = getActivity().findViewById(R.id.spseldev);
                spinner.setAdapter(a);
                loading.dismiss();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String item=spinner.getSelectedItem().toString();
                        String a=item.replaceAll("([a-z])", "");
                        String b=a.replaceAll("([A-Z])", "");
                        String prid=b.replaceAll("([.])", "");
                        SharedPreferences settings = getActivity().getSharedPreferences("manid", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("did", prid);
                        editor.commit();



                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here

                    }

                });
            }
            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getActivity(), "Please Select A Project First", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void fetchqa(){
//Creating a rest adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creating an object of our api interface
        ManagerTask service = retrofit.create(ManagerTask.class);
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);
        SharedPreferences settings = getActivity().getSharedPreferences("manager", 0);
        String pid=settings.getString("pid","");
        Call<ResponseData> call = service.View_Project_Employees(pid);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                List<MessageItem> listResponse =response.body().getMessage();
                List<String> listSpinner = new ArrayList<String>();
                listSpinner.add("Select QA");
                for (int i = 0; i < listResponse.size(); i++){
                    String id= String.valueOf(listResponse.get(i).getEmpid());
                    String fname=listResponse.get(i).getEmpFirstName();
                    String lname=listResponse.get(i).getEmpLastName();
                    String fn=id+"."+fname+" "+lname;
                    listSpinner.add(fn);
                }

                final ArrayAdapter a = new ArrayAdapter(getActivity(),R.layout.spinner_item_black,listSpinner);
                a.setDropDownViewResource(R.layout.spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                Spinner spinner = getActivity().findViewById(R.id.spqa);
                spinner.setAdapter(a);
                loading.dismiss();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String item=spinner.getSelectedItem().toString();
                        String a=item.replaceAll("([a-z])", "");
                        String b=a.replaceAll("([A-Z])", "");
                        String prid=b.replaceAll("([.])", "");
                        SharedPreferences settings = getActivity().getSharedPreferences("manid", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("qid", prid);
                        editor.commit();


                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here

                    }

                });
            }
            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getActivity(), "Please Select A Project First", Toast.LENGTH_LONG).show();
            }
        });

    }

}
