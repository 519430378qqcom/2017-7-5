package com.lvshandian.lemeng.adapter.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.entity.mine.WithdrawRecordBean;

import java.util.List;

/**
 * Created by ssb on 2017/6/24 13:33.
 * company: lvshandian
 */

public class WithdrawRecordAdapter extends RecyclerView.Adapter<WithdrawRecordAdapter.FloatingHolder> {
    Context context;
    List<WithdrawRecordBean.RowsBean> rowsList;

    public WithdrawRecordAdapter(Context context, List<WithdrawRecordBean.RowsBean> rowsBeen) {
        this.context = context;
        this.rowsList = rowsBeen;
    }

    @Override
    public FloatingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.withdraw_record_item, parent, false);
        FloatingHolder holder = new FloatingHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FloatingHolder holder, int position) {
        WithdrawRecordBean.RowsBean rowsBean = rowsList.get(position);
        holder.floating_number.setText(context.getString(R.string.floating_moneny, String.valueOf(rowsBean.getAmount())));
        holder.floating_time.setText(rowsBean.getCreatedTime());
    }

    @Override
    public int getItemCount() {
        return rowsList.size();
    }

    class FloatingHolder extends RecyclerView.ViewHolder {
        TextView floating_number;
        TextView floating_time;

        public FloatingHolder(View itemView) {
            super(itemView);
            floating_number = (TextView) itemView.findViewById(R.id.floating_number);
            floating_time = (TextView) itemView.findViewById(R.id.floating_time);
        }
    }
}
