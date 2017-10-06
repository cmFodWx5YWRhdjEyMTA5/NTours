package com.NamohTours.Activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;

public class WebUrl extends AppCompatActivity {

    WebView web;
    String WebUrl, Notificationurl, currency, weather, timezone, world, about, privacy, terms, quiz, games, event;
    ConnectionDetector cd;
    Toolbar toolbar;
    TextView txtNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        // getLayoutInflater().inflate(R.layout.activity_temp,frameLayout);

        toolbar = (Toolbar) findViewById(R.id.WebToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // notification url
        Notificationurl = getIntent().getStringExtra("url");

        // for flight and hotel url
        WebUrl = getIntent().getStringExtra("Web");

        //http://www.namohtours.com/

        about = "http://namohtours.com/about.html";
        terms = "http://namohtours.com/terms.html";

        privacy = " http://namohtours.com/policy.html";

        currency = "https://www.google.com/finance/converter";
        weather = "https://www.timeanddate.com/weather/";
        timezone = "https://www.timeanddate.com/worldclock/converter.html";
        world = "http://www.worldatlas.com/";
        quiz = "https://goo.gl/forms/LteqF7SeV7kqLPbB3";
        games = "http://www.baptistebrunet.com/games/fruit_salad/";

        event = "https://goo.gl/forms/aZX64VWfRcD5PqC83";

        // world="http://hextris.io/";


        web = (WebView) findViewById(R.id.tempWebview);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.getSettings().setLoadsImagesAutomatically(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web.setScrollbarFadingEnabled(true);
        web.getSettings().setPluginState(WebSettings.PluginState.ON);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.getSettings().getDomStorageEnabled();

        if (cd.isConnectingToInternet(getApplicationContext())) {


            if (WebUrl.equals("notification")) {
                web.loadUrl(Notificationurl);
            }

            if (WebUrl.equals("about")) {
                web.loadUrl(about);
            }

            if (WebUrl.equals("privacy")) {
                web.loadUrl(privacy);
            }

            if (WebUrl.equals("terms")) {
                web.loadUrl(terms);
            }

            if (WebUrl.equals("currency")) {
                web.loadUrl(currency);
            }

            if (WebUrl.equals("weather")) {
                web.loadUrl(weather);
            }

            if (WebUrl.equals("timezone")) {
                web.loadUrl(timezone);
            }

            if (WebUrl.equals("world")) {
                web.loadUrl(world);
            }

            if (WebUrl.equals("quiz")) {
                web.loadUrl(quiz);
            }

            if (WebUrl.equals("games")) {
                web.loadUrl(games);
            }

            if (WebUrl.equals("event")) {
                web.loadUrl(event);
            }


        } else {
            Snackbar.make(web, "Please turn on your mobile data or wifi", Snackbar.LENGTH_LONG).show();
        }

        web.setWebViewClient(new MyBrowser());
    }


    public class MyBrowser extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView web, String url) {

            web.loadUrl(url);

            return true;

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
