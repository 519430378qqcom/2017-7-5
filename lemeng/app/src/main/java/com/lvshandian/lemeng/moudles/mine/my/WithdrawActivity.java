package com.lvshandian.lemeng.moudles.mine.my;

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
import com.lvshandian.lemeng.moudles.mine.activity.WeichatDraw;
import com.lvshandian.lemeng.utils.CacheUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.view.LoadingDialog;
import com.lvshandian.lemeng.view.RoundDialog;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.squareup.okhttp.MediaType;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 提现界面
 * Created by shang on 2017/4/11.
 */
public class WithdrawActivity extends BaseActivity {

    @Bind(R.id.ll_ali)
    AutoLinearLayout ll_ali;
    @Bind(R.id.ll_wchat)
    AutoLinearLayout ll_wchat;

    private String exchangeStatus;//1 绑定微信公众号  0未绑定
    /**
     * 绑定微信公众号dialog
     */
    private RoundDialog bindDialog;
    /**
     * 请求Loading
     */
    private LoadingDialog mLoading;

    private UMShareAPI mShareAPI;
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
                    SharedPreferenceUtils.saveGoldCoin(mContext, mAppUser.getGoldCoin());
                    exchangeStatus = mAppUser.getExchangeStatus();
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_draw_with;
    }

    @Override
    protected void initListener() {
        ll_ali.setOnClickListener(this);
        ll_wchat.setOnClickListener(this);

    }

    @Override
    protected void initialized() {
        initTitle("", "选择提现", null);
        exchangeStatus = getIntent().getStringExtra("exchangeStatus");
        mShareAPI = UMShareAPI.get(mContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_ali:
                /**
                 * 支付宝提现
                 */
                startActivity(new Intent(mContext, DrawRedPackage.class));
                break;
            case R.id.ll_wchat:
                /**
                 * 微信公众号提现
                 */
                if (exchangeStatus != null && exchangeStatus.equals("1")) {
                    //前去
                    startActivity(new Intent(mContext, WeichatDraw.class));
                } else {
                    initBindDialog();
                }
                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }


    /**
     * 请求用户信息
     */
    private void initUser() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId());
        httpDatas.getNewDataCharServerCodeNoLoading("查询用户信息", Request.Method.POST, UrlBuilder.selectUserInfo, map, mHandler, RequestCode.SELECT_USER);
    }

    /**
     * 绑定微信对话框
     */
    private void initBindDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_quit_login, null);
        bindDialog = new RoundDialog(this, view, R.style.dialog, 0.66f, 0.2f);
        bindDialog.setCanceledOnTouchOutside(false);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("绑定微信公众号才能提现,是否绑定");
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindDialog != null && bindDialog.isShowing()) {
                    bindDialog.dismiss();
                }
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindDialog != null && bindDialog.isShowing()) {
                    bindDialog.dismiss();
                }
                mLoading = new LoadingDialog(mContext);
                weixinShouQuan();
            }
        });
        bindDialog.show();
    }


    /**
     * 请求第三方授权
     */
    private void weixinShouQuan() {
        mShareAPI.getPlatformInfo(WithdrawActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            LogUtils.e("微信登录", "is onStart()");
            if (mLoading != null && !mLoading.isShowing()) {
                mLoading.show();
                mLoading.setText("正在获取授权");
            }
        }
        @Override
        public void onComplete(SHARE_MEDIA platForm, int action, Map<String, String> map) {

            if (map==null||map.size()==0){
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
                showToast("授权失败");
                return;
            }
            Map<String, String> params = new HashMap<>();
            params.clear();
            params.put("userId", appUser.getId());
            params.put("userName", map.get("screen_name").toString());
            params.put("openId", map.get("openid").toString());
            params.put("usid", map.get("openid").toString());
            params.put("unionId", map.get("unionid").toString());
            bindWeichat(params);
            if (mLoading != null && mLoading.isShowing()) {
                mLoading.dismiss();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            LogUtils.e("微信登录", "is onError()");
            if (mLoading != null && mLoading.isShowing()) {
                mLoading.dismiss();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            LogUtils.e("微信登录", "is onCancel()");
            if (mLoading != null && mLoading.isShowing()) {
                mLoading.dismiss();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }



    /**
     * 绑定微信公众号
     *
     * @param params
     */
    private void bindWeichat(Map<String, String> params) {
        if (params != null) {
            JSONObject jsonObject = new JSONObject(params);
            String json = jsonObject.toString();
            LogUtils.e("json: " + json);
            OkHttpUtils.postString()
                    .url(UrlBuilder.serverUrl + UrlBuilder.WEICHAT_BIND)
                    .addHeader("udid", "haiyan")
                    .mediaType(MediaType.parse("application/json;charset=UTF-8"))
                    .content(json)
                    .build().
                    execute(new StringCallback() {
                        @Override
                        public void onError(com.squareup.okhttp.Request request, Exception e) {
                            LogUtil.e("绑定微信失败", e.toString());
                        }

                        @Override
                        public void onResponse(String response) {
                            LogUtil.e("绑定微信成功", response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("code").equals("0")) {
                                    showToast("绑定成功!");
                                    initUser();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


}
