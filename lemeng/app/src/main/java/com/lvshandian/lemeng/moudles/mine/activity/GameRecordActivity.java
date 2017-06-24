package com.lvshandian.lemeng.moudles.mine.activity;

import android.content.Intent;
import android.graphics.Matrix;
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
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.moudles.mine.fragment.DepositRecordFragment;
import com.lvshandian.lemeng.moudles.mine.fragment.FloatingRecordFragment;
import com.lvshandian.lemeng.moudles.mine.fragment.WithdrawRecordFragment;

import butterknife.Bind;

/**
 * 游戏记录
 */
public class GameRecordActivity extends BaseActivity {
    @Bind(R.id.tv_floating_record)
    TextView tv_floating_record;
    @Bind(R.id.tv_deposit_record)
    TextView tv_deposit_record;
    @Bind(R.id.tv_withwraw_record)
    TextView tv_withwraw_record;
    @Bind(R.id.first_line)
    RelativeLayout first_line;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_recharge)
    TextView tv_recharge;
    @Bind(R.id.text_account_balance)
    TextView text_account_balance;
    @Bind(R.id.my_pager)
    ViewPager my_pager;
    private int[] ids = {R.id.tv_floating_record, R.id.tv_deposit_record, R.id.tv_withwraw_record};
    private Fragment[] fragments = {new FloatingRecordFragment(), new DepositRecordFragment(), new WithdrawRecordFragment()};
    private int current_index = 0;
    private int linewidth = 0;//移动距离

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_game;
    }


    @Override
    protected void initialized() {
        text_account_balance.setText(appUser.getGoldCoin());
    }

    @Override
    protected void initListener() {
        tv_floating_record.setOnClickListener(this);
        tv_deposit_record.setOnClickListener(this);
        tv_withwraw_record.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        initLine();

        my_pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                return fragments.length;
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
                anim(position);
                for (int i = 0; i < ids.length; i++) {
                    if (position == i) {
                        ((TextView) findViewById(ids[i])).setTextColor(getResources().getColor(R.color.main));
                    } else {
                        ((TextView) findViewById(ids[i])).setTextColor(getResources().getColor(R.color.tv_color3));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_floating_record:
                my_pager.setCurrentItem(0);
                break;
            case R.id.tv_deposit_record:
                my_pager.setCurrentItem(1);
                break;
            case R.id.tv_withwraw_record:
                my_pager.setCurrentItem(2);
                break;
            case R.id.tv_recharge:
                startActivity(new Intent(mContext, ChargeCoinsActivity.class));
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    /**
     * 初始化页签下划线
     */
    private void initLine() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //获取屏幕宽度
        int screenWidth = dm.widthPixels;
        Matrix matrix = new Matrix();
        linewidth = screenWidth / fragments.length;
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

}
