package com.kitelytech.pmsapp.rest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Updatephoto {
    @Multipart
    @POST("imgStore_Path")
    Call<ResponseBody> imgStore_Path (@Part MultipartBody.Part file ,
                                @Part("Email_Id")RequestBody fname,
                                @Part("Old_Img") RequestBody id);

}
