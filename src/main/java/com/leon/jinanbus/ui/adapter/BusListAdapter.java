package com.leon.jinanbus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leon.jinanbus.R;
import com.leon.jinanbus.domain.BusInfo;

import java.util.List;

/**
 * 详情界面车辆列表的Adapter
 */
public class BusListAdapter extends RecyclerView.Adapter<BusListHolder> {
    public List<BusInfo.Result> mBusInfos;
    private Context mContext;

    public BusListAdapter(Context context, List<BusInfo.Result> busInfos) {
        this.mContext = context;
        this.mBusInfos = busInfos;
    }

    @Override
    public BusListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BusListHolder(View.inflate(mContext, R.layout.item_bus_list, null));
    }

    @Override
    public void onBindViewHolder(BusListHolder holder, int position) {
        BusInfo.Result bus = mBusInfos.get(position);

        holder.tvNumber.setText("车辆编号：" + bus.busId);
        holder.tvCardId.setText("车牌号：" + bus.cardId);
        holder.tvVelocity.setText("速度：" + bus.velocity);
    }

    @Override
    public int getItemCount() {
        return mBusInfos.size();
    }
}

class BusListHolder extends RecyclerView.ViewHolder {
    TextView tvNumber;
    TextView tvCardId;
    TextView tvVelocity;

    public BusListHolder(View itemView) {
        super(itemView);

        tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
        tvCardId = (TextView) itemView.findViewById(R.id.tv_card_id);
        tvVelocity = (TextView) itemView.findViewById(R.id.tv_velocity);
    }
}
