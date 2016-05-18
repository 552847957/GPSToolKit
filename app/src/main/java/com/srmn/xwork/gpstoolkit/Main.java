package com.srmn.xwork.gpstoolkit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.gis.GISLocationService;
import com.srmn.xwork.androidlib.gis.GISSatelliteStatus;
import com.srmn.xwork.androidlib.utils.DeviceUtils;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.ServiceUtil;
import com.srmn.xwork.androidlib.utils.SharedPrefsUtil;
import com.srmn.xwork.gpstoolkit.App.BaseActivity;
import com.srmn.xwork.gpstoolkit.Entities.Marker;
import com.tencent.bugly.beta.Beta;


import org.xutils.view.annotation.ContentView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_main)
public class Main extends BaseActivity
        implements TrackerFragment.OnFragmentInteractionListener
        , LocationFragment.OnFragmentInteractionListener {
    private static final String TAG = "Main";

    //声明AMapLocationClient类对象


    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private ViewPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private Handler mainhandler = new Handler();


    public GISLocation getCurrentLocation() {
        return getSharedPrefsJSonValue(TAG, "CurrentLocation", GISLocation.class);
    }


    public void setCurrentLocation(GISLocation currentLocation) {
        putSharedPrefsJSonValue(TAG, "CurrentLocation", currentLocation);
    }


    public GISSatelliteStatus getGisSatelliteStatus() {
        return getSharedPrefsJSonValue(TAG, "GisSatelliteStatus", GISSatelliteStatus.class);
    }

    public void setGisSatelliteStatus(GISSatelliteStatus gisSatelliteStatus) {
        putSharedPrefsJSonValue(TAG, "GisSatelliteStatus", gisSatelliteStatus);
    }

    private MyReceiver receiver = null;


    private void openGPSSettings() {

        boolean isGPSOn = DeviceUtils.isGPS_ON();

        if (isGPSOn) {

            getMyApp().showShortToastMessage("GPS模块正常");

            return;
        } else {

            getMyApp().showShortToastMessage("请开启GPS！");

            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            startActivity(intent);

            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.application_map_32);

        mTabLayout = (TabLayout) findViewById(R.id.tl_main_tabs);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new HomeFragment(), "首页");
        mSectionsPagerAdapter.addFragment(new LocationFragment(), "位置标注");
        mSectionsPagerAdapter.addFragment(new TrackerFragment(), "线路追踪");
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        openGPSSettings();

        mainhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /***** 检查更新 *****/
                Beta.checkUpgrade();
            }
        }, 3000);

    }


    private void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //下面传入参数
            m.invoke(menu, enable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(Main.this, GISLocationService.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        boolean serviceIsStart = ServiceUtil.isWorked(GISLocationService.SERVICE_NAME, this);

        if (!serviceIsStart) {
            startService(new Intent(Main.this, GISLocationService.class));
        }

        //注册广播接收器
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GISLocationService.SERVICE_NAME);
        registerReceiver(receiver, filter);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        setIconEnable(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public boolean ConnectToGPS() {
        if (getGisSatelliteStatus() == null)
            return false;
        if (getGisSatelliteStatus().getConnnectSatellites() <= 0)
            return false;
        return true;
    }

    public void newMark() {


        if (getCurrentLocation() == null) {
            getMyApp().showShortToastMessage("没有位置信息");
            return;
        }

        if (getCurrentLocation().getLatitude() < 1 || getCurrentLocation().getLongitude() < 1) {
            getMyApp().showShortToastMessage("没有位置信息,无法发布");
            return;
        }

        Marker marker = new Marker(getCurrentLocation());

        Intent intent = new Intent();
        //Intent传递参数
        intent.putExtra("EditMarker", marker);

        gotoActivity(intent, MarkEditor.class);

    }

    public void trackPath() {
        if (getCurrentLocation() == null) {
            getMyApp().showShortToastMessage("没有位置信息");
            return;
        }

        Intent intent = new Intent();
        //Intent传递参数
        intent.putExtra("location", getCurrentLocation());

        gotoActivity(intent, TrackerMap.class);
    }


    /**
     * 获取广播数据
     *
     * @author jiqinlin
     */
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle.containsKey(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_LOCATION)) {
                setCurrentLocation((GISLocation) bundle.getSerializable(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_LOCATION));
                ((HomeFragment) mSectionsPagerAdapter.getItem(0)).setLocation(getCurrentLocation());
            } else if (bundle.containsKey(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_SATELLITE_STATUS)) {
                setGisSatelliteStatus((GISSatelliteStatus) bundle.getSerializable(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_SATELLITE_STATUS));
                ((HomeFragment) mSectionsPagerAdapter.getItem(0)).setGPSSatellites(getGisSatelliteStatus().getFindSatellites(), getGisSatelliteStatus().getConnnectSatellites());
            }

        }
    }


    /**
     * 主页面VierPager适配器
     * Created by Lichenwei
     * Date: 2015-08-16
     * Time: 13:47
     */
    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
            mTitles = new ArrayList<String>();
        }

        /**
         * 新添Fragment内容和标题
         *
         * @param fragment
         * @param title
         */
        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
