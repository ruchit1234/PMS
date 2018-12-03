package com.kitelytech.pmsapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.adapter.ProjectListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ProjectListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectListFragment extends Fragment{

    private RecyclerView rvProjectList;
    private ProjectListAdapter mListAdapter;
    private FloatingActionButton mActionButton;
    private ProjectListAdapter.OnProductItemListener itemListener;
    public ProjectListFragment() {

    }

    public static ProjectListFragment newInstance(String param1, String param2) {
        ProjectListFragment fragment = new ProjectListFragment();
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
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        rvProjectList = (RecyclerView)view.findViewById(R.id.rvProjectList);
        mActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rvProjectList.setLayoutManager(layoutManager);
        rvProjectList.setHasFixedSize(true);
        List<String>  input = new ArrayList<>();
        for (int i=0; i<10; i++){
            input.add("Project Title:" + i);
        }
        mListAdapter = new ProjectListAdapter(getActivity(), input);
        rvProjectList.setAdapter(mListAdapter);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialog dialog = new FullScreenDialog();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, FullScreenDialog.TAG);
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
    public void onDetach() {
        super.onDetach();
    }

}
