package com.NamohTours.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.NamohTours.Model.TourProductOptionsResponse;
import com.NamohTours.R;

import java.util.ArrayList;

public class ProductOptionsActivity extends AppCompatActivity {

    private final static String TAG = ProductOptionsActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView txtPrice;
    ArrayList<TourProductOptionsResponse> sample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_options);

        toolbar = (Toolbar) findViewById(R.id.OptionsToolbar);
        txtPrice = (TextView) findViewById(R.id.txtOptionsPrice);

        setSupportActionBar(toolbar);


        sample = new ArrayList<TourProductOptionsResponse>();
        sample = (ArrayList<TourProductOptionsResponse>) getIntent().getSerializableExtra("arr");



    }
}
