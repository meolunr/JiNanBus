package com.leon.jinanbus.ui.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import com.leon.jinanbus.R;
import com.leon.jinanbus.ui.activity.base.BaseActivity;
import com.leon.jinanbus.ui.fragment.AboutFragment;
import com.leon.jinanbus.ui.fragment.CollectFragment;
import com.leon.jinanbus.ui.fragment.HomeFragment;
import com.leon.jinanbus.ui.fragment.SettingFragment;
import com.leon.jinanbus.utils.SharedPrefUtils;
import com.leon.jinanbus.utils.StatusBarUtils;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int mCurrentIndex = -1;
    private DrawerLayout dlMain;
    private NavigationView navDrawer;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void setStatusBarColor() {
        dlMain = (DrawerLayout) findViewById(R.id.dl_main);
        StatusBarUtils.setColorForDrawerLayout(this, getResources().getColor(R.color.colorPrimary), dlMain);
    }

    @Override
    protected void initView() {
        navDrawer = (NavigationView) findViewById(R.id.nav_drawer);
    }

    @Override
    protected void initListener() {
        navDrawer.setCheckedItem(R.id.menu_home);
        navDrawer.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dlMain, getToolBar(), 0, 0);
        toggle.syncState();
        dlMain.addDrawerListener(toggle);
    }

    @Override
    protected void initData() {
        showAgreementDialog();
        switchFragment(0);
    }

    private void showAgreementDialog() {
        if (SharedPrefUtils.getBoolean(this, "isShowAgreement", true)) {
            new AlertDialog.Builder(this)
                    .setTitle("使用协议")
                    .setMessage(R.string.agreement)
                    .setPositiveButton("确定", null)
                    .setNegativeButton("不再提示", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPrefUtils.putBoolean(MainActivity.this, "isShowAgreement", false);
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.menu_home:
                switchFragment(0);
                break;
            case R.id.menu_collect:
                switchFragment(1);
                break;
            case R.id.menu_setting:
                switchFragment(2);
                break;
            case R.id.menu_about:
                switchFragment(3);
                break;
        }

        dlMain.closeDrawers();

        return true;
    }

    /**
     * 切换Fragment，在ToolBar初始化之后调用
     *
     * @param index 页面角标
     */
    private void switchFragment(int index) {
        if (mCurrentIndex != index) {
            String title = "";
            Fragment fragment = null;

            switch (index) {
                case 0:
                    title = "泉城公交";
                    fragment = new HomeFragment();
                    break;
                case 1:
                    title = "收藏";
                    fragment = new CollectFragment();
                    break;
                case 2:
                    title = "设置";
                    fragment = new SettingFragment();
                    break;
                case 3:
                    title = "关于";
                    fragment = new AboutFragment();
                    break;
            }

            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.fragment_in, R.animator.fragment_out)
                    .replace(R.id.fl_content, fragment)
                    .commit();

            getToolBar().setTitle(title);

            mCurrentIndex = index;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}