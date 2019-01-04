package com.kitelytech.pmsapp.fragment;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.MainActivity;
import com.kitelytech.pmsapp.adapter.ProjectListAdapter;
import com.kitelytech.pmsapp.pojo.Fetchempproj;
import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.Manager;
import com.kitelytech.pmsapp.pojo.Message;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Employeeprojlist;
import com.kitelytech.pmsapp.rest.Emptask;
import com.kitelytech.pmsapp.rest.Taskperproject;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ProjectListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectListFragment extends Fragment{

    private RecyclerView rvProjectList;
    ProjectListAdapter mListAdapter;
    private boolean shouldRefreshOnResume = false;
    FloatingActionButton fab;
    View fabBGLayout;
    boolean isFABOpen=false;

    public static ProjectListFragment newInstance() {
        ProjectListFragment fragment = new ProjectListFragment();
        return fragment;
    }
    public ProjectListFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(shouldRefreshOnResume){
            // refresh fragment
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shouldRefreshOnResume){
            // refresh fragment
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        fab= view.findViewById(R.id.addtask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = getActivity().getSharedPreferences("manager", 0);
                String man=settings.getString("manager", "").toString();
                SharedPreferences emp = getActivity().getSharedPreferences("employee", 0);
                String em=emp.getString("employee","").toString();
                if (man.equals("manager")){
                    FullScreenDialog mProfileFragment = new FullScreenDialog();
                    Fragment fragment = mProfileFragment;
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
//                fragmentTransaction.replace(R.id.frame, fragment, "Profile");
//                fragmentTransaction.commitAllowingStateLoss();
//                setTitle("Profile");
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.id_main_frame, fragment).commit();
                }
            else if(em.equals("employee")) {
                    EmployeeTask mProfileFragment = new EmployeeTask();
                    Fragment fragment = mProfileFragment;
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
//                fragmentTransaction.replace(R.id.frame, fragment, "Profile");
//                fragmentTransaction.commitAllowingStateLoss();
//                setTitle("Profile");
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.id_main_frame, fragment).commit();

                }

}
        });



        SharedPreferences id = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String prid= id.getString("id", "");
//Api Call to fetch All Tasks:
        if (prid.equals("")) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Emptask service = retrofit.create(Emptask.class);
    final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();
//Result is our pojo class
    SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
    String emailtoken = settings.getString("email", "").toString();
    Call<Fetchemptask> call = service.Bind_All_Task(emailtoken);
    call.enqueue(new Callback<Fetchemptask>() {
        @Override
        public void onResponse(Call<Fetchemptask> call, Response<Fetchemptask> response) {
            generateEmployeeList(response.body().getMessage(), response.body().getManagers());
        progressDialog.dismiss();
        }
        @Override
        public void onFailure(Call<Fetchemptask> call, Throwable t) {
        }
    });
}
else{
//Api Calling To fetch task according to the project ID:

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Taskperproject service = retrofit.create(Taskperproject.class);
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();
//Result is our pojo class
            SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
            String emailtoken = settings.getString("email", "").toString();
            Call<Fetchemptask> call = service.Bind_Task_Based_On_Assign_To(prid,emailtoken);
            call.enqueue(new Callback<Fetchemptask>() {
                @Override
                public void onResponse(Call<Fetchemptask> call, Response<Fetchemptask> response) {
                    progressDialog.dismiss();
                    generateEmployeeList(response.body().getMessage(), response.body().getManagers());
                }

                @Override
                public void onFailure(Call<Fetchemptask> call, Throwable t) {

                }
            });
            }/*Method to generate List of employees project using RecyclerView with custom adapter*/
        return view;
    }
    public void generateEmployeeList(ArrayList<Taskmsg> empDataList, ArrayList<Manager>taskmanagers) {
        rvProjectList = getActivity().findViewById(R.id.rvProjectList);

        mListAdapter = new ProjectListAdapter(empDataList,getActivity(),taskmanagers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvProjectList.setAdapter(mListAdapter);
        rvProjectList.setLayoutManager(layoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
