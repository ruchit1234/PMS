package com.kitelytech.pmsapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;



import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;

import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.rest.APIInterface;
import com.kitelytech.pmsapp.rest.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */

/*
4/12/2018
Successfully consumed retrofit api and implement user login with valid credentials
 */
public class LoginActivity extends AppCompatActivity {




    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView tvForgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        tvForgotPass = (TextView) findViewById(R.id.tvForgotPass);
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivity(intent);
            }
        });
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();
                            }
        });

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing Up...");
            progressDialog.show();

            String memail = mEmailView.getText().toString().trim();
            String mpassword = mPasswordView.getText().toString().trim();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            APIInterface service = retrofit.create(APIInterface.class);

//Result is our pojo class
            Call<Result> call = service.Admin_Employee_Login(email, password);

            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    progressDialog.dismiss();
                    //.getMessage is POJO method to listen for final json output
                    SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("logged", "logged");
                    editor.putString("email",email);
                    editor.commit();
                    String msg=response.body().getMessage();
                    if (msg.equals("Login_Admin")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        SharedPreferences admin = getSharedPreferences("admin", MODE_PRIVATE);
                        SharedPreferences.Editor edadmin = admin.edit();
                        edadmin.putString("admin", "admin");
                        edadmin.commit();
                    }
                    else if(msg.equals("Login_Manager")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        SharedPreferences admin = getSharedPreferences("manager", MODE_PRIVATE);
                        SharedPreferences.Editor edadmin = admin.edit();
                        edadmin.putString("manager", "manager");
                        edadmin.commit();
                    }
                    else if(msg.equals("Login_Emp")){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        SharedPreferences admin = getSharedPreferences("employee", MODE_PRIVATE);
                        SharedPreferences.Editor edadmin = admin.edit();
                        edadmin.putString("employee", "employee");
                        edadmin.commit();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }




}

