package com.leon.jinanbus.ui.fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.jinanbus.R;

public class AboutFragment extends BaseFragment implements View.OnClickListener {
    private TextView tvAgreement;
    private TextView tvVersion;
    private Button btnAuthor;
    private Button btnUpdate;

    @Override
    protected int getContentView() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initView(View view) {
        tvAgreement = (TextView) view.findViewById(R.id.tv_Agreement);
        tvVersion = (TextView) view.findViewById(R.id.tv_version);
        btnAuthor = (Button) view.findViewById(R.id.btn_athor);
        btnUpdate = (Button) view.findViewById(R.id.btn_update);
    }

    @Override
    protected void initListener() {
        tvAgreement.setOnClickListener(this);
        btnAuthor.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

            tvVersion.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_Agreement:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("使用协议");
                builder.setMessage(R.string.agreement);
                builder.setPositiveButton("确定", null);
                builder.show();
                break;
            case R.id.btn_athor:
                new AlertDialog.Builder(getActivity())
                        .setTitle("联系作者")
                        .setItems(new String[]{"微博：@Leon连续", "邮箱：Leon0301@foxmail.com"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://weibo.com/SkyTrous")));
                                        break;
                                    case 1:
                                        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        manager.setPrimaryClip(ClipData.newPlainText(null, "Leon0301@foxmail.com"));
                                        Toast.makeText(getActivity(), "邮箱地址已复制", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.btn_update:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://pan.baidu.com/s/1pLQWFGZ")));
                break;
        }
    }
}