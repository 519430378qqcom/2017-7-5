package com.lvshandian.lemeng.widget;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.utils.GiftAnimationUtil;
import com.lvshandian.lemeng.entity.GiftSendModel;
import com.lvshandian.lemeng.widget.view.AvatarView;
import com.squareup.picasso.Picasso;

public class GiftFrameLayout extends FrameLayout {
    private LayoutInflater mInflater;
    RelativeLayout anim_rl;
    AvatarView anim_header;
    ImageView anim_gift;
    ImageView anim_light;
    TextView anim_nickname, anim_sign;
    TextView anim_num;
    String RepeatGiftNumber = "1";//记录连点的次数
    private String fromUserId;//送礼物人ID
    private String giftId;//礼物ID

    int repeatCount = 0;
    private boolean isShowing = false;

    /**
     * 动画集合
     */
    private AnimatorSet animatorSet;

    private CountDownTimer timer;

    public GiftFrameLayout(Context context) {
        this(context, null);
    }

    public GiftFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        View view = mInflater.inflate(R.layout.show_gift_animation, this, false);
        anim_rl = (RelativeLayout) view.findViewById(R.id.animation_person_rl);
        anim_gift = (ImageView) view.findViewById(R.id.animation_gift);
        anim_light = (ImageView) view.findViewById(R.id.animation_light);
//        anim_num = (StrokeTextView) view.findViewById(R.id.animation_num);
        anim_num = (TextView) view.findViewById(R.id.animation_num);
        anim_header = (AvatarView) view.findViewById(R.id.gift_userheader_iv);
        anim_nickname = (TextView) view.findViewById(R.id.gift_usernickname_tv);
        anim_sign = (TextView) view.findViewById(R.id.gift_usersign_tv);
        this.addView(view);
    }

    public void hideView() {
        anim_gift.setVisibility(INVISIBLE);
        anim_light.setVisibility(INVISIBLE);
        anim_num.setVisibility(INVISIBLE);
    }

    public void setModel(GiftSendModel model) {
        if (0 != model.getGiftCount()) {
            this.repeatCount = model.getGiftCount();
        }
        if (!TextUtils.isEmpty(model.getNickname())) {
            anim_nickname.setText(model.getNickname());
        }
        if (!TextUtils.isEmpty(model.getSig())) {
            anim_sign.setText(model.getSig());
        }
        if (!TextUtils.isEmpty(model.getUserAvatarRes())) {
            anim_header.setAvatarUrl(model.getUserAvatarRes());
        }
        if (!TextUtils.isEmpty(model.getGift_id())) {
            Picasso.with(MyApplication.mContext).load(model.getGift_id()).into(anim_gift);
        }
        if (!TextUtils.isEmpty(model.getRepeatGiftNumber())) {
            RepeatGiftNumber = model.getRepeatGiftNumber();
        }
        if (!TextUtils.isEmpty(model.getFromUserId())) {
            fromUserId = model.getFromUserId();
        }
        if (!TextUtils.isEmpty(model.getGiftId())) {
            giftId = model.getGiftId();
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public AnimatorSet startAnimation(final int repeatCount) {
        hideView();

        if (timer != null) {
            timer.cancel();
        }
        /**
         * 布局飞入动画
         */
        ObjectAnimator flyFromLtoR = GiftAnimationUtil.createFlyFromLtoR(anim_rl, -getWidth(), 0, 400, new OvershootInterpolator());
        flyFromLtoR.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                GiftFrameLayout.this.setVisibility(View.VISIBLE);
                GiftFrameLayout.this.setAlpha(1f);
                isShowing = true;
                anim_num.setText("x " + RepeatGiftNumber);
//                if (RepeatGiftNumber.equals("1")){
//                    anim_num.setText("x " + RepeatGiftNumber);
//                }else {
//                    anim_num.setText("连 x" + RepeatGiftNumber);
//                }
//                    anim_num.setText("x " + 1);
            }
        });

        /**
         * 礼物飞入动画
         */
        ObjectAnimator flyFromLtoR2 = GiftAnimationUtil.createFlyFromLtoR(anim_gift, -getWidth(), 0, 400, new DecelerateInterpolator());
        flyFromLtoR2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                anim_gift.setVisibility(View.VISIBLE);
            }


            @Override
            public void onAnimationEnd(Animator animation) {
                GiftAnimationUtil.startAnimationDrawable(anim_light);
                anim_num.setVisibility(View.VISIBLE);
            }
        });

        /**
         * 数量增加动画
         */
        ObjectAnimator scaleGiftNum = GiftAnimationUtil.scaleGiftNum(anim_num, repeatCount);
        scaleGiftNum.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
//                anim_num.setText("连 x" + RepeatGiftNumber);
                anim_num.setText("x" + RepeatGiftNumber);
            }
        });

        /**
         * 向上渐变消失动画
         */
//        final ObjectAnimator fadeAnimator = GiftAnimationUtil.createFadeAnimator(GiftFrameLayout.this, 0, -100, 300, 400);
        final ObjectAnimator fadeAnimator = GiftAnimationUtil.createFadeAnimator(GiftFrameLayout.this, 0, 0, 300, 400);
        fadeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                GiftFrameLayout.this.setVisibility(View.INVISIBLE);
            }
        });

        /**
         * 复原动画
         */
        ObjectAnimator fadeAnimator2 = GiftAnimationUtil.createFadeAnimator(GiftFrameLayout.this, 100, 0, 20, 0);

        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                animatorSet = GiftAnimationUtil.startAnimationHint(fadeAnimator);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isShowing = false;
                    }
                });
            }
        };
        timer.start();


        if (RepeatGiftNumber.equals("1") || !isShowing()) {
            animatorSet = GiftAnimationUtil.startAnimationing(flyFromLtoR, flyFromLtoR2);
        } else {
            anim_gift.setVisibility(View.VISIBLE);
            anim_num.setVisibility(View.VISIBLE);
            animatorSet = GiftAnimationUtil.startAnimationTex(scaleGiftNum);
//            anim_num.setText("连 x " + RepeatGiftNumber);
        }

//        animatorSet = GiftAnimationUtil.startAnimation(flyFromLtoR, flyFromLtoR2, scaleGiftNum, fadeAnimator, fadeAnimator2);
//        animatorSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                LogUtil.e("测试礼物", "动画集合");
//                isShowing = false;
//            }
//        });
        return animatorSet;

    }
}