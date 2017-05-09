package com.lvshandian.lemeng.moudles.start;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.lvshandian.lemeng.MainActivity;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.base.CustomStringCallBackOne;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.moudles.mine.bean.LoginFrom;
import com.lvshandian.lemeng.utils.CacheUtils;
import com.lvshandian.lemeng.utils.DESUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.view.LoadingDialog;
import com.lvshandian.lemeng.wangyiyunxin.config.DemoCache;
import com.lvshandian.lemeng.wangyiyunxin.config.preference.Preferences;
import com.lvshandian.lemeng.wangyiyunxin.config.preference.UserPreferences;
import com.netease.nim.uikit.cache.DataCacheManager;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.squareup.okhttp.MediaType;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;


/**
 * 启动界面 on 2016/10/20.
 */
public class LoginSelectActivity extends BaseActivity {
    @Bind(R.id.iv_facebook_login)
    ImageView facebookLogin;
    @Bind(R.id.iv_twitter_login)
    ImageView twitterLogin;
    @Bind(R.id.iv_google_login)
    ImageView googleLogin;
    @Bind(R.id.iv_wechat_login)
    ImageView wchatLogin;
    @Bind(R.id.iv_moble_login)
    ImageView mobleLogin;

    private UMShareAPI mShareAPI;
    private LoadingDialog mLoading;
    private AbortableFuture<LoginInfo> loginRequest;
    private String account = null;
    private String token = null;
    public static final String KICK_OUT = "KICK_OUT";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_login;
    }

    @Override
    protected void initialized() {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        mLoading = new LoadingDialog(this);
        onParseIntent();

        mShareAPI = UMShareAPI.get(mContext);
    }

    @Override
    protected void initListener() {
        facebookLogin.setOnClickListener(this);
        twitterLogin.setOnClickListener(this);
        googleLogin.setOnClickListener(this);
        wchatLogin.setOnClickListener(this);
        mobleLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_facebook_login:

                break;
            case R.id.iv_twitter_login:

                break;
            case R.id.iv_google_login:

                break;
            case R.id.iv_wechat_login:
                weChatLogin();
                break;
            case R.id.iv_moble_login:
                gotoActivity(LoginActivity.class, false);
                break;

        }

    }


    /**
     * QQ登录
     */
    private void qqLogin() {
        thirdShouQuan(SHARE_MEDIA.QQ);
    }

    /**
     * 微信登录
     */
    private void weChatLogin() {
        thirdShouQuan(SHARE_MEDIA.WEIXIN);
    }

    /**
     * 请求第三方授权
     *
     * @param platForm
     */
    private void thirdShouQuan(final SHARE_MEDIA platForm) {
        mShareAPI.getPlatformInfo(LoginSelectActivity.this, platForm, umAuthListener);
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

            if (map == null || map.size() == 0) {
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
                showToast("授权失败");
                return;
            }

            final Map<String, String> params = new HashMap<>();
            params.clear();
            LogUtils.e("授权登录返回数据: ", map.toString());
            if (platForm == SHARE_MEDIA.WEIXIN) {
                params.put("unionId", map.get("unionid").toString());
                params.put("password", map.get("accessToken").toString());
                params.put("userName", map.get("openid").toString());
                params.put("nickName", map.get("screen_name").toString());
                params.put("picUrl", map.get("profile_image_url").toString());
                if (map.get("gender").equals("男")) {
                    params.put("gender", "1");
                } else {
                    params.put("gender", "0");
                }
            } else if (platForm == SHARE_MEDIA.QQ) {
                params.put("unionId", map.get("uid").toString());
                params.put("userName", map.get("uid").toString());
                params.put("password", map.get("accessToken").toString());
                params.put("nickName", map.get("screen_name").toString());
                params.put("picUrl", map.get("profile_image_url").toString());
                if (map.get("gender").equals("男")) {
                    params.put("gender", "1");
                } else {
                    params.put("gender", "0");
                }
            }
            thirdLogin(params);
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
     * 第三方登录
     *
     * @param params 请求参数,用于生成请求实体 json
     */
    private void thirdLogin(Map<String, String> params) {
        if (params != null) {
            JSONObject jsonObject = new JSONObject(params);
            String json = jsonObject.toString();
            LogUtils.e("json: " + json);
            OkHttpUtils.postString()
                    .url(UrlBuilder.chargeServerUrl + UrlBuilder.openRegister)
                    .addHeader("udid", "haiyan")
                    .mediaType(MediaType.parse("application/json;charset=UTF-8"))
                    .content(json)
                    .build().
                    execute(new CustomStringCallBackOne(mContext, HttpDatas.KEY_CODE_SUCCESS) {
                        @Override
                        public void onFaild() {
                            showToast("登录失败");
                        }

                        @Override
                        public void onSucess(String data) {
                            LogUtils.e("第三方登录: " + data);
                            loginSucess(data);
                            LoginFrom loginFrom = new LoginFrom();
                            loginFrom.setThirdLogin(true);
                            loginFrom.setPassword("");
                            CacheUtils.saveObject(mContext, loginFrom, CacheUtils.PASSWORD);
                        }
                    });
        }
    }


    private void onParseIntent() {
        if (getIntent().getBooleanExtra(KICK_OUT, false)) {
            int type = NIMClient.getService(AuthService.class).getKickedClientType();
            String client;
            switch (type) {
                case ClientType.Web:
                    client = "网页端";
                    break;
                case ClientType.Windows:
                    client = "电脑端";
                    break;
                case ClientType.REST:
                    client = "服务端";
                    break;
                default:
                    client = "移动端";
                    break;
            }
            EasyAlertDialogHelper.showOneButtonDiolag(mContext, getString(R.string.kickout_notify),
                    String.format(getString(R.string.kickout_content), client), getString(R.string.ok), true, null);
        }
    }

    /**
     * 登录成功
     *
     * @param json
     */
    private void loginSucess(String json) {
        appUser = JSON.parseObject(json, AppUser.class);
        //存储用户信息
        CacheUtils.saveObject(mContext, appUser, CacheUtils.USERINFO);
        SharedPreferenceUtils.saveGoldCoin(mContext, appUser.getGoldCoin());
        loginWangYi();
    }


    /**
     * 登录网易云信
     *
     * @author sll
     * @time 2016/11/16 13:39
     */
    private void loginWangYi() {
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        try {
            account = DESUtil.decrypt(appUser.getNeteaseAccount());
            token = DESUtil.decrypt(appUser.getNeteaseToken());
            LogUtils.i("WangYi", "account:" + account + "\ntoken:" + token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 登录
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtils.i("WangYi", "login success");

                onLoginDone();
                DemoCache.setAccount(account);
                saveLoginInfo(account, token);

                // 初始化消息提醒
                NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                // 初始化免打扰
                if (UserPreferences.getStatusConfig() == null) {
                    UserPreferences.setStatusConfig(DemoCache.getNotificationConfig());
                }
                NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());

                // 构建缓存
                DataCacheManager.buildDataCacheAsync();

                // 进入主界面
                gotoActivity(MainActivity.class, true);
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    showToast(R.string.login_failed);
                } else {
                    showToast("登录失败: " + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                showToast(R.string.login_exception);
                onLoginDone();
            }
        });
    }

    private void onLoginDone() {
        loginRequest = null;
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
        Preferences.saveAppLogin("1");
        Preferences.saveWyyxLogin("1");
    }
}
