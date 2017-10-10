package com.NamohTours.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.NamohTours.Model.UserEditProfileResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

public class UserEditProfile extends AppCompatActivity {

    String password, confirmPassword;
    private EditText inputFname, inputLname, inputTelephone, inputEmail;
    private Button btnSubmit;
    private String firstname, lastname, email, telephone;


    private static final String TAG = UserEditProfile.class.getSimpleName();

    SharedPreferences.Editor editor;
    private SharedPreferences RegisterPrefences;


    SharedPreferences prefs;

    String restoretoken;

    ConnectionDetector cd;

    private ProgressBar progressBar;


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);


        toolbar = (Toolbar) findViewById(R.id.changeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firstname = getIntent().getStringExtra("fname");
        lastname = getIntent().getStringExtra("lname");
        email = getIntent().getStringExtra("email");
        telephone = getIntent().getStringExtra("contact");


        inputFname = (EditText) findViewById(R.id.input_changeFname);
        inputLname = (EditText) findViewById(R.id.input_changeLname);
        inputEmail = (EditText) findViewById(R.id.input_changeEmail);
        inputTelephone = (EditText) findViewById(R.id.input_changeTelephone);


        btnSubmit = (Button) findViewById(R.id.btnchangeSubmit);
        progressBar = (ProgressBar) findViewById(R.id.changePrgrs);


        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        restoretoken = prefs.getString(TOKEN_KEY, null);


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        inputFname.setText(firstname);
        inputLname.setText(lastname);
        inputEmail.setText(email);
        inputTelephone.setText(telephone);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstname = inputFname.getText().toString();
                lastname = inputLname.getText().toString();
                email = inputEmail.getText().toString();
                telephone = inputTelephone.getText().toString();


                if (cd.isConnectingToInternet(getApplicationContext())) {


                    inputFname.setEnabled(false);
                    inputLname.setEnabled(false);
                    inputTelephone.setEnabled(false);
                    inputEmail.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    Call<UserEditProfileResponse> call = apiService.editProfile(restoretoken, new UserEditProfileResponse(email, firstname, lastname, telephone));


                    call.enqueue(new Callback<UserEditProfileResponse>() {
                        @Override
                        public void onResponse(Call<UserEditProfileResponse> call, Response<UserEditProfileResponse> response) {

                            UserEditProfileResponse registerResponse = response.body();


                            Boolean sucess;
                            String statuscode;
                            UserEditProfileResponse response1;


                            if (registerResponse.getSuccess() != null) {

                                sucess = registerResponse.getSuccess();

                                if (sucess) {

                                    // Log.d(TAG , "Sucessful Data First Name : " +response1.getFirstName());


                                    progressBar.setVisibility(View.GONE);
                                    Snackbar.make(btnSubmit, "Profile Updated Successfully", Snackbar.LENGTH_LONG).show();

                                    Intent changePassword = new Intent(UserEditProfile.this, Account.class);
                                    startActivity(changePassword);
                                    finish();


                                } else {


                                    inputFname.setEnabled(true);
                                    inputLname.setEnabled(true);
                                    inputTelephone.setEnabled(true);
                                    inputEmail.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                    Snackbar.make(btnSubmit, "Profile Not Updated ", Snackbar.LENGTH_LONG).show();
                                    if ((response1 = registerResponse.getWarning()) != null)

                                    {
                                        if ((response1.getWarning()) != null) {
                                            Snackbar.make(btnSubmit, response1.getWarning().toString(), Snackbar.LENGTH_LONG).show();
                                        }


                                    }
                                }

                            }


                            if ((statuscode = registerResponse.getStatusCode()) != null) {
                                if (statuscode.equals("401")) {
                                    inputFname.setEnabled(true);
                                    inputLname.setEnabled(true);
                                    inputTelephone.setEnabled(true);
                                    inputEmail.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                    Snackbar.make(btnSubmit, registerResponse.getStatusText(), Snackbar.LENGTH_LONG).show();
                                }
                            }


                        }


                        @Override
                        public void onFailure(Call<UserEditProfileResponse> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);

                            inputEmail.setEnabled(true);
                            progressBar.setVisibility(View.GONE);

                        }
                    });


                } else {
                    Snackbar.make(btnSubmit, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }


            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();

       /* Intent changePassword = new Intent(UserEditProfile.this,Account.class);
        startActivity(changePassword);
        finish();*/
    }
}
