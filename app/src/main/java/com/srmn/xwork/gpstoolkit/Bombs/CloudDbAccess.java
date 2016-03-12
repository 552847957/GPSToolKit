package com.srmn.xwork.gpstoolkit.Bombs;

import android.content.Context;

import com.srmn.xwork.androidlib.utils.DateTimeUtil;
import com.srmn.xwork.androidlib.utils.DeviceUtils;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.gpstoolkit.App.MyApplication;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;
import com.srmn.xwork.gpstoolkit.Entities.RouterPathItem;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by kiler on 2016/3/12.
 */
public class CloudDbAccess {

    private Context context;

    public CloudDbAccess(Context context) {
        this.context = context;
    }

    public void cloudUpdateRouterPath(final RouterPath item) {

        final RouterPathBmobObject routerPath = new RouterPathBmobObject();
        routerPath.setId(item.getId());
        routerPath.setCode(item.getCode());
        routerPath.setStartTime(DateTimeUtil.FormatDateTime(item.getStartTime()));
        routerPath.setEndTime(DateTimeUtil.FormatDateTime(item.getEndTime()));
        routerPath.setPointCount(item.getId());
        routerPath.setComment(item.getComment());
        routerPath.setDeviceID(DeviceUtils.getAndroid_Id(context));
        routerPath.setDistance(item.getDistance());
        routerPath.setIsUpload(item.getIsUpload());
        routerPath.setLastLatLngHashCode(item.getLastLatLngHashCode());
        routerPath.setSpeed(item.getSpeed());
        routerPath.save(context, new SaveListener() {
            @Override
            public void onSuccess() {

                final String objID = routerPath.getObjectId();

                cloudSaveRouterPathItem(item.getItems());

                try {
                    MyApplication.getInstance().getDaos().getRouterPathDaoInstance().updateObjectID(item, objID);
                    MyApplication.getInstance().showShortToastMessage("云数据插入成功");
                } catch (DbException e) {
                    MyApplication.getInstance().showShortToastMessage("更新云ID失败：" + e.getMessage());
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int i, String s) {
                MyApplication.getInstance().showShortToastMessage("云数据插入失败:" + s);
            }
        });

    }


    public void cloudSaveRouterPathItem(List<RouterPathItem> subItems) {

        List<BmobObject> datas = new ArrayList<BmobObject>();
        for (int i = 0; i < subItems.size(); i++) {

            RouterPathItem subItem = subItems.get(i);

            RouterPathItemBmobObject routerPathItem = new RouterPathItemBmobObject();

            routerPathItem.setId(subItem.getId());
            routerPathItem.setSpeed(subItem.getSpeed());
            routerPathItem.setAccuracy(subItem.getAccuracy());
            routerPathItem.setAltitude(subItem.getAltitude());
            routerPathItem.setLatitude(subItem.getLatitude());
            routerPathItem.setLongitude(subItem.getLongitude());
            routerPathItem.setDeviceID(DeviceUtils.getAndroid_Id(context));
            routerPathItem.setRouteGPSTime(DateTimeUtil.FormatDateTime(subItem.getRouteGPSTime()));
            routerPathItem.setRouteLocalTime(DateTimeUtil.FormatDateTime(subItem.getRouteLocalTime()));
            routerPathItem.setDirection(subItem.getDirection());
            routerPathItem.setRouterPathid(subItem.getRouterPathid());

            routerPathItem.save(context);

        }
    }


    public void cloudSaveRouterPathItem(List<RouterPathItem> subItems, SaveListener saveListener) {

        List<BmobObject> datas = new ArrayList<BmobObject>();
        for (int i = 0; i < subItems.size(); i++) {

            RouterPathItem subItem = subItems.get(i);

            RouterPathItemBmobObject routerPathItem = new RouterPathItemBmobObject();

            routerPathItem.setId(subItem.getId());
            routerPathItem.setSpeed(subItem.getSpeed());
            routerPathItem.setAccuracy(subItem.getAccuracy());
            routerPathItem.setAltitude(subItem.getAltitude());
            routerPathItem.setLatitude(subItem.getLatitude());
            routerPathItem.setLongitude(subItem.getLongitude());
            routerPathItem.setDeviceID(DeviceUtils.getAndroid_Id(context));
            routerPathItem.setRouteGPSTime(DateTimeUtil.FormatDateTime(subItem.getRouteGPSTime()));
            routerPathItem.setRouteLocalTime(DateTimeUtil.FormatDateTime(subItem.getRouteLocalTime()));
            routerPathItem.setDirection(subItem.getDirection());
            routerPathItem.setRouterPathid(subItem.getRouterPathid());

            datas.add(routerPathItem);
        }
        new BmobObject().insertBatch(context, datas, saveListener);

    }
}
