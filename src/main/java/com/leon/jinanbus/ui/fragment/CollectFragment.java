package com.leon.jinanbus.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.leon.jinanbus.R;
import com.leon.jinanbus.db.CollectDao;
import com.leon.jinanbus.domain.CollectItem;
import com.leon.jinanbus.ui.activity.BusLineDetailActivity;
import com.leon.jinanbus.ui.adapter.CollectAdapter;
import com.leon.jinanbus.ui.interfaces.OnItemClickListener;
import com.leon.jinanbus.ui.interfaces.OnItemLongClickListener;
import com.leon.jinanbus.ui.widget.ItemDivider;

import java.util.List;

public class CollectFragment extends BaseFragment {
    private RecyclerView rvCollect;
    private List<CollectItem> mCollects;

    @Override
    protected int getContentView() {
        return R.layout.fragment_collect;
    }

    @Override
    protected void initView(View view) {
        rvCollect = (RecyclerView) view.findViewById(R.id.rv_collect);
    }

    @Override
    protected void initListener() {
        rvCollect.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCollect.addItemDecoration(new ItemDivider(getActivity(), ItemDivider.VERTICAL));
    }

    @Override
    protected void initData() {
        mCollects = CollectDao.getInstance(getActivity()).queryAll();

        final CollectAdapter adapter = new CollectAdapter(getActivity(), mCollects);
        rvCollect.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                CollectItem item = mCollects.get(position);

                Intent intent = new Intent(getActivity(), BusLineDetailActivity.class);
                intent.putExtra("id", item.id);
                intent.putExtra("lineName", item.busNum);

                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, final int position) {
                final CollectItem item = mCollects.get(position);

                new AlertDialog.Builder(getActivity())
                        .setMessage("确定要删除吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CollectDao.getInstance(getActivity()).delete(item.id);
                                mCollects.remove(position);

                                adapter.notifyItemRemoved(position);
                            }
                        })
                        .show();
            }
        });
    }
}