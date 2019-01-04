package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Fetchemptask;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Taskperproject {
    @FormUrlEncoded
    @POST("Bind_Task_Based_On_Assign_To")
    Call<Fetchemptask> Bind_Task_Based_On_Assign_To(
            @Field("Project_Id")String proid,
            @Field("Emp_Email") String empemail
    );
}
