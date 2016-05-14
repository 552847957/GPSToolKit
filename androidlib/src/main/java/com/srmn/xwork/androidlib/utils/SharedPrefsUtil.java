package com.srmn.xwork.androidlib.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.srmn.xwork.androidlib.ui.MyApplication;


/**
 * 偏好参数存储工具类
 */
public class SharedPrefsUtil {

    /**
     * 存储数据(Long)
     */
    public static void putLongValue(Context context, String name, String key, long value) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    /**
     * 存储数据(Int)
     */
    public static void putIntValue(Context context, String name, String key, int value) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    /**
     * 存储数据(String)
     */
    public static void putStringValue(Context context, String name, String key, String value) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    /**
     * 存储数据(boolean)
     */
    public static void putBooleanValue(Context context, String name, String key,
                                       boolean value) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    /**
     * 存储数据(JSON Object)
     */
    public static <T> void putJSonValue(Context context, String name, String key, T obj) {
        if (obj == null) {
            SharedPrefsUtil.putStringValue(context, name, key, "");
            return;
        }

        Gson gson = GsonUtil.getGson();

        SharedPrefsUtil.putStringValue(context, name, key, gson.toJson(obj));
    }


    public static <T> T getJSonValue(Context context, String name, String key, Class<T> classOfT) {
        String json = SharedPrefsUtil.getStringValue(context, name, key, "");

        if (json == null)
            return null;

        Gson gson = GsonUtil.getGson();

        T obj = gson.fromJson(json, classOfT);

        return obj;
    }

    public static String getStringValue(Context context, String name, String key, String defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defValue);
    }

    /**
     * 取出数据(Long)
     */
    public static long getLongValue(Context context, String name, String key, long defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    /**
     * 取出数据(int)
     */
    public static int getIntValue(Context context, String name, String key, int defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    /**
     * 取出数据(boolean)
     */
    public static boolean getBooleanValue(Context context, String name, String key,
                                          boolean defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }


    public static void clear(Context context, String name) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
    }


    public static void remove(Context context, String name, String key) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).commit();
    }
}