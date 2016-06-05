package com.leon.jinanbus.ui.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.leon.jinanbus.R;
import com.leon.jinanbus.utils.StatusBarUtils;

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        setStatusBarColor();
        initToolBar();
        initView();
        initListener();
        initData();
    }

    protected void setStatusBarColor() {
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    protected void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle(R.string.app_name);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected final Toolbar getToolBar() {
        return mToolBar;
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();
}