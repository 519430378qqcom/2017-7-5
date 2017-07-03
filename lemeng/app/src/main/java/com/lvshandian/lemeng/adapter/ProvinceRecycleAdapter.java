package com.lvshandian.lemeng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.entity.ProvinceCity;
import com.lvshandian.lemeng.interfaces.ItemClickListener;

import java.util.List;


/**
 * Created by gjj on 2016/12/20.
 */

public class ProvinceRecycleAdapter extends RecyclerView.Adapter<RecycleHolder> {

    private Context mContext;
    private List<ProvinceCity> mDatas;
    private final LayoutInflater mInflator;

    public ProvinceRecycleAdapter(Context context, List<ProvinceCity> list) {
        mContext = context;
        mDatas = list;
        mInflator = LayoutInflater.from(mContext);
    }

    @Override
    public RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflator.inflate(R.layout.item_province_city, null);
        return new RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, final int position) {
        ProvinceCity city = mDatas.get(position);
        holder.getTvProvince().setText(city.getName());

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }
}

class RecycleHolder extends RecyclerView.ViewHolder {

    public TextView getTvProvince() {
        return tvProvince;
    }

    private TextView tvProvince;

    public View getItemView() {
        return itemView;
    }

    private View itemView;

    public RecycleHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        tvProvince = (TextView) itemView.findViewById(R.id.tv_province);
    }
}

