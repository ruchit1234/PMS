package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Password {

    @FormUrlEncoded
    @POST("Change_Password")
    Call<Result> Change_Password(
            @Field("Email_Id") String Email_Id,
            @Field("Old_Password") String Old_Password,
            @Field("New_Password") String New_Password
            );
    @FormUrlEncoded
    @POST("Forget_Password")
    Call<Result> Forget_Password(
            @Field("Email_Id") String Email_Id
    );
}
