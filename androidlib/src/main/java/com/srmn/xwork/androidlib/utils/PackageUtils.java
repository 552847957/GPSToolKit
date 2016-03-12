package com.srmn.xwork.androidlib.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.util.Log;

/**
 * Created by kiler on 2016/1/4.
 */
public class PackageUtils {

    public static final String TAG = "PackageUtils";

    public static String readApplicationMetaDataStringValue(String key, Application context) {
        ApplicationInfo info = null;

        String msg = "";

        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString(key);
            Log.d(TAG, "Read application meta data " + key + " string value " + msg);
        } catch (PackageManager.NameNotFoundException e) {

            Log.d(TAG, "Read application meta data " + key + " string value failed:" + e.getMessage());
            e.printStackTrace();
        }
        return msg;
    }

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionCode + "";
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String readActivityMetaDataStringValue(String key, Activity context) {
        ActivityInfo info = null;

        String msg = "";

        try {
            info = context.getPackageManager().getActivityInfo(context.getComponentName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString(key);
            Log.d(TAG, "Read activity meta data " + key + " string value " + msg);
        } catch (PackageManager.NameNotFoundException e) {

            Log.d(TAG, "Read activity meta data " + key + " string value failed:" + e.getMessage());
            e.printStackTrace();
        }
        return msg;
    }


    public static String readServiceMetaDataStringValue(String key, Service context, Class<?> serviceClass) {
        ComponentName cn = new ComponentName(context, serviceClass);

        ServiceInfo info = null;

        String msg = "";

        try {
            info = context.getPackageManager().getServiceInfo(cn, PackageManager.GET_META_DATA);
            msg = info.metaData.getString(key);
            Log.d(TAG, "Read service meta data " + key + " string value " + msg);
        } catch (PackageManager.NameNotFoundException e) {

            Log.d(TAG, "Read service meta data " + key + " string value failed:" + e.getMessage());
            e.printStackTrace();
        }
        return msg;
    }


    public static String reaReceiverMetaDataStringValue(String key, Service context, Class<?> receiverClass) {
        ComponentName cn = new ComponentName(context, receiverClass);

        ActivityInfo info = null;

        String msg = "";

        try {
            info = context.getPackageManager().getReceiverInfo(cn, PackageManager.GET_META_DATA);
            msg = info.metaData.getString(key);
            Log.d(TAG, "Read receiver meta data " + key + " string value " + msg);
        } catch (PackageManager.NameNotFoundException e) {

            Log.d(TAG, "Read receiver meta data " + key + " string value failed:" + e.getMessage());
            e.printStackTrace();
        }
        return msg;
    }
}
