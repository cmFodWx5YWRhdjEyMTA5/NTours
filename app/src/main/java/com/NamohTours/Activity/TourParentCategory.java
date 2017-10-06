package com.NamohTours.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.NamohTours.Adapter.CategoryAdapter;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Model.TokenResponse;
import com.NamohTours.Model.TourCategoryDetailResponse;
import com.NamohTours.Model.TourCategoryResponse;
import com.NamohTours.Model.TourProductDetailResponse;
import com.NamohTours.Model.UserLoginRegisterResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.*;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourParentCategory extends LeftDrawer {


    private static final String TAG = TourParentCategory.class.getSimpleName();

    private RecyclerView recyclerView;


    SharedPreferences prefs;

    String restoretoken;

    private ConnectionDetector cd;

    SharedPreferences.Editor editor;
    private SharedPreferences RegisterPrefences;

    ProgressBar tourProgress;


    TourCategoryResponse registerResponse;

    List<TourCategoryDetailResponse> dummyCategory, movies;

    ProgressDialog progressdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_tour_category);


        getLayoutInflater().inflate(R.layout.activity_tour_category, frameLayout);

//        FooterBar.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(TourParentCategory.this));


        recyclerView.addItemDecoration(new DividerItemDecoration(TourParentCategory.this, DividerItemDecoration.HORIZONTAL));

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        tourProgress = (ProgressBar) findViewById(R.id.tourCategory_Progress);

        // for update wish count
        // invalidateOptionsMenu();


        tourProgress.setVisibility(View.VISIBLE);
        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        // get User Register or Not and change value to true or false

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);


        progressdialog = new ProgressDialog(TourParentCategory.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);


        // Set Wishlist counter Badge


        //  invalidateOptionsMenu();


        registerResponse = new TourCategoryResponse();

        movies = new ArrayList<TourCategoryDetailResponse>();


        //Creating Api Interface
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            Call<TourCategoryResponse> call = apiService.getTourCategory(restoretoken);


            call.enqueue(new Callback<TourCategoryResponse>() {
                @Override
                public void onResponse(Call<TourCategoryResponse> call, Response<TourCategoryResponse> response) {

                    registerResponse = response.body();


                    TourProductDetailResponse detailResponse;

                    String sucess;


                    if (response.body() != null) {


                        if ((sucess = registerResponse.getSuccess()) != null) {

                            if (sucess.equals("true")) {


                                if (response.body().getTourCategoryDetailResponses() != null) {

                                    dummyCategory = response.body().getTourCategoryDetailResponses();

                                    for (int i = 0; i < dummyCategory.size(); i++) {


                                        if ((dummyCategory.get(i).getCategory_id().equals("325")) || (dummyCategory.get(i).getCategory_id().equals("179")) || (dummyCategory.get(i).getCategory_id().equals("326"))) {
                                            // if Passport / Visa / Bookign dont add in category list
                                        } else {
                                            movies.add(dummyCategory.get(i));
                                        }

                                    }


                                }


                                tourProgress.setVisibility(View.GONE);

                                recyclerView.setAdapter(new CategoryAdapter(movies, R.layout.list_item_tour_category, TourParentCategory.this, new ClickListener() {
                                    @Override
                                    public void onClick(int position) {


                                        Log.e(TAG, "onResponse: Product Id " + movies.get(position).getCategory_id());

                                        if (cd.isConnectingToInternet(getApplicationContext())) {
                                            TourCategoryDetailResponse res = movies.get(position);


                                            if (res != null) {

                                                String CategoryyId = res.getCategory_id();


                                                CheckSubCategory(Integer.parseInt(CategoryyId));


                                            }
                                        } else {
                                            txtInternet.setVisibility(View.VISIBLE);
                                            tourProgress.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.GONE);

                                            Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                                        }


                                    }
                                }));


                            }
                        }

                        if ((sucess = registerResponse.getStatusCode()) != null) {

                            if (sucess.equals("401")) {


                                Call<TokenResponse> call1 = apiService.getRefreshToken(AUTHORIZATION, new TokenResponse(restoretoken));


                                call1.enqueue(new Callback<TokenResponse>() {
                                    @Override
                                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

                                        TokenResponse registerResponse = response.body();


                                        String response1, token;


                                        if ((response1 = registerResponse.getToken()) != null) {
                                            token = registerResponse.getToken();

                                            editor.putString(TOKEN_KEY, token);
                                            editor.putString(TOKEN_EXPIRY, registerResponse.getExpires());
                                            editor.commit();

                                            restoretoken = token;


                                            Intent start = new Intent(TourParentCategory.this, TourParentCategory.class);
                                            startActivity(start);
                                            finish();


                                        }


                                    }

                                    @Override
                                    public void onFailure(Call<TokenResponse> call, Throwable t) {

                                    }
                                });


                            }
                        }


                    } else {
                        tourProgress.setVisibility(View.GONE);
                        Snackbar.make(recyclerView, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onFailure(Call<TourCategoryResponse> call, Throwable t) {


                    tourProgress.setVisibility(View.GONE);
                    Snackbar.make(recyclerView, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
                }
            });


        } else {

            txtInternet.setVisibility(View.VISIBLE);
            tourProgress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();


        }


        txtInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cd.isConnectingToInternet(getApplicationContext())) {

                    Intent intent = new Intent(TourParentCategory.this, TourParentCategory.class);
                    startActivity(intent);
                    finish();

                } else {
                    txtInternet.setVisibility(View.VISIBLE);
                    tourProgress.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.tour_category, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.menu_carts:


                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourParentCategory.this, GetCartProductsActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case R.id.menu_logout:


                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    SignUp();

                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }
                return true;
            case R.id.menu_account:


                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourParentCategory.this, Account.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }


                return true;


            case R.id.menu_order_history:

                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourParentCategory.this, UserOrderHistoryActivity.class);
                    startActivity(account);

                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }


                return true;



            case R.id.menu_wishlists:


                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourParentCategory.this, TourWishlistProduct.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


            case R.id.menu_documents:

                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourParentCategory.this, UserDocumentActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStop() {

//        sliderShow.stopAutoCycle();
        super.onStop();
    }

    private void SignUp() {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        progressdialog.show();

        Call<UserLoginRegisterResponse> call = apiService.logoutUser(restoretoken);


        call.enqueue(new Callback<UserLoginRegisterResponse>() {
            @Override
            public void onResponse(Call<UserLoginRegisterResponse> call, Response<UserLoginRegisterResponse> response) {

                UserLoginRegisterResponse registerResponse = response.body();

                String response1;


                if ((response1 = registerResponse.getSuccess()) != null)

                {

                    // Log.e(TAG, "Response : " + response1);

                    if (response1.equals("true")) {

                        progressdialog.dismiss();

                        SharedPreferences.Editor editor = RegisterPrefences.edit();
                        editor.putBoolean(UserRegister, false);
                        editor.commit();

                        SharedPreferences.Editor editor1 = prefs.edit();
                        editor1.remove(TOKEN_KEY);
                        editor1.remove(TOKEN_EXPIRY);
                        editor1.commit();


                        //Snackbar.make(recyclerView,"Logout Successfully",Snackbar.LENGTH_SHORT).show();
                        Intent log = new Intent(TourParentCategory.this, Login.class);
                        startActivity(log);
                        finish();
                    }

                } else {
                    progressdialog.dismiss();
                    Snackbar.make(recyclerView, "Sorry , Please Try Later..!!", Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserLoginRegisterResponse> call, Throwable t) {

                progressdialog.dismiss();
                // Log.e(TAG, "Error : " + t.toString());
            }
        });


    }


    public void CheckSubCategory(final Integer parent_category_id) {

        progressdialog.show();

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<TourCategoryResponse> call = apiService.getSubCategories(restoretoken, parent_category_id);


        call.enqueue(new Callback<TourCategoryResponse>() {
            @Override
            public void onResponse(Call<TourCategoryResponse> call, Response<TourCategoryResponse> response) {

                registerResponse = response.body();


                String sucess;

                if ((sucess = registerResponse.getSuccess()) != null) {

                    if (sucess.equals("true")) {


                        progressdialog.dismiss();
                        Intent description = new Intent(TourParentCategory.this, TourSubCategory.class);
                        //description.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        description.putExtra("cat_id", String.valueOf(parent_category_id));
                        startActivity(description);
                        // finish();

                    }

                    if (sucess.equals("false")) {
                        progressdialog.dismiss();

                        Intent description = new Intent(TourParentCategory.this, TourProduct.class);
                        //  description.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        description.putExtra("cat_id", String.valueOf(parent_category_id));
                        description.putExtra("Sub", "Parent");
                        startActivity(description);
                        //finish();

                    }
                }


            }

            @Override
            public void onFailure(Call<TourCategoryResponse> call, Throwable t) {

                tourProgress.setVisibility(View.GONE);
                progressdialog.dismiss();

            }
        });

    }

}

