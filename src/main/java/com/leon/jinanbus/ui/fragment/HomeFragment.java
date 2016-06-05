package com.leon.jinanbus.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leon.jinanbus.R;
import com.leon.jinanbus.domain.BusLineNotice;
import com.leon.jinanbus.net.BusApi;
import com.leon.jinanbus.ui.activity.NoticeActivity;
import com.leon.jinanbus.ui.adapter.NoticeAdapter;
import com.leon.jinanbus.ui.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class HomeFragment extends BaseFragment {
    private TextView tvLoading;
    private ProgressBar pbLoading;

    private RecyclerView rvNotcie;
    private SwipeRefreshLayout slSwipe;
    private List<BusLineNotice.Notice> mNotices;
    private NoticeAdapter mAdapter;

    private boolean mIsLoading;

    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        tvLoading = (TextView) view.findViewById(R.id.tv_loading);
        pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);

        rvNotcie = (RecyclerView) view.findViewById(R.id.rv_notice);
        slSwipe = (SwipeRefreshLayout) view.findViewById(R.id.sl_swipe);
    }

    @Override
    protected void initListener() {
        slSwipe.setColorSchemeResources(R.color.colorPrimary);
        slSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mIsLoading) {
                    BusApi.getInstance().getBusLineNotice(0, 20, new Subscriber<BusLineNotice>() {
                        @Override
                        public void onStart() {
                            mIsLoading = true;
                        }

                        @Override
                        public void onCompleted() {
                            mIsLoading = false;
                            mAdapter.notifyDataSetChanged();
                            slSwipe.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mIsLoading = false;
                            slSwipe.setRefreshing(false);
                        }

                        @Override
                        public void onNext(BusLineNotice busLineNotice) {
                            mNotices.clear();
                            mNotices.addAll(busLineNotice.results);
                        }
                    });
                }
            }
        });

        rvNotcie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotcie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastPosition = ((LinearLayoutManager) rvNotcie.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                int total = recyclerView.getAdapter().getItemCount();

                if (lastPosition == total - 1 && !mIsLoading) {
                    BusApi.getInstance().getBusLineNotice(total, 20, new Subscriber<BusLineNotice>() {
                        @Override
                        public void onStart() {
                            mIsLoading = true;
                        }

                        @Override
                        public void onCompleted() {
                            mIsLoading = false;
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mIsLoading = false;
                        }

                        @Override
                        public void onNext(BusLineNotice busLineNotice) {
                            mNotices.addAll(busLineNotice.results);
                        }
                    });
                }
            }
        });

        tvLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLoading.setVisibility(View.GONE);
                loadNewNotice();
            }
        });
    }

    @Override
    protected void initData() {
        mNotices = new ArrayList<>();
        mAdapter = new NoticeAdapter(getActivity(), mNotices);
        rvNotcie.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                BusLineNotice.Notice notice = mNotices.get(position);

                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                intent.putExtra("title", notice.title);
                intent.putExtra("detail", notice.detail);

                startActivity(intent);
            }
        });

        loadNewNotice();
    }

    private void loadNewNotice() {
        if (!mIsLoading) {
            BusApi.getInstance().getBusLineNotice(0, 20, new Subscriber<BusLineNotice>() {
                @Override
                public void onStart() {
                    mIsLoading = true;
                    pbLoading.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCompleted() {
                    mIsLoading = false;
                    pbLoading.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    mIsLoading = false;
                    tvLoading.setText("哎呀..什么都没有ค(TㅅT)");
                    tvLoading.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onNext(BusLineNotice busLineNotice) {
                    mNotices.addAll(busLineNotice.results);
                }
            });
        }
    }
}