package com.leon.jinanbus.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.leon.jinanbus.R;
import com.leon.jinanbus.ui.activity.base.BaseNoToolBarActivity;

public class SearchActivity extends BaseNoToolBarActivity {
    private EditText etKeyword;
    private ImageButton ibClear;

    @Override
    protected int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        ibClear = (ImageButton) findViewById(R.id.ib_clear);
        etKeyword = (EditText) findViewById(R.id.et_keyword);
    }

    @Override
    protected void initListener() {
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                ibClear.setVisibility(editable.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                ibSearch(null);

                return true;
            }
        });
    }

    @Override
    protected void initData() {
        String keyword = getIntent().getStringExtra("keyword");

        if (!TextUtils.isEmpty(keyword)) {
            etKeyword.setText(keyword);
            etKeyword.setSelection(keyword.length());
        }
    }

    public void flOther(View view) {
        onBackPressed();
    }

    public void ibBack(View view) {
        onBackPressed();
    }

    public void ibClear(View view) {
        etKeyword.setText("");
    }

    public void ibSearch(View view) {
        String keyword = etKeyword.getText().toString();

        if (!TextUtils.isEmpty(keyword)) {
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra("keyword", keyword);
            startActivity(intent);
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void setStatusBarColor() {
    }
}