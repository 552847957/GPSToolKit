package com.srmn.xwork.gpstoolkit.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srmn.xwork.gpstoolkit.Dao.DaoContainer;

import org.xutils.x;

/**
 * Created by kiler on 2016/2/26.
 */
public class BaseFragment extends com.srmn.xwork.androidlib.ui.BaseFragment {

    public DaoContainer getDaos() {
        return MyApplication.getInstance().getDaos();
    }

    public MyApplication getMyApplication() {
        return MyApplication.getInstance();
    }

    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

}
