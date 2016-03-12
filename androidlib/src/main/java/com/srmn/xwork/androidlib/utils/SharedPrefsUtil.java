package com.srmn.xwork.androidlib.utils;

import android.content.Context;

import com.srmn.xwork.androidlib.ui.MyApplication;


/**
 * 偏好参数存储工具类
 */
public class SharedPrefsUtil {

    /**
     * 存储数据(Long)
     */
    public static void putLongValue(String name, String key, long value) {
        MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    /**
     * 存储数据(Int)
     */
    public static void putIntValue(String name, String key, int value) {
        MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    /**
     * 存储数据(String)
     */
    public static void putStringValue(String name, String key, String value) {
        MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    /**
     * 存储数据(boolean)
     */
    public static void putBooleanValue(String name, String key,
                                       boolean value) {
        MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    /**
     * 取出数据(Long)
     */
    public static long getLongValue(String name, String key, long defValue) {
        return MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    /**
     * 取出数据(int)
     */
    public static int getIntValue(String name, String key, int defValue) {
        return MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    /**
     * 取出数据(boolean)
     */
    public static boolean getBooleanValue(String name, String key,
                                          boolean defValue) {
        return MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    /**
     * 取出数据(String)
     */
    public static String getStringValue(String name, String key,
                                        String defValue) {
        return MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defValue);
    }

    /**
     * 清空所有数据
     */
    public static void clear(String name) {
        MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
    }

    /**
     * 移除指定数据
     */
    public static void remove(String name, String key) {
        MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).commit();
    }
}