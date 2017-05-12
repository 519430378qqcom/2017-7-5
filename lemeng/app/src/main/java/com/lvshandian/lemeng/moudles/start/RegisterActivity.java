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
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.activity.ExplainWebViewActivity;
import com.lvshandian.lemeng.moudles.mine.activity.SettingPerson;
import com.lvshandian.lemeng.moudles.mine.my.StateCodeActivity;
import com.lvshandian.lemeng.utils.CacheUtils;
import com.lvshandian.lemeng.utils.TextPhoneNumber;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 注册页面
 * Created by 张振 on 2016/11/8.
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
    @Bind(R.id.ll_xieyi)
    LinearLayout ll_xieyi;
    @Bind(R.id.ll_quhao)
    LinearLayout ll_quhao;
    @Bind(R.id.btn_register)
    TextView btnRegister;
    @Bind(R.id.tv_quhao)
    TextView tv_quhao;

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
                    showToast("注册成功");
                    AppUser appUser = JSON.parseObject(json, AppUser.class);
                    //存储用户信息
                    CacheUtils.saveObject(RegisterActivity.this, appUser, CacheUtils.USERINFO);
                    startActivity(new Intent(RegisterActivity.this, SettingPerson.class).putExtra("isRegister", "register"));
                    break;
                case RequestCode.FORGETPSWD_TAG:
                    showToast("修改成功,请重新登录");
                    AppUser appUser1 = JSON.parseObject(json, AppUser.class);
                    //存储用户信息
                    CacheUtils.saveObject(RegisterActivity.this, appUser1, CacheUtils.USERINFO);
                    finish();
                    break;

                case RequestCode.GET_CODE:
                    showToast("验证码已发送");
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
        ll_quhao.setOnClickListener(this);
        ll_xieyi.setOnClickListener(this);
        tvSendCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }


    @Override
    protected void initialized() {
        type = getIntent().getStringExtra("type");
        if (type.equals("1")) {
            initTitle("", "注册", "");
            ll_xieyi.setVisibility(View.VISIBLE);
            btnRegister.setText("注册");
        } else {
            initTitle("", "找回密码", "");
            ll_xieyi.setVisibility(View.GONE);
            btnRegister.setText("确认");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
            case R.id.ll_quhao:
                Intent intent = new Intent(mContext, StateCodeActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.ll_xieyi:
                Intent intent1 = new Intent(mContext, ExplainWebViewActivity.class);
                intent1.putExtra("flag", 2000);
                startActivity(intent1);
                break;
            case R.id.tv_send_code:
                sendCode();
                break;
            case R.id.btn_register:
                String str = tv_quhao.getText().toString();
                str = str.substring(str.lastIndexOf("+") + 1, str.length());
                String phone = str + edRegisterPhone.getText().toString();

                String registerCode = edRegisterCode.getText().toString();
                String pwd = edRegisterPassword.getText().toString();
                if (TextUtils.isEmpty(edRegisterPhone.getText().toString()) || edRegisterPhone.getText().toString().length() != 11 || !TextPhoneNumber.isPhone(edRegisterPhone.getText().toString())) {
                    showToast("手机号不正确");
                    return;
                }
                if (TextUtils.isEmpty(registerCode)) {
                    showToast("短信验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pwd) || pwd.length() < 4 || pwd.length() > 16) {
                    showToast("密码不正确，请设置为4-16位字符");
                    return;
                }
                if (type.equals("1")) {
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
        httpDatas.getNewDataCharServer("修改密码", Request.Method.POST, UrlBuilder.forgetPswd, map, mHandler2, RequestCode.FORGETPSWD_TAG);
    }


    /**
     * 发送验证码
     */
    private void sendCode() {
        // 给request赋一个TAG，以便于取消时候使用
        String phone =  edRegisterPhone.getText().toString();
        if (!phone.equals("") && phone.length() == 11 && TextPhoneNumber.isPhone(phone)) {
            tvSendCode.setEnabled(false);
            tvSendCode.setTextColor(getContext().getResources().getColor(R.color.gray));
            tvSendCode.setText(waitTime + "s");
            handler.postDelayed(runnable, 1000);

            String str = tv_quhao.getText().toString();
            str = str.substring(str.lastIndexOf("+") + 1, str.length());
            phone = str + edRegisterPhone.getText().toString();

            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
            map.put("mobile", phone);
            httpDatas.getNewDataCharServer("获取验证码", Request.Method.GET, UrlBuilder.getCode, map, mHandler2, RequestCode.GET_CODE);

        } else {
            showToast("请输入正确手机号");
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
                tvSendCode.setText("发送验证码");
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
                        tv_quhao.setText(data.getStringExtra("stateCode"));
                    }
                }
                break;
        }
    }
}
