package com.lvshandian.lemeng.moudles.mine.my;

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
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.DrawMonyRatioBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.utils.DecimalUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
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
    private String ratio = "4000";
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
                    LogUtils.e("提现返回信息: " + json);
                    String goldCoin = String.valueOf(Long.valueOf(SharedPreferenceUtils.getGoldCoin(mContext)) - (long) (Double.valueOf(etWantDraw.getText().toString().trim()) * Integer.parseInt(ratio)));
                    SharedPreferenceUtils.saveGoldCoin(mContext, goldCoin);
                    money = DecimalUtils.divideWithRoundingModeAndScale(goldCoin, ratio, RoundingMode.DOWN, 2);
                    tvCanDraw.setText(money + "元");
                    showToast("提现申请成功!");
                    break;
                case RequestCode.DRAW_MONEY_RATIO:
                    LogUtils.e("提现比例返回信息: " + json);
                    DrawMonyRatioBean ratioBean = JsonUtil.json2Bean(json, DrawMonyRatioBean.class);
                    ratio = (Integer.parseInt(ratioBean.getExchangeGold()) / Integer.parseInt(ratioBean.getExchangeAmount())) + "";
                    money = DecimalUtils.divideWithRoundingModeAndScale(SharedPreferenceUtils.getGoldCoin(mContext), ratio, RoundingMode.DOWN, 2);
                    tvCanDraw.setText(money + "元");
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
                        btnConfirm.setBackgroundResource(R.drawable.shape_bg_btn);
                    }
                }
            }
        });
    }

    @Override
    protected void initialized() {
        initTitle("", "支付宝提现", null);
        drawMoneyRatio();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(etWantDraw.getText().toString().trim())) {
                    showToast("请输入提现金额");
                } else if (TextUtils.isEmpty(etAlipayNumber.getText().toString().trim())) {
                    showToast("请输入支付宝帐号");
                } else if (Double.valueOf(etWantDraw.getText().toString().trim()) < 0) {
                    showToast("提现金额必须大于0");
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
        httpDatas.getNewDataCharServer("查询提现比例", Request.Method.GET, UrlBuilder.drawMoney_ratio, null, mHandler, RequestCode.DRAW_MONEY_RATIO);
    }

    /**
     * 提现
     */
    private void drawMoney() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId());
        map.put("exchangeCash", etWantDraw.getText().toString().trim());
        map.put("account", etAlipayNumber.getText().toString().trim());
        httpDatas.getNewDataCharServer("提现信息", Request.Method.GET, UrlBuilder.drawMoney, map, mHandler, RequestCode.DRAW_MONEY);
    }

}
