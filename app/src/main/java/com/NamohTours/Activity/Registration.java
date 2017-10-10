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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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


public class Registration extends AppCompatActivity {

    private static final String TAG = Registration.class.getSimpleName();
    SharedPreferences prefs;
    String restoretoken;
    SharedPreferences.Editor editor;
    String firstname, lastname, telephone, password, confirm, city, email, dummyemail, registrationId, regID;
    String regId = "";
    Long unixtime;
    ConnectionDetector cd;
    ShowAlertDialog showAlertDialog;
    ProgressBar progressBar;
    RequestParams params = new RequestParams();
    ProgressDialog prgDialog;
    private EditText inputName, inputLName, inputPassword, inputConfirmPwd, inputContact, inputEmail;
    private Button btnRegister;
    private TextView txtLogin;
    private AutoCompleteTextView CityautocompleteView;
    private SharedPreferences RegisterPrefences;
    private String address_1 = "address";
    // India Country id from open cart db
    private String countryid = "99";
    // Maharashtra Zone id from open cart db
    private String zone_id = "1493";
    private Integer agree = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);

        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        restoretoken = prefs.getString(TOKEN_KEY, null);

        String[] cityArray = getResources().getStringArray(R.array.city_array);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please Wait...");


        registrationId = prefs.getString(FIREBASEREGID, "");


        // Declare AutoComplete adapter and design of autocomplete textview  first then auto complete text view intailsation
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cityArray);


        // Edit Text
        inputName = (EditText) findViewById(R.id.input_name);
        inputLName = (EditText) findViewById(R.id.input_lname);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputConfirmPwd = (EditText) findViewById(R.id.input_confirm_password);
        inputContact = (EditText) findViewById(R.id.input_contact);
        inputEmail = (EditText) findViewById(R.id.input_email);
        // inputCity=(TextInputEditText)findViewById(R.id.input_city);

        // button
        btnRegister = (Button) findViewById(R.id.btn_register);

        //TextView
        txtLogin = (TextView) findViewById(R.id.txtPleaseLogin);

        // AutoCompleteTextView
        CityautocompleteView = (AutoCompleteTextView) findViewById(R.id.autoComplete_City);


        progressBar = (ProgressBar) findViewById(R.id.registerProgress);

        showAlertDialog = new ShowAlertDialog();


        CityautocompleteView.setAdapter(adapter);

        CityautocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                city = (String) parent.getItemAtPosition(position);

            }
        });

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
                            //  finish();
                        }
                    });
                }
            }
        } else {
            Snackbar.make(btnRegister, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                firstname = inputName.getText().toString();
                lastname = inputLName.getText().toString();
                password = inputPassword.getText().toString();
                confirm = inputConfirmPwd.getText().toString();
                email = inputEmail.getText().toString();
                telephone = inputContact.getText().toString();

                unixtime = System.currentTimeMillis() / 1000L;

                dummyemail = unixtime.toString();


                // if connected to internet

                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {

                    boolean isValidFirstName = ValidationToolBox.validateFirstName(firstname);
                    boolean isValidLastName = ValidationToolBox.validateLastName(lastname);
                    boolean isValidContact = ValidationToolBox.validateMobNo(telephone);
                    boolean isValidPassword = ValidationToolBox.validatePassword(password);
                    boolean isValidConfirmPwd = ValidationToolBox.validatePassword(confirm);
                    boolean isValidMatchPwd = ValidationToolBox.validatePasswordAndConfirmPwd(password, confirm);
                    boolean isValidEmail = ValidationToolBox.validateEmailId(email);


                    if (isValidFirstName) {
                        if (isValidLastName) {

                            if (isValidContact) {

                                if (isValidPassword) {

                                    if (isValidConfirmPwd) {

                                        if (isValidMatchPwd) {

                                            if (isValidEmail) {

                                                if (!(TextUtils.isEmpty(city))) {
                                                    inputName.setEnabled(false);
                                                    inputLName.setEnabled(false);
                                                    inputContact.setEnabled(false);
                                                    inputPassword.setEnabled(false);
                                                    inputConfirmPwd.setEnabled(false);
                                                    inputEmail.setEnabled(false);


                                                    progressBar.setVisibility(View.VISIBLE);


                                                    // if token is not null
                                                    if (restoretoken != null) {
                                                        Call<UserLoginRegisterResponse> call = apiService.registeruser(restoretoken, new UserLoginRegisterDetailResponse(address_1, city, countryid, email, firstname, lastname, telephone, zone_id, password, confirm, agree));


                                                        call.enqueue(new Callback<UserLoginRegisterResponse>() {
                                                            @Override
                                                            public void onResponse(Call<UserLoginRegisterResponse> call, Response<UserLoginRegisterResponse> response) {

                                                                UserLoginRegisterResponse registerResponse = response.body();


                                                                String sucess, statuscode;
                                                                UserLoginRegisterDetailResponse response1;


                                                                if ((sucess = registerResponse.getSuccess()) != null) {


                                                                    if (sucess.contains("true")) {
                                                                        if ((response1 = registerResponse.getData()) != null) {

                                                                            // for Register on.net side for notifications

                                                                            prgDialog.show();
                                                                            prgDialog.setCanceledOnTouchOutside(false);
                                                                            prgDialog.setCancelable(false);


                                                                            regID = FirebaseInstanceId.getInstance().getToken();
                                                                            final String devid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                                                            final String mobileno = inputContact.getText().toString();
                                                                            final String fname = inputName.getText().toString();
                                                                            final String lname = inputLName.getText().toString();
                                                                            final String emailId = inputEmail.getText().toString();
                                                                            final String CustID = registerResponse.getData().getCustomer_id();


                                                                            params.put("flag", "R");
                                                                            params.put("regId", regID);
                                                                            params.put("devId", devid);
                                                                            params.put("mobile", mobileno);
                                                                            params.put("fname", fname);
                                                                            params.put("lname", lname);

                                                                            final int DEFAULT_TIMEOUT = 70 * 1000;

                                                                            // Make RESTful webservice call using AsyncHttpClient object
                                                                            AsyncHttpClient client = new AsyncHttpClient();
                                                                            client.setTimeout(DEFAULT_TIMEOUT);


                                                                            client.post(CommonUtilities.APP_SERVER_URL, params,
                                                                                    new AsyncHttpResponseHandler() {
                                                                                        @Override
                                                                                        public void onSuccess(int i, Header[] headers, byte[] bytes) {

                                                                                            SharedPreferences.Editor editor = RegisterPrefences.edit();
                                                                                            editor.putBoolean(UserRegister, true);
                                                                                            editor.putString(UserName, fname + " " + lname);
                                                                                            editor.putString(UserContact, mobileno);
                                                                                            editor.putString(UserEmail, emailId);
                                                                                            editor.putString(UserID, CustID);
                                                                                            editor.commit();


                                                                                            // prgDialog.hide();
                                                                                            if (prgDialog != null) {
                                                                                                prgDialog.dismiss();
                                                                                            }


                                                                                            Toast.makeText(getApplicationContext(), "Register Successful..!!", Toast.LENGTH_SHORT).show();
                                                                                            Intent next = new Intent(Registration.this, TourParentCategory.class);
                                                                                            startActivity(next);
                                                                                            finish();


                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable error) {


                                                                                            inputName.setEnabled(true);
                                                                                            inputLName.setEnabled(true);
                                                                                            inputContact.setEnabled(true);
                                                                                            inputPassword.setEnabled(true);
                                                                                            inputConfirmPwd.setEnabled(true);
                                                                                            inputEmail.setEnabled(true);
                                                                                            progressBar.setVisibility(View.GONE);

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

                                                                    }


                                                                    if (sucess.contains("false")) {


                                                                        inputName.setEnabled(true);
                                                                        inputLName.setEnabled(true);
                                                                        inputContact.setEnabled(true);
                                                                        inputPassword.setEnabled(true);
                                                                        inputConfirmPwd.setEnabled(true);
                                                                        inputEmail.setEnabled(true);
                                                                        progressBar.setVisibility(View.GONE);
                                                                        if ((response1 = registerResponse.getWarning()) != null)

                                                                        {

                                                                            if ((response1.getError()) != null) {
                                                                                Snackbar.make(btnRegister, response1.getError(), Snackbar.LENGTH_LONG).show();
                                                                            }


                                                                        }
                                                                    }

                                                                }


                                                                if ((statuscode = registerResponse.getStatusCode()) != null) {
                                                                    if (statuscode.equals("401")) {
                                                                        inputName.setEnabled(true);
                                                                        inputLName.setEnabled(true);
                                                                        inputContact.setEnabled(true);
                                                                        inputPassword.setEnabled(true);
                                                                        inputConfirmPwd.setEnabled(true);
                                                                        inputEmail.setEnabled(true);
                                                                        progressBar.setVisibility(View.GONE);
                                                                        // Snackbar.make(btnRegister, registerResponse.getStatusText(), Snackbar.LENGTH_LONG).show();


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

                                                                                    Intent start = new Intent(Registration.this, Registration.class);
                                                                                    startActivity(start);
                                                                                    finish();


                                                                                    Snackbar.make(btnRegister, "Please Try Again", Snackbar.LENGTH_LONG).show();


                                                                                }


                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<TokenResponse> call, Throwable t) {


                                                                                inputName.setEnabled(true);
                                                                                inputLName.setEnabled(true);
                                                                                inputContact.setEnabled(true);
                                                                                inputPassword.setEnabled(true);
                                                                                inputConfirmPwd.setEnabled(true);
                                                                                inputEmail.setEnabled(true);
                                                                                progressBar.setVisibility(View.GONE);
                                                                                CityautocompleteView.setEnabled(true);


                                                                                if (prgDialog != null) {
                                                                                    prgDialog.dismiss();
                                                                                }

                                                                                Snackbar.make(btnRegister, "Please Try Again", Snackbar.LENGTH_LONG).show();


                                                                            }
                                                                        });


                                                                    }
                                                                }


                                                            }


                                                            @Override
                                                            public void onFailure(Call<UserLoginRegisterResponse> call, Throwable t) {


                                                                inputName.setEnabled(true);
                                                                inputLName.setEnabled(true);
                                                                inputContact.setEnabled(true);
                                                                inputPassword.setEnabled(true);
                                                                inputConfirmPwd.setEnabled(true);
                                                                inputEmail.setEnabled(true);
                                                                progressBar.setVisibility(View.GONE);
                                                                CityautocompleteView.setEnabled(true);


                                                                if (prgDialog != null) {
                                                                    prgDialog.dismiss();
                                                                }

                                                                Snackbar.make(btnRegister, "Please Try Again", Snackbar.LENGTH_LONG).show();


                                                            }
                                                        });

                                                    }


                                                } else {
                                                    CityautocompleteView.setError("Enter city");
                                                }
                                            } else {
                                                inputEmail.setError(getResources().getString(R.string.invalid_email));
                                            }

                                        } else {
                                            inputConfirmPwd.setError(getResources().getString(R.string.pwd_cofirmpwd));
                                        }


                                    } else {
                                        inputConfirmPwd.setError(getResources().getString(R.string.invalid_password));
                                    }

                                } else {
                                    inputPassword.setError(getResources().getString(R.string.invalid_password));
                                }

                            } else {
                                inputContact.setError(getResources().getString(R.string.invalid_mobile));
                            }

                        } else {
                            inputLName.setError(getResources().getString(R.string.invalid_lname));
                        }

                    } else {
                        inputName.setError(getResources().getString(R.string.invalid_fname));
                    }


                } else {
                    Snackbar.make(btnRegister, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }


            }
        });


        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(Registration.this, Login.class);
                startActivity(next);
                finish();

            }
        });


    }


}
