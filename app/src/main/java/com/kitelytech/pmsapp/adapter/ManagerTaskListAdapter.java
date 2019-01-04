package com.kitelytech.pmsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.TaskDetails;
import com.kitelytech.pmsapp.pojo.Project;
import com.kitelytech.pmsapp.pojo.Taskmsg;

import java.util.ArrayList;


public class ManagerTaskListAdapter extends RecyclerView.Adapter<ManagerTaskListAdapter.ProjectViewHolder> {

    private ArrayList<Taskmsg> dataList;
    private ArrayList<Project> projects;

    Context mContext;


    public ManagerTaskListAdapter(ArrayList<Taskmsg> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext=mContext;
           }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.man_task_list_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        String fname=dataList.get(position).getEmpFirstName();
        String lname=dataList.get(position).getEmpLastName();
        String prdesc=dataList.get(position).getTaskDescription();
        String pid= dataList.get(position).getProjectId().toString();
        String em=dataList.get(position).getTaskCreatedBy();
        String taskid=dataList.get(position).getTaskId().toString();
        prdesc = prdesc.replaceAll("\\<.*?\\>", "");
        holder.txttname.setText(dataList.get(position).getTaskName());
        holder.txttdesc.setText(prdesc);
        holder.txttaskcrt.setText(dataList.get(position).getTaskCreatedBy());
        holder.txtempname.setText(fname +" "+ lname);
        holder.itemView.setOnClickListener(v -> {
            Intent in=new Intent(mContext,TaskDetails.class);
            in.putExtra("tid",taskid);
            in.putExtra("pid",pid);
            in.putExtra("email",em);
            mContext.startActivity(in);



        });

    }



    @Override
    public int getItemCount() {

        return dataList.size();

    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView txttname,txttdesc,txtempname,txttaskcrt;
        ProjectViewHolder(View itemView) {
            super(itemView);
            txttname = (TextView) itemView.findViewById(R.id.tv_taskname);
            txttaskcrt=itemView.findViewById(R.id.tv_taskcreatedby);
            txttdesc = (TextView) itemView.findViewById(R.id.tv_taskdescription);
            txtempname = (TextView) itemView.findViewById(R.id.tv_empname);
        }
    }

}
