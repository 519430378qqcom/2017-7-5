package com.lvshandian.lemeng.adapter.mine;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.interfaces.CommonAdapter;
import com.lvshandian.lemeng.interfaces.ViewHolder;
import com.lvshandian.lemeng.widget.view.CircleImageView;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.lvshandian.lemeng.utils.PicassoUtil;
import com.netease.nim.uikit.team.activity.FunseBean;

import java.util.List;

/**
 * Created by gjj on 2016/12/6.
 */

public class FunseListAdapter extends CommonAdapter<FunseBean> {
    private boolean isFunse;

    public FunseListAdapter(Context context, List<FunseBean> mDatas, int itemLayoutId, boolean isFunse) {
        super(context, mDatas, itemLayoutId);
        this.isFunse = isFunse;
    }

    @Override
    public void convert(ViewHolder helper, FunseBean item, final int position) {

        CircleImageView ivHeader = helper.getView(R.id.cv_userHead);
        TextView tvName = helper.getView(R.id.tv_item_uname);
        TextView tvSign = helper.getView(R.id.tv_item_usign);
        ImageView ivSex = helper.getView(R.id.tv_item_usex);
        ImageView tvLevel = helper.getView(R.id.tv_item_ulevel);
        ImageView ivAttention = helper.getView(R.id.iv_item_attention);

        String picUrl = item.getPicUrl();
        PicassoUtil.newInstance().onRoundnessImage(mContext, picUrl, ivHeader);

        String nickName = item.getNickName();
        tvName.setText(nickName);

        String signature = item.getSignature();
        tvSign.setText(signature);

        String gender = item.getGender();
        if (TextUtils.equals(gender, "1")) {
            ivSex.setImageResource(R.mipmap.male);
        } else {
            ivSex.setImageResource(R.mipmap.female);
        }

        String level = item.getLevel();
        tvLevel.setImageResource(GrademipmapUtils.LevelImg[Integer.valueOf(level) - 1]);

        String follow = item.getFollow();

        if (isFunse) {
            if (TextUtils.equals(follow, "1")) {
                ivAttention.setImageResource(R.mipmap.me_following);
            } else {
                ivAttention.setImageResource(R.mipmap.me_follow);
                ivAttention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemFollowClick(position, (ImageView) v);
                        }
                    }
                });
            }

        }
    }

    private OnItemFollowsClickListener mListener;

    public void setOnItemFollowsClick(OnItemFollowsClickListener listener) {
        mListener = listener;
    }

    public interface OnItemFollowsClickListener {
        void onItemFollowClick(int position, ImageView view);
    }
}


