package com.lvshandian.lemeng.moudles.index.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvshandian.lemeng.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class GameAdapter extends BaseAdapter {
    private ArrayList<Integer> jbList;
    private Context context;
    public GameAdapter(Context mContext, ArrayList<Integer> jbList) {
        this.jbList = jbList;
        context = mContext;
    }

    @Override
    public int getCount() {
        return jbList.size();
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
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_game, null);
            viewHolder.gameImg = (ImageView) convertView.findViewById(R.id.game_img);
            viewHolder.gameText = (TextView) convertView.findViewById(R.id.game_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
         viewHolder.gameImg.setImageResource(jbList.get(position));
        return convertView;
    }

    private class ViewHolder{
        ImageView gameImg;
        TextView gameText;
    }
}
