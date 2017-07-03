package com.lvshandian.lemeng.activity.mine;

import android.content.ClipboardManager;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.activity.start.LoginSelectActivity;
import com.lvshandian.lemeng.activity.start.LogoutHelper;
import com.lvshandian.lemeng.entity.AppUser;
import com.lvshandian.lemeng.entity.QuitLogin;
import com.lvshandian.lemeng.entity.mine.LoginFrom;
import com.lvshandian.lemeng.widget.view.RoundDialog;
import com.lvshandian.lemeng.widget.view.ToggleView;
import com.lvshandian.lemeng.utils.FileCacheUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.UMUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import com.lvshandian.lemeng.third.wangyiyunxin.config.preference.Preferences;
import com.lvshandian.lemeng.third.wangyiyunxin.contact.activity.BlackListActivity;
import com.lvshandian.lemeng.third.wangyiyunxin.session.SessionHelper;

/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity {
    @Bind(R.id.copy)
    TextView copy;
    @Bind(R.id.rl_account_safety)
    AutoRelativeLayout rlAccountSafety;
    @Bind(R.id.rl_edit)
    AutoRelativeLayout rlEdit;
    @Bind(R.id.rl_about)
    AutoRelativeLayout rlAbout;
    @Bind(R.id.rl_blacklist)
    AutoRelativeLayout rlblacklist;
    @Bind(R.id.rl_changkong)
    AutoRelativeLayout rlChangkong;
    @Bind(R.id.on_off)
    ToggleView onOff;
    @Bind(R.id.rl_feedback)
    AutoRelativeLayout rlFeedback;
    @Bind(R.id.tv_cache)
    TextView tvCache;
    @Bind(R.id.all_clear_cache)
    AutoRelativeLayout allClearCache;
    @Bind(R.id.btn_quit)
    TextView btnQuit;
    @Bind(R.id.number)
    TextView number;
    @Bind(R.id.ll_account_safety)
    LinearLayout ll_account_safety;
    /**
     * 退出登录对话框
     */
    private RoundDialog mQuitDialog;
    /**
     * 清除缓存对话框
     */
    private RoundDialog mCacheDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initListener() {
        copy.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        allClearCache.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        rlAccountSafety.setOnClickListener(this);
        rlEdit.setOnClickListener(this);
        rlblacklist.setOnClickListener(this);
        rlChangkong.setOnClickListener(this);
        onOff.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.settings), null);
        setCache();
        initQuitDialog();
        initClearCacheDialog();
        initInfo();
        if (appUser.getLoginStatus() != null && appUser.getLoginStatus().equals("1")) {
            ll_account_safety.setVisibility(View.GONE);
        } else {
            ll_account_safety.setVisibility(View.VISIBLE);
        }
    }

    private void initInfo() {
        number.setText(appUser.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
            case R.id.copy: //复制
                String numstr = number.getText().toString().trim();
                ClipboardManager copy = (ClipboardManager) mContext
                        .getSystemService(mContext.CLIPBOARD_SERVICE);
                copy.setText(numstr);
                showToast(getString(R.string.copy_succeed));
                break;
            case R.id.rl_account_safety:  //账号安全
                gotoActivity(ModifyPasswordActivity.class, false);
                break;
            case R.id.rl_edit:  //编辑资料
                startActivity(new Intent(getContext(), SettingPerson.class));
                break;
            case R.id.on_off: //免打扰开关

                break;
            case R.id.rl_blacklist://黑名单
                blackList();
                break;
            case R.id.rl_changkong://我的场控
                startActivity(new Intent(getContext(), MyControllerActivity.class));
                break;
            case R.id.rl_feedback: //意见反馈
                sayTips();
                break;
            case R.id.all_clear_cache: //清除缓存
                if (mCacheDialog != null && !mCacheDialog.isShowing()) {
                    mCacheDialog.show();
                }
                break;
            case R.id.rl_about://关于乐檬
                Intent intent1 = new Intent(mContext, ExplainWebViewActivity.class);
                intent1.putExtra(getString(R.string.web_flag), getString(R.string.about_us));
                startActivity(intent1);
                break;
            case R.id.btn_quit://退出登录
                if (mQuitDialog != null && !mQuitDialog.isShowing()) {
                    mQuitDialog.show();
                }
                break;
        }
    }

    /**
     * 退出登录
     */
    private void quitLogin() {
        logout();
        //清空已保存的用户信息
//        CacheUtils.saveObject(this, null, CacheUtils.PASSWORD);
//        CacheUtils.saveObject(mContext, null, CacheUtils.USERINFO);
        AppUser appUser = new AppUser();
        LoginFrom loginFrom = new LoginFrom();
        SharedPreferenceUtils.saveUserInfo(mContext, appUser);
        SharedPreferenceUtils.saveLoginFrom(mContext, loginFrom);

        //发送到MainActivity，关闭页面
        EventBus.getDefault().post(new QuitLogin());
        //开启登录页面
        gotoActivity(LoginSelectActivity.class, true);
    }

    /**
     * 注销
     */
    private void logout() {
        LogoutHelper.logout();
        removeLoginState();
        NIMClient.getService(AuthService.class).logout();

        UMUtils.removeAlias();
    }

    /**
     * 清除登陆状态
     */
    private void removeLoginState() {
        Preferences.saveUserToken("");
    }


    /**
     * 显示缓存
     */
    private void setCache() {
        String size = FileCacheUtils.getCacheSize(mContext);
        if (size != null && size.equals("0.0Byte")) {
            size = "0KB";
        }
        if (tvCache != null)
            tvCache.setText(size);
    }


    /**
     * 清除缓存
     */
    private void clearCache() {
        FileCacheUtils.cleanApplicationData(mContext);
        setCache();
    }


    /**
     * 意见反馈
     *
     * @author sll
     * @time 2016/12/1 10:12
     */
    private void sayTips() {
        SessionHelper.startP2PSession(this, "miu_1");
    }

    /**
     * 黑名单
     */
    private void blackList() {
        BlackListActivity.start(this);
    }


    /**
     * 确认退出对话框
     */
    private void initQuitDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_quit_login, null);
        mQuitDialog = new RoundDialog(this, view, R.style.dialog, 0.66f, 0.2f);
        mQuitDialog.setCanceledOnTouchOutside(false);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuitDialog != null && mQuitDialog.isShowing()) {
                    mQuitDialog.dismiss();
                }
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuitDialog != null && mQuitDialog.isShowing()) {
                    mQuitDialog.dismiss();
                }
                quitLogin();
            }
        });
    }


    /**
     * 清除缓存对话框
     */
    private void initClearCacheDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_quit_login, null);
        mCacheDialog = new RoundDialog(this, view, R.style.dialog, 0.66f, 0.2f);
        mCacheDialog.setCanceledOnTouchOutside(false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvTitle.setText(getString(R.string.if_clear_cache));
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCacheDialog != null && mCacheDialog.isShowing()) {
                    mCacheDialog.dismiss();
                }
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCacheDialog != null && mCacheDialog.isShowing()) {
                    mCacheDialog.dismiss();
                }
                clearCache();
            }
        });
    }

}
