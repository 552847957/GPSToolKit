package com.srmn.xwork.gpstoolkit.App;

import com.srmn.xwork.gpstoolkit.Dao.DaoContainer;

/**
 * Created by kiler on 2016/2/23.
 */
public class BaseActivity extends com.srmn.xwork.androidlib.ui.BaseActivity {


    public DaoContainer getDaos() {
        return MyApplication.getInstance().getDaos();
    }

    public MyApplication getMyApp() {
        return MyApplication.getInstance();
    }


    public int Dp2Px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int Px2Dp(float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
