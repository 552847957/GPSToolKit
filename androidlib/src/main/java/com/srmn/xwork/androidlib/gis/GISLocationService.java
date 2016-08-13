package com.srmn.xwork.androidlib.gis;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.srmn.xwork.androidlib.ui.MyApplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kiler on 2016/2/16.
 */
public class GISLocationService extends Service implements AMapLocationListener {


    public static final long LOCATION_UPDATE_MIN_TIME = 3 * 1000;
    public static final long LOCATION_RESET_TIME = 1000 * 60 * 6;
    public static final float LOCATION_UPDATE_MIN_DISTANCE = 5;
    public static final String INTENT_ACTION_UPDATE_DATA_EXTRA_LOCATION = "INTENT_ACTION_UPDATE_DATA_EXTRA_LOCATION";
    public static final String INTENT_ACTION_UPDATE_DATA_EXTRA_SATELLITE_STATUS = "INTENT_ACTION_UPDATE_DATA_EXTRA_SATELLITE_STATUS";
    public static final String SERVICE_NAME = "com.srmn.xwork.androidlib.gis.GISLocationService";
    public static final String REPEATING = "repeating";
    private LocationManager locationManager;
    private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // 卫星信号
    private int maxSatellites = 0;
    private int connnectSatellites = 0;
    private int findSatellites = 0;
    /**
     * 卫星状态监听器
     */
    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
            GpsStatus status = locationManager.getGpsStatus(null); // 取当前状态
            updateGpsStatus(event, status);
        }
    };
    private AlarmManager am;
    //  private WakeReceiver wakeReceiver;
    private PendingIntent pi;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;
    private LocationReceiver locationReceiver;

    @Override
    public void onCreate() {

        super.onCreate();
        initLocation();

        if (am != null && pi != null) {
            am.cancel(pi);
        }

        IntentFilter intentFile = new IntentFilter();
        intentFile.addAction(REPEATING);
        locationReceiver = new LocationReceiver();
        registerReceiver(locationReceiver, intentFile);
        Intent intent = new Intent();
        intent.setAction(REPEATING);
        pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        long time = SystemClock.currentThreadTimeMillis();
        am.cancel(pi);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, LOCATION_RESET_TIME, pi); //每6分钟发起一次定位重置


    }

    private void initLocation() {


        // 在Service销毁的时候销毁定位资源
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(this);
            //停止定位
            mLocationClient.stopLocation();
            //销毁定位客户端。
            mLocationClient.onDestroy();
        }

        //释放唤醒锁
        if (wakeLock != null) {
            wakeLock.release();
        }


        //唤醒锁，防止息屏
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, SERVICE_NAME);
        wakeLock.acquire();

        //初始化定位参数
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//      locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//      locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);

        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(LOCATION_UPDATE_MIN_TIME);

        mLocationClient = new AMapLocationClient(MyApplication.getInstance().getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(locationOption);
        //启动定位
        mLocationClient.startLocation();

        locationManager = ((LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE));


        locationManager.addGpsStatusListener(statusListener);
    }

    private void updateGpsStatus(int event, GpsStatus status) {
        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            numSatelliteList.clear();
            findSatellites = 0;
            while (it.hasNext() && findSatellites <= maxSatellites) {
                GpsSatellite s = it.next();
                numSatelliteList.add(s);
                findSatellites++;
            }
        }

        GISSatelliteStatus gisSatelliteStatus = new GISSatelliteStatus();
        gisSatelliteStatus.setConnnectSatellites(connnectSatellites);
        gisSatelliteStatus.setFindSatellites(findSatellites);


        Intent intent = new Intent();

        intent.putExtra(INTENT_ACTION_UPDATE_DATA_EXTRA_SATELLITE_STATUS, gisSatelliteStatus);

        intent.setAction(SERVICE_NAME);

        sendBroadcast(intent);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        connnectSatellites = aMapLocation.getSatellites();
        // 发送广播传送地点位置信息到地图显示界面
        // 当数据正常获取的时候，把位置信息通过广播发送到接受方,
        // 也就是需要处理这些数据的组件。
        Intent intent = new Intent();

        intent.putExtra(INTENT_ACTION_UPDATE_DATA_EXTRA_LOCATION, GISAMapLocationTask.ConvertLocation(aMapLocation));

        intent.setAction(SERVICE_NAME);

        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 在Service销毁的时候销毁定位资源
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(this);
            //停止定位
            mLocationClient.stopLocation();
            //销毁定位客户端。
            mLocationClient.onDestroy();
        }

        //释放唤醒锁
        if (wakeLock == null)
            return;
        wakeLock.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }

    }

}
