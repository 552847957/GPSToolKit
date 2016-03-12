package com.srmn.xwork.gpstoolkit.Bombs;

import org.xutils.db.annotation.Column;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * String、Integer、Float、Short、Byte、Double、Character、Boolean、Object、Array。
 * 同时也支持BmobObject、BmobDate、BmobGeoPoint、BmobFile特有的数据类型。
 * Created by kiler on 2016/3/12.
 */
public class RouterPathItemBmobObject extends BmobObject {

    private Integer id;
    private Integer routerPathid;
    private String routeGPSTime;
    private String routeLocalTime;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double speed;
    private Double direction;
    private Double accuracy;
    private String deviceID;

    public RouterPathItemBmobObject() {
        this.setTableName("RouterPathItem");
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRouteGPSTime() {
        return routeGPSTime;
    }

    public void setRouteGPSTime(String routeGPSTime) {
        this.routeGPSTime = routeGPSTime;
    }

    public String getRouteLocalTime() {
        return routeLocalTime;
    }

    public void setRouteLocalTime(String routeLocalTime) {
        this.routeLocalTime = routeLocalTime;
    }

    public Integer getRouterPathid() {
        return routerPathid;
    }

    public void setRouterPathid(Integer routerPathid) {
        this.routerPathid = routerPathid;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
