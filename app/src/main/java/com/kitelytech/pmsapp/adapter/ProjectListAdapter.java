package com.kitelytech.pmsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.MainActivity;
import com.kitelytech.pmsapp.activity.TaskActivity;
import com.kitelytech.pmsapp.fragment.TaskFragment;

import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Khyati Kachhiya on 18,October,2018
 * Cloudester Software
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectListViewHolder> {
    private List<String> values;

    private Context mContext;
//    public OnProductItemListener onProductItemListener;

    public ProjectListAdapter(Context c, List<String> values) {
        this.mContext = c;
        this.values = values;
//        this.onProductItemListener = itemListener;
    }
    @NonNull
    @Override
    public ProjectListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                viewGroup.getContext());
        View v =
                inflater.inflate(R.layout.project_item_list, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ProjectListViewHolder viewHolder = new ProjectListViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListViewHolder holder, int i) {
        holder.tvProjectName.setText(values.get(i));
        holder.setClickListener(new OnProductItemListener() {
            @Override
            public void onItemClick(View view, int position) {
//                openTaskFragment(i);
                Intent intent = new Intent(mContext, TaskActivity.class);
                intent.putExtra("title", values.get(i));
                mContext.startActivity(intent);
            }
        });
    }

    private void openTaskFragment(int position){
        MainActivity mainActivity = (MainActivity) mContext;
        Fragment frag = new TaskFragment();
        mainActivity.switchContent(R.id.frame, frag, "My Tasks");
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ProjectListViewHolder extends ViewHolder implements View.OnClickListener {
        TextView tvProjectName;
        CardView cvProjectList;
        OnProductItemListener onProductItemListener;
        public ProjectListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = (TextView)itemView.findViewById(R.id.tvProjectName);
            cvProjectList = (CardView)itemView.findViewById(R.id.cvProjects);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(OnProductItemListener itemClickListener) {
            this.onProductItemListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            onProductItemListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnProductItemListener {
        void onItemClick(View view, int position);
    }
}
