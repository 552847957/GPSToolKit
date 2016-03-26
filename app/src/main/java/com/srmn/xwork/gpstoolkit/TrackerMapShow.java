package com.srmn.xwork.gpstoolkit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.gis.GISLocationService;
import com.srmn.xwork.androidlib.gis.GISSatelliteStatus;
import com.srmn.xwork.androidlib.utils.DateTimeUtil;
import com.srmn.xwork.androidlib.utils.DeviceUtils;
import com.srmn.xwork.androidlib.utils.ServiceUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.gpstoolkit.App.BaseActivity;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;
import com.srmn.xwork.gpstoolkit.Entities.RouterPathItem;
import com.srmn.xwork.gpstoolkit.Tasks.SaveRouterPathTask;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kiler on 2016/3/12.
 */
@ContentView(R.layout.activity_tracker_map)
public class TrackerMapShow extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "TrackerMapShow";
    @ViewInject(R.id.btnStart)
    protected Button btnStart;
    @ViewInject(R.id.btnEnd)
    protected Button btnEnd;
    @ViewInject(R.id.btnUpload)
    protected Button btnUpload;


    @ViewInject(R.id.txtTrackerTime)
    protected TextView txtTrackerTime;
    @ViewInject(R.id.txtTrackerLength)
    protected TextView txtTrackerLength;


    @ViewInject(R.id.txtSatelliteCount)
    protected TextView txtSatelliteCount;
    @ViewInject(R.id.txtTrackerStatus)
    protected TextView txtTrackerStatus;
    @ViewInject(R.id.txtTrackerLocationInfo)
    protected TextView txtTrackerLocationInfo;


    private MapView mapView;
    private com.amap.api.maps.AMap aMap;
    private RouterPath routerPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        hideActionBar();


        btnStart.setVisibility(View.GONE);
        btnEnd.setVisibility(View.GONE);
        btnUpload.setVisibility(View.VISIBLE);

        btnUpload.setOnClickListener(this);

        if (aMap == null) {
            aMap = mapView.getMap();
            initMap();
        }


    }


    private void initMap() {

        if (aMap == null)
            return;

        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(false);
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        //使用Intent对象得到FirstActivity传递来的参数
        Intent intent = getIntent();

        if (intent.hasExtra("route")) {

            routerPath = (RouterPath) intent.getSerializableExtra("route");
            routerPath.setItems(getMyApp().getDaos().getRouterPathItemDaoInstance().findAllByRouterPathId(routerPath.getId()));
            txtTrackerTime.setText(StringUtil.formatTime(routerPath.getTimeCount()));
            txtTrackerLength.setText(StringUtil.formatDistance(routerPath.getDistance()));
            if (routerPath.getItems().size() < 2) {
                showShortToastMessage("轨迹点少于2个不显示");
                return;
            }

            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {

                    List<LatLng> points = new ArrayList<LatLng>();
                    LatLngBounds.Builder build = new LatLngBounds.Builder();

                    for (RouterPathItem item : routerPath.getItems()) {
                        points.add(new LatLng(item.getLatitude(), item.getLongitude()));
                        build.include(new LatLng(item.getLatitude(), item.getLongitude()));
                    }

                    aMap.addMarker(new MarkerOptions()
                            .position(points.get(0))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.start64)));

                    aMap.addMarker(new MarkerOptions()
                            .position(points.get(points.size() - 1))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.end64)));


                    Polyline polyline = aMap.addPolyline((new PolylineOptions())
                            .addAll(points).color(Color.BLUE));

                    // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(build.build(), 10));


                }
            });


        }
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    //注册广播接收器

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpload:

                if (!DeviceUtils.isNetworkisAvailable(context)) {
                    showShortToastMessage("当前网络未能连通，请先保持网路连接！");
                    return;
                }

                if (routerPath == null) {
                    showShortToastMessage("当前对象不存在！");
                    return;
                }


                SaveRouterPathTask saveRouterPathTask = new SaveRouterPathTask(this, "上传数据", "开始上传数据", "数据上传中%s");

                saveRouterPathTask.execute(routerPath);

                break;
        }
    }


}
