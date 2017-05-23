package com.lvshandian.lemeng.moudles.mine.my.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.bean.RoomUserBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sll on 2016/12/2.
 */

public class RoomUsersDataAdapter extends RecyclerView.Adapter<RoomUsersDataAdapter.RoomUserHolder> {
    private LayoutInflater mInflator;
    private Context context;
    private List<RoomUserBean> mDatas;

    public RoomUsersDataAdapter(Context context, List<RoomUserBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        mInflator = LayoutInflater.from(context);
    }


    @Override
    public RoomUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflator.inflate(R.layout.item_room_user_head, parent, false);
        return new RoomUserHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RoomUserHolder holder, final int position) {
        RoomUserBean bean = mDatas.get(position);
//        ImageLoader.getInstance().displayImage(bean.getPicUrl(), holder.ivHeader);
        if (TextUtils.isEmpty(bean.getPicUrl())){

        }else {
            Picasso.with(context).load(bean.getPicUrl()).placeholder(R.mipmap.head_default)
                    .error(R.mipmap.head_default).resize(50, 50).into(holder.ivHeader);
        }


        holder.ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, position);
            }
        });

        holder.ivHeader.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mLongClickListener.onItemLongClickListener(v, position);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    private OnItemLongClickListener mLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    class RoomUserHolder extends RecyclerView.ViewHolder {
        private ImageView ivHeader;

        public RoomUserHolder(View itemView) {
            super(itemView);
            ivHeader = (ImageView) itemView.findViewById(R.id.room_user_header);
        }
    }
}


