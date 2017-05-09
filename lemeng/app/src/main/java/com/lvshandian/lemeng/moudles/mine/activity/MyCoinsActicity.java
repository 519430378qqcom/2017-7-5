package com.lvshandian.lemeng.moudles.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.my.WithdrawActivity;
import com.lvshandian.lemeng.utils.CacheUtils;
import com.lvshandian.lemeng.utils.CountUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 我的乐票界面
 * Created by Administrator on 2017/1/17.
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
                    LogUtils.e("查询个人信息返回json: " + json);
                    AppUser mAppUser = JsonUtil.json2Bean(json, AppUser.class);
                    CacheUtils.saveObject(mContext, mAppUser, CacheUtils.USERINFO);
                    String myCoin = mAppUser.getGoldCoin();
                    SharedPreferenceUtils.saveGoldCoin(mContext,myCoin);
                    myCoin = CountUtils.getCount(Long.parseLong(myCoin));
                    myCoins.setText(myCoin);
                    mytvRedPackage.setText(mAppUser.getExchangeCash() + "元");
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
        initTitle("", "我的乐票", null);
        String myCoin = appUser.getGoldCoin();
        myCoin = CountUtils.getCount(Long.parseLong(myCoin));
        myCoins.setText(myCoin);
        mytvRedPackage.setText(appUser.getExchangeCash() + "元");
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
        httpDatas.getNewDataCharServerCodeNoLoading("查询用户信息", Request.Method.POST, UrlBuilder.selectUserInfo, map, mHandler, RequestCode.SELECT_USER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_receive:
                startActivity(new Intent(mContext, WithdrawActivity.class).putExtra("exchangeStatus",exchangeStatus));
                break;
            case R.id.tv_recharge:
//                startActivity(new Intent(mContext, ChargeCoinsActivity.class).putExtra("yanpiao", appUser.getGoldCoin()));
//                startActivity(new Intent(mContext, PayOrderActivity.class));

//                String url = String.format(UrlBuilder.YINLIAN_PAY_WEB, appUser.getId());
//                Intent intent = new Intent(mContext, ExplainWebViewActivity.class);
//                intent.putExtra("flag", 1000);
//                intent.putExtra("url", url);
//                startActivity(intent);

                startActivity(new Intent(mContext,ExplainWebViewActivity.class));
                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }

}
