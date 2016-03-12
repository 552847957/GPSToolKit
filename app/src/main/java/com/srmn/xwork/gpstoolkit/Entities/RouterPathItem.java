package com.srmn.xwork.gpstoolkit.Entities;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by kiler on 2016/3/10.
 */
@Table(name = "RouterPathItem")
public class RouterPathItem implements Serializable {

    @Column(name = "id", isId = true, autoGen = true)
    private Integer id;
    @Column(name = "routerPathid")
    private Integer routerPathid;
    @Column(name = "routeGPSTime")
    private Date routeGPSTime;
    @Column(name = "routeLocalTime")
    private Date routeLocalTime;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "altitude")
    private double altitude;
    @Column(name = "speed")
    private double speed;
    @Column(name = "direction")
    private double direction;
    @Column(name = "accuracy")
    private double accuracy;
    @Column(name = "objID")
    private String objID;

    public String getObjID() {
        return objID;
    }

    public void setObjID(String objID) {
        this.objID = objID;
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

    public Date getRouteGPSTime() {
        return routeGPSTime;
    }

    public void setRouteGPSTime(Date routeGPSTime) {
        this.routeGPSTime = routeGPSTime;
    }

    public Date getRouteLocalTime() {
        return routeLocalTime;
    }

    public void setRouteLocalTime(Date routeLocalTime) {
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

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }


    public int getPointHashCode() {
        return getPointString().hashCode();
    }

    public String getPointString() {
        return String.format("%3.6f,%3.6f", this.getLatitude(), this.getLongitude());
    }
}
