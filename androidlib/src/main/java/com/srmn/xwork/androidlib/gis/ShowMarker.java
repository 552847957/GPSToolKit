package com.srmn.xwork.androidlib.gis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiler on 2016/5/15.
 */
public class ShowMarker implements Serializable {
    public int iconResourseID;
    public double lat;
    public double lng;
    public String title;
    public String jsonData;


    public ShowMarker() {

    }


    public int getIconResourseID() {
        return iconResourseID;
    }

    public void setIconResourseID(int iconResourseID) {
        this.iconResourseID = iconResourseID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
