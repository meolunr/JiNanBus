package com.leon.jinanbus.ui.activity;

import android.view.MenuItem;
import android.webkit.WebView;

import com.leon.jinanbus.R;
import com.leon.jinanbus.ui.activity.base.BaseActivity;

/**
 * 通知详情Activity
 */
public class NoticeActivity extends BaseActivity {
    private WebView mWebView;

    @Override
    protected int getContentView() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        getToolBar().setTitle("公告详情");

        String title = getIntent().getStringExtra("title");
        String detail = getIntent().getStringExtra("detail");

        String replace = detail.replace("\n", "</p><p>");
        String html = "<h3>" + title + "</h3><p>" + replace + "</p>";

        mWebView.loadDataWithBaseURL("about:blank", html, "text/html", "UTF-8", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}