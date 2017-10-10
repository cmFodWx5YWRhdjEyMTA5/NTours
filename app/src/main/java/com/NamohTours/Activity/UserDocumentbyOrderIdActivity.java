package com.NamohTours.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.NamohTours.Adapter.CustomerUploadedDocsAdapter;
import com.NamohTours.Adapter.OrderIdUplodedDocsAdapter;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Model.GetUserUploadDocumentsByOrderIdResponse;
import com.NamohTours.Model.GetUserUploadDocumentsDetails;
import com.NamohTours.Model.GetUserUploadDocumentsResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;
import static com.NamohTours.Service.Prefs.UserID;

public class UserDocumentbyOrderIdActivity extends AppCompatActivity {


    private static final Integer READ_EXTERNAL = 0x3;
    private static final Integer WRITE_EXTERNAL = 0x4;


    private static final String TAG = UserDocumentbyOrderIdActivity.class.getSimpleName();
    private SharedPreferences preferences;
    private String restoreToken, orderId, tempName, tempUrl;
    private ArrayList<GetUserUploadDocumentsDetails> docsList;

    // Progress Dialog
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    private RecyclerView custDocs;
    private TextView txtNodocs, txtOrderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_documentby_order_id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ODocsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        custDocs = (RecyclerView) findViewById(R.id.ODocsRecyclerView);
        custDocs.setLayoutManager(new LinearLayoutManager(UserDocumentbyOrderIdActivity.this));
        custDocs.addItemDecoration(new DividerItemDecoration(UserDocumentbyOrderIdActivity.this, DividerItemDecoration.HORIZONTAL));

        txtOrderId = (TextView) findViewById(R.id.txtOrderIde);
        txtNodocs = (TextView) findViewById(R.id.txtNoODocs);
        // Token
        preferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoreToken = preferences.getString(TOKEN_KEY, null);


        // Get order id from Intent last Activity
        orderId = getIntent().getStringExtra("order_id");

        txtOrderId.setText("Order Id: \t" + orderId);


        docsList = new ArrayList<GetUserUploadDocumentsDetails>();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


            Call<GetUserUploadDocumentsByOrderIdResponse> call = apiInterface.getUserDocumentListByOrderId(restoreToken, new GetUserUploadDocumentsByOrderIdResponse(orderId));

            call.enqueue(new Callback<GetUserUploadDocumentsByOrderIdResponse>() {
                @Override
                public void onResponse(Call<GetUserUploadDocumentsByOrderIdResponse> call, final Response<GetUserUploadDocumentsByOrderIdResponse> response) {


                    if (response.body().getSuccess()) {

                        if (response.body().getData().size() > 0 && response.body().getData() != null) {


                            for (String key : response.body().getData().keySet()) {

                                docsList.add(response.body().getData().get(key));
                            }


                            custDocs.setAdapter(new OrderIdUplodedDocsAdapter(docsList, R.layout.list_item_cust_docs, UserDocumentbyOrderIdActivity.this, new ClickListener() {
                                @Override
                                public void onClick(int position) {


                                    tempName = docsList.get(position).getUpload_id() + "_" + docsList.get(position).getMask();
                                    tempUrl = docsList.get(position).getFullFileName();


                                    String path = Environment.getExternalStorageDirectory() + "/" + "NamohDocs/Order/" + orderId;
                                    File fo = new File(path);
                                    if (!fo.exists()) {
                                        fo.mkdirs();
                                    }
                                    // Output stream

                                    File newFile = new File(path, tempName);

                                    // If file not Exist then Download icon will be shown in Adapter, And download that file
                                    if (!newFile.exists()) {


                                        // Runtime Permission
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                            // If permission  Already Granted then Download file
                                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                                new UserDocumentbyOrderIdActivity.DownloadFileFromURL().execute(docsList.get(position).getFullFileName());
                                            }

                                            // Else Ask for Permission
                                            else {
                                                ActivityCompat.requestPermissions(UserDocumentbyOrderIdActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL);
                                            }
                                            //askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL);
                                        } else {
                                            new UserDocumentbyOrderIdActivity.DownloadFileFromURL().execute(docsList.get(position).getFullFileName());
                                        }
                                    }

                                    // Else view that file
                                    else {

                                        // Runtime Permission
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            // If permission  Already Granted then View File
                                            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                                openFile(tempName);
                                            }

                                            // Else Ask for Permission
                                            else {
                                                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL);
                                            }


                                            // askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL);
                                        } else {
                                            openFile(tempName);
                                        }


                                    }


                                }
                            }));


                        } else {

                            txtOrderId.setVisibility(View.GONE);
                            custDocs.setVisibility(View.GONE);
                            txtNodocs.setVisibility(View.VISIBLE);

                            // No Docs Yet!
                        }

                    } else {
                        // no documents yet
                        txtOrderId.setVisibility(View.GONE);
                        custDocs.setVisibility(View.GONE);
                        txtNodocs.setVisibility(View.VISIBLE);

                    }


                }

                @Override
                public void onFailure(Call<GetUserUploadDocumentsByOrderIdResponse> call, Throwable t) {

                    Snackbar.make(custDocs, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                }
            });


        } else {
            // No Internet Connection
            Snackbar.make(custDocs, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();
        }

    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(UserDocumentbyOrderIdActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserDocumentbyOrderIdActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(UserDocumentbyOrderIdActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(UserDocumentbyOrderIdActivity.this, new String[]{permission}, requestCode);
            }
        }

        // permission already granted
        else {


            if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                openFile(tempName);
            } else if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new UserDocumentbyOrderIdActivity.DownloadFileFromURL().execute(tempUrl);
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

                    openFile(tempName);


                    break;
                //Write External Storage
                case 4:


                    new UserDocumentbyOrderIdActivity.DownloadFileFromURL().execute(tempUrl);

                    break;
            }

            // Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            //  Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Showing Dialog
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    /**
     * Background Async Task to download file
     */
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {


                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String path = Environment.getExternalStorageDirectory() + "/" + "NamohDocs/Order/" + orderId;
                File fo = new File(path);
                if (!fo.exists()) {
                    fo.mkdirs();
                }
                // Output stream

                File newFile = new File(path, tempName);

                //if (!newFile.exists()) {

                OutputStream output = new FileOutputStream(newFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {

            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);


            // WE finish here activity because we want to change icon of image after downloading the image
            // We finish here this activity and start again this activity with getIntent for getting order id
            // from Last Intent Activity
            finish();
            startActivity(getIntent());
          /*  Intent again = new Intent(UserDocumentbyOrderIdActivity.this, UserDocumentbyOrderIdActivity.class);
            startActivity(again);
            finish();*/


        }


    }


    private void openFile(String docName) {

        String mainPath = Environment.getExternalStorageDirectory() + "/" + "NamohDocs/Order/" + orderId;
        File file = new File(mainPath, docName);
        Uri path = Uri.fromFile(file);


        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);


        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        //  pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, type);
        try {
            startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {

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


}

