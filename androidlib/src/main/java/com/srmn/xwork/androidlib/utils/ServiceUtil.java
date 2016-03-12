package com.srmn.xwork.androidlib.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by kiler on 2016/2/16.
 */
public class ServiceUtil {

    public static boolean isWorked(String className, Context context) {
        ActivityManager myManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }
}
