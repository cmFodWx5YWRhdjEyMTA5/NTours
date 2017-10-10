package com.NamohTours.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.NamohTours.Adapter.ProductUserReviewAdapter;
import com.NamohTours.Model.ProductAddCartDetails;
import com.NamohTours.Model.ProductAddCartResponse;
import com.NamohTours.Model.ProductReviewDetailListResponse;
import com.NamohTours.SmtpMail.GMailSender;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.NamohTours.Adapter.RecylerExpandAdapter;
import com.NamohTours.Model.TourProductResponseById;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.View.ExtratabHeader;
import com.NamohTours.View.ProductExtraTabHeader;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;
import static com.NamohTours.Service.Prefs.UserContact;
import static com.NamohTours.Service.Prefs.UserName;

public class ProductDescription extends LeftDrawer {

    private static final String TAG = ProductDescription.class.getSimpleName();

    String productDesc;
    TextView txtDesc, txtPrice, txtSpecialPrice, txtOptionPrice, txtOptionSpecialPrice;
    Button btnEnquireForThisTour, btnOptions, btnAddtoCart;
    EditText edtDate, edtQty;
    DatePickerDialog datePickerDialog;
    Toolbar toolbar;

    String ProductId, CategoryId, subCategory, Sub_Parent_Id, ParentId, ExtraTabtitle, ExtratabDesc, productPrice,
            productSpecialPrice, name, contact, edtqty, edtdate, StockQty;
    ConnectionDetector cd;

    SharedPreferences prefs;

    String restoretoken;
    SharedPreferences.Editor editor;
    TourProductResponseById registerResponse;
    Integer product_id;
    RecyclerView recyclerView, reviewRecyler;
    private SharedPreferences RegisterPrefences;
    private ArrayList<String> _images;
    private GalleryPagerAdapter _adapter;
    private ViewPager _pager;
    private LinearLayout _thumbnails;

    private WebView webProductDesc;

    private ArrayList<ExtratabHeader> Header;
    private List<ProductReviewDetailListResponse> responseList;
    private RecylerExpandAdapter expandAdapter;

    private ProgressDialog progressdialog;
    private ProductUserReviewAdapter productUserReviewAdapter;
    private CardView productReviewCard;
    private AlertDialog alertDialog = null;

    private LinearLayout ll;
    private AppCompatCheckBox[] ch;
    private AppCompatRadioButton[] radio;
    private int mChangePrice;
    private String mProductChangeHotelPrice;
    private SlidingUpPanelLayout slidingLayout;

    private ArrayList<HashMap<String, String>> optionsKeyValue, optionsMail;
    private HashMap<String, String> OptionsKeysValues, OptionsMailKeyValue;
    private List<String> selectedOptionsChekboxIds;

    private HashMap<String, Object> optionsIds;

    Target target;
    private boolean Btnflag, isOptionsAvailable, isDateOptionAvaliable;

    public ProductDescription() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_product_description);

        getLayoutInflater().inflate(R.layout.activity_product_description, frameLayout);
        HeaderLayout.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.setHomeAsUpIndicator(R.drawable.ic_back_white);

        // sliding LAyout for options
        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        progressdialog = new ProgressDialog(ProductDescription.this);
        progressdialog.setMessage("Sending Enquiry....");
        progressdialog.setCancelable(false);

        // shared Preferences
        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);

        name = RegisterPrefences.getString(UserName, null);
        contact = RegisterPrefences.getString(UserContact, null);


        ll = (LinearLayout) findViewById(R.id.ll_Bottom);

        recyclerView = (RecyclerView) findViewById(R.id.productdescRecyler);

        recyclerView.setLayoutManager(new LinearLayoutManager(ProductDescription.this));

        recyclerView.addItemDecoration(new DividerItemDecoration(ProductDescription.this, DividerItemDecoration.HORIZONTAL));


        // For Product Review
        reviewRecyler = (RecyclerView) findViewById(R.id.productReviewRecyler);
        reviewRecyler.setLayoutManager(new LinearLayoutManager(ProductDescription.this, LinearLayoutManager.HORIZONTAL, false));

        //reviewRecyler.addItemDecoration(new DividerItemDecoration(ProductDescription.this, DividerItemDecoration.HORIZONTAL));

        productReviewCard = (CardView) findViewById(R.id.productReviewCard);

        txtPrice = (TextView) findViewById(R.id.tour_productdesc_subtitle);
        txtSpecialPrice = (TextView) findViewById(R.id.tour_productdesc_special);

        txtOptionPrice = (TextView) findViewById(R.id.tour_productdescOption_subtitle);
        txtOptionSpecialPrice = (TextView) findViewById(R.id.tour_productOption_special);

        edtDate = (EditText) findViewById(R.id.edt_date);
        edtQty = (EditText) findViewById(R.id.edt_qty);


        btnEnquireForThisTour = (Button) findViewById(R.id.btnEnquireForThisTour);
        btnOptions = (Button) findViewById(R.id.btnOptions);
        btnAddtoCart = (Button) findViewById(R.id.btn_addtoCart);

        _pager = (ViewPager) findViewById(R.id.pager);
        _thumbnails = (LinearLayout) findViewById(R.id.thumbnails);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";


        ProductId = getIntent().getStringExtra("product_id");

        CategoryId = getIntent().getStringExtra("cat_id");
        subCategory = getIntent().getStringExtra("Sub");
        ParentId = getIntent().getStringExtra("parent_id");
        Sub_Parent_Id = getIntent().getStringExtra("sub_parent_id");
        productPrice = getIntent().getStringExtra("price");
        productSpecialPrice = getIntent().getStringExtra("special");


        Btnflag = false;
        isOptionsAvailable = false;
        isDateOptionAvaliable = false;

        if ((productSpecialPrice != null) || (productPrice != null)) {
            if (!(productSpecialPrice.equals("false"))) {
                txtSpecialPrice.setVisibility(View.VISIBLE);

                txtSpecialPrice.setText("\u20B9" + "\u0020" + productSpecialPrice);


                txtPrice.setText("\u20B9" + "\u0020" + productPrice);
                txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                txtOptionSpecialPrice.setVisibility(View.VISIBLE);

                txtOptionSpecialPrice.setText("\u20B9" + "\u0020" + productSpecialPrice);


                txtOptionPrice.setText("\u20B9" + "\u0020" + productPrice);
                txtOptionPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);





            } else {
                if (!(productPrice.startsWith("0"))) {
                    txtPrice.setText("\u20B9" + "\u0020" + productPrice);
                    txtOptionPrice.setText("\u20B9" + "\u0020" + productPrice);
                } else {
                    txtPrice.setText("Contact for Price");
                    txtOptionPrice.setText("Contact for Price");
                }


            }

        }


        // If User already clicked on btnemail then dont send mail again
        if (Btnflag) {
            btnEnquireForThisTour.setBackgroundColor(getResources().getColor(R.color.gray));

        }

        //  else send mail
        else {
            btnEnquireForThisTour.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnEnquireForThisTour.setEnabled(true);
            btnEnquireForThisTour.setClickable(true);
        }


        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });


        slidingLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        // get User Register or Not and change value to true or false

        _adapter = new GalleryPagerAdapter(this);

        responseList = new ArrayList<ProductReviewDetailListResponse>();
        optionsKeyValue = new ArrayList<HashMap<String, String>>();
        optionsMail = new ArrayList<HashMap<String, String>>();

        OptionsKeysValues = new HashMap<String, String>();
        OptionsMailKeyValue = new HashMap<String, String>();
        optionsIds = new HashMap<String, Object>();
        selectedOptionsChekboxIds = new ArrayList<String>();


        if (ProductId != null) {

            //Creating Api Interface
            final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            if (cd.isConnectingToInternet(getApplicationContext())) {


                product_id = Integer.parseInt(ProductId);

                Header = new ArrayList<>();


                Call<TourProductResponseById> call = apiService.getProductbyId(restoretoken, product_id);


                call.enqueue(new Callback<TourProductResponseById>() {
                    @Override
                    public void onResponse(Call<TourProductResponseById> call, Response<TourProductResponseById> response) {

                        registerResponse = response.body();


                        String sucess, error;

                        if ((sucess = registerResponse.getSuccess()) != null) {

                            if (sucess.equals("true")) {


                                int siz = registerResponse.getData().getTourProductOptionsResponses().size();
                                StockQty = registerResponse.getData().getQuantity();



                                for (int i = 0; i < siz; i++) {


                                    // If Date options if available then show
                                    // FOR DATE
                                    if (registerResponse.getData().getTourProductOptionsResponses().get(i).getType().equals("date")) {
                                        isDateOptionAvaliable = true;
                                        edtdate = registerResponse.getData().getTourProductOptionsResponses().get(i).getProduct_option_id();
                                        optionsIds.put(edtdate, null);

                                    }

                                    // FOR radio , dropdown
                                    if (registerResponse.getData().getTourProductOptionsResponses().get(i).getType().equals("select")) {


                                        isOptionsAvailable = true;

                                        int radioSize = registerResponse.getData().getTourProductOptionsResponses().get(i).getTourProductOptionsDetailResponseList().size();


                                        RadioGroup rg = new RadioGroup(ProductDescription.this);

                                        radio = new AppCompatRadioButton[radioSize];

                                        TextView textView = new TextView(ProductDescription.this);
                                        textView.setText(registerResponse.getData().getTourProductOptionsResponses().get(i).getName());
                                        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        //   textView.setTextSize(getResources().getDimension(R.dimen.secondary_text_size));
                                        textView.setTypeface(null, Typeface.BOLD);
                                        ll.addView(textView);


                                        // create Dynamic Checkbox
                                        for (int j = 0; j < radioSize; j++) {
                                            radio[j] = new AppCompatRadioButton(ProductDescription.this);
                                            radio[j].setId(Integer.parseInt(registerResponse.getData().getTourProductOptionsResponses().get(i).getTourProductOptionsDetailResponseList().get(j).getProduct_option_value_id()));
                                            radio[j].setTextColor(getResources().getColor(R.color.colorGrey));
                                            radio[j].setText(Html.fromHtml(registerResponse.getData().getTourProductOptionsResponses().get(i).getTourProductOptionsDetailResponseList().get(j).getName()));
                                            // add radio in radio group
                                            rg.addView(radio[j]);
                                            //  ll.addView(radio[j]);
                                        }

                                        // add Radio Group in linear layout
                                        ll.addView(rg);
                                        final int ji = i;


                                        for (int k = 0; k < radioSize; k++) {
                                            final int jk = k;
                                            radio[jk].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView,
                                                                             boolean isChecked) {


                                                    if (isChecked) {

                                                        // Price is not False
                                                       /* if (!(registerResponse.getData().getTourProductOptionsResponses().get(ji).getTourProductOptionsDetailResponseList().get(jk).getPrice().equals("false")))
                                                        {*/


                                                        String Optionsvalue = registerResponse.getData().getTourProductOptionsResponses().get(ji).getTourProductOptionsDetailResponseList().get(jk).getPrice();
                                                        String OptionsName = registerResponse.getData().getTourProductOptionsResponses().get(ji).getTourProductOptionsDetailResponseList().get(jk).getName();
                                                        String Optionskey = registerResponse.getData().getTourProductOptionsResponses().get(ji).getName();
                                                        String optionsId = registerResponse.getData().getTourProductOptionsResponses().get(ji).getProduct_option_id();
                                                        String optionValueId = registerResponse.getData().getTourProductOptionsResponses().get(ji).getTourProductOptionsDetailResponseList().get(jk).getProduct_option_value_id();


                                                        if (Optionsvalue.equals("false")) {
                                                            Optionsvalue = "0";
                                                        } else {
                                                            Optionsvalue = Optionsvalue;
                                                        }
                                                        // If OptionsKEyValues is not null then check key is already exist or not
                                                        if (optionsKeyValue.size() > 0) {

                                                            // If Exist then remove and change with new selected index's key and price
                                                            if (OptionsKeysValues.containsKey(Optionskey)) {

                                                                // OptionsKeysValues.remove(Optionskey);
                                                                optionsKeyValue.remove(OptionsKeysValues);


                                                                OptionsKeysValues.put(Optionskey, Optionsvalue);

                                                                optionsKeyValue.clear();

                                                                optionsKeyValue.add(OptionsKeysValues);

                                                                chnagePriceWithOption();


                                                                optionsIds.put(optionsId, optionValueId);

                                                                // For Mail Enquiry
                                                                if (OptionsMailKeyValue.containsKey(Optionskey)) {

                                                                    optionsMail.remove(OptionsMailKeyValue);


                                                                    OptionsMailKeyValue.put(Optionskey, OptionsName);

                                                                    optionsMail.clear();

                                                                    optionsMail.add(OptionsMailKeyValue);

                                                                }


                                                            }

                                                            // If Not exist then add in arrayList
                                                            else {

                                                                OptionsKeysValues.put(Optionskey, Optionsvalue);
                                                                optionsKeyValue.add(OptionsKeysValues);

                                                                OptionsMailKeyValue.put(Optionskey, OptionsName);
                                                                optionsMail.add(OptionsMailKeyValue);

                                                                optionsIds.put(optionsId, optionValueId);


                                                                chnagePriceWithOption();


                                                            }

                                                        }

                                                        // If OptionsKEyValues is null then add values first time
                                                        else {


                                                            OptionsKeysValues.put(Optionskey, Optionsvalue);
                                                            optionsKeyValue.add(OptionsKeysValues);


                                                            OptionsMailKeyValue.put(Optionskey, OptionsName);
                                                            optionsMail.add(OptionsMailKeyValue);

                                                            optionsIds.put(optionsId, optionValueId);


                                                            chnagePriceWithOption();


                                                        }

                                                    } else {
                                                        // DO NOTHING

                                                    }


                                                }
                                            });


                                        } // For loop


                                    }
                                    // if loop (select)


                                    /*// If Date options if available then show
                                    // FOR DATE
                                    if (registerResponse.getData().getTourProductOptionsResponses().get(i).getType().equals("date"))
                                    {
                                        edtdate = registerResponse.getData().getTourProductOptionsResponses().get(i).getProduct_option_id();
                                        optionsIds.put(edtdate, null);

                                    }

                                    // else dont show date edit text
                                    else
                                    {
                                        Log.e(TAG, "onResponse: ----------don't show date because there is no need of date field ----");
                                    }*/


                                    // FOR CHECKBOX
                                    if (registerResponse.getData().getTourProductOptionsResponses().get(i).getType().equals("checkbox")) {


                                        // For How many checkbox
                                        int checksize = registerResponse.getData().getTourProductOptionsResponses().get(i).getTourProductOptionsDetailResponseList().size();

                                        ch = new AppCompatCheckBox[checksize];


                                        TextView textView = new TextView(ProductDescription.this);
                                        textView.setText(registerResponse.getData().getTourProductOptionsResponses().get(i).getName());
                                        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        //textView.setTextSize(getResources().getDimension(R.dimen.secondary_text_size));
                                        textView.setTypeface(null, Typeface.BOLD);
                                        ll.addView(textView);


                                        // create Dynamic Checkbox
                                        for (int j = 0; j < checksize; j++) {
                                            ch[j] = new AppCompatCheckBox(ProductDescription.this);
                                            ch[j].setId(Integer.parseInt(registerResponse.getData().getTourProductOptionsResponses().get(i).getTourProductOptionsDetailResponseList().get(j).getProduct_option_value_id()));
                                            ch[j].setTextColor(getResources().getColor(R.color.colorGrey));
                                            ch[j].setText(Html.fromHtml(registerResponse.getData().getTourProductOptionsResponses().get(i).getTourProductOptionsDetailResponseList().get(j).getName()));
                                            ll.addView(ch[j]);
                                        }


                                        final int ji = i;
                                        // Checkbox Listener
                                        for (int k = 0; k < checksize; k++) {
                                            final int jk = k;
                                            ch[jk].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                    String Optionskey = registerResponse.getData().getTourProductOptionsResponses().get(ji).getName();
                                                    String Optionsvalue = registerResponse.getData().getTourProductOptionsResponses().get(ji).getTourProductOptionsDetailResponseList().get(jk).getPrice();
                                                    String OptionsName = registerResponse.getData().getTourProductOptionsResponses().get(ji).getTourProductOptionsDetailResponseList().get(jk).getName();
                                                    String optionsId = registerResponse.getData().getTourProductOptionsResponses().get(ji).getProduct_option_id();
                                                    String optionValueId = registerResponse.getData().getTourProductOptionsResponses().get(ji).getTourProductOptionsDetailResponseList().get(jk).getProduct_option_value_id();


                                                    if (isChecked) {

                                                        // Put in hash map
                                                        OptionsKeysValues.put(OptionsName, Optionsvalue);

                                                        // clear arraylist
                                                        optionsKeyValue.clear();

                                                        // add in arraylist
                                                        optionsKeyValue.add(OptionsKeysValues);

                                                        chnagePriceWithOption();


                                                        selectedOptionsChekboxIds.add(optionValueId);

                                                        optionsIds.put(optionsId, selectedOptionsChekboxIds);


                                                        // Add for Enquiry Mail

                                                        OptionsMailKeyValue.put(Optionskey, OptionsName);

                                                        optionsMail.clear();

                                                        optionsMail.add(OptionsMailKeyValue);


                                                    } else {


                                                        // Remove Unchecked key from HashMap (we remove key then value also removed)
                                                        OptionsKeysValues.remove(OptionsName);

                                                        OptionsMailKeyValue.remove(Optionskey);


                                                        // removing value from arraylist
                                                        for (int j = 0; j < selectedOptionsChekboxIds.size(); j++) {
                                                            if (selectedOptionsChekboxIds.get(j).equals(optionValueId)) {
                                                                selectedOptionsChekboxIds.remove(j);

                                                            }


                                                        }


                                                        // Update Hashmap list

                                                        // if arraylist is greater than 0 then update hashmap with values
                                                        if (selectedOptionsChekboxIds.size() > 0) {
                                                            optionsIds.put(optionsId, selectedOptionsChekboxIds.toString());

                                                        }

                                                        // else remove key from hashmap
                                                        else {
                                                            optionsIds.remove(optionsId);

                                                        }


                                                        chnagePriceWithOption();


                                                    }

                                                }
                                            });
                                        }


                                    }// End of if loop (for Checkbox)


                                }
                                // end of for loop


                                //-----------------------------------------------------------------------------------------------


                                // If DateFlag is Not True then Invisible DateField
                                // If Date is not Available then don't show Date Edit Text

                                if (!(isDateOptionAvaliable)) {
                                    // Log.e(TAG, "onResponse: Date is Not Avaliable here ..... ");

                                    edtDate.setVisibility(View.INVISIBLE);
                                }


                                // get Product Itinerary Images list
                                if (response.body().getData().getOriginal_imagesList() != null) {

                                    // dummyimages = response.body().getData().getOriginal_imagesList();
                                    _images = response.body().getData().getOriginal_imagesList();
                                    _pager.setOffscreenPageLimit(_images.size());
                                    _pager.setAdapter(_adapter);
                                }


                                // Itinernary Description
                                productDesc = registerResponse.getData().getDescription();
                                //  webProductDesc.loadData(productDesc,mimeType,encoding);


                                // get User Review List
                                if (response.body().getData().getResponse() != null) {

                                    if (response.body().getData().getResponse().getReviews() != null) {
                                        // if Review List is not null
                                        if (response.body().getData().getResponse().getReviews().size() > 0) {

                                            responseList = response.body().getData().getResponse().getReviews();

                                            productUserReviewAdapter = new ProductUserReviewAdapter(responseList, R.layout.list_item_user_product_review, ProductDescription.this);
                                            reviewRecyler.setAdapter(productUserReviewAdapter);
                                            productUserReviewAdapter.notifyDataSetChanged();

                                        } else {
                                            productReviewCard.setVisibility(View.INVISIBLE);
                                        }
                                    } else {
                                        productReviewCard.setVisibility(View.INVISIBLE);
                                    }
                                } else {
                                    productReviewCard.setVisibility(View.INVISIBLE);
                                }


                                // getExtra Tabs Resposne
                                if (response.body().getData().getExtraTabResponse() != null) {


                                    ArrayList<ProductExtraTabHeader> headers = new ArrayList<ProductExtraTabHeader>();

                                    // add Itinerary in recycler view
                                    headers.add(new ProductExtraTabHeader(productDesc));
                                    Header.add(new ExtratabHeader("Itinerary", headers));

                                    // now add others extra tab in recycler view

                                    for (int i = 0; i < response.body().getData().getExtraTabResponse().size(); i++) {

                                        ExtraTabtitle = response.body().getData().getExtraTabResponse().get(i).getHeading();
                                        ExtratabDesc = response.body().getData().getExtraTabResponse().get(i).getDescription();

                                        headers = new ArrayList<ProductExtraTabHeader>();

                                        headers.add(new ProductExtraTabHeader(ExtratabDesc));

                                        Header.add(new ExtratabHeader(ExtraTabtitle, headers));


                                    }


                                    expandAdapter = new RecylerExpandAdapter(ProductDescription.this, Header);
                                    //recyclerView.setAdapter(new RecylerExpandAdapter(ProductDescription.this,Header));
                                    recyclerView.setAdapter(expandAdapter);
                                    expandAdapter.notifyDataSetChanged();


                                }

                            }

                            if (sucess.equals("false")) {

                                if ((error = registerResponse.getError()) != null) {

                                    Snackbar.make(txtDesc, error, Snackbar.LENGTH_LONG).show();
                                }

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<TourProductResponseById> call, Throwable t) {

                        Snackbar.make(txtDesc, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                    }
                });


                btnEnquireForThisTour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (Btnflag) {
                            // You Already Send Enquiry for this Product
                            Snackbar.make(btnEnquireForThisTour, "Enquiry already send", Snackbar.LENGTH_LONG).show();
                        } else {
                            // Show Enquiry Dialog
                            showEnqDialog();
                        }

                    }
                });


                btnAddtoCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        edtqty = edtQty.getText().toString();

                        if (!(TextUtils.isEmpty(edtqty))) {


                            if (StockQty != null) {

                                // If Stock is Greater than User entered Qty then add to cart
                                // Means Stock is Avaliable
                                if (Integer.parseInt(StockQty) >= Integer.parseInt(edtqty)) {

                                    AddToCart(String.valueOf(product_id), edtqty, optionsIds);

                                }

                                // Else show Out Of Stock Msg
                                // Out of Stock tour
                                else {
                                    Snackbar.make(btnAddtoCart, "Out of Stock", Snackbar.LENGTH_LONG).show();
                                }

                        }


                        } else {
                            Snackbar.make(btnAddtoCart, "Enter quantity", Snackbar.LENGTH_LONG).show();
                        }

                    }
                });


                edtDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day


                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(ProductDescription.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // set day of month , month and year value in the edit text
                                  /*  edtDate.setText(dayOfMonth + "/"
                                            + (monthOfYear + 1) + "/" + year);
*/

                                        //year + "-" + (monthOfYear + 1) + "-" + dayOfMonth
                                        // Display DDMMYY format of date
                                        edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        // But Post YYMMDD format for Request for add to cart
                                        // Add date in options
                                        optionsIds.put(edtdate, year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                    }
                                }, mYear, mMonth, mDay);

                        // SET Minimum date is today's date in DatePickerDialog
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });

            } else {


                Snackbar.make(txtDesc, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
            }


        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_descrption, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here

        if (item.getItemId() == R.id.menu_cart) {
            startActivity(new Intent(ProductDescription.this, GetCartProductsActivity.class));
        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    private void showEnqDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Send Enquiry");
        builder.setMessage("Do you really want to send enquiry ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                StringBuilder body = new StringBuilder();
                body.append("Name :" + name);
                body.append(System.getProperty("line.separator"));
                body.append("Contact :" + contact);
                body.append(System.getProperty("line.separator"));
                body.append("Product Id :" + product_id);
                body.append(System.getProperty("line.separator"));
                body.append("Tour Options :" + OptionsMailKeyValue.toString());

                new SendMailAsync(body.toString()).execute();


            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();

        //alertDialog.setButton();
        alertDialog.show();


    }


    private void chnagePriceWithOption() {
        int tempPrice = 0, Price = 0;


        for (String key : OptionsKeysValues.values()) {

            Price = getPriceWithFormat(key);

            // Add all selected option price in to temp int
            tempPrice = tempPrice + Price;

            // Update Price
            UpdateTxtPrice(tempPrice);

        }

    }


    private int getPriceWithFormat(String price) {


        // Remove Rupees Symbol from price

        if (price.contains("₹")) {
            price = price.substring(price.indexOf("₹") + 2);

        }

        // Replace all commas from price for addition or subtraction

        price = price.replaceAll(",", "");


        int formattedPrice = Integer.parseInt(price);


        return formattedPrice;
    }


    private void UpdateTxtPrice(int OptionsPrice) {


        // Remove Comma from Options Price
        OptionsPrice = getPriceWithFormat(String.valueOf(OptionsPrice));


        // If Spcl Price is not empty
        if ((!(productSpecialPrice.equals("false")) && (productSpecialPrice != null))) {

            if ((!(productPrice.equals("false"))) && (productPrice != null)) {

                // Remove Ruppes symbol and comma from price and spcl Price
                int spclPric = getPriceWithFormat(productSpecialPrice);
                int Price = getPriceWithFormat(productPrice);


                // Add Options Amount into Price and Spcl Price
                Price = Price + OptionsPrice;
                spclPric = spclPric + OptionsPrice;

                // Add comma in price
                String addedSpclPrice = NumberFormat.getIntegerInstance().format(spclPric);
                String addedPrice = NumberFormat.getIntegerInstance().format(Price);


                UpdatePriceText(addedPrice, addedSpclPrice);

            }
        }

        // If Price is not zero

        if (!(productPrice.startsWith("0"))) {
            int Price = getPriceWithFormat(productPrice);


            // Add Options Amount into Price
            Price = Price + OptionsPrice;

            // Add comma in price

            String addedPrice = NumberFormat.getIntegerInstance().format(Price);
            String addedSpclPrice = "false";

            UpdatePriceText(addedPrice, addedSpclPrice);

        } else {
            // Do Nothing
        }


    }


    private void UpdatePriceText(String Price, String SpecialPrice) {
        if ((SpecialPrice != null) || (Price != null)) {
            if (!(SpecialPrice.equals("false"))) {
                txtOptionSpecialPrice.setVisibility(View.VISIBLE);

                txtOptionSpecialPrice.setText("\u20B9" + "\u0020" + SpecialPrice);


                txtOptionPrice.setText("\u20B9" + "\u0020" + Price);
                txtOptionPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            } else {
                if (!(Price.startsWith("0"))) {
                    txtOptionPrice.setText("\u20B9" + "\u0020" + Price);
                } else {
                    txtOptionPrice.setText("Contact for Price");
                }


            }
        }
    }


    private void UpdatePrice(int SpeclPrice, int Pric, int HotelPrice, int MealPrice) {


        String SpecialPrice, Price;


        // Add Fix Product Price + Selected Hotel Price + Selected Meal Price
        Pric = Pric + HotelPrice + MealPrice;

        Price = NumberFormat.getIntegerInstance().format(Pric);

        // If Special is not zero then add all prices
        if (!(SpeclPrice == 0)) {

            SpeclPrice = SpeclPrice + HotelPrice + MealPrice;

            SpecialPrice = NumberFormat.getIntegerInstance().format(SpeclPrice);


        }

        // else special Price will be false
        else {
            SpecialPrice = "false";

        }


        if ((SpecialPrice != null) || (Price != null)) {
            if (!(productSpecialPrice.equals("false"))) {
                txtSpecialPrice.setVisibility(View.VISIBLE);

                txtSpecialPrice.setText("\u20B9" + "\u0020" + SpecialPrice);


                txtPrice.setText("\u20B9" + "\u0020" + Price);
                txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            } else {
                if (!(Price.startsWith("0"))) {
                    txtPrice.setText("\u20B9" + "\u0020" + Price);
                } else {
                    txtPrice.setText("Contact for Price");
                }


            }

        }
    }


    private void AddToCart(String productId, String Quantity, HashMap<String, Object> Body) {


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<ProductAddCartResponse> call = apiService.addToCart(restoretoken, new ProductAddCartDetails(productId, Quantity, Body, "add"));

        call.enqueue(new Callback<ProductAddCartResponse>() {
            @Override
            public void onResponse(Call<ProductAddCartResponse> call, Response<ProductAddCartResponse> response) {


                if (response.body().getSuccess().equals("true")) {
                    // added successfully
                    //Snackbar.make(btnAddtoCart, "Product added to cart successfully!", Snackbar.LENGTH_LONG).show();

                    Intent gotoCart = new Intent(ProductDescription.this, GetCartProductsActivity.class);
                    startActivity(gotoCart);
                    finish();




                } else {


                    if (response.body().getWarning().getOption().size() > 0) {

                        Snackbar.make(btnAddtoCart, "Please Select Options", Snackbar.LENGTH_LONG).show();


                    }



                    // not added successfully
                    //  Snackbar.make(btnAddtoCart,"Please select options,date,quantity",Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ProductAddCartResponse> call, Throwable t) {

            }
        });


    }



    private class GalleryPagerAdapter extends PagerAdapter {

        Context _context;
        LayoutInflater _inflater;

        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return _images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.pager_gallery_item, container, false);
            container.addView(itemView);

            // Get the border size to show around each image
            int borderSize = _thumbnails.getPaddingTop();

            // Get the size of the actual thumbnail image
            int thumbnailSize = ((FrameLayout.LayoutParams)
                    _pager.getLayoutParams()).bottomMargin - (borderSize * 2);

            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(0, 0, borderSize, 0);

            // You could also set like so to remove borders
            //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            //        ViewGroup.LayoutParams.WRAP_CONTENT,
            //        ViewGroup.LayoutParams.WRAP_CONTENT);

            final ImageView thumbView = new ImageView(_context);
            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbView.setLayoutParams(params);
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    // Set the pager position when thumbnail clicked
                    _pager.setCurrentItem(position);
                }
            });
            _thumbnails.addView(thumbView);

            final SubsamplingScaleImageView imageView =
                    (SubsamplingScaleImageView) itemView.findViewById(R.id.product_desc_image);


            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setImage(ImageSource.bitmap(bitmap));
                    thumbView.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                    imageView.setImage(ImageSource.resource(R.drawable.category_placeholder));
                    thumbView.setImageDrawable(getResources().getDrawable(R.drawable.category_placeholder));
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };


            Picasso.with(_context).load(_images.get(position)).into(target);
            imageView.setTag(target);

            // We setTag for imageView with picasso target because Garbage Collector, and reference target with view

           /* Picasso.with(_context).load(_images.get(position)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setImage(ImageSource.bitmap(bitmap));
                    thumbView.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    imageView.setImage(ImageSource.resource(R.drawable.category_placeholder));
                    thumbView.setImageDrawable(getResources().getDrawable(R.drawable.category_placeholder));

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    imageView.setImage(ImageSource.resource(R.drawable.category_placeholder));
                    // thumbView.setImageDrawable(getResources().getDrawable(R.drawable.category_placeholder));
                }
            });*/

            // Asynchronously load the image and set the thumbnail and pager view


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    private class SendMailAsync extends AsyncTask<Void, Void, String> {


        private String mailBody;

        private SendMailAsync(String body) {

            this.mailBody = body;


        }


        @Override
        protected void onPreExecute() {

            progressdialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {


            GMailSender sender = new GMailSender("contact@namoh.co.in",
                    "namoh@1212");
            try {
                sender.sendMail("Mobile App Product Enquiry", mailBody,
                        "contact@namoh.co.in", "contact@namoh.co.in");

                return "success";
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }


        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (result.equals("success")) {

                progressdialog.dismiss();
                Snackbar.make(recyclerView, "Thanks,We will contact you shortly!", Snackbar.LENGTH_LONG).show();

                Btnflag = true;
                btnEnquireForThisTour.setBackgroundColor(getResources().getColor(R.color.gray));




            } else {
                progressdialog.dismiss();
                Snackbar.make(recyclerView, "Sorry,Please Try Later", Snackbar.LENGTH_LONG).show();
            }


        }


    }


}
