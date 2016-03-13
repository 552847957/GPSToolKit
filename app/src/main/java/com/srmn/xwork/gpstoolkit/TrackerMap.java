package com.srmn.xwork.gpstoolkit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.gis.GISLocationService;
import com.srmn.xwork.androidlib.gis.GISSatelliteStatus;
import com.srmn.xwork.androidlib.utils.ServiceUtil;
import com.srmn.xwork.gpstoolkit.App.BaseActivity;
import com.srmn.xwork.gpstoolkit.App.MyApplication;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


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

    private MapView mapView;

    private com.amap.api.maps.AMap aMap;

    private MyReceiver receiver = null;

    private GISLocation currentLocation;
    private GISSatelliteStatus gisSatelliteStatus;

    private MyGPSReceiver gpsReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        hideActionBar();

        btnStart.setVisibility(View.VISIBLE);
        btnEnd.setVisibility(View.VISIBLE);
        btnUpload.setVisibility(View.GONE);

        btnStart.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            initMap();
        }
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

        //使用Intent对象得到FirstActivity传递来的参数
        Intent intent = getIntent();

        if (intent.hasExtra("location")) {

            GISLocation location = (GISLocation) intent.getSerializableExtra("location");

            LatLng centerPosition = new LatLng(location.getLatitude(), location.getLongitude());

            moveToCenter(centerPosition);

        }
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
    }

    @Override
    public void onStart() {
        super.onStart();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyApplication.INTENAL_ACTION_NEWTRACKERITEM);
        registerReceiver(receiver, filter);
    }
    //注册广播接收器

    @Override
    public void onStop() {
        super.onStop();
        if (receiver == null) {
            unregisterReceiver(receiver);
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
                break;
            case R.id.btnEnd:
                if (!getMyApp().getStackerIsStart()) {
                    showShortToastMessage("轨迹跟踪未开始，请先开始！");
                    return;
                }
                getMyApp().endCurrentTracePath();
                break;
        }


    }


    /**
     * 获取广播数据
     *
     * @author jiqinlin
     */
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Bundle bundle=intent.getExtras();

            Log.i(TAG, "检测到新的点。");

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
