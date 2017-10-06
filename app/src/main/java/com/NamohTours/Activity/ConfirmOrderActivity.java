package com.NamohTours.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NamohTours.Adapter.ConfirmOrderAdapter;
import com.NamohTours.Model.ApplyCouponCodeOnOrder;
import com.NamohTours.Model.ApplyVoucherCodeOnOrder;
import com.NamohTours.Model.ConfirmOrderProductResponse;
import com.NamohTours.Model.ConfirmOrderResponse;
import com.NamohTours.Model.OrderCashChequePaymentMethodResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.PAYMENT;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

public class ConfirmOrderActivity extends AppCompatActivity {

    private static final String TAG = ConfirmOrderActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private EditText edtCoupon, edtVoucher;
    private Button btnCouponApply, btnVoucherApply, btnPlaceOrder;
    private LinearLayout llPrice;
    private Toolbar toolbar;
    private SharedPreferences prefs;
    private String restoretoken, payment;
    private ProgressDialog progressDialog;

    private List<ConfirmOrderProductResponse> productDetailResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        toolbar = (Toolbar) findViewById(R.id.ConfirmTollbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.confirmOrderProductRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ConfirmOrderActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(ConfirmOrderActivity.this, DividerItemDecoration.HORIZONTAL));

        btnCouponApply = (Button) findViewById(R.id.btnCouponApply);
        btnVoucherApply = (Button) findViewById(R.id.btnVoucherApply);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);

        edtCoupon = (EditText) findViewById(R.id.edtCoupon);
        edtVoucher = (EditText) findViewById(R.id.edtVoucher);

        llPrice = (LinearLayout) findViewById(R.id.llOrderPrice);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);
        payment = prefs.getString(PAYMENT, null);

        progressDialog = new ProgressDialog(ConfirmOrderActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        productDetailResponses = new ArrayList<ConfirmOrderProductResponse>();


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {

            Call<ConfirmOrderResponse> call = apiService.ConfirmOrderRes(restoretoken);

            progressDialog.show();

            call.enqueue(new Callback<ConfirmOrderResponse>() {
                @Override
                public void onResponse(Call<ConfirmOrderResponse> call, Response<ConfirmOrderResponse> response) {


                    if (response.body().getSuccess().equals("true")) {

                        progressDialog.dismiss();
                        if ((response.body().getResponse().getProducts().size() > 0) && (response.body().getResponse().getProducts() != null)) {

                            for (int i = 0; i < response.body().getResponse().getTotals().size(); i++) {


                                if (response.body().getResponse().getTotals().get(i).getTitle().equals("Total")) {
                                    TextView txt = new TextView(ConfirmOrderActivity.this);
                                    txt.setText(response.body().getResponse().getTotals().get(i).getTitle() + ":\t\t"
                                            + response.body().getResponse().getTotals().get(i).getText());

                                    //txt.setTextSize(getResources().getDimension(R.dimen.price_text_size));

                                    txt.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    txt.setTypeface(null, Typeface.BOLD);
                                    txt.setGravity(Gravity.CENTER);

                                    llPrice.addView(txt);
                                } else {
                                    TextView txt = new TextView(ConfirmOrderActivity.this);
                                    txt.setText(response.body().getResponse().getTotals().get(i).getTitle() + ":\t\t"
                                            + response.body().getResponse().getTotals().get(i).getText());

                                    //txt.setTextSize(getResources().getDimension(R.dimen.price_text_size));

                                    txt.setGravity(Gravity.CENTER);

                                    llPrice.addView(txt);
                                }




                            }


                            productDetailResponses = response.body().getResponse().getProducts();

                            recyclerView.setAdapter(new ConfirmOrderAdapter(productDetailResponses, R.layout.list_item_confirm_order, ConfirmOrderActivity.this));


                        }

                    } else {

                        progressDialog.dismiss();

                        Snackbar.make(btnPlaceOrder, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<ConfirmOrderResponse> call, Throwable t) {
                    progressDialog.dismiss();

                    Snackbar.make(btnPlaceOrder, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
                }
            });


            btnVoucherApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!(TextUtils.isEmpty(edtVoucher.getText().toString()))) {

                        ApplyVoucherCode(edtVoucher.getText().toString());
                    }

                }
            });


            btnCouponApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(TextUtils.isEmpty(edtCoupon.getText().toString()))) {
                        ApplyCouponCode(edtCoupon.getText().toString());
                    }
                }
            });


            btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (productDetailResponses.size() > 0) {
                        if (payment.equals("ccavenue")) {
                            Intent intent = new Intent(ConfirmOrderActivity.this, OrderPayWebActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            CashPayment(payment);
                        }


                    }


                }
            });

        } else {
            Snackbar.make(btnPlaceOrder, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }


    }


    private void ApplyVoucherCode(String voucherCode) {
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            Call<ApplyVoucherCodeOnOrder> call = apiService.voucherCode(restoretoken, new ApplyVoucherCodeOnOrder(voucherCode));

            progressDialog.show();

            call.enqueue(new Callback<ApplyVoucherCodeOnOrder>() {
                @Override
                public void onResponse(Call<ApplyVoucherCodeOnOrder> call, Response<ApplyVoucherCodeOnOrder> response) {

                    if (response.body().getSuccess().equals("true")) {
                        progressDialog.dismiss();

                        Intent vc = new Intent(ConfirmOrderActivity.this, ConfirmOrderActivity.class);
                        startActivity(vc);
                        finish();


                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(btnPlaceOrder, response.body().getError(), Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<ApplyVoucherCodeOnOrder> call, Throwable t) {

                    progressDialog.dismiss();
                    Snackbar.make(btnPlaceOrder, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                }
            });

        } else {
            Snackbar.make(btnPlaceOrder, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }
    }


    private void ApplyCouponCode(String Couponcode) {
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            Call<ApplyCouponCodeOnOrder> call = apiService.couponCode(restoretoken, new ApplyCouponCodeOnOrder(Couponcode));

            progressDialog.show();

            call.enqueue(new Callback<ApplyCouponCodeOnOrder>() {
                @Override
                public void onResponse(Call<ApplyCouponCodeOnOrder> call, Response<ApplyCouponCodeOnOrder> response) {

                    if (response.body().getSuccess().equals("true")) {
                        progressDialog.dismiss();

                        Intent vc = new Intent(ConfirmOrderActivity.this, ConfirmOrderActivity.class);
                        startActivity(vc);
                        finish();


                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(btnPlaceOrder, response.body().getError(), Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<ApplyCouponCodeOnOrder> call, Throwable t) {

                    progressDialog.dismiss();
                    Snackbar.make(btnPlaceOrder, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                }
            });

        } else {
            Snackbar.make(btnPlaceOrder, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }
    }


    private void CashPayment(String payment) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {

            Call<OrderCashChequePaymentMethodResponse> call = apiService.payWeb(restoretoken, new OrderCashChequePaymentMethodResponse(payment));

            progressDialog.show();
            call.enqueue(new Callback<OrderCashChequePaymentMethodResponse>() {
                @Override
                public void onResponse(Call<OrderCashChequePaymentMethodResponse> call, Response<OrderCashChequePaymentMethodResponse> response) {


                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {

                            Intent order = new Intent(ConfirmOrderActivity.this, OrderPlacedActivity.class);
                            order.putExtra("status", "true");
                            startActivity(order);
                            finish();
                        } else {
                            Intent order = new Intent(ConfirmOrderActivity.this, OrderPlacedActivity.class);
                            order.putExtra("status", "false");
                            startActivity(order);
                            finish();

                        }

                    }


                }

                @Override
                public void onFailure(Call<OrderCashChequePaymentMethodResponse> call, Throwable t) {

                    progressDialog.dismiss();
                    Snackbar.make(btnPlaceOrder, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                }
            });

        } else {
            Snackbar.make(btnPlaceOrder, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }


    }


    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }
}
