package com.lvshandian.lemeng.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.entity.AppUser;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.net.RequestCode;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.utils.CountUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 我的乐票界面
 */
public class MyCoinsActicity extends BaseActivity {
    @Bind(R.id.tv_my_coins)
    TextView myCoins;
    @Bind(R.id.tv_red_Package)
    TextView mytvRedPackage;
    @Bind(R.id.tv_receive)
    TextView tv_Rreceive;
    @Bind(R.id.tv_recharge)
    TextView tv_recharge;

    private String exchangeStatus;//1 绑定微信公众号  0未绑定

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                case RequestCode.SELECT_USER:
                    AppUser mAppUser = JsonUtil.json2Bean(json, AppUser.class);
//                    CacheUtils.saveObject(mContext, mAppUser, CacheUtils.USERINFO);
                    SharedPreferenceUtils.saveUserInfo(mContext, mAppUser);
                    String myCoin = mAppUser.getGoldCoin();
                    SharedPreferenceUtils.saveGoldCoin(mContext, myCoin);
                    myCoin = CountUtils.getCount(Long.parseLong(myCoin));
                    myCoins.setText(myCoin);
                    mytvRedPackage.setText(getString(R.string.rmb, mAppUser.getExchangeCash()));
                    exchangeStatus = mAppUser.getExchangeStatus();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_coins;
    }

    @Override
    protected void initListener() {
        tv_recharge.setOnClickListener(this);
        tv_Rreceive.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.my_lepiao), null);
        String myCoin = appUser.getGoldCoin();
        myCoin = CountUtils.getCount(Long.parseLong(myCoin));
        myCoins.setText(myCoin);
        mytvRedPackage.setText(getString(R.string.rmb, appUser.getExchangeCash()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUser();
    }

    /**
     * 请求用户信息
     */
    private void initUser() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId());
        httpDatas.getNewDataCharServerCode1("查询用户信息", false, Request.Method.POST, UrlBuilder.SELECT_USER_INFO, map, mHandler, RequestCode.SELECT_USER, TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_receive:
                startActivity(new Intent(mContext, WithdrawActivity.class).putExtra("exchangeStatus", exchangeStatus));
                break;
            case R.id.tv_recharge:
//                Intent intent = new Intent(mContext, ExplainWebViewActivity.class);
//                intent.putExtra(getString(R.string.web_intent_flag), 1000);
//                startActivity(intent);
                startActivity(new Intent(mContext, ChargeCoinsActivity.class));
//                showToast("暂时不支持充值");
                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }

}
