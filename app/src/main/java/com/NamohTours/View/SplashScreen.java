package com.NamohTours.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/*
import info.androidhive.retrofit.R;
import info.androidhive.retrofit.model.TokenResponse;
import info.androidhive.retrofit.rest.ApiClient;
import info.androidhive.retrofit.rest.ApiInterface;
*/

import com.NamohTours.Activity.Login;
import com.NamohTours.Model.TokenResponse;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;
import com.NamohTours.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.AUTHORIZATION;
import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.TOKEN_EXPIRY;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    private static final String TAG = SplashScreen.class.getSimpleName();


    SharedPreferences prefs;

    String restoretoken;


    ConnectionDetector cd;

    ProgressBar spalshProgress;
    SharedPreferences.Editor editor;

    ImageView NamohISO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        spalshProgress = (ProgressBar) findViewById(R.id.splashProgress);

        NamohISO = (ImageView) findViewById(R.id.namohISO);


        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        //Creating Api Interface
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        final Animation ISOlogoAnimation = AnimationUtils.loadAnimation(this, R.anim.left_right_animation);

        if (cd.isConnectingToInternet(getApplicationContext())) {


            // if token is not in Shared Preference
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

/* if (loginemail.isEmpty() || loginpassword.isEmpty())
                                    {
                                        Snackbar.make(btnLogin, "Please Fill Login Details", Snackbar.LENGTH_LONG).show();
                                    }

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date()); // Now use today date.
                            String output1 = sdf.format(c.getTime());
                            System.out.println(output1);
                            c.add(Calendar.DATE, 30); // Adding 30 days
                            String output = sdf.format(c.getTime());
                            System.out.println(output);
*/



                            NamohISO.startAnimation(ISOlogoAnimation);
                            ISOlogoAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    NamohISO.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                    Intent intent = new Intent(SplashScreen.this, Login.class);
                                    startActivity(intent);
                                    finish();

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });


                        }


                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {


                        Toast.makeText(getApplicationContext(), "Please Try Later...", Toast.LENGTH_LONG).show();

                        //  Snackbar.make(spalshProgress,"Some Problem to Load",Snackbar.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } else {


                NamohISO.startAnimation(ISOlogoAnimation);

                NamohISO.setVisibility(View.VISIBLE);
                ISOlogoAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // NamohISO.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {


                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                        finish();

                        /*new Handler().postDelayed(new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                // This method will be executed once the timer is over
                                // Start your app main activity
                                Intent i = new Intent(getApplicationContext(), Login.class);
                                startActivity(i);
                                // close this activity
                                finish();
                            }
                        }, SPLASH_TIME_OUT);
*/
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                // Log.e(TAG,"Token in shared Preferences :"+restoretoken);

            }


        } else {
            NamohISO.startAnimation(ISOlogoAnimation);

            NamohISO.setVisibility(View.VISIBLE);
            ISOlogoAnimation.setAnimationListener(new Animation.AnimationListener() {


                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();

                  /*  new Handler().postDelayed(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            // This method will be executed once the timer is over
                            // Start your app main activity
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);
                            // close this activity
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
*/
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

}
/*  new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);*/