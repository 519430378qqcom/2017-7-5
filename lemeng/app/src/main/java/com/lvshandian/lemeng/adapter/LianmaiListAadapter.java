package com.lvshandian.lemeng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.widget.view.AvatarView;
import com.netease.nim.uikit.team.activity.FunseBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public class LianmaiListAadapter extends RecyclerView.Adapter<LianmaiListAadapter.LianmaiViewHolder> {

    private List<FunseBean> funseList;
    private Context context;
    private OnRecyclerClickListener onRecyclerClickListener;

    public LianmaiListAadapter(Context context, List<FunseBean> funseBeen) {
        this.funseList = funseBeen;
        this.context = context;
    }

    @Override
    public LianmaiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lianmai_item, parent, false);
        LianmaiViewHolder holder = new LianmaiViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final LianmaiViewHolder holder, final int position) {
        final FunseBean funseBean = funseList.get(position);
        if (!TextUtils.isEmpty(funseBean.getPicUrl())) {
            Picasso.with(context).load(funseBean.getPicUrl()).placeholder(R.mipmap.head_default)
                    .error(R.mipmap.head_default).into(holder.iv_head);
        }

        holder.tv_name.setText(funseBean.getNickName());
//        holder.cb_checkBox.setChecked(funseBean.isChecked());
        if (funseBean.isChecked()){
            holder.cb_checkBox.setBackgroundResource(R.drawable.lianmai_selecte);
        }else {
            holder.cb_checkBox.setBackgroundResource(R.drawable.lianmai_unselecte);
        }
        holder.cb_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerClickListener.onRecyclerClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return funseList.size();
    }

    class LianmaiViewHolder extends RecyclerView.ViewHolder {
        AvatarView iv_head;
        TextView tv_name;
        CheckBox cb_checkBox;

        public LianmaiViewHolder(View itemView) {
            super(itemView);
            iv_head = (AvatarView) itemView.findViewById(R.id.iv_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            cb_checkBox = (CheckBox) itemView.findViewById(R.id.cb_checkBox);
        }
    }

    public interface OnRecyclerClickListener {
        void onRecyclerClick(int position);
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }
}
