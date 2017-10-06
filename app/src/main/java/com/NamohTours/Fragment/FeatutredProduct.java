package com.NamohTours.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.NamohTours.Activity.Account;
import com.NamohTours.Activity.GetCartProductsActivity;
import com.NamohTours.Activity.Login;
import com.NamohTours.Activity.ProductDescription;
import com.NamohTours.Activity.TourParentCategory;
import com.NamohTours.Activity.TourWishlistProduct;
import com.NamohTours.Activity.UserDocumentActivity;
import com.NamohTours.Activity.UserOrderHistoryActivity;
import com.NamohTours.Adapter.FeaturedProductAdapter;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Model.TourFeaturedProductResponse;
import com.NamohTours.Model.TourFeaturedProductsListResponse;
import com.NamohTours.Model.TourProductDetailResponse;
import com.NamohTours.Model.UserLoginRegisterResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;
import com.joanzapata.iconify.widget.IconButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.TOKEN_EXPIRY;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;
import static com.NamohTours.Service.Prefs.UserRegister;


public class FeatutredProduct extends Fragment {


    private static final String TAG = FeatutredProduct.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private RecyclerView mRecylerView;
    private SharedPreferences prefs;

    private String restoretoken;

    private ConnectionDetector cd;


    private SharedPreferences.Editor editor;
    private SharedPreferences RegisterPrefences;

    private ProgressBar tourProgress;

    private String success;

    private TourFeaturedProductResponse registerResponse;


    private List<TourFeaturedProductsListResponse> FeaturedList;
    private ProgressDialog progressdialog;
    private RelativeLayout badgeLayout;
    private TextView itemMessagesBadgeTextView;
    private IconButton iconButtonMessages;


    public FeatutredProduct() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FeatutredProduct newInstance(String param1, String param2) {
        FeatutredProduct fragment = new FeatutredProduct();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_featutred_product, container, false);

        mRecylerView = (RecyclerView) rootView.findViewById(R.id.FeaturedProducts);


        mRecylerView.setLayoutManager(new LinearLayoutManager((AppCompatActivity) getActivity()));

        mRecylerView.addItemDecoration(new DividerItemDecoration((AppCompatActivity) getActivity(), DividerItemDecoration.HORIZONTAL));

        ViewCompat.setNestedScrollingEnabled(mRecylerView, false);

        tourProgress = (ProgressBar) rootView.findViewById(R.id.tourFeatured_Progress);

        tourProgress.setVisibility(View.VISIBLE);


        // getting token from shared preference
        prefs = ((AppCompatActivity) getActivity()).getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        // get User Register or Not and change value to true or false

        RegisterPrefences = ((AppCompatActivity) getActivity()).getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);


        progressdialog = new ProgressDialog((AppCompatActivity) getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);


        FeaturedList = new ArrayList<TourFeaturedProductsListResponse>();
        registerResponse = new TourFeaturedProductResponse();


        // load gif in webview
        /*web = (WebView)rootView.findViewById(R.id.FeturedWeb);
        web.clearCache(false);
        Log.e("FP","Webview Calling ");
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("file:///android_asset/resize.gif");


*/


        initData();

        return rootView;
    }


    private void initData() {

        //Creating Api Interface
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {


            Call<TourFeaturedProductResponse> call = apiService.getFeaturedProduct(restoretoken);


            call.enqueue(new Callback<TourFeaturedProductResponse>() {
                @Override
                public void onResponse(Call<TourFeaturedProductResponse> call, Response<TourFeaturedProductResponse> response) {

                    registerResponse = response.body();


                    TourProductDetailResponse detailResponse;

                    String sucess, error;

                    if ((sucess = registerResponse.getSuccess()) != null) {

                        if (sucess.equals("true")) {


                            FeaturedList = registerResponse.getTourCategoryDetailResponses().get(0).getFeaturedProductsListResponses();


                            tourProgress.setVisibility(View.GONE);


                            mRecylerView.setAdapter(new FeaturedProductAdapter(FeaturedList, R.layout.list_item_tour_product, ((AppCompatActivity) getActivity()), new ClickListener() {
                                @Override
                                public void onClick(int position) {


                                    TourFeaturedProductsListResponse res = FeaturedList.get(position);


                                    if (res != null) {


                                        String product_id = res.getProduct_id();
                                        Intent description = new Intent((AppCompatActivity) getActivity(), ProductDescription.class);
                                        description.putExtra("product_id", product_id);
                                        description.putExtra("Sub", "Featured");
                                        startActivity(description);


                                    }


                                }
                            }));

                        }

                        if (sucess.equals("false")) {

                            tourProgress.setVisibility(View.GONE);


                            if ((error = registerResponse.getError()) != null) {
                                //tourProgress.setVisibility(View.GONE);
                                Snackbar.make(mRecylerView, error, Snackbar.LENGTH_LONG).show();
                            }

                        }
                    }


                }

                @Override
                public void onFailure(Call<TourFeaturedProductResponse> call, Throwable t) {


                    tourProgress.setVisibility(View.GONE);


                }
            });


        } else {


            tourProgress.setVisibility(View.GONE);

            Snackbar.make(mRecylerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        // super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        // menu.clear();
        inflater.inflate(R.menu.fragment_tour, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //   return true;
        // }
        switch (id) {


            case R.id.carts:


                if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), GetCartProductsActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(mRecylerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


            case R.id.account:
                //deleteall();

                if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), Account.class);
                    startActivity(account);
                    //  finish();

                } else {

                    Snackbar.make(mRecylerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }


                return true;


            case R.id.logout:
                if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    SignUp();

                } else {

                    Snackbar.make(mRecylerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }
                return true;

            case R.id.wishlist:
                if (ConnectionDetector.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent(((AppCompatActivity) getActivity()), TourWishlistProduct.class);
                    startActivity(account);


                } else {

                    Snackbar.make(mRecylerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


            case R.id.order_history:

                if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), UserOrderHistoryActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(mRecylerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


            case R.id.documents:

                if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), UserDocumentActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(mRecylerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;




        }
        return super.onOptionsItemSelected(item);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                        Intent log = new Intent((AppCompatActivity) getActivity(), Login.class);
                        startActivity(log);
                        ((AppCompatActivity) getActivity()).finish();
                    }

                } else {
                    progressdialog.dismiss();
                    Snackbar.make(mRecylerView, "Sorry , Please Try Later..!!", Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserLoginRegisterResponse> call, Throwable t) {

                progressdialog.dismiss();
                Snackbar.make(mRecylerView, "Sorry , Please Try Later..!!", Snackbar.LENGTH_LONG).show();
                // Log.e(TAG, "Error : " + t.toString());
            }
        });


    }


}
