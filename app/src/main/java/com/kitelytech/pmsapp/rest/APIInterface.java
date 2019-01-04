package com.kitelytech.pmsapp.rest;




import com.kitelytech.pmsapp.pojo.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Khyati Kachhiya on 04,October,2018
 * Cloudester Software
 */
public interface APIInterface {
    @FormUrlEncoded
    @POST("Admin_Employee_Login")
    Call<Result> Admin_Employee_Login(
            @Field("Admin_User_Name") String email,
            @Field("Password") String password
    );
}
