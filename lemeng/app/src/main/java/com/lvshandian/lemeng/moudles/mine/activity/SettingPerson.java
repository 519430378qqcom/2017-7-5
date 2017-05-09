package com.lvshandian.lemeng.moudles.mine.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.lvshandian.lemeng.MainActivity;
import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.lemeng.Args;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.interf.ResultListener;
import com.lvshandian.lemeng.moudles.index.activity.AddressSelectActivity;
import com.lvshandian.lemeng.tripartite.OptionPicker;
import com.lvshandian.lemeng.tripartite.WheelView;
import com.lvshandian.lemeng.utils.AliYunImageUtils;
import com.lvshandian.lemeng.utils.CacheUtils;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.UpdateImagerUtils;
import com.lvshandian.lemeng.wangyiyunxin.config.DemoCache;
import com.lvshandian.lemeng.wangyiyunxin.config.preference.Preferences;
import com.lvshandian.lemeng.wangyiyunxin.config.preference.UserPreferences;
import com.lvshandian.lemeng.widget.AvatarView;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 修改个人资料
 * Created by Administrator on 2017/1/19.
 */

public class SettingPerson extends BaseActivity {

    @Bind(R.id.iv_head)
    AvatarView ivHead;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.rl_name)
    AutoRelativeLayout rlName;
    @Bind(R.id.rl_age)
    AutoRelativeLayout rlAge;
    @Bind(R.id.rl_xz)
    AutoRelativeLayout rlXz;
    @Bind(R.id.rl_address)
    AutoRelativeLayout rlAddress;
    @Bind(R.id.rl_qm)
    AutoRelativeLayout rlQm;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_qm)
    TextView tvQm;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.xz_name)
    TextView xz_name;

    /**
     * 地址
     */
    public static TextView citys;

    /**
     * 头像的aliyun地址
     */
    private String headUrl;

    /**
     * 头像名称
     */
    private String imageName;

    /**
     * 头像路径
     */
    private String imagePath;

    /**
     * 拍照或相册生成的uri
     */
    protected Uri destUri = null;

    /**
     * 判断是否从注册界面跳转过来的
     */
    private String isRegister;

    /**
     * 选择性别的popupWindow
     */
    private PopupWindow popupWindow;
    protected static Uri tempUri;
    private Bitmap photo = null;
    protected static final int TAKE_PICTURE = 110;
    protected static final int CHOOSE_PICTURE = 120;
    private static final int CROP_SMALL_PICTURE = 123;
    private static final int CHANGE_PERSON_NAME = 124;

    private AbortableFuture<LoginInfo> loginRequest;
    private String account = null;
    private String token = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                //关注请求接收数据
                case RequestCode.USER_TAG:
                    LogUtils.e("修改个人信息: " + json);
                    //存储用户信息
                    try {
                        JSONObject obj = new JSONObject(json);
                        appUser.setAddress(obj.getString("address"));
                        appUser.setConstellation(obj.getString("constellation"));
                        appUser.setGender(obj.getString("gender"));
                        appUser.setId(obj.getString("id"));
                        appUser.setNickName(obj.getString("nickName"));
                        appUser.setPicUrl(obj.getString("picUrl"));
                        appUser.setSignature(obj.getString("signature"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    CacheUtils.saveObject(SettingPerson.this, appUser, CacheUtils.USERINFO);
                    if (isRegister.equals("register")) {
                        showToast("编辑资料成功");
                        loginWangYi();
                        MyApplication.finishActivity();
                        startActivity(new Intent(mContext, MainActivity.class));
                    } else {
                        showToast("修改成功");
                        finish();
                    }
                    sendUserToWangYi();
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_person;
    }

    @Override
    protected void initListener() {
        ivHead.setOnClickListener(this);
        rlAge.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
        rlQm.setOnClickListener(this);
        rlXz.setOnClickListener(this);
        rlName.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        isRegister = getIntent().getStringExtra("isRegister");
        initTitle("", "编辑资料", "保存");
        if (isRegister.equals("unRegister")) {
            initappUser();
        }
    }


    /**
     * 初始个人信息
     */
    private void initappUser() {
        if (appUser != null) {
            tvName.setText(appUser.getNickName());
            String gender = appUser.getGender();
            if (gender != null) {
                if (gender.equals("1"))
                    tvSex.setText("男");
                if (gender.equals("0"))
                    tvSex.setText("女");
            }


            if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(appUser.getSignature())) {
                tvQm.setText("这个家伙很懒，什么都没留下");
            } else {
                tvQm.setText(appUser.getSignature());
            }

            String constellation = appUser.getConstellation();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(constellation))
                xz_name.setText(constellation);

            String address = appUser.getAddress();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(address))
                tvAddress.setText(address);

            String picUrl = appUser.getPicUrl();
            ImageLoader.getInstance().loadImage(picUrl, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ivHead.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
            case R.id.tv_titlebar_right:
                String nick = tvName.getText().toString();
                String gender = tvSex.getText().toString();
                String xz = xz_name.getText().toString();
                String address = tvAddress.getText().toString();
                String signature = tvQm.getText().toString();
                if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(headUrl)) {
                    if (isRegister.equals("unRegister")) {
                        headUrl = appUser.getPicUrl();
                    } else {
                        showToast("请上传头像");
                        return;
                    }
                }
                if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(nick)) {
                    showToast("请填写昵称");
                    return;
                }
                if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(gender) || !(gender.equals("男") || gender.equals("女"))) {
                    showToast("请填写男或女");
                    return;
                }
                if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(xz)) {
                    showToast("请填写星座");
                    return;
                }
                if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(address)) {
                    showToast("请填写地址");
                    return;
                }
                if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(signature)) {
                    showToast("请填写签名");
                    return;
                }
                if (userState())
                    changeappUser(headUrl, nick, gender, xz, address, signature);
                break;
            case R.id.rl_address://地址
                citys = tvAddress;
                Intent intent = new Intent(SettingPerson.this, AddressSelectActivity.class);
                intent.putExtra("only", true);
                startActivity(intent);
                break;
            case R.id.rl_age://性別
                getPopupWindow();
                break;
            case R.id.rl_qm: //签名
                Intent intent2 = new Intent(SettingPerson.this, UpdatePersonActivity.class);
                intent2.putExtra("name", tvQm.getText().toString());
                intent2.putExtra("title", "签名");
                startActivityForResult(intent2, 2);
                break;
            case R.id.rl_xz: //星座
                boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
                OptionPicker picker = new OptionPicker(this,
                        isChinese ? new String[]{
                                "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
                                "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"
                        } : new String[]{
                                "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
                                "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
                        });
                picker.setCycleDisable(false);//不禁用循环
                picker.setTopBackgroundColor(0xFFEEEEEE);
                picker.setTopHeight(50);
                picker.setTopLineColor(0xFF33B5E5);
                picker.setTopLineHeight(1);
                picker.setTitleText(isChinese ? "请选择" : "Please pick");
                picker.setTitleTextColor(0xFF999999);
                picker.setTitleTextSize(12);
                picker.setCancelTextColor(0xFF33B5E5);
                picker.setCancelTextSize(13);
                picker.setSubmitTextColor(0xFF33B5E5);
                picker.setSubmitTextSize(13);
                picker.setTextColor(0xFFEE0000, 0xFF999999);
                WheelView.LineConfig config = new WheelView.LineConfig();
                config.setColor(0xFFEE0000);//线颜色
                config.setAlpha(140);//线透明度
                config.setRatio((float) (1.0 / 8.0));//线比率
                picker.setLineConfig(config);
                picker.setItemWidth(200);
                picker.setBackgroundColor(0xFFE1E1E1);
                picker.setSelectedIndex(7);
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        xz_name.setText(item);
                    }
                });
                picker.show();
                break;
            case R.id.rl_name: //名称
                Intent intent1 = new Intent(SettingPerson.this, PersonNameActivity.class);
                intent1.putExtra("nickName", tvName.getText().toString());
                startActivityForResult(intent1, CHANGE_PERSON_NAME);
                break;
            case R.id.iv_head://点击上传头像
                //权限请求
                requestPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                selectPhoto();
                break;
            case R.id.btn_man:
                tvSex.setText("男");
                popupWindow.dismiss();
                break;
            case R.id.btn_womanto:
                tvSex.setText("女");
                popupWindow.dismiss();
                break;
            case R.id.btn_cancel:
                popupWindow.dismiss();
                break;
        }

    }


    /**
     * 选择性别的PopupWindow
     */
    public void getPopupWindow() {
        popupWindow = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_popupwindow, null);
        popupWindow.setContentView(view);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        backgroundAlpha(0.5f);

        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(tvSex, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopOnDismissListner());

        Button btn_man = (Button) view.findViewById(R.id.btn_man);
        Button btn_womanto = (Button) view.findViewById(R.id.btn_womanto);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_man.setOnClickListener(this);
        btn_womanto.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    /**
     * PopupWindow弹出后,activity改变透明度
     *
     * @param alpha
     */
    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = alpha;
        this.getWindow().setAttributes(params);
    }

    /**
     * PopupWindow dismiss监听
     */
    private class PopOnDismissListner implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 选择拍照或者图库选择
     */
    private void selectPhoto() {
        imageName = String.valueOf(System.currentTimeMillis()).substring(8);
        destUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), imageName + ".png"));
        final Dialog dialog = new Dialog(this, R.style.homedialog);
        final View view = View.inflate(this, R.layout.dialog_user_head, null);
        final LinearLayout cancelHeadLinear = (LinearLayout) view.findViewById(R.id.cancel_head_linear);
        //拍照
        view.findViewById(R.id.photo_head_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageName + ".png"));
                // 指定照片保存路径（SD卡），imageName + ".png"为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
                dialog.dismiss();
            }
        });
        //相册
        view.findViewById(R.id.album_head_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                dialog.dismiss();
            }
        });
        //取消
        cancelHeadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                break;
            case CHOOSE_PICTURE:
                if (null != data && null != data.getData()) {
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                }
                break;
            case CROP_SMALL_PICTURE:
                aliyunIdKey();
                Bitmap photo = BitmapFactory.decodeFile(destUri.getPath());
                ivHead.setImageBitmap(photo);
//                try {
//                    photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(destUri));
//                    uploadPic(photo);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case 2://签名
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getStringExtra("qianming"))) {
                        tvQm.setText(data.getStringExtra("qianming"));
                    }
                }
                break;
            case CHANGE_PERSON_NAME:
                if (data != null) {
                    tvName.setText(data.getStringExtra("personName"));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            LogUtils.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, destUri);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    private void uploadPic(Bitmap bitmap) {
        ivHead.setImageBitmap(bitmap);
        imagePath = UpdateImagerUtils.savePhoto(bitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), imageName);
        if (imagePath != null) {
            try {
                aliyunIdKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 请求阿里云id、key，成功后上传头像
     */
    private void aliyunIdKey() {
//        AliYunImageUtils.newInstance().uploadImage(mContext, imagePath, new ResultListener() {
        AliYunImageUtils.newInstance().uploadImage(mContext, destUri.getPath(), new ResultListener() {
            @Override
            public void onSucess(String data) {
                headUrl = data;
            }

            @Override
            public void onFaild() {

            }
        });
    }

    /**
     * 修改用户信息
     */
    private void changeappUser(String picUrl, String nickName, String gender, String constellation, String address, String signature) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId());
        map.put("picUrl", picUrl);
        map.put("nickName", nickName);
        if (gender.equals("男"))
            map.put("gender", "1");
        if (gender.equals("女"))
            map.put("gender", "0");
        map.put("constellation", constellation);
        map.put("address", address);
        map.put("signature", signature);
        httpDatas.getNewDataCharServer("修改用户信息", Request.Method.POST, UrlBuilder.user, map, mHandler, RequestCode.USER_TAG);
    }


    /**
     * 发送修改后的信息给网易云信
     */
    private void sendUserToWangYi() {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.Name, appUser.getNickName());
        fields.put(UserInfoFieldEnum.AVATAR, appUser.getPicUrl());
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {

                    @Override
                    public void onResult(int code, Void aVoid, Throwable throwable) {
                        if (code == ResponseCode.RES_SUCCESS) {
                            LogUtils.i("WangYi", "上传昵称头像成功");
                        } else {
                            LogUtils.i("WangYi", "上传昵称头像失败");
                        }
                    }
                });
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
                gotoActivity(MainActivity.class, true);
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(SettingPerson.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingPerson.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(SettingPerson.this, R.string.login_exception, Toast.LENGTH_LONG).show();
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

    @Subscribe //注册一个在后台线程执行的方法,用于接收事件
    public void onEventMainThread(Args args) {
        if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(args.getCity())) {
            tvAddress.setText(args.getCity());
        }
    }

}
