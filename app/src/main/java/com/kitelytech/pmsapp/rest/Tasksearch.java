package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Fetchemptask;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Tasksearch {
    @FormUrlEncoded
    @POST("Allocated_Search")
    Call<Fetchemptask> Allocated_Search(
            @Field("Task_Search")String Task_Search,
            @Field("Emp_Email") String Emp_Email
    );
}
