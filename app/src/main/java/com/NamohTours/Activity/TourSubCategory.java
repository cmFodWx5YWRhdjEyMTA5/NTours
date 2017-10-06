package com.NamohTours.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.NamohTours.Adapter.SubCategoryAdapter;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Model.TourCategoryDetailResponse;
import com.NamohTours.Model.TourProductDetailResponse;
import com.NamohTours.Model.TourCategoryResponse;
import com.NamohTours.Model.UserLoginRegisterResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.View.ShowAlertDialog;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.TOKEN_EXPIRY;

public class TourSubCategory extends LeftDrawer {


    private static final String TAG = TourSubCategory.class.getSimpleName();
    private static final String UserRegister = "UserRegistred";
    private static String MY_PREFS_NAME = "SHARED";
    private static String TOKEN_KEY = "TOKEN";
    private static String Register_Preference = "RegisterUser";
    SharedPreferences prefs;
    String restoretoken;
    ConnectionDetector cd;
    String LogoutUserResult;
    //    private Context mContext;
    SharedPreferences.Editor editor;
    ProgressBar tourProgress;
    String success;
    TourCategoryResponse registerResponse;
    List<TourCategoryDetailResponse> movies, returnSoretedDataList, getSortedList;
    Integer category_id, pat_cat_id;
    String CategoryId, SubCategoryId;
    private RecyclerView recyclerView;
    private ShowAlertDialog showAlertDialog;
    private SharedPreferences RegisterPrefences;
    private ProgressDialog progressdialog;
    private Menu menu;


    // Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_tour_sub_category);
        getLayoutInflater().inflate(R.layout.activity_tour_sub_category, frameLayout);
        HeaderLayout.setVisibility(View.GONE);
//        FooterBar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.setHomeAsUpIndicator(R.drawable.ic_back_white);


       /* toolbar = (Toolbar)findViewById(R.id.tourSubCategory_toolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        recyclerView = (RecyclerView) findViewById(R.id.tourSubCategory_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(TourSubCategory.this));

        recyclerView.addItemDecoration(new DividerItemDecoration(TourSubCategory.this, DividerItemDecoration.HORIZONTAL));

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        tourProgress = (ProgressBar) findViewById(R.id.tourSubCategoryProgress);

        tourProgress.setVisibility(View.VISIBLE);
        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        // get User Register or Not and change value to true or false

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);


        CategoryId = getIntent().getStringExtra("cat_id");
        SubCategoryId = getIntent().getStringExtra("sub_cat_id");


        progressdialog = new ProgressDialog(TourSubCategory.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);


        registerResponse = new TourCategoryResponse();

        movies = new ArrayList<TourCategoryDetailResponse>();


        if (cd.isConnectingToInternet(getApplicationContext())) {
            // if Category id is not null
            if (CategoryId != null) {
                //Creating Api Interface
                final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


                category_id = Integer.parseInt(CategoryId);

                Call<TourCategoryResponse> call = apiService.getSubCategories(restoretoken, category_id);


                call.enqueue(new Callback<TourCategoryResponse>() {
                    @Override
                    public void onResponse(Call<TourCategoryResponse> call, Response<TourCategoryResponse> response) {

                        registerResponse = response.body();


                        TourProductDetailResponse detailResponse;

                        String sucess, error;

                        if ((sucess = registerResponse.getSuccess()) != null) {

                            if (sucess.equals("true")) {


                                movies = response.body().getTourCategoryDetailResponses();

                                tourProgress.setVisibility(View.GONE);

                                //Log.e(TAG, "Call : " + call.request().url());
                                recyclerView.setAdapter(new SubCategoryAdapter(movies, R.layout.list_item_tour_sub_category, TourSubCategory.this, new ClickListener() {
                                    @Override
                                    public void onClick(int position) {


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

                            if (sucess.equals("false")) {

                                if ((error = registerResponse.getError()) != null) {
                                    tourProgress.setVisibility(View.GONE);
                                    Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show();
                                }

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<TourCategoryResponse> call, Throwable t) {


                        tourProgress.setVisibility(View.GONE);
                        Snackbar.make(recyclerView, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                    }
                });

            }
            // if category id is null
            else {
            }

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

                    Intent intent = new Intent(TourSubCategory.this, TourSubCategory.class);
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


   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        this.menu = menu;
        UpdateWishList();
        return true;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tour_category, menu);//Menu Resource, Menu


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:


                if (cd.isConnectingToInternet(getApplicationContext())) {
                    SignUp();

                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }
                return true;
            case R.id.menu_account:


                if (cd.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourSubCategory.this, Account.class);
                    startActivity(account);
                    // finish();


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }


                return true;


            case R.id.menu_wishlists:
                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourSubCategory.this, TourWishlistProduct.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case R.id.menu_carts:


                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourSubCategory.this, GetCartProductsActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case R.id.menu_order_history:

                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourSubCategory.this, UserOrderHistoryActivity.class);
                    startActivity(account);

                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }


                return true;

            case R.id.menu_documents:

                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourSubCategory.this, UserDocumentActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case android.R.id.home:

                onBackPressed();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();

    }


    public void CheckSubCategory(Integer parent_category_id) {

        progressdialog.show();

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        pat_cat_id = parent_category_id;


        Call<TourCategoryResponse> call = apiService.getSubCategories(restoretoken, pat_cat_id);


        call.enqueue(new Callback<TourCategoryResponse>() {
            @Override
            public void onResponse(Call<TourCategoryResponse> call, Response<TourCategoryResponse> response) {

                registerResponse = response.body();


                String sucess;

                if ((sucess = registerResponse.getSuccess()) != null) {

                    if (sucess.equals("true")) {

                        Log.e(TAG, "Response: " + response.raw());


                        progressdialog.dismiss();
                        Intent description = new Intent(TourSubCategory.this, TourSubSubCategory.class);
                        description.putExtra("cat_id", String.valueOf(pat_cat_id));
                        description.putExtra("parent_id", CategoryId);
                       /* description.putExtra("Sub","Subcat");
                       // description.putExtra("")*/
                        startActivity(description);
                        // finish();

                    }

                    if (sucess.equals("false")) {

                        progressdialog.dismiss();

                        Intent description = new Intent(TourSubCategory.this, TourProduct.class);
                        description.putExtra("cat_id", String.valueOf(pat_cat_id));
                        description.putExtra("parent_id", CategoryId);
                        description.putExtra("Sub", "Sub");
                        startActivity(description);
                        //  finish();

                    }
                }


            }

            @Override
            public void onFailure(Call<TourCategoryResponse> call, Throwable t) {

                progressdialog.dismiss();
                Log.e(TAG, "Error : " + t.toString());
                tourProgress.setVisibility(View.GONE);


            }
        });

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


                        Intent log = new Intent(TourSubCategory.this, Login.class);
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

            }
        });


    }

}

