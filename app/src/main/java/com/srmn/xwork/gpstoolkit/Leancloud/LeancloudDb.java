package com.srmn.xwork.gpstoolkit.Leancloud;

import com.avos.avoscloud.AVObject;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;
import com.srmn.xwork.gpstoolkit.Entities.RouterPathItem;

/**
 * Created by kiler on 2016/3/22.
 */
public class LeancloudDb {

    public LeancloudDb() {

    }


    public AVObject convertRouterPathToClould(RouterPath routerPath) {
        AVObject avObject = new AVObject("RouterPath");

        avObject.put("Id", routerPath.getId());
        avObject.put("Code", routerPath.getCode());
        avObject.put("PointCount", routerPath.getPointCount());
        avObject.put("Distance", routerPath.getDistance()); // 4 表示微博机构认证，假设的。
        avObject.put("StartTime", routerPath.getStartTime());
        avObject.put("EndTime", routerPath.getEndTime());
        avObject.put("Speed", routerPath.getSpeed());
        avObject.put("Comment", routerPath.getComment());
        avObject.put("IsUpload", routerPath.getIsUpload());
        avObject.put("LastLatLngHashCode", routerPath.getLastLatLngHashCode());
        avObject.put("ObjID", routerPath.getObjID());

        return avObject;
    }

    public AVObject convertRouterPathItemToClould(RouterPathItem routerPathItem) {
        AVObject avObject = new AVObject("RouterPathItem");

        avObject.put("Id", routerPathItem.getId());
        avObject.put("RouterPathid", routerPathItem.getRouterPathid());
        avObject.put("RouteGPSTime", routerPathItem.getRouteGPSTime());
        avObject.put("RouteLocalTime", routerPathItem.getRouteLocalTime());
        avObject.put("Latitude", routerPathItem.getLatitude());
        avObject.put("Longitude", routerPathItem.getLongitude());
        avObject.put("Altitude", routerPathItem.getAltitude());
        avObject.put("Speed", routerPathItem.getSpeed());
        avObject.put("Direction", routerPathItem.getDirection());
        avObject.put("Accuracy", routerPathItem.getAccuracy());
        avObject.put("ObjID", routerPathItem.getObjID());

        return avObject;
    }


}
