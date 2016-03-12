package com.srmn.xwork.gpstoolkit.App;

import com.srmn.xwork.gpstoolkit.Dao.DaoContainer;

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

}
