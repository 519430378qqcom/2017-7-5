package com.lvshandian.lemeng.moudles.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.moudles.mine.bean.FloatingRecordBean;

import java.util.List;

/**
 * Created by ssb on 2017/6/24 13:33.
 * company: lvshandian
 */

public class FloatingRecordAdapter extends RecyclerView.Adapter<FloatingRecordAdapter.FloatingHolder> {
    Context context;
    List<FloatingRecordBean.DataBean> floatingRecords;

    public FloatingRecordAdapter(Context context, List<FloatingRecordBean.DataBean> floatingRecords) {
        this.context = context;
        this.floatingRecords = floatingRecords;
    }

    @Override
    public FloatingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_float_record_item, parent, false);
        FloatingHolder holder = new FloatingHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FloatingHolder holder, int position) {
        FloatingRecordBean.DataBean floatingRecord = floatingRecords.get(position);
        holder.floating_type.setText(floatingRecord.getState());
        holder.floating_time.setText(floatingRecord.getRefreshTimes());
        holder.floating_number.setText(context.getString(R.string.floating_moneny, floatingRecord.getAmount()));
    }

    @Override
    public int getItemCount() {
        return floatingRecords.size();
    }

    class FloatingHolder extends RecyclerView.ViewHolder {
        TextView floating_type;
        TextView floating_time;
        TextView floating_number;

        public FloatingHolder(View itemView) {
            super(itemView);
            floating_type = (TextView) itemView.findViewById(R.id.floating_type);
            floating_time = (TextView) itemView.findViewById(R.id.floating_time);
            floating_number = (TextView) itemView.findViewById(R.id.floating_number);
        }
    }
}
