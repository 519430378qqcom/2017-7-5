package com.lvshandian.lemeng.moudles.index.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.bean.LastAwardBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ssb on 2017/6/21 14:04.
 * company: lvshandian
 */

public class LucyRankingAdapter extends RecyclerView.Adapter<LucyRankingAdapter.RankViewHolder> {
    Context context;
    List<LastAwardBean.RoomRanksBean> ranksBean;

    public LucyRankingAdapter(Context context, List<LastAwardBean.RoomRanksBean> ranksBean) {
        this.context = context;
        this.ranksBean = ranksBean;
    }

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_ranking_item, parent, false);
        RankViewHolder rankViewHolder = new RankViewHolder(view);
        return rankViewHolder;
    }

    @Override
    public void onBindViewHolder(RankViewHolder holder, int position) {
        LastAwardBean.RoomRanksBean ranks = ranksBean.get(position);
        if (position == 0 || position == 2 || position == 4) {
            holder.parent_bg.setBackgroundColor(Color.parseColor("#1ed8d8d8"));
        } else {
            holder.parent_bg.setBackgroundColor(Color.parseColor("#00000000"));
        }
        if (position == 0) {
            holder.rank_number.setVisibility(View.VISIBLE);
            holder.rank_number.setImageResource(R.mipmap.room_rank_1);
        } else if (position == 1) {
            holder.rank_number.setVisibility(View.VISIBLE);
            holder.rank_number.setImageResource(R.mipmap.room_rank_2);
        } else if (position == 2) {
            holder.rank_number.setVisibility(View.VISIBLE);
            holder.rank_number.setImageResource(R.mipmap.room_rank_3);
        } else {
            holder.rank_number.setVisibility(View.INVISIBLE);
        }

        Picasso.with(context).load(ranks.getPicurl()).placeholder(R.mipmap.head_default)
                .error(R.mipmap.head_default).into(holder.rank_head);

        holder.rank_name.setText(ranks.getNickName());
        holder.rank_money.setText("+" + ranks.getAmount());
    }

    @Override
    public int getItemCount() {
        return ranksBean.size();
    }

    class RankViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parent_bg;
        ImageView rank_number;
        ImageView rank_head;
        TextView rank_name;
        TextView rank_money;

        public RankViewHolder(View itemView) {
            super(itemView);
            parent_bg = (LinearLayout) itemView.findViewById(R.id.parent_bg);
            rank_number = (ImageView) itemView.findViewById(R.id.rank_number);
            rank_head = (ImageView) itemView.findViewById(R.id.rank_head);
            rank_name = (TextView) itemView.findViewById(R.id.rank_name);
            rank_money = (TextView) itemView.findViewById(R.id.rank_money);
        }
    }
}
