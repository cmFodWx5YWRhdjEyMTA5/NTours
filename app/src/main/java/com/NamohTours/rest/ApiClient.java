package com.NamohTours.rest;

import android.os.Environment;


import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    //public static final String BASE_URL = "http://api.themoviedb.org/3/";
    //public static  String BASE_URL = "http://namohtours.com/";

    // public static String BASE_URL = "http://opencartoauth.opencart-api.com:80/api/rest/";
    public static String BASE_URL = "http://namohtours.com/api/rest/";
    //

    private static Retrofit retrofit = null;
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL);


    public static Retrofit getClient() {
        if (retrofit == null) {


            // FOR Logging
           /* HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
             httpClient.addInterceptor(logging);// <-- this is the important line!
             */


            File httpCacheDirectory = new File(Environment.getDownloadCacheDirectory(), "HttpCache");// Here to facilitate the file directly on the SD Kagan catalog HttpCache in ， Generally put in context.getCacheDir() in
            int cacheSize = 10 * 1024 * 1024;// Set cache file size 10M
            Cache cache = new Cache(httpCacheDirectory, cacheSize);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(logging);
            httpClient.connectTimeout(300, TimeUnit.SECONDS);
            httpClient.readTimeout(300, TimeUnit.SECONDS);
            httpClient.cache(cache);


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


}
