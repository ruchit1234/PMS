package com.kitelytech.pmsapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitelytech.pmsapp.R;

public class SimpleAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    Context mcontext;
String man;

    public SimpleAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
this.mcontext=context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        SharedPreferences settings = mcontext.getSharedPreferences("manager", 0);
         man=settings.getString("manager", "").toString();
        if (view == null) {

                view = layoutInflater.inflate(R.layout.custom_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        if (man.equals("manager")){
            switch (position) {
                case 0:
                    viewHolder.textView.setText("     Task List");
                    viewHolder.imageView.setImageResource(R.drawable.tasklist);
                    break;
                case 1:
                    viewHolder.textView.setText("     Calendar");
                    viewHolder.imageView.setImageResource(R.drawable.calendat);
                    break;
                case 2:
                    viewHolder.textView.setText("     Conversations");
                    viewHolder.imageView.setImageResource(R.drawable.chatting);
                    break;
                case 3:
                    viewHolder.textView.setText("     View Tasks");
                    viewHolder.imageView.setImageResource(R.drawable.tasklist);
                    break;
                case 4:
                    viewHolder.textView.setText("     Settings");
                    viewHolder.imageView.setImageResource(R.drawable.settings);
                    break;
                    case 5:
                    viewHolder.textView.setText("     Logout");
                    viewHolder.imageView.setImageResource(R.drawable.logout);
                    break;

            }}
            else {
            switch (position) {
                case 0:
                    viewHolder.textView.setText("     Task List");
                    viewHolder.imageView.setImageResource(R.drawable.tasklist);
                    break;
                case 1:
                    viewHolder.textView.setText("     Calendar");
                    viewHolder.imageView.setImageResource(R.drawable.calendat);
                    break;
                case 2:
                    viewHolder.textView.setText("     Conversations");
                    viewHolder.imageView.setImageResource(R.drawable.chatting);
                    break;
                case 3:
                    viewHolder.textView.setText("     About Project");
                    viewHolder.imageView.setImageResource(R.drawable.aboutus);
                    break;
                case 4:
                    viewHolder.textView.setText("     Settings");
                    viewHolder.imageView.setImageResource(R.drawable.settings);
                    break;
                case 5:
                    viewHolder.textView.setText("     Logout");
                    viewHolder.imageView.setImageResource(R.drawable.logout);
                    break;

            }
        }
        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}