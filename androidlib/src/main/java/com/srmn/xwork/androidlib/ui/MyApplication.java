package com.srmn.xwork.androidlib.ui;

import android.app.Application;
import android.content.Context;
import android.os.RemoteException;
import android.widget.Toast;

import com.amap.api.maps.MapsInitializer;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.xutils.x;

/**
 * Created by kiler on 2016/1/24.
 */
public class MyApplication extends Application {


    public static final String TAG = "MyApplication";

    public static final String SharedPrefsNAME = "SGI";

    protected static MyApplication instance;


    public static MyApplication getInstance() {
        return instance;
    }

    public static void setInstance(MyApplication instance) {
        MyApplication.instance = instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        TypefaceProvider.registerDefaultIconSets();
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
    }


    public void showShortToastMessage(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToastMessage(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
