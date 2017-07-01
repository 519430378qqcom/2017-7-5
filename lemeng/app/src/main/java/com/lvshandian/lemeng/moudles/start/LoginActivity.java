package com.lvshandian.lemeng.moudles.start;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.lvshandian.lemeng.MainActivity;
import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.activity.ExplainWebViewActivity;
import com.lvshandian.lemeng.moudles.mine.bean.LoginFrom;
import com.lvshandian.lemeng.moudles.mine.my.SettingHeadAndNick;
import com.lvshandian.lemeng.moudles.mine.my.StateCodeActivity;
import com.lvshandian.lemeng.utils.DESUtil;
import com.lvshandian.lemeng.utils.MD5Utils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.UMUtils;
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

import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.ed_login_phone)
    EditText etLoginPhone;
    @Bind(R.id.ed_login_password)
    EditText etLoginPassword;
    @Bind(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @Bind(R.id.btn_login)
    TextView btnLogin;
    @Bind(R.id.user_agreement)
    LinearLayout userAgreement;
    @Bind(R.id.ll_phone_code)
    LinearLayout llPhoneCode;
    @Bind(R.id.tv_phone_code)
    TextView tvPhoneCode;

    private AbortableFuture<LoginInfo> loginRequest;
    private String account = null;
    private String token = null;
    public static final String KICK_OUT = "KICK_OUT";

    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                //用户登录成功
                case RequestCode.LOGIN_TAG:
                    loginSucess(json);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initListener() {
        tvForgetPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        userAgreement.setOnClickListener(this);
        llPhoneCode.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.login), getString(R.string.register));
        //请求基础权限，存储
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        onParseIntent();
    }


    /**
     * 登录成功
     *
     * @param json
     */
    private void loginSucess(String json) {
        AppUser appUser = JSON.parseObject(json, AppUser.class);
        String passWord = etLoginPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(passWord)) {
            passWord = MD5Utils.md5(passWord);
            appUser.setPassword(passWord);
        }
        //存储用户信息
//        CacheUtils.saveObject(LoginActivity.this, appUser, CacheUtils.USERINFO);
        SharedPreferenceUtils.saveUserInfo(mContext, appUser);
        loginWangYi(appUser);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_phone_code:
                Intent intent = new Intent(mContext, StateCodeActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.user_agreement:
                Intent intent1 = new Intent(mContext, ExplainWebViewActivity.class);
                intent1.putExtra(getString(R.string.web_flag), getString(R.string.user_agreement));
                startActivity(intent1);
                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
            case R.id.tv_titlebar_right:
                startActivity(new Intent(mContext, RegisterActivity.class).putExtra(getString(R.string.register_flag), getString(R.string.register)));
                break;
            case R.id.btn_login:
//                if (TextUtils.isEmpty(etLoginPhone.getText().toString().trim()) || !TextPhoneNumber.isPhone(etLoginPhone.getText().toString())) {
                if (TextUtils.isEmpty(etLoginPhone.getText().toString().trim())) {
                    showToast(getString(R.string.user_name_error));
                    return;
                }
                if (TextUtils.isEmpty(etLoginPassword.getText().toString().trim())) {
                    showToast(getString(R.string.input_right_password));
                    return;
                }
                String phone = tvPhoneCode.getText().toString();
                phone = phone.substring(phone.lastIndexOf("+") + 1, phone.length()) + etLoginPhone.getText().toString();
                login(phone, etLoginPassword.getText().toString().trim());
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(mContext, RegisterActivity.class).putExtra(getString(R.string.register_flag), getString(R.string.retrieve_password)));
                break;
        }

    }

    /**
     * 登录
     */
    private void login(String name, String pass) {
        LoginFrom loginFrom = new LoginFrom();
        loginFrom.setThirdLogin(false);
        loginFrom.setPassword(pass);
//        CacheUtils.saveObject(mContext, loginFrom, CacheUtils.PASSWORD);
        SharedPreferenceUtils.saveLoginFrom(mContext, loginFrom);
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("userName", name);
        map.put("password", pass);
        map.put("differentStatus", "0");
        httpDatas.getDataForJson("登录", true, Request.Method.POST, UrlBuilder.LOGIN, map, mHandler2, RequestCode.LOGIN_TAG, TAG);
    }


    private void onParseIntent() {
        if (getIntent().getBooleanExtra(KICK_OUT, false)) {
            int type = NIMClient.getService(AuthService.class).getKickedClientType();
            String client;
            switch (type) {
                case ClientType.Web:
                    client = getString(R.string.web_terminal);
                    break;
                case ClientType.Windows:
                    client = getString(R.string.computer_terminal);
                    break;
                case ClientType.REST:
                    client = getString(R.string.service_terminal);
                    break;
                default:
                    client = getString(R.string.mobile_terminal);
                    break;
            }
            EasyAlertDialogHelper.showOneButtonDiolag(LoginActivity.this, getString(R.string.kickout_notify),
                    String.format(getString(R.string.kickout_content), client), getString(R.string.ok), true, null);
        }
    }


    /**
     * 登录网易云信
     */
    private void loginWangYi(final AppUser appUser) {
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        try {
            account = DESUtil.decrypt(appUser.getNeteaseAccount());//
            token = DESUtil.decrypt(appUser.getNeteaseToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 登录
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
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
                for (Activity activity : MyApplication.listActivity) {
                    if (activity instanceof LoginSelectActivity) {
                        activity.finish();
                    }
                }
                UMUtils.addExclusiveAlias();

                if (TextUtils.isEmpty(appUser.getPicUrl()) || TextUtils.isEmpty(appUser.getNickName())) {
                    // 进入填写昵称和头像界面
                    gotoActivity(SettingHeadAndNick.class, true);
                } else {
                    // 进入主界面
                    gotoActivity(MainActivity.class, true);
                }
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    showToast(getString(R.string.login_failed));
                } else {
                    showToast(getString(R.string.login_failure) + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                showToast(getString(R.string.login_exception));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getStringExtra("stateCode"))) {
                        tvPhoneCode.setText(data.getStringExtra("stateCode"));
                    }
                }
                break;
        }
    }
}
