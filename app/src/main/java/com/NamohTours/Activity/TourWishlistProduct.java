package com.NamohTours.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.NamohTours.Adapter.WishlistProductAdapter;
import com.NamohTours.Database.DBManager;
import com.NamohTours.Database.DatabaseHelper;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.WishListClick;
import com.NamohTours.Model.CoampreProductResponse;
import com.NamohTours.Model.ComapreProductDetailResponse;
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
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

public class TourWishlistProduct extends AppCompatActivity {


    private static final String TAG = TourWishlistProduct.class.getSimpleName();
    public DBManager dbManager;
    List<ComapreProductDetailResponse> ress;
    private RecyclerView productWishListRecyclerView;
    private SharedPreferences prefs;
    private String restoretoken;
    private ConnectionDetector cd;
    private Toolbar toolbar;
    private ProgressBar WishListProgress;
    //private List<GetWishlistProductsListResponse> wishList;
    private TextView noWishList;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_wishlist_product);

        toolbar = (Toolbar) findViewById(R.id.tourProductToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wishlist");

        productWishListRecyclerView = (RecyclerView) findViewById(R.id.tour_WishlistProdcut_recyclerview);
        productWishListRecyclerView.setLayoutManager(new LinearLayoutManager(TourWishlistProduct.this));

        productWishListRecyclerView.addItemDecoration(new DividerItemDecoration(TourWishlistProduct.this, DividerItemDecoration.HORIZONTAL));

        noWishList = (TextView) findViewById(R.id.txtNoWishlistProdcut);
        WishListProgress = (ProgressBar) findViewById(R.id.tourWishlistProduct_Progress);
        WishListProgress.setVisibility(View.VISIBLE);
        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);


        dbManager = new DBManager(TourWishlistProduct.this);
        dbManager.open();
        final Cursor cursor = dbManager.getWishList();

        // get Data from notification
        dbHelper = new DatabaseHelper(getApplicationContext());
        database = dbHelper.getWritableDatabase();


        if ((dbManager.getWish() != null) && (dbManager.getWish().size() > 0)) {
            String allIds = TextUtils.join(",", dbManager.getWish());


            if (cd.isConnectingToInternet(getApplicationContext())) {
                final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

                Call<CoampreProductResponse> call = apiService.getCompareProduct(restoretoken, allIds);
                call.enqueue(new Callback<CoampreProductResponse>() {
                    @Override
                    public void onResponse(Call<CoampreProductResponse> call, Response<CoampreProductResponse> response) {

                        WishListProgress.setVisibility(View.GONE);


                        if ((response.body().getWishLisDetailResponse() != null) && (response.body().getWishLisDetailResponse().size() > 0)) {

                            ress = new ArrayList<ComapreProductDetailResponse>(response.body().getWishLisDetailResponse().values());

                        } else {

                            WishListProgress.setVisibility(View.GONE);
                            productWishListRecyclerView.setVisibility(View.GONE);
                            noWishList.setVisibility(View.VISIBLE);


                        }


                        //   WishlistProductAdapter  wishAdapter = new WishlistProductAdapter(ress, R.layout.list_item_tour_wishlist_product, TourWishlistProduct.this, new ClickListener() {

                        productWishListRecyclerView.setAdapter(new WishlistProductAdapter(ress, R.layout.list_item_tour_wishlist_product, TourWishlistProduct.this, new ClickListener() {
                            @Override
                            public void onClick(int position) {

                                String product_id = ress.get(position).getProduct_id();
                                String price = ress.get(position).getPrice();
                                String specialPrice = ress.get(position).getSpecial();

                                Intent description = new Intent(TourWishlistProduct.this, ProductDescription.class);
                                description.putExtra("product_id", product_id);
                                description.putExtra("price", price);
                                description.putExtra("special", specialPrice);
                                startActivity(description);
                            }
                        },


                                new WishListClick() {
                                    @Override
                                    public void onClick(int position) {

                                        //   ress.remove(position);



                            }
                                }

                        ));


                    }

                    @Override
                    public void onFailure(Call<CoampreProductResponse> call, Throwable t) {

                        WishListProgress.setVisibility(View.GONE);
                        // noWishList.setVisibility(View.VISIBLE);
                        Snackbar.make(productWishListRecyclerView, "Sorry,Please try later!", Snackbar.LENGTH_LONG).show();
                        productWishListRecyclerView.setVisibility(View.GONE);

                    }
                });


            } else {
                WishListProgress.setVisibility(View.GONE);
                productWishListRecyclerView.setVisibility(View.GONE);

                Snackbar.make(productWishListRecyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();


            }

        } else {

            WishListProgress.setVisibility(View.GONE);
            noWishList.setVisibility(View.VISIBLE);
            productWishListRecyclerView.setVisibility(View.GONE);
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

        super.onBackPressed();
        finish();

    }
}
