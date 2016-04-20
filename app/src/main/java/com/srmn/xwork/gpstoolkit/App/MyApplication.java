package com.srmn.xwork.gpstoolkit.App;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.genymotion.api.GenymotionManager;
import com.genymotion.api.Gps;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.gis.GISLocationService;
import com.srmn.xwork.androidlib.gis.GISSatelliteStatus;
import com.srmn.xwork.androidlib.utils.AMapUtils;
import com.srmn.xwork.androidlib.utils.DateTimeUtil;
import com.srmn.xwork.androidlib.utils.NumberUtil;
import com.srmn.xwork.androidlib.utils.SharedPrefsUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.gpstoolkit.Cfg.Configuration;
import com.srmn.xwork.gpstoolkit.Dao.DaoContainer;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;
import com.srmn.xwork.gpstoolkit.Entities.RouterPathItem;
import com.srmn.xwork.gpstoolkit.HomeFragment;
import com.srmn.xwork.gpstoolkit.Leancloud.LeancloudDb;
import com.srmn.xwork.gpstoolkit.Main;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.Date;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by kiler on 2016/2/20.
 */
public class MyApplication extends com.srmn.xwork.androidlib.ui.MyApplication {


    public static final String TAG = "MyApplication";
    public static final String GPS_TOOL_KIT = "GPSToolKit";
    public static final String KEY_CURRENT_TACKER_PATH_CODE = "CurrentTackerPathCode";

    public static final String INTENAL_ACTION_NEWTRACKERITEM = "GPSToolKit.action.newTrackerItem";
    public static final String INTENAL_ACTION_NEWTRACKERITEM_ROUTE = "RoutePath";

    protected DbManager dbmanager;
    protected DaoContainer daoContainer;
    protected LeancloudDb cloudDb;
    private MyGSPReceiver receiver = null;

    public static MyApplication getInstance() {
        return (MyApplication) instance;
    }

    private void initBuglyBeta() {
        /***** Beta高级设置 *****/
        /**
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;
        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = false;
        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000;
        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(Main.class);

        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(this, Configuration.BuglyAppID, true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initBuglyBeta();

//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                // 数据库的名字
                .setDbName("GPSToolKit")
                // 保存到指定路径
                // .setDbDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()))
                // 数据库的版本号
                .setDbVersion(10)
                // 数据库版本更新监听
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        if (oldVersion < newVersion) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3
                            Log.e(TAG, "数据库版本更新了！");
                            try {
                                db.dropDb();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

        dbmanager = x.getDb(daoConfig);
        daoContainer = new DaoContainer(dbmanager);
        cloudDb = new LeancloudDb();
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, Configuration.LeancloudAppID, Configuration.LeancloudAppKey);


        //注册广播接收器
        receiver = new MyGSPReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GISLocationService.SERVICE_NAME);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    public DaoContainer getDaos() {
        return daoContainer;
    }

    public LeancloudDb getCloudDb() {
        return cloudDb;
    }

    public boolean getStackerIsStart() {
        return !getCurrentTackerPathCode().equals("");
    }

    public void startNewTracePath() {
        //存在已经开始的轨迹记录现必须关闭了
        if (getStackerIsStart()) {
            endCurrentTracePath();
        }

        String codePrefix = DateTimeUtil.FormatDateTime(new Date(), "yyyyMMddHHmmss");

        int i = 1;
        boolean findCode = true;
        String code = "";

        while (findCode) {
            code = codePrefix + NumberUtil.GetCode(i, 3);
            findCode = getDaos().getRouterPathDaoInstance().existByCode(code);
            i++;
        }

        RouterPath routerPath = new RouterPath();
        routerPath.setCode(code);
        routerPath.setStartTime(new Date());
        routerPath.setSpeed(0);
        routerPath.setIsUpload(false);
        routerPath.setPointCount(0);
        routerPath.setDistance(0);
        routerPath.setComment("");

        try {
            getDaos().getRouterPathDaoInstance().saveBindingId(routerPath);
            setCurrentTackerPathCode(routerPath.getCode());
            showShortToastMessage("开始记录轨迹成功！");
        } catch (DbException e) {
            showShortToastMessage("开始记录轨迹失败：" + e.getMessage());
            e.printStackTrace();
        }

    }

    public void TrackerCurrentTracePathItem(GISLocation location) {

        if (location.getLongitude() < 1) {
            Log.i(TAG, "无效点，跳过！");
            return;
        }

        if (!getStackerIsStart())
            return;

        RouterPath routerPath = getDaos().getRouterPathDaoInstance().findByCode(getCurrentTackerPathCode());

        if (routerPath == null)
            return;

        RouterPathItem routerPathItem = new RouterPathItem();
        routerPathItem.setRouterPathid(routerPath.getId());
        routerPathItem.setRouteLocalTime(new Date());
        routerPathItem.setRouteGPSTime(location.getLocationTime());
        routerPathItem.setLatitude(location.getLatitude());
        routerPathItem.setLongitude(location.getLongitude());
        routerPathItem.setAltitude(location.getAltitude());
        routerPathItem.setDirection(location.getBearing());
        routerPathItem.setSpeed(location.getSpeed());
        routerPathItem.setAccuracy(location.getAccuracy());


        if (routerPath.getLastLatLngHashCode() != null && routerPath.getLastLatLngHashCode().equals(routerPathItem.getPointHashCode())) {
            Log.i(TAG, "与上一个轨迹点重复，跳过！");
            showShortToastMessage("与上一个轨迹点重复，跳过！轨迹点：" + routerPathItem.getPointString() + "--HashCode:" + routerPathItem.getPointHashCode());
            return;
        }


        try {
            getDaos().getRouterPathItemDaoInstance().save(routerPathItem);
            //更新主表信息
            UpdateRouterPathItemInfo(routerPath);
            routerPath.setLastLatLngHashCode(routerPathItem.getPointHashCode());
            getDaos().getRouterPathDaoInstance().update(routerPath);

            if (routerPath.getPointCount() != null && routerPath.getPointCount() > 1) {
                Intent intent = new Intent(INTENAL_ACTION_NEWTRACKERITEM);
                intent.putExtra(INTENAL_ACTION_NEWTRACKERITEM_ROUTE, routerPath);
                sendBroadcast(intent);
            }
            Log.i(TAG, "更新轨迹点成功！");
        } catch (DbException e) {
            Log.e(TAG, "更新轨迹点失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void endCurrentTracePath() {
        if (!getStackerIsStart())
            return;

        RouterPath routerPath = getDaos().getRouterPathDaoInstance().findByCode(getCurrentTackerPathCode());

        if (routerPath == null) {
            setCurrentTackerPathCode("");
            showShortToastMessage("当前跟踪已经失效，清理ID成功！");
            return;
        }

        try {

            UpdateRouterPathItemInfo(routerPath);

            routerPath.setEndTime(new Date());
            routerPath.setIsUpload(false);
            getDaos().getRouterPathDaoInstance().update(routerPath);
            setCurrentTackerPathCode("");


            showShortToastMessage("结束记录轨迹成功！共计" + routerPath.getPointCount() + "个点");


        } catch (DbException e) {
            showShortToastMessage("结束记录轨迹失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void UpdateRouterPathItemInfo(RouterPath routerPath) {

        List<RouterPathItem> subItems = getDaos().getRouterPathItemDaoInstance().findAllByRouterPathId(routerPath.getId());

        double distance = 0;

        if (subItems.size() >= 2) {
            for (int i = 1; i < subItems.size(); i++) {
                distance = distance + AMapUtils.calculateLineDistance(subItems.get(i - 1).getLatitude(), subItems.get(i - 1).getLongitude(), subItems.get(i).getLatitude(), subItems.get(i).getLongitude());
            }
        }

        Date endTime = routerPath.getEndTime();

        if (endTime == null) {
            endTime = new Date();
        }

        long s = (endTime.getTime() - routerPath.getStartTime().getTime()) / 1000;

        routerPath.setSpeed(distance / s);
        routerPath.setPointCount(subItems.size());
        routerPath.setDistance(distance);
        routerPath.setItems(subItems);
    }


    private String getCurrentTackerPathCode() {
        return SharedPrefsUtil.getStringValue(GPS_TOOL_KIT, KEY_CURRENT_TACKER_PATH_CODE, "");
    }

    private void setCurrentTackerPathCode(String currentTackerPathCode) {
        SharedPrefsUtil.putStringValue(GPS_TOOL_KIT, KEY_CURRENT_TACKER_PATH_CODE, currentTackerPathCode);
    }

    public RouterPath getCurrentTackerPath() {
        return getDaos().getRouterPathDaoInstance().findByCode(getCurrentTackerPathCode());
    }

    /**
     * 获取广播数据
     *
     * @author jiqinlin
     */
    public class MyGSPReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle.containsKey(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_LOCATION)) {
                GISLocation currentLocation = (GISLocation) bundle.getSerializable(GISLocationService.INTENT_ACTION_UPDATE_DATA_EXTRA_LOCATION);
                if (getStackerIsStart())
                    TrackerCurrentTracePathItem(currentLocation);
            }

        }
    }

}
