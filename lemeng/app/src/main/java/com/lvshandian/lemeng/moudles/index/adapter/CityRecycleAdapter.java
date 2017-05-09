package com.lvshandian.lemeng.moudles.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.interf.ItemClickListener;

import java.util.List;


/**
 * Created by gjj on 2016/12/20.
 */

public class CityRecycleAdapter extends RecyclerView.Adapter<CityHolder> {

    private Context mContext;
    private List<String> mDatas;
    private final LayoutInflater mInflator;

    public CityRecycleAdapter(Context context, List<String> list) {
        mContext = context;
        mDatas = list;
        mInflator = LayoutInflater.from(mContext);
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflator.inflate(R.layout.item_province_city, null);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, final int position) {
        String city = mDatas.get(position);
        holder.getTvProvince().setText(city);

        holder.getIvImage().setVisibility(View.GONE);

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

class CityHolder extends RecyclerView.ViewHolder {

    public ImageView getIvImage() {
        return ivImage;
    }

    private ImageView ivImage;

    public TextView getTvProvince() {
        return tvProvince;
    }

    private TextView tvProvince;

    public View getItemView() {
        return itemView;
    }

    private View itemView;

    public CityHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        tvProvince = (TextView) itemView.findViewById(R.id.tv_province);

        ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
    }
}
