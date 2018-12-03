package com.kitelytech.pmsapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.ApplyLeaveActivity;
import com.kitelytech.pmsapp.adapter.ProjectListAdapter;
import com.kitelytech.pmsapp.adapter.TakeLeaveAdapter;

import java.util.ArrayList;
import java.util.List;

public class TakeLeaveFragment extends Fragment {
    private Context mContext;
    private RecyclerView rv_TakeLeave;
    private TakeLeaveAdapter mLeaveListAdapetr;

    private Button btnApplyLeave, btnHoliday;
    public TakeLeaveFragment() {
        // Required empty public constructor
    }

       // TODO: Rename and change types and number of parameters
    public static TakeLeaveFragment newInstance(String param1, String param2) {
        TakeLeaveFragment fragment = new TakeLeaveFragment();
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
        View view = inflater.inflate(R.layout.fragment_take_leave, container, false);
        mContext = view.getContext();

        rv_TakeLeave = (RecyclerView)view.findViewById(R.id.rvTakeLeave);
        btnApplyLeave = (Button) view.findViewById(R.id.btnApplyLeave);
        btnHoliday = (Button) view.findViewById(R.id.btnHoliday);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_TakeLeave.setLayoutManager(layoutManager);
        rv_TakeLeave.setHasFixedSize(true);
        List<String> input = new ArrayList<>();
        for (int i=0; i<10; i++){
            input.add("Project Title:" + i);
        }
        mLeaveListAdapetr = new TakeLeaveAdapter(input);

        rv_TakeLeave.setAdapter(mLeaveListAdapetr);

        btnApplyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ApplyLeaveActivity.class);
                mContext.startActivity(intent);
            }
        });
        return view;
    }


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
