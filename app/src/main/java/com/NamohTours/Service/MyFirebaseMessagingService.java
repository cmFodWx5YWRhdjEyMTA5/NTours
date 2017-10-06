package com.NamohTours.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.NamohTours.Activity.LeftDrawer;
import com.NamohTours.Activity.Login;
import com.NamohTours.Database.DBManager;
import com.NamohTours.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.UserRegister;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private DBManager dbManager;
    public static final int NOTIFICATION_ID = 1;
    private SharedPreferences RegisterPrefences;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            System.out.println("Notification " + remoteMessage.getNotification().getBody());

            handleNotification(remoteMessage.getNotification().getBody());

        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {


            try {
                JSONObject json = new JSONObject(remoteMessage.getData());

                handleDataMessage(json);
            } catch (Exception e) {

            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {


            Log.e(TAG, "Message :" + json.getString("message"));
            String message = json.getString("message");
          /*  String tempmessage = json.getString("message");

            //= tempmessage.substring(tempmessage.indexOf("#",tempmessage.lastIndexOf("#")));

            String bold = tempmessage.substring(tempmessage.indexOf("#"),tempmessage.lastIndexOf("#"));

            Log.e(TAG,"Bold Text : "+bold);

            String normal = tempmessage.substring(tempmessage.lastIndexOf("#")+1);

            String Norm = "\n"+normal;


            Log.e(TAG, "handleDataMessage: Normal String "+Norm);

            SpannableString ss1=  new SpannableString(bold+Norm);
            ss1.setSpan(new StyleSpan(Typeface.BOLD), 1, bold.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            Log.e(TAG, "handleDataMessage: Sapn :"+ss1);
            String message = ss1.toString();


            Log.e(TAG, "handleDataMessage: Real : "+message);*/
            String url = json.getString("url");
            //  String url = "";

            dbManager = new DBManager(this);
            dbManager.open();


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
            Date date = new Date();
            String when = dateFormat.format(date);


            dbManager.insert(message, when, url);

            sendNotification(message, url);

            System.out.println("Url in Not Null" + url);


            dbManager.close();

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    private void sendNotification(String msg, String url) {

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);


        boolean isUserRegister = RegisterPrefences.getBoolean(UserRegister, false);


        if (isUserRegister == true) {

            Intent resultIntent = new Intent(getApplicationContext(), LeftDrawer.class);
            resultIntent.putExtra("call", "notification");
            resultIntent.putExtra("msg", msg);
            resultIntent.putExtra("url", url);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Namoh Tours")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setWhen(System.currentTimeMillis()) //
                    .setLights(0xffffffff, 0, 0)
                    .setSmallIcon(R.mipmap.ic_launcher);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mNotifyBuilder.setSound(alarmSound);
            long[] pattern = {200, 200};
            mNotifyBuilder.setVibrate(pattern);

            mNotifyBuilder.setContentIntent(resultPendingIntent);

            mNotifyBuilder.setContentText(msg);

            // Set autocancel
            mNotifyBuilder.setAutoCancel(true);
            // Post a notification
            mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());

        } else {
            Intent resultIntent = new Intent(getApplicationContext(), Login.class);
            resultIntent.putExtra("msg", msg);
            resultIntent.putExtra("url", url);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Namoh Tours")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setWhen(System.currentTimeMillis()) //
                    .setLights(0xffffffff, 0, 0)
                    .setSmallIcon(R.mipmap.ic_launcher);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mNotifyBuilder.setSound(alarmSound);
            long[] pattern = {200, 200};
            mNotifyBuilder.setVibrate(pattern);

            mNotifyBuilder.setContentIntent(resultPendingIntent);

            mNotifyBuilder.setContentText(msg);

            // Set autocancel
            mNotifyBuilder.setAutoCancel(true);
            // Post a notification
            mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());

        }

    }
}

