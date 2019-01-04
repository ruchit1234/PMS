package com.kitelytech.pmsapp.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.adapter.Tabadapter;
import com.kitelytech.pmsapp.fragment.Comments;
import com.kitelytech.pmsapp.fragment.Files;
import com.kitelytech.pmsapp.fragment.Taskdet;
import com.kitelytech.pmsapp.fragment.taskhistory;

public class TaskDetails extends AppCompatActivity {
    private Tabadapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new Tabadapter(getSupportFragmentManager());
        adapter.addFragment(new Taskdet(), "Details");
        adapter.addFragment(new Comments(), "Comments");
        adapter.addFragment(new Files(), "Files");
        adapter.addFragment(new taskhistory(), "History");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Intent localIntent = getIntent();
        String taskid=localIntent.getStringExtra("tid");
        Intent bundle = new Intent();
        bundle.putExtra("tid",taskid);


//APi calling to fetch task based on ID

    }
}
