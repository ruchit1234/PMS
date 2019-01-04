package com.kitelytech.pmsapp.rest;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Khyati Kachhiya on 04,October,2018
 * Cloudester Software
 */
public class ApiClient {

    public static final String BASE_URL = "http://testpms.cloudester.com/api/";

    private static Retrofit retrofit = null;


    public static Retrofit getClient(final String token) {


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //getAccessToken is your own accessToken(retrieve it by saving in shared preference or any other option )
                        if(token.isEmpty()){
                            Log.d("retrofit 2","Authorization header is already present or token is empty....");
                            return chain.proceed(chain.request());
                        }
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;





    }

}
