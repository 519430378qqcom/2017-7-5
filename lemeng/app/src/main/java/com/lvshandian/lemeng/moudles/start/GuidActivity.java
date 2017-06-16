package com.lvshandian.lemeng.moudles.start;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 开机引导页
 */
public class GuidActivity extends BaseActivity {
    @Bind(R.id.contentPager)
    ViewPager contentPager;
    @Bind(R.id.logo)
    ImageView logo;

    private boolean isfirst = false;
    /**
     * 界面列表
     */
    private ArrayList<View> views;

    /**
     * 引导图片资源
     */
    private final int[] pics = {R.mipmap.welcome_1, R.mipmap.welcome_2, R.mipmap.welcome_3};

    private ObjectAnimator anim;

    @Override
    protected void initialized() {
        views = new ArrayList<>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(pics[i]);
            views.add(iv);

        }
        contentPager.setAdapter(new ViewPagerAdapter(views));
    }

    @Override
    protected void initListener() {
        contentPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    views.get(position).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferenceUtils.put(GuidActivity.this, "is_first", false);
                            Intent intent = new Intent(GuidActivity.this, StartActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    if (anim != null) {
                        anim.cancel();
                        logo.clearAnimation();
                    }
                    anim = ObjectAnimator.ofFloat(logo, "alpha", logo.getAlpha(), 0);
                    anim.setDuration(500);
                    anim.start();
                } else if (state == 0) {
                    if (anim != null) {
                        anim.cancel();
                        logo.clearAnimation();
                    }
                    anim = ObjectAnimator.ofFloat(logo, "alpha", logo.getAlpha(), 1);
                    anim.setDuration(500);
                    anim.start();
                }
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_guid_layout;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        isfirst = (boolean) SharedPreferenceUtils.get(this, "is_first", true);
        //非首次登录直接跳过
        if (!isfirst) {
            Intent intent = new Intent(GuidActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {
        /**
         * 界面列表
         */
        private ArrayList<View> views;

        public ViewPagerAdapter(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return (views == null) ? 0 : views.size();
        }

        @Override
        public Object instantiateItem(View view, int position) {
            ((ViewPager) view).addView(views.get(position), 0);
            return views.get(position);
        }

        //判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View view, Object arg1) {
            return (view == arg1);
        }

        //销毁position位置的界面
        @Override
        public void destroyItem(View view, int position, Object arg2) {
            ((ViewPager) view).removeView(views.get(position));
        }
    }


}
