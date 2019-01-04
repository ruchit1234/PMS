package com.kitelytech.pmsapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.pojo.Taskmsg;

import java.util.ArrayList;

import static android.content.Context.MODE_WORLD_READABLE;


public class Commentlistadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private ArrayList<Taskmsg> dataList;

    Context mContext;


    public Commentlistadapter(ArrayList<Taskmsg> dataList,Context mContext) {
        this.dataList = dataList;
        this.mContext=mContext;
    }
    @Override
    public int getItemCount() {

        return dataList.size();

    }

    @Override
    public int getItemViewType(int position) {
        String comid=dataList.get(position).getEmpid();
        SharedPreferences myPrefs = mContext.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String eid = myPrefs.getString("empid","");


        if (comid.equals(eid)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                SentMessageHolder holder1 = (SentMessageHolder) holder;
                String msg=dataList.get(position).getComment();
                String firstName=dataList.get(position).getEmpFirstName();
                String lastname=dataList.get(position).getEmpLastName();
                String name= firstName+" "+lastname;
                holder1.messageText.setText(msg);
                holder1.nameText.setText(name);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ReceivedMessageHolder holder2 = (ReceivedMessageHolder) holder;
                String recmsg=dataList.get(position).getComment();
                String recfname=dataList.get(position).getEmpFirstName();
                String reclname=dataList.get(position).getEmpLastName();
                String recname= recfname+" "+reclname;
                holder2.messageText.setText(recmsg);
                holder2.nameText.setText(recname);
        }
    }

    public class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.sentmsg);
            nameText = (TextView) itemView.findViewById(R.id.sentname);

        }



        }

    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.recmsg);
            nameText = (TextView) itemView.findViewById(R.id.recname);

        }


        }
    }
