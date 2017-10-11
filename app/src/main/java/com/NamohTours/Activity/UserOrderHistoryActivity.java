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
import android.widget.TextView;

import com.NamohTours.Adapter.UserOrderHistoryAdapter;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.WishListClick;
import com.NamohTours.Model.GetUserOrderHistory;
import com.NamohTours.Model.GetUserOrderHistoryDetail;
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
import static com.NamohTours.Service.Prefs.UserID;

public class UserOrderHistoryActivity extends AppCompatActivity {


    public static final String TAG = UserOrderHistoryActivity.class.getSimpleName();
    private String restoretoken, CustomerID, orderId;
    private SharedPreferences prefs, RegisterPrefences;
    private TextView txtOrderHistory;
    private RecyclerView orderHistoryRecyler;
    private List<GetUserOrderHistoryDetail> orderHistoryDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.OrderHistoryToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderHistoryRecyler = (RecyclerView) findViewById(R.id.orderHistoryRecyler);
        orderHistoryRecyler.setLayoutManager(new LinearLayoutManager(UserOrderHistoryActivity.this));
        orderHistoryRecyler.addItemDecoration(new DividerItemDecoration(UserOrderHistoryActivity.this, DividerItemDecoration.HORIZONTAL));
        ViewCompat.setNestedScrollingEnabled(orderHistoryRecyler, false);

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);

        CustomerID = RegisterPrefences.getString(UserID, null);

        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        txtOrderHistory = (TextView) findViewById(R.id.txtOrderHistory);

        orderHistoryDetails = new ArrayList<GetUserOrderHistoryDetail>();


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            Call<GetUserOrderHistory> call = apiService.userOrderHistory(restoretoken, CustomerID);

            call.enqueue(new Callback<GetUserOrderHistory>() {
                @Override
                public void onResponse(Call<GetUserOrderHistory> call, final Response<GetUserOrderHistory> response) {


                    if (response.body().getSuccess()) {

                        if ((response.body().getData().size() > 0) && (response.body().getData() != null)) {

                            orderHistoryDetails = response.body().getData();

                            orderHistoryRecyler.setAdapter(new UserOrderHistoryAdapter(orderHistoryDetails, R.layout.list_item_order_history, UserOrderHistoryActivity.this, new ClickListener() {
                                @Override
                                public void onClick(int position) {

                                    // for Order History Details View
                                    orderId = response.body().getData().get(position).getOrder_id();

                                    Intent orderView = new Intent(UserOrderHistoryActivity.this, OrderHistoryDetailActivity.class);
                                    orderView.putExtra("orderId", orderId);
                                    startActivity(orderView);

                                }
                            },

                                    // For documents View
                                    new WishListClick() {
                                        @Override
                                        public void onClick(int position) {

                                            orderId = response.body().getData().get(position).getOrder_id();

                                            Intent orderView = new Intent(UserOrderHistoryActivity.this, UserDocumentbyOrderIdActivity.class);
                                            orderView.putExtra("order_id", orderId);
                                            startActivity(orderView);


                                        }
                                    }

                            ));


                        } else {

                            orderHistoryRecyler.setVisibility(View.GONE);
                            txtOrderHistory.setVisibility(View.VISIBLE);
                            //  Snackbar.make(orderHistoryRecyler, "No orders found!", Snackbar.LENGTH_LONG).show();
                        }


                    } else {
                        orderHistoryRecyler.setVisibility(View.GONE);
                        txtOrderHistory.setVisibility(View.VISIBLE);
                        // Snackbar.make(orderHistoryRecyler, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onFailure(Call<GetUserOrderHistory> call, Throwable t) {

                    Snackbar.make(orderHistoryRecyler, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                }
            });


        } else {
            Snackbar.make(orderHistoryRecyler, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
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
