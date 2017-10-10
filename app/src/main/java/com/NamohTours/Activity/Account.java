package com.NamohTours.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.NamohTours.Model.UserAccountDetailResponse;
import com.NamohTours.Model.UserAccountResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.View.ShowAlertDialog;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

public class Account extends AppCompatActivity {

    TextView txtFirstname, txtLastname, txtEmail, txtContact, txtRewardPoints, txtTitle, txtChangePassword, txtEditProfile;
    SharedPreferences prefs;
    String restoretoken, firstname, lastname, email, contact;
    ConnectionDetector cd;
    Toolbar toolbar;
    private ShowAlertDialog showAlertDialog;
    private ProgressBar accountProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        toolbar = (Toolbar) findViewById(R.id.accountToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtFirstname = (TextView) findViewById(R.id.userFirstName);

        txtEmail = (TextView) findViewById(R.id.userEmail);
        txtContact = (TextView) findViewById(R.id.userPhone);
        txtRewardPoints = (TextView) findViewById(R.id.userRewardPoints);
        accountProgress = (ProgressBar) findViewById(R.id.AccountProgress);

        txtChangePassword = (TextView) findViewById(R.id.txtChangePassword);
        txtEditProfile = (TextView) findViewById(R.id.txtEditProfile);

        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<UserAccountResponse> call = apiService.getAccountDetails(restoretoken);


        call.enqueue(new Callback<UserAccountResponse>() {
            @Override
            public void onResponse(Call<UserAccountResponse> call, Response<UserAccountResponse> response) {

                UserAccountResponse registerResponse = response.body();

                String Sucess, error;

                UserAccountDetailResponse userAccountDetailResponse = new UserAccountDetailResponse();

                if ((Sucess = registerResponse.getSuccess()) != null) {
                    if ((Sucess.contains("true"))) {
                        accountProgress.setVisibility(View.GONE);

                        // Snackbar.make(txtRewardPoints,Sucess,Snackbar.LENGTH_LONG).show();

                        txtTitle.setText(String.valueOf(registerResponse.getData().getFirstname().charAt(0)));


                        String email1 = "Email id : ";
                        String emailId = registerResponse.getData().getEmail();
                        SpannableString ss1 = new SpannableString(email1);
                        ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, ss1.length(), 0);
                        txtEmail.append(ss1);
                        txtEmail.append(emailId);


                        String contact1 = "Contact : ";
                        String contactno = registerResponse.getData().getTelephone();
                        SpannableString ss = new SpannableString(contact1);
                        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);
                        txtContact.append(ss);
                        txtContact.append(contactno);


                        String reward = "Reward Points : ";
                        SpannableString ss2 = new SpannableString(reward);
                        ss2.setSpan(new StyleSpan(Typeface.BOLD), 0, ss2.length(), 0);
                        txtRewardPoints.append(ss2);

                        if (registerResponse.getData().getRewardsDetailsList() != null) {

                            String rewardpoints = registerResponse.getData().getRewardsDetailsList().get(0).getPoints();


                            txtRewardPoints.append(rewardpoints);

                        } else {
                            String rewardpoints = "0";

                            txtRewardPoints.append(rewardpoints);

                        }


                        txtFirstname.setText(registerResponse.getData().getFirstname() + "\t" + registerResponse.getData().getLastname());

                        //  txtContact.setText("Contact : "+registerResponse.getData().getTelephone());


                        firstname = registerResponse.getData().getFirstname();
                        lastname = registerResponse.getData().getLastname();
                        email = registerResponse.getData().getEmail();
                        contact = registerResponse.getData().getTelephone();



                          /*  if(registerResponse.getData().getRewardsDetailsList() != null)
                            {
                                txtRewardPoints.setText("Reward Points: "+registerResponse.getData().getRewardsDetailsList().get(0).getPoints());

                            }
                            else
                                {
                                txtRewardPoints.setText("Reward Points: "+0);

                            }*/

                    }
                    // if there is any error
                    else if ((Sucess.contains("false"))) {

                        if ((error = registerResponse.getError()) != null) {
                            Snackbar.make(txtRewardPoints, error, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<UserAccountResponse> call, Throwable t) {


            }
        });


        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changePassword = new Intent(Account.this, ChangePassword.class);
                startActivity(changePassword);
                // finish();
            }
        });


        txtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(Account.this, UserEditProfile.class);
                editProfile.putExtra("fname", firstname);
                editProfile.putExtra("lname", lastname);
                editProfile.putExtra("email", email);
                editProfile.putExtra("contact", contact);
                startActivity(editProfile);
                // finish();
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

        // super.onBackPressed();
        finish();
        super.onBackPressed();

    }


}
