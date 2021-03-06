package com.lvshandian.lemeng.activity.mine;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.AddressSelectActivity;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.interfaces.ResultListener;
import com.lvshandian.lemeng.entity.AppUser;
import com.lvshandian.lemeng.entity.lemeng.Args;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.net.RequestCode;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.widget.tripartite.OptionPicker;
import com.lvshandian.lemeng.widget.tripartite.WheelView;
import com.lvshandian.lemeng.widget.view.AvatarView;
import com.lvshandian.lemeng.widget.view.CustomPopWindow;
import com.lvshandian.lemeng.utils.AliYunImageUtils;
import com.lvshandian.lemeng.utils.DateUtils;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 修改个人资料
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
     * 拍照或相册生成的uri
     */
    protected static Uri imageUri;

    /**
     * 选择性别的popupWindow
     */
    private CustomPopWindow popupWindow;

    protected static final int TAKE_PICTURE = 110;
    protected static final int CHOOSE_PICTURE = 120;
    private static final int CROP_SMALL_PICTURE = 123;
    private static final int CHANGE_PERSON_NAME = 124;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                //关注请求接收数据
                case RequestCode.USER_TAG:
                    AppUser appUser = JSON.parseObject(json, AppUser.class);
                    //存储用户信息
                    SharedPreferenceUtils.saveUserInfo(mContext, appUser);
                    sendUserToWangYi(appUser);
                    showToast(getString(R.string.edit_success));
                    finish();
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
        initTitle("", getString(R.string.edit_profile), getString(R.string.save));
        initappUser();
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
                    tvSex.setText(getString(R.string.male));
                if (gender.equals("0"))
                    tvSex.setText(getString(R.string.female));
            }


            if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(appUser.getSignature())) {
                tvQm.setText(getString(R.string.default_sign));
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
                    headUrl = appUser.getPicUrl();
                }
                if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(nick)) {
                    showToast(getString(R.string.input_right_nickname));
                    return;
                }
                if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(gender)) {
                    showToast(getString(R.string.input_right_gender));
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
                picker.setTopBackgroundColor(0xFFFFFFFF);
                picker.setTopHeight(50);
                picker.setTopLineColor(0xFFE4E4E4);  //顶部线颜色
                picker.setTopLineHeight(1);
                picker.setTitleText(isChinese ? "请选择" : "Please pick");
                picker.setTitleTextColor(0xFF999999);
                picker.setTitleTextSize(12);
                picker.setCancelTextColor(0xFF000000);
                picker.setCancelTextSize(13);
                picker.setSubmitTextColor(0xFFF6D42C);
                picker.setSubmitTextSize(13);
                picker.setTextColor(0xFF000000, 0xFF999999);  //选中，未选中
                WheelView.LineConfig config = new WheelView.LineConfig();
                config.setColor(0xFFE5E5E5);//滚轮分割线颜色
                config.setAlpha(255);//线透明度
                config.setRatio(0);//线比率
                picker.setLineConfig(config);
                picker.setItemWidth(400);
                picker.setBackgroundColor(0xFFFFFFFF);
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
                tvSex.setText(getString(R.string.male));
                popupWindow.dismiss();
                break;
            case R.id.btn_womanto:
                tvSex.setText(getString(R.string.female));
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
        popupWindow = new CustomPopWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_popupwindow, null);
        popupWindow.setContentView(view);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(tvSex, Gravity.BOTTOM, 0, 0);

        Button btn_man = (Button) view.findViewById(R.id.btn_man);
        Button btn_womanto = (Button) view.findViewById(R.id.btn_womanto);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_man.setOnClickListener(this);
        btn_womanto.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }


    /**
     * 选择拍照或者图库选择
     */
    private void selectPhoto() {
        File imageFile = new File(com.lvshandian.lemeng.interfaces.Constant.APP_PATH + "image/");
        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
        imageUri = Uri.fromFile(new File(imageFile, "IMG_" + DateUtils.currentTime() + ".jpg"));
        final Dialog dialog = new Dialog(this, R.style.homedialog);
        final View view = View.inflate(this, R.layout.dialog_user_head, null);
        final LinearLayout cancelHeadLinear = (LinearLayout) view.findViewById(R.id.cancel_head_linear);
        //拍照
        view.findViewById(R.id.photo_head_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定照片保存路径
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
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
                startPhotoZoom(imageUri); // 开始对图片进行裁剪处理
                break;
            case CHOOSE_PICTURE:
                if (null != data && null != data.getData()) {
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                }
                break;
            case CROP_SMALL_PICTURE:
                if (resultCode == RESULT_OK) {
                    aliyunIdKey();
                    Bitmap photo = BitmapFactory.decodeFile(imageUri.getPath());
                    ivHead.setImageBitmap(photo);
                }
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 请求阿里云id、key，成功后上传头像
     */
    private void aliyunIdKey() {
        AliYunImageUtils.newInstance().uploadImage(mContext, imageUri.getPath(), new ResultListener() {
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
        if (gender.equals(getString(R.string.male)))
            map.put("gender", "1");
        if (gender.equals(getString(R.string.female)))
            map.put("gender", "0");
        map.put("constellation", constellation);
        map.put("address", address);
        map.put("signature", signature);
        httpDatas.getNewDataCharServer("修改用户信息", true, Request.Method.POST, UrlBuilder.EDIT_PROFILE, map, mHandler, RequestCode.USER_TAG, TAG);
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
                            LogUtils.i("WangYi", "上传昵称头像成功");
                        } else {
                            LogUtils.i("WangYi", "上传昵称头像失败");
                        }
                    }
                });
    }


    @Subscribe //注册一个在后台线程执行的方法,用于接收事件
    public void onEventMainThread(Args args) {
        if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(args.getCity())) {
            tvAddress.setText(args.getCity());
        }
    }

}
