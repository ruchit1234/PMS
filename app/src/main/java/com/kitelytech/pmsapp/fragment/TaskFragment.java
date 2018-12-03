package com.kitelytech.pmsapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.adapter.ProjectListAdapter;
import com.kitelytech.pmsapp.adapter.TaskListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {
    private RecyclerView rvTaskLists;
    private FloatingActionButton mActionButton;
    private TaskListAdapter mTaskListAdapter;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
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
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        rvTaskLists = (RecyclerView)view.findViewById(R.id.rvTaskLists);
//        mActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvTaskLists.getContext(),
                layoutManager.getOrientation());
        rvTaskLists.addItemDecoration(dividerItemDecoration);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTaskLists.setLayoutManager(layoutManager);
        rvTaskLists.setHasFixedSize(true);
        List<String> input = new ArrayList<>();
        for (int i=0; i<10; i++){
            input.add("Task Title:" + i);
        }
        mTaskListAdapter = new TaskListAdapter(input);
        rvTaskLists.setAdapter(mTaskListAdapter);
//        mActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FullScreenDialog dialog = new FullScreenDialog();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                dialog.show(ft, FullScreenDialog.TAG);
//            }
//        });
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
