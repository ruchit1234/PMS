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

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.adapter.Commentlistadapter;
import com.kitelytech.pmsapp.adapter.Tabadapter;
import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.Manager;
import com.kitelytech.pmsapp.pojo.MessageItem;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Fetchemployeedetailsinterface;
import com.kitelytech.pmsapp.rest.Fetchtaskonid;
import com.kitelytech.pmsapp.rest.File;
import com.kitelytech.pmsapp.rest.Notification;
import com.kitelytech.pmsapp.rest.Password;
import com.kitelytech.pmsapp.rest.Taskperproject;
import com.kitelytech.pmsapp.rest.UpdateEmployeeinterface;

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

import static android.content.Context.MODE_PRIVATE;


public class Settingsfragment extends Fragment{

    public Settingsfragment() {
        // Required empty public constructor
    }


    public static Settingsfragment newInstance(String param1, String param2) {
        Settingsfragment fragment = new Settingsfragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    LabeledSwitch labeledSwitch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        labeledSwitch = view.findViewById(R.id.smsnot);
checkstatus();
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
if (isOn){
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Notification service = retrofit.create(Notification.class);
    //While the app fetched data we are displaying a progress dialog
    SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
    String emailtoken = settings.getString("email", "").toString();
    Call<Result> call = service.Sms(emailtoken, "0"/*2-Turn on notification*/);
    call.enqueue(new Callback<Result>() {
        @Override
        public void onResponse(Call<Result> call, Response<Result> response) {
            String msg=response.body().getMessage().toString();
            if (msg.equals("update successfulyy!!")){
                SharedPreferences admin = getActivity().getSharedPreferences("State", MODE_PRIVATE);
                SharedPreferences.Editor edadmin = admin.edit();
                edadmin.putString("state", "on");
                edadmin.commit();
                Toast.makeText(getContext(),"Notification On",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<Result> call, Throwable t) {
            Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
        }
    });
}
else{
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Notification service = retrofit.create(Notification.class);
    //While the app fetched data we are displaying a progress dialog
    SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
    String emailtoken = settings.getString("email", "").toString();
    Call<Result> call = service.Sms(emailtoken, "1"/*1-Turn off notification*/);
    call.enqueue(new Callback<Result>() {
        @Override
        public void onResponse(Call<Result> call, Response<Result> response) {
            String msg=response.body().getMessage().toString();
            if (msg.equals("update successfulyy!!")){
                Toast.makeText(getContext(),"Notification Off",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<Result> call, Throwable t) {
            Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
        }
    });
}
            }

        });
        return view;
    }
    public void checkstatus(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Notification service = retrofit.create(Notification.class);
        //While the app fetched data we are displaying a progress dialog
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken = settings.getString("email", "").toString();
        Call<Result> call = service.Find_Task_Created_By(emailtoken);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                String msg= String.valueOf(response.body().getSmsNotify());
                if (msg.equals("1")){
                    labeledSwitch.setOn(true);
                }
                else {
                    labeledSwitch.setOn(false);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
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
