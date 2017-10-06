package com.NamohTours.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

import com.NamohTours.Adapter.OrderHistoryDetailViewAdapter;
import com.NamohTours.Model.OrderHistoryMainResponse;
import com.NamohTours.Model.OrderHistoryOptionResponse;
import com.NamohTours.Model.OrderHistoryProductResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryDetailActivity extends AppCompatActivity {

    private static final String TAG = OrderHistoryDetailActivity.class.getSimpleName();

    private RecyclerView OHRecyclerView;
    private LinearLayout LLOHPrice;
    private TextView txtOHID, txtOHDate, txtOHPayment, txtOHStatus;
    private SharedPreferences preferences;
    private String restroreToken, Order_Id;
    private List<OrderHistoryProductResponse> orderHistoryProductResponses;
    private List<OrderHistoryOptionResponse> orderHistoryOptionResponses;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.OrderHistoryViewToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        OHRecyclerView = (RecyclerView) findViewById(R.id.OrderHistoryViewRecyler);

        OHRecyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryDetailActivity.this));

        OHRecyclerView.addItemDecoration(new DividerItemDecoration(OrderHistoryDetailActivity.this, DividerItemDecoration.HORIZONTAL));

        ViewCompat.setNestedScrollingEnabled(OHRecyclerView, false);



        progressDialog = new ProgressDialog(OrderHistoryDetailActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        txtOHID = (TextView) findViewById(R.id.txtOHId);
        txtOHDate = (TextView) findViewById(R.id.txtOHDate);
        txtOHPayment = (TextView) findViewById(R.id.txtOHPayment);
        txtOHStatus = (TextView) findViewById(R.id.txtOHStatus);

        LLOHPrice = (LinearLayout) findViewById(R.id.llOHPrice);


        preferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restroreToken = preferences.getString(TOKEN_KEY, null);

        Order_Id = getIntent().getStringExtra("orderId");


        orderHistoryProductResponses = new ArrayList<OrderHistoryProductResponse>();
        orderHistoryOptionResponses = new ArrayList<OrderHistoryOptionResponse>();


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<OrderHistoryMainResponse> call = apiInterface.orderHistorybyOrderId(restroreToken, Order_Id);

            progressDialog.show();
            call.enqueue(new Callback<OrderHistoryMainResponse>() {
                @Override
                public void onResponse(Call<OrderHistoryMainResponse> call, Response<OrderHistoryMainResponse> response) {


                    if (response.body().getSuccess()) {
                        progressDialog.dismiss();


                        // Dynamic Price and GST
                        for (int i = 0; i < response.body().getData().getTotals().size(); i++) {


                            if (response.body().getData().getTotals().get(i).getTitle().equals("Total")) {
                                TextView txt = new TextView(OrderHistoryDetailActivity.this);
                                txt.setText(response.body().getData().getTotals().get(i).getTitle() + ":\t\t"
                                        + response.body().getData().getTotals().get(i).getText());

                                //txt.setTextSize(getResources().getDimension(R.dimen.price_text_size));

                                txt.setTextColor(getResources().getColor(R.color.colorPrimary));
                                txt.setTypeface(null, Typeface.BOLD);
                                txt.setGravity(Gravity.CENTER);

                                LLOHPrice.addView(txt);
                            } else {

                                TextView txt = new TextView(OrderHistoryDetailActivity.this);
                                txt.setText(response.body().getData().getTotals().get(i).getTitle() + ":\t\t"
                                        + response.body().getData().getTotals().get(i).getText());
                                txt.setGravity(Gravity.CENTER);
                                LLOHPrice.addView(txt);
                            }

                        }


                        txtOHID.setText(response.body().getData().getOrder_id());
                        txtOHDate.setText(response.body().getData().getDate_modified().substring(0, 11));
                        txtOHPayment.setText(response.body().getData().getPayment_method());
                        txtOHStatus.setText(response.body().getData().getOrder_status());

                        // User selected options for product
                        for (int j = 0; j < response.body().getData().getProducts().size(); j++) {

                            for (int k = 0; k < response.body().getData().getProducts().get(j).getOption().size(); k++) {
                                orderHistoryOptionResponses.add(response.body().getData().getProducts().get(j).getOption().get(k));

                            }


                        }


                        orderHistoryProductResponses = response.body().getData().getProducts();


                        OHRecyclerView.setAdapter(new OrderHistoryDetailViewAdapter(orderHistoryProductResponses, R.layout.list_item_order_history_detail, OrderHistoryDetailActivity.this));


                    } else {

                        // If orders are not done yet!
                        OHRecyclerView.setVisibility(View.GONE);
                        Snackbar.make(OHRecyclerView, response.body().getWarning(), Snackbar.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<OrderHistoryMainResponse> call, Throwable t) {

                    Snackbar.make(OHRecyclerView, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                }
            });


        } else {
            Snackbar.make(OHRecyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
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
