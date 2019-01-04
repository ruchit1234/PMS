package com.kitelytech.pmsapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.adapter.ManagerTaskListAdapter;
import com.kitelytech.pmsapp.pojo.Fetchempproj;
import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.Message;
import com.kitelytech.pmsapp.pojo.Project;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.ManagerTask;
import com.kitelytech.pmsapp.rest.Password;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class ManagerTaskdet extends Fragment{
    String tsklvl=null,pid=null;
    private RecyclerView rvProjectList;
    ManagerTaskListAdapter mListAdapter;
    Spinner spinner,manproj;

    public ManagerTaskdet() {
        // Required empty public constructor
    }


    public static ManagerTaskdet newInstance(String param1, String param2) {
        ManagerTaskdet fragment = new ManagerTaskdet();
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
        View view = inflater.inflate(R.layout.fragment_manager_taskdet, container, false);
        spinner=view.findViewById(R.id.mansp);
        rvProjectList =view.findViewById(R.id.manrecvw);
        manproj = view.findViewById(R.id.manprj);
        fetchproj();
        return view;
    }
    public void fetchproj(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creating an object of our api interface
        ManagerTask service = retrofit.create(ManagerTask.class);
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Fetching Data", "Please wait...", false, false);
        Call<Fetchempproj> call = service.Bind_Current_Project();
        call.enqueue(new Callback<Fetchempproj>() {
            @Override
            public void onResponse(Call<Fetchempproj> call, Response<Fetchempproj> response) {
                List<Message> listResponse = response.body().getMessage();
                List<String> listSpinner = new ArrayList<String>();
                listSpinner.add("All");
                for (int i = 0; i < listResponse.size(); i++) {
                    String id = listResponse.get(i).getProjectId().toString();
                    String name = listResponse.get(i).getProjectName();
                    String fn = id + "." + name;
                    listSpinner.add(fn);
                }

                final ArrayAdapter a = new ArrayAdapter(getActivity(), R.layout.spinner_item_black, listSpinner);
                a.setDropDownViewResource(R.layout.spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                manproj.setAdapter(a);
                loading.dismiss();

            }

                @Override
                public void onFailure (Call < Fetchempproj > call, Throwable t){

                }


        });
        manproj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=manproj.getSelectedItem().toString();
                String a=item.replaceAll("([a-z])", "");
                String b=a.replaceAll("([A-Z])", "");
                String prid=b.replaceAll("([.])", "");
                pid = prid;
                if (item.contains("All")){

                }
                else{
                    status();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

            public void fetchtask(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ManagerTask service = retrofit.create(ManagerTask.class);

//Result is our pojo class
        SharedPreferences settings = getActivity().getSharedPreferences("tskst", 0);
        String status= settings.getString("st", "").toString();
        Call<Fetchemptask> call = service.Bind_Task_Based_On_Project_And_Status(pid,status);
        call.enqueue(new Callback<Fetchemptask>() {
            @Override
            public void onResponse(Call<Fetchemptask> call, Response<Fetchemptask> response) {

                   generateEmployeeList(response.body().getMessage());

               }

            @Override
            public void onFailure(Call<Fetchemptask> call, Throwable t) {
                Toast.makeText(getContext(),"No Task Found For Specified Criteria",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void generateEmployeeList(ArrayList<Taskmsg> empDataList) {

        mListAdapter = new ManagerTaskListAdapter(empDataList, getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvProjectList.setAdapter(mListAdapter);
        rvProjectList.setLayoutManager(layoutManager);
    }


    private void status(){
//Creating a rest adapter
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.add("Select Status");
        listSpinner.add("New");
        listSpinner.add("In Progress");
        listSpinner.add("In Review");
        listSpinner.add("Pending");
        listSpinner.add("Close");
        listSpinner.add("Paused");
        final ArrayAdapter a = new ArrayAdapter(getContext(),R.layout.spinner_item_black,listSpinner);
        a.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(a);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String item=spinner.getSelectedItem().toString();
                if (item.contains("Select Status")){
                    SharedPreferences bid = getActivity().getSharedPreferences("tskst", 0);
                    SharedPreferences.Editor editor = bid.edit();
                    editor.remove("st");
                    editor.commit();
                }
                if (item.equals("New"))
                {
                    tsklvl="0";
                    SharedPreferences settings = getActivity().getSharedPreferences("tskst", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("st", tsklvl);
                    editor.commit();
                    fetchtask();
                }
                else if (item.equals("In Progress"))
                {
                    tsklvl="1";
                    SharedPreferences settings = getActivity().getSharedPreferences("tskst", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("st", tsklvl);
                    editor.commit();
                    fetchtask();

                }
                else if (item.equals("In Review"))
                {
                    tsklvl="2";
                    SharedPreferences settings = getActivity().getSharedPreferences("tskst", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("st", tsklvl);
                    editor.commit();
                    fetchtask();

                }
                else if (item.equals("Pending"))
                {
                    tsklvl="3";
                    SharedPreferences settings = getActivity().getSharedPreferences("tskst", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("st", tsklvl);
                    editor.commit();
                    fetchtask();

                }
                else if (item.equals("Close"))
                {
                    tsklvl="4";
                    SharedPreferences settings =getActivity().getSharedPreferences("tskst", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("st", tsklvl);
                    editor.commit();
                    fetchtask();

                }
                else if (item.equals("Paused"))
                {
                    tsklvl="5";
                    SharedPreferences settings = getActivity().getSharedPreferences("tskst", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("st", tsklvl);
                    editor.commit();
                    fetchtask();

                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

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
