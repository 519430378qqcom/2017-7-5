package com.lvshandian.lemeng.moudles.index.live.redpackage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/19
 * version: 1.0
 * desc   : 直播间红包模块view类
 */
public class RedPackageView implements View.OnClickListener {
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
     * 关闭
     */
    private ImageView iv_close;
    /**
     * 确定
     */
    private Button btn_ok;
    /**
     * 抢
     */
    private Button btn_snatch;
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
    /**
     * 红包根布局
     */
    private RelativeLayout rl_redpackage_container;
    /**
     * 排行根布局
     */
    private RelativeLayout rl_rank_container;
    public RedPackageView(Context context) {
        mContext = context;
        redPackageView = View.inflate(context, R.layout.red_package, null);
        iv_close = (ImageView) redPackageView.findViewById(R.id.iv_close);
        btn_snatch = (Button) redPackageView.findViewById(R.id.btn_snatch);
        btn_ok = (Button) redPackageView.findViewById(R.id.btn_ok);
        tv_other = (TextView) redPackageView.findViewById(R.id.tv_other);
        iv_niujinbi1 = (ImageView) redPackageView.findViewById(R.id.iv_niujinbi1);
        iv_niujinbi2 = (ImageView) redPackageView.findViewById(R.id.iv_niujinbi2);
        iv_niujinbi3 = (ImageView) redPackageView.findViewById(R.id.iv_niujinbi3);
        iv_niujinbi4 = (ImageView) redPackageView.findViewById(R.id.iv_niujinbi4);
        ll_num_container = (LinearLayout) redPackageView.findViewById(R.id.ll_num_container);
        rl_redpackage_container = (RelativeLayout) redPackageView.findViewById(R.id.rl_redpackage_container);
        rl_rank_container = (RelativeLayout) redPackageView.findViewById(R.id.rl_rank_container);
        tv_other.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_snatch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close://关闭
            case R.id.btn_ok:
                dismiss();
                break;
            case R.id.btn_snatch://抢红包
                break;
            case R.id.tv_other://查看其他人的运气
                break;
        }
    }

    /**
     * 显示显示视图
     */
    public void show() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(redPackageView.getWidth(), redPackageView.getHeight());
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
            case 0:
                mipmapId = R.mipmap.redpackage_num0;
                break;
            case 1:
                mipmapId = R.mipmap.redpackage_num1;
                break;
            case 2:
                mipmapId = R.mipmap.redpackage_num2;
                break;
            case 3:
                mipmapId = R.mipmap.redpackage_num3;
                break;
            case 4:
                mipmapId = R.mipmap.redpackage_num4;
                break;
            case 5:
                mipmapId = R.mipmap.redpackage_num5;
                break;
            case 6:
                mipmapId = R.mipmap.redpackage_num6;
                break;
            case 7:
                mipmapId = R.mipmap.redpackage_num7;
                break;
            case 8:
                mipmapId = R.mipmap.redpackage_num8;
                break;
            case 9:
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
    public void returnRedPackage(){
        rl_redpackage_container.setVisibility(View.VISIBLE);
        rl_rank_container.setVisibility(View.GONE);
    }
}
