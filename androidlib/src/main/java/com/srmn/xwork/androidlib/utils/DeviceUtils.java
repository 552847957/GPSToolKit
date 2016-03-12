package com.srmn.xwork.androidlib.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.srmn.xwork.androidlib.ui.MyApplication;


/**
 * Created by kiler on 2016/1/4.
 */
public class DeviceUtils {

    public static boolean isGPS_ON() {
        LocationManager locationManager = ((LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void setGPS_ON() {
        //获取GPS现在的状态（打开或是关闭状态）
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(MyApplication.getContext().getContentResolver(), LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            Settings.Secure.setLocationProviderEnabled(MyApplication.getContext().getContentResolver(), LocationManager.GPS_PROVIDER, true);
            MyApplication.getInstance().showShortToastMessage("检测到GPS未打开，强制打开GPS。");
        }
    }


    public static boolean isMobile_ON() {
        if (MyApplication.getContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) MyApplication.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isConnected();
            }
        }
        return false;
    }

    public static boolean isWifi_ON() {
        if (MyApplication.getContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) MyApplication.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isConnected()) {
                return true;
            }

            return false;
        }
        return false;
    }

    public static boolean isWifi_InternetAccess() {
        WifiManager mWifiManager = (WifiManager) MyApplication.getContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.isWifiEnabled()) {
            return mWifiManager.pingSupplicant();
        }
        return false;
    }


    public static boolean isNetworkisAvailable(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getApplicationContext().getSystemService(
                            Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                return false;
            }

            NetworkInfo networkinfo = manager.getActiveNetworkInfo();

            return networkinfo.isAvailable();


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String getAndroid_Id(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
