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
import com.NamohTours.Service.ValidationToolBox;
import com.NamohTours.SmtpMail.GMailSender;
import com.NamohTours.View.ArrayListAnySize;

import java.util.ArrayList;
import java.util.HashMap;

import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.UserContact;
import static com.NamohTours.Service.Prefs.UserName;

public class Passport extends AppCompatActivity implements View.OnClickListener {


    private static final Integer READ_EXTERNAL = 0x3;
    private static final String TAG = Passport.class.getSimpleName();
    AutoCompleteTextView CityautocompleteView;
    String name, telephone, city, body, selectedimage, flagForImage, email;
    int columnIndex;
    String attachmentFile;
    private Uri URI = null;
    private int GALLERY = 1;
    private EditText inputName, inputContact, inpuEmail;
    private TextView txtAttach, txtBankAttach, txtMarkSheetAttach, txtBirthCertificateAttach, txtAadharCancel, txtBankCancel, txtMarksheetCancel, txtBirthCancel;
    private Button btnSubmit;
    private Toolbar toolbar;

    private ConnectionDetector cd;

    private ProgressDialog progressdialog;

    private ArrayList<String> attachments;

    private HashMap<String, String> DocsAttachmentList;


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


        txtAttach.setTag("Aadhar");
        txtBankAttach.setTag("Bank");
        txtBirthCertificateAttach.setTag("Birth");
        txtMarkSheetAttach.setTag("Mark");



        txtAadharCancel = (TextView) findViewById(R.id.txtAadharCancel);
        txtBankCancel = (TextView) findViewById(R.id.txtBankCancel);
        txtMarksheetCancel = (TextView) findViewById(R.id.txtMarksheetCancel);
        txtBirthCancel = (TextView) findViewById(R.id.txtBirthCancel);


        attachments = new ArrayList<>();
        DocsAttachmentList = new HashMap<String, String>();


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
                email = inpuEmail.getText().toString();


                if (cd.isConnectingToInternet(getApplicationContext())) {

                    if ((!TextUtils.isEmpty(inputName.getText().toString())) && (!TextUtils.isEmpty(inputContact.getText().toString()))) {


                        boolean isValidName = ValidationToolBox.validateFullName(name);
                        boolean isValidContact = ValidationToolBox.validateMobNo(telephone);

                        if (DocsAttachmentList.size() > 0) {
                            // Add all values in ArrayList<String>

                            for (String values : DocsAttachmentList.values()) {
                                attachments.add(values);
                            }

                        }



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

                                    new SendMailAsync(body.toString(), attachments).execute();
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

                                        new SendMailAsync(body.toString(), attachments).execute();


                                    } else {
                                        inpuEmail.setError(getResources().getString(R.string.invalid_email));
                                    }


                                }


                            } else {
                                inputContact.setError(getResources().getString(R.string.invalid_mobile));
                            }


                        } else {
                            inputName.setError(getResources().getString(R.string.invalid_name));
                        }


                    } else {

                        Snackbar.make(btnSubmit, "Enter all details ", Snackbar.LENGTH_LONG).show();

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

                String[] filePathColumn = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                attachmentFile = cursor.getString(columnIndex);

                selectedimage = attachmentFile;

                // attachList.add(selectedimage);


                URI = Uri.parse("file://" + attachmentFile);
                cursor.close();


                // set Checkbox for attached image
                if (flagForImage.equals("Aadhar")) {

                    DocsAttachmentList.put("Aadhar", selectedimage);

                    //attachments.add(0, selectedimage);
                    txtAttach.setBackgroundResource(R.drawable.ic_checkbox);
                    txtAttach.setTag("AadharCheck");
                    txtAadharCancel.setVisibility(View.VISIBLE);
                    Toast.makeText(Passport.this, "Document attached", Toast.LENGTH_SHORT).show();
                }

                if (flagForImage.equals("Bank")) {

                    DocsAttachmentList.put("Bank", selectedimage);

                    //  attachments.add(1, selectedimage);
                    txtBankAttach.setBackgroundResource(R.drawable.ic_checkbox);
                    txtBankAttach.setTag("BankCheck");
                    txtBankCancel.setVisibility(View.VISIBLE);
                    Toast.makeText(Passport.this, "Document attached", Toast.LENGTH_SHORT).show();

                }


                if (flagForImage.equals("MarkSheet")) {

                    DocsAttachmentList.put("Mark", selectedimage);

                    // attachments.add(2, selectedimage);
                    txtMarkSheetAttach.setBackgroundResource(R.drawable.ic_checkbox);
                    txtMarkSheetAttach.setTag("MarkCheck");
                    txtMarksheetCancel.setVisibility(View.VISIBLE);
                    Toast.makeText(Passport.this, "Document attached", Toast.LENGTH_SHORT).show();

                }


                if (flagForImage.equals("Birth")) {

                    DocsAttachmentList.put("Birth", selectedimage);

                    // attachments.add(3, selectedimage);
                    txtBirthCertificateAttach.setBackgroundResource(R.drawable.ic_checkbox);
                    txtBirthCertificateAttach.setTag("BirthCheck");
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


                if (txtAttach.getTag().toString().contains("Check")) {

                    // If Text view tag is Check then do nothing , and texview image is checkbox then

                }

                //  else text view image is attach then open gallery
                else {
                    choosePhotoFromGallary();
                }


                break;


            case R.id.txtBankAttach:

                flagForImage = "Bank";

                if (txtBankAttach.getTag().toString().contains("Check")) {
                    // If Text view tag is Check then do nothing , and texview image is checkbox then
                }

                //  else text view image is attach then open gallery
                else {
                    choosePhotoFromGallary();
                }


                break;

            case R.id.txtBirthAttach:

                flagForImage = "Birth";

                if (txtBirthCertificateAttach.getTag().toString().contains("Check")) {
                    // If Text view tag is Check then do nothing , and texview image is checkbox then
                }


                //  else text view image is attach then open gallery
                else {
                    choosePhotoFromGallary();
                }

                break;

            case R.id.txtMarksheetAttach:


                flagForImage = "MarkSheet";

                if (txtMarkSheetAttach.getTag().toString().contains("Check")) {
                    // If Text view tag is Check then do nothing , and texview image is checkbox then
                }


                //  else text view image is attach then open gallery
                else {
                    choosePhotoFromGallary();
                }

                break;


            case R.id.txtAadharCancel:


                DocsAttachmentList.remove("Aadhar");

                // attachments.remove(0);

                txtAadharCancel.setVisibility(View.INVISIBLE);
                txtAttach.setBackgroundResource(R.drawable.ic_attach);
                // After removing attachment set again original tag to textview
                txtAttach.setTag("Aadhar");

                Toast.makeText(Passport.this, "Attachment removed", Toast.LENGTH_SHORT).show();

                // Snackbar.make(btnSubmit, "Attachment removed", Snackbar.LENGTH_LONG).show();

                break;


            case R.id.txtBankCancel:


                DocsAttachmentList.remove("Bank");
                //attachments.remove(1);

                txtBankCancel.setVisibility(View.INVISIBLE);
                txtBankAttach.setBackgroundResource(R.drawable.ic_attach);
                // After removing attachment set again original tag to textview
                txtBankAttach.setTag("Bank");

                Toast.makeText(Passport.this, "Attachment removed", Toast.LENGTH_SHORT).show();
                //  Snackbar.make(btnSubmit, "Attachment removed", Snackbar.LENGTH_LONG).show();
                break;


            case R.id.txtMarksheetCancel:


                DocsAttachmentList.remove("Mark");
                // attachments.remove(2);

                txtMarksheetCancel.setVisibility(View.INVISIBLE);
                txtMarkSheetAttach.setBackgroundResource(R.drawable.ic_attach);
                // After removing attachment set again original tag to textview
                txtMarkSheetAttach.setTag("Mark");

                Toast.makeText(Passport.this, "Attachment removed", Toast.LENGTH_SHORT).show();
                //  Snackbar.make(btnSubmit, "Attachment removed", Snackbar.LENGTH_LONG).show();
                break;


            case R.id.txtBirthCancel:

                DocsAttachmentList.remove("Birth");

                // attachments.remove(3);

                txtBirthCancel.setVisibility(View.INVISIBLE);

                txtBirthCertificateAttach.setBackgroundResource(R.drawable.ic_attach);
                // After removing attachment set again original tag to textview
                txtBirthCertificateAttach.setTag("Birth");
                Toast.makeText(Passport.this, "Attachment removed", Toast.LENGTH_SHORT).show();
                //  Snackbar.make(btnSubmit, "Attachment removed", Snackbar.LENGTH_LONG).show();

                break;
        }


    }


    private class SendMailAsync extends AsyncTask<Void, Void, String> {


        private String mailBody;
        private ArrayList<String> mailAttach;


        private SendMailAsync(String body, ArrayList<String> attachList) {

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
                inpuEmail.setText("");
                CityautocompleteView.setText("");


                txtAttach.setBackgroundResource(R.drawable.ic_attach);
                txtBankAttach.setBackgroundResource(R.drawable.ic_attach);
                txtBirthCertificateAttach.setBackgroundResource(R.drawable.ic_attach);
                txtMarkSheetAttach.setBackgroundResource(R.drawable.ic_attach);

                txtAttach.setTag("Aadhar");
                txtBankAttach.setTag("Bank");
                txtBirthCertificateAttach.setTag("Birth");
                txtMarkSheetAttach.setTag("Mark");


                txtAadharCancel.setVisibility(View.INVISIBLE);
                txtBankCancel.setVisibility(View.INVISIBLE);
                txtBirthCancel.setVisibility(View.INVISIBLE);
                txtMarksheetCancel.setVisibility(View.INVISIBLE);


                attachments.clear();
                DocsAttachmentList.clear();

                progressdialog.dismiss();

                Snackbar.make(btnSubmit, "Passport enquiry send successfully !", Snackbar.LENGTH_LONG).show();
            } else {

                inputName.setText("");
                inputContact.setText("");
                inpuEmail.setText("");

                txtAttach.setBackgroundResource(R.drawable.ic_attach);
                txtBankAttach.setBackgroundResource(R.drawable.ic_attach);
                txtBirthCertificateAttach.setBackgroundResource(R.drawable.ic_attach);
                txtMarkSheetAttach.setBackgroundResource(R.drawable.ic_attach);


                txtAttach.setTag("Aadhar");
                txtBankAttach.setTag("Bank");
                txtBirthCertificateAttach.setTag("Birth");
                txtMarkSheetAttach.setTag("Mark");

                txtAadharCancel.setVisibility(View.INVISIBLE);
                txtBankCancel.setVisibility(View.INVISIBLE);
                txtBirthCancel.setVisibility(View.INVISIBLE);
                txtMarksheetCancel.setVisibility(View.INVISIBLE);

                attachments.clear();
                DocsAttachmentList.clear();

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
