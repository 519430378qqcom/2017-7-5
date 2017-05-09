package com.lvshandian.lemeng.moudles.mine.fragment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.moudles.mine.bean.VideoBean;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by zhang on 2016/11/18.
 * 创建图片适配器
 */

public class VideoAdapter extends BaseAdapter {
    List<VideoBean> list;
    Context mContext;
    String isShowAdd;

    public VideoAdapter(Context mContext, List<VideoBean> list, String isShowAdd) {
        this.mContext = mContext;
        this.list = list;
        this.isShowAdd = isShowAdd;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
    ViewHolder viewHolder;

    public int getCount() {
        if (list.size() < 4) {
            return list.size();
        } else {
            return 4;
        }
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {


        viewHolder = new ViewHolder();


        view = LayoutInflater.from(mContext).inflate(
                R.layout.frament_mine_photo, null);
        viewHolder.img_gridview = (ImageView) view.findViewById(R.id.img_gridview);
        viewHolder.video_start = (ImageView) view.findViewById(R.id.video_start);
        viewHolder.img_add_touming = (ImageView) view.findViewById(R.id.img_add_touming);
        int width = viewHolder.img_gridview.getMeasuredWidth();
        viewHolder.img_gridview.setMinimumHeight(width);
        view.setTag(viewHolder);
        if (isShowAdd.equals("isShow")) {
            if (position == 3 || position == list.size() - 1) {
                viewHolder.video_start.setVisibility(View.GONE);
                viewHolder.img_add_touming.setVisibility(View.VISIBLE);
                if (position == 3){
                    Transformation transformation = new RoundedTransformationBuilder()
                            .borderColor(Color.TRANSPARENT)
                            .borderWidthDp(1)
                            .cornerRadiusDp(12)
                            .oval(false)
                            .build();
                    Picasso.with(mContext)
                            .load(list.get(position).getThumbnailUrl())
                            .fit()
                            .transform(transformation)
                            .into(viewHolder.img_gridview);
                }
                return view;
            } else {
//            ImageLoader.getInstance().displayImage("drawable://" + R.mipmap.app_icon, viewHolder.img_gridview);
                viewHolder.video_start.setVisibility(View.VISIBLE);
                viewHolder.img_add_touming.setVisibility(View.GONE);
                Transformation transformation = new RoundedTransformationBuilder()
                        .borderColor(Color.TRANSPARENT)
                        .borderWidthDp(1)
                        .cornerRadiusDp(12)
                        .oval(false)
                        .build();
                Picasso.with(mContext)
                        .load(list.get(position).getThumbnailUrl())
                        .fit()
                        .transform(transformation)
                        .into(viewHolder.img_gridview);
//            ImageLoader.getInstance().displayImage(list.get(position).getThumbnailUrl(), viewHolder.img_gridview);
            }
        } else {
            viewHolder.video_start.setVisibility(View.VISIBLE);
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.TRANSPARENT)
                    .borderWidthDp(1)
                    .cornerRadiusDp(12)
                    .oval(false)
                    .build();
            Picasso.with(mContext)
                    .load(list.get(position).getThumbnailUrl())
                    .fit()
                    .transform(transformation)
                    .into(viewHolder.img_gridview);
        }
        return view;

    }

    private class ViewHolder {
        ImageView img_gridview, video_start,img_add_touming;
    }

}
