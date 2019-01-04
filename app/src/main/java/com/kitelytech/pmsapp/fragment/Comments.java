package com.kitelytech.pmsapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.adapter.Commentlistadapter;
import com.kitelytech.pmsapp.adapter.Tabadapter;
import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.Manager;
import com.kitelytech.pmsapp.pojo.MessageItem;
import com.kitelytech.pmsapp.pojo.ResponseData;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Fetchemployeedetailsinterface;
import com.kitelytech.pmsapp.rest.Fetchtaskonid;
import com.kitelytech.pmsapp.rest.Taskperproject;
import com.kitelytech.pmsapp.rest.UpdateEmployeeinterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class Comments extends Fragment{
    RecyclerView tvcommlist;
    Commentlistadapter mcomadapter;
    public Comments() {
        // Required empty public constructor
    }


    public static Comments newInstance(String param1, String param2) {
        Comments fragment = new Comments();
        Bundle args = new Bundle();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit eid = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken= settings.getString("email","").toString();
        Fetchemployeedetailsinterface eidserv = eid.create(Fetchemployeedetailsinterface.class);
        Call<ResponseData> c = eidserv.Bind_Employee_Details_Based_On_Id(emailtoken);
        c.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> c, Response<ResponseData> response) {
                //.getMessage is POJO method to listen for final json output
                List<MessageItem> listResponse =response.body().getMessage();
                int id= listResponse.get(0).getEmpid();
                SharedPreferences myPrefs = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE );
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("empid", String.valueOf(id));
                editor.commit();


            }

            @Override
            public void onFailure(Call<ResponseData> c, Throwable t) {
                Toast.makeText(getActivity(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });


    }

            @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        Button comment=view.findViewById(R.id.comment);
        EditText com=view.findViewById(R.id.tvcomm);
        comment.setOnClickListener(v -> {

            String comm = com.getText().toString();
                if (comm.equals("")){
                    Toast.makeText(getContext(),"Comment Cannot be Empty",Toast.LENGTH_LONG).show();
                }
                else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
            String emailtoken = settings.getString("email", "").toString();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                   Comments tag=this;
            Fetchemployeedetailsinterface service = retrofit.create(Fetchemployeedetailsinterface.class);
            Call<ResponseData> call = service.Bind_Employee_Details_Based_On_Id(emailtoken);
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    //.getMessage is POJO method to listen for final json output
                    List<MessageItem> listResponse = response.body().getMessage();
                    int id = listResponse.get(0).getEmpid();
                    Fetchtaskonid service = retrofit.create(Fetchtaskonid.class);
                    Intent localIntent = getActivity().getIntent();
                    String tid=localIntent.getStringExtra("tid");
                    EditText etcom=view.findViewById(R.id.tvcomm);
                    Call<Result> com = service.Add_Comment_Details(tid, id, comm);
                    com.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> com, Response<Result> response) {
                            String msg = response.body().getMessage().toString();
                            if (msg.equals("Comment Added Successfully..")) {
                               Toast.makeText(getContext(), "Comment Added Successfully....", Toast.LENGTH_LONG).show();
                               ft.detach(tag).attach(tag).commit();
                                etcom.setText("");
                            }

                        }
                        @Override
                        public void onFailure(Call<Result> com, Throwable t) {
                            Toast.makeText(getContext(), "Error: Check your connection or restart the app", Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }

            });

            }

        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Fetchtaskonid service = retrofit.create(Fetchtaskonid.class);
//Result is our pojo class
        Intent localIntent = getActivity().getIntent();
        String tid=localIntent.getStringExtra("tid");
        Call<Fetchemptask> call = service.Project_Task_Comment(tid);
        call.enqueue(new Callback<Fetchemptask>() {
            @Override
            public void onResponse(Call<Fetchemptask> call, Response<Fetchemptask> response) {
                generatecommentlist(response.body().getMessage());

            }

            @Override
            public void onFailure(Call<Fetchemptask> call, Throwable t) {

            }

        });

   return view;
    }
    public void generatecommentlist(ArrayList<Taskmsg> empDataList) {
        tvcommlist = getActivity().findViewById(R.id.rvcommlist);

        mcomadapter = new Commentlistadapter(empDataList,getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        tvcommlist.setAdapter(mcomadapter);
        tvcommlist.setLayoutManager(layoutManager);
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
