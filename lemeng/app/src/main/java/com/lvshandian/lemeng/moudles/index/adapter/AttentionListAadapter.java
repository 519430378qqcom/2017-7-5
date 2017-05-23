package com.lvshandian.lemeng.moudles.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.bean.LiveBean;
import com.lvshandian.lemeng.widget.AvatarView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */

public class AttentionListAadapter extends RecyclerView.Adapter<AttentionListAadapter.HotViewHolder> {

    private List<LiveBean> mData ;
    private Context context;
    private OnRecyclerClickListener onRecyclerClickListener;

    public AttentionListAadapter(Context context, List<LiveBean> liveBeen) {
        this.mData = liveBeen;
        this.context = context;
    }

    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hall_live, parent, false);
        HotViewHolder holder = new HotViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HotViewHolder holder, final int position) {
        if (mData.size() > 0) {
            LiveBean user = mData.get(position);

            if (user.getRoomsType().equals("1")) {
                holder.iv_is_liveing.setVisibility(View.VISIBLE);
            } else {
                holder.iv_is_liveing.setVisibility(View.GONE);
            }

            holder.mUserNick.setText(user.getCreator().getNickName());
            Picasso.with(context).load(user.getLivePicUrl()).placeholder(R.mipmap.zhan_da)
                    .error(R.mipmap.zhan_da).into(holder.mUserPic);
            holder.mUserHead.setAvatarUrl(user.getCreator().getPicUrl());
            holder.mUserNums.setText(String.valueOf(user.getOnlineUserNum()) + " 在看");
            holder.mLinerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerClickListener.onRecyclerClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class HotViewHolder extends RecyclerView.ViewHolder {
        TextView mUserNick, mUserNums;
        ImageView mUserPic;
        AvatarView mUserHead;
        LinearLayout mLinerLayout;
        ImageView vip;
        ImageView iv_is_liveing;

        public HotViewHolder(View itemView) {
            super(itemView);
            mUserNick = (TextView) itemView.findViewById(R.id.tv_live_nick);
            mUserNums = (TextView) itemView.findViewById(R.id.tv_live_usernum);
            mUserHead = (AvatarView) itemView.findViewById(R.id.iv_live_user_head);
            mUserPic = (ImageView) itemView.findViewById(R.id.iv_live_user_pic);
            mLinerLayout = (LinearLayout) itemView.findViewById(R.id.parent_layout);
            vip = (ImageView) itemView.findViewById(R.id.vip);
            iv_is_liveing = (ImageView) itemView.findViewById(R.id.iv_is_liveing);
        }
    }

    public interface OnRecyclerClickListener {
        void onRecyclerClick(int position);
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }
}
