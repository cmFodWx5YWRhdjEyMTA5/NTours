package com.NamohTours.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.NamohTours.R;

/**
 * Created by Pooja Mantri on 31/7/17.
 */

public class NotificationAdapter extends CursorAdapter {


    private static final String TAG = "NotificationAdapter";

    public NotificationAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_notification, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        // Find fields to populate in inflated template
        CardView notificaionView = (CardView) view.findViewById(R.id.NotifficationView);
        TextView txtId = (TextView) view.findViewById(R.id.txtNotificationid);
        TextView txtNotification = (TextView) view.findViewById(R.id.txtNotificationtitle);
        TextView txtDate = (TextView) view.findViewById(R.id.txtNotificationdesc);

        int position = cursor.getPosition();

        if (position % 3 == 0) {
            notificaionView.setCardBackgroundColor(view.getResources().getColor(R.color.lightBlue));
        } else if (position % 3 == 1) {
            notificaionView.setCardBackgroundColor(view.getResources().getColor(R.color.lightPink));
        } else if (position % 3 == 2) {
            notificaionView.setCardBackgroundColor(view.getResources().getColor(R.color.lightYelloow));
        }


        // Extract properties from cursor
        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        String tempmessage = cursor.getString(cursor.getColumnIndexOrThrow("message"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));


        // If Notification contain # then make (After # and before # line bold)  and after remaining is  normal text
        if (tempmessage.contains("#")) {
            String bold = tempmessage.substring(tempmessage.indexOf("#"), tempmessage.lastIndexOf("#"));

            bold = bold.replace("#", "");


            String normal = tempmessage.substring(tempmessage.lastIndexOf("#") + 1);

            String Norm = "\n" + normal;


            // Make Notification (After # and before # is a Headline) bold ,and  remaining other text is on next line
            SpannableString ss1 = new SpannableString(bold + Norm);
            ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, bold.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            String message = ss1.toString();

            txtId.setText(id);
            txtNotification.setText(ss1);
            txtDate.setText(time);
        }


        // else
        else {
            txtId.setText(id);
            txtNotification.setText(tempmessage);
            txtDate.setText(time);
        }


        // Populate fields with extracted properties


    }
}
