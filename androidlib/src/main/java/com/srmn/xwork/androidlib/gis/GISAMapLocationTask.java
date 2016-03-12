package com.srmn.xwork.androidlib.gis;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.srmn.xwork.androidlib.ui.MyApplication;
import com.srmn.xwork.androidlib.utils.NumberUtil;


import java.util.Date;

/**
 * Created by kiler on 2016/1/4.
 */
public class GISAMapLocationTask implements AMapLocationListener {

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private GISLocationListener mLocationListener = null;

    private GISLocationOption mlocationOption;

    public GISAMapLocationTask(GISLocationOption locationOption, GISLocationListener locationListener) {
        this.mlocationOption = locationOption;
        this.mLocationListener = locationListener;
    }

    public static GISLocation ConvertLocation(AMapLocation aMapLocation) {

        GISLocation gisLocation = new GISLocation();
        gisLocation.setBearing(aMapLocation.getBearing());
        gisLocation.setSpeed(aMapLocation.getSpeed());
        gisLocation.setErrorCode(aMapLocation.getErrorCode());
        gisLocation.setErrorInfo(aMapLocation.getErrorInfo());
        gisLocation.setLocationType(aMapLocation.getLocationType());//定位成功回调信息，设置相关消息
        gisLocation.setLatitude(NumberUtil.roundNumber(aMapLocation.getLatitude(), 6));//获取纬度
        gisLocation.setLongitude(NumberUtil.roundNumber(aMapLocation.getLongitude(), 6));//获取经度
        gisLocation.setAccuracy(NumberUtil.roundNumber(aMapLocation.getAccuracy(), 0));//获取精度信息
        gisLocation.setLocationTime(new Date(aMapLocation.getTime()));//定位时间
        gisLocation.setAddress(aMapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果
        gisLocation.setCountry(aMapLocation.getCountry());//国家信息
        gisLocation.setProvince(aMapLocation.getProvince());//省信息
        gisLocation.setCity(aMapLocation.getCity());//城市信息
        gisLocation.setDistrict(aMapLocation.getDistrict());//城区信息
        gisLocation.setRoad(aMapLocation.getRoad());//街道信息
        gisLocation.setCityCode(aMapLocation.getCityCode());//城市编码
        gisLocation.setAdCode(aMapLocation.getAdCode());//地区编码
        gisLocation.setAltitude(NumberUtil.roundNumber(aMapLocation.getAltitude(), 2));
        gisLocation.setSatellites(aMapLocation.getSatellites());

        return gisLocation;
    }

    public static GISLocationOption BuildGISLocationOption() {

        GISLocationOption locationOption = new GISLocationOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(GISLocationOption.LocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(2000);

        return locationOption;

    }

    public static GISLocationOption BuildSingleGISLocationOption() {

        GISLocationOption locationOption = new GISLocationOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(GISLocationOption.LocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(true);

        return locationOption;

    }

    public GISLocationOption getLocationOption() {
        return mlocationOption;
    }

    private AMapLocationClientOption generateConvertOption() {
        //初始化定位参数
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        if (mlocationOption.getLocationMode().equals(GISLocationOption.LocationMode.Hight_Accuracy))
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        else if (mlocationOption.getLocationMode().equals(GISLocationOption.LocationMode.Battery_Saving))
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        else if (mlocationOption.getLocationMode().equals(GISLocationOption.LocationMode.Device_Sensors))
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        else
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(mlocationOption.isNeedAddress());
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(mlocationOption.isOnceLocation());
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(mlocationOption.isWifiActiveScan());
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(mlocationOption.isMockEnable());
        //设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(mlocationOption.getInterval());

        return locationOption;
    }

    public void start() {
        //初始化定位
        mLocationClient = new AMapLocationClient(MyApplication.getInstance().getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);

        AMapLocationClientOption aMapLocationClientOption = this.generateConvertOption();
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(aMapLocationClientOption);
        //启动定位
        mLocationClient.startLocation();

    }

    public void stop() {

        mLocationClient.unRegisterLocationListener(this);
        //停止定位
        mLocationClient.stopLocation();
        //销毁定位客户端。
        mLocationClient.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mLocationListener.onLocationChanged(ConvertLocation(aMapLocation));
    }


//    public static GISLocationOption.LocationMode calLocationMode() {
//
//    }

}
