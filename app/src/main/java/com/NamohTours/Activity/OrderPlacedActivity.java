package com.NamohTours.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.NamohTours.R;

public class OrderPlacedActivity extends AppCompatActivity {

    private TextView txtHeading, txtOrder;
    private Toolbar toolbar;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        toolbar = (Toolbar) findViewById(R.id.OrderToolbar);
        setSupportActionBar(toolbar);

        String status = getIntent().getStringExtra("status");

        txtHeading = (TextView) findViewById(R.id.txtOrderHeading);
        txtOrder = (TextView) findViewById(R.id.txtOrder);
        btnContinue = (Button) findViewById(R.id.btnContinue);

        if (!(TextUtils.isEmpty(status))) {
            // If order status is TRUE
            if (status.equals("true")) {
                txtHeading.setText(R.string.order_true);
                txtOrder.setText(R.string.order_text_true);


            } else {

                txtHeading.setText(R.string.order_false);
                txtOrder.setText(R.string.order_text_false);


            }

        }

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tru = new Intent(OrderPlacedActivity.this, TourParentCategory.class);
                tru.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tru);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {

        finish();
        Intent tru = new Intent(OrderPlacedActivity.this, TourParentCategory.class);
        tru.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(tru);
        finish();
    }
}
