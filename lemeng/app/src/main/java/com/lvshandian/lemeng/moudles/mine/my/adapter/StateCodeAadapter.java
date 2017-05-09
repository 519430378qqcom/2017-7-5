package com.lvshandian.lemeng.moudles.mine.my.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.moudles.mine.bean.StateCodeBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/5/9.
 */

public class StateCodeAadapter extends RecyclerView.Adapter<StateCodeAadapter.StateCodeViewHolder> {

    private List<StateCodeBean> mData = new ArrayList<>();
    private Context context;
    private OnRecyclerClickListener onRecyclerClickListener;

    public StateCodeAadapter(Context context, List<StateCodeBean> rankingList) {
        this.mData = rankingList;
        this.context = context;
    }

    @Override
    public StateCodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_state_code, parent, false);
        StateCodeViewHolder holder = new StateCodeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(StateCodeViewHolder holder, final int position) {
        StateCodeBean user = mData.get(position);
        holder.tv_stateName.setText(user.getCountryName());
        holder.tv_stateCode.setText("+" + user.getCountryTop());

        holder.ll_state_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerClickListener.onRecyclerClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class StateCodeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_stateName;
        TextView tv_stateCode;
        LinearLayout ll_state_code;

        public StateCodeViewHolder(View itemView) {
            super(itemView);
            tv_stateName = (TextView) itemView.findViewById(R.id.tv_stateName);
            tv_stateCode = (TextView) itemView.findViewById(R.id.tv_stateCode);
            ll_state_code = (LinearLayout) itemView.findViewById(R.id.ll_state_code);
        }
    }

    public interface OnRecyclerClickListener {
        void onRecyclerClick(int position);
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }
}
