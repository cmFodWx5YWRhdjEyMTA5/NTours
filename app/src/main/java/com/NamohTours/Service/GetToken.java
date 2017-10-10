package com.NamohTours.Service;

import android.content.Context;
import android.content.SharedPreferences;

import com.NamohTours.Model.TokenResponse;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.TOKEN_EXPIRY;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

/**
 * Created by ubuntu on 8/5/17.
 */

public class GetToken {
    // Base 64 encoded clientId:clientSecret string into following and add basdic after encoded string
    private final static String AUTHORIZATION = "Basic ZGVtb19vYXV0aF9jbGllbnQ6ZGVtb19vYXV0aF9zZWNyZXQ=";

    /*private static String MY_PREFS_NAME = "SHARED";
    private static String TOKEN_KEY = "TOKEN";
    private static String TOKEN_EXPIRY="EXPIRY";
    private static String Register_Preference = "RegisterUser";
    private static final String UserRegister = "UserRegistred";*/
    private Context mContext;
    SharedPreferences.Editor editor;
    private SharedPreferences RegisterPrefences;

    SharedPreferences prefs;

    String restoretoken;

    String token, expiry;
    boolean success;

    //Creating Api Interface
    final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


    public GetToken(Context context) {
        this.mContext = context;
    }


    public void gettingToken() {
        //Creating Api Interface
        // ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        Call<TokenResponse> call = apiService.getToken(AUTHORIZATION);

        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

                TokenResponse tokenResponse = response.body();
                token = tokenResponse.getToken();
                expiry = tokenResponse.getExpires();

                editor.putString(TOKEN_KEY, token);
                editor.putString(TOKEN_EXPIRY, expiry);

                editor.commit();


            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

            }
        });
        // return token;
    }


}