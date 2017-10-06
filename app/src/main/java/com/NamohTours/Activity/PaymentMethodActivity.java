package com.NamohTours.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.NamohTours.Model.GetPaymentMethodResponse;
import com.NamohTours.Model.PostPaymentMethodOption;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.PAYMENT;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;


public class PaymentMethodActivity extends AppCompatActivity {


    Button btnPaymentContinue;
    private AppCompatRadioButton[] radio;
    private LinearLayout llPayment;
    private Toolbar toolbar;

    private SharedPreferences prefs;
    private String restoretoken, paymentMethodCode;
    private AppCompatCheckBox ChkPaymentTerms;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);


        toolbar = (Toolbar) findViewById(R.id.PaymentToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnPaymentContinue = (Button) findViewById(R.id.btnPaymentContinue);
        ChkPaymentTerms = (AppCompatCheckBox) findViewById(R.id.ChkPaymentTerms);

        llPayment = (LinearLayout) findViewById(R.id.llPayment);


        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            Call<GetPaymentMethodResponse> responseCall = apiService.getPaymentMethodResponse(restoretoken);

            progressDialog.show();
            responseCall.enqueue(new Callback<GetPaymentMethodResponse>() {
                @Override
                public void onResponse(Call<GetPaymentMethodResponse> call, Response<GetPaymentMethodResponse> response) {

                    if (response.body().getSuccess().equals("true")) {


                        progressDialog.dismiss();

                        if (response.body().getPaymentMethodDetailResponse().getPayment_methods().size() > 0) {

                            int radiosize = response.body().getPaymentMethodDetailResponse().getPayment_methods().size();

                            // We didn't require COP (Cash on Pickup) so we subtract 1 from radio size
                            radiosize -= 1;

                            RadioGroup rg = new RadioGroup(PaymentMethodActivity.this);

                            radio = new AppCompatRadioButton[radiosize];

                            int i = 0;

                            for (String key : response.body().getPaymentMethodDetailResponse().getPayment_methods().keySet()) {


                                if (!(response.body().getPaymentMethodDetailResponse().getPayment_methods().get(key).getCode().equals("cop"))) {

                                    radio[i] = new AppCompatRadioButton(PaymentMethodActivity.this);
                                    radio[i].setText(response.body().getPaymentMethodDetailResponse().getPayment_methods().get(key).getTitle());
                                    radio[i].setTextColor(getResources().getColor(R.color.colorGrey));
                                    radio[i].setTag(response.body().getPaymentMethodDetailResponse().getPayment_methods().get(key).getCode());
                                    rg.addView(radio[i]);
                                    i++;
                                } else {

                                    // don't add cop (cash om pickup ) method in this radio group
                                }


                            }


                            // Add radio group to LinearLayout
                            llPayment.addView(rg);


                            for (int j = 0; j < radiosize; j++) {
                                final int k = j;

                                radio[j].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {

                                            paymentMethodCode = radio[k].getTag().toString();


                                        }
                                    }
                                });

                            }


                        } else {
                            progressDialog.dismiss();
                            Snackbar.make(btnPaymentContinue, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                        }

                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(btnPaymentContinue, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<GetPaymentMethodResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Snackbar.make(btnPaymentContinue, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
                }
            });


            btnPaymentContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if ((ChkPaymentTerms.isChecked()) && (!TextUtils.isEmpty(paymentMethodCode))) {

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(PAYMENT, paymentMethodCode);
                        editor.commit();

                        PaymentContinue("1", paymentMethodCode);


                    } else {
                        Snackbar.make(btnPaymentContinue, "Please select payment method, terms & conditions", Snackbar.LENGTH_LONG).show();
                    }


                }
            });

        } else {

            Snackbar.make(btnPaymentContinue, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }


    }


    private void PaymentContinue(String checkBoxValue, String PaymentMethodOption) {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            Call<PostPaymentMethodOption> call = apiService.postPaymentMethod(restoretoken, new PostPaymentMethodOption(PaymentMethodOption, checkBoxValue));


            progressDialog.show();

            call.enqueue(new Callback<PostPaymentMethodOption>() {
                @Override
                public void onResponse(Call<PostPaymentMethodOption> call, Response<PostPaymentMethodOption> response) {

                    if (response.body().getSuccess().equals("true")) {

                        progressDialog.dismiss();
                        Intent pay = new Intent(PaymentMethodActivity.this, ConfirmOrderActivity.class);
                        startActivity(pay);
                        // finish();

                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(btnPaymentContinue, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<PostPaymentMethodOption> call, Throwable t) {

                    progressDialog.dismiss();
                    Snackbar.make(btnPaymentContinue, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                }
            });

        } else {
            Snackbar.make(btnPaymentContinue, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }
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
    }


}
