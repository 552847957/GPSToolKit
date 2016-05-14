package com.srmn.xwork.androidlib.utils;

import android.content.Intent;
import android.net.Uri;

import com.amap.api.maps.model.LatLng;

import java.io.File;

/**
 * Created by kiler on 2016/5/14.
 */
public class IntentUtil {


    public static Intent openAmapLocation(String appName, String pointName, LatLng latLng) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.addCategory("android.intent.category.DEFAULT");
        it.setPackage("com.autonavi.minimap");
        Uri mUri = Uri.parse(String.format("androidamap://viewMap?sourceApplication=%s&poiname=%s&lat=%s&lon=%s&dev=0", appName, pointName, NumberUtil.roundNumber(latLng.latitude, 6) + "", NumberUtil.roundNumber(latLng.longitude, 6) + ""));
        return it;
    }


    public static Intent openImage(String filePath) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        Uri mUri = Uri.parse("file://" + filePath);
        it.setDataAndType(mUri, "image/*");
        return it;
    }


    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.setDataAndType(uri, "text/html");

        return intent;
    }


    //android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "application/vnd.ms-excel");

        return intent;
    }


}
