package com.srmn.xwork.gpstoolkit;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Marker;
import com.google.gson.Gson;
import com.srmn.xwork.androidlib.gis.ShowMarker;
import com.srmn.xwork.androidlib.maps.ShowMap;
import com.srmn.xwork.androidlib.ui.MutiImageView;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;

/**
 * Created by kiler on 2016/5/17.
 */
public class ShowMarkerMap extends ShowMap {

    @Override
    public int getInfoWindowView() {
        return 0;
        //return R.layout.marker_info;
    }

    @Override
    public void render(Marker marker, View view) {

        if (marker == null)
            return;

        if (view == null)
            return;


        if (marker.getObject() == null)
            return;
        Gson gson = GsonUtil.getGson();
        ShowMarker showMarker = gson.fromJson((String) marker.getObject(), ShowMarker.class);

        String markerJson = showMarker.getJsonData();

        if (StringUtil.isNullOrEmpty(markerJson))
            return;

        Gson gson2 = GsonUtil.getGson();
        final com.srmn.xwork.gpstoolkit.Entities.Marker markerdata = gson.fromJson(markerJson, com.srmn.xwork.gpstoolkit.Entities.Marker.class);

        if (markerdata == null)
            return;


        TextView txtName = ((TextView) view.findViewById(R.id.txtName));
        TextView txtLocation = ((TextView) view.findViewById(R.id.txtLocation));
        TextView txtDesciption = ((TextView) view.findViewById(R.id.txtDesciption));
        TextView txtInfo = ((TextView) view.findViewById(R.id.txtInfo));
        LinearLayout ll_ViewImages = ((LinearLayout) view.findViewById(R.id.ll_ViewImages));
        LinearLayout ll_Edit = ((LinearLayout) view.findViewById(R.id.ll_Edit));

        txtName.setText(markerdata.getName());
        txtLocation.setText(markerdata.getLocationInfo());
        txtDesciption.setText(markerdata.getDescription());
        txtInfo.setText(markerdata.getInfo());

        ll_ViewImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //Intent传递参数
                intent.putExtra("imagePaths", markerdata.getImages());
                gotoActivity(intent, MutiImageView.class);
            }
        });

        ll_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //Intent传递参数
                intent.putExtra("EditMarker", markerdata);
                gotoActivity(intent, MarkEditor.class);
            }
        });

    }


}
