package com.NamohTours.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.NamohTours.Activity.TourSubCategory;
import com.NamohTours.Activity.TourWishlistProduct;
import com.NamohTours.Activity.WebUrl;
import com.NamohTours.Adapter.NotificationAdapter;
import com.NamohTours.Database.DBManager;
import com.NamohTours.Database.DatabaseHelper;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.joanzapata.iconify.widget.IconButton;
import com.squareup.okhttp.internal.http.StreamAllocation;

public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    private static final String TAG = NotificationFragment.class.getSimpleName();

    private DBManager dbManager;
    TextView idTextView, titleTextView, descTextView;
    public ListView listView;


    //  public SimpleCursorAdapter adapter;

    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;


    final String[] from = new String[]{DatabaseHelper._ID,
            DatabaseHelper.MESSAGE, DatabaseHelper.TIME};

    final int[] to = new int[]{R.id.txtNotificationid, R.id.txtNotificationtitle, R.id.txtNotificationdesc};
    private long _id;

    int idt;
    String url, msg;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NotificationAdapter adapter;

    private OnFragmentInteractionListener mListener;
    private Cursor cursor;


    public NotificationFragment() {
        // Required empty public constructor
    }


    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           /* mParam1 = getArguments().getString("msg");
            mParam2 = getArguments().getString("url");*/
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        listView = (ListView) rootView.findViewById(R.id.Notification_list);
        listView.setEmptyView(rootView.findViewById(R.id.Notification_empty));


        Toolbar toolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        AppCompatActivity actionBar = (AppCompatActivity) getActivity();
        actionBar.setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) actionBar.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        initData();
        return rootView;
    }


    private void initData() {
        dbManager = new DBManager((AppCompatActivity) getActivity());
        dbManager.open();
        cursor = dbManager.fetch();


        // get Data from notification
        dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.getWritableDatabase();


        adapter = new NotificationAdapter(getActivity(), cursor);

        // adapter = new SimpleCursorAdapter((AppCompatActivity)getActivity(), R.layout.list_item_notification, cursor, from, to, 0);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this
        );

        // listView.setAdapter(adapter);
        //  urltype = getIntent().getStringExtra("UrlType");
//        url = getIntent().getStringExtra("url");
//        msg= getIntent().getStringExtra("msg");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final Cursor cur = (Cursor) parent.getItemAtPosition(position);
        final String url = cur.getString(cur.getColumnIndexOrThrow("url"));
        //Log.e(TAG,"Url on Notification click :"+url);


        if (url.equals("http://www.google.com")) {
            // Not Do Anything
        } else {
            Intent OpenUrl = new Intent((AppCompatActivity) getActivity(), WebUrl.class);
            OpenUrl.putExtra("Web", "notification");
            OpenUrl.putExtra("url", url);
            startActivity(OpenUrl);
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        final Cursor curs = (Cursor) parent.getItemAtPosition(position);
        final long ID = curs.getLong(curs.getColumnIndexOrThrow("_id"));
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {

                // Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                // Get the notification from the database.

                dbManager.delete(ID);
                cursor.requery();
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
        curs.requery();

        return true;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        // super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        inflater.inflate(R.menu.menu_notification, menu);
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

            case R.id.notification_wishlist:
                if (ConnectionDetector.isConnectingToInternet((AppCompatActivity) getActivity())) {
                    Intent account = new Intent(((AppCompatActivity) getActivity()), TourWishlistProduct.class);
                    startActivity(account);

                } else {

                    Snackbar.make(listView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

                }

                break;


        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteall() {
        // AlertDialog.Builder builder = new AlertDialog.Builder(this);


        Cursor c = database.rawQuery(" select * from " + DatabaseHelper.TABLE_NAME, null);

        if (c != null && c.getCount() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder((AppCompatActivity) getActivity());
            builder.setTitle("Delete All");
            builder.setMessage("Are you sure you want to Delete All Notification?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog

                    dbManager.deleteall();
                    listView.setAdapter(null);
                    adapter.notifyDataSetChanged();
                    Snackbar.make(listView, "All notifications deleted", Snackbar.LENGTH_LONG).show();
                    //  Toast.makeText((A), "All notifications deleted", Toast.LENGTH_LONG).show();
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
            Snackbar.make(listView, "No notifications ", Snackbar.LENGTH_LONG).show();
        }

    }

}
