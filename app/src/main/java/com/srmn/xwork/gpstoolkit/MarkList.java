package com.srmn.xwork.gpstoolkit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.Gson;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.gis.ShowMarker;
import com.srmn.xwork.androidlib.maps.ShowMap;
import com.srmn.xwork.androidlib.ui.BaseArrayAdapter;
import com.srmn.xwork.androidlib.ui.MutiImageView;
import com.srmn.xwork.androidlib.ui.SingleImageView;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.IOUtil;
import com.srmn.xwork.androidlib.utils.SharedPrefsUtil;
import com.srmn.xwork.androidlib.utils.UIUtil;
import com.srmn.xwork.gpstoolkit.App.BaseActivity;
import com.srmn.xwork.gpstoolkit.App.MyApplication;
import com.srmn.xwork.gpstoolkit.Entities.Marker;
import com.srmn.xwork.gpstoolkit.Entities.MarkerCategory;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_mark_list)
public class MarkList extends BaseActivity {

    @ViewInject(R.id.el_list)
    protected ListView el_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //使用Intent对象得到FirstActivity传递来的参数
        Intent intent = getIntent();

        if (!intent.hasExtra("MarkerGategoryID")) {
            showShortToastMessage("没有ID，无法接收数据。");
            finish();
            return;
        }

        int ID = intent.getIntExtra("MarkerGategoryID", 0);

        putSharedPrefsIntValue("MarkList", "MarkListID", ID);

        loadUIData();

    }

    private void loadUIData() {

        int ID = getSharedPrefsIntValue("MarkList", "MarkListID", 0);

        MarkerCategory markerCategory = this.getDaos().getMarkerCategoryDaoInstance().findById(ID);
        markerCategory.setMarkers(getDaos().getMarkerDaoInstance().queryByCategoryID(markerCategory.getId()));

        el_list.setAdapter(new MyAdapter(context, R.layout.item_location_full, markerCategory.getMarkers()));


    }


    class MyAdapter extends BaseArrayAdapter<Marker> {

        public MyAdapter(Context context, int resource, List<Marker> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Marker marker = (Marker) getItem(position);

            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(layoutID, null);
                viewHolder = new ViewHolder();
                viewHolder.txtDesciption = (TextView) view.findViewById(R.id.txtDesciption);
                viewHolder.txtName = (TextView) view.findViewById(R.id.txtName);
                viewHolder.txtLocation = (TextView) view.findViewById(R.id.txtLocation);
                viewHolder.txtName = (TextView) view.findViewById(R.id.txtName);
                viewHolder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);
                viewHolder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
                viewHolder.btnEdit = (BootstrapButton) view.findViewById(R.id.btnEdit);
                viewHolder.btnDelete = (BootstrapButton) view.findViewById(R.id.btnDelete);
                viewHolder.btnShow = (BootstrapButton) view.findViewById(R.id.btnShow);
                viewHolder.btnImage = (BootstrapButton) view.findViewById(R.id.btnImage);

                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.txtInfo.setText(marker.getInfo());
            viewHolder.txtDesciption.setText(marker.getDescription());
            viewHolder.txtLocation.setText(marker.getLocationInfo());
            viewHolder.txtName.setText(marker.getName() + "");

            View.OnClickListener editHandler = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    //Intent传递参数
                    intent.putExtra("EditMarker", marker);
                    gotoActivity(intent, MarkEditor.class);
//                    Intent intent = new Intent();
//                    //Intent传递参数
//                    intent.putExtra("EditMarker", marker);
//                    gotoActivity(intent, SingleImageView.class);

                }
            };

            viewHolder.ll_item.setOnClickListener(editHandler);
            viewHolder.btnEdit.setOnClickListener(editHandler);

            View.OnClickListener deleteHandler = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UIUtil.showConfrim(context, "确定确认", "确定删除该标注？", R.drawable.ic_info_grey600_18dp, "确定", "取消", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                getDaos().getMarkerDaoInstance().delete(marker);
                                loadUIData();
                            } catch (DbException e) {
                                e.printStackTrace();
                                showShortToastMessage("删除失败：" + e.getMessage());
                            }
                        }
                    });

                }
            };

            viewHolder.btnDelete.setOnClickListener(deleteHandler);


            View.OnClickListener showMapHandler = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<ShowMarker> showMarkers = new ArrayList<>();

                    Gson gson1 = GsonUtil.getGson();

                    ShowMarker showMarker = new ShowMarker();
                    showMarker.setTitle(marker.getName());
                    showMarker.setLat(marker.getLatitude());
                    showMarker.setLng(marker.getLongitude());
                    showMarker.setIconResourseID(R.drawable.poi_marker_pressed);
                    showMarker.setJsonData(gson1.toJson(marker));

                    showMarkers.add(showMarker);

                    Gson gson = GsonUtil.getGson();

                    Intent intent = new Intent();
                    //Intent传递参数
                    intent.putExtra("showMarkers", gson.toJson(showMarkers));
                    gotoActivity(intent, ShowMarkerMap.class);

                }
            };

            viewHolder.btnShow.setOnClickListener(showMapHandler);


            View.OnClickListener showImageHandler = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    //Intent传递参数
                    intent.putExtra("imagePaths", marker.getImages());
                    gotoActivity(intent, MutiImageView.class);

                }
            };

            viewHolder.btnImage.setOnClickListener(showImageHandler);



            return view;
        }

        class ViewHolder {
            public TextView txtName;
            public TextView txtLocation;
            public TextView txtDesciption;
            public TextView txtInfo;
            public LinearLayout ll_item;
            public BootstrapButton btnEdit;
            public BootstrapButton btnDelete;
            public BootstrapButton btnShow;
            public BootstrapButton btnImage;

        }
    }
}