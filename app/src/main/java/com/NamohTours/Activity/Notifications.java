package com.NamohTours.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NamohTours.Database.DBManager;
import com.NamohTours.Database.DatabaseHelper;
import com.NamohTours.R;

public class Notifications extends LeftDrawer {


    private static final String TAG = Notifications.class.getSimpleName();

    public DBManager dbManager;
    TextView idTextView, titleTextView, descTextView;
    public ListView listView;


    public SimpleCursorAdapter adapter;

    private DatabaseHelper dbHelper;


    private Context context;
    private SQLiteDatabase database;


    final String[] from = new String[]{DatabaseHelper._ID,
            DatabaseHelper.MESSAGE, DatabaseHelper.TIME};

    final int[] to = new int[]{R.id.txtNotificationid, R.id.txtNotificationtitle, R.id.txtNotificationdesc};
    private long _id;

    int idt;
    String url, msg;


    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_notifications);
        getLayoutInflater().inflate(R.layout.activity_notifications, frameLayout);


        //  FooterBar.setVisibility(View.GONE);

        context = getApplicationContext();


        dbManager = new DBManager(this);
        dbManager.open();
        final Cursor cursor = dbManager.fetch();


        // get Data from notification
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();

        listView = (ListView) findViewById(R.id.Notification_list_view);
        listView.setEmptyView(findViewById(R.id.empty));


        adapter = new SimpleCursorAdapter(this, R.layout.list_item_notification, cursor, from, to, 0);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // listView.setAdapter(adapter);
        //  urltype = getIntent().getStringExtra("UrlType");
       /* url = getIntent().getStringExtra("url");
        msg= getIntent().getStringExtra("msg");

*/

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Notifications.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                final long ID = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ii) {

                        // Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                        // Get the state's capital from this listview_item_row in the database.

                        dbManager.delete(ID); // sqlcon is one my class, InfoAPI object

                        adapter.notifyDataSetChanged();
                        // cursor.close();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int ii) {
                                dialog.dismiss();
                            }
                        }
                );
                builder.show();
                //  cursor.close();
                cursor.requery();

                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final Cursor cur = (Cursor) parent.getItemAtPosition(position);
                final String url = cur.getString(cur.getColumnIndexOrThrow("url"));
                //Log.e(TAG,"Url on Notification click :"+url);


                if (url.equals("http://www.google.com")) {
                    // Not Do Anything
                } else {
                    Intent OpenUrl = new Intent(getApplicationContext(), WebUrl.class);
                    OpenUrl.putExtra("Web", "notification");
                    OpenUrl.putExtra("url", url);
                    startActivity(OpenUrl);
                }

               /* Intent OpenUrl = new Intent(getApplicationContext(),WebUrl.class);
                OpenUrl.putExtra("Web","notification");
                OpenUrl.putExtra("url",url);
                startActivity(OpenUrl);*/


            }
        });

        //   database.close();

    }


    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onPause() {


        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //   return true;
        // }
        switch (id) {

            case R.id.all:
                deleteall();
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteall() {
        // AlertDialog.Builder builder = new AlertDialog.Builder(this);


        Cursor c = database.rawQuery(" select * from " + DatabaseHelper.TABLE_NAME, null);

        if (c != null && c.getCount() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete All");
            builder.setMessage("Are you sure you want to Delete All Notification?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog

                    dbManager.deleteall();
                    listView.setAdapter(null);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(Notifications.this, "All notifications deleted", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        } else {
            Toast.makeText(this, "No Notifications", Toast.LENGTH_LONG).show();
        }


/*

        if(database.isOpen())
        {
            Cursor c = database.rawQuery(" select * from " + DatabaseHelper.TABLE_NAME, null);

            if (c != null && c.getCount() > 0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete All");
                builder.setMessage("Are you sure you want to Delete All Notification?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        dbManager.deleteall();
                        listView.setAdapter(null);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Notifications.this, "All notifications deleted", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



            } else
            {
                Toast.makeText(this, "No Notifications", Toast.LENGTH_LONG).show();
            }


           // database.close();

        }



        else
        {


            database = dbHelper.getWritableDatabase();

            Cursor c = database.rawQuery(" select * from " + DatabaseHelper.TABLE_NAME, null);

            if (c != null && c.getCount() > 0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete All");
                builder.setMessage("Are you sure you want to Delete All Notification?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        dbManager.deleteall();
                        listView.setAdapter(null);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Notifications.this, "All notifications deleted", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



            } else
            {
                Toast.makeText(this, "No Notifications", Toast.LENGTH_LONG).show();
            }
*/



       /* Cursor c = database.rawQuery(" select * from " + DatabaseHelper.TABLE_NAME, null);

        if (c != null && c.getCount() > 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete All");
            builder.setMessage("Are you sure you want to Delete All Notification?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog

                    dbManager.deleteall();
                    listView.setAdapter(null);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(Notifications.this, "All notifications deleted", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();



        } else
        {
            Toast.makeText(this, "No Notifications", Toast.LENGTH_LONG).show();
        }
*/        //   c.close();
        // database.close();


    }
}



