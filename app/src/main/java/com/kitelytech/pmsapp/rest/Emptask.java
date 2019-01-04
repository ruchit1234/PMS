package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Fetchemptask;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Emptask {
    @FormUrlEncoded
    @POST("Bind_All_Task")
    Call<Fetchemptask> Bind_All_Task(
            @Field("Emp_Email") String empemail
    );
    @Multipart
    @POST("Task_Add_By_Employee")
    Call<ResponseBody> Task_Add_By_Employee (@Part MultipartBody.Part file ,
                                             @Part("file_name") RequestBody file_name,
                                             @Part("Task_Name") RequestBody Task_Name,
                                             @Part("Task_Desc") RequestBody Task_Desc,
                                             @Part("Task_Created_By") RequestBody Task_Created_By,
                                             @Part("Project_Id") RequestBody Project_Id,
                                             @Part("Assign_To") RequestBody Assign_To,
                                             @Part("Task_Type") RequestBody Task_Type,
                                             @Part("Estimated_Hours") RequestBody Estimated_Hours,
                                             @Part("T_Level") RequestBody T_Level
    );

    @Multipart
    @POST("Task_Add_By_Employee")
    Call<ResponseBody> Task_Add_By_Employee_Without_File (@Part("Task_Name") RequestBody Task_Name,
                                             @Part("Task_Desc") RequestBody Task_Desc,
                                             @Part("Task_Created_By") RequestBody Task_Created_By,
                                             @Part("Project_Id") RequestBody Project_Id,
                                             @Part("Assign_To") RequestBody Assign_To,
                                             @Part("Task_Type") RequestBody Task_Type,
                                             @Part("Estimated_Hours") RequestBody Estimated_Hours,
                                             @Part("T_Level") RequestBody T_Level
    );

}
