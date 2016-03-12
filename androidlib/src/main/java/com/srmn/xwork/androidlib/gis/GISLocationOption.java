package com.srmn.xwork.androidlib.gis;

/**
 * Created by kiler on 2016/1/4.
 */
public class GISLocationOption {

    private LocationMode locationMode;
    private boolean needAddress;
    private boolean onceLocation;
    private boolean wifiActiveScan;
    private boolean mockEnable;
    private int interval;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public LocationMode getLocationMode() {
        return locationMode;
    }

    public void setLocationMode(LocationMode locationMode) {
        this.locationMode = locationMode;
    }

    public boolean isMockEnable() {
        return mockEnable;
    }

    public void setMockEnable(boolean mockEnable) {
        this.mockEnable = mockEnable;
    }

    public boolean isNeedAddress() {
        return needAddress;
    }

    public void setNeedAddress(boolean needAddress) {
        this.needAddress = needAddress;
    }

    public boolean isOnceLocation() {
        return onceLocation;
    }

    public void setOnceLocation(boolean onceLocation) {
        this.onceLocation = onceLocation;
    }

    public boolean isWifiActiveScan() {
        return wifiActiveScan;
    }

    public void setWifiActiveScan(boolean wifiActiveScan) {
        this.wifiActiveScan = wifiActiveScan;
    }

    public static enum LocationMode {
        Battery_Saving,
        Device_Sensors,
        Hight_Accuracy;

        private LocationMode() {
        }
    }

}
