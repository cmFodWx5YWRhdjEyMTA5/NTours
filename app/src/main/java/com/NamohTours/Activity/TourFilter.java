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

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_deals) {

                    getLayoutInflater().inflate(R.layout.activity_tour_filter, frameLayout);
                    //overridePendingTransition(0,0);*/
                }

                if (tabId == R.id.tab_contact) {

                }
            }
        });


        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {


                    //  LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_tour_category,frameLayout,true);


           /* View child = getLayoutInflater().inflate(R.layout.activity_tour_category, null);
            frameLayout.addView(child);*/


                  /*  Intent home = new Intent(getBaseContext(),TourParentCategory.class);
                    startActivity(home);
                    overridePendingTransition(0,0);*/
                }

                if (tabId == R.id.tab_deals) {


                    LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_tour_filter, frameLayout, true);
           /* View child = getLayoutInflater().inflate(R.layout.activity_tour_filter, null);
            frameLayout.addView(child);*/

                   /* Intent home = new Intent(getBaseContext(),TourFilter.class);
                    startActivity(home);
                    overridePendingTransition(0,0);*/
                }

                if (tabId == R.id.tab_contact) {

                }
            }
        });


    }
}
