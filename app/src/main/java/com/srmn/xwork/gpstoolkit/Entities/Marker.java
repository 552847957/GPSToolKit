package com.srmn.xwork.gpstoolkit.Entities;

import com.google.gson.reflect.TypeToken;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.IOUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiler on 2016/2/20.
 */
@Table(name = "Marker")
public class Marker implements Serializable {

    @Column(name = "name")
    public String name;
    @Column(name = "description")
    public String description;
    @Column(name = "locationInfo")
    public String locationInfo;
    @Column(name = "images")
    public String images;
    @Column(name = "markerCategoryID")
    public int markerCategoryID;
    public List<String> imagesList;
    @Column(name = "id", isId = true, autoGen = true)
    private Integer id;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "altitude")
    private double altitude;
    @Column(name = "objID")
    private String objID;

    public Marker() {

    }

    public Marker(GISLocation currentLocation) {
        this.name = currentLocation.getAddress();
        this.description = currentLocation.getAddress();
        this.locationInfo = currentLocation.toLocationInfo();
        this.imagesList = new ArrayList<String>();
        this.latitude = currentLocation.getLatitude();
        this.longitude = currentLocation.getLongitude();
        this.altitude = currentLocation.getAltitude();
    }

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

    public List<String> getImagesList() {
        if (this.getImages() == null || this.getImages() == "") {
            String gson = GsonUtil.getGson().toJson(new ArrayList<String>());

            this.setImages(gson);
        }

        imagesList = GsonUtil.<List<String>>DeserializerSingleDataResult(this.getImages(), new TypeToken<List<String>>() {
        }.getType());

        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        String gson = GsonUtil.getGson().toJson(imagesList);

        this.setImages(gson);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {

        if (StringUtil.isNullOrEmpty(locationInfo))
            return;

        this.locationInfo = locationInfo;

        String[] datas = this.locationInfo.split(",");

        if (datas.length > 0) {
            this.setLongitude(Double.parseDouble(datas[0]));
        }
        if (datas.length > 1) {
            this.setLatitude(Double.parseDouble(datas[1]));
        }
        if (datas.length > 2) {
            this.setAltitude(Double.parseDouble(datas[2]));
        }

    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getMarkerCategoryID() {
        return markerCategoryID;
    }

    public void setMarkerCategoryID(int markerCategoryID) {
        this.markerCategoryID = markerCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {

        int totalImageCount = this.getImagesList().size();

        long totalSize = 0;

        for (String imagePath : this.getImagesList()) {
            totalSize += new File(imagePath).length();
        }

        return "共" + totalImageCount + "张图片 | 总体积" + IOUtil.getDataSize(totalSize) + "";
    }

    @Override
    public String toString() {
        return this.getId() + "";
    }
}
