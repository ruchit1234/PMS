package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.Result;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface File {
    @FormUrlEncoded
    @POST("task_base_fileView")
    Call<Fetchemptask> task_base_fileView(
            @Field("Task_Id") String Task_Id,
            @Field("Emp_Id") String Emp_Id
    );
    @Multipart
    @POST("docUploadOnTask")
    Call<ResponseBody> docUploadOnTask (@Part MultipartBody.Part file ,
                                        @Part("Task_Id")RequestBody Task_Id,
                                        @Part("Task_Assign_To") RequestBody Task_Assign_To,
                                        @Part("file_rename") RequestBody file_rename
                                  );
    @FormUrlEncoded
    @POST("docUploadOnTaskDelete")
    Call<Result> docUploadOnTaskDelete(
            @Field("fileId") String fileId
    );
}
