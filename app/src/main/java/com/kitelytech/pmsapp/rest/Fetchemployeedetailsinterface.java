package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.ResponseData;
import com.kitelytech.pmsapp.pojo.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface Fetchemployeedetailsinterface {
    @FormUrlEncoded
    @POST("Bind_Employee_Details_Based_On_Id")
    Call<ResponseData> Bind_Employee_Details_Based_On_Id(
            @Field("Emp_Email") String empemail
    );


}
