package com.lvshandian.lemeng.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.moudles.mine.my.PayOrderActivity;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, null);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.e("微信支付返回码", resp.errCode + "");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {// (0:正常支付)-----(-1:错误)------(-2：用户取消)
                for (Activity activity : MyApplication.listActivity) {
                    if (activity instanceof PayOrderActivity) {
                        activity.finish();
                    }
                }
                ToastUtils.showMessageDefault(WXPayEntryActivity.this, "支付成功");
            } else if (resp.errCode == -1) {
                ToastUtils.showMessageDefault(WXPayEntryActivity.this, "请求错误,请稍候再试");
            } else {
                ToastUtils.showMessageDefault(WXPayEntryActivity.this, "取消支付");
            }
            finish();

        }
    }
}