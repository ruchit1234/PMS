package com.kitelytech.pmsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitelytech.pmsapp.R;

import java.util.List;

/**
 * Created by Khyati Kachhiya on 25,October,2018
 * Cloudester Software
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {
    private List<String> values;

    public TaskListAdapter(List<String> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(
                viewGroup.getContext());
        View v =
                inflater.inflate(R.layout.task_list_item, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        TaskListViewHolder viewHolder = new TaskListViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int i) {
        holder.tvTaskName.setText(values.get(i));
    }

    @Override
    public int getItemCount() {
        return this.values.size();
    }

    public class TaskListViewHolder extends RecyclerView.ViewHolder{

        TextView tvTaskName;
        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = (TextView)itemView.findViewById(R.id.tvTaskName);
        }
    }
}
