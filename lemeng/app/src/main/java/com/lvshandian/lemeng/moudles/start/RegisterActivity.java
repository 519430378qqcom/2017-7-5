package com.lvshandian.lemeng.moudles.start;

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
import com.lvshandian.lemeng.moudles.mine.my.StateCodeActivity;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.TextPhoneNumber;
import com.lvshandian.lemeng.wangyiyunxin.config.DemoCache;
import com.lvshandian.lemeng.wangyiyunxin.config.preference.Preferences;
import com.lvshandian.lemeng.wangyiyunxin.config.preference.UserPreferences;
import com.netease.nim.uikit.cache.DataCacheManager;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.ed_register_phone)
    EditText edRegisterPhone;
    @Bind(R.id.ed_register_code)
    EditText edRegisterCode;
    @Bind(R.id.tv_send_code)
    TextView tvSendCode;
    @Bind(R.id.ed_register_password)
    EditText edRegisterPassword;
    @Bind(R.id.user_agreement)
    LinearLayout userAgreement;
    @Bind(R.id.ll_phone_code)
    LinearLayout llPhoneCode;
    @Bind(R.id.btn_register)
    TextView btnRegister;
    @Bind(R.id.tv_phone_code)
    TextView tvPhoneCode;

    private int waitTime = 60;
    /**
     * 根据type判断是注册界面还是忘记密码界面
     */
    private String type = "";

    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                case RequestCode.REGISTER_TAG:
                    showToast(getString(R.string.register_success));
                    AppUser appUser = JSON.parseObject(json, AppUser.class);
                    //存储用户信息
//                    CacheUtils.saveObject(RegisterActivity.this, appUser, CacheUtils.USERINFO);
                    SharedPreferenceUtils.saveUserInfo(mContext, appUser);
                    loginWangYi(appUser);

//                    startActivity(new Intent(RegisterActivity.this, SettingPerson.class).putExtra("isRegister", "register"));
                    break;
                case RequestCode.FORGETPSWD_TAG:
                    showToast(getString(R.string.edit_password_success));
                    AppUser appUser1 = JSON.parseObject(json, AppUser.class);
                    //存储用户信息
//                    CacheUtils.saveObject(RegisterActivity.this, appUser1, CacheUtils.USERINFO);
                    SharedPreferenceUtils.saveUserInfo(mContext, appUser1);
                    finish();
                    break;

                case RequestCode.GET_CODE:
                    showToast(getString(R.string.validate_code_sent));
                    break;

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initListener() {
        llPhoneCode.setOnClickListener(this);
        userAgreement.setOnClickListener(this);
        tvSendCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }


    @Override
    protected void initialized() {
        type = getIntent().getStringExtra(getString(R.string.register_flag));
        if (getString(R.string.register).equals(type)) {
            initTitle("", getString(R.string.register), "");
            userAgreement.setVisibility(View.VISIBLE);
            btnRegister.setText(getString(R.string.register));
        } else {
            initTitle("", getString(R.string.retrieve_password), "");
            userAgreement.setVisibility(View.GONE);
            btnRegister.setText(getString(R.string.retrieve_password));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
            case R.id.ll_phone_code:
                Intent intent = new Intent(mContext, StateCodeActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.user_agreement:
                Intent intent1 = new Intent(mContext, ExplainWebViewActivity.class);
                intent1.putExtra(getString(R.string.web_flag), getString(R.string.user_agreement));
                startActivity(intent1);
                break;
            case R.id.tv_send_code:
                sendCode();
                break;
            case R.id.btn_register:
                String str = tvPhoneCode.getText().toString();
                str = str.substring(str.lastIndexOf("+") + 1, str.length());
                String phone = str + edRegisterPhone.getText().toString();

                String registerCode = edRegisterCode.getText().toString();
                String pwd = edRegisterPassword.getText().toString();
                if (TextUtils.isEmpty(edRegisterPhone.getText().toString()) || edRegisterPhone.getText().toString().length() != 11 || !TextPhoneNumber.isPhone(edRegisterPhone.getText().toString())) {
                    showToast(getString(R.string.input_right_phone));
                    return;
                }
                if (TextUtils.isEmpty(registerCode)) {
                    showToast(getString(R.string.input_right_validate));
                    return;
                }
                if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 16) {
                    showToast(getString(R.string.input_right_password));
                    return;
                }
                if (getString(R.string.register).equals(type)) {
                    register(phone, pwd, registerCode);
                } else {
                    forgetPswd(phone, pwd, registerCode);
                }
                break;
        }

    }

    /**
     * 注册
     */
    private void register(String name, String pass, String code) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("userName", name);
        map.put("password", pass);
        map.put("identityCode", code);
        httpDatas.getNewDataCharServer("注册", Request.Method.POST, UrlBuilder.REGISTER, map, mHandler2, RequestCode.REGISTER_TAG);
    }

    /**
     * 忘记密码
     */
    private void forgetPswd(String name, String pass, String code) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("userName", name);
        map.put("password", pass);
        map.put("identityCode", code);
        httpDatas.DataJsonAdmin("修改密码", Request.Method.POST, UrlBuilder.FORGET_PASSWORD, map, mHandler2, RequestCode.FORGETPSWD_TAG);
    }


    /**
     * 发送验证码
     */
    private void sendCode() {
        // 给request赋一个TAG，以便于取消时候使用
        String phone = edRegisterPhone.getText().toString();
        if (!phone.equals("") && phone.length() == 11 && TextPhoneNumber.isPhone(phone)) {
            tvSendCode.setEnabled(false);
            tvSendCode.setTextColor(getContext().getResources().getColor(R.color.gray));
            tvSendCode.setText(waitTime + "s");
            handler.postDelayed(runnable, 1000);

            String str = tvPhoneCode.getText().toString();
            str = str.substring(str.lastIndexOf("+") + 1, str.length());
            phone = str + edRegisterPhone.getText().toString();

            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
            map.put("mobile", phone);
            httpDatas.getNewDataCharServer("获取验证码", Request.Method.GET, UrlBuilder.GET_CODE, map, mHandler2, RequestCode.GET_CODE);

        } else {
            showToast(getString(R.string.input_right_phone));
        }

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            waitTime--;
            tvSendCode.setText(waitTime + "s");
            tvSendCode.setTextColor(getContext().getResources().getColor(R.color.gray));
            if (waitTime == 0) {
                handler.removeCallbacks(runnable);
                tvSendCode.setText(getString(R.string.sent_validate));
                tvSendCode.setTextColor(getContext().getResources().getColor(R.color.main));
                tvSendCode.setEnabled(true);
                waitTime = 60;
                return;
            }
            handler.postDelayed(this, 1000);

        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


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


    private AbortableFuture<LoginInfo> loginRequest;
    private String account = null;
    private String token = null;

    /**
     * 登录网易云信
     *
     * @author sll
     * @time 2016/11/16 13:39
     */
    private void loginWangYi(final AppUser appUser) {
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        account = appUser.getNeteaseAccount();
        token = appUser.getNeteaseToken();
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
                // 进入主界面
                MyApplication.finishActivity();
                gotoActivity(MainActivity.class, true);
                sendUserToWangYi(appUser);
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    showToast(R.string.login_error);
                } else {
                    showToast(getString(R.string.login_failure) + code);
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

    /**
     * 发送修改后的信息给网易云信
     */
    private void sendUserToWangYi(AppUser appUser) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.Name, appUser.getNickName());
        fields.put(UserInfoFieldEnum.AVATAR, appUser.getPicUrl());
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {

                    @Override
                    public void onResult(int code, Void aVoid, Throwable throwable) {
                        if (code == ResponseCode.RES_SUCCESS) {
                        } else {
                        }
                    }
                });
    }
}
