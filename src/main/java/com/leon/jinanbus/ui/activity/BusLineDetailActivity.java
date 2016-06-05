package com.leon.jinanbus.ui.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.leon.jinanbus.R;
import com.leon.jinanbus.db.CollectDao;
import com.leon.jinanbus.domain.BusInfo;
import com.leon.jinanbus.domain.BusLine;
import com.leon.jinanbus.net.BusApi;
import com.leon.jinanbus.ui.activity.base.BaseActivity;
import com.leon.jinanbus.ui.adapter.BusListAdapter;
import com.leon.jinanbus.ui.adapter.BusPagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;
import rx.functions.Action1;

public class BusLineDetailActivity extends BaseActivity {
    private TextView tvOrientation;
    private TextView tvFare;
    private TextView tvTime;

    private FloatingActionButton mFab;
    private TabLayout mTab;
    private AppBarLayout mAppBar;
    private ViewPager mPager;
    private BusPagerAdapter mAdapter;
    private String mId;
    private Timer mTimer;
    private boolean mIsTimerRuning;
    private int mRefreshSpeed;

    @Override
    protected int getContentView() {
        return R.layout.activity_busline_detail;
    }

    @Override
    protected void initView() {
        tvOrientation = (TextView) findViewById(R.id.tv_orientation);
        tvFare = (TextView) findViewById(R.id.tv_fare);
        tvTime = (TextView) findViewById(R.id.tv_time);

        mAppBar = (AppBarLayout) findViewById(R.id.app_bar);
        mTab = (TabLayout) findViewById(R.id.tab);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void initListener() {
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < 0 - appBarLayout.getTotalScrollRange() + 168) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = ProgressDialog.show(BusLineDetailActivity.this, null, "方向切换中", false, false);

                BusApi.getInstance().switchOrientation(mId, new Subscriber<BusLine>() {
                    @Override
                    public void onCompleted() {
                        updateCollectMenu(getToolBar().getMenu());
                        initTimer();

                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(BusLine busLine) {
                        loadNewBusLine(busLine);
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
        getToolBar().setTitle(getIntent().getStringExtra("lineName"));
        mId = getIntent().getStringExtra("id");

        mAdapter = new BusPagerAdapter(this);
        mPager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mPager);

        SharedPreferences pref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        mRefreshSpeed = Integer.parseInt(pref.getString("refresh_speed", "14"));
        mAdapter.busLineView.setShowBusId(pref.getBoolean("show_bus_id", false));

        BusApi.getInstance().getBusLine(mId, new Subscriber<BusLine>() {
            @Override
            public void onCompleted() {
                initTimer();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BusLine busLine) {
                loadNewBusLine(busLine);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mIsTimerRuning) {
            initTimer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mTimer != null) {
            mIsTimerRuning = false;
            mTimer.cancel();
        }
    }

    private void initTimer() {
        mIsTimerRuning = true;

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 每隔指定时间刷新车辆数据
                BusApi.getInstance().getBusInfo(mId, new Action1<BusInfo>() {
                    @Override
                    public void call(BusInfo busInfo) {
                        mAdapter.busLineView.setBusInfo(busInfo);

                        if (busInfo.result != null) {
                            mAdapter.rvBus.setAdapter(new BusListAdapter(BusLineDetailActivity.this,
                                    busInfo.result));
                        }
                    }
                });
            }
        }, 0, mRefreshSpeed * 1000);
    }

    private void loadNewBusLine(BusLine busLine) {
        BusLine.Result busLineInfo = busLine.result;

        mId = busLineInfo.id;

        tvOrientation.setText(busLineInfo.startStationName + " - " + busLineInfo.endStationName);
        tvFare.setText(busLineInfo.ticketPrice);
        tvTime.setText(busLineInfo.operationTime);

        mAdapter.busLineView.setBusLine(busLine);
    }

    /**
     * 设置收藏按钮状态
     */
    private void updateCollectMenu(Menu menu) {
        boolean exist = CollectDao.getInstance(this).query(mId);
        menu.findItem(R.id.action_collect)
                .setIcon(exist ? R.drawable.ic_busline_collect : R.drawable.ic_busline_no_collect);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bus_detail, menu);
        updateCollectMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_collect:
                if (CollectDao.getInstance(this).query(mId)) {
                    CollectDao.getInstance(this).delete(mId);
                    item.setIcon(R.drawable.ic_busline_no_collect);
                } else {
                    CollectDao.getInstance(this).add(mId, getToolBar().getTitle() + "", tvOrientation.getText() + "");
                    item.setIcon(R.drawable.ic_busline_collect);
                    Snackbar.make(mTab, "收藏成功", Snackbar.LENGTH_SHORT).show();
                }

                break;
        }

        return true;
    }
}