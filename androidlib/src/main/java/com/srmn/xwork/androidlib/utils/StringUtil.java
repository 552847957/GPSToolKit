package com.srmn.xwork.androidlib.utils;

/**
 * Created by kiler on 2015/10/7.
 */
public class StringUtil {

    public static boolean isNullOrEmpty(String value) {
        if (value == null)
            return true;
        if (value.equals(""))
            return true;
        if (value.length() == 0)
            return true;
        return false;
    }

    public static String formatDistance(double value) {
        if (value < 1000)
            return String.format("%3.1f", value) + " m";
        else
            return String.format("%3.3f", value / 1000) + " km";
    }

    public static String formatTime(int value) {
        int hours = value / 3600;
        int minute = (value % 3600) / 60;
        int second = ((value % 3600) % 60);
        return String.format("%d", hours) + ":" + String.format("%02d", minute) + "'" + String.format("%02d", second) + "''";
    }
}
