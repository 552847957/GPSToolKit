package com.srmn.xwork.androidlib.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiler on 2016/2/21.
 */
public abstract class BaseArrayAdapter<A> extends BaseAdapter {

    protected final Context context;
    protected List<A> data = new ArrayList<A>();
    protected LayoutInflater inflater;
    protected int layoutID;


    public BaseArrayAdapter(Context context, int resource, List<A> objects) {
        super();
        this.data = objects;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.layoutID = resource;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}