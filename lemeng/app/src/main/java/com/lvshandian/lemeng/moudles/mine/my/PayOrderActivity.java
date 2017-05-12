package com.lvshandian.lemeng.moudles.mine.my;

import android.content.Intent;
import android.view.View;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.moudles.mine.activity.ExplainWebViewActivity;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.Bind;

/**
 * 支付界面
 * Created by shang on 2017/3/27.
 */
public class PayOrderActivity extends BaseActivity {

    @Bind(R.id.ll_aliPay)
    AutoLinearLayout ll_aliPay;
    @Bind(R.id.ll_wchatPay)
    AutoLinearLayout ll_wchatPay;
    @Bind(R.id.ll_yinlian)
    AutoLinearLayout ll_yinlian;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_order;
    }

    @Override
    protected void initListener() {
        ll_aliPay.setOnClickListener(this);
        ll_wchatPay.setOnClickListener(this);
        ll_yinlian.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", "选择支付", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_aliPay:
                showToast("暂缓开通");
                break;
            case R.id.ll_wchatPay:
                showToast("暂缓开通");
                break;
            case R.id.ll_yinlian:
                String url = String.format(UrlBuilder.YINLIAN_PAY_WEB, appUser.getId());
                Intent intent = new Intent(mContext, ExplainWebViewActivity.class);
                intent.putExtra("flag", 1000);
                startActivity(intent);
                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }

}
