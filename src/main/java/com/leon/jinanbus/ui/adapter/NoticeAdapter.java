package com.leon.jinanbus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leon.jinanbus.R;
import com.leon.jinanbus.domain.BusLineNotice;
import com.leon.jinanbus.ui.interfaces.OnItemClickListener;

import java.util.List;

/**
 * 首页公告RecycleView的Adapter
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeHolder> {
    private OnItemClickListener mListener;

    private Context mContext;
    public List<BusLineNotice.Notice> mNotices;

    public NoticeAdapter(Context context, List<BusLineNotice.Notice> results) {
        this.mContext = context;
        this.mNotices = results;
    }

    @Override
    public NoticeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeHolder(View.inflate(mContext, R.layout.item_home_notice, null), mListener);
    }

    @Override
    public void onBindViewHolder(NoticeHolder holder, final int position) {
        final BusLineNotice.Notice notice = mNotices.get(position);

        holder.tvTitle.setText(notice.title);
        holder.tvBrief.setText(notice.detail);
    }

    @Override
    public int getItemCount() {
        return mNotices.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}

class NoticeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvTitle;
    TextView tvBrief;
    private OnItemClickListener mListener;

    public NoticeHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        mListener = listener;

        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvBrief = (TextView) itemView.findViewById(R.id.tv_brief);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, getLayoutPosition());
        }
    }
}