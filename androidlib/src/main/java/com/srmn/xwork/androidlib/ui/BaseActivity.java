package com.srmn.xwork.androidlib.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;


import com.google.gson.Gson;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.SharedPrefsUtil;
import com.srmn.xwork.androidlib.utils.UIUtil;

import org.xutils.x;


/**
 * Created by kiler on 2016/1/8.
 */
public class BaseActivity extends AppCompatActivity {
    protected AppCompatActivity context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        x.view().inject(this);

    }


    public void showShortToastMessage(String message) {
        UIUtil.showShortToastMessage(context, message);
    }

    public void showLongToastMessage(String message) {
        UIUtil.showLongToastMessage(context, message);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void gotoActivity(Class cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    public void gotoActivity(Intent intent, Class cls) {
        intent.setClass(context, cls);
        startActivity(intent);
    }


    public void gotoActivity(Class cls, int taskType) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(taskType);
        startActivity(intent);
    }

    public void gotoActivityForResult(Class cls, int requestCode) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setActionBarTitle(String actionBarTitle) {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(actionBarTitle);
    }

    public void toggleActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            if (actionBar.isShowing()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }
    }

    public void showActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.show();
    }

    public void hideActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    public void showBackButton() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void hideBackButton() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(false);
    }


    /**
     * 存储数据(Long)
     */
    public void putSharedPrefsLongValue(String name, String key, long value) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    /**
     * 存储数据(Int)
     */
    public void putSharedPrefsIntValue(String name, String key, int value) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    /**
     * 存储数据(String)
     */
    public void putSharedPrefsStringValue(String name, String key, String value) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    /**
     * 存储数据(boolean)
     */
    public void putSharedPrefsBooleanValue(String name, String key,
                                           boolean value) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    /**
     * 存储数据(JSON Object)
     */
    public <T> void putSharedPrefsJSonValue(String name, String key, T obj) {
        if (obj == null) {
            putSharedPrefsStringValue(name, key, "");
            return;
        }

        Gson gson = GsonUtil.getGson();

        putSharedPrefsStringValue(name, key, gson.toJson(obj));
    }


    public <T> T getSharedPrefsJSonValue(String name, String key, Class<T> classOfT) {
        String json = getSharedPrefsStringValue(name, key, "");

        if (json == null)
            return null;

        Gson gson = GsonUtil.getGson();

        T obj = gson.fromJson(json, classOfT);

        return obj;
    }

    public String getSharedPrefsStringValue(String name, String key, String defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defValue);
    }

    /**
     * 取出数据(Long)
     */
    public long getSharedPrefsLongValue(String name, String key, long defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    /**
     * 取出数据(int)
     */
    public int getSharedPrefsIntValue(String name, String key, int defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    /**
     * 取出数据(boolean)
     */
    public boolean getSharedPrefsBooleanValue(String name, String key,
                                              boolean defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }



}
