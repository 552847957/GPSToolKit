package com.srmn.xwork.androidlib.gis;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiler on 2016/3/14.
 */
public class AMapHelper {


    //计算地图上矩形区域的面积，单位平方米。
    public static float calculateArea(double leftTopLat, double leftTopLng, double rightBottomLat, double rightBottomLng) {
        return com.amap.api.maps.AMapUtils.calculateArea(new LatLng(leftTopLat, leftTopLng), new LatLng(rightBottomLat, rightBottomLng));
    }

    //根据用户的起点和终点经纬度计算两点间距离，此距离为相对较短的距离，单位米。
    public static float calculateLineDistance(double startLat, double startLng, double endLat, double endLng) {
        return com.amap.api.maps.AMapUtils.calculateLineDistance(new LatLng(startLat, startLng), new LatLng(endLat, endLng));
    }

    public static Marker drawMarkerOnView(AMap aMap, BitmapDescriptor icon, LatLng centerPosition) {
        return aMap.addMarker(new MarkerOptions()
                .position(centerPosition)
                .icon(icon));


    }

    public static Marker drawMarkerOnView(AMap aMap, int iconResourseID, LatLng centerPosition) {
        return drawMarkerOnView(aMap, (BitmapDescriptorFactory.fromResource(iconResourseID)), centerPosition);
    }

    public static Marker drawMarkerOnView(AMap aMap, ShowMarker showMarker) {
        return aMap.addMarker(new MarkerOptions()
                .position(new LatLng(showMarker.getLat(), showMarker.getLng())).title(showMarker.getTitle()).snippet(showMarker.getTitle() + " 222")
                .icon(BitmapDescriptorFactory.fromResource(showMarker.getIconResourseID())));
    }

    public static MarkerOptions generateMarkerOptions(ShowMarker showMarker) {
        return new MarkerOptions().anchor(0.5f, 0.5f)
                .position(new LatLng(showMarker.getLat(), showMarker.getLng()))
                .title(showMarker.getTitle())
                .snippet(showMarker.getTitle() + " 222");
    }
    public static void showMarkersOnView(AMap aMap, List<ShowMarker> showMarkers) {

//        List<LatLng> points = new ArrayList<>();
//
//        for (ShowMarker showMarket : showMarkers) {
//            Marker marker = drawMarkerOnView(aMap, showMarket);
//            points.add(marker.getPosition());
//        }
//


        if (showMarkers.size() > 1) {
            ArrayList<MarkerOptions> markerOptionses = new ArrayList<>();

            for (ShowMarker showMarker : showMarkers) {
                markerOptionses.add(generateMarkerOptions(showMarker));
            }

            ArrayList<Marker> markers = aMap.addMarkers(markerOptionses, true);

            for (int i = 0; i < markers.size(); i++) {
                markers.get(i).setObject(showMarkers.get(i));
            }

        } else {
            Marker marker = aMap.addMarker(generateMarkerOptions(showMarkers.get(0)));
            marker.setObject(showMarkers.get(0));
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 11));
        }
    }



    public static Polyline drawLineOnView(AMap aMap, List<LatLng> lines, int startIconResourseID, int endIconResourseID, int color) {
        aMap.addMarker(new MarkerOptions()
                .position(lines.get(0))
                .icon(BitmapDescriptorFactory.fromResource(startIconResourseID)));

        aMap.addMarker(new MarkerOptions()
                .position(lines.get(lines.size() - 1))
                .icon(BitmapDescriptorFactory.fromResource(endIconResourseID)));


        return aMap.addPolyline((new PolylineOptions()).addAll(lines).color(color));
    }

    public static void setViewFit(AMap aMap, List<LatLng> points, int padding) {
        LatLngBounds.Builder build = new LatLngBounds.Builder();

        for (LatLng item : points) {
            build.include(item);
        }
        // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘zoom像素的填充区域
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(build.build(), padding));
    }

}
