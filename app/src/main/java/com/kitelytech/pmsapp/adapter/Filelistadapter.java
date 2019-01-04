package com.kitelytech.pmsapp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kitelytech.pmsapp.BuildConfig;
import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.pojo.FileUtils;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Taskmsg;
import com.kitelytech.pmsapp.rest.ApiClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Filelistadapter extends RecyclerView.Adapter<Filelistadapter.ProjectViewHolder>{
    private String url;

    private ArrayList<Taskmsg> dataList;

    Context mContext;

    public Filelistadapter(ArrayList<Taskmsg> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext=mContext;
    }


    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.file_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        String fname=dataList.get(position).getFilename();
        String tbid=dataList.get(position).getTBD_Id();
        holder.txtfilename.setText(fname);
        Activity activity = (Activity) mContext;
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=dataList.get(position).getFilename();
                url="http://pms.cloudester.com/task_doc/"+fname;
                new DownloadFile().execute(url);
            }
        });
        //Logic to dynamically check for a file and remove open button if file not exists
        File root = new File("/storage/emulated/0/pms/");
        boolean check = new File(root,fname).exists();
        if (check == false){
            ((ViewManager)holder.open.getParent()).removeView(holder.open);
        }
        holder.open.setOnClickListener(v -> {
            File toInstall = new File("/storage/emulated/0/pms/" + fname);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(intent);
            } else {
                Uri apkUri = Uri.fromFile(toInstall);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "*/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }

        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiClient.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                com.kitelytech.pmsapp.rest.File service = retrofit.create(com.kitelytech.pmsapp.rest.File.class);
                //While the app fetched data we are displaying a progress dialog
                Call<Result> call = service.docUploadOnTaskDelete(tbid);
                call.enqueue(new Callback<Result>() {

                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        String msg=response.body().getMessage();
                        if (msg.equals("0")){
                            Toast.makeText(mContext,"File Deleted",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {

                    }
                });

            }
        });


    }



    @Override
    public int getItemCount() {

        return dataList.size();

    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView txtfilename;
        Button download,delete,open;
        ProjectViewHolder(View itemView) {
            super(itemView);
            txtfilename = (TextView) itemView.findViewById(R.id.tvfilename);
            download=itemView.findViewById(R.id.actionButton);
            delete=itemView.findViewById(R.id.btdeletefile);
            open=itemView.findViewById(R.id.btopenfile);
            SharedPreferences settings = mContext.getSharedPreferences("manager", 0);
            String man=settings.getString("manager", "").toString();
            SharedPreferences emp = mContext.getSharedPreferences("employee", 0);
            String em=emp.getString("employee","").toString();
            if (em.equals("employee")){
            View v = (View) itemView.findViewById(R.id.btdeletefile);
            ((ViewManager)v.getParent()).removeView(v);
            }
        }
    }

    public class DownloadFile extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;


        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(mContext);
                       this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        this.progressDialog.setCancelable(false);
                        this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                    int lengthOfFile = (int) connection.getContentLengthLong();
                Log.i("Length", String.valueOf(lengthOfFile));


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);



                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "pms/";
                //Create pms folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((double)(total * 100.0) / (double ) lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;
            } catch (Exception e) {

                Log.e("Error: ", e.getMessage());
            }
            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));

         }

                    @Override
                    protected void onPostExecute (String message){
                        // dismiss the dialog after the file was downloaded
                        this.progressDialog.dismiss();
                        //New Approach using fileprovider use if android version is nougat or higher
                        Toast.makeText(mContext,
                                      message, Toast.LENGTH_LONG).show();

                    }
                }
            };