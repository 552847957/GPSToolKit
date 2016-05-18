package com.srmn.xwork.androidlib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kiler on 2016/1/15.
 */
public class DateTimeUtil {

    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat MonthFormat = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat MiniteTimeFormat = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat WWeekFormat = new SimpleDateFormat("EEEE");
    public static final SimpleDateFormat DateTimeNameFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.US);

    public static String FormatDate(Date date) {
        if (date == null)
            return "";
        return DateFormat.format(date);
    }

    public static String FormatDateTime(Date date) {
        if (date == null)
            return "";
        return DateTimeFormat.format(date);
    }

    public static String FormatMonth(Date date) {
        if (date == null)
            return "";
        return MonthFormat.format(date);
    }


    public static String FormatMiniteTime(Date date) {
        if (date == null)
            return "";
        return MiniteTimeFormat.format(date);
    }

    public static String FormatWeek(Date dateTime) {
        if (dateTime == null)
            return "";
        return WWeekFormat.format(dateTime);
    }

    public static String FormatTimeName(Date dateTime) {
        if (dateTime == null)
            return "";
        return DateTimeNameFormat.format(dateTime);
    }

    public static String FormatDateTime(Date dateTime, String format) {
        if (dateTime == null)
            return "";
        return new SimpleDateFormat(format).format(dateTime);
    }

    public static long GetTimeChangeSecond(Date startDateTime, Date endDateTime) {
        return (endDateTime.getTime() - startDateTime.getTime()) / 1000;
    }



    public static Date BuildDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return cal.getTime();
    }
}
