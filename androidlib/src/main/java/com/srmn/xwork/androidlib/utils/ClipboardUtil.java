package com.srmn.xwork.androidlib.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by kiler on 2016/5/14.
 */
public class ClipboardUtil {

    public static void copyTextToClipboard(Context context, String lable, String text) {
        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // Creates a new text clip to put on the clipboard
        ClipData clip = ClipData.newPlainText(lable, text);

        clipboard.setPrimaryClip(clip);
    }
}
