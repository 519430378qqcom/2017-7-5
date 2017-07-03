package com.lvshandian.lemeng.fragment;

import android.content.Intent;
import android.graphics.Matrix;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.AddressSelectActivity;
import com.lvshandian.lemeng.activity.SearchActivity;
import com.lvshandian.lemeng.interfaces.ResultListener;
import com.lvshandian.lemeng.entity.lemeng.Args;
import com.lvshandian.lemeng.utils.Constant;
import com.lvshandian.lemeng.utils.LocationUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 首页Fragment页面
 * Created by shang on 2017/4/14.
 */
public class IndexPagerFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.first_line)
    RelativeLayout first_line;
    @Bind(R.id.my_pager)
    ViewPager my_pager;
    @Bind(R.id.tv_new)
    TextView tv_new;
    @Bind(R.id.tv_hot)
    TextView tv_hot;
    @Bind(R.id.tv_address)
    TextView tv_address;
    @Bind(R.id.iv_search)
    ImageView iv_search;
    @Bind(R.id.iv_address)
    ImageView iv_address;

    private int[] ids = {R.id.tv_new, R.id.tv_hot, R.id.tv_address};
    private Fragment[] fragments = {new NewsFragment(), new HotFragment(), new AddressFragment()};

    private int current_index = 0;
    private static int linewidth = 0;//移动距离

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void initialized() {
        EventBus.getDefault().register(this);
        tv_address.setText((String) SharedPreferenceUtils.get(mContext, Constant.ADDRESS, "北京"));
    }

    @Override
    protected void initListener() {
        iv_search.setOnClickListener(this);
        tv_new.setOnClickListener(this);
        tv_hot.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        initView();
        initLine();
    }

    private void initView() {
        my_pager.setAdapter(new FragmentPagerAdapter(this.getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                for (int i = 0; i < fragments.length; i++) {
                    if (position == i) {
                        return fragments[i];
                    }
                }
                return null;
            }

            @Override
            public int getCount() {
                return ids.length;
            }
        });

        my_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            /**
             * pager页数发生变化后执行
             * @param position 最后停留的位置
             */
            @Override
            public void onPageSelected(int position) {
                setPager(position);
                anim(position);
                for (int i = 0; i < ids.length; i++) {
                    if (position == i) {
                        ((TextView) view.findViewById(ids[i])).setTextColor(getResources().getColor(R
                                .color.main));
                    } else {
                        ((TextView) view.findViewById(ids[i])).setTextColor(getResources().getColor(R
                                .color.tv_color6));
                    }

                    if (position == 2) {
                        iv_address.setVisibility(View.VISIBLE);
                    } else {
                        iv_address.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化页签下划线
     */
    private void initLine() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //获取屏幕宽度
        int screenWidth = dm.widthPixels;
        Matrix matrix = new Matrix();
        linewidth = screenWidth / 5;
        matrix.postTranslate(0, 0);
    }

    /**
     * 页签下划线移动动画
     *
     * @param index
     */
    private void anim(int index) {
        Animation animation = new TranslateAnimation(linewidth * current_index, linewidth * index, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(200);
        first_line.startAnimation(animation);
        current_index = index;
    }


    /**
     * 别的界面通过下标选择跳转Fragment
     *
     * @param index Fragment的下标
     */
    public void setPager(int index) {
        my_pager.setCurrentItem(index);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                //搜索
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.tv_new:
                my_pager.setCurrentItem(0);
                break;
            case R.id.tv_hot:
                my_pager.setCurrentItem(1);
                break;
            case R.id.tv_address:
                if (my_pager.getCurrentItem() == 2) {
                    startActivity(new Intent(mContext, AddressSelectActivity.class).putExtra("only", false));
                } else {
                    my_pager.setCurrentItem(2);
                }
                break;
        }
    }

    @Subscribe //接受发送来的地理位置
    public void onEventMainThread(Args args) {
        if (args.getGender() == 10) {
            if (!TextUtils.isEmpty(args.getCity())) {
                tv_address.setText(args.getCity());
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * MainActivity定位成功后发送到这里
     *
     * @param location
     */
    @Subscribe
    public void onEventMainThread(Location location) {
        if (location != null) {
            LocationUtils.newInstance().getCityByLocation(getContext(), location, new ResultListener() {
                @Override
                public void onSucess(String data) {
                }

                @Override
                public void onFaild() {

                }
            });
        }
    }
}
