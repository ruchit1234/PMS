package com.kitelytech.pmsapp.fragment;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import com.kitelytech.pmsapp.adapter.ProjectListAdapter;
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
import com.kitelytech.pmsapp.rest.Tasksearch;
import com.kitelytech.pmsapp.rest.UpdateEmployeeinterface;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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


public class Search_fragment extends Fragment{

    public Search_fragment() {
        // Required empty public constructor
    }


    public static Search_fragment newInstance(String param1, String param2) {
        Search_fragment fragment = new Search_fragment();
        Bundle args = new Bundle();
        return fragment;
    }
    private List<String> lastSearches;
    private MaterialSearchBar searchBar;
    RecyclerView rvlist;
    ProjectListAdapter mListAdapter;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String txt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchBar = (MaterialSearchBar) view.findViewById(R.id.searchBar);
        searchBar.setSpeechMode(true);
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                String s = enabled ? "enabled" : "disabled";
                Toast.makeText(getContext(), "Search " + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
              txt = text.toString();
              search();
              }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode){

                    case MaterialSearchBar.BUTTON_SPEECH:
                        openVoiceRecognizer();
                break;
                }
            }
        });


        return view;
    }
    private void openVoiceRecognizer(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt = (result.get(0));
                    search();
                }
                break;
            }

        }
    }

    public void search(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Tasksearch service = retrofit.create(Tasksearch.class);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
//Result is our pojo class
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        String emailtoken = settings.getString("email", "").toString();
        Call<Fetchemptask> call = service.Allocated_Search(txt,emailtoken);
        call.enqueue(new Callback<Fetchemptask>() {
            @Override
            public void onResponse(Call<Fetchemptask> call, Response<Fetchemptask> response) {
                generateEmployeeList(response.body().getMessage(), response.body().getManagers());
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<Fetchemptask> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void generateEmployeeList(ArrayList<Taskmsg> empDataList, ArrayList<Manager>taskmanagers) {
        rvlist = getActivity().findViewById(R.id.rvlist);

        mListAdapter = new ProjectListAdapter(empDataList,getActivity(),taskmanagers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvlist.setAdapter(mListAdapter);
        rvlist.setLayoutManager(layoutManager);
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
