package com.kitelytech.pmsapp.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.adapter.Filelistadapter;
import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.FileUtils;
import com.kitelytech.pmsapp.pojo.MessageItem;
import com.kitelytech.pmsapp.pojo.ResponseData;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Fetchemployeedetailsinterface;
import com.kitelytech.pmsapp.rest.File;
import java.util.ArrayList;
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


public class Files extends Fragment{
    RecyclerView rvfilelist;
    Filelistadapter filelistadapter;
    Filelistadapter.DownloadFile downloadFile;
     EditText edt;
    private String filename;

    public Files() {
        // Required empty public constructor
    }


    public static Files newInstance(String param1, String param2) {
        Files fragment = new Files();
        Bundle args = new Bundle();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_files, container, false);

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        FloatingActionButton addfile=view.findViewById(R.id.addfile);
        addfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filename=null;
                if (filename==null){
                showChangeLangDialog();
                }

                }


        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//Result is our pojo class
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken = settings.getString("email", "").toString();
        Intent localIntent = getActivity().getIntent();
        String tid=localIntent.getStringExtra("tid");
        File service = retrofit.create(File.class);
        Fetchemployeedetailsinterface ser = retrofit.create(Fetchemployeedetailsinterface.class);
        Call<ResponseData> call = ser.Bind_Employee_Details_Based_On_Id(emailtoken);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                //.getMessage is POJO method to listen for final json output
                List<MessageItem> listResponse = response.body().getMessage();

                String id = String.valueOf(listResponse.get(0).getEmpid());

                Call<Fetchemptask> c = service.task_base_fileView(tid,id);
                c.enqueue(new Callback<Fetchemptask>() {
                    @Override
                    public void onResponse(Call<Fetchemptask> c, Response<Fetchemptask> response) {
                        generateEmployeeList(response.body().getMessage());
                    }

                    @Override
                    public void onFailure(Call<Fetchemptask> c, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });



               return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode==RESULT_OK) {
if (data!=null) {
    try {


        Uri selectedfile = data.getData(); //The uri with the location of the file
        String fname = getFileName(selectedfile);

        java.io.File file = FileUtils.getFile(getActivity(), selectedfile);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        File service = retrofit.create(File.class);

        Intent localIntent = getActivity().getIntent();
        String tid = localIntent.getStringExtra("tid");
        SharedPreferences settings = getActivity().getSharedPreferences("PrefEID", 0);
        String eid = settings.getString("eid", "").toString();
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getActivity().getContentResolver().getType(selectedfile)),
                        (java.io.File) file
                );
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", fname, requestFile);

        // add another part within the multipart request
        RequestBody taskid =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, tid);
        RequestBody empid =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, eid);
        RequestBody rename =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, filename);

        Files tag = this;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // finally, execute the request
        Call<ResponseBody> call = service.docUploadOnTask(body, taskid, empid, rename);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_LONG).show();
                ft.detach(tag).attach(tag).commit();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }catch (Exception e){
       Toast.makeText(getContext(),"File Type Not Supported",Toast.LENGTH_LONG).show();
    }
}
                }
        }
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        edt =  dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("File Name:");
        dialogBuilder.setMessage("Enter New File Name:");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                filename=edt.getText().toString();
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
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


    public void generateEmployeeList(ArrayList<Taskmsg> empDataList) {
        rvfilelist = getActivity().findViewById(R.id.rvfilelist);
        filelistadapter = new Filelistadapter(empDataList,getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvfilelist.setAdapter(filelistadapter);
        rvfilelist.setLayoutManager(layoutManager);
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


}
