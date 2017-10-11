package com.NamohTours.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.NamohTours.Service.ValidationToolBox;
import com.google.firebase.iid.FirebaseInstanceId;
import com.NamohTours.Model.ForgetPasswordResponse;
import com.NamohTours.Model.TokenResponse;
import com.NamohTours.Model.UserLoginRegisterDetailResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.AUTHORIZATION;
import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.TOKEN_EXPIRY;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

public class ForgetPassword extends AppCompatActivity {


    private static final String TAG = ForgetPassword.class.getSimpleName();

    private SharedPreferences.Editor editor;
    private SharedPreferences RegisterPrefences;

    private SharedPreferences prefs;

    private String restoretoken;

    ConnectionDetector cd;

    private String email, regID;


    private Button btnSubmit;
    private EditText inputEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        inputEmail = (EditText) findViewById(R.id.input_Forgottenemail);
        btnSubmit = (Button) findViewById(R.id.btnForgottenSubmit);
        progressBar = (ProgressBar) findViewById(R.id.ForgotProgress);

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);

        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        restoretoken = prefs.getString(TOKEN_KEY, null);

        //Creating Api Interface
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        // if token is null
        if (cd.isConnectingToInternet(getApplicationContext())) {
            if (restoretoken == null) {
                if (TextUtils.isEmpty(restoretoken)) {
                    Call<TokenResponse> call = apiService.getToken(AUTHORIZATION);

                    call.enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

                            TokenResponse tokenResponse = response.body();
                            String token;
                            if ((token = tokenResponse.getToken()) != null) {

                                editor.putString(TOKEN_KEY, token);
                                editor.putString(TOKEN_EXPIRY, tokenResponse.getExpires());

                                editor.commit();
                                restoretoken = token;


                            }


                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {

                            Toast.makeText(getApplicationContext(), "Please Try Later...", Toast.LENGTH_LONG).show();


                        }
                    });
                }
            }
        } else {
            Snackbar.make(btnSubmit, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = inputEmail.getText().toString();

                regID = FirebaseInstanceId.getInstance().getToken();
                //city = inputCity.getText().toString();


                if (cd.isConnectingToInternet(getApplicationContext())) {


                    // if Text Fileds Empty
                    if (TextUtils.isEmpty(email)) {
                        Snackbar.make(btnSubmit, "Enter Email Id", Snackbar.LENGTH_LONG).show();
                    } else {

                        boolean isValidEmailId = ValidationToolBox.validateEmailId(email);

                        if (isValidEmailId) {

                            inputEmail.setEnabled(false);

                            progressBar.setVisibility(View.VISIBLE);


                            // if token is not null
                            if (restoretoken != null) {


                                Call<ForgetPasswordResponse> call = apiService.forgottenPassword(restoretoken, new ForgetPasswordResponse(email));


                                call.enqueue(new Callback<ForgetPasswordResponse>() {
                                    @Override
                                    public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {

                                        ForgetPasswordResponse registerResponse = response.body();

                                        String sucess, statuscode;
                                        if ((sucess = registerResponse.getSuccess()) != null) {

                                            if (sucess.contains("true")) {
                                                progressBar.setVisibility(View.GONE);
                                                inputEmail.setText("");
                                                inputEmail.setEnabled(true);
                                                Snackbar.make(btnSubmit, "Reset Password sent to your email id.please check", Snackbar.LENGTH_LONG).show();
                                            }
                                            if (sucess.contains("false")) {

                                                if (registerResponse.getWarning() instanceof String) {
                                                    inputEmail.setEnabled(true);
                                                    progressBar.setVisibility(View.GONE);
                                                    inputEmail.setText("");
                                                    inputEmail.setEnabled(true);

                                                    Snackbar.make(btnSubmit, registerResponse.getWarning().toString(), Snackbar.LENGTH_LONG).show();
                                                } else if (registerResponse.getWarning() instanceof List) {

                                                    inputEmail.setEnabled(true);
                                                    progressBar.setVisibility(View.GONE);
                                                    inputEmail.setText("");
                                                    inputEmail.setEnabled(true);

                                                    Snackbar.make(btnSubmit, ((List) registerResponse.getWarning()).get(0).toString(), Snackbar.LENGTH_LONG).show();
                                                }


                                            }
                                        }


                                        if ((statuscode = registerResponse.getStatusCode()) != null) {
                                            if (statuscode.equals("401")) {

                                                inputEmail.setEnabled(true);
                                                progressBar.setVisibility(View.GONE);
                                                Snackbar.make(btnSubmit, registerResponse.getStatusText(), Snackbar.LENGTH_LONG).show();
                                            }
                                        }


                                    }

                                    @Override
                                    public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                                        inputEmail.setEnabled(true);

                                        progressBar.setVisibility(View.GONE);

                                        Toast.makeText(getApplicationContext(), "Please try later...", Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                        } else {


                            inputEmail.setError(getResources().getString(R.string.invalid_email));
                            // Snackbar.make(btnSubmit, R.string.invalid_email, Snackbar.LENGTH_LONG).show();
                        }

                    }
                } else {


                    Snackbar.make(btnSubmit, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }
            }


        });


    }

}
