package com.srmn.xwork.gpstoolkit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.srmn.xwork.androidlib.utils.ServiceUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.gpstoolkit.App.BaseActivity;
import com.srmn.xwork.gpstoolkit.App.MyApplication;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;
import com.srmn.xwork.gpstoolkit.Entities.RouterPathItem;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@ContentView(R.layout.activity_tracker_map)
public class TrackerMap extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "TrackerMap";
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
    Timer timer = new Timer();
    private MapView mapView;
    private com.amap.api.maps.AMap aMap;
    private RouterPath routerPath;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                if (!getMyApp().getStackerIsStart()) {
                    return;
                }
                routerPath = getMyApp().getCurrentTackerPath();

                if (routerPath == null)
                    return;

                getMyApp().UpdateRouterPathItemInfo(routerPath);

                txtTrackerTime.setText(StringUtil.formatTime(routerPath.getTimeCount()));
                txtTrackerLength.setText(StringUtil.formatDistance(routerPath.getDistance()));
                if (routerPath.getItems().size() < 2) {
                    return;
                }
                List<LatLng> points = new ArrayList<LatLng>();
                LatLngBounds.Builder build = new LatLngBounds.Builder();

                for (RouterPathItem item : routerPath.getItems()) {
                    points.add(new LatLng(item.getLatitude(), item.getLongitude()));
                    build.include(new LatLng(item.getLatitude(), item.getLongitude()));
                }

                aMap.clear();

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
            super.handleMessage(msg);
        }

        ;
    };
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    private GISLocation currentLocation;
    private GISSatelliteStatus gisSatelliteStatus;
    private MyGPSReceiver gpsReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        hideActionBar();


        Intent intent = getIntent();

        if (intent.hasExtra("location")) {
            currentLocation = (GISLocation) intent.getSerializableExtra("location");

            txtTrackerLocationInfo.setText(currentLocation.toLocationInfo());
        }


        if (getMyApp().getStackerIsStart()) {
            btnStart.setVisibility(View.GONE);
            btnEnd.setVisibility(View.VISIBLE);
        } else {
            btnStart.setVisibility(View.VISIBLE);
            btnEnd.setVisibility(View.GONE);
        }

        btnUpload.setVisibility(View.GONE);

        btnStart.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            initMap();
        }

        timer.schedule(task, 5000, 10000); // 10s后执行task,经过10s再次执行


    }

    private void initMap() {

        if (aMap == null)
            return;
        aMap.getUiSettings().setZoomGesturesEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(false);
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {


                if (currentLocation != null) {
                    LatLng centerPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                    moveToCenter(centerPosition);
                }

            }
        });


        //使用Intent对象得到FirstActivity传递来的参数

    }

    private void moveToCenter(LatLng centerPosition) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPosition, 17));
        aMap.addMarker(new MarkerOptions()
                .position(centerPosition)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
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
        if (timer != null)
            timer.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();

        gpsReceiver = new MyGPSReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(GISLocationService.SERVICE_NAME);
        registerReceiver(gpsReceiver, filter1);



    }
    //注册广播接收器

    @Override
    public void onStop() {
        super.onStop();
        if (gpsReceiver == null) {
            unregisterReceiver(gpsReceiver);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                if (getMyApp().getStackerIsStart()) {
                    showShortToastMessage("轨迹跟踪已经开始，请先关闭！");
                    return;
                }
                getMyApp().startNewTracePath();
                btnStart.setVisibility(View.GONE);
                btnEnd.setVisibility(View.VISIBLE);
                break;
            case R.id.btnEnd:
                if (!getMyApp().getStackerIsStart()) {
                    showShortToastMessage("轨迹跟踪未开始，请先开始！");
                    return;
                }
                getMyApp().endCurrentTracePath();
                finish();
                break;
        }


    }




    /**
     * 获取广播数据
     *
     * @author jiqinlin
     */
    public class MyGPSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle.containsKey(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_LOCATION)) {
                currentLocation = (GISLocation) bundle.getSerializable(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_LOCATION);
                txtTrackerLocationInfo.setText(currentLocation.toLocationInfo());

            } else if (bundle.containsKey(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_SATELLITE_STATUS)) {
                gisSatelliteStatus = (GISSatelliteStatus) bundle.getSerializable(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_SATELLITE_STATUS);
                txtSatelliteCount.setText(gisSatelliteStatus.getConnnectSatellites() + "");
                if (gisSatelliteStatus.getConnnectSatellites() <= 0) {
                    txtTrackerStatus.setText("GPS卫星连接中...");
                } else {
                    txtTrackerStatus.setText("GPS卫星已连接。");
                }
            }

        }
    }

}
