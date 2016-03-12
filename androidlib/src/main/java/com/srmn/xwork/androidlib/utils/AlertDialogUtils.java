package com.srmn.xwork.androidlib.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by kiler on 2016/1/12.
 */
public class AlertDialogUtils {

    public static void showOkCancel(String Title, String okText, String cancelText, Context context, DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancelClick, View view) {
        new AlertDialog.Builder(context)
                .setTitle(Title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(view)
                .setPositiveButton(okText, okClick)
                .setNegativeButton(cancelText, cancelClick)
                .show();
    }


}





