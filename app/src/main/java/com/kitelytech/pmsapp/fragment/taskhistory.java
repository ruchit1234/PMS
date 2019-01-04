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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.MainActivity;
import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.MessageItem;
import com.kitelytech.pmsapp.pojo.ResponseData;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.APIInterface;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Fetchemployeedetailsinterface;
import com.kitelytech.pmsapp.rest.Fetchtaskonid;
import com.kitelytech.pmsapp.rest.UpdateEmployeeinterface;
import com.kitelytech.pmsapp.rest.Updatephoto;
import com.kitelytech.pmsapp.rest.UpimgClient;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickClick;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class taskhistory extends Fragment{

    public taskhistory() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_taskhistory, container, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SharedPreferences qapref = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String qaid = qapref.getString("qaid",null);
        try {
            if (Integer.parseInt(qaid)>0){
                SharedPreferences set = getActivity().getSharedPreferences("qapref", 0);
                SharedPreferences.Editor ed = set.edit();
                ed.remove("qaid");
                ed.commit();

            }
        }
        catch (NumberFormatException nfe){
            nfe.printStackTrace();
        }
        Fetchtaskonid service = retrofit.create(Fetchtaskonid.class);
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken = settings.getString("email", "").toString();
        Intent localIntent = getActivity().getIntent();
        String tid=localIntent.getStringExtra("tid");
        String pid=localIntent.getStringExtra("pid");
        String em=localIntent.getStringExtra("email");
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
                String taskcrttime=listResponse.get(0).getTaskCreatedTime();
                String taskastime=listResponse.get(0).getTaskAssignTime();
                String totalhrs=listResponse.get(0).getTotalHours();
                String worktothrs=listResponse.get(0).getWorkingTotalHours();
                String srtdttym= String.valueOf(listResponse.get(0).getStartDateTime());

                TextView t1,t2,t3,t4,t5;
                t1=view.findViewById(R.id.tvtskcrttimehi);
                t2=view.findViewById(R.id.tvtaskasstymhi);
                t3=view.findViewById(R.id.tvtothourshi);
                t4=view.findViewById(R.id.tvtotworkhourshi);
                t5=view.findViewById(R.id.tvstartdttymhi);
                t1.setText(taskcrttime);
                t2.setText(taskastime);
                t3.setText(totalhrs);
                t4.setText(worktothrs);
                t5.setText(srtdttym);

            }

            @Override
            public void onFailure(Call<Fetchemptask> call, Throwable t) {


            }
        });
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
