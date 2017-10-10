package com.NamohTours.Activity;

import android.Manifest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NamohTours.Fragment.FeatutredProduct;
import com.NamohTours.Fragment.MoreFragment;
import com.NamohTours.Fragment.NotificationFragment;
import com.NamohTours.Fragment.SearchFragment;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.prechat.PreChatForm;
import com.zopim.android.sdk.prechat.ZopimChatActivity;
import com.zopim.android.sdk.widget.ChatWidgetService;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.TAB;

public class LeftDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private static final String TAG = LeftDrawer.class.getSimpleName();

    DrawerLayout drawerLayout;

    LinearLayout frameLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    BottomBar bottomBar;
    Toolbar toolbar;
    String flag = "A";
    private String flag1 = "Z";
    AlertDialog alertDialog = null;
    static final Integer CALL = 0x2;
    private String Tab, TAB_CONTACT;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    TextView txtCategories, txtFeatured, txtInternet;

    LinearLayout HeaderLayout;

    private String url, msg, call;

    ConnectionDetector cd;
    private static final int REQUEST_INVITE = 10;
    private FloatingActionButton fabChat;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_left_drawer);
        // getLayoutInflater().inflate(R.layout.activity_left_drawer,BottomFrameLayout);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fabChat = (FloatingActionButton) findViewById(R.id.fab_chat);

        fabChat.setOnClickListener(this);

        // form before chat
        PreChatForm defaultPreChat = new PreChatForm.Builder()
                .name(PreChatForm.Field.REQUIRED)
                .email(PreChatForm.Field.REQUIRED)
                .phoneNumber(PreChatForm.Field.REQUIRED)
                .build();

        // ZenDesk Chat
        // Initaliztion Acccount Key
        ZopimChat.init("1gORp3MztJ6CLzH62QB1eQrw1IVzacle")
                .preChatForm(defaultPreChat);


        // Disable Chat Widget of Zendesk
        ChatWidgetService.disable();


        txtCategories = (TextView) findViewById(R.id.txtCategories);
        txtFeatured = (TextView) findViewById(R.id.txtFeatured);
        HeaderLayout = (LinearLayout) findViewById(R.id.HeaderLayout);


        // Notification Data

        call = getIntent().getStringExtra("call");
        url = getIntent().getStringExtra("url");
        msg = getIntent().getStringExtra("msg");


// get intent String from Tabhost  Activity
        String drawerExtra = getIntent().getStringExtra("drawer");

        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        TAB_CONTACT = prefs.getString(TAB, null);


        frameLayout = (LinearLayout) findViewById(R.id.HomeFrame);
        bottomBar = (BottomBar) findViewById(R.id.bottomBarr);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        txtInternet = (TextView) findViewById(R.id.txtInternet);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (drawerExtra != null) {
            if (drawerExtra.equals("drawer")) {
                // hide toolbar in tab Host

                getSupportActionBar().hide();
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }


        if (call != null) {
            if (call.equals("notification")) ;
            {

                bottomBar.setDefaultTab(R.id.tab_deals);

               /* Fragment fragment = new NotificationFragment();
                Bundle args = new Bundle();
                args.putString("msg", msg);
                args.putString("url",url);
                fragment.setArguments(args);

                getSupportFragmentManager().beginTransaction().replace((int) R.id.HomeFrame, fragment).commit();*/

            }
        }


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes final int tabId) {


                Tab = bottomBar.getCurrentTab().getTitle();


                if ((tabId == R.id.tab_home)) {

                    if (!TextUtils.isEmpty(TAB_CONTACT)) {

                        if (TAB_CONTACT.equals("contact")) {
                            // bottomBar.setDefaultTab(R.id.tab_contact);
                            showContactDialog();
                        }


                    }


                    if (flag.equals("A")) {

                    } else {
                        HeaderLayout.setVisibility(View.VISIBLE);
                        frameLayout.removeAllViews();
                        frameLayout.invalidate();

                        Intent i = new Intent(getApplicationContext(), TourParentCategory.class);
                        startActivity(i);
                        finish();

                    }


                    txtCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            frameLayout.removeAllViews();
                            frameLayout.invalidate();


                            txtCategories.setTextColor(getResources().getColor(R.color.colorPrimary));
                            txtFeatured.setTextColor(getResources().getColor(R.color.black));


                            if (flag.equals("A")) {


                            } else {
                                HeaderLayout.setVisibility(View.VISIBLE);
                                frameLayout.removeAllViews();
                                frameLayout.invalidate();
                                Intent i = new Intent(getApplicationContext(), TourParentCategory.class);
                                startActivity(i);
                                finish();

                            }


                        }
                    });


                    txtFeatured.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            flag = "B";

                            frameLayout.removeAllViews();
                            frameLayout.invalidate();

                            txtFeatured.setTextColor(getResources().getColor(R.color.colorPrimary));
                            txtCategories.setTextColor(getResources().getColor(R.color.black));


                            Fragment fragment = new FeatutredProduct();
                            getSupportFragmentManager().beginTransaction().replace((int) R.id.HomeFrame, fragment).commit();


                        }
                    });

                } else if (tabId == R.id.tab_deals)

                {


                    flag = "B";

                    HeaderLayout.setVisibility(View.GONE);
                    frameLayout.removeAllViews();
                    frameLayout.invalidate();
                    Fragment fragment = new NotificationFragment();
                    getSupportFragmentManager().beginTransaction().replace((int) R.id.HomeFrame, fragment).addToBackStack("deals").commit();



                              /*  Intent i = new Intent(getApplicationContext(), Notifications.class);
                                startActivity(i);
                                finish();
*/

                } else if (tabId == R.id.tab_contact) {


                    showContactDialog();


                } else if (tabId == R.id.tab_search) {

                    flag = "B";
                    //  FooterBar.setVisibility(View.GONE);
                    HeaderLayout.setVisibility(View.GONE);
                    frameLayout.removeAllViews();
                    frameLayout.invalidate();


                    Fragment fragment = new SearchFragment();


                    getSupportFragmentManager().beginTransaction().replace((int) R.id.HomeFrame, fragment).addToBackStack("search").commit();


                } else if (tabId == R.id.tab_more) {
                    flag = "B";

                    HeaderLayout.setVisibility(View.GONE);
                    frameLayout.removeAllViews();
                    frameLayout.invalidate();


                    Fragment fragment = new MoreFragment();


                    getSupportFragmentManager().beginTransaction().replace((int) R.id.HomeFrame, fragment).addToBackStack("home").commit();

                }

            }

        });


        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {


                    frameLayout.removeAllViews();
                    frameLayout.invalidate();
                    Intent i = new Intent(getApplicationContext(), TourParentCategory.class);
                    startActivity(i);
                    finish();
                    // getLayoutInflater().inflate(R.layout.activity_tour_category,frameLayout);

                } else if (tabId == R.id.tab_contact) {


                    showContactDialog();


                }





            }
        });

    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*//to prevent current item select over and over
        if (item.isChecked()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }*/

        if (id == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), TourParentCategory.class));
            finish();
        }

        if (id == R.id.nav_events) {
            Intent events = new Intent(LeftDrawer.this, TourSubCategory.class);
            events.putExtra("cat_id", "467");
            startActivity(events);
        }

        if (id == R.id.nav_tickets) {
            Intent tickects = new Intent(LeftDrawer.this, TourSubCategory.class);
            tickects.putExtra("cat_id", "468");
            startActivity(tickects);
        }


        if (id == R.id.nav_passport) {

            startActivity(new Intent(getApplicationContext(), Passport.class));
            //  finish();
        }


        if (id == R.id.nav_visa) {
            startActivity(new Intent(getApplicationContext(), Visa.class));
            //   finish();

        }

        if (id == R.id.nav_forex) {
            startActivity(new Intent(getApplicationContext(), ForexActivity.class));
        }

        if (id == R.id.nav_insurance) {
            startActivity(new Intent(getApplicationContext(), TravelInsurance.class));
        }

        if (id == R.id.nav_contact) {
            showContactDialog();
        }


        if (id == R.id.nav_feedback) {
            startActivity(new Intent(getApplicationContext(), Enquiry.class));

            // finish();
        }

        if (id == R.id.nav_refer) {
            onInviteClicked();
        }

       /* if(id == R.id.nav_rate)
        {
            final String appPackageName = getApplicationContext().getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+appPackageName)));
            }
        }
*/

        if (id == R.id.nav_currancy) {

            Intent currency = new Intent(LeftDrawer.this, WebUrl.class);
            currency.putExtra("Web", "currency");
            startActivity(currency);

        }

        if (id == R.id.nav_weather) {
            Intent weather = new Intent(LeftDrawer.this, WebUrl.class);
            weather.putExtra("Web", "weather");
            startActivity(weather);
        }

        if (id == R.id.nav_timezone) {
            Intent timezone = new Intent(LeftDrawer.this, WebUrl.class);
            timezone.putExtra("Web", "timezone");
            startActivity(timezone);
        }

        if (id == R.id.nav_world) {
            Intent world = new Intent(LeftDrawer.this, WebUrl.class);
            world.putExtra("Web", "world");
            startActivity(world);
        }

        if (id == R.id.nav_entertain) {
            startActivity(new Intent(LeftDrawer.this, EntertainMentActivity.class));
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showContactDialog() {


        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.custom_contact_dialog, null);
        //final EditText subEditText = (EditText)subView.findViewById(R.id.dialogEditText);
        //final ImageView subImageView = (ImageView)subView.findViewById(R.id.image);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(subView);
        builder.setNegativeButton("Cancel", null);


        LinearLayout LinearContactCall, LinearContactEnquiry, LinearContactChat;

        LinearContactCall = (LinearLayout) subView.findViewById(R.id.LinearContactCall);
        LinearContactEnquiry = (LinearLayout) subView.findViewById(R.id.LinearContactEnquiry);
        LinearContactChat = (LinearLayout) subView.findViewById(R.id.LinearContactChat);


        LinearContactCall.setOnClickListener(this);
        LinearContactEnquiry.setOnClickListener(this);
        LinearContactChat.setOnClickListener(this);
        TextView txtEnquiry = (TextView) subView.findViewById(R.id.txtQueryHeading);
        // txtEnquiry.setOnClickListener(this);

        TextView txtCall = (TextView) subView.findViewById(R.id.txtcallHeading);
        //txtCall.setOnClickListener(this);

        alertDialog = builder.create();

        //alertDialog.setButton();
        alertDialog.show();


    }

    @Override
    public void onClick(View v) {


        int id = v.getId();


        if ((id == R.id.LinearContactEnquiry)) {
            alertDialog.dismiss();
            Intent filter = new Intent(LeftDrawer.this, Enquiry.class);
            startActivity(filter);
            // finish();
        }

        if ((id == R.id.LinearContactCall)) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alertDialog.dismiss();
                askForPermission(Manifest.permission.CALL_PHONE, CALL);
            } else {

                alertDialog.dismiss();
                String phone = "07350530009";
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);

            }


        }


        // with Zendesk Chat
        if (id == R.id.LinearContactChat) {
            alertDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), ZopimChatActivity.class));
        }

        // floting Action button
        if (id == R.id.fab_chat) {
            startActivity(new Intent(getApplicationContext(), ZopimChatActivity.class));
        }


    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(LeftDrawer.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LeftDrawer.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(LeftDrawer.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(LeftDrawer.this, new String[]{permission}, requestCode);
            }
        }

        // permission already granted
        else {

            String phone = "07350530009";
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);

           /* alertDialog.dismiss();
            String phone = "07350530009";
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
            startActivity(intent);*/

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    //  askForGPS();
                    break;
                //Call
                case 2:

                    String phone = "07350530009";
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    startActivity(intent);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    }
                    break;
                //Write external Storage
                case 3:
                    break;
                //Read External Storage
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                //Camera
                case 5:
                   /* Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }*/
                    break;
                //Accounts
                case 6:
                  /*  AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                    Account[] list = manager.getAccounts();
                    Toast.makeText(this,""+list[0].name,Toast.LENGTH_SHORT).show();
                    for(int i=0; i<list.length;i++){

                    }*/
            }

            //  Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


        int fragments = getSupportFragmentManager().getBackStackEntryCount();


        if (fragments == 1) {

            Intent i = new Intent(LeftDrawer.this, TourParentCategory.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        } else {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                finish();
            }

        }


    }

    // For Share Intent
    private void onInviteClicked() {

        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(LeftDrawer.this);
        intentBuilder.setChooserTitle("Choose Share App")
                .setType("text/plain")
                .setSubject("Flat button for android")
                .setText("Hey check out Namoh App at: https://goo.gl/nD2eSG")
                .startChooser();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }


        editor.remove(TAB).commit();

    }

}



