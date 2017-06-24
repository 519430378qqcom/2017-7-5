package com.lvshandian.lemeng.moudles.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.moudles.mine.fragment.DepositRecordFragment;
import com.lvshandian.lemeng.moudles.mine.fragment.FloatingRecordFragment;
import com.lvshandian.lemeng.moudles.mine.fragment.WithdrawRecordFragment;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.Bind;

/**
 * 游戏记录
 */
public class GameRecordActivity extends BaseActivity {
    @Bind(R.id.ll_floating_record)
    AutoLinearLayout ll_floating_record;
    @Bind(R.id.ll_deposit_record)
    AutoLinearLayout ll_deposit_record;
    @Bind(R.id.ll_withwraw_record)
    AutoLinearLayout ll_withwraw_record;

    @Bind(R.id.tv_floating_record)
    TextView tv_floating_record;
    @Bind(R.id.tv_deposit_record)
    TextView tv_deposit_record;
    @Bind(R.id.tv_withwraw_record)
    TextView tv_withwraw_record;

    @Bind(R.id.line_floating_record)
    View line_floating_record;
    @Bind(R.id.line_deposit_record)
    View line_deposit_record;
    @Bind(R.id.line_withwraw_record)
    View line_withwraw_record;

    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_recharge)
    TextView tv_recharge;
    @Bind(R.id.text_account_balance)
    TextView text_account_balance;

    @Bind(R.id.my_pager)
    ViewPager my_pager;
    private Fragment[] fragments = {new FloatingRecordFragment(), new DepositRecordFragment(), new WithdrawRecordFragment()};

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
        ll_floating_record.setOnClickListener(this);
        ll_deposit_record.setOnClickListener(this);
        ll_withwraw_record.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
        iv_back.setOnClickListener(this);

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
                restStatus();
                updateBottom(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_floating_record:
                my_pager.setCurrentItem(0);
                break;
            case R.id.ll_deposit_record:
                my_pager.setCurrentItem(1);
                break;
            case R.id.ll_withwraw_record:
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


    private void updateBottom(int position) {
        restStatus();
        switch (position) {
            case 0:
                tv_floating_record.setTextColor(getResources().getColor(R.color.main));
                line_floating_record.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_deposit_record.setTextColor(getResources().getColor(R.color.main));
                line_deposit_record.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_withwraw_record.setTextColor(getResources().getColor(R.color.main));
                line_withwraw_record.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void restStatus() {
        tv_floating_record.setTextColor(Color.BLACK);
        tv_deposit_record.setTextColor(Color.BLACK);
        tv_withwraw_record.setTextColor(Color.BLACK);
        line_floating_record.setVisibility(View.INVISIBLE);
        line_deposit_record.setVisibility(View.INVISIBLE);
        line_withwraw_record.setVisibility(View.INVISIBLE);
    }
}
