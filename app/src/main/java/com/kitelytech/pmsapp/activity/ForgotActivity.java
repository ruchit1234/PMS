package com.kitelytech.pmsapp.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Password;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotActivity extends AppCompatActivity {
    EditText fp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fp=findViewById(R.id.etEmailAddress);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void forgpass(View view) {
        if (fp.getText().toString().equals("")){
            Toast.makeText(ForgotActivity.this,"Please Enter A Email Address",Toast.LENGTH_LONG).show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Password service = retrofit.create(Password.class);
        //While the app fetched data we are displaying a progress dialog
        Call<Result> call = service.Forget_Password(fp.getText().toString());
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
               Toast.makeText(ForgotActivity.this,"Please Check Your Email For Further Instructions..!!!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(ForgotActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
