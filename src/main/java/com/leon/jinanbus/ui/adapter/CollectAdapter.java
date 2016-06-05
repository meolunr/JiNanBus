package com.leon.jinanbus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leon.jinanbus.R;
import com.leon.jinanbus.domain.CollectItem;
import com.leon.jinanbus.ui.interfaces.OnItemClickListener;
import com.leon.jinanbus.ui.interfaces.OnItemLongClickListener;

import java.util.List;

/**
 * 收藏RecycleView的Adapter
 */
public class CollectAdapter extends RecyclerView.Adapter<CollectHolder> {
    public List<CollectItem> mItems;

    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;

    private Context mContext;

    public CollectAdapter(Context context, List<CollectItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public CollectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectHolder(View.inflate(mContext, R.layout.item_search_result, null),
                mClickListener, mLongClickListener);
    }

    @Override
    public void onBindViewHolder(final CollectHolder holder, int position) {
        final CollectItem item = mItems.get(position);

        holder.tvBusNum.setText(item.busNum);
        holder.tvOrientation.setText(item.orientation);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }
}

class CollectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    TextView tvBusNum;
    TextView tvOrientation;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;

    public CollectHolder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        super(itemView);
        mClickListener = clickListener;
        mLongClickListener = longClickListener;

        tvBusNum = (TextView) itemView.findViewById(R.id.tv_busNum);
        tvOrientation = (TextView) itemView.findViewById(R.id.tv_orientation);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onItemClick(v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mLongClickListener != null) {
            mLongClickListener.onItemLongClick(v, getLayoutPosition());
        }

        return true;
    }
}