package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Fetchempproj;
import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.ResponseData;
import com.kitelytech.pmsapp.pojo.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ManagerTask {
   @FormUrlEncoded
    @POST("Bind_CurrentManager_Project")
    Call<Fetchempproj> Bind_CurrentManager_Project(
            @Field("Manager_Email") String Manager_Email
    );

    @GET("Bind_Current_Project")
    Call<Fetchempproj> Bind_Current_Project(
    );

 @FormUrlEncoded
    @POST("View_Project_Employees")
    Call<ResponseData> View_Project_Employees(
            @Field("Project_Id") String Project_Id
    );
    @FormUrlEncoded
    @POST("Bind_Task_Based_On_Project_And_Status")
    Call<Fetchemptask> Bind_Task_Based_On_Project_And_Status(
            @Field("Project_Id") String Project_Id,
            @Field("Task_Status") String Task_Status
    );
    @FormUrlEncoded
    @POST("Task_Details_and_Dev_Qa")
    Call<Result> Task_Details_and_Dev_Qa(
            @Field("Task_Name") String Task_Name,
            @Field("Task_Desc") String Task_Desc,
            @Field("Task_Created_By") String Task_Created_By,
            @Field("Project_Id") String Project_Id,
            @Field("Assign_To") String Assign_To,
            @Field("Dev_Id") String Dev_Id,
            @Field("Qa_Id") String Qa_Id,
            @Field("Dev_Hours") String Dev_Hours,
            @Field("Qa_Hours") String Qa_Hours,
            @Field("T_Level") String T_Level,
            @Field("txtChargeables_Hours") String txtChargeables_Hours);
    @FormUrlEncoded
    @POST("View_Task_Based_On_Status")
    Call<Fetchemptask> View_Task_Based_On_Status(
            @Field("Task_Status") String Task_Status
    );
}
