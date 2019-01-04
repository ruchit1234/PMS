package com.kitelytech.pmsapp.rest;

import com.kitelytech.pmsapp.pojo.Result;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Khyati Kachhiya on 04,October,2018
 * Cloudester Software
 */
public interface UpdateEmployeeinterface {
    @FormUrlEncoded
    @POST("Update_Emp_Profile_Details")
    Call<Result> Update_Emp_Profile_Details(
            @Field("Emp_First_Name") String fname,
            @Field("Emp_Last_Name") String lname,
            @Field("Emp_Address") String empadd,
            @Field("Emp_Id") int empid

    );

}
