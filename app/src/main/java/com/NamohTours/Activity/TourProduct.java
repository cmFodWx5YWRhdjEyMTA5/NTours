package com.NamohTours.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.NamohTours.Adapter.ProductAdapter;
import com.NamohTours.Database.DBManager;
import com.NamohTours.Database.DatabaseHelper;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.WishListClick;
import com.NamohTours.Model.TourProductDetailResponse;
import com.NamohTours.Model.TourProductResponse;
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

public class TourProduct extends LeftDrawer {


    private static final String TAG = TourProduct.class.getSimpleName();
    private static final String UserRegister = "UserRegistred";
    private static String MY_PREFS_NAME = "SHARED";
    private static String TOKEN_KEY = "TOKEN";
    private static String Register_Preference = "RegisterUser";
    final CharSequence[] sort = {"A-Z", "Z-A", "Price-Low-High", "Price-High-Low"};
    public DBManager dbManager;
    RecyclerView recyclerView;
    SharedPreferences prefs;
    String restoretoken;
    ConnectionDetector cd;
    String LogoutUserResult;
    //    private Context mContext;
    SharedPreferences.Editor editor;
    ProgressBar tourProgress;

    String success;

    TourProductResponse registerResponse;

    TextView txtProductSort, txtProductFilter, txtNoProdcutFound, txtSort, txtFilter;
    // for Sort Dialog
    int selectedElement = -1;
    List<TourProductDetailResponse> movies, returnSoretedDataList, getSortedList;
    Integer category_id;
    String CategoryId, Parent_Category_Id, SubCategory, Sub_Parent_Id, Filters, FiltersCategoryId, FiltersCount, Wishcount;
    Toolbar toolbar;
    private ShowAlertDialog showAlertDialog;
    private SharedPreferences RegisterPrefences;
    private ProgressDialog progressdialog;
    private List<String> FiltersList;
    private ArrayList<String> WishLists;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private RelativeLayout badgeLayout;
    private TextView itemMessagesBadgeTextView;
    private IconButton iconButtonMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_tour_product);

       /* toolbar = (Toolbar)findViewById(R.id.tourProductToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/

        getLayoutInflater().inflate(R.layout.activity_tour_product, frameLayout);
        HeaderLayout.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.setHomeAsUpIndicator(R.drawable.ic_back_white);


        recyclerView = (RecyclerView) findViewById(R.id.tour_Prodcut_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(TourProduct.this));

        recyclerView.addItemDecoration(new DividerItemDecoration(TourProduct.this, DividerItemDecoration.HORIZONTAL));
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        tourProgress = (ProgressBar) findViewById(R.id.tourProduct_Progress);

        txtProductSort = (TextView) findViewById(R.id.txtProductSort);
        txtProductFilter = (TextView) findViewById(R.id.txtProductFilter);
        txtNoProdcutFound = (TextView) findViewById(R.id.txtNoProdcutFilter);
        /*txtSort=(TextView)findViewById(R.id.txtSort);
        txtFilter =(TextView)findViewById(R.id.txtFilter);*/

        tourProgress.setVisibility(View.VISIBLE);
        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        // get User Register or Not and change value to true or false

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);

        showAlertDialog = new ShowAlertDialog();

        FiltersList = new ArrayList<String>();


        CategoryId = getIntent().getStringExtra("cat_id");
        Parent_Category_Id = getIntent().getStringExtra("parent_id");
        SubCategory = getIntent().getStringExtra("Sub");
        Sub_Parent_Id = getIntent().getStringExtra("sub_parent_id");
        Filters = getIntent().getStringExtra("filters");
        FiltersCategoryId = getIntent().getStringExtra("category");
        FiltersCount = getIntent().getStringExtra("FilterCount");

        FiltersList = getIntent().getStringArrayListExtra("Filters");


        progressdialog = new ProgressDialog(TourProduct.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);


        movies = new ArrayList<TourProductDetailResponse>();
        returnSoretedDataList = new ArrayList<TourProductDetailResponse>();
        registerResponse = new TourProductResponse();

        WishLists = new ArrayList<String>();


        dbManager = new DBManager(TourProduct.this);
        dbManager.open();
        Cursor cursor = dbManager.getWishList();

        Wishcount = String.valueOf(cursor.getCount());

        // get Data from notification
        dbHelper = new DatabaseHelper(getApplicationContext());
        database = dbHelper.getWritableDatabase();


        cursor.close();
        dbManager.close();
        database.close();


        if (cd.isConnectingToInternet(getApplicationContext())) {
            // if Category id is not null
            if (CategoryId != null)

            {
                String text = "<font color=#3F51B5>Sort By<br/></font> <font color=#8A8A8A>Relevance</font>";
                txtProductSort.setText(Html.fromHtml(text));


                String text1 = "<font color=#3F51B5>Filter&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/></font> <font color=#8A8A8A>0 Selected</font>";
                txtProductFilter.setText(Html.fromHtml(text1));


                //Creating Api Interface
                final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

                category_id = Integer.parseInt(CategoryId);


                Call<TourProductResponse> call = apiService.getTourPrdocutByCategoryId(restoretoken, category_id);


                call.enqueue(new Callback<TourProductResponse>() {
                    @Override
                    public void onResponse(Call<TourProductResponse> call, final Response<TourProductResponse> response) {

                        registerResponse = response.body();


                        TourProductDetailResponse detailResponse;

                        String sucess, error;

                        if ((sucess = registerResponse.getSuccess()) != null) {

                            if (sucess.equals("true")) {


                                movies = response.body().getData();

                                tourProgress.setVisibility(View.GONE);


                                //Log.e(TAG, "Call : " + call.request().url());
                                recyclerView.setAdapter(new ProductAdapter(movies, R.layout.list_item_tour_product, TourProduct.this, new ClickListener() {
                                    @Override
                                    public void onClick(int position) {
                                        if (cd.isConnectingToInternet(getApplicationContext())) {
                                            TourProductDetailResponse res = movies.get(position);

                                            if (res != null) {

                                                String product_id = res.getId();
                                                String price = res.getPrice();
                                                String specialPrice = res.getSpecial();

                                                Intent description = new Intent(TourProduct.this, ProductDescription.class);
                                                description.putExtra("product_id", product_id);
                                                description.putExtra("cat_id", CategoryId);
                                                description.putExtra("parent_id", Parent_Category_Id);
                                                description.putExtra("sub_parent_id", Sub_Parent_Id);
                                                description.putExtra("Sub", SubCategory);
                                                description.putExtra("price", price);
                                                description.putExtra("special", specialPrice);
                                                startActivity(description);
                                                //  finish();

                                            }
                                        } else {
                                            txtInternet.setVisibility(View.VISIBLE);
                                            tourProgress.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.GONE);

                                            Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();


                                        }

                                    }
                                }, new WishListClick() {
                                    @Override
                                    public void onClick(int position) {

                                        invalidateOptionsMenu();


                                        //  Snackbar.make(recyclerView,"Product added to wishlist!",Snackbar.LENGTH_LONG).show();
/*
                                        WishLists = dbManager.getWish();
                                        if(!WishLists.contains(product_id))
                                        {
                                            dbManager.inserWishlist(product_id);

                                            Log.e(TAG,"Cursor Values : "+dbManager.getWish());

                                        }*/


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
                    public void onFailure(Call<TourProductResponse> call, Throwable t) {

                        Snackbar.make(recyclerView, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
                        tourProgress.setVisibility(View.GONE);

                    }
                });


                txtProductSort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showSortDialog();
                    }
                });

                txtProductFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent filter = new Intent(TourProduct.this, CategoryFilters.class);
                        filter.putExtra("cat_id", CategoryId);
                        filter.putExtra("parent_id", Parent_Category_Id);
                        filter.putExtra("sub_parent_id", Sub_Parent_Id);
                        filter.putExtra("Sub", SubCategory);
                        startActivity(filter);
                        finish();
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


        // If Filters selected
        if (Filters != null) {

            String text = "<font color=#3F51B5>Sort By<br/></font> <font color=#8A8A8A>Relevance</font>";
            txtProductSort.setText(Html.fromHtml(text));


            if (FiltersCount != null) {
                String text12 = "<font color=#3F51B5>Filter<br/></font>";
                String text21 = "<font color=#8A8A8A>" + FiltersCount + "  Selected </font>";

                txtProductFilter.setText(Html.fromHtml(text12 + text21));
                //txtFilter.setVisibility(View.VISIBLE);
                //txtFilter.setText(FiltersCount+" "+"Selected");
                //  txtProductFilter.setText("Filter by          \n"+FiltersCount+" "+"Selected");
            } else {
                String text12 = "<font color=#3F51B5>Filter<br/></font>";
                String text21 = "<font color=#8A8A8A> 0 Selected </font>";

                txtProductFilter.setText(Html.fromHtml(text12 + text21));
            }


            final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            if (cd.isConnectingToInternet(getApplicationContext())) {


                category_id = Integer.parseInt(FiltersCategoryId);


                Call<TourProductResponse> call = apiService.getFiltersProducts(restoretoken, category_id, Filters);


                call.enqueue(new Callback<TourProductResponse>() {
                    @Override
                    public void onResponse(Call<TourProductResponse> call, Response<TourProductResponse> response) {

                        registerResponse = response.body();


                        TourProductDetailResponse detailResponse;

                        String sucess, error;

                        if ((sucess = registerResponse.getSuccess()) != null) {

                            if (sucess.equals("true")) {

                                Log.e(TAG, "Response: " + response.raw());

                                movies = response.body().getData();

                                tourProgress.setVisibility(View.GONE);

                                //Log.e(TAG, "Call : " + call.request().url());
                                recyclerView.setAdapter(new ProductAdapter(movies, R.layout.list_item_tour_product, TourProduct.this, new ClickListener() {
                                    @Override
                                    public void onClick(int position) {


                                        if (cd.isConnectingToInternet(getApplicationContext())) {
                                            TourProductDetailResponse res = movies.get(position);


                                            if (res != null) {
                                                String product_id = res.getId();

                                                Intent description = new Intent(TourProduct.this, ProductDescription.class);
                                                description.putExtra("product_id", product_id);
                                                description.putExtra("cat_id", FiltersCategoryId);
                                                description.putExtra("parent_id", Parent_Category_Id);
                                                description.putExtra("sub_parent_id", Sub_Parent_Id);
                                                description.putExtra("Sub", SubCategory);
                                                startActivity(description);
                                                //  finish();


                                            }

                                        } else {
                                            txtInternet.setVisibility(View.VISIBLE);
                                            tourProgress.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.GONE);

                                            Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                                        }


                                    }

                                }, new WishListClick() {
                                    @Override
                                    public void onClick(int position) {

                                        invalidateOptionsMenu();

                                        //  Snackbar.make(recyclerView,"Product added to wishlist!",Snackbar.LENGTH_LONG).show();
                                    }
                                }
                                ));


                            }

                            if (sucess.equals("false")) {

                                if ((error = registerResponse.getError()) != null) {
                                    tourProgress.setVisibility(View.GONE);
                                    txtNoProdcutFound.setVisibility(View.VISIBLE);
                                    Snackbar.make(recyclerView, "No tours", Snackbar.LENGTH_LONG).show();

                                }

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<TourProductResponse> call, Throwable t) {
                        Snackbar.make(recyclerView, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();
                        tourProgress.setVisibility(View.GONE);

                    }
                });


            } else {
                txtInternet.setVisibility(View.VISIBLE);
                tourProgress.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

            }


            txtProductSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showSortDialog();
                }
            });

            txtProductFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (FiltersList != null) {
                        Intent filter = new Intent(TourProduct.this, CategoryFilters.class);
                        filter.putExtra("cat_id", FiltersCategoryId);
                        filter.putExtra("parent_id", Parent_Category_Id);
                        filter.putExtra("sub_parent_id", Sub_Parent_Id);
                        filter.putExtra("Sub", SubCategory);
                        filter.putExtra("Filters", (ArrayList<String>) FiltersList);
                        startActivity(filter);
                        finish();
                    } else {
                        Intent filter = new Intent(TourProduct.this, CategoryFilters.class);
                        filter.putExtra("cat_id", FiltersCategoryId);
                        filter.putExtra("parent_id", Parent_Category_Id);
                        filter.putExtra("sub_parent_id", Sub_Parent_Id);
                        filter.putExtra("Sub", SubCategory);
                        startActivity(filter);
                        finish();
                    }


                }
            });


        } else {

        }

        txtInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cd.isConnectingToInternet(getApplicationContext())) {

                    Intent intent = new Intent(TourProduct.this, TourProduct.class);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem wish = menu.findItem(R.id.menu_productwishlists);

        if (wish != null) {
            MenuItemCompat.setActionView(wish, R.layout.layout_wish_count);


            badgeLayout = (RelativeLayout) MenuItemCompat.getActionView(wish);
            itemMessagesBadgeTextView = (TextView) badgeLayout.findViewById(R.id.badge_textView);
            itemMessagesBadgeTextView.setVisibility(View.GONE); // initially hidden

            iconButtonMessages = (IconButton) badgeLayout.findViewById(R.id.badge_icon_button);

            Wishcount = String.valueOf(dbManager.getWish().size());

            if (!(Wishcount.equals("0"))) {
                itemMessagesBadgeTextView.setText(Wishcount);
                itemMessagesBadgeTextView.setVisibility(View.VISIBLE);
                iconButtonMessages.setTextColor(getResources().getColor(R.color.white));

            } else {
                itemMessagesBadgeTextView.setVisibility(View.INVISIBLE);
            }

            iconButtonMessages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                        Intent account = new Intent(TourProduct.this, TourWishlistProduct.class);
                        startActivity(account);


                    } else {

                        Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                    }

                }
            });
        } else {

        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);//Menu Resource, Menu


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_productlogout:


                if (cd.isConnectingToInternet(getApplicationContext())) {
                    SignUp();

                } else {
                   /* showAlertDialog.showAlert(TourProduct.this, "No Internet Connection", "You don't have internet connection..Please Try Again Later ", false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });*/

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }
                return true;
            case R.id.menu_productaccount:


                if (cd.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourProduct.this, Account.class);
                    startActivity(account);
                    //  finish();

                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }


                return true;

            case R.id.menu_productcart:


                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourProduct.this, GetCartProductsActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case R.id.menu_product_order_history:

                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourProduct.this, UserOrderHistoryActivity.class);
                    startActivity(account);

                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }


                return true;

            case R.id.menu_product_documents:

                if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                    Intent account = new Intent(TourProduct.this, UserDocumentActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


           /* case R.id.menu_wishlists:
                if(ConnectionDetector.isConnectingToInternet(getApplicationContext()))
                {
                    Intent account = new Intent(TourProduct.this,TourWishlistProduct.class);
                    startActivity(account);



                }
                else
                {

                    Snackbar.make(recyclerView,"Please turn on your mobile data or wifi ",Snackbar.LENGTH_LONG).show();

                }

                return true;
*/

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

       /* if(SubCategory.equals("Sub"))
        {


               Intent sub = new Intent(TourProduct.this,TourSubCategory.class);
              sub.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sub.putExtra("cat_id",Parent_Category_Id);
                startActivity(sub);
                finish();
                Log.e(TAG,"Goto sub Activity");




        }

        if(SubCategory.equals("SubSub"))
        {

            Intent subsub = new Intent(TourProduct.this,TourSubSubCategory.class);
            subsub.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            subsub.putExtra("cat_id",Sub_Parent_Id);
            subsub.putExtra("parent_id",Parent_Category_Id);
            startActivity(subsub);
            finish();
            Log.e(TAG,"Goto sub sub Activity");
        }

        if(SubCategory.equals("Parent"))
        {
            Intent parent = new Intent(TourProduct.this,TourParentCategory.class);
            parent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(parent);
            finish();
        }
*/
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
                        Intent log = new Intent(TourProduct.this, Login.class);
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


    // Sort Dialog
    private void showSortDialog() {


        //txtSort.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(TourProduct.this)
                .setTitle("Sort By ")
                .setSingleChoiceItems(sort, selectedElement, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub

                        String text12 = "<font color=#3F51B5>Filter<br/></font>";
                        String text21 = "<font color=#8A8A8A> 0 Selected </font>";

                        txtProductFilter.setText(Html.fromHtml(text12 + text21));


                        selectedElement = which;
                        if (which == 0) {
                            // txtProductSort.setText("Sort by \n"+sort[which].toString());

                            String text = "<font color=#3F51B5>Sort By<br/></font>";
                            String text2 = "<font color=#8A8A8A>" + sort[which].toString() + "</font>";
                            String text3 = text + text2;
                            txtProductSort.setText(Html.fromHtml(text + text2));
                            SortProducts(0);


                        }

                        if (which == 1) {
                            //  txtProductSort.setText("Sort by \n"+sort[which].toString());

                            String text = "<font color=#3F51B5>Sort By<br/></font>";
                            String text2 = "<font color=#8A8A8A>" + sort[which].toString() + "</font>";
                            String text3 = text + text2;
                            txtProductSort.setText(Html.fromHtml(text + text2));
                            SortProducts(0);
                            SortProducts(1);
                            //Toast.makeText(TourParentCategory.this, "The Selelcted Sort Option is Second : " + sort[which], Toast.LENGTH_LONG).show();

                        }


                        if (which == 2) {
                            //   txtProductSort.setText("Sort by \n"+sort[which].toString());

                            String text = "<font color=#3F51B5>Sort By<br/></font>";
                            String text2 = "<font color=#8A8A8A>" + sort[which].toString() + "</font>";
                            String text3 = text + text2;
                            txtProductSort.setText(Html.fromHtml(text + text2));
                            SortProducts(0);
                            SortProducts(2);
                        }

                        if (which == 3) {
                            //  txtProductSort.setText("Sort by \n"+sort[which].toString());
                            String text = "<font color=#3F51B5>Sort By<br/></font>";
                            String text2 = "<font color=#8A8A8A>" + sort[which].toString() + "</font>";
                            String text3 = text + text2;
                            txtProductSort.setText(Html.fromHtml(text + text2));
                            SortProducts(0);
                            SortProducts(3);
                        }


//dismissing the dialog when the user makes a selection.
                        dialog.dismiss();
                    }
                });
        AlertDialog alertdialog2 = builder2.create();
        alertdialog2.show();
        //return alertdialog2;
    }


    // REST METHOD FOR SORT PRODUCTS BY NAME , PRICE
    private void SortProducts(int sortIndex) {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<TourProductResponse> call = null;
        if (sortIndex == 0) {

            call = apiService.getSortedCategoryProductByNameAsc(restoretoken, category_id);
        }

        if (sortIndex == 1) {
            call = apiService.getSortedCategoryProductByNameDesc(restoretoken, category_id);
        }

        if (sortIndex == 2) {
            call = apiService.getSortedCategoryProductByPriceAsc(restoretoken, category_id);
        }

        if (sortIndex == 3) {
            call = apiService.getSortedCategoryProductByPriceDesc(restoretoken, category_id);
        }


        call.enqueue(new Callback<TourProductResponse>() {
            @Override
            public void onResponse(Call<TourProductResponse> call, Response<TourProductResponse> response) {


                String response1, error1;


                if ((response1 = registerResponse.getSuccess()) != null)

                {


                    if (response1.equals("true")) {


                        returnSoretedDataList = response.body().getData();

                        recyclerView.setAdapter(new ProductAdapter(returnSoretedDataList, R.layout.list_item_tour_product, TourProduct.this, new ClickListener() {
                            @Override
                            public void onClick(int position) {


                            }
                        }, new WishListClick() {
                            @Override
                            public void onClick(int position) {

                                invalidateOptionsMenu();
                                // Snackbar.make(recyclerView,"Product added to wishlist!",Snackbar.LENGTH_LONG).show();
                            }
                        }
                        ));
                        //  recyclerView.notify();

                    }

                    if (response1.equals("false")) {

                        if ((error1 = registerResponse.getError()) != null) {
                            Snackbar.make(recyclerView, error1, Snackbar.LENGTH_LONG).show();
                        }

                    }

                } else {

                    Snackbar.make(recyclerView, "Sorry , Please Try Later..!!", Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<TourProductResponse> call, Throwable t) {

                // Log.e(TAG, "Error : " + t.toString());
            }
        });


        //return  returnSoretedDataList;
    }

}
