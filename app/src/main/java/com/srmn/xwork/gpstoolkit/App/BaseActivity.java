package com.srmn.xwork.gpstoolkit.App;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.srmn.xwork.gpstoolkit.Dao.DaoContainer;
import com.srmn.xwork.gpstoolkit.R;

import butterknife.ButterKnife;

/**
 * Created by kiler on 2016/2/23.
 */
public abstract class BaseActivity extends com.srmn.xwork.androidlib.ui.BaseActivity {


    public DaoContainer getDaos() {
        return MyApplication.getInstance().getDaos();
    }

    public MyApplication getMyApp() {
        return MyApplication.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        ButterKnife.bind(this);
    }

    public int Dp2Px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int Px2Dp(float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    protected abstract int getLayoutID();
}
