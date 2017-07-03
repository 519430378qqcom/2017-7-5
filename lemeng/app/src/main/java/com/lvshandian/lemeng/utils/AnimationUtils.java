package com.lvshandian.lemeng.utils;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.entity.CustomGiftBean;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/2/21.
 */

public class AnimationUtils {
    private AnimationDrawable animationDrawable;
    AutoRelativeLayout mRoot;
    Activity context;
    protected List<CustomGiftBean> messageQueueList = new ArrayList<>();
    boolean isFireworkGiftAnimationPlayEnd = true;
    boolean isFirst = false;
    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
        }
    };
    public static AnimationUtils anmation;

    public static AnimationUtils getAnimation() {
        if (anmation == null) {
            anmation = new AnimationUtils();
        }

        return anmation;
    }


    public void SwichAnimation(CustomGiftBean customGiftBean, Activity context, AutoRelativeLayout mRoot, int type) {
        this.mRoot = mRoot;
        this.context = context;
        messageQueueList.add(customGiftBean);
        if (isFirst) {
            LogUtils.e("礼物id--return" + customGiftBean.getGift_item_index());
            return;
        }
        isFirst = true;
        switch (Integer.parseInt(customGiftBean.getGift_item_index().trim())) {
            //跑车
            case 16:
                showFireworksAnimationCar(customGiftBean);
                break;
            //火箭
            case 17:
                showFireworksAnimationRocket(customGiftBean);
                break;
            //烟花
            case 18:
                showFireworksAnimationFireworks(customGiftBean);
                break;
            //飞机礼物
            case 19:
                showFireworksAnimationAircraft(customGiftBean);
                break;
            //游轮
            case 20:
                showFireworksAnimationSteamer(customGiftBean);
                break;
            //蛋糕
            case 21:
                showFireworksAnimationCake(customGiftBean);
                break;
            //结婚
            case 22:
                showFireworksAnimationMarry(customGiftBean);
                break;

        }

    }

    private void Animation(CustomGiftBean customGiftBean) {
        switch (Integer.parseInt(customGiftBean.getGift_item_index().trim())) {
            //跑车
            case 16:
                showFireworksAnimationCar(customGiftBean);
                break;
            //火箭
            case 17:
                showFireworksAnimationRocket(customGiftBean);
                break;
            //烟花
            case 18:
                showFireworksAnimationFireworks(customGiftBean);
                break;
            //飞机礼物
            case 19:
                showFireworksAnimationAircraft(customGiftBean);
                break;
            //游轮
            case 20:
                showFireworksAnimationSteamer(customGiftBean);
                break;
            //蛋糕
            case 21:
                showFireworksAnimationCake(customGiftBean);
                break;
            //结婚
            case 22:
                showFireworksAnimationMarry(customGiftBean);
                break;
        }

    }

    private FramesSequenceAnimation framesSequenceAnimation = null;

    /**
     * @dw 火箭
     * @author 张亚楠
     */
    protected void showFireworksAnimationRocket(CustomGiftBean mSendGiftBean) {
        if (!isFireworkGiftAnimationPlayEnd) {
            return;
        }
        isFireworkGiftAnimationPlayEnd = false;
        final ImageView image = new ImageView(context);
        framesSequenceAnimation = new FramesSequenceAnimation(context, image,
                R.array.feed_icons, 20);
        framesSequenceAnimation.setOneShot(true);
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)); // 设置图片宽高
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        mRoot.addView(image);
        framesSequenceAnimation.start();
        framesSequenceAnimation
                .setFramesSequenceAnimationListener(new FramesSequenceAnimation.FramesSequenceAnimationListener() {

                    @Override
                    public void AnimationStopped() {
                        // TODO Auto-generated method stub
                        if (mRoot == null) return;
                        messageQueueList.remove(0);
                        mRoot.removeView(image);
                        isFireworkGiftAnimationPlayEnd = true;
                        if (messageQueueList.size() > 0) {
                            Animation(messageQueueList.get(0));
                        } else {
                            isFirst = false;
                        }


                    }

                    @Override
                    public void AnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                });
    }


    /**
     * @dw 结婚
     * @author 张亚楠
     */
    protected void showFireworksAnimationMarry(CustomGiftBean mSendGiftBean) {
        if (!isFireworkGiftAnimationPlayEnd) {
            return;
        }
        isFireworkGiftAnimationPlayEnd = false;
        final ImageView image = new ImageView(context);
        framesSequenceAnimation = new FramesSequenceAnimation(context, image,
                R.array.marry_icons, 30);
        framesSequenceAnimation.setOneShot(true);
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)); // 设置图片宽高
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        mRoot.addView(image);
        framesSequenceAnimation.start();
        framesSequenceAnimation
                .setFramesSequenceAnimationListener(new FramesSequenceAnimation.FramesSequenceAnimationListener() {

                    @Override
                    public void AnimationStopped() {
                        // TODO Auto-generated method stub
                        if (mRoot == null) return;
                        messageQueueList.remove(0);
                        mRoot.removeView(image);
                        isFireworkGiftAnimationPlayEnd = true;
                        if (messageQueueList.size() > 0) {
                            Animation(messageQueueList.get(0));
                        } else {
                            isFirst = false;

                        }


                    }

                    @Override
                    public void AnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                });

    }

    /**
     * @dw 蛋糕
     * @author 张亚楠
     */
    protected void showFireworksAnimationCake(CustomGiftBean mSendGiftBean) {
        if (!isFireworkGiftAnimationPlayEnd) {
            return;
        }
        isFireworkGiftAnimationPlayEnd = false;
        final ImageView image = new ImageView(context);
        framesSequenceAnimation = new FramesSequenceAnimation(context, image,
                R.array.cake_icons, 30);
        framesSequenceAnimation.setOneShot(true);
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)); // 设置图片宽高
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        mRoot.addView(image);
        framesSequenceAnimation.start();
        framesSequenceAnimation
                .setFramesSequenceAnimationListener(new FramesSequenceAnimation.FramesSequenceAnimationListener() {

                    @Override
                    public void AnimationStopped() {
                        // TODO Auto-generated method stub
                        if (mRoot == null) return;
                        messageQueueList.remove(0);
                        mRoot.removeView(image);
                        isFireworkGiftAnimationPlayEnd = true;
                        if (messageQueueList.size() > 0) {
                            Animation(messageQueueList.get(0));
                        } else {
                            isFirst = false;

                        }


                    }

                    @Override
                    public void AnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                });

    }

    /**
     * @dw 飞机
     * @author 张亚楠
     */
    protected void showFireworksAnimationAircraft(CustomGiftBean mSendGiftBean) {
        if (!isFireworkGiftAnimationPlayEnd) {
            return;
        }
        isFireworkGiftAnimationPlayEnd = false;
        final ImageView image = new ImageView(context);
        framesSequenceAnimation = new FramesSequenceAnimation(context, image,
                R.array.aircraft_icons, 30);
        framesSequenceAnimation.setOneShot(true);
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)); // 设置图片宽高
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        mRoot.addView(image);
        framesSequenceAnimation.start();
        framesSequenceAnimation
                .setFramesSequenceAnimationListener(new FramesSequenceAnimation.FramesSequenceAnimationListener() {

                    @Override
                    public void AnimationStopped() {
                        // TODO Auto-generated method stub
                        if (mRoot == null) return;
                        messageQueueList.remove(0);
                        mRoot.removeView(image);
                        isFireworkGiftAnimationPlayEnd = true;
                        if (messageQueueList.size() > 0) {
                            Animation(messageQueueList.get(0));
                        } else {
                            isFirst = false;

                        }


                    }

                    @Override
                    public void AnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                });

    }

    /**
     * @dw 跑车
     * @author 张亚楠
     */
    protected void showFireworksAnimationCar(CustomGiftBean mSendGiftBean) {
        if (!isFireworkGiftAnimationPlayEnd) {
            return;
        }
        isFireworkGiftAnimationPlayEnd = false;
        final ImageView image = new ImageView(context);
        framesSequenceAnimation = new FramesSequenceAnimation(context, image,
                R.array.car_icons, 30);
        framesSequenceAnimation.setOneShot(true);
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)); // 设置图片宽高
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        mRoot.addView(image);
        framesSequenceAnimation.start();
        framesSequenceAnimation
                .setFramesSequenceAnimationListener(new FramesSequenceAnimation.FramesSequenceAnimationListener() {

                    @Override
                    public void AnimationStopped() {
                        // TODO Auto-generated method stub
                        if (mRoot == null) return;
                        messageQueueList.remove(0);
                        mRoot.removeView(image);
                        isFireworkGiftAnimationPlayEnd = true;
                        if (messageQueueList.size() > 0) {
                            Animation(messageQueueList.get(0));
                        } else {
                            isFirst = false;

                        }


                    }

                    @Override
                    public void AnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                });

    }

    /**
     * @dw 烟花
     * @author 张亚楠
     */
    protected void showFireworksAnimationFireworks(CustomGiftBean mSendGiftBean) {
        if (!isFireworkGiftAnimationPlayEnd) {
            return;
        }
        isFireworkGiftAnimationPlayEnd = false;
        final ImageView image = new ImageView(context);
        framesSequenceAnimation = new FramesSequenceAnimation(context, image,
                R.array.fireworks_icons, 30);
        framesSequenceAnimation.setOneShot(true);
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)); // 设置图片宽高
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        mRoot.addView(image);
        framesSequenceAnimation.start();
        framesSequenceAnimation
                .setFramesSequenceAnimationListener(new FramesSequenceAnimation.FramesSequenceAnimationListener() {

                    @Override
                    public void AnimationStopped() {
                        // TODO Auto-generated method stub
                        if (mRoot == null) return;
                        messageQueueList.remove(0);
                        mRoot.removeView(image);
                        isFireworkGiftAnimationPlayEnd = true;
                        if (messageQueueList.size() > 0) {
                            Animation(messageQueueList.get(0));
                        } else {
                            isFirst = false;

                        }


                    }

                    @Override
                    public void AnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                });

    }


    /**
     * @dw 游轮
     * @author 张亚楠
     */
    protected void showFireworksAnimationSteamer(CustomGiftBean mSendGiftBean) {
        if (!isFireworkGiftAnimationPlayEnd) {
            return;
        }
        isFireworkGiftAnimationPlayEnd = false;
        final ImageView image = new ImageView(context);
        framesSequenceAnimation = new FramesSequenceAnimation(context, image,
                R.array.steamer_icons, 30);
        framesSequenceAnimation.setOneShot(true);
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)); // 设置图片宽高
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        mRoot.addView(image);
        framesSequenceAnimation.start();
        framesSequenceAnimation
                .setFramesSequenceAnimationListener(new FramesSequenceAnimation.FramesSequenceAnimationListener() {

                    @Override
                    public void AnimationStopped() {
                        // TODO Auto-generated method stub
                        if (mRoot == null) return;
                        messageQueueList.remove(0);
                        mRoot.removeView(image);
                        isFireworkGiftAnimationPlayEnd = true;
                        if (messageQueueList.size() > 0) {
                            Animation(messageQueueList.get(0));
                        } else {
                            isFirst = false;

                        }


                    }

                    @Override
                    public void AnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                });

    }
}
