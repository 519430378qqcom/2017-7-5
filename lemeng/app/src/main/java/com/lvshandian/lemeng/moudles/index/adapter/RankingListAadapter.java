package com.lvshandian.lemeng.moudles.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.bean.ContributionBeanBack;
import com.lvshandian.lemeng.widget.view.AvatarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class RankingListAadapter extends RecyclerView.Adapter<RankingListAadapter.RankingViewHolder> {

    private List<ContributionBeanBack> mData = new ArrayList<>();
    private Context context;
    private OnRecyclerClickListener onRecyclerClickListener;

    public RankingListAadapter(Context context, List<ContributionBeanBack> rankingList) {
        this.mData = rankingList;
        this.context = context;
    }

    @Override
    public RankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false);
        RankingViewHolder holder = new RankingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RankingViewHolder holder, final int position) {
        if (position == mData.size() - 1) {
            holder.iv_ranking.setVisibility(View.INVISIBLE);
            holder.iv_head.setImageResource(R.mipmap.icon_look_more);
            holder.tv_rank_nick.setVisibility(View.GONE);
            holder.zhanwei.setVisibility(View.VISIBLE);
            holder.iv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerClickListener.onRecyclerClick(position);
                }
            });
        }else {
            ContributionBeanBack user = mData.get(position);
            holder.tv_rank_nick.setVisibility(View.VISIBLE);
            holder.zhanwei.setVisibility(View.GONE);
            if (position == 0) {
                holder.iv_ranking.setVisibility(View.VISIBLE);
                holder.iv_ranking.setImageResource(R.mipmap.ic_ranking_1);
            } else if (position == 1) {
                holder.iv_ranking.setVisibility(View.VISIBLE);
                holder.iv_ranking.setImageResource(R.mipmap.ic_ranking_2);
            } else if (position == 2) {
                holder.iv_ranking.setVisibility(View.VISIBLE);
                holder.iv_ranking.setImageResource(R.mipmap.ic_ranking_3);
            } else {
                holder.iv_ranking.setVisibility(View.INVISIBLE);
            }
            holder.tv_rank_nick.setText(user.getNickName());
            holder.iv_head.setAvatarUrl(user.getPicUrl());
            holder.iv_head.setOnClickListener(new View.OnClickListener() {
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

    class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rank_nick;
        ImageView iv_ranking;
        AvatarView iv_head;
        View zhanwei;

        public RankingViewHolder(View itemView) {
            super(itemView);
            iv_head = (AvatarView) itemView.findViewById(R.id.iv_head);
            iv_ranking = (ImageView) itemView.findViewById(R.id.iv_ranking);
            tv_rank_nick = (TextView) itemView.findViewById(R.id.tv_rank_nick);
            zhanwei = itemView.findViewById(R.id.zhanwei);
        }
    }

    public interface OnRecyclerClickListener {
        void onRecyclerClick(int position);
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }
}
