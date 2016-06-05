package com.leon.jinanbus.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leon.jinanbus.R;
import com.leon.jinanbus.domain.LineList;
import com.leon.jinanbus.net.BusApi;
import com.leon.jinanbus.ui.activity.base.BaseNoToolBarActivity;
import com.leon.jinanbus.ui.adapter.ResultAdapter;
import com.leon.jinanbus.ui.interfaces.OnItemClickListener;
import com.leon.jinanbus.ui.widget.ItemDivider;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class SearchResultActivity extends BaseNoToolBarActivity {
    private TextView tvLoading;
    private ProgressBar pbLoading;

    private TextView tvKeyword;
    private RecyclerView rvResult;
    private LinearLayout llSearchBox;
    private List<LineList.Result> mResults;
    private ResultAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initView() {
        tvLoading = (TextView) findViewById(R.id.tv_loading);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        tvKeyword = (TextView) findViewById(R.id.tv_keyword);
        rvResult = (RecyclerView) findViewById(R.id.rv_result);
        llSearchBox = (LinearLayout) findViewById(R.id.ll_search_box);
    }

    @Override
    protected void initListener() {
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        rvResult.addItemDecoration(new ItemDivider(this, ItemDivider.VERTICAL));

        llSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResultActivity.this, SearchActivity.class);
                intent.putExtra("keyword", tvKeyword.getText());

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    protected void initData() {
        mResults = new ArrayList<>();
        mAdapter = new ResultAdapter(SearchResultActivity.this, mResults);
        rvResult.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                LineList.Result result = mResults.get(position);

                Intent intent = new Intent(SearchResultActivity.this, BusLineDetailActivity.class);
                intent.putExtra("id", result.id);
                intent.putExtra("lineName", result.lineName);

                startActivity(intent);
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tvLoading.setVisibility(View.GONE);

        mResults.clear();
        mAdapter.notifyDataSetChanged();

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String keyword = intent.getStringExtra("keyword");
        tvKeyword.setText(keyword);
        BusApi.getInstance().queryBusNum(keyword, new Subscriber<LineList>() {
            @Override
            public void onStart() {
                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCompleted() {
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                tvLoading.setText("什么也没有找到~");
                tvLoading.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onNext(LineList lineList) {
                rvResult.setVisibility(View.VISIBLE);

                mResults.addAll(lineList.result);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void ibBack(View view) {
        finish();
    }

    @Override
    protected void setStatusBarColor() {
    }
}