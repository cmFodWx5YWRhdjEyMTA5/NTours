package com.NamohTours.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.NamohTours.Model.GetUserPaymentAddress;
import com.NamohTours.Model.PostUserPaymentAddressResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

public class PaymentAddressActivity extends AppCompatActivity {


    private EditText txtBillingName, txtAddrss, txtBillingCity, txtBillingState, txtBillingCountry;
    private Button btnContinue;
    private Toolbar toolbar;

    private SharedPreferences prefs;
    private String restoretoken, address_id;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_address);


        toolbar = (Toolbar) findViewById(R.id.BillingToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtBillingName = (EditText) findViewById(R.id.txtBillingName);
        txtAddrss = (EditText) findViewById(R.id.txtBillingAddress);
        txtBillingCity = (EditText) findViewById(R.id.txtBillingCity);
        txtBillingState = (EditText) findViewById(R.id.txtBillingState);
        txtBillingCountry = (EditText) findViewById(R.id.txtBillingCountry);

        btnContinue = (Button) findViewById(R.id.btnBillingContinue);


        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);


        progressDialog = new ProgressDialog(PaymentAddressActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {

            progressDialog.show();
            Call<GetUserPaymentAddress> call = apiService.getUserPaymentAddress(restoretoken);

            call.enqueue(new Callback<GetUserPaymentAddress>() {
                @Override
                public void onResponse(Call<GetUserPaymentAddress> call, Response<GetUserPaymentAddress> response) {


                    if (response.body().getSuccess().equals("true")) {

                        progressDialog.dismiss();
                        if ((response.body().getData().getAddressList().size() > 0) && (response.body().getData().getAddressList() != null)) {

                            address_id = response.body().getData().getAddressList().get(0).getAddress_id();
                            txtBillingName.setText(response.body().getData().getAddressList().get(0).getFirstname() + "\t" + response.body().getData().getAddressList().get(0).getLastname());
                            txtAddrss.setText(response.body().getData().getAddressList().get(0).getAddress_1());
                            txtBillingCity.setText(response.body().getData().getAddressList().get(0).getCity());
                            txtBillingState.setText(response.body().getData().getAddressList().get(0).getZone());
                            txtBillingCountry.setText(response.body().getData().getAddressList().get(0).getCountry());

                        }

                    } else {

                        progressDialog.dismiss();
                        Snackbar.make(btnContinue, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<GetUserPaymentAddress> call, Throwable t) {

                    progressDialog.dismiss();

                    Snackbar.make(btnContinue, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
                }
            });


            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressDialog.show();
                    setExistingAddress();
                    /*Intent checkout = new Intent(PaymentAddressActivity.this, PaymentMethodActivity.class);
                    startActivity(checkout);
                    finish();*/
                }
            });


        } else {
            Snackbar.make(btnContinue, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }


    }


    // Post Payment Address
    private void setExistingAddress() {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            Call<PostUserPaymentAddressResponse> callResponse = apiService.postUserPaymentAddress(restoretoken, new PostUserPaymentAddressResponse("existing", address_id));

            callResponse.enqueue(new Callback<PostUserPaymentAddressResponse>() {
                @Override
                public void onResponse(Call<PostUserPaymentAddressResponse> call, Response<PostUserPaymentAddressResponse> response) {


                    if (response.body().getSuccess().equals("true")) {

                        progressDialog.dismiss();
                        Intent checkout = new Intent(PaymentAddressActivity.this, PaymentMethodActivity.class);
                        startActivity(checkout);
                        // finish();
                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(btnContinue, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onFailure(Call<PostUserPaymentAddressResponse> call, Throwable t) {

                    progressDialog.dismiss();
                    Snackbar.make(btnContinue, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
                    btnContinue.setVisibility(View.INVISIBLE);
                }
            });


        } else {
            Snackbar.make(btnContinue, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
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
