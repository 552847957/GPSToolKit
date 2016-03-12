package com.srmn.xwork.gpstoolkit.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by kiler on 2016/3/10.
 */
@Table(name = "RouterPath")
public class RouterPath implements Serializable {

    @Column(name = "code")
    public String code;
    @Column(name = "id", isId = true, autoGen = true)
    private Integer id;
    @Column(name = "pointCount")
    private Integer pointCount;
    @Column(name = "distance")
    private double distance;
    @Column(name = "startTime")
    private Date startTime;
    @Column(name = "endTime")
    private Date endTime;
    @Column(name = "speed")
    private double speed;
    @Column(name = "comment")
    private String comment;
    @Column(name = "isUpload")
    private boolean isUpload;

    @Column(name = "lastLatLngHashCode")
    private Integer lastLatLngHashCode;
    @Column(name = "objID")
    private String objID;
    private List<RouterPathItem> items;

    public Integer getLastLatLngHashCode() {
        return lastLatLngHashCode;
    }

    public void setLastLatLngHashCode(Integer lastLatLngHashCode) {
        this.lastLatLngHashCode = lastLatLngHashCode;
    }

    public String getObjID() {
        return objID;
    }

    public void setObjID(String objID) {
        this.objID = objID;
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public List<RouterPathItem> getItems() {
        return items;
    }

    public void setItems(List<RouterPathItem> items) {
        this.items = items;
    }


    public int getTimeCount() {

        Date endTime = this.getEndTime();

        if (endTime == null) {
            endTime = new Date();
        }

        return (int) (endTime.getTime() - this.getStartTime().getTime()) / 1000;
    }
}
