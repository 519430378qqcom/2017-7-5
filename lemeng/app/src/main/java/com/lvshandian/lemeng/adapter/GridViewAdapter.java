package com.lvshandian.lemeng.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.bean.GiftBean;
import com.lvshandian.lemeng.widget.view.LoadUrlImageView;


import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class GridViewAdapter extends BaseAdapter {
    private List<GiftBean> giftList;
    private Context context;

    public GridViewAdapter(List<GiftBean> giftList, Context context) {
        this.giftList = giftList;
        this.context = context;
    }

    public GridViewAdapter(List<GiftBean> giftList) {
        this.giftList = giftList;
    }

    @Override
    public int getCount() {
        return giftList.size();
    }

    @Override
    public Object getItem(int position) {
        return giftList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_show_gift, null);
            viewHolder = new ViewHolder();
            viewHolder.mGiftViewImg = (LoadUrlImageView) convertView.findViewById(R.id.iv_show_gift_img);
            viewHolder.mGiftPrice = (TextView) convertView.findViewById(R.id.tv_show_gift_price);
            viewHolder.mGiftExperience = (TextView) convertView.findViewById(R.id.tv_show_gift_experience);
            viewHolder.tv_gift_name = (TextView) convertView.findViewById(R.id.tv_gift_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GiftBean g = giftList.get(position);
        viewHolder.mGiftViewImg.setImageLoadUrl(g.getStaticIcon());
        viewHolder.mGiftPrice.setText(g.getMemberConsume() + "");//金币价格
        viewHolder.tv_gift_name.setText(g.getName());//礼物名字
        return convertView;
    }

    private class ViewHolder {
        public LoadUrlImageView mGiftViewImg;
        public TextView mGiftPrice, mGiftExperience,tv_gift_name;
    }
}
