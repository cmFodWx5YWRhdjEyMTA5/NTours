package com.NamohTours.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.NamohTours.Activity.Account;
import com.NamohTours.Activity.GetCartProductsActivity;
import com.NamohTours.Activity.Login;
import com.NamohTours.Activity.SearchResults;
import com.NamohTours.Activity.TourWishlistProduct;
import com.NamohTours.Activity.UserDocumentActivity;
import com.NamohTours.Activity.UserOrderHistoryActivity;
import com.NamohTours.Model.UserLoginRegisterResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;


import org.cryse.widget.persistentsearch.DefaultVoiceRecognizerDelegate;
import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.VoiceRecognitionDelegate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.TOKEN_EXPIRY;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;
import static com.NamohTours.Service.Prefs.UserRegister;

public class SearchFragment extends Fragment implements PersistentSearchView.SearchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private PersistentSearchView mPersisitantSarchView;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1023;
    private static final String TAG = SearchFragment.class.getSimpleName();

    private SharedPreferences prefs;

    private String restoretoken;

    private ConnectionDetector cd;


    private SharedPreferences.Editor editor;
    private SharedPreferences RegisterPrefences;

    private ProgressDialog progressdialog;

    public SearchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // For Toolbar Menu


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

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mPersisitantSarchView = (PersistentSearchView) rootView.findViewById(R.id.PersistentSearch);

        Toolbar toolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);

        //    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        AppCompatActivity actionBar = (AppCompatActivity) getActivity();
        actionBar.setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) actionBar.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        VoiceRecognitionDelegate delegate = new DefaultVoiceRecognizerDelegate(this, VOICE_RECOGNITION_REQUEST_CODE);
        if (delegate.isVoiceRecognitionAvailable()) {
            mPersisitantSarchView.setVoiceRecognitionDelegate(delegate);
        }


        // getting token from shared preference
        prefs = ((AppCompatActivity) getActivity()).getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        // get User Register or Not and change value to true or false

        RegisterPrefences = ((AppCompatActivity) getActivity()).getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);


        progressdialog = new ProgressDialog((AppCompatActivity) getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);


        mPersisitantSarchView.setSearchListener(this);
        return rootView;
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    // PersistentSearchView All Implemented Methods

    @Override
    public void onSearchCleared() {


    }

    @Override
    public void onSearchTermChanged(String term) {


    }

    @Override
    public void onSearch(String s) {


        if (s != null) {


            Bundle bundle = new Bundle();
            bundle.putString("search", s);
            Intent search = new Intent(getActivity(), SearchResults.class);
            search.putExtras(bundle);
            startActivity(search);
            // finish();
        }

    }

    @Override
    public void onSearchEditOpened() {

    }

    @Override
    public void onSearchEditClosed() {


    }

    @Override
    public boolean onSearchEditBackPressed() {
        return false;
    }

    @Override
    public void onSearchExit() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mPersisitantSarchView.populateEditText(matches);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

                    Snackbar.make(mPersisitantSarchView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case R.id.account:

                if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), Account.class);
                    startActivity(account);
                    //  finish();

                } else {

                    Snackbar.make(mPersisitantSarchView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }


                return true;


            case R.id.logout:
                if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    SignUp();

                } else {

                    Snackbar.make(mPersisitantSarchView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }
                return true;

            case R.id.wishlist:
                if (ConnectionDetector.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent(((AppCompatActivity) getActivity()), TourWishlistProduct.class);
                    startActivity(account);


                } else {

                    Snackbar.make(mPersisitantSarchView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


            case R.id.order_history:

                if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), UserOrderHistoryActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(mPersisitantSarchView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


            case R.id.documents:

                if (cd.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), UserDocumentActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(mPersisitantSarchView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


        }
        return super.onOptionsItemSelected(item);
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
                        Intent log = new Intent((AppCompatActivity) getActivity(), Login.class);
                        startActivity(log);
                        ((AppCompatActivity) getActivity()).finish();
                    }

                } else {
                    progressdialog.dismiss();
                    Snackbar.make(mPersisitantSarchView, "Sorry , Please Try Later..!!", Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserLoginRegisterResponse> call, Throwable t) {

                progressdialog.dismiss();

            }
        });


    }


}
