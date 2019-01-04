package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Fetchempproj;
import com.kitelytech.pmsapp.pojo.ResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Employeeprojlist {
    @FormUrlEncoded
    @POST("Bind_Employee_Projects")
    Call<Fetchempproj> Bind_Employee_Projects(
            @Field("Emp_Email") String empemail
    );
}
