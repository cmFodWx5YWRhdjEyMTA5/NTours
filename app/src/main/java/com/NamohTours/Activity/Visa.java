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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.Service.ValidationToolBox;
import com.NamohTours.SmtpMail.GMailSender;

import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.UserContact;
import static com.NamohTours.Service.Prefs.UserName;

public class Visa extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private EditText inputName, inputContact, inputEmail;

    private Button btnSubmit;
    private AutoCompleteTextView CityautocompleteView;
    private String name, telephone, city, email, option;
    private Spinner VisaOptions;

    private static final String TAG = Passport.class.getSimpleName();

    private ConnectionDetector cd;


    private Toolbar toolbar;

    private ProgressDialog progressdialog;
    private SharedPreferences RegisterPrefences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa);


        toolbar = (Toolbar) findViewById(R.id.VisaToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);


        name = RegisterPrefences.getString(UserName, null);
        telephone = RegisterPrefences.getString(UserContact, null);


        VisaOptions = (Spinner) findViewById(R.id.VisaOptions);


        progressdialog = new ProgressDialog(Visa.this);
        progressdialog.setMessage("Sending Email....");
        progressdialog.setCancelable(false);


        // Edit Text
        inputName = (EditText) findViewById(R.id.input_Visaname);

        inputContact = (EditText) findViewById(R.id.input_Visacontact);

        inputEmail = (EditText) findViewById(R.id.input_Visaemail);

        // button
        btnSubmit = (Button) findViewById(R.id.btn_VisaSubmit);


        inputName.setText(name);
        inputContact.setText(telephone);

        // AutoCompleteTextView
        CityautocompleteView = (AutoCompleteTextView) findViewById(R.id.autoComplete_VisaCountry);


        String[] cityArray = getResources().getStringArray(R.array.country_array);

        // Declare AutoComplete adapter and design of autocomplete textview  first then auto complete text view intailsation
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cityArray);


        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.visaOptions_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        VisaOptions.setAdapter(staticAdapter);

        // Apply OnItemclickListner on Spinner
        VisaOptions.setOnItemSelectedListener(this);


        CityautocompleteView.setAdapter(adapter);

        CityautocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                city = (String) parent.getItemAtPosition(position);

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = inputName.getText().toString();
                telephone = inputContact.getText().toString();
                email = inputEmail.getText().toString();


                // If Internet is connected

                if (cd.isConnectingToInternet(getApplicationContext())) {


                    if ((!TextUtils.isEmpty(inputName.getText().toString())) && (!TextUtils.isEmpty(inputContact.getText().toString())))

                    {

                        if (((VisaOptions.getSelectedItem() != null) && (!VisaOptions.getSelectedItem().equals("Select Option")))) {


                            boolean isValidName = ValidationToolBox.validateFullName(name);
                            boolean isValidContact = ValidationToolBox.validateMobNo(telephone);

                            if (isValidName) {

                                if (isValidContact) {

                                    // If Email is Empty not need to validation
                                    if (TextUtils.isEmpty(email)) {
                                        final StringBuilder body = new StringBuilder();
                                        body.append("Name :" + name);
                                        body.append(System.getProperty("line.separator"));
                                        body.append("Contact :" + telephone);
                                        body.append(System.getProperty("line.separator"));
                                        body.append("Email :" + email);
                                        body.append(System.getProperty("line.separator"));
                                        body.append("City :" + city);

                                        new SendMailAsync(body.toString()).execute();

                                    }


                                    // Else Validate Email Id
                                    else {
                                        boolean isValidEmail = ValidationToolBox.validateEmailId(email);

                                        if (isValidEmail) {
                                            final StringBuilder body = new StringBuilder();
                                            body.append("Name :" + name);
                                            body.append(System.getProperty("line.separator"));
                                            body.append("Contact :" + telephone);
                                            body.append(System.getProperty("line.separator"));
                                            body.append("Email :" + email);
                                            body.append(System.getProperty("line.separator"));
                                            body.append("City :" + city);

                                            new SendMailAsync(body.toString()).execute();

                                        } else {
                                            inputEmail.setError(getResources().getString(R.string.invalid_email));
                                        }
                                    }
                                } else {
                                    inputContact.setError(getResources().getString(R.string.invalid_mobile));
                                }


                            } else {
                                inputName.setError(getResources().getString(R.string.invalid_name));
                            }


                        } else {

                            Snackbar.make(VisaOptions, "Select visa option", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(btnSubmit, "Enter all details", Snackbar.LENGTH_LONG).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {

        } else {
            option = parent.getItemAtPosition(position).toString();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                sender.sendMail("Mobile App Visa Enquiry", mailBody,
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
                progressdialog.dismiss();
                Snackbar.make(btnSubmit, "Visa enquiry send successfully !", Snackbar.LENGTH_LONG).show();


            } else {
                progressdialog.dismiss();
                Snackbar.make(btnSubmit, "Sorry,Please Try Later", Snackbar.LENGTH_LONG).show();
            }


        }


    }


}

