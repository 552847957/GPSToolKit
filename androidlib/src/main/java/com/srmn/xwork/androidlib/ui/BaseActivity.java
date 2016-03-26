package com.srmn.xwork.androidlib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;


import com.srmn.xwork.androidlib.utils.UIUtil;

import org.xutils.x;


/**
 * Created by kiler on 2016/1/8.
 */
public class BaseActivity extends AppCompatActivity {
    protected AppCompatActivity context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        x.view().inject(this);
    }


    public void showShortToastMessage(String message) {
        UIUtil.showShortToastMessage(context, message);
    }

    public void showLongToastMessage(String message) {
        UIUtil.showLongToastMessage(context, message);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void gotoActivity(Class cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    public void gotoActivity(Intent intent, Class cls) {
        intent.setClass(context, cls);
        startActivity(intent);
    }


    public void gotoActivity(Class cls, int taskType) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(taskType);
        startActivity(intent);
    }

    public void gotoActivityForResult(Class cls, int requestCode) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setActionBarTitle(String actionBarTitle) {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(actionBarTitle);
    }

    public void toggleActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            if (actionBar.isShowing()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }
    }

    public void showActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.show();
    }

    public void hideActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    public void showBackButton() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void hideBackButton() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(false);
    }


}
