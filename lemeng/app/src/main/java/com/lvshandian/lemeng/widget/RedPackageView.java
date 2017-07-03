package com.lvshandian.lemeng.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.entity.GrabPackageBean;
import com.lvshandian.lemeng.entity.PackageRank;
import com.lvshandian.lemeng.interfaces.IGetRedPackage;
import com.lvshandian.lemeng.interfaces.IRedPackageContract;
import com.lvshandian.lemeng.widget.presenter.RedPackagePresenter;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.squareup.picasso.Picasso;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/19
 * version: 1.0
 * desc   : 直播间红包模块view类
 */
public class RedPackageView implements View.OnClickListener, IRedPackageContract.IView {
    private Context mContext;
    /**
     * 红包视图的父视图
     */
    private ViewGroup parentView;
    /**
     * 红包view
     */
    private View redPackageView;
    /**
     * 红包标识
     */
    private String redenvelope;
    IGetRedPackage iGetRedPackage;
    /**
     * 关闭
     */
    private ImageView iv_close;
    private ImageView iv_box_bg;
    private ImageView iv_box;
    /**
     * 确定
     */
    private Button btn_ok;
    /**
     * 抢
     */
    private RelativeLayout rl_grab;
    /**
     * 查看其他人运气
     */
    private TextView tv_other;
    /**
     * 显示红包金额的容器
     */
    private LinearLayout ll_num_container;
    /**
     * 获得红包金币的动画view
     */
    private ImageView iv_niujinbi1, iv_niujinbi2, iv_niujinbi3, iv_niujinbi4;
    private ImageView iv_return;
    /**
     * 红包根布局
     */
    private RelativeLayout rl_redpackage_container;
    /**
     * 排行根布局
     */
    private RelativeLayout rl_rank_container;
    /**
     * 红包记录列表
     */
    private RecyclerView rcy_redpackage_rank;
    private RedPackagePresenter redPackagePresenter;
    private RedPackageAdapter redPackageAdapter;
    private PackageRank packageRank;

    public RedPackageView(Context context, ViewGroup parentView, String redenvelope,IGetRedPackage iGetRedPackage) {
        mContext = context;
        this.parentView = parentView;
        this.redenvelope = redenvelope;
        this.iGetRedPackage = iGetRedPackage;
        redPackageView = View.inflate(context, R.layout.red_package, null);
        iv_close = (ImageView) redPackageView.findViewById(R.id.iv_close);
        iv_box = (ImageView) redPackageView.findViewById(R.id.iv_box);
        iv_box_bg = (ImageView) redPackageView.findViewById(R.id.iv_box_bg);
        rl_grab = (RelativeLayout) redPackageView.findViewById(R.id.rl_grab);
        btn_ok = (Button) redPackageView.findViewById(R.id.btn_ok);
        tv_other = (TextView) redPackageView.findViewById(R.id.tv_other);
        iv_niujinbi1 = (ImageView) redPackageView.findViewById(R.id.iv_niujinbi1);
        iv_niujinbi2 = (ImageView) redPackageView.findViewById(R.id.iv_niujinbi2);
        iv_niujinbi3 = (ImageView) redPackageView.findViewById(R.id.iv_niujinbi3);
        iv_niujinbi4 = (ImageView) redPackageView.findViewById(R.id.iv_niujinbi4);
        iv_return = (ImageView) redPackageView.findViewById(R.id.iv_return);
        ll_num_container = (LinearLayout) redPackageView.findViewById(R.id.ll_num_container);
        rl_redpackage_container = (RelativeLayout) redPackageView.findViewById(R.id.rl_redpackage_container);
        rl_rank_container = (RelativeLayout) redPackageView.findViewById(R.id.rl_rank_container);
        rcy_redpackage_rank = (RecyclerView) redPackageView.findViewById(R.id.rcy_redpackage_rank);
        tv_other.setOnClickListener(this);
        iv_return.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        rl_grab.setOnClickListener(this);
        redPackagePresenter = new RedPackagePresenter(this);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(iv_box_bg, "rotation", 0f, 360f);
        rotation.setDuration(4000);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatCount(1000);
        rotation.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close://关闭
            case R.id.btn_ok:
                dismiss();
                break;
            case R.id.rl_grab://抢红包
                redPackagePresenter.grabPackage(SharedPreferenceUtils.getUserInfo(mContext).getId(), redenvelope);
                break;
            case R.id.tv_other://查看其他人的运气
                showLuckyPlayers();
                redPackagePresenter.packageRank(redenvelope);
                break;
            case R.id.iv_return://返回
                returnRedPackage();
                break;
        }
    }

    /**
     * 显示显示视图
     */
    public void show() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        redPackageView.setLayoutParams(layoutParams);
        parentView.addView(redPackageView);
    }

    /**
     * 移除红包视图
     */
    public void dismiss() {
        parentView.removeView(redPackageView);
    }

    /**
     * 宝箱中抢到金币的动画
     */
    public void getCoinAnimation() {
        coinAnimator(iv_niujinbi1, 0, 1);
        coinAnimator(iv_niujinbi2, 200, 1);
        coinAnimator(iv_niujinbi3, 300, 1);
        coinAnimator(iv_niujinbi4, 400, 1);
    }

    /**
     * 单个金币的动画
     *
     * @param target  动画目标
     * @param delay   延迟执行
     * @param aXSpeed x轴的加速度
     */
    private void coinAnimator(final View target, int delay, final int aXSpeed) {
        ValueAnimator animator = ValueAnimator.ofFloat(1);
        animator.setTarget(target);
        animator.setDuration(1000);
        animator.setStartDelay(delay);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                target.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                target.setVisibility(View.GONE);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                target.setTranslationX(-(target.getLeft() * aXSpeed * animatedValue * animatedValue / 2));
                target.setTranslationY(-(target.getTop() * animatedValue));
            }
        });
        animator.start();
    }

    /**
     * 显示抢到的金币数
     *
     * @param sum
     */
    public void showGetCoinSum(int sum) {
        String s = String.valueOf(sum);
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(getSumId(chars[i]));
            ll_num_container.addView(imageView);
        }
    }

    /**
     * 获取对应的数字图片id
     *
     * @param aChar
     * @return
     */
    private int getSumId(char aChar) {
        int mipmapId = 0;
        switch (aChar) {
            case '0':
                mipmapId = R.mipmap.redpackage_num0;
                break;
            case '1':
                mipmapId = R.mipmap.redpackage_num1;
                break;
            case '2':
                mipmapId = R.mipmap.redpackage_num2;
                break;
            case '3':
                mipmapId = R.mipmap.redpackage_num3;
                break;
            case '4':
                mipmapId = R.mipmap.redpackage_num4;
                break;
            case '5':
                mipmapId = R.mipmap.redpackage_num5;
                break;
            case '6':
                mipmapId = R.mipmap.redpackage_num6;
                break;
            case '7':
                mipmapId = R.mipmap.redpackage_num7;
                break;
            case '8':
                mipmapId = R.mipmap.redpackage_num8;
                break;
            case '9':
                mipmapId = R.mipmap.redpackage_num9;
                break;

        }
        return mipmapId;
    }

    /**
     * 显示红包排行
     */
    public void showLuckyPlayers() {
        rl_rank_container.setVisibility(View.VISIBLE);
        rl_redpackage_container.setVisibility(View.GONE);
    }

    /**
     * 返回红包页面
     */
    public void returnRedPackage() {
        rl_redpackage_container.setVisibility(View.VISIBLE);
        rl_rank_container.setVisibility(View.GONE);
    }

    @Override
    public void grabPackage(GrabPackageBean grabPackageBean) {
        //0 为获取红包成功 1为异常 2 已经抢过红包 3 红包已抢完
        switch (grabPackageBean.getCode()) {
            case 0:
                showGetCoinSum(grabPackageBean.getData());
                iGetRedPackage.getRedPackage(grabPackageBean.getData());
                getCoinAnimation();
                rl_grab.setVisibility(View.GONE);
                btn_ok.setVisibility(View.VISIBLE);
                tv_other.setVisibility(View.VISIBLE);
                break;
            case 3:
                iv_box.setImageResource(R.mipmap.redpackage_empty);
                rl_grab.setVisibility(View.GONE);
                btn_ok.setVisibility(View.VISIBLE);
                tv_other.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void packageRank(PackageRank packageRank) {
        if(packageRank.getCode() ==1) {
            return;
        }
        this.packageRank = packageRank;
        if(redPackageAdapter == null) {
            redPackageAdapter = new RedPackageAdapter();
            rcy_redpackage_rank.setLayoutManager(new LinearLayoutManager(mContext));
            rcy_redpackage_rank.setAdapter(redPackageAdapter);
        }else {
            redPackageAdapter.notifyDataSetChanged();
        }
    }

    class RedPackageAdapter extends RecyclerView.Adapter<RedPackageAdapter.RedPHolder> {
        @Override
        public RedPHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RedPHolder(LayoutInflater.from(mContext).inflate(R.layout.redpackage_rank_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RedPHolder holder, int position) {
            PackageRank.DataBean dataBean = packageRank.getData().get(position);
            Picasso.with(mContext).load(dataBean.getPicUrl()).placeholder(R.mipmap.head_default).into(holder.iv_rank_head);
            holder.iv_rank_name.setText(dataBean.getNickName());
            holder.tv_rank_sum.setText(dataBean.getAmount()+"");
        }

        @Override
        public int getItemCount() {
            return packageRank.getData().size();
        }

        class RedPHolder extends RecyclerView.ViewHolder {
            ImageView iv_rank_head;
            TextView iv_rank_name;
            TextView tv_rank_sum;

            public RedPHolder(View itemView) {
                super(itemView);
                iv_rank_head = (ImageView) itemView.findViewById(R.id.iv_rank_head);
                iv_rank_name = (TextView) itemView.findViewById(R.id.iv_rank_name);
                tv_rank_sum = (TextView) itemView.findViewById(R.id.tv_rank_sum);
            }
        }
    }
}
