package com.srmn.xwork.gpstoolkit.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srmn.xwork.gpstoolkit.Dao.DaoContainer;
import com.srmn.xwork.gpstoolkit.R;

import org.xutils.x;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiler on 2016/2/26.
 */
public class BaseFragment extends com.srmn.xwork.androidlib.ui.BaseFragment {

    protected Unbinder unbinder;



    public DaoContainer getDaos() {
        return MyApplication.getInstance().getDaos();
    }

    public MyApplication getMyApplication() {
        return MyApplication.getInstance();
    }


}
