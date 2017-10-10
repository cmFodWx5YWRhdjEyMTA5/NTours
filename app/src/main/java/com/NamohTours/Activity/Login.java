package com.NamohTours.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.NamohTours.Service.ValidationToolBox;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.NamohTours.Model.TokenResponse;
import com.NamohTours.Model.UserAccountResponse;
import com.NamohTours.Model.UserLoginFields;
import com.NamohTours.Model.UserLoginRegisterDetailResponse;
import com.NamohTours.Model.UserLoginRegisterResponse;
import com.NamohTours.R;
import com.NamohTours.Service.CommonUtilities;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.View.ShowAlertDialog;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.AUTHORIZATION;
import static com.NamohTours.Service.Prefs.FIREBASEREGID;
import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.TOKEN_EXPIRY;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;
import static com.NamohTours.Service.Prefs.UserContact;
import static com.NamohTours.Service.Prefs.UserEmail;
import static com.NamohTours.Service.Prefs.UserID;
import static com.NamohTours.Service.Prefs.UserName;
import static com.NamohTours.Service.Prefs.UserRegister;

public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getSimpleName();
    SharedPreferences.Editor editor;
    String loginemail, loginpassword, firstname, lastname, email, contact, registrationId, regID;
    SharedPreferences prefs;
    String restoretoken, tempToken;
    ConnectionDetector cd;
    RequestParams params = new RequestParams();
    ProgressDialog prgDialog;
    String url, msg;
    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private TextView txtRegister, txtForget;
    private SharedPreferences RegisterPrefences;
    private ShowAlertDialog showAlertDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.input_loginemail);
        inputPassword = (EditText) findViewById(R.id.input_loginpassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtForget = (TextView) findViewById(R.id.txtForget);
        progressBar = (ProgressBar) findViewById(R.id.loginProgress);


        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Please Wait...");
        prgDialog.setCancelable(false);


        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);

        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        restoretoken = prefs.getString(TOKEN_KEY, null);
        registrationId = prefs.getString(FIREBASEREGID, "");


        boolean isUserRegister = RegisterPrefences.getBoolean(UserRegister, false);
        url = getIntent().getStringExtra("url");
        msg = getIntent().getStringExtra("msg");


        if (isUserRegister == true) {
            Intent next = new Intent(this, TourParentCategory.class);
            startActivity(next);
            finish();
        }


        //Creating Api Interface
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


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

                            //  Snackbar.make(spalshProgress,"Some Problem to Load",Snackbar.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        } else {
            Snackbar.make(btnLogin, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loginemail = inputEmail.getText().toString();
                loginpassword = inputPassword.getText().toString();
                regID = FirebaseInstanceId.getInstance().getToken();
                //city = inputCity.getText().toString();


                if (cd.isConnectingToInternet(getApplicationContext())) {


                    // if Text Fileds Empty
                    if (loginemail.isEmpty() && loginpassword.isEmpty()) {
                        Snackbar.make(btnLogin, "Enter login details", Snackbar.LENGTH_LONG).show();
                    } else {


                        boolean isValidEmail = ValidationToolBox.validateEmailId(loginemail);
                        boolean isValidMobile = ValidationToolBox.validateMobNo(loginemail);

                        boolean isValidPassword = ValidationToolBox.validatePassword(loginpassword);

                        if (isValidEmail || isValidMobile) {

                            if (isValidPassword) {

                                inputEmail.setEnabled(false);
                                inputPassword.setEnabled(false);
                                progressBar.setVisibility(View.VISIBLE);


                                // if token is not null
                                if (restoretoken != null) {


                                    Call<UserLoginRegisterResponse> call = apiService.loginuser(restoretoken, new UserLoginFields(loginemail, loginpassword));


                                    call.enqueue(new Callback<UserLoginRegisterResponse>() {
                                        @Override
                                        public void onResponse(Call<UserLoginRegisterResponse> call, Response<UserLoginRegisterResponse> response) {

                                            UserLoginRegisterResponse registerResponse = response.body();

                                            UserLoginRegisterDetailResponse response1;


                                            String sucess, statuscode;
                                            if ((sucess = registerResponse.getSuccess()) != null) {

                                                if (sucess.contains("true")) {
                                                    if ((response1 = registerResponse.getData()) != null) {

                                                        progressBar.setVisibility(View.GONE);

                                                        prgDialog.show();
                                                        prgDialog.setCanceledOnTouchOutside(false);
                                                        prgDialog.setCancelable(false);


                                                        //regID = FirebaseInstanceId.getInstance().getToken();
                                                        final String devid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                                        final String mobileno = registerResponse.getData().getTelephone();
                                                        final String fname = registerResponse.getData().getFirstName();
                                                        final String lname = registerResponse.getData().getLastName();
                                                        final String emailid = registerResponse.getData().getEmail();
                                                        final String CustID = registerResponse.getData().getCustomer_id();


                                                        params.put("flag", "L");
                                                        params.put("regId", regID);
                                                        params.put("devId", devid);
                                                        params.put("mobile", mobileno);
                                                        params.put("fname", fname);
                                                        params.put("lname", lname);


                                                        // Make RESTful webservice call using AsyncHttpClient object
                                                        final int DEFAULT_TIMEOUT = 70 * 1000;
                                                        AsyncHttpClient client = new AsyncHttpClient();
                                                        client.setTimeout(DEFAULT_TIMEOUT);

                                                        client.post(CommonUtilities.APP_SERVER_URL, params,
                                                                new AsyncHttpResponseHandler() {
                                                                    @Override
                                                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                                                        // prgDialog.hide();
                                                                        if (prgDialog != null) {
                                                                            prgDialog.dismiss();
                                                                        }


                                                                        Toast.makeText(getApplicationContext(), "Login  Successful..!!", Toast.LENGTH_SHORT).show();

                                                                        SharedPreferences.Editor editor = RegisterPrefences.edit();
                                                                        editor.putBoolean(UserRegister, true);
                                                                        editor.putString(UserName, fname + " " + lname);
                                                                        editor.putString(UserContact, mobileno);
                                                                        editor.putString(UserEmail, emailid);
                                                                        editor.putString(UserID, CustID);
                                                                        editor.commit();

                                                                        Intent next = new Intent(Login.this, TourParentCategory.class);
                                                                        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        startActivity(next);
                                                                        finish();


                                                                    }

                                                                    @Override
                                                                    public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable error) {

                                                                        // prgDialog.hide();
                                                                        if (prgDialog != null) {
                                                                            prgDialog.dismiss();
                                                                        }

                                                                        inputPassword.setEnabled(true);
                                                                        inputEmail.setEnabled(true);
                                                                        progressBar.setVisibility(View.GONE);

                                                                        // When Http response code is '404'
                                                                        if (statusCode == 404) {

                                                                            Toast.makeText(getApplicationContext(), "Internet Required . Please Try Later.", Toast.LENGTH_LONG).show();
                                                                        }
                                                                        // When Http response code is '500'
                                                                        else if (statusCode == 500) {
                                                                            Toast.makeText(getApplicationContext(), "Internet Required . Please Try Later.", Toast.LENGTH_LONG).show();
                                                                        }
                                                                        // When Http response code other than 404, 500
                                                                        else {
                                                                            Toast.makeText(getApplicationContext(),
                                                                                    "Internet Required . Please Try Later.",
                                                                                    Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });


                                                    }
                                                }
                                                if (sucess.contains("false")) {

                                                    inputPassword.setEnabled(true);
                                                    inputEmail.setEnabled(true);
                                                    progressBar.setVisibility(View.GONE);

                                                    if ((response1 = registerResponse.getWarning()) != null) {

                                                        if ((response1.getError()) != null) {
                                                            Snackbar.make(btnLogin, response1.getError(), Snackbar.LENGTH_LONG).show();
                                                        }

                                                    }
                                                }
                                            }


                                            // If Token is Expired so , refresh token (replace old token with new token)

                                            if ((statuscode = registerResponse.getStatusCode()) != null) {
                                                if (statuscode.equals("401")) {

                                                    prgDialog.show();
                                                    prgDialog.setCanceledOnTouchOutside(false);
                                                    prgDialog.setCancelable(false);


                                                    Call<TokenResponse> call1 = apiService.getRefreshToken(AUTHORIZATION, new TokenResponse(restoretoken));


                                                    call1.enqueue(new Callback<TokenResponse>() {
                                                        @Override
                                                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

                                                            TokenResponse registerResponse = response.body();


                                                            String response1, token;


                                                            if ((response1 = registerResponse.getToken()) != null) {
                                                                token = registerResponse.getToken();

                                                                editor.putString(TOKEN_KEY, token);
                                                                editor.putString(TOKEN_EXPIRY, registerResponse.getExpires());
                                                                editor.commit();

                                                                restoretoken = token;


                                                                inputPassword.setEnabled(true);
                                                                inputEmail.setEnabled(true);
                                                                progressBar.setVisibility(View.GONE);


                                                                if (prgDialog != null) {
                                                                    prgDialog.dismiss();
                                                                }

                                                                Intent start = new Intent(Login.this, Login.class);
                                                                startActivity(start);
                                                                finish();


                                                                Snackbar.make(btnLogin, "Please Try Again", Snackbar.LENGTH_LONG).show();


                                                            }


                                                        }

                                                        @Override
                                                        public void onFailure(Call<TokenResponse> call, Throwable t) {


                                                            inputPassword.setEnabled(true);
                                                            inputEmail.setEnabled(true);
                                                            progressBar.setVisibility(View.GONE);


                                                            if (prgDialog != null) {
                                                                prgDialog.dismiss();
                                                            }
                                                            Snackbar.make(btnLogin, "Please Try Again", Snackbar.LENGTH_LONG).show();


                                                        }
                                                    });


                                                }
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<UserLoginRegisterResponse> call, Throwable t) {
                                            inputEmail.setEnabled(true);
                                            inputPassword.setEnabled(true);
                                            progressBar.setVisibility(View.GONE);


                                            Snackbar.make(btnLogin, "Please Try Again", Snackbar.LENGTH_LONG).show();

//                                    finish();


                                        }
                                    });

                                } else {

                                    //  TOKEN is EMPty

                                    GetToken();
                                }
                            } else {

                                // Not Valid Password

                                inputPassword.setError(getResources().getString(R.string.invalid_password));
                            }
                        } else {

                            // Not Valid Email Id
                            inputEmail.setError("Invalid login Id");
                        }
                    }


                } else {

                    Snackbar.make(btnLogin, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }
            }


        });


        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(Login.this, Registration.class);
                startActivity(next);
                finish();
            }
        });

        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(Login.this, ForgetPassword.class);
                startActivity(next);
                finish();
            }
        });
    }

    private void GetToken() {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<TokenResponse> call = apiService.getRefreshToken(AUTHORIZATION, new TokenResponse(restoretoken));


        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

                TokenResponse registerResponse = response.body();


                String response1, token;


                if ((response1 = registerResponse.getToken()) != null) {
                    token = registerResponse.getToken();

                    editor.putString(TOKEN_KEY, token);
                    editor.putString(TOKEN_EXPIRY, registerResponse.getExpires());
                    editor.commit();

                    restoretoken = token;


                }

                if ((response1 = registerResponse.getSuccess()).equals("false")) {

                    if ((response1 = registerResponse.getError()) != null) {
                        Snackbar.make(btnLogin, registerResponse.getError(), Snackbar.LENGTH_LONG).show();
                    }

                }


            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {


            }
        });


    }


    // for getting account details and send to .net side for notification
    public void getAccount() {

        prgDialog.show();
        prgDialog.setCanceledOnTouchOutside(false);
        prgDialog.setCancelable(false);


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<UserAccountResponse> call = apiService.getAccountDetails(restoretoken);


        call.enqueue(new Callback<UserAccountResponse>() {
            @Override
            public void onResponse(Call<UserAccountResponse> call, Response<UserAccountResponse> response) {

                UserAccountResponse registerResponse = response.body();

                String Sucess;

                //  UserAccountDetailResponse userAccountDetailResponse = new UserAccountDetailResponse();

                if ((Sucess = registerResponse.getSuccess()) != null) {
                    if ((Sucess.contains("true"))) {


                        firstname = registerResponse.getData().getFirstname();
                        lastname = registerResponse.getData().getLastname();
                        contact = registerResponse.getData().getTelephone();


                        regID = FirebaseInstanceId.getInstance().getToken();
                        final String devid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        final String mobileno = contact;
                        final String fname = firstname;
                        final String lname = lastname;


                        params.put("flag", "L");
                        params.put("regId", registrationId);
                        params.put("devId", devid);
                        params.put("mobile", mobileno);
                        params.put("fname", fname);
                        params.put("lname", lname);


                        // Make RESTful webservice call using AsyncHttpClient object
                        AsyncHttpClient client = new AsyncHttpClient();

                        client.post(CommonUtilities.APP_SERVER_URL, params,
                                new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        // prgDialog.hide();
                                        if (prgDialog != null) {
                                            prgDialog.dismiss();
                                        }


                                        Toast.makeText(getApplicationContext(), "Login Successful..!!", Toast.LENGTH_SHORT).show();
                                        Intent next = new Intent(Login.this, TourParentCategory.class);
                                        startActivity(next);
                                        finish();


                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable error) {

                                        // prgDialog.hide();
                                        if (prgDialog != null) {
                                            prgDialog.dismiss();
                                        }
                                        // When Http response code is '404'
                                        if (statusCode == 404) {
                                            Toast.makeText(getApplicationContext(), "Internet Required . Please Try Later.", Toast.LENGTH_LONG).show();
                                        }
                                        // When Http response code is '500'
                                        else if (statusCode == 500) {
                                            Toast.makeText(getApplicationContext(), "Internet Required . Please Try Later.", Toast.LENGTH_LONG).show();
                                        }
                                        // When Http response code other than 404, 500
                                        else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Internet Required . Please Try Later.",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                    }
                    // if there is any error
                    else if ((Sucess.contains("false"))) {


                    }
                }

            }

            @Override
            public void onFailure(Call<UserAccountResponse> call, Throwable t) {


            }
        });


    }


    public void registerWithNotification() {


    }


}