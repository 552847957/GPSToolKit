package com.srmn.xwork.androidlib.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.srmn.xwork.androidlib.R;

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


    public static void showConfrim(Context context, String title, String message, int icon, String okBtnName, String canelBtnName, DialogInterface.OnClickListener okClick) {
        Dialog alertDialog = new AlertDialog.Builder(context).
                setTitle(title).
                setMessage(message).
                setIcon(icon).
                setPositiveButton(okBtnName, okClick).
                setNegativeButton(canelBtnName, null).
                create();
        alertDialog.show();
    }


    public static void showShortToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner
     * @param value
     */
    public static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner
     * @param value
     */
    public static <T> void setSpinnerItemSelectedByValue(Spinner spinner, String value, String propertyName, Class<T> type) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {

            try {
                T id = PropertyUtil.getPropertyValue(propertyName, type, apsAdapter.getItem(i));

                if (value.equals(id.toString())) {
                    spinner.setSelection(i, true);// 默认选中项
                    break;
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }


        }
    }
}
