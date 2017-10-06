package com.NamohTours.Activity;

import android.support.annotation.IdRes;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.NamohTours.R;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class TourFilter extends LeftDrawer {


    private static final String TAG = TourFilter.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tour_filter);

        getLayoutInflater().inflate(R.layout.activity_tour_filter, frameLayout);
        //bottomBar.getCurrentTab().setActiveColor(getResources().getColor(R.color.colorPrimary));
        // bottomBar.setDefaultTab(R.id.tab_deals);

        // bottomBar.setDefaultTab(R.id.tab_deals);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.FilterToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_deals) {

                    Log.e(TAG, "Selected at Deals");
                    getLayoutInflater().inflate(R.layout.activity_tour_filter, frameLayout);
                    //overridePendingTransition(0,0);*/
                }

                if (tabId == R.id.tab_contact) {
                    Log.e(TAG, "Selected at Contact");


                   /* Intent home = new Intent(TourFilter.this,Enquiry.class);
                    startActivity(home);
                    finish();
                    overridePendingTransition(0,0);*/

                }
            }
        });


        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {

                    Log.e(TAG, "ReSelected at Home");
                    //  LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_tour_category,frameLayout,true);


           /* View child = getLayoutInflater().inflate(R.layout.activity_tour_category, null);
            frameLayout.addView(child);*/


                  /*  Intent home = new Intent(getBaseContext(),TourParentCategory.class);
                    startActivity(home);
                    overridePendingTransition(0,0);*/
                }

                if (tabId == R.id.tab_deals) {

                    Log.e(TAG, "ReSelected at Deals");
                    LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_tour_filter, frameLayout, true);
           /* View child = getLayoutInflater().inflate(R.layout.activity_tour_filter, null);
            frameLayout.addView(child);*/

                   /* Intent home = new Intent(getBaseContext(),TourFilter.class);
                    startActivity(home);
                    overridePendingTransition(0,0);*/
                }

                if (tabId == R.id.tab_contact) {
                    Log.e(TAG, "ReSelected at Contact");
                }
            }
        });


    }
}
