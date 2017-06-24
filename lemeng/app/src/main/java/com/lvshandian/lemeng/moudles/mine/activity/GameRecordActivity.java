package com.lvshandian.lemeng.moudles.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
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

    @Bind(R.id.tv_recharge)
    TextView tv_recharge;

    private int index = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_game;
    }


    @Override
    protected void initialized() {
        initFragments();

    }

    @Override
    protected void initListener() {
        ll_floating_record.setOnClickListener(this);
        ll_deposit_record.setOnClickListener(this);
        ll_withwraw_record.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.ll_floating_record:
                if (index == 0)
                    return;
                index = 0;
                ft.replace(R.id.game_frame, new FloatingRecordFragment());
                break;
            case R.id.ll_deposit_record:
                if (index == 1)
                    return;
                index = 1;
                ft.replace(R.id.game_frame, new DepositRecordFragment());
                break;
            case R.id.ll_withwraw_record:
                if (index == 2)
                    return;
                index = 2;
                ft.replace(R.id.game_frame, new WithdrawRecordFragment());
                break;
            case R.id.tv_recharge:
                startActivity(new Intent(mContext, ChargeCoinsActivity.class));
                break;
        }
        ft.commit();
        updateBottom(index);
    }

    private void initFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.game_frame, new FloatingRecordFragment());
        ft.commit();
        updateBottom(index);
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
