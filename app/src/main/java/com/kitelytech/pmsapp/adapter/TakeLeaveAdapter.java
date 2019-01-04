package com.kitelytech.pmsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kitelytech.pmsapp.R;

import java.util.List;

/**
 * Created by Khyati Kachhiya on 24,October,2018
 * Cloudester Software
 */
public class TakeLeaveAdapter extends RecyclerView.Adapter<TakeLeaveAdapter.TakeLeaveViewHolder> {
    private List<String> values;

    public TakeLeaveAdapter(List<String> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public TakeLeaveViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(
                viewGroup.getContext());
        View v =
                inflater.inflate(R.layout.take_leave_item_list, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        TakeLeaveAdapter.TakeLeaveViewHolder viewHolder = new TakeLeaveViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TakeLeaveAdapter.TakeLeaveViewHolder takeLeaveViewHolder, int i) {


    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class TakeLeaveViewHolder extends RecyclerView.ViewHolder {

        public TakeLeaveViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
