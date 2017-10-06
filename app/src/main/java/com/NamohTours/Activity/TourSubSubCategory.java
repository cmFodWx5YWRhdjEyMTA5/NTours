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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.NamohTours.Adapter.CategoryAdapter;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Model.TourCategoryDetailResponse;
import com.NamohTours.Model.TourCategoryResponse;
import com.NamohTours.Model.TourProductDetailResponse;
import com.NamohTours.Model.UserLoginRegisterResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.View.ShowAlertDialog;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;
import com.joanzapata.iconify.widget.IconButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.TOKEN_EXPIRY;

public class TourSubSubCategory extends LeftDrawer {


    private static final String TAG = TourSubSubCategory.class.getSimpleName();

    RecyclerView recyclerView;


    SharedPreferences prefs;

    String restoretoken;

    ConnectionDetector cd;

    private ShowAlertDialog showAlertDialog;

    private static String MY_PREFS_NAME = "SHARED";
    private static String TOKEN_KEY = "TOKEN";

    String LogoutUserResult;

    private static String Register_Preference = "RegisterUser";
    private static final String UserRegister = "UserRegistred";
    //    private Context mContext;
    SharedPreferences.Editor editor;
    private SharedPreferences RegisterPrefences;

    ProgressBar tourProgress;

    String success;

    TourCategoryResponse registerResponse;

    // for Sort Dialog
    int selectedElement = -1;
    List<TourCategoryDetailResponse> movies, returnSoretedDataList, getSortedList;

    Integer category_id;

    String CategoryId, Parent_Category_Id;
    Toolbar toolbar;

    private ProgressDialog progressdialog;
    private RelativeLayout badgeLayout;
    private TextView itemMessagesBadgeTextView;
    private IconButton iconButtonMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_tour_sub_sub_caategory);


        getLayoutInflater().inflate(R.layout.activity_tour_sub_sub_caategory, frameLayout);
        // FooterBar.setVisibility(View.GONE);
        HeaderLayout.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.setHomeAsUpIndicator(R.drawable.ic_back_white);



        /*toolbar = (Toolbar)findViewById(R.id.tourSubSubCategory_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        recyclerView = (RecyclerView) findViewById(R.id.tourSubSubCategory_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(TourSubSubCategory.this));

        recyclerView.addItemDecoration(new DividerItemDecoration(TourSubSubCategory.this, DividerItemDecoration.HORIZONTAL));

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        tourProgress = (ProgressBar) findViewById(R.id.tourSubSubCategoryProgress);

        tourProgress.setVisibility(View.VISIBLE);
        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        // get User Register or Not and change value to true or false

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);

        showAlertDialog = new ShowAlertDialog();


        CategoryId = getIntent().getStringExtra("cat_id");
        Parent_Category_Id = getIntent().getStringExtra("parent_id");


        progressdialog = new ProgressDialog(TourSubSubCategory.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);


        registerResponse = new TourCategoryResponse();

        movies = new ArrayList<TourCategoryDetailResponse>();


        if (cd.isConnectingToInternet(getApplicationContext())) {


            // if Category id is not null
            if (CategoryId != null)

            {

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
                                recyclerView.setAdapter(new CategoryAdapter(movies, R.layout.list_item_tour_category, TourSubSubCategory.this, new ClickListener() {
                                    @Override
                                    public void onClick(int position) {


                                        if (cd.isConnectingToInternet(getApplicationContext())) {
                                            TourCategoryDetailResponse res = movies.get(position);


                                            if (res != null) {

                                                String CategoryyId = res.getCategory_id();

                                                Intent product = new Intent(TourSubSubCategory.this, TourProduct.class);
                                                product.putExtra("cat_id", CategoryyId);
                                                product.putExtra("parent_id", Parent_Category_Id);
                                                product.putExtra("sub_parent_id", res.getParent_id());
                                                product.putExtra("Sub", "SubSub");
                                                startActivity(product);


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

                    Intent intent = new Intent(TourSubSubCategory.this, TourSubSubCategory.class);
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
                    Intent account = new Intent(TourSubSubCategory.this, Account.class);
                    startActivity(account);
                    // finish();


                } else {


                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }


                return true;

            case R.id.menu_carts:


                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourSubSubCategory.this, GetCartProductsActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case R.id.menu_order_history:

                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourSubSubCategory.this, UserOrderHistoryActivity.class);
                    startActivity(account);

                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }


                return true;

            case R.id.menu_wishlists:
                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourSubSubCategory.this, TourWishlistProduct.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case R.id.menu_documents:

                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourSubSubCategory.this, UserDocumentActivity.class);
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
       /* Intent home = new Intent(TourSubSubCategory.this,TourSubCategory.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.putExtra("cat_id",Parent_Category_Id);
        startActivity(home);
       */

        finish();
        super.onBackPressed();

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

                        Log.e(TAG, "Logout  : " + response1);
                        //Snackbar.make(recyclerView,"Logout Successfully",Snackbar.LENGTH_SHORT).show();
                        Intent log = new Intent(TourSubSubCategory.this, Login.class);
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

                // Log.e(TAG, "Error : " + t.toString());

                progressdialog.dismiss();
            }
        });


    }
}
