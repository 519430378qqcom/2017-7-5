package com.lvshandian.lemeng.adapter.mine;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.adapter.CommonAdapter;
import com.lvshandian.lemeng.adapter.ViewHolder;
import com.lvshandian.lemeng.entity.ContributionBeanBack;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */


public class ContributionListAdapter extends CommonAdapter<ContributionBeanBack> {
    private List<ContributionBeanBack> mDatas;

    public ContributionListAdapter(Context context, List<ContributionBeanBack> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.mDatas = mDatas;
    }

    @Override
    public void convert(ViewHolder helper, ContributionBeanBack item, int position) {

        ImageView iv_rank = helper.getView(R.id.iv_rank);
        ImageView iv_level = helper.getView(R.id.iv_level);
        ImageView iv_header = helper.getView(R.id.iv_header);
        ImageView iv_gender = helper.getView(R.id.iv_gender);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_gong_xian = helper.getView(R.id.tv_gong_xian);
        TextView tv_no = helper.getView(R.id.tv_no);

        ContributionBeanBack bean = mDatas.get(position);
        if (position == 0) {
            iv_rank.setVisibility(View.VISIBLE);
            tv_no.setVisibility(View.GONE);
            iv_rank.setImageResource(R.mipmap.yinpai);
        } else if (position == 1) {
            iv_rank.setVisibility(View.VISIBLE);
            tv_no.setVisibility(View.GONE);
            iv_rank.setImageResource(R.mipmap.tongpiao);
        } else {
            tv_no.setVisibility(View.VISIBLE);
            iv_rank.setVisibility(View.GONE);
        }

        String gender = bean.getGender();
        iv_gender.setImageResource(android.text.TextUtils.equals(gender, "1") ? R.mipmap.male : R.mipmap.female);

        int level = Integer.valueOf(bean.getLevel());
        iv_level.setImageResource(GrademipmapUtils.LevelImg[level - 1]);

        String nickName = bean.getNickName();
        tv_name.setText(nickName);

        tv_gong_xian.setText(mContext.getResources().getString(R.string.contribution_lepiao_num,String.valueOf(bean.getSumAmount())));

        String picUrl = bean.getPicUrl();
        Picasso.with(mContext).load(picUrl).into(iv_header);

        tv_no.setText("No." + (position + 2));
    }

}
