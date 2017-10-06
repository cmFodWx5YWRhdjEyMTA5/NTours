package com.NamohTours.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.SmtpMail.GMailSender;
import com.NamohTours.View.ArrayListAnySize;

import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.UserContact;
import static com.NamohTours.Service.Prefs.UserName;

public class Enquiry extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private static final Integer READ_EXTERNAL = 0x3;
    private static final String TAG = Enquiry.class.getSimpleName();
    int columnIndex;
    String attachmentFile;
    Toolbar toolbar;
    private Uri URI = null;
    private int GALLERY = 1;
    private ArrayListAnySize<String> attachList;
    private EditText inputName, inputEmail, inputContact, inputFeedback;
    private Spinner Options;
    private Button btnSubmit;
    private TextView txtTourAttach, txtCancel;
    private String Name, email, telephone, Feedback, options, selectedimage;
    private ConnectionDetector cd;


    private ProgressDialog progressdialog;
    private SharedPreferences RegisterPrefences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);

        toolbar = (Toolbar) findViewById(R.id.enquiryToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inputName = (EditText) findViewById(R.id.input_EnqName);
        inputContact = (EditText) findViewById(R.id.input_EnqPhone);
        inputEmail = (EditText) findViewById(R.id.input_EnqEmail);
        inputFeedback = (EditText) findViewById(R.id.input_EnqComment);

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);

        Options = (Spinner) findViewById(R.id.Options);

        txtTourAttach = (TextView) findViewById(R.id.txtTourAttachment);

        txtCancel = (TextView) findViewById(R.id.txtAttchCancel);

        btnSubmit = (Button) findViewById(R.id.btnEnquiry);

        Name = RegisterPrefences.getString(UserName, null);
        telephone = RegisterPrefences.getString(UserContact, null);


        inputName.setText(Name);
        inputContact.setText(telephone);

        attachList = new ArrayListAnySize<>();
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.options_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        Options.setAdapter(staticAdapter);

        // Apply OnItemclickListner on Spinner
        Options.setOnItemSelectedListener(this);


        txtTourAttach.setOnClickListener(this);
        txtCancel.setOnClickListener(this);

        progressdialog = new ProgressDialog(Enquiry.this);
        progressdialog.setMessage("Sending Email....");
        progressdialog.setCancelable(false);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Name = inputName.getText().toString();
                telephone = inputContact.getText().toString();
                email = inputEmail.getText().toString();
                Feedback = inputFeedback.getText().toString();


                if (cd.isConnectingToInternet(getApplicationContext())) {

                    if ((!TextUtils.isEmpty(inputName.getText().toString())) && (!TextUtils.isEmpty(inputContact.getText().toString())) && ((Options.getSelectedItem() != null) && (!Options.getSelectedItem().equals("Select Option")))) {


                        final StringBuilder body = new StringBuilder();
                        body.append("Name :" + Name);
                        body.append(System.getProperty("line.separator"));
                        body.append("Contact :" + telephone);
                        body.append(System.getProperty("line.separator"));
                        body.append("Email Id :" + email);
                        body.append(System.getProperty("line.separator"));
                        body.append("Option :" + options);
                        body.append(System.getProperty("line.separator"));
                        body.append("Comment :" + Feedback);
                        body.append(System.getProperty("line.separator"));


                        new SendMailAsync(body.toString(), attachList).execute();


                        // Using SMTP
                      /*  new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {

                                  //  progressdialog.show();
                                    GMailSender sender = new GMailSender("contact@namoh.co.in",
                                            "namoh@1212");
                                    sender.sendMailWithAttach("Namoh Tours Enquiry", body.toString(),
                                            "contact@namoh.co.in", "contact@namoh.co.in", attachList);


                                    txtTourAttach.setBackgroundResource(R.drawable.ic_attach);
                                    attachList.clear();


                                      //  progressdialog.dismiss();


                                    Snackbar.make(btnSubmit, "Enquiry Send Successfully !", Snackbar.LENGTH_LONG).show();


                                } catch (Exception e) {
                                    Log.e("SendMail", e.getMessage(), e);
                                }
                            }

                        }).start();*/


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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position == 0) {

        } else {
            options = parent.getItemAtPosition(position).toString();

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {


        int id = v.getId();


        if (id == R.id.txtTourAttachment) {
            choosePhotoFromGallary();
        }

        if (id == R.id.txtAttchCancel) {
            attachList.clear();
            txtCancel.setVisibility(View.GONE);
            txtTourAttach.setBackgroundResource(R.drawable.ic_attach);
            Snackbar.make(btnSubmit, "Attachment removed", Snackbar.LENGTH_LONG).show();

        }

    }

    public void choosePhotoFromGallary() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, GALLERY);
        }


    }


    // Marshmallow runtime Permission
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Enquiry.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Enquiry.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Enquiry.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Enquiry.this, new String[]{permission}, requestCode);
            }
        }

        // permission already granted
        else {


            if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }


            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    // Gallery Selected Image Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {

                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                attachmentFile = cursor.getString(columnIndex);
                Log.e("Attachment Path:", attachmentFile);

                selectedimage = attachmentFile;

                attachList.add(selectedimage);


                URI = Uri.parse("file://" + attachmentFile);
                cursor.close();


                txtTourAttach.setBackgroundResource(R.drawable.ic_checkbox);
                txtCancel.setVisibility(View.VISIBLE);
                // Toast.makeText(Enquiry.this, "Document attached", Toast.LENGTH_SHORT).show();

            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                // GALLERY
                case 1:
                    //  askForGPS();
                    break;
                //Call
                case 2:


                    //Read External Storage
                case 3:


                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(galleryIntent, GALLERY);
                    break;
                //Camera
                case 4:


            }

            //  Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }


    private class SendMailAsync extends AsyncTask<Void, Void, String> {


        private String mailBody;
        private ArrayListAnySize<String> mailAttach;


        private SendMailAsync(String body, ArrayListAnySize<String> attachList) {

            this.mailBody = body;
            this.mailAttach = attachList;

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
                sender.sendMailWithAttach("Namoh Tours Enquiry", mailBody,
                        "contact@namoh.co.in", "contact@namoh.co.in", mailAttach);

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

                txtTourAttach.setBackgroundResource(R.drawable.ic_attach);
                inputName.setText("");
                inputContact.setText("");
                inputEmail.setText("");
                inputFeedback.setText("");
                txtCancel.setVisibility(View.GONE);
                attachList.clear();
                progressdialog.dismiss();
                Snackbar.make(btnSubmit, "Enquiry send successfully !", Snackbar.LENGTH_LONG).show();

            } else {
                txtTourAttach.setBackgroundResource(R.drawable.ic_attach);
                txtCancel.setVisibility(View.GONE);
                attachList.clear();
                progressdialog.dismiss();
                Snackbar.make(btnSubmit, "Sorry,Please Try Later", Snackbar.LENGTH_LONG).show();
            }


        }


    }

}