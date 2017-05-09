package com.lvshandian.lemeng.moudles.mine.activity;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.widget.PinchImageView;
import com.lvshandian.lemeng.widget.PinchImageViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 可放大缩小界面，支持传入string字符串和string集合
 * Created by shangshuaibo on 2017/01/06 11:58
 */
public class BigImageActivity extends BaseActivity {
    private DisplayImageOptions thumbOptions = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true).cacheInMemory(true).build();
    private DisplayImageOptions originOptions = new DisplayImageOptions.Builder().build();
    private PinchImageViewPager viewPager;
    private TextView tv_bigImage;
    private List<String> imageList;
    private String imageStr;
    private int clickPosition;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bigimage;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        imageList = getIntent().getStringArrayListExtra("imageList");
        imageStr = getIntent().getStringExtra("imageStr");
        clickPosition = getIntent().getIntExtra("clickPosition", 0);
        initView();
    }

    private void initView() {
        tv_bigImage = (TextView) findViewById(R.id.tv_bigImage);
        viewPager = (PinchImageViewPager) findViewById(R.id.image_viewpager);
        viewPager.setAdapter(new MyPagerAdapter());
        if (imageList != null && imageList.size() > 1) {
            viewPager.setOffscreenPageLimit(imageList.size());
            viewPager.setCurrentItem(clickPosition);
            tv_bigImage.setText((clickPosition + 1) + "/" + imageList.size());
            viewPager.setOnPageChangeListener(new PinchImageViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int
                        positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    tv_bigImage.setText((position + 1) + "/" + imageList.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return (imageList == null) ? 1 : imageList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PinchImageView bigImage = new PinchImageView(BigImageActivity.this);
            bigImage.setOnClickListener(new View.OnClickListener() {
                // ViewPager单独页面设置监听
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            bigImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (imageList == null) {
                ImageLoader.getInstance().displayImage(imageStr, bigImage, thumbOptions);
            } else {
                ImageLoader.getInstance().displayImage(imageList.get(position), bigImage,
                        thumbOptions);

            }
            container.addView(bigImage);
            return bigImage;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            PinchImageView bigImage = (PinchImageView) object;
            if (imageList == null) {
                ImageLoader.getInstance().displayImage(imageStr, bigImage, originOptions);
            } else {
                ImageLoader.getInstance().displayImage(imageList.get(position), bigImage,
                        originOptions);

            }
            viewPager.setMainPinchImageView(bigImage);
        }
    }

}