package com.lvshandian.lemeng.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.bean.JoinRoomBean;
import com.lvshandian.lemeng.bean.LiveBean;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.bean.QuitApp;
import com.lvshandian.lemeng.bean.QuitLogin;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.moudles.index.live.WatchLiveActivity;
import com.lvshandian.lemeng.moudles.mine.bean.LoginFrom;
import com.lvshandian.lemeng.moudles.start.LoginActivity;
import com.lvshandian.lemeng.moudles.start.LogoutHelper;
import com.lvshandian.lemeng.utils.DESUtil;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.NetWorkUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.view.ShowPop;
import com.lvshandian.lemeng.wangyiyunxin.config.preference.Preferences;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tandong.sa.activity.SmartFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Activity 基类  zhang 2016/11/07
 */
public abstract class BaseActivity extends SmartFragmentActivity implements View.OnClickListener {

    protected View snackView;
    protected Context mContext;
    protected HttpDatas httpDatas;
    protected UrlBuilder urlBuilder;
    protected TextView titlebar_right;
    protected AppUser appUser;
    private LayoutInflater mInflator;
    private LiveListBean live;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e("BaseActivity", getClass().getSimpleName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
        }

        setMiuiStatusBarDarkMode(true, this);
        setMeizuStatusBarDarkIcon(true, this);

        mInflator = LayoutInflater.from(this);
        urlBuilder = new UrlBuilder();
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
            // 删除窗口背景
            ButterKnife.bind(this);
        }
//        appUser = (AppUser) CacheUtils.readObject(this, CacheUtils.USERINFO);
        appUser = SharedPreferenceUtils.getUserInfo(mContext);
        mContext = this;
        snackView = getWindow().getDecorView().getRootView();
        httpDatas = new HttpDatas(this, snackView);
        MyApplication.setListActivity(this);
        preliminary();
        EventBus.getDefault().register(this);

        //设置虚拟按键变成三个小点
        final Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        window.setAttributes(params);

        /**
         * 虚拟键盘弹出来监听
         */
        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setHideVirtualKey(window);
            }
        });
    }

    @Override
    protected void onResume() {
        //友盟统计
        MobclickAgent.onResume(this);
        registerReceiveCustom(true);
        super.onResume();
//        appUser = (AppUser) CacheUtils.readObject(this, CacheUtils.USERINFO);
        appUser = SharedPreferenceUtils.getUserInfo(mContext);
        /**
         * 废除用户：根据姓名和发送的消息进行废除用户强制退出；
         */
        Observer<List<IMMessage>> incomingMessageObserver =
                new Observer<List<IMMessage>>() {
                    @Override
                    public void onEvent(List<IMMessage> messages) {
                        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                        IMMessage imMessage = messages.get(0);
                        if (imMessage.getSessionId().equals("miu_1") && imMessage.getContent()
                                .equals("exit")) {
                            LogUtils.e("退出拉黑客戶端" + imMessage.getFromNick() + imMessage.getContent
                                    ());
                            quitLogin();
                        }
                        LogUtils.e("messages: " + imMessage.getSessionId() + imMessage.getContent
                                ());
                    }
                };
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        MobclickAgent.onPause(this);
    }


    /**
     * 隐藏虚拟键盘
     * @param window
     */
    public void setHideVirtualKey(Window window){
        //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT>=19){
            uiOptions |= 0x00001000;
        }else{
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
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
        SharedPreferenceUtils.saveUserInfo(mContext,appUser);
        SharedPreferenceUtils.saveLoginFrom(mContext,loginFrom);

        //发送到MainActivity，关闭页面
        EventBus.getDefault().post(new QuitLogin());
        //开启登录页面
        gotoActivity(LoginActivity.class, true);
    }

    /**
     * 注销
     */
    private void logout() {
        LogoutHelper.logout();
        removeLoginState();
        NIMClient.getService(AuthService.class).logout();
    }

    /**
     * 清除登陆状态
     */
    private void removeLoginState() {
        Preferences.saveUserToken("");
    }

    @Override
    protected void onStop() {
        registerReceiveCustom(false);
        super.onStop();
    }

    /**
     * 向用户展示信息前的准备工作在这个方法里处理
     */
    protected void preliminary() {
        // 初始化数据
        initialized();
        // 初始化组件
        initListener();

    }

    /**
     * 获取全局的Context
     *
     * @return {@link #mContext = this.getApplicationContext();}
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 布局文件ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initialized();


    /**
     * 默认退出
     */
    public void defaultFinish() {
        super.finish();
    }


    public void gotoActivity(Class<? extends Activity> clazz, boolean finish) {
        Intent intent = new Intent(this, clazz);
        this.startActivity(intent);
        if (finish) {
            this.finish();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim
                        .activity_move_out_start);
            }
        }

    }

    /**
     * 吐司
     *
     * @param msg
     */
    public void showToast(String msg) {
        com.lvshandian.lemeng.utils.ToastUtils.showMessageDefault(mContext, msg);
    }


    /**
     * 初始化标题,不需要右按钮，传空即可
     *
     * @param left
     * @param title
     * @param right
     */
    protected void initTitle(String left, String title, String right) {
        TextView titlebar_left = (TextView) findViewById(R.id.tv_titlebar_left);
        TextView titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        titlebar_right = (TextView) findViewById(R.id.tv_titlebar_right);

        if (left == null) {
            titlebar_left.setVisibility(View.INVISIBLE);
        } else {
            titlebar_left.setText(left);
            titlebar_left.setVisibility(View.VISIBLE);
        }
        if (right == null) {
            titlebar_right.setVisibility(View.INVISIBLE);
        } else {
            titlebar_right.setText(right);
            titlebar_right.setVisibility(View.VISIBLE);
        }
        titlebar_title.setText(title);
        titlebar_left.setOnClickListener(this);
        titlebar_right.setOnClickListener(this);
        titlebar_title.setOnClickListener(this);

    }

    /**
     * 判断用户是否登录（appuser不为空则表示登录中）
     *
     * @author sll
     * @time 2016/11/15 14:33
     */
    public boolean userState() {
        boolean isState = false;
        if (appUser == null) {
//            appUser = (AppUser) CacheUtils.readObject(this, CacheUtils.USERINFO);
            appUser = SharedPreferenceUtils.getUserInfo(mContext);
            if (appUser == null) {
                gotoActivity(LoginActivity.class, true);
                isState = false;
            } else {
                isState = true;
            }
        } else {
            isState = true;
        }
        return isState;
    }

    /**
     * 权限请求-读写、相机
     * Manifest.permission.ACCESS_FINE_LOCATION,//位置
     * Manifest.permission.READ_SMS,//短信
     * Manifest.permission.READ_PHONE_STATE,//读取电话状态权限
     * Manifest.permission.RECORD_AUDIO,//麦克风
     * Manifest.permission.READ_CONTACTS,//读写
     * Manifest.permission.CAMERA,//相机
     * Manifest.permission.WRITE_EXTERNAL_STORAGE//存储
     * Manifest.permission.READ_EXTERNAL_STORAGE
     *
     * @author sll
     * @time 2016/11/15 16:37
     */
    public void requestPermission(String... permission) {
        AndPermission.with(this)
                .requestCode(101)
                .permission(permission)
                .rationale(mRationaleListener)
                .send();
    }

    @PermissionYes(101)
    private void getLocationYes() {
        Toast.makeText(this, "获取权限成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionNo(101)
    private void getLocationNo() {
        Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int checkSelfPermission(String permission) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission
                (mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            requestPermission(permission);
        } else {
        }
        return super.checkSelfPermission(permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // 这个Activity中没有Fragment，这句话可以注释。
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            new AlertDialog.Builder(BaseActivity.this)
                    .setTitle("友好提醒")
                    .setMessage("没有权限将带来不好体验，请把定位权限赐给我吧！")
                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.resume();// 用户同意继续申请。
                        }
                    })
                    .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.cancel(); // 用户拒绝申请。
                        }
                    }).show();
        }
    };

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 修改小米状态栏颜色
     *
     * @param darkmode
     * @param activity
     */
    public void setMiuiStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改魅族状态栏颜色
     *
     * @param dark
     * @param activity
     */
    public static boolean setMeizuStatusBarDarkIcon(boolean dark, Activity activity) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        MyApplication.removeListActivity(this);
        super.onDestroy();
    }


    /**
     * 发送自定义通知，用于连麦
     * {"result":"1","id":2,"content":"主播同意了您的视频连线请求","type":"0"}
     *
     * @param sessionId 发送给谁的id
     * @param content   内容
     * @param id
     * @param type      标示类型
     * @author sll
     * @time 2016/12/9 20:44
     */
    public void sendCustomNotificationForLive(String sessionId, String userId, String userName, String userAvatar,
                                              String vip, String content, String id, String type, String result) {
        LogUtils.i("WangYi_CN", "发送自定义通知sessionId " + sessionId);
        CustomNotification notification = new CustomNotification();
        notification.setSessionId(sessionId);
        notification.setSessionType(SessionTypeEnum.P2P);

        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("userName", userName);
        json.put("userAvatar", userAvatar);
        json.put("vip", vip);
        json.put("content", content);
        json.put("id", id);
        json.put("type", type);
        json.put("result", result);
        notification.setContent(json.toString());
        LogUtils.i("WangYi_CN", "发送自定义通知json " + json.toString());
        notification.setApnsText(content);
        // 发送自定义通知
        NIMClient.getService(MsgService.class).sendCustomNotification(notification);
    }


    /**
     * 自定义通知接收
     *
     * @author sll
     * @time 2016/12/6 18:08
     */
    private void registerReceiveCustom(boolean register) {
        if (register)
            LogUtils.i("WangYi_CN", "Watch接收自定义通知:开启");
        else
            LogUtils.i("WangYi_CN", "Watch接收自定义通知:关闭");

        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(observer, register);
    }

    Observer<CustomNotification> observer = new Observer<CustomNotification>() {
        @Override
        public void onEvent(final CustomNotification message) {
//          在这里处理自定义通知。
            try {
                /**
                 * 获取到关注人直播提醒，点击进入直播间
                 */
                org.json.JSONObject jsonObject = new org.json.JSONObject(message.getContent());
                String content = jsonObject.getString("content");
                String roomId = jsonObject.getString("roomId");
                joinRoom(content, roomId);
            } catch (JSONException e) {

            }
        }
    };

    /**
     * 加入直播间
     *
     * @param content
     * @param roomId
     */
    private void joinRoom(final String content, final String roomId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ok(content, roomId);
            }
        }, 1500);
    }

    /**
     * 弹出关注人提醒，进入直播间
     *
     * @param content
     * @param roomId
     */

    private void ok(final String content, String roomId) {
        String url = UrlBuilder.serverUrl + UrlBuilder.joinRoom + roomId;
        OkHttpUtils.get().url(url)
                .build().execute(new CustomStringCallBack(mContext, HttpDatas.KEY_CODE) {
            @Override
            public void onFaild() {
            }

            @Override
            public void onSucess(String data) {
                final LiveBean liveBean = JsonUtil.json2Bean(data, LiveBean.class);
                View inflate = mInflator.inflate(R.layout.pop_vedio_notifaction, null);
                TextView tvContent = (TextView) inflate.findViewById(R.id.tv_text);
                tvContent.setText(content);
                /**
                 * 点击进入直播间
                 */
                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (NetWorkUtil.getConnectedType(mContext) == 0) {
                            initDialog();
                            baseDialogTitle.setText("当前为移动网络,是否继续观看");
                            baseDialogLeft.setText("取消观看");
                            baseDialogRight.setText("继续观看");
                            baseDialogLeft.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (baseDialog != null && baseDialog.isShowing()) {
                                        baseDialog.dismiss();
                                    }
                                }
                            });
                            baseDialogRight.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (baseDialog != null && baseDialog.isShowing()) {
                                        baseDialog.dismiss();
                                    }
                                    ifEnter(liveBean);
                                }
                            });
                        } else {
                            ifEnter(liveBean);
                        }
                    }
                });

                ShowPop showPop = new ShowPop(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT);
                showPop.setContentView(inflate);
                showPop.setFocusable(true);
                showPop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color
                        .transparent)));
                int heightPixels = getResources().getDisplayMetrics().heightPixels;
                showPop.showAsDropDown(getWindow().getDecorView().findViewById(android.R.id
                        .content), 0, -heightPixels + 100);
            }
        });
    }


    /**
     * 跳转到观看页面
     *
     * @author sll
     * @time 2016/12/16 16:58
     */
    private void startActivityToWatch(LiveListBean live) {
        Intent intent = new Intent(this, WatchLiveActivity.class);
        intent.putExtra("LIVE", live);
        startActivity(intent);
    }

    /**
     * 判断是否进入过该私密直播
     *
     * @author sll
     * @time 2016/12/16 17:14
     */
    public void ifEnter(LiveBean liveListBean) {
        live = transformLiveListBeen(liveListBean);
        String url = UrlBuilder.chargeServerUrl + UrlBuilder.ifEnter;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("roomId", live.getRooms().getId() + "");
        hashMap.put("userId", appUser.getId());
        String json = new org.json.JSONObject(hashMap).toString();
        LogUtils.e("Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        LogUtils.i("WangYi_secret", "是否进入过该私密直播: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("WangYi_secret", "是否进入过该私密直播: " + response);
                        JoinRoomBean joinRoom = JsonUtil.json2Bean(response, JoinRoomBean.class);
                        if (joinRoom != null && joinRoom.isSuccess() && joinRoom.getCode() != 1) {
                            //第一次进入
                            if (live!= null && !TextUtils.isEmpty(live.getRooms().getPrivateFlag() + "")
                                    && ((live.getRooms().getPrivateFlag() + "")).equals("1")) {
                                joinSecret(live);
                            } else {
                                startActivityToWatch(live);
                            }
                        } else {
                            //不是第一次，直接进入
                            startActivityToWatch(live);
                        }
                    }
                });
    }

    /**
     * 判断是否进入过该私密直播
     *
     * @author sll
     * @time 2016/12/16 17:14
     */
    public void ifEnterTwo(final LiveListBean live) {
        String url = UrlBuilder.chargeServerUrl + UrlBuilder.ifEnter;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("roomId", live.getRooms().getId() + "");
        hashMap.put("userId", appUser.getId());
        String json = new org.json.JSONObject(hashMap).toString();
        LogUtils.e("Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        LogUtils.i("WangYi_secret", "是否进入过该私密直播: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("WangYi_secret", "是否进入过该私密直播: " + response);
                        JoinRoomBean joinRoom = JsonUtil.json2Bean(response, JoinRoomBean.class);
                        if (joinRoom != null && joinRoom.isSuccess() && joinRoom.getCode() != 1) {
                            //第一次进入
                            if (live != null && !TextUtils.isEmpty(live.getRooms().getPrivateFlag() + "")
                                    && ((live.getRooms().getPrivateFlag() + "")).equals("1")) {
                                joinSecret(live);
                            } else {
                                startActivityToWatch(live);
                            }
                        } else {
                            //不是第一次，直接进入
                            startActivityToWatch(live);
                        }
                    }
                });
    }

    /**
     * 支付进入该私密直播
     * "createrId":createId , @"entrantId":userId ,@"coinNum":coinNum
     *
     * @author sll
     * @time 2016/12/16 17:14
     */
    private void updateCoin(final LiveListBean live) {
        String url = UrlBuilder.chargeServerUrl + UrlBuilder.updateCoin;
        LogUtils.e("WangYi_secret", "url: " + url);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("createrId", live.getRooms().getUserId() + "");
        hashMap.put("entrantId", appUser.getId());
        hashMap.put("coinNum", live.getRooms().getRoomPay() + "");
        String json = new org.json.JSONObject(hashMap).toString();
        LogUtils.e("Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        LogUtils.i("WangYi_secret", "付进入该私密直播: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("WangYi_secret", "付进入该私密直播: " + response);
                        JoinRoomBean joinRoom = JsonUtil.json2Bean(response, JoinRoomBean.class);
                        if (joinRoom != null && joinRoom.isSuccess() && joinRoom.getCode() == 1) {
                            startActivityToWatch(live);
                        } else {
                            //账户余额不足
                            showToast("账户余额不足");
                        }
                    }
                });

    }

    /**
     * 私密直播时弹出，须支付或输入密码
     *
     * @author sll
     * @time 2016/12/16 15:59
     */
    private void joinSecret(final LiveListBean live) {
        final Dialog dialog = new Dialog(this, R.style.homedialog);
        final View view = View.inflate(this, R.layout.dialog_join_secret, null);
        final TextView joinSecretPrompt = (TextView) view.findViewById(R.id.join_secret_prompt);
        final LinearLayout joinSecretCancel = (LinearLayout) view.findViewById(R.id
                .join_secret_cancel);
        joinSecretPrompt.setText("需要密码或" + live.getRooms().getRoomPay() + "金币进入该房间");
        //支付
        view.findViewById(R.id.join_secret_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCoin(live);
                dialog.dismiss();
            }
        });
        //密码
        view.findViewById(R.id.join_secret_pw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinForPw(live);
                dialog.dismiss();
            }
        });
        //取消
        joinSecretCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 支付密码进入私密直播
     *
     * @author sll
     * @time 2016/12/16 16:34
     */
    private void joinForPw(final LiveListBean live) {
        final Dialog dialog = new Dialog(this, R.style.homedialog);
        final View view = View.inflate(this, R.layout.dialog_join_secret_pwd, null);
        final EditText pwdEdit = (EditText) view.findViewById(R.id.join_secret_pwd_edit);
        //取消
        view.findViewById(R.id.join_secret_pwd_cancel).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确定
        view.findViewById(R.id.join_secret_pwd_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pwdEdit.getText().toString())) {
                    showToast("请输入密码");
                } else if (pwdEdit.getText().toString().equals(live.getRooms().getRoomPw())) {
                    //密码正确，进入直播间
                    startActivityToWatch(live);
                    dialog.dismiss();
                } else {
                    showToast("密码错误，请确认后再输入");
                }
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 将有用的数据类型进行转换
     *
     * @param liveBean
     * @return
     */
    public LiveListBean transformLiveListBeen(final LiveBean liveBean) {
        LiveListBean liveListBean = new LiveListBean();
        liveListBean.setLivePicUrl(liveBean.getLivePicUrl());
        liveListBean.setPicUrl(liveBean.getCreator().getPicUrl());
        liveListBean.setAddress(liveBean.getCity());
        liveListBean.setNickName(liveBean.getCreator().getNickName());
        liveListBean.setId(Integer.parseInt(liveBean.getCreator().getId().trim()));
        LiveListBean.RoomsBean creatorBean = new LiveListBean.RoomsBean();
        creatorBean.setCity(liveBean.getCity());
        creatorBean.setId(Integer.parseInt(liveBean.getId()));
        creatorBean.setOnlineUserNum(Integer.parseInt(liveBean.getOnlineUserNum().trim()));
        creatorBean.setUserId(Integer.parseInt(liveBean.getCreator().getId()));
        creatorBean.setRoomId(Integer.parseInt(liveBean.getRoomId()));
        creatorBean.setRoomsType(liveBean.getRoomsType());

        try {
            creatorBean.setBroadcastUrl(DESUtil.decrypt(liveBean.getBroadcastUrl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        creatorBean.setPublishUrl(liveBean.getPublishUrl());
        if (!TextUtils.isEmpty(liveBean.getPrivateFlag())) {
            creatorBean.setPrivateFlag(Integer.parseInt(liveBean.getPrivateFlag().trim()));//是否是私密房间1是2否
        }
        if (!TextUtils.isEmpty(liveBean.getRoomPay())) {
            creatorBean.setRoomPay(Integer.parseInt(liveBean.getRoomPay()));//私密直播需要的金币
        }
        creatorBean.setRoomPw(liveBean.getRoomPw());//私密直播需要的密码
        liveListBean.setRooms(creatorBean);
        return liveListBean;
    }

    @Subscribe
    public void onEventMainThread(QuitApp quit) {
        finish();
    }


    public TextView baseDialogTitle, baseDialogLeft, baseDialogRight;
    public Dialog baseDialog;
    public View baseDialogLine;

    /**
     * 自定义dialog
     */
    public void initDialog() {
        if (baseDialog == null) {
            baseDialog = new Dialog(mContext, R.style.CustomDialog);
        }
        baseDialog.setCanceledOnTouchOutside(true);
        baseDialog.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.custom_dialog, null);

        baseDialog.setContentView(view);
        WindowManager.LayoutParams params = baseDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        baseDialog.getWindow().setAttributes(params);
        baseDialog.show();

        baseDialogTitle = (TextView) view.findViewById(R.id.tvTitle);
        baseDialogLeft = (TextView) view.findViewById(R.id.btnLeft);
        baseDialogRight = (TextView) view.findViewById(R.id.btnRight);
        baseDialogLine = view.findViewById(R.id.line);
    }
}