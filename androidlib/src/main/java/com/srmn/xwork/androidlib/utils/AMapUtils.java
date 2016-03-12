package com.srmn.xwork.androidlib.utils;

import com.amap.api.maps.model.LatLng;
import com.srmn.xwork.androidlib.gis.GISLocation;

/**
 * Created by kiler on 2016/3/11.
 */
public class AMapUtils {

    //计算地图上矩形区域的面积，单位平方米。
    public static float calculateArea(double leftTopLat, double leftTopLng, double rightBottomLat, double rightBottomLng) {
        return com.amap.api.maps.AMapUtils.calculateArea(new LatLng(leftTopLat, leftTopLng), new LatLng(rightBottomLat, rightBottomLng));
    }

    //根据用户的起点和终点经纬度计算两点间距离，此距离为相对较短的距离，单位米。
    public static float calculateLineDistance(double startLat, double startLng, double endLat, double endLng) {
        return com.amap.api.maps.AMapUtils.calculateLineDistance(new LatLng(startLat, startLng), new LatLng(endLat, endLng));
    }

}
