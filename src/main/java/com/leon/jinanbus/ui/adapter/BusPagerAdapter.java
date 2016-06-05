package com.leon.jinanbus.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.leon.jinanbus.R;
import com.leon.jinanbus.ui.widget.BusLineView;
import com.leon.jinanbus.ui.widget.ItemDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆详情界面ViewPager的Adapter
 */
public class BusPagerAdapter extends PagerAdapter {
    public BusLineView busLineView;
    public RecyclerView rvBus;

    private Context mContext;
    private List<View> mViews = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    public BusPagerAdapter(Context context) {
        this.mContext = context;

        initPageView();
        rvBus.setLayoutManager(new LinearLayoutManager(mContext));
        rvBus.addItemDecoration(new ItemDivider(mContext, ItemDivider.VERTICAL));

        mTitles.add("线路地图");
        mTitles.add("车辆列表");
    }

    private void initPageView() {
        View mapView = View.inflate(mContext, R.layout.pager_busline_map, null);
        busLineView = (BusLineView) mapView.findViewById(R.id.busline);

        View listView = View.inflate(mContext, R.layout.pager_busline_list, null);
        rvBus = (RecyclerView) listView.findViewById(R.id.rv_bus);

        mViews.add(mapView);
        mViews.add(listView);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));

        return mViews.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
