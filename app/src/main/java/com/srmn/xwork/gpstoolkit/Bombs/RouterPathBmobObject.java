package com.srmn.xwork.gpstoolkit.Bombs;

import org.xutils.db.annotation.Column;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by kiler on 2016/3/12.
 */
public class RouterPathBmobObject extends BmobObject {

    public String code;
    private Integer id;
    private Integer pointCount;
    private Double distance;
    private String startTime;
    private String endTime;
    private Double speed;
    private String comment;
    private Boolean isUpload;
    private Integer lastLatLngHashCode;
    private String deviceID;

    public RouterPathBmobObject() {
        this.setTableName("RouterPath");
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public Integer getLastLatLngHashCode() {
        return lastLatLngHashCode;
    }

    public void setLastLatLngHashCode(Integer lastLatLngHashCode) {
        this.lastLatLngHashCode = lastLatLngHashCode;
    }

    public Integer getPointCount() {
        return pointCount;
    }

    public void setPointCount(Integer pointCount) {
        this.pointCount = pointCount;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
