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
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Fetchemployeedetailsinterface;
import com.kitelytech.pmsapp.rest.Fetchtaskonid;
import com.kitelytech.pmsapp.rest.Password;
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


public class ChangePasswordFragment extends Fragment{

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        EditText op,newp,cp;
        Button sb;
        op=view.findViewById(R.id.etEnterOldPass);
    newp=view.findViewById(R.id.etEnterNewPass);
    cp=view.findViewById(R.id.etEnterNewConfirmPass);
    sb=view.findViewById(R.id.passsub);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

sb.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (newp.getText().toString().equals( cp.getText().toString())) {

            //Creating an object of our api interface
            Password service = retrofit.create(Password.class);
            //While the app fetched data we are displaying a progress dialog
            SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
            String emailtoken = settings.getString("email", "").toString();
            Call<Result> call = service.Change_Password(emailtoken, op.getText().toString(), newp.getText().toString());
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    String msg = response.body().getMessage().toString();
                    if (msg.equals("Password Changed Successfully..!!")) {
                        Toast.makeText(getContext(), "Password Changed Successfully!!", Toast.LENGTH_LONG).show();
                        op.getText().clear();
                        newp.getText().clear();
                        cp.getText().clear();
                    } else if (msg.equals("Invalid password..!!try again")) {
                        Toast.makeText(getContext(), "Error Invalid Old Password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    else {
            Toast.makeText(getContext(),"Passwords Don't Match",Toast.LENGTH_LONG).show();
        }
    }
}
);
return view;
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
