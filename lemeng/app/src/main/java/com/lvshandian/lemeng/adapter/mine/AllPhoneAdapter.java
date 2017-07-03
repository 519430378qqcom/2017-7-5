package com.lvshandian.lemeng.adapter.mine;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.entity.mine.PhotoBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by lsd on 2017/4/14 0014.
 */

public class AllPhoneAdapter extends BaseAdapter {
     private List<PhotoBean> list;
     private Context mContext;
    public AllPhoneAdapter(Context mContext, List<PhotoBean> list) {
         this.list = list;
         this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView  == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_all_phone_layout,null);
            viewHolder.img_gridview = (ImageView) convertView.findViewById(R.id.Img);
            viewHolder.video_start = (ImageView) convertView.findViewById(R.id.video_start);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PhotoBean photoBean = list.get(position);
        viewHolder.video_start.setVisibility(View.GONE);
        ImageLoader.getInstance().displayImage(photoBean.getUrl(),viewHolder.img_gridview);
        return convertView;
    }


    private class ViewHolder {
        ImageView img_gridview,video_start;
    }

}
