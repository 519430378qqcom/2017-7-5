package com.lvshandian.lemeng.activity.mine;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.activity.start.LoginActivity;
import com.lvshandian.lemeng.entity.AppUser;
import com.lvshandian.lemeng.entity.QuitLogin;
import com.lvshandian.lemeng.entity.mine.LoginFrom;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.net.RequestCode;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import com.lvshandian.lemeng.third.wangyiyunxin.config.preference.Preferences;

/**
 * 修改密码
 */
public class ModifyPasswordActivity extends BaseActivity {
    @Bind(R.id.et_old_password)
    EditText etOldPassword;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.et_repeat_password)
    EditText etRepeatPassword;
    @Bind(R.id.btn_save)
    TextView btnSave;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestCode.MODIFY_PASSWORD:
                    showToast(getString(R.string.change_password_succeed));
                    String info = msg.getData().getString(HttpDatas.info);
                    quitLogin();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void initListener() {
        btnSave.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.change_password), null);
    }

    /**
     * 修改密码成功后，退出登录
     */
    private void quitLogin() {
        //清空已保存的用户信息
//        CacheUtils.saveObject(mContext, null, CacheUtils.USERINFO);
        AppUser appUser = new AppUser();
        SharedPreferenceUtils.saveUserInfo(mContext, appUser);
        //发送到MainActivity，关闭页面
        EventBus.getDefault().post(new QuitLogin());
        Preferences.saveAppLogin("0");
        Preferences.saveWyyxLogin("0");
        //开启登录页面
        MyApplication.finishActivity();
        gotoActivity(LoginActivity.class, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
            case R.id.btn_save:
                if (checkInfo()) {
                    saveNewPass();
                }
                break;
        }
    }

    /**
     * 保存新密码
     */
    private void saveNewPass() {
        Map<String, String> params = new HashMap<>();
        params.put("id", appUser.getId());
        params.put("password", etNewPassword.getText().toString().trim());
        httpDatas.getDataForJson("修改密码", true, Request.Method.POST, urlBuilder.MODIFY_PASSWORD, params, handler, RequestCode.MODIFY_PASSWORD, TAG);
    }


    /**
     * 检查信息
     *
     * @return
     */
    private boolean checkInfo() {
//        AppUser userInfo = (AppUser) CacheUtils.readObject(mContext, CacheUtils.USERINFO);
        AppUser userInfo = SharedPreferenceUtils.getUserInfo(mContext);
        if (userInfo != null) {


            String etOldPass = etOldPassword.getText().toString().trim();
            if (TextUtils.isEmpty(etOldPass)) {
                showToast(getString(R.string.old_password_hint));
                return false;
            }
//            LoginFrom from = (LoginFrom) CacheUtils.readObject(this, CacheUtils.PASSWORD);
            LoginFrom from = SharedPreferenceUtils.getLoginFrom(this);
            if (from != null) {
                boolean thirdLogin = from.isThirdLogin();
                if (!thirdLogin) {
                    String oldPassword = from.getPassword();
                    LogUtils.e("oldPassWord: " + oldPassword);
                    LogUtils.e("etOldPass: " + etOldPass);
                    if (!android.text.TextUtils.equals(oldPassword, etOldPass)) {
                        showToast(getString(R.string.old_password_failure));
                        return false;
                    }
                }
            }

            String newPass = etNewPassword.getText().toString().trim();
            String repeatPass = etRepeatPassword.getText().toString().trim();
            if (android.text.TextUtils.isEmpty(newPass)) {
                showToast(getString(R.string.new_password_hint));
                return false;
            }
            if (android.text.TextUtils.isEmpty(repeatPass)) {
                showToast(getString(R.string.again_new_password_hint));
                return false;
            }
            if (!android.text.TextUtils.equals(repeatPass, newPass)) {
                showToast(getString(R.string.password_different_twice));
                return false;
            }
            if (newPass.equals(from.getPassword())) {
                showToast(getString(R.string.new_and_old_password_must_different));
                return false;
            }
        }
        return true;
    }
}
