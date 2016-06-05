package com.leon.jinanbus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leon.jinanbus.R;
import com.leon.jinanbus.domain.LineList;
import com.leon.jinanbus.ui.interfaces.OnItemClickListener;

import java.util.List;

/**
 * 搜索结果RecycleView的Adapter
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultHolder> {
    private OnItemClickListener mListener;
    private Context mContext;
    private List<LineList.Result> mResults;

    public ResultAdapter(Context context, List<LineList.Result> results) {
        mContext = context;
        mResults = results;
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultHolder(View.inflate(mContext, R.layout.item_search_result, null), mListener);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        final LineList.Result result = mResults.get(position);

        holder.tvBusNum.setText(result.lineName);
        holder.tvOrientation.setText(result.startStationName + " - " + result.endStationName);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}

class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvBusNum;
    TextView tvOrientation;
    private OnItemClickListener mListener;

    public ResultHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        mListener = listener;

        tvBusNum = (TextView) itemView.findViewById(R.id.tv_busNum);
        tvOrientation = (TextView) itemView.findViewById(R.id.tv_orientation);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, getLayoutPosition());
        }
    }
}