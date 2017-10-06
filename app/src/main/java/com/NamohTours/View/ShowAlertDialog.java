package com.NamohTours.View;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by ubuntu on 8/5/17.
 */

public class ShowAlertDialog {

    public void showAlert(Context context, String title, String message, Boolean status, DialogInterface.OnClickListener dialogInterface) {
        // material dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        String positiveText = "Ok";

        builder.setPositiveButton(positiveText, dialogInterface);

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
       /* AlertDialog alertDialog = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.ic_error_outline : R.drawable.ic_error_outline);


        // Showing Alert Message
        alertDialog.show();*/
    }
}
