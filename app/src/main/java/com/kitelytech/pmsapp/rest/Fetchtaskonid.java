package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Fetchemptask;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.pojo.Taskmsg;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Fetchtaskonid {
    @FormUrlEncoded
    @POST("Allocated_Search")
    Call<Fetchemptask> Allocated_Search(
            @Field("Task_Search") String eid,
            @Field("Emp_Email") String empemail

    );

    @FormUrlEncoded
    @POST("Add_Comment_Details")
    Call<Result> Add_Comment_Details(
            @Field("Task_Id") String Task_Id,
            @Field("Emp_Id") int Emp_Id,
            @Field("Comment") String comment


    );

    @FormUrlEncoded
    @POST("Project_Task_Comment")
    Call<Fetchemptask> Project_Task_Comment(
            @Field("Task_Id") String Task_Id

    );

    @FormUrlEncoded
    @POST("Insert_Task_Start_Finish_Time")
    Call<Taskmsg> Insert_Task_Start_Finish_Time(
            @Field("Task_Id") String Task_Id,
            @Field("Emp_Id") String Emp_Id,
            @Field("Status") String Status,
            @Field("Task_Type") String Task_Type,
            @Field("Btn_status") String Btn_status
    );
    @FormUrlEncoded
    @POST("Insert_Task_Start_Finish_Time")
    Call<Taskmsg> Movetoqa(
            @Field("Task_Id") String Task_Id,
            @Field("Emp_Id") String Emp_Id,
            @Field("Assign_To") String Assign_To,
            @Field("Status") String Status,
            @Field("Btn_status") String Btn_status
    );
    @FormUrlEncoded
    @POST("Insert_Task_Start_Finish_Time")
    Call<Taskmsg> Finishtask(
            @Field("Task_Id") String Task_Id,
            @Field("Emp_Id") String Emp_Id,
            @Field("Status") String Status,
            @Field("Btn_status") String Btn_status
    );
    @FormUrlEncoded
    @POST("Bind_Task_QA")
    Call<Fetchemptask> Bind_Task_QA(
            @Field("Task_Id") String Task_Id,
            @Field("Project_Id") String Project_Id
    );


}
