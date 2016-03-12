package com.srmn.xwork.androidlib.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

/**
 * Created by kiler on 2016/2/25.
 */
public class UIUtil {


    public static void setListViewHeigth(Adapter adapter, ListView lst) {
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, lst);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lst.getLayoutParams();
        params.height = totalHeight + (lst.getDividerHeight() * (lst.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        lst.setLayoutParams(params);
    }


//    public static void setRecyclerViewHeigth(RecyclerView.Adapter adapter ,RecyclerView lst)
//    {
//        int totalHeight = 0;
//        for (int i = 0; i < adapter.getItemCount(); i++) {
//            View listItem = adapter.view
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = lst.getLayoutParams();
//        params.height = totalHeight + (adapter.getItemHeight() * (lst.getChildCount() - 1));
//        ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
//        lst.setLayoutParams(params);
//    }
}
