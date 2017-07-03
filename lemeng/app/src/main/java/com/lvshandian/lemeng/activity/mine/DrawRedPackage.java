package com.lvshandian.lemeng.activity.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.entity.DrawMonyRatioBean;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.net.RequestCode;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.utils.DecimalUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.TextUtils;

import java.math.RoundingMode;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 支付宝提现
 */
public class DrawRedPackage extends BaseActivity {
    @Bind(R.id.can_draw_money)
    TextView tvCanDraw;
    @Bind(R.id.red_package_money)
    EditText etWantDraw;
    @Bind(R.id.alipay_number)
    EditText etAlipayNumber;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    /**
     * 提现比例
     */
    private String ratio = "1";
    /**
     * 可提现金额
     */
    private String money;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                case RequestCode.DRAW_MONEY:
                    String goldCoin = String.valueOf(Long.valueOf(SharedPreferenceUtils.getGoldCoin(mContext)) - (long) (Double.valueOf(etWantDraw.getText().toString().trim()) * Integer.parseInt(ratio)));
                    SharedPreferenceUtils.saveGoldCoin(mContext, goldCoin);
                    money = DecimalUtils.divideWithRoundingModeAndScale(goldCoin, ratio, RoundingMode.DOWN, 2);
                    tvCanDraw.setText(getString(R.string.rmb, money));
                    showToast(getString(R.string.withdraw_application_succeed));
                    break;
                case RequestCode.DRAW_MONEY_RATIO:
                    DrawMonyRatioBean ratioBean = JsonUtil.json2Bean(json, DrawMonyRatioBean.class);
                    ratio = (Integer.parseInt(ratioBean.getExchangeGold()) / Integer.parseInt(ratioBean.getExchangeAmount())) + "";
                    money = DecimalUtils.divideWithRoundingModeAndScale(SharedPreferenceUtils.getGoldCoin(mContext), ratio, RoundingMode.DOWN, 2);
                    tvCanDraw.setText(getString(R.string.rmb, money));
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_draw_red_package;
    }

    @Override
    protected void initListener() {
        btnConfirm.setOnClickListener(this);
        etWantDraw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etWantDraw.getText().toString().trim())) {
                    if (Double.valueOf(etWantDraw.getText().toString().trim()) > Double.valueOf(money)) {
                        btnConfirm.setEnabled(false);
                        btnConfirm.setBackgroundColor(Color.parseColor("#999999"));
                    } else {
                        btnConfirm.setEnabled(true);
                        btnConfirm.setBackgroundResource(R.drawable.selector_loginbtn_circle);
                    }
                }
            }
        });
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.alipay_withdraw), null);
        drawMoneyRatio();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(etWantDraw.getText().toString().trim())) {
                    showToast(getString(R.string.please_input_withdraw_num));
                } else if (TextUtils.isEmpty(etAlipayNumber.getText().toString().trim())) {
                    showToast(getString(R.string.please_input_alipay_num));
                } else if (Double.valueOf(etWantDraw.getText().toString().trim()) < 0) {
                    showToast(getString(R.string.withdraw_num_must_big_0));
                } else {
                    drawMoney();
                }
                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }

    /**
     * 查询提现比例
     */
    private void drawMoneyRatio() {
        httpDatas.getNewDataCharServer("查询提现比例", true, Request.Method.GET, UrlBuilder.WITHDRAW_RATIO, null, mHandler, RequestCode.DRAW_MONEY_RATIO, TAG);
    }

    /**
     * 提现
     */
    private void drawMoney() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId());
        map.put("exchangeCash", etWantDraw.getText().toString().trim());
        map.put("account", etAlipayNumber.getText().toString().trim());
        httpDatas.getNewDataCharServer("提现信息", true, Request.Method.GET, UrlBuilder.WITHDRAW, map, mHandler, RequestCode.DRAW_MONEY, TAG);
    }

}
