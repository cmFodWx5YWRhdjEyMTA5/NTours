package com.NamohTours.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NamohTours.Activity.Account;
import com.NamohTours.Activity.GetCartProductsActivity;
import com.NamohTours.Activity.Login;
import com.NamohTours.Activity.TourWishlistProduct;
import com.NamohTours.Activity.UserDocumentActivity;
import com.NamohTours.Activity.UserOrderHistoryActivity;
import com.NamohTours.Activity.WebUrl;
import com.NamohTours.Model.UserLoginRegisterResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.TOKEN_EXPIRY;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;
import static com.NamohTours.Service.Prefs.UserRegister;

public class MoreFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedPreferences RegisterPrefences;
    private ProgressDialog progressdialog;

    private TextView txtAbout, txtBlog, txtTerms, txtPrivacy, txtEvent;
    private String restoretoken;
    private SharedPreferences prefs;


    public MoreFragment() {
        // Required empty public constructor
    }


    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_more, container, false);

        txtAbout = (TextView) rootView.findViewById(R.id.txtAbout);
        txtBlog = (TextView) rootView.findViewById(R.id.txtBlog);
        txtPrivacy = (TextView) rootView.findViewById(R.id.txtPrivacy);
        txtTerms = (TextView) rootView.findViewById(R.id.txtTerms);
        txtEvent = (TextView) rootView.findViewById(R.id.txtEvent);


        txtAbout.setOnClickListener(this);
        txtBlog.setOnClickListener(this);
        txtPrivacy.setOnClickListener(this);
        txtTerms.setOnClickListener(this);
        txtEvent.setOnClickListener(this);

        // getting token from shared preference
        prefs = ((AppCompatActivity) getActivity()).getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);

        // get User Register or Not and change value to true or false

        RegisterPrefences = ((AppCompatActivity) getActivity()).getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);


        progressdialog = new ProgressDialog((AppCompatActivity) getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);


        return rootView;
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

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.txtAbout:

                Intent about = new Intent(getActivity(), WebUrl.class);
                about.putExtra("Web", "about");
                getActivity().startActivity(about);
                break;

            case R.id.txtBlog:

                break;

            case R.id.txtPrivacy:

                Intent privacy = new Intent(getActivity(), WebUrl.class);
                privacy.putExtra("Web", "privacy");
                getActivity().startActivity(privacy);
                break;

            case R.id.txtTerms:
                Intent terms = new Intent(getActivity(), WebUrl.class);
                terms.putExtra("Web", "terms");
                getActivity().startActivity(terms);
                break;

            case R.id.txtEvent:
                Intent event = new Intent(getActivity(), WebUrl.class);
                event.putExtra("Web", "event");
                getActivity().startActivity(event);

                break;


            // https://goo.gl/forms/aZX64VWfRcD5PqC83


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


                if (ConnectionDetector.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), GetCartProductsActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(txtTerms, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;
            case R.id.account:
                //deleteall();

                if (ConnectionDetector.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), Account.class);
                    startActivity(account);
                    //  finish();

                } else {

                    Snackbar.make(txtTerms, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }


                return true;


            case R.id.logout:
                if (ConnectionDetector.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    SignUp();

                } else {

                    Snackbar.make(txtTerms, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }
                return true;

            case R.id.wishlist:
                if (ConnectionDetector.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent(((AppCompatActivity) getActivity()), TourWishlistProduct.class);
                    startActivity(account);


                } else {

                    Snackbar.make(txtTerms, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case R.id.order_history:

                if (ConnectionDetector.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), UserOrderHistoryActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(txtTerms, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                return true;


            case R.id.documents:

                if (ConnectionDetector.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent((AppCompatActivity) getActivity(), UserDocumentActivity.class);
                    startActivity(account);


                } else {

                    Snackbar.make(txtTerms, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

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
                        Intent log = new Intent((AppCompatActivity) getActivity(), Login.class);
                        startActivity(log);
                        ((AppCompatActivity) getActivity()).finish();
                    }

                } else {
                    progressdialog.dismiss();
                    Snackbar.make(txtTerms, "Sorry , Please Try Later..!!", Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserLoginRegisterResponse> call, Throwable t) {

                progressdialog.dismiss();
                Snackbar.make(txtTerms, "Sorry , Please Try Later..!!", Snackbar.LENGTH_LONG).show();
                // Log.e(TAG, "Error : " + t.toString());
            }
        });


    }
}

