package com.NamohTours.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NamohTours.Adapter.CartProductAdapter;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.AddCartQtyClickListner;
import com.NamohTours.Interface.SubtractCartQtyClickListner;
import com.NamohTours.Model.DeleteCartProductResponse;
import com.NamohTours.Model.ProductGetCartProductsList;
import com.NamohTours.Model.ProductGetCartResponse;
import com.NamohTours.Model.UpdateCartProductQuantity;
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

public class GetCartProductsActivity extends AppCompatActivity {

    private static final String TAG = GetCartProductsActivity.class.getSimpleName();
    private RecyclerView cartProductsRecylerview;
    private Button btnCheckout;
    private TextView txtCart;
    private LinearLayout llPrice;
    private SharedPreferences prefs;
    private String restoretoken, productKey;
    private ConnectionDetector cd;
    private SharedPreferences.Editor editor;
    private List<ProductGetCartProductsList> cartProductsList;
    private Integer productQty;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cart_products);


        Toolbar toolbar = (Toolbar) findViewById(R.id.cartProductToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cartProductsRecylerview = (RecyclerView) findViewById(R.id.cart_list);
        cartProductsRecylerview.setLayoutManager(new LinearLayoutManager(GetCartProductsActivity.this));
        cartProductsRecylerview.addItemDecoration(new DividerItemDecoration(GetCartProductsActivity.this, DividerItemDecoration.HORIZONTAL));
        ViewCompat.setNestedScrollingEnabled(cartProductsRecylerview, false);

        llPrice = (LinearLayout) findViewById(R.id.llPrice);

        txtCart = (TextView) findViewById(R.id.txtCart);
        btnCheckout = (Button) findViewById(R.id.btnChekout);

        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);


        progressDialog = new ProgressDialog(GetCartProductsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        cartProductsList = new ArrayList<ProductGetCartProductsList>();


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            Call<ProductGetCartResponse> responseCall = apiService.getCartProducts(restoretoken);

            responseCall.enqueue(new Callback<ProductGetCartResponse>() {
                @Override
                public void onResponse(Call<ProductGetCartResponse> call, final Response<ProductGetCartResponse> response) {


                    if (response.body().getSuccess().equals("true")) {

                        if ((response.body().getData().getCartProductsList() != null) && (response.body().getData().getCartProductsList().size() > 0)) {
                            cartProductsList = response.body().getData().getCartProductsList();


                            for (int i = 0; i < response.body().getData().getPriceResponse().size(); i++) {


                                if (response.body().getData().getPriceResponse().get(i).getTitle().equals("Total")) {
                                    TextView txt = new TextView(GetCartProductsActivity.this);
                                    txt.setText(response.body().getData().getPriceResponse().get(i).getTitle() + ":\t\t"
                                            + response.body().getData().getPriceResponse().get(i).getText());

                                    //txt.setTextSize(getResources().getDimension(R.dimen.price_text_size));

                                    txt.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    txt.setTypeface(null, Typeface.BOLD);
                                    txt.setGravity(Gravity.CENTER);

                                    llPrice.addView(txt);
                                } else {
                                    TextView txt = new TextView(GetCartProductsActivity.this);
                                    txt.setText(response.body().getData().getPriceResponse().get(i).getTitle() + ":\t\t"
                                            + response.body().getData().getPriceResponse().get(i).getText());

                                    //txt.setTextSize(getResources().getDimension(R.dimen.price_text_size));

                                    txt.setGravity(Gravity.CENTER);

                                    llPrice.addView(txt);
                                }


                            }


                            cartProductsRecylerview.setAdapter(new CartProductAdapter(cartProductsList, R.layout.list_item_get_cart_product, GetCartProductsActivity.this, new ClickListener() {
                                @Override
                                public void onClick(int position) {

                                    productKey = response.body().getData().getCartProductsList().get(position).getKey();

                                    progressDialog.show();
                                    DeleteCartProduct(productKey);

                                }

                            }, new AddCartQtyClickListner() {
                                @Override
                                public void onClick(int position) {


                                    productKey = response.body().getData().getCartProductsList().get(position).getKey();

                                    productQty = Integer.parseInt(response.body().getData().getCartProductsList().get(position).getQuantity());

                                    productQty = productQty + 1;

                                    progressDialog.show();

                                    UpdateCartProductQty(productKey, String.valueOf(productQty));

                                }
                            },

                                    new SubtractCartQtyClickListner() {
                                        @Override
                                        public void onClick(int position) {
                                            productKey = response.body().getData().getCartProductsList().get(position).getKey();

                                            productQty = Integer.parseInt(response.body().getData().getCartProductsList().get(position).getQuantity());

                                            productQty = productQty - 1;

                                            progressDialog.show();

                                            UpdateCartProductQty(productKey, String.valueOf(productQty));

                                        }
                                    }


                            ));
                        }

                        // No cart items
                        else {
                            cartProductsRecylerview.setVisibility(View.GONE);
                            txtCart.setVisibility(View.VISIBLE);
                            btnCheckout.setVisibility(View.INVISIBLE);
                            Snackbar.make(cartProductsRecylerview, "Cart is Empty!", Snackbar.LENGTH_LONG).show();
                        }

                    }

                    // if success false
                    else {
                        cartProductsRecylerview.setVisibility(View.GONE);
                        txtCart.setVisibility(View.VISIBLE);
                        btnCheckout.setVisibility(View.INVISIBLE);
                        Snackbar.make(cartProductsRecylerview, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onFailure(Call<ProductGetCartResponse> call, Throwable t) {

                    Snackbar.make(cartProductsRecylerview, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
                }
            });


            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cartProductsList.size() > 0) {
                        Intent checkout = new Intent(GetCartProductsActivity.this, PaymentAddressActivity.class);
                        startActivity(checkout);
                        //finish();
                    }

                    /*else
                    {
                        cartProductsRecylerview.setVisibility(View.GONE);
                        txtCart.setVisibility(View.VISIBLE);
                        Snackbar.make(cartProductsRecylerview, "No tours in cart", Snackbar.LENGTH_LONG).show();
                    }*/

                }
            });

        } else {
            Snackbar.make(cartProductsRecylerview, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }


    }


    private void DeleteCartProduct(String key) {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<DeleteCartProductResponse> deleteCall = apiService.deleteCartProducts(restoretoken, new DeleteCartProductResponse(key, "delete"));

        deleteCall.enqueue(new Callback<DeleteCartProductResponse>() {
            @Override
            public void onResponse(Call<DeleteCartProductResponse> call, Response<DeleteCartProductResponse> response) {


                if (response.body().getSuccess().equals("true")) {
                    Intent update = new Intent(GetCartProductsActivity.this, GetCartProductsActivity.class);
                    startActivity(update);
                    finish();

                    progressDialog.dismiss();
                    Snackbar.make(cartProductsRecylerview, "Product deleted ", Snackbar.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Snackbar.make(cartProductsRecylerview, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<DeleteCartProductResponse> call, Throwable t) {

                progressDialog.dismiss();
                Snackbar.make(cartProductsRecylerview, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
            }
        });


    }


    private void UpdateCartProductQty(String key, String quantity) {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<UpdateCartProductQuantity> deleteCall = apiService.updateCartQty(restoretoken, new UpdateCartProductQuantity(key, quantity, "update"));

        deleteCall.enqueue(new Callback<UpdateCartProductQuantity>() {
            @Override
            public void onResponse(Call<UpdateCartProductQuantity> call, Response<UpdateCartProductQuantity> response) {


                if (response.body().getSuccess().equals("true")) {
                    //          startActivity(new Intent(GetCartProductsActivity.this,GetCartProductsActivity.class));

                    Intent update = new Intent(GetCartProductsActivity.this, GetCartProductsActivity.class);
                    startActivity(update);
                    finish();

                    Snackbar.make(cartProductsRecylerview, "Product Updated ", Snackbar.LENGTH_LONG).show();

                    progressDialog.dismiss();

                } else {
                    Snackbar.make(cartProductsRecylerview, response.body().getWarning(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UpdateCartProductQuantity> call, Throwable t) {

                progressDialog.dismiss();
                Snackbar.make(cartProductsRecylerview, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
            }
        });

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
        super.onBackPressed();
    }


}
