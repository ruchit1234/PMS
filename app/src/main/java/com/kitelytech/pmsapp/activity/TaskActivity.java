package com.kitelytech.pmsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.adapter.TaskListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private RecyclerView rvTaskLists;
    private TaskListAdapter mTaskListAdapter;
    private String titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Intent intent = getIntent();
        titleName = intent.getStringExtra("title");
        rvTaskLists = (RecyclerView)findViewById(R.id.rvTaskLists);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(titleName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
