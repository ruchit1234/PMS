package com.kitelytech.pmsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.MainActivity;
import com.kitelytech.pmsapp.activity.TaskDetails;
import com.kitelytech.pmsapp.fragment.Taskdet;
import com.kitelytech.pmsapp.pojo.Manager;
import com.kitelytech.pmsapp.pojo.Message;
import com.kitelytech.pmsapp.pojo.Taskmsg;

import java.util.ArrayList;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;


public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    private ArrayList<Taskmsg> dataList;
    private ArrayList<Manager> managers;

    Context mContext;


    public ProjectListAdapter(ArrayList<Taskmsg> dataList,Context mContext,ArrayList<Manager> managers) {
        this.dataList = dataList;
        this.mContext=mContext;
        this.managers=managers;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.task_list_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        String fname=dataList.get(position).getEmpFirstName();
        String lname=dataList.get(position).getEmpLastName();
        String prdesc=dataList.get(position).getTaskDescription();
        String taskcrtby=dataList.get(position).getTaskCreatedBy();
        String taskid=dataList.get(position).getTaskId().toString();
        String pid=dataList.get(position).getProjectId().toString();
        prdesc = prdesc.replaceAll("\\<.*?\\>", "");
        holder.txtprjname.setText(dataList.get(position).getProjectName());
        holder.txttname.setText(dataList.get(position).getTaskName());
        holder.txttdesc.setText(prdesc);
        holder.txttaskcrt.setText(dataList.get(position).getTaskCreatedBy());
        holder.txtempname.setText(fname +" "+ lname);
        holder.itemView.setOnClickListener(v -> {
        Intent in=new Intent(mContext,TaskDetails.class);
        in.putExtra("tid",taskid);
        in.putExtra("pid",pid);
        mContext.startActivity(in);



        });

    }



    @Override
    public int getItemCount() {

        return dataList.size();

    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView txtprjname,txttname,txttdesc,txtempname,txttaskcrt;
        ImageView imgcancel;
        ProjectViewHolder(View itemView) {
            super(itemView);
            txtprjname = (TextView) itemView.findViewById(R.id.tv_projname);
            txttname = (TextView) itemView.findViewById(R.id.tv_taskname);
            txttaskcrt=itemView.findViewById(R.id.tv_taskcreatedby);
            txttdesc = (TextView) itemView.findViewById(R.id.tv_taskdescription);
            txtempname = (TextView) itemView.findViewById(R.id.tv_empname);
        }
    }

}
