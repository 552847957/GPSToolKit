package com.srmn.xwork.gpstoolkit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.srmn.xwork.androidlib.ui.BaseArrayAdapter;
import com.srmn.xwork.androidlib.utils.DateTimeUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.gpstoolkit.App.BaseFragment;
import com.srmn.xwork.gpstoolkit.Entities.MarkerCategory;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackerFragment extends BaseFragment {


    @BindView(R.id.el_list)
    protected ListView el_list;
    protected List<RouterPath> dataItems;
    protected RouterPathAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public TrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackerFragment newInstance(String param1, String param2) {
        TrackerFragment fragment = new TrackerFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {

        dataItems = getDaos().getRouterPathDaoInstance().findAll();

        if (dataItems == null) {
            dataItems = new ArrayList<RouterPath>();
        }

        adapter = new RouterPathAdapter(getContext(), R.layout.item_route_path, dataItems);
        el_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        unbinder = ButterKnife.bind(this, view);
        // TODO Use fields...
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class RouterPathAdapter extends BaseArrayAdapter<RouterPath> {

        public RouterPathAdapter(Context context, int resource, List<RouterPath> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final RouterPath obj = (RouterPath) getItem(position);

            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(layoutID, null);
                viewHolder = new ViewHolder(view);
//                viewHolder.txtName = (TextView) view.findViewById(R.id.txtName);
//                viewHolder.txtDateTime = (TextView) view.findViewById(R.id.txtDateTime);
//                viewHolder.txtPointCount = (TextView) view.findViewById(R.id.txtPointCount);
//                viewHolder.txtTime = (TextView) view.findViewById(R.id.txtTime);
//                viewHolder.txtLength = (TextView) view.findViewById(R.id.txtLength);
//                viewHolder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);

                viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("route", obj);
                        ((Main) getActivity()).gotoActivity(intent, TrackerMapShow.class);
                    }
                });

                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }


            viewHolder.txtName.setText("编号：" + obj.getCode() + "");
            viewHolder.txtDateTime.setText(DateTimeUtil.FormatDate(obj.getStartTime()));
            viewHolder.txtPointCount.setText(obj.getPointCount() + "");
            viewHolder.txtTime.setText(StringUtil.formatTime(obj.getTimeCount()) + "");
            viewHolder.txtLength.setText(StringUtil.formatDistance(obj.getDistance()) + "");

            return view;
        }

        class ViewHolder {
            @BindView(R.id.txtName)
            public TextView txtName;
            @BindView(R.id.txtDateTime)
            public TextView txtDateTime;
            @BindView(R.id.txtPointCount)
            public TextView txtPointCount;
            @BindView(R.id.txtTime)
            public TextView txtTime;
            @BindView(R.id.txtLength)
            public TextView txtLength;
            @BindView(R.id.ll_item)
            public LinearLayout ll_item;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
