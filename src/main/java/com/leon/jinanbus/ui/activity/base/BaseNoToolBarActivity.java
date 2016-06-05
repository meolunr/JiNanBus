package com.leon.jinanbus.ui.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.leon.jinanbus.R;
import com.leon.jinanbus.utils.StatusBarUtils;

public abstract class BaseNoToolBarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        setStatusBarColor();
        initView();
        initListener();
        initData();
    }

    protected void setStatusBarColor() {
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();
}
