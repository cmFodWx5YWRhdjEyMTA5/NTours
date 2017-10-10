package com.NamohTours.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.NamohTours.R;

import java.util.HashMap;
import java.util.Map;


import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.PAYMENT;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;


public class OrderPayWebActivity extends AppCompatActivity {

    private static final String TAG = OrderPayWebActivity.class.getSimpleName();
    private SharedPreferences prefs;
    private String restoretoken, url, payment;
    private boolean flag = false;


    private WebView web;
    private Map<String, String> headers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay_web);

        web = (WebView) findViewById(R.id.PaymentWeb);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);
        payment = prefs.getString(PAYMENT, null);


        //   OnlinePayment();


        if (payment.equals("ccavenue")) {

            OnlinePayment();

        } else {

            // get payment sucess using retrofit service

          /*  ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


            if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {

                Call<Void> call = apiService.payWeb(restoretoken,new OrderCashChequePaymentMethodResponse("type"));

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            } else {
                Snackbar.make(web, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
            }*/

        }

    }


    private void OnlinePayment() {

        WebSettings webSettings = web.getSettings();
        // webSettings.setJavaScriptEnabled(true);

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadsImagesAutomatically(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web.setScrollbarFadingEnabled(true);
        web.getSettings().setPluginState(WebSettings.PluginState.ON);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.getSettings().getDomStorageEnabled();


        headers = new HashMap<String, String>();

        url = "http://namohtours.com/api/rest/pay";

        headers.put("Authorization", restoretoken);

        web.loadUrl("http://namohtours.com/api/rest/pay", headers);

        web.setWebViewClient(new MyBrowser());

    }


    private class MyBrowser extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


            if (url.contains("checkout/success")) {
                Intent sucess = new Intent(OrderPayWebActivity.this, OrderPlacedActivity.class);
                sucess.putExtra("status", "true");
                startActivity(sucess);
                finish();
            } else if (url.contains("checkout/cart")) {

                Intent fail = new Intent(OrderPayWebActivity.this, OrderPlacedActivity.class);
                fail.putExtra("status", "false");
                startActivity(fail);
                finish();

            } else if (url.contains("checkout/checkout")) {
                Intent fail = new Intent(OrderPayWebActivity.this, OrderPlacedActivity.class);
                fail.putExtra("status", "false");
                startActivity(fail);
                finish();
            }


        }


    }

    @Override
    public void onBackPressed() {

        Intent tru = new Intent(OrderPayWebActivity.this, TourParentCategory.class);
        tru.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(tru);
        finish();
    }


}


