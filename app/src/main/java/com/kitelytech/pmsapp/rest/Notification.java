package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Notification {
    @FormUrlEncoded
    @POST("Sms")
    Call<Result> Sms(
            @Field("mail_id") String mail_id,
            @Field("sms_notify") String sms_notify
    );
    @FormUrlEncoded
    @POST("Find_Task_Created_By")
    Call<Result> Find_Task_Created_By(
            @Field("Created_By") String Created_By
    );

}
