package com.NamohTours.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.SmtpMail.GMailSender;

import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.UserContact;
import static com.NamohTours.Service.Prefs.UserName;

public class ForexActivity extends AppCompatActivity {


    private static final String TAG = ForexActivity.class.getSimpleName();
    Toolbar toolbar;

    private EditText inputName, inputEmail, inputContact, inputCurrency, inputAmount;
    private Spinner Options;
    private Button btnSubmit;
    private String Name, email, telephone, currency, amount;
    private ConnectionDetector cd;


    private ProgressDialog progressdialog;
    private SharedPreferences RegisterPrefences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forex);

        toolbar = (Toolbar) findViewById(R.id.forexToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inputName = (EditText) findViewById(R.id.input_ForexName);
        inputContact = (EditText) findViewById(R.id.input_ForexPhone);
        inputEmail = (EditText) findViewById(R.id.input_ForexEmail);
        inputCurrency = (EditText) findViewById(R.id.input_ForexCurrency);
        inputAmount = (EditText) findViewById(R.id.input_ForexAmount);

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);


        btnSubmit = (Button) findViewById(R.id.btnForexEnquiry);


        Name = RegisterPrefences.getString(UserName, null);
        telephone = RegisterPrefences.getString(UserContact, null);


        inputName.setText(Name);
        inputContact.setText(telephone);


        progressdialog = new ProgressDialog(ForexActivity.this);
        progressdialog.setMessage("Sending Email....");
        progressdialog.setCancelable(false);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Name = inputName.getText().toString();
                telephone = inputContact.getText().toString();
                email = inputEmail.getText().toString();
                amount = inputAmount.getText().toString();
                currency = inputCurrency.getText().toString();


                if (cd.isConnectingToInternet(getApplicationContext())) {

                    if ((!TextUtils.isEmpty(inputName.getText().toString())) && (!TextUtils.isEmpty(inputContact.getText().toString())) && (!TextUtils.isEmpty(inputCurrency.getText().toString())) && (!TextUtils.isEmpty(inputAmount.getText().toString())))

                    {
                        final StringBuilder body = new StringBuilder();
                        body.append("Name :" + Name);
                        body.append(System.getProperty("line.separator"));
                        body.append("Contact :" + telephone);
                        body.append(System.getProperty("line.separator"));
                        body.append("Email Id :" + email);
                        body.append(System.getProperty("line.separator"));
                        body.append("Required Currency :" + currency);
                        body.append(System.getProperty("line.separator"));
                        body.append("Tentative Amount :" + amount);
                        body.append(System.getProperty("line.separator"));


                        new SendMailAsync(body.toString()).execute();


                    } else {
                        Snackbar.make(btnSubmit, "Please enter all details ", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(btnSubmit, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }


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
                sender.sendMail("Mobile App Forex Enquiry", mailBody,
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

                inputName.setText("");
                inputContact.setText("");
                inputEmail.setText("");
                inputCurrency.setText("");
                inputAmount.setText("");

                progressdialog.dismiss();
                Snackbar.make(btnSubmit, "Forex enquiry send successfully !", Snackbar.LENGTH_LONG).show();

            } else {

                progressdialog.dismiss();
                Snackbar.make(btnSubmit, "Sorry,Please Try Later", Snackbar.LENGTH_LONG).show();
            }


        }


    }

}