package com.lvshandian.lemeng.activity.mine;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.activity.MainActivity;
import com.lvshandian.lemeng.activity.start.LogoutHelper;
import com.lvshandian.lemeng.interfaces.ResultListener;
import com.lvshandian.lemeng.entity.AppUser;
import com.lvshandian.lemeng.entity.QuitLogin;
import com.lvshandian.lemeng.entity.mine.LoginFrom;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.net.RequestCode;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.widget.view.AvatarView;
import com.lvshandian.lemeng.utils.AliYunImageUtils;
import com.lvshandian.lemeng.utils.DateUtils;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.UMUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import com.lvshandian.lemeng.third.wangyiyunxin.config.preference.Preferences;

import static com.lvshandian.lemeng.R.id.iv_head;

public class SettingHeadAndNick extends BaseActivity {
    @Bind(iv_head)
    AvatarView ivHead;
    @Bind(R.id.nick_name)
    EditText nickName;
    @Bind(R.id.next)
    TextView next;

    /**
     * 裁剪后的地址
     */
    protected Uri imageUri;

    /**
     * 头像的aliyun地址
     */
    private String headUrl;

    protected static final int TAKE_PICTURE = 110;
    protected static final int CHOOSE_PICTURE = 120;
    private static final int CROP_SMALL_PICTURE = 123;

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
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_head_and_nick;
    }

    @Override
    protected void initListener() {
        ivHead.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        if (!TextUtils.isEmpty(appUser.getPicUrl())) {
            headUrl = appUser.getPicUrl();
            Picasso.with(mContext).load(headUrl).into(ivHead);
        }
        if (!TextUtils.isEmpty(appUser.getNickName())) {
            String nick = appUser.getNickName();
            nickName.setText(nick);
            if (nick.length() > 10) {
                nickName.setSelection(10);
            } else {
                nickName.setSelection(nick.length());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case iv_head://点击上传头像
                //权限请求
                requestPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                selectPhoto();
                break;
            case R.id.next:
                if (TextUtils.isEmpty(headUrl)) {
                    showToast(getString(R.string.please_uploading_avatar));
                    return;
                }
                if (TextUtils.isEmpty(nickName.getText().toString().trim())) {
                    showToast(getString(R.string.input_right_nickname));
                    return;
                }
                changeappUser(headUrl, nickName.getText().toString().trim());
                break;
        }
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
    private void changeappUser(String picUrl, String nickName) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId());
        map.put("picUrl", picUrl);
        map.put("nickName", nickName);
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

    @Override
    public void onBackPressed() {
        quitLogin();
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
        finish();
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
}
