package com.NamohTours.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.NamohTours.Adapter.ProductAdapter;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.WishListClick;
import com.NamohTours.Model.TourProductDetailResponse;
import com.NamohTours.Model.TourProductResponse;
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
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

public class SearchResults extends AppCompatActivity {


    private static final String TAG = SearchResults.class.getSimpleName();

    RecyclerView recyclerView;
    ProgressBar tourProgress;
    TextView txtSearachNotFound;

    SharedPreferences prefs;
    private SharedPreferences RegisterPrefences;

    String restoretoken;

    ConnectionDetector cd;

    List<TourProductDetailResponse> searchList;
    TourProductResponse searchResponse;
    String searchQuery;
    Toolbar searchResultsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        searchResultsToolbar = (Toolbar) findViewById(R.id.search_results_toolbar);
        setSupportActionBar(searchResultsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.tour_Search_Prodcut_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(SearchResults.this));

        recyclerView.addItemDecoration(new DividerItemDecoration(SearchResults.this, DividerItemDecoration.HORIZONTAL));
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        tourProgress = (ProgressBar) findViewById(R.id.tourSearchProduct_Progress);


        txtSearachNotFound = (TextView) findViewById(R.id.txtSearchNotfound);


        tourProgress.setVisibility(View.VISIBLE);
        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        // get User Register or Not and change value to true or false

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();

        searchQuery = bundle.getString("search");


        //Creating Api Interface
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Log.e(TAG, "Search Text : " + searchQuery);


        searchList = new ArrayList<TourProductDetailResponse>();
        searchResponse = new TourProductResponse();


        if (searchQuery != null) {


            if (cd.isConnectingToInternet(getApplicationContext())) {

                Call<TourProductResponse> call = apiService.getSearchProduct(restoretoken, searchQuery);


                call.enqueue(new Callback<TourProductResponse>() {
                    @Override
                    public void onResponse(Call<TourProductResponse> call, Response<TourProductResponse> response) {

                        searchResponse = response.body();


                        TourProductDetailResponse detailResponse;

                        String sucess, error;

                        if ((sucess = searchResponse.getSuccess()) != null) {

                            if (sucess.equals("true")) {

                                Log.e(TAG, "Response: " + response.raw());

                                searchList = response.body().getData();

                                tourProgress.setVisibility(View.GONE);

                                //Log.e(TAG, "Call : " + call.request().url());
                                recyclerView.setAdapter(new ProductAdapter(searchList, R.layout.list_item_tour_product, SearchResults.this, new ClickListener() {
                                    @Override
                                    public void onClick(int position) {

                                        TourProductDetailResponse res = searchList.get(position);


                                        if (res != null) {


                                            String product_id = res.getId();
                                            String price = res.getPrice();
                                            String specialPrice = res.getSpecial();

                                            Intent description = new Intent(SearchResults.this, ProductDescription.class);
                                            description.putExtra("product_id", product_id);
                                            description.putExtra("Sub", "Search");
                                            description.putExtra("price", price);
                                            description.putExtra("special", specialPrice);

                                            startActivity(description);
                                            // finish();


                                        }


                                    }
                                },
                                        new WishListClick() {

                                            @Override
                                            public void onClick(int position) {
                                                // Snackbar.make(recyclerView,"Product added to wishlist!",Snackbar.LENGTH_LONG).show();

                                            }

                                        }));



                               /* @Override
                                public void onClick(int position) {


                                    TourProductDetailResponse res = searchList.get(position);


                                    if(res !=null)
                                    {


                                        String product_id = res.getId();

                                        Intent description = new Intent(SearchResults.this, ProductDescription.class);
                                        description.putExtra("product_id",product_id);
                                        description.putExtra("Sub","Search");
                                        startActivity(description);
                                       // finish();


                                    }




                                }
                            });*/


                            }

                            if (sucess.equals("false")) {

                                if ((error = searchResponse.getError()) != null) {
                                    tourProgress.setVisibility(View.GONE);
                                    txtSearachNotFound.setVisibility(View.VISIBLE);
                                    Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show();
                                }

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<TourProductResponse> call, Throwable t) {

                        Log.e(TAG, "Error : " + t.toString());
                        tourProgress.setVisibility(View.GONE);

                    }
                });


            } else {
                tourProgress.setVisibility(View.GONE);

                Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

            }


            // RecyclerView On OItem click goto ProductDescription
/*

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    TourProductDetailResponse sample = searchResponse.getData().get(position);

                    String desciption = sample.getDescription();
                    String product_id = sample.getId();

                    Intent description = new Intent(SearchResults.this, ProductDescription.class);
                    description.putExtra("product_id",product_id);
                    description.putExtra("Sub","Search");
                    startActivity(description);
                    finish();


                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


*/


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


//        super.onBackPressed();

        // Fragment fragment = new Fragment();


       /* Intent search = new Intent(SearchResults.this,Search.class);
        startActivity(search);
        finish();*/

    }
}
