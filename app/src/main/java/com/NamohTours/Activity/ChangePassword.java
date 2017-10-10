package com.NamohTours.Activity;

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

import com.NamohTours.Model.ChangePasswordFields;
import com.NamohTours.Model.ChangePasswordResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;


public class ChangePassword extends AppCompatActivity {

    String password, confirmPassword;
    private EditText inputPassword, inputChangePassword;
    private Button btnSubmit;

    private static final String TAG = ChangePassword.class.getSimpleName();

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
        setContentView(R.layout.activity_change_password);

        toolbar = (Toolbar) findViewById(R.id.changePasswordToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inputPassword = (EditText) findViewById(R.id.input_changePassword);
        inputChangePassword = (EditText) findViewById(R.id.input_changeConfirmPassword);
        progressBar = (ProgressBar) findViewById(R.id.changeProgress);
        btnSubmit = (Button) findViewById(R.id.btnChangePwd);


        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        restoretoken = prefs.getString(TOKEN_KEY, null);


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = inputPassword.getText().toString().trim();
                confirmPassword = inputChangePassword.getText().toString().trim();


                if (cd.isConnectingToInternet(getApplicationContext())) {




                   /* if (!(password.length() >= 4) || (!(confirmPassword.length() >= 4))) {
                        Snackbar.make(btnSubmit, "Password must be between 4 and 20 characters!", Snackbar.LENGTH_LONG).show();
                    }

                    if ((!TextUtils.isEmpty(inputPassword.getText().toString())) && (!TextUtils.isEmpty(inputChangePassword.getText().toString()))) {
                        if (!confirmPassword.equals(password)) {
                            Snackbar.make(btnSubmit, "Password Doesn't Match", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    if ((TextUtils.isEmpty(inputPassword.getText().toString())) || (TextUtils.isEmpty(inputChangePassword.getText().toString()))) {
                        Snackbar.make(btnSubmit, "Pleasesuccess Fill All Details", Snackbar.LENGTH_LONG).show();
                    }*/


                    //   if (confirmPassword.equals(password)) {


                    inputPassword.setEnabled(false);
                    inputChangePassword.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    ChangePasswordFields fields = new ChangePasswordFields(password, confirmPassword);


                    Call<ChangePasswordResponse> call = apiService.changePassword(restoretoken, fields);


                    call.enqueue(new Callback<ChangePasswordResponse>() {
                        @Override
                        public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {

                            //  ChangePasswordResponse registerResponse = response.body();


                            Boolean sucess = response.body().getSuccess();


                               /* progressBar.setVisibility(View.GONE);
                                inputPassword.setEnabled(true);
                                inputChangePassword.setEnabled(true);

*/

                            if (sucess) {


                                progressBar.setVisibility(View.GONE);
                                inputPassword.setText("");
                                inputChangePassword.setText("");
                                inputPassword.setEnabled(true);
                                inputChangePassword.setEnabled(true);
                                Snackbar.make(btnSubmit, "New Password Updated Successfully.", Snackbar.LENGTH_LONG).show();

                            }


                            //if (sucess.contains("false")) {
                            else {
                                progressBar.setVisibility(View.GONE);
                                inputPassword.setText("");
                                inputChangePassword.setText("");
                                inputPassword.setEnabled(true);
                                inputChangePassword.setEnabled(true);
                                Snackbar.make(btnSubmit, "Password Not Updated.", Snackbar.LENGTH_LONG).show();


                            }


                        }

                        @Override
                        public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);

                            inputPassword.setText("");
                            inputChangePassword.setText("");
                            inputPassword.setEnabled(true);
                            inputChangePassword.setEnabled(true);


                        }


                    });


                    // }
                } else

                {
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

           /* Intent changePassword = new Intent(ChangePassword.this,Account.class);
            startActivity(changePassword);*/
        finish();

        super.onBackPressed();
    }


}
