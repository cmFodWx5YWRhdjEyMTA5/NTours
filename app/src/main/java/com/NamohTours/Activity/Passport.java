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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.SmtpMail.GMailSender;
import com.NamohTours.View.ArrayListAnySize;

import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.UserContact;
import static com.NamohTours.Service.Prefs.UserName;

public class Passport extends AppCompatActivity implements View.OnClickListener {


    private Uri URI = null;
    private int GALLERY = 1;
    private static final Integer READ_EXTERNAL = 0x3;
    private EditText inputName, inputContact, inpuEmail;
    private TextView txtAttach, txtBankAttach, txtMarkSheetAttach, txtBirthCertificateAttach, txtAadharCancel, txtBankCancel, txtMarksheetCancel, txtBirthCancel;
    private Button btnSubmit;

    AutoCompleteTextView CityautocompleteView;
    String name, telephone, city, body, selectedimage, flagForImage, email;


    private static final String TAG = Passport.class.getSimpleName();
    int columnIndex;
    String attachmentFile;

    private Toolbar toolbar;

    private ConnectionDetector cd;

    private ProgressDialog progressdialog;

    private ArrayListAnySize<String> attachments;

    private SharedPreferences RegisterPrefences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);


        toolbar = (Toolbar) findViewById(R.id.PassportToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);


        name = RegisterPrefences.getString(UserName, null);
        telephone = RegisterPrefences.getString(UserContact, null);


        String[] cityArray = getResources().getStringArray(R.array.city_array);


        // Declare AutoComplete adapter and design of autocomplete textview  first then auto complete text view intailsation
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cityArray);


        // Edit Text
        inputName = (EditText) findViewById(R.id.input_Passportname);

        inputContact = (EditText) findViewById(R.id.input_Passportcontact);

        inpuEmail = (EditText) findViewById(R.id.input_Passportemail);

        // button
        btnSubmit = (Button) findViewById(R.id.btn_PaasportSubmit);


        inputName.setText(name);
        inputContact.setText(telephone);


        txtAttach = (TextView) findViewById(R.id.txtAadharAttach);

        txtBankAttach = (TextView) findViewById(R.id.txtBankAttach);

        txtBirthCertificateAttach = (TextView) findViewById(R.id.txtBirthAttach);

        txtMarkSheetAttach = (TextView) findViewById(R.id.txtMarksheetAttach);


        txtAadharCancel = (TextView) findViewById(R.id.txtAadharCancel);
        txtBankCancel = (TextView) findViewById(R.id.txtBankCancel);
        txtMarksheetCancel = (TextView) findViewById(R.id.txtMarksheetCancel);
        txtBirthCancel = (TextView) findViewById(R.id.txtBirthCancel);


        attachments = new ArrayListAnySize<>();
        // AutoCompleteTextView
        CityautocompleteView = (AutoCompleteTextView) findViewById(R.id.autoComplete_PaasportCity);


        CityautocompleteView.setAdapter(adapter);

        CityautocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Log.i("Main", "Clicked on Item ");
                city = (String) parent.getItemAtPosition(position);

            }
        });


        progressdialog = new ProgressDialog(Passport.this);
        progressdialog.setMessage("Sending Email....");
        progressdialog.setCancelable(false);

        txtAttach.setOnClickListener(this);
        txtBankAttach.setOnClickListener(this);
        txtBirthCertificateAttach.setOnClickListener(this);
        txtMarkSheetAttach.setOnClickListener(this);

        txtAadharCancel.setOnClickListener(this);
        txtBankCancel.setOnClickListener(this);
        txtMarksheetCancel.setOnClickListener(this);
        txtBirthCancel.setOnClickListener(this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = inputName.getText().toString();
                telephone = inputContact.getText().toString();


                if (cd.isConnectingToInternet(getApplicationContext())) {

                    if ((!TextUtils.isEmpty(inputName.getText().toString())) && (!TextUtils.isEmpty(inputContact.getText().toString()))) {


                        final StringBuilder body = new StringBuilder();
                        body.append("Name :" + name);
                        body.append(System.getProperty("line.separator"));
                        body.append("Contact :" + telephone);
                        body.append(System.getProperty("line.separator"));
                        body.append("Email :" + email);
                        body.append(System.getProperty("line.separator"));
                        body.append("City :" + city);


                        new SendMailAsync(body.toString(), attachments).execute();


                    } else {

                        Snackbar.make(btnSubmit, "Please enter all details ", Snackbar.LENGTH_LONG).show();

                    }
                } else {
                    Snackbar.make(btnSubmit, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
                }


            }
        });


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


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Passport.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Passport.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Passport.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Passport.this, new String[]{permission}, requestCode);
            }
        }

        // permission already granted
        else {


            if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }


            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
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

            // Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            //  Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {

                Uri selectedImage = data.getData();
                Log.e(TAG, "Gallery IMAGE :" + selectedImage.toString());


                String[] filePathColumn = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                attachmentFile = cursor.getString(columnIndex);
                Log.e("Attachment Path:", attachmentFile);

                selectedimage = attachmentFile;

                // attachList.add(selectedimage);


                URI = Uri.parse("file://" + attachmentFile);
                cursor.close();


                // set Checkbox for attached image
                if (flagForImage.equals("Aadhar")) {

                    attachments.add(0, selectedimage);
                    txtAttach.setBackgroundResource(R.drawable.ic_checkbox);
                    txtAadharCancel.setVisibility(View.VISIBLE);
                    Toast.makeText(Passport.this, "Document attached", Toast.LENGTH_SHORT).show();
                }

                if (flagForImage.equals("Bank")) {

                    attachments.add(1, selectedimage);
                    txtBankAttach.setBackgroundResource(R.drawable.ic_checkbox);
                    txtBankCancel.setVisibility(View.VISIBLE);
                    Toast.makeText(Passport.this, "Document attached", Toast.LENGTH_SHORT).show();

                }


                if (flagForImage.equals("MarkSheet")) {

                    attachments.add(2, selectedimage);
                    txtMarkSheetAttach.setBackgroundResource(R.drawable.ic_checkbox);
                    txtMarksheetCancel.setVisibility(View.VISIBLE);
                    Toast.makeText(Passport.this, "Document attached", Toast.LENGTH_SHORT).show();

                }


                if (flagForImage.equals("Birth")) {

                    attachments.add(3, selectedimage);
                    txtBirthCertificateAttach.setBackgroundResource(R.drawable.ic_checkbox);
                    txtBirthCancel.setVisibility(View.VISIBLE);
                    Toast.makeText(Passport.this, "Document attached", Toast.LENGTH_SHORT).show();

                }

            }
        }
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
    public void onClick(View v) {

        int id = v.getId();


        switch (id) {
            case R.id.txtAadharAttach:

                flagForImage = "Aadhar";

                choosePhotoFromGallary();


                break;


            case R.id.txtBankAttach:

                flagForImage = "Bank";

                choosePhotoFromGallary();

                break;

            case R.id.txtBirthAttach:

                flagForImage = "Birth";

                choosePhotoFromGallary();

                break;

            case R.id.txtMarksheetAttach:

                flagForImage = "MarkSheet";

                choosePhotoFromGallary();

                break;


            case R.id.txtAadharCancel:

                attachments.remove(0);
                txtAttach.setBackgroundResource(R.drawable.ic_attach);
                txtAadharCancel.setVisibility(View.INVISIBLE);

                Snackbar.make(btnSubmit, "Attachment removed", Snackbar.LENGTH_LONG).show();

                break;


            case R.id.txtBankCancel:

                attachments.remove(1);
                txtBankAttach.setBackgroundResource(R.drawable.ic_attach);
                txtBankCancel.setVisibility(View.INVISIBLE);
                Snackbar.make(btnSubmit, "Attachment removed", Snackbar.LENGTH_LONG).show();
                break;


            case R.id.txtMarksheetCancel:

                attachments.remove(2);

                txtMarksheetCancel.setVisibility(View.INVISIBLE);
                txtMarkSheetAttach.setBackgroundResource(R.drawable.ic_attach);

                Snackbar.make(btnSubmit, "Attachment removed", Snackbar.LENGTH_LONG).show();
                break;


            case R.id.txtBirthCancel:

                attachments.remove(3);

                txtBirthCancel.setVisibility(View.INVISIBLE);

                txtBirthCertificateAttach.setBackgroundResource(R.drawable.ic_attach);

                Snackbar.make(btnSubmit, "Attachment removed", Snackbar.LENGTH_LONG).show();

                break;
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
                sender.sendMailWithAttach("Mobile App Passport Enquiry", mailBody,
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

                inputName.setText("");
                inputContact.setText("");

                txtAttach.setBackgroundResource(R.drawable.ic_attach);
                txtBankAttach.setBackgroundResource(R.drawable.ic_attach);
                txtBirthCertificateAttach.setBackgroundResource(R.drawable.ic_attach);
                txtMarkSheetAttach.setBackgroundResource(R.drawable.ic_attach);


                txtAadharCancel.setVisibility(View.INVISIBLE);
                txtBankCancel.setVisibility(View.INVISIBLE);
                txtBirthCancel.setVisibility(View.INVISIBLE);
                txtMarksheetCancel.setVisibility(View.INVISIBLE);


                attachments.clear();

                progressdialog.dismiss();

                Snackbar.make(btnSubmit, "Passport enquiry send successfully !", Snackbar.LENGTH_LONG).show();
            } else {

                inputName.setText("");
                inputContact.setText("");

                txtAttach.setBackgroundResource(R.drawable.ic_attach);
                txtBankAttach.setBackgroundResource(R.drawable.ic_attach);
                txtBirthCertificateAttach.setBackgroundResource(R.drawable.ic_attach);
                txtMarkSheetAttach.setBackgroundResource(R.drawable.ic_attach);

                txtAadharCancel.setVisibility(View.INVISIBLE);
                txtBankCancel.setVisibility(View.INVISIBLE);
                txtBirthCancel.setVisibility(View.INVISIBLE);
                txtMarksheetCancel.setVisibility(View.INVISIBLE);

                attachments.clear();

                progressdialog.dismiss();

                Snackbar.make(btnSubmit, "Sorry,Please Try Later", Snackbar.LENGTH_LONG).show();
            }


        }


    }


   /* private class ArrayListAnySize<E> extends ArrayList<E>{
        @Override
        public void add(int index, E element){
            if(index >= 0 && index <= size()){
                super.add(index, element);
                return;
            }
            int insertNulls = index - size();
            for(int i = 0; i < insertNulls; i++){
                super.add(null);
            }
            super.add(element);
        }
    }*/

}
