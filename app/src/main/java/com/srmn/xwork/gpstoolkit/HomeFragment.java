package com.srmn.xwork.gpstoolkit;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.beardedhen.androidbootstrap.AwesomeTextView;

import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.maps.MyLocation;
import com.srmn.xwork.androidlib.maps.offlinemap.OfflineMapActivity;
import com.srmn.xwork.androidlib.ui.MyApplication;
import com.srmn.xwork.androidlib.utils.DateTimeUtil;
import com.srmn.xwork.androidlib.utils.NumberUtil;
import com.srmn.xwork.gpstoolkit.App.BaseFragment;


import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement th
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    private OnFragmentInteractionListener mListener;


    @BindView(R.id.iconStatus)
    protected AwesomeTextView iconStatus;
    @BindView(R.id.iconGPSStatus)
    protected AwesomeTextView iconGPSStatus;
    @BindView(R.id.txtGPSConnectInfo)
    protected TextView txtGPSConnectInfo;
    @BindView(R.id.txtGPSConnectSatellitesInfo)
    protected TextView txtGPSConnectSatellitesInfo;
    @BindView(R.id.txtGPSFindSatellitesInfo)
    protected TextView txtGPSFindSatellitesInfo;
    @BindView(R.id.txtConectText)
    protected TextView txtConectText;
    @BindView(R.id.txtlat)
    protected TextView txtlat;
    @BindView(R.id.txtlng)
    protected TextView txtlng;
    @BindView(R.id.txtaltaccuracy)
    protected TextView txtaltaccuracy;
    @BindView(R.id.txtaddress)
    protected TextView txtaddress;
    @BindView(R.id.gvMainNav)
    protected GridView gvMainNav;
    @BindView(R.id.imgGPSConnectSatellitesInfo)
    protected ImageView imgGPSConnectSatellitesInfo;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        // TODO Use fields...
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onStart() {
        super.onStart();
        iconStatus.startRotate(true, AwesomeTextView.AnimationSpeed.SLOW);
        iconGPSStatus.startFlashing(true, AwesomeTextView.AnimationSpeed.MEDIUM);
        loadMainNav();
    }

    @OnItemClick(R.id.gvMainNav)
    public void onItemClick(AdapterView<?> parent, View v, int aPosition,
                            long arg3) {
        int index = aPosition + 1;//id是从0开始的，所以需要+1
        Intent intent = new Intent();

        //MyApplication.getInstance().showShortToastMessage(index+"");

        switch (index) {
            case 1:
                Main main = (Main) this.getActivity();
                if (main != null && main.getCurrentLocation() != null && DateTimeUtil.GetTimeChangeSecond(main.getCurrentLocation().getLocationTime(), new Date()) > 20) {
                    MyApplication.getInstance().showShortToastMessage("最近20秒以内未能获取最新的定位信息。");
                    return;
                }
                ((Main) this.getActivity()).newMark();
                break;
            case 2:
                ((Main) this.getActivity()).trackPath();
                break;

            case 3:

                break;

            case 4:
                ((Main) this.getActivity()).gotoActivity(MyLocation.class);
                break;

            case 5:

                break;
            case 6:
                ((Main) this.getActivity()).gotoActivity(OfflineMapActivity.class);
                break;

            case 7:
                break;
            case 8:


                break;
            case 9:

                break;
        }
    }

    private void loadMainNav() {

        ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();


        addNewHomeItem(meumList, R.drawable.flag_128, "标注位置");
        addNewHomeItem(meumList, R.drawable.route_128, "线路追踪");
        addNewHomeItem(meumList, R.drawable.rule_128, "区域管理");
        addNewHomeItem(meumList, R.drawable.location_arrow_128, "当前位置");
        addNewHomeItem(meumList, R.drawable.compass_128, "指南针");
        addNewHomeItem(meumList, R.drawable.offlinemap_128, "离线地图");
        addNewHomeItem(meumList, R.drawable.map_download128, "下载数据");
        addNewHomeItem(meumList, R.drawable.map_upload128, "上传数据");
        addNewHomeItem(meumList, R.drawable.settings_128, "系统设置");


        SimpleAdapter saItem = new SimpleAdapter(this.getActivity(),
                meumList, //数据源
                R.layout.home_item, //xml实现
                new String[]{"ItemImage", "ItemText"}, //对应map的Key
                new int[]{R.id.ItemImage, R.id.ItemText});  //对应R的Id

        //添加Item到网格中
        gvMainNav.setAdapter(saItem);
    }


    private void addNewHomeItem(ArrayList<HashMap<String, Object>> meumList, int iconIndex, String title) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ItemImage", iconIndex);
        map.put("ItemText", title);
        meumList.add(map);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public void setGPSSatellites(int maxSatellites, int connnectSatellites) {

        if (txtGPSConnectInfo == null)
            return;
        if (txtConectText == null)
            return;
        if (iconStatus == null)
            return;
        if (iconGPSStatus == null)
            return;
        if (imgGPSConnectSatellitesInfo == null)
            return;
        if (txtGPSConnectSatellitesInfo == null)
            return;
        if (txtGPSFindSatellitesInfo == null)
            return;


        if (connnectSatellites <= 0) {
            txtGPSConnectInfo.setText("正在等待GPS校准 ");
            txtConectText.setText("连接中");
            iconStatus.startRotate(true, AwesomeTextView.AnimationSpeed.SLOW);
            iconGPSStatus.startFlashing(true, AwesomeTextView.AnimationSpeed.MEDIUM);


        } else {
            txtGPSConnectInfo.setText("GPS卫星已连接");
            txtConectText.setText("已连接");
            iconStatus.clearAnimation();
            iconGPSStatus.clearAnimation();

        }


        if (connnectSatellites >= (maxSatellites / 2)) {
            imgGPSConnectSatellitesInfo.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_settings_input_antenna_black_18dp));
        } else {
            imgGPSConnectSatellitesInfo.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_settings_input_antenna_grey600_18dp));
        }

        txtGPSConnectSatellitesInfo.setText(String.format("已校准：%d", connnectSatellites));
        txtGPSFindSatellitesInfo.setText(String.format("发现卫星数目(%d颗)", maxSatellites));
    }



    public void setLocation(GISLocation loc) {


        if (txtlng == null)
            return;
        if (txtlat == null)
            return;
        if (txtaltaccuracy == null)
            return;
        if (txtaddress == null)
            return;

        if (loc == null) {
            txtlng.setText(String.format("经度：%3.6f", 0));
            txtlat.setText(String.format("纬度：%3.6f", 0));
            txtaltaccuracy.setText(String.format("海拔：%2.1f m 精度:±%2.2f (米)", 0.0, 0.0));
            txtaddress.setText("");
        } else {
            txtlng.setText(String.format("经度：%3.6f", NumberUtil.roundNumber(loc.getLongitude(), 6)));
            txtlat.setText(String.format("纬度：%3.6f", NumberUtil.roundNumber(loc.getLatitude(), 6)));
            txtaltaccuracy.setText(String.format("海拔：%2.1f m 精度:±%2.2f (米)", NumberUtil.roundNumber(loc.getAltitude(), 2), NumberUtil.roundNumber(loc.getAccuracy(), 2)));
            txtaddress.setText(loc.getAddress());
        }
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
