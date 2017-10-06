package com.NamohTours.Service;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Lenovo on 24/02/2015.
 */
public class CommonUtilities {

    // give your server registration url here  http://localhost:
    //--24 april 2015

    public static final String APP_SERVER_URL = "http://serv.namohtours.com//Getdata.aspx?shareRegId=true";
    public static final String OTP_SERVER_URL = "http://sendmsg.abmra.in//GetOTP.aspx?shareRegId=true";
    public static final String key = "ABMRA";
    //public static final String CHANGE_PASSWORD_URL="http://namohtours.com/api/rest/account/password";


    public static final String CHANGE_PASSWORD_URL = "http://namohtours.com/index.php?route=rest/account/password";


    // Google project id
    public static final String SENDER_ID = "921025068464";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "MERCHANT BANK";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.example.win7.mynotification.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * `   * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);


    }


}
