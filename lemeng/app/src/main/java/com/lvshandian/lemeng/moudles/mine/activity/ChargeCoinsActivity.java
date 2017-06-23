package com.lvshandian.lemeng.moudles.mine.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.Bind;

/**
 * 颜票充值界面
 */
public class ChargeCoinsActivity extends BaseActivity {

    @Bind(R.id.ll_one)
    AutoLinearLayout llOne;
    @Bind(R.id.ll_two)
    AutoLinearLayout llTwo;
    @Bind(R.id.ll_three)
    AutoLinearLayout llThree;
    @Bind(R.id.ll_for)
    AutoLinearLayout llFor;
    @Bind(R.id.ll_five)
    AutoLinearLayout llFive;
    @Bind(R.id.ll_sex)
    AutoLinearLayout llSex;
    @Bind(R.id.btn_cz)
    TextView btnCz;
    @Bind(R.id.lepiao)
    TextView tvLp;
    /**
     * 初始充值金额
     */
    private String money = "6";
    /**
     * 颜票数量
     */
    private String lepiao;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_coins;
    }

    @Override
    protected void initListener() {

        llOne.setOnClickListener(this);
        llTwo.setOnClickListener(this);
        llThree.setOnClickListener(this);
        llFor.setOnClickListener(this);
        llFive.setOnClickListener(this);
        llSex.setOnClickListener(this);
        btnCz.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.top_up_recharge), null);
        lepiao = getIntent().getStringExtra("lepiao");
        tvLp.setText(lepiao);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_one:
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "6";
                break;
            case R.id.ll_two:
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "30";
                break;
            case R.id.ll_three:
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "98";
                break;
            case R.id.ll_for:
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "298";
                break;
            case R.id.ll_five:
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "588";
                break;
            case R.id.ll_sex:
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                money = "1598";
                break;
            //充值
            case R.id.btn_cz:
                showToast(getString(R.string.stay_open));
                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }

}
