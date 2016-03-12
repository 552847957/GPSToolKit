package com.srmn.xwork.androidlib.gis;

/**
 * Created by kiler on 2016/1/5.
 */
public interface GISLocationListener {
    void onStarted();

    void onLocationChanged(GISLocation location);

    void onEnd();

    void onFailed(String errorInfo);

    void onProgress(String message, Integer progressIndex);
}
