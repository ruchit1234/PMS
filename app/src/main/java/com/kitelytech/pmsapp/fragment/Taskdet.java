package com.kitelytech.pmsapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.ApiClient;

import com.kitelytech.pmsapp.rest.Fetchtaskonid;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class Taskdet extends Fragment{

    public Taskdet() {
        // Required empty public constructor
    }


    public static Taskdet newInstance(String param1, String param2) {
        Taskdet fragment = new Taskdet();
        Bundle args = new Bundle();
        return fragment;

    }
Spinner qa,task;
    String tasktype,taskstatus,st="0";
    List<String> listSpinner = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Intent localIntent = getActivity().getIntent();
        String tid=localIntent.getStringExtra("tid");
        Toast.makeText(getContext(),tid,Toast.LENGTH_LONG).show();*/
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_taskdet, container, false);
        SharedPreferences ma = getActivity().getSharedPreferences("manager", 0);
        String man=ma.getString("manager", "").toString();
        qa=view.findViewById(R.id.spqa);
        task=view.findViewById(R.id.sptaskstatus);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SharedPreferences qapref = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String qaid = qapref.getString("qaid",null);
        Intent localIntent = getActivity().getIntent();
        String tid=localIntent.getStringExtra("tid");
        String pid=localIntent.getStringExtra("pid");
        String em=localIntent.getStringExtra("email");

       Fetchtaskonid service = retrofit.create(Fetchtaskonid.class);
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken = settings.getString("email", "").toString();

        Call<Fetchemptask> call;
        if (localIntent.hasExtra("email")){
            call = service.Allocated_Search(tid, em);
            }
        else {
            call = service.Allocated_Search(tid, emailtoken);
        }
        call.enqueue(new Callback<Fetchemptask>() {
            @Override
            public void onResponse(Call<Fetchemptask> call, Response<Fetchemptask> response) {
                List<Taskmsg> listResponse =response.body().getMessage();
                String pname=listResponse.get(0).getProjectName();
                String tname=listResponse.get(0).getTaskName();
                final String mimeType = "text/html";
                final String encoding = "UTF-8";
                String html=listResponse.get(0).getTaskDescription();
                String devesthours=listResponse.get(0).getDevEstimatedHours();
                String qaesthours=listResponse.get(0).getQAEstimatedHours();
                String taskcrttime=listResponse.get(0).getTaskCreatedTime();
                tasktype= String.valueOf(listResponse.get(0).getTaskType());
                String taskastime=listResponse.get(0).getTaskAssignTime();
                taskstatus= String.valueOf(listResponse.get(0).getTaskStatus());
                String tasklvl=listResponse.get(0).getTaskLevel().toString();
                String totalhrs=listResponse.get(0).getTotalHours();
                String worktothrs=listResponse.get(0).getWorkingTotalHours();
                String srtdttym= String.valueOf(listResponse.get(0).getStartDateTime());
                String fname=listResponse.get(0).getEmpFirstName();
                String lname=listResponse.get(0).getEmpLastName();
                String email=listResponse.get(0).getTaskCreatedBy();
                TextView t1,t2,t4,t5,t6,t7,t8,t9,t12,t13,t14,t15,t16,t17;
//region Spinner task Status
                //Method To populate task status into spinner

 //endregion
                WebView t3;
                t1=view.findViewById(R.id.tvprnm);
                t1.setText(pname);
                t2=view.findViewById(R.id.tvtasknm);
                t2.setText(tname);
                t3=view.findViewById(R.id.wbdesc);
                t3.loadDataWithBaseURL("", html, mimeType, encoding, "");
                t4=view.findViewById(R.id.tvesthrs);
                t4.setText(devesthours);
                t5=view.findViewById(R.id.tvqaesthrs);
                t5.setText(qaesthours);
                t9=view.findViewById(R.id.tvtaskstatus);
                if (taskstatus.equals("0")) {
                    t9.setText("New");
                }
                else if (taskstatus.equals("1")) {
                    t9.setText("In Progress");
                }
                else if (taskstatus.equals("2")) {
                    t9.setText("In Review");
                }
                else if (taskstatus.equals("3")) {
                    t9.setText("Pending");
                }
                else if (taskstatus.equals("4")) {
                    t9.setText("Close");
                }
                else if (taskstatus.equals("5")) {
                    t9.setText("Paused");
                }

                t8=view.findViewById(R.id.tvtasklvl);
                if (tasklvl.equals("-1")) {
                    t8.setText("NP");
                }
               else if (tasklvl.equals("0")) {
                    t8.setText("Urgent");
                }
               else if (tasklvl.equals("1")) {
                    t8.setText("Highest");
                }
                else if (tasklvl.equals("2")) {
                    t8.setText("High");
                }
                else if (tasklvl.equals("3")) {
                    t8.setText("Normal");
                }
                else if (tasklvl.equals("4")) {
                    t8.setText("Low");
                }
                else if (tasklvl.equals("5")) {
                    t8.setText("Lowest");
                }
                t15=view.findViewById(R.id.tvemfname);
                t15.setText(fname+" "+lname);
                t17=view.findViewById(R.id.tvtaskcrtby);
                t17.setText(email);

                           }

            @Override
            public void onFailure(Call<Fetchemptask> call, Throwable t) {


            }
        });
        if (man.equals("manager")){
            ((ViewManager)qa.getParent()).removeView(qa);
            ((ViewManager)task.getParent()).removeView(task );
        }
        else {
            fetchqa();
            status();
        }
        return view;
    }
    public void status(){
        listSpinner.add("Select A Action");
        listSpinner.add("Start");
        listSpinner.add("Pause");
        listSpinner.add("Finish");
        final ArrayAdapter a = new ArrayAdapter(getContext(),R.layout.spinner_item,listSpinner);
        a.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        task.setAdapter(a);
        task.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spitem=task.getSelectedItem().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiClient.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Fetchtaskonid service = retrofit.create(Fetchtaskonid.class);
                Intent localIntent = getActivity().getIntent();
                Taskdet tag=Taskdet.this;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                String tid=localIntent.getStringExtra("tid");
                SharedPreferences myPrefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                String eid = myPrefs.getString("empid","");
                if(spitem.contains("Start")) {
                    Call<Taskmsg> call = service.Insert_Task_Start_Finish_Time(tid, eid,"1", tasktype, "0");
                    call.enqueue(new Callback<Taskmsg>() {
                        @Override
                        public void onResponse(Call<Taskmsg> call, Response<Taskmsg> response) {
                            // List<Taskmsg> listResponse =response.body().getMessage();
                            String msg = response.body().getMessage();
                            if (msg.contains("-1")) {
                                Toast.makeText(getContext(), "Can not start this task, as another task is in progress", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Task Started", Toast.LENGTH_LONG).show();
                                ft.detach(tag).attach(tag).commit();
                            }

                        }

                        @Override
                        public void onFailure(Call<Taskmsg> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(spitem.contains("Pause")) {
                    Call<Taskmsg> call = service.Insert_Task_Start_Finish_Time(tid, eid,"5", tasktype, "1");
                    call.enqueue(new Callback<Taskmsg>() {
                        @Override
                        public void onResponse(Call<Taskmsg> call, Response<Taskmsg> response) {
                            // List<Taskmsg> listResponse =response.body().getMessage();
                            ft.detach(tag).attach(tag).commit();
                            Toast.makeText(getContext(), "Task Paused", Toast.LENGTH_LONG).show();


                        }

                        @Override
                        public void onFailure(Call<Taskmsg> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                else if(spitem.equals("Move To QA")) {
                    SharedPreferences qapref = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
                    String qaid = qapref.getString("qaid",null);
                    Call<Taskmsg> call = service.Movetoqa(tid, eid, qaid, "2", "2");
                    call.enqueue(new Callback<Taskmsg>() {
                        @Override
                        public void onResponse(Call<Taskmsg> call, Response<Taskmsg> response) {
                            // List<Taskmsg> listResponse =response.body().getMessage();
                            ft.detach(tag).attach(tag).commit();
                            Toast.makeText(getContext(), "Task Moved To QA", Toast.LENGTH_LONG).show();


                        }

                        @Override
                        public void onFailure(Call<Taskmsg> call, Throwable t) {

                            ft.detach(tag).attach(tag).commit();
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });}




                else if(spitem.contains("Finish"))
                {
                    Call<Taskmsg> call = service.Finishtask(tid, eid,"", "3");
                    call.enqueue(new Callback<Taskmsg>() {
                        @Override
                        public void onResponse(Call<Taskmsg> call, Response<Taskmsg> response) {
                            // List<Taskmsg> listResponse =response.body().getMessage();
                            String msg = response.body().getMessage();
                            if (msg.contains("-1")) {
                                Toast.makeText(getContext(), "You Cannot Finish This Task", Toast.LENGTH_LONG).show();
                            } else {
                                ft.detach(tag).attach(tag).commit();
                                Toast.makeText(getContext(), "Task Finished", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Taskmsg> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
public void fetchqa(){
    SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
    String emailtoken = settings.getString("email", "").toString();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Intent localIntent = getActivity().getIntent();
    String tid=localIntent.getStringExtra("tid");
    String pid=localIntent.getStringExtra("pid");
    String em=localIntent.getStringExtra("email");
    Fetchtaskonid service = retrofit.create(Fetchtaskonid.class);
    Call<Fetchemptask> call1 = service.Bind_Task_QA(tid,pid);
    call1.enqueue(new Callback<Fetchemptask>() {
        @Override
        public void onResponse(Call<Fetchemptask> call1, Response<Fetchemptask> response) {
            List<Taskmsg> listResponse =response.body().getMessage();
            List<String> listqa = new ArrayList<String>();
            listqa.add("Select A QA");
            for (int i = 0; i < listResponse.size(); i++){
                String fname=listResponse.get(i).getEmpFirstName();
                String lname=listResponse.get(i).getEmpLastName();
                String id=listResponse.get(i).getEmpid();
                String fn=id+"."+fname+" "+lname;
                listqa.add(fn);
            }

            final ArrayAdapter a = new ArrayAdapter(getContext(),R.layout.spinner_item,listqa);
            a.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            qa.setAdapter(a);
            qa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item=qa.getSelectedItem().toString();
                    String a=item.replaceAll("([a-z])", "");
                    String b=a.replaceAll("([A-Z])", "");
                    String qaid=b.replaceAll("([.])", "");
                    if (item.equals("Select A QA")){}
                   else {
                        listSpinner.add("Move To QA");
                        SharedPreferences settings = getActivity().getSharedPreferences("qapref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("qaid", qaid);
                        editor.commit();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        @Override
        public void onFailure(Call<Fetchemptask> call1, Throwable t) {

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


}
