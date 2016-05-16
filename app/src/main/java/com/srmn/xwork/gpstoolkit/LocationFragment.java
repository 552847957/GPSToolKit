package com.srmn.xwork.gpstoolkit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.Gson;
import com.srmn.xwork.androidlib.gis.ShowMarker;
import com.srmn.xwork.androidlib.maps.ShowMap;
import com.srmn.xwork.androidlib.ui.BaseArrayAdapter;
import com.srmn.xwork.androidlib.utils.ClipboardUtil;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.IOUtil;
import com.srmn.xwork.androidlib.utils.NumberUtil;
import com.srmn.xwork.gpstoolkit.App.BaseActivity;
import com.srmn.xwork.gpstoolkit.App.BaseFragment;
import com.srmn.xwork.gpstoolkit.App.MyApplication;
import com.srmn.xwork.gpstoolkit.Entities.Marker;
import com.srmn.xwork.gpstoolkit.Entities.MarkerCategory;

import org.apache.commons.io.FileUtils;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_location)
public class LocationFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @ViewInject(R.id.el_list)
    protected ListView el_list;
    protected List<MarkerCategory> parentData = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        el_list.setAdapter(new MyAdapter(getContext(), R.layout.item_category_full, parentData));
    }

    private void initData() {

        parentData = getDaos().getMarkerCategoryDaoInstance().findAllFullMarkerCategory();

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class MyAdapter extends BaseArrayAdapter<MarkerCategory> {

        public MyAdapter(Context context, int resource, List<MarkerCategory> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {

            final MarkerCategory markerCategory = (MarkerCategory) getItem(position);

            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(layoutID, null);
                viewHolder = new ViewHolder();
                viewHolder.txtDesciption = (TextView) view.findViewById(R.id.txtDesciption);
                viewHolder.txtName = (TextView) view.findViewById(R.id.txtName);
                viewHolder.btnPublish = (BootstrapButton) view.findViewById(R.id.btnPublish);
                viewHolder.btnExport = (BootstrapButton) view.findViewById(R.id.btnExport);
                viewHolder.btnMap = (BootstrapButton) view.findViewById(R.id.btnMap);
                viewHolder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.txtDesciption.setText(markerCategory.getDescription());
            viewHolder.txtName.setText(markerCategory.getName() + "");

            viewHolder.btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ShowMarker> showMarkers = new ArrayList<>();

                    for (Marker marker : markerCategory.getMarkers()) {
                        Gson gson1 = GsonUtil.getGson();
                        ShowMarker showMarker = new ShowMarker();
                        showMarker.setTitle(marker.getName());
                        showMarker.setLat(marker.getLatitude());
                        showMarker.setLng(marker.getLongitude());
                        showMarker.setIconResourseID(R.drawable.poi_marker_pressed);
                        showMarker.setJsonData(gson1.toJson(marker));
                        showMarkers.add(showMarker);
                    }


                    Gson gson = GsonUtil.getGson();

                    Intent intent = new Intent();
                    //Intent传递参数
                    intent.putExtra("showMarkers", gson.toJson(showMarkers));
                    ((BaseActivity) getActivity()).gotoActivity(intent, ShowMarkerMap.class);
                }
            });


            viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    //Intent传递参数
                    intent.putExtra("MarkerGategoryID", markerCategory.getId());

                    ((BaseActivity) getActivity()).gotoActivity(intent, MarkList.class);
                }
            });


            viewHolder.btnExport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String baseDir = IOUtil.getDiskCacheDir(context) + "/export_data/" + markerCategory.getName().replace(" ", "") + "/";

                    File file = new File(baseDir);

                    if (file.exists()) {
                        try {
                            FileUtils.deleteDirectory(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    file.mkdirs();

                    String csvFilePath = baseDir + "datafile.csv";

                    File csvFile = new File(csvFilePath);

                    if (csvFile.exists())
                        csvFile.delete();

                    StringBuilder sbcsv = new StringBuilder();
                    //生成列头
                    sbcsv.append("\"name\",\"address\",\"x\",\"y\",\"no\"\r\n");

                    int number = 0;

                    for (Marker marker : markerCategory.getMarkers()) {
                        number++;
                        sbcsv.append("\"" + marker.getName() + "\",\"" + marker.getDescription() + "\",\"" + marker.getLongitude() + "\",\"" + marker.getLatitude() + "\",\"" + NumberUtil.GetCode(number, 3) + "\"\r\n");
                    }

                    try {
                        IOUtil.writeTxtToFile(sbcsv.toString(), csvFilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    int i = 0;
                    for (Marker marker : markerCategory.getMarkers()) {
                        i++;
                        String baseFileFormat = baseDir + NumberUtil.GetCode(i, 3) + "_%s_%s";
                        int j = 0;
                        for (String filePath : marker.getImagesList()) {
                            j++;
                            File copyfile = new File(filePath);

                            if (!copyfile.exists())
                                continue;
                            String copyToFileName = String.format(baseFileFormat, j, copyfile.getName());

                            if (!copyToFileName.toLowerCase().endsWith(".png")) {
                                copyToFileName += ".png";
                            }
                            try {
                                FileUtils.copyFile(copyfile, new File(copyToFileName));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    MyApplication.getInstance().showLongToastMessage("数据成功导出到目录" + baseDir);

                    ClipboardUtil.copyTextToClipboard(context, "GPS工具箱导出路径", baseDir);

                }
            });

            return view;
        }

        class ViewHolder {
            public TextView txtName;
            public TextView txtDesciption;
            public BootstrapButton btnPublish;
            public BootstrapButton btnExport;
            public BootstrapButton btnMap;
            public LinearLayout ll_item;
        }
    }
}