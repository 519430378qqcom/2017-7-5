package com.lvshandian.lemeng.moudles.mine.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.lemeng.CustomStringCallBack;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.interf.ResultListener;
import com.lvshandian.lemeng.utils.AliYunImageUtils;
import com.lvshandian.lemeng.utils.BitmpTools;
import com.lvshandian.lemeng.utils.ImageCompressUtils;
import com.lvshandian.lemeng.utils.MiPictureHelper;
import com.lvshandian.lemeng.utils.PermisionUtils;
import com.lvshandian.lemeng.utils.PicassoUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.widget.view.CustomPopWindow;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 提交认证界面
 */
public class AuthenticationActivity extends BaseActivity {
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_verify_code)
    EditText etVerifyCode;
    @Bind(R.id.tv_get_verify_code)
    TextView tvGetVerifyCode;
    @Bind(R.id.et_idcard_num)
    EditText etIdcardNum;
    @Bind(R.id.iv_id_card)
    ImageView ivIdCard;
    @Bind(R.id.iv_hand_card)
    ImageView ivHandCard;
    @Bind(R.id.btn_submit)
    TextView btnSubmit;
    @Bind(R.id.ll_phone_code)
    LinearLayout ll_quhao;
    @Bind(R.id.tv_phone_code)
    TextView tv_quhao;

    /**
     * 验证码倒计时1分钟
     */
    private final long daoJiShi = 1000 * 60;

    /**
     * 倒计时单位 秒
     */
    private final long countDownInterval = 1000;

    /**
     * 计时器
     */
    private CountDownTimer mTimer;

    /**
     * popupWindow
     */
    private CustomPopWindow popupWindow;

    /**
     * 图片储存目录
     */
    public String SDPATH = Environment.getExternalStorageDirectory() + "/lemeng/";

    /**
     * 图片储存路径
     */
    private File mFile;
    private final int TAKE_PICTURE = 1;
    private final int LOCAL_PICTURE = TAKE_PICTURE + 1;
    private PICTURE_FLAG mFlag = PICTURE_FLAG.NONE;

    /**
     * 上传图片的数量
     */
    private int uploadImageCount = 0;

    /**
     * 身份证正面照图片的本地路径
     */
    private String ID_IMAGE_PATH;

    /**
     * 手持身份证正面照图片的本地路径
     */
    private String HAND_ID_IMAGE_PATH;

    /**
     * 身份证正面照图片的网络路径
     */
    private String idImageNetPath;

    /**
     * 身份证正面照图片的网络路径
     */
    private String handIdImageNetPath;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                //关注请求接收数据
                case RequestCode.REAL_NAME_VERTIFY:
//                    appUser.setVerified("1");
//                    CacheUtils.saveObject(AuthenticationActivity.this, appUser, CacheUtils.USERINFO);
                    SharedPreferenceUtils.put(mContext, "verified", "1");
                    showToast(getString(R.string.uploading_succeed));
                    startActivity(new Intent(mContext, RealNameVertifyActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void initListener() {
        tvGetVerifyCode.setOnClickListener(this);
        ivIdCard.setOnClickListener(this);
        ivHandCard.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        ll_quhao.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.my_authentication), null);
        initCutDonwTime();
        File file = new File(SDPATH);
        if (!file.exists()) {
            file.mkdirs();
        }

    }



    /**
     * 拍照和选择相册的PopupWindow
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
        popupWindow.showAtLocation(btnSubmit, Gravity.BOTTOM, 0, 0);

        Button btn_man = (Button) view.findViewById(R.id.btn_man);
        Button btn_womanto = (Button) view.findViewById(R.id.btn_womanto);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_man.setText(getString(R.string.take_pictures));
        btn_womanto.setText(getString(R.string.select_an_album));
        btn_man.setOnClickListener(this);
        btn_womanto.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    /**
     * 初始倒计时
     */
    private void initCutDonwTime() {
        mTimer = new CountDownTimer(daoJiShi, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetVerifyCode.setText(String.valueOf((int) (millisUntilFinished / countDownInterval)) + "s");
            }

            @Override
            public void onFinish() {
                tvGetVerifyCode.setText(getString(R.string.sent_validate));
                tvGetVerifyCode.setEnabled(true);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_phone_code:
                Intent intent = new Intent(mContext, StateCodeActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
            case R.id.tv_get_verify_code:
                getVerifyCode();
                break;
            case R.id.iv_id_card:
                mFlag = PICTURE_FLAG.ID_CARD;
                getPopupWindow();
                break;
            case R.id.iv_hand_card:
                mFlag = PICTURE_FLAG.HAND_ID_CARD;
                getPopupWindow();
                break;
            case R.id.btn_man:
                //拍照
                camera();
                popupWindow.dismiss();
                break;
            case R.id.btn_womanto:
                //手机图库
                takeLocalImage();
                popupWindow.dismiss();
                break;
            case R.id.btn_cancel:
                popupWindow.dismiss();
                break;
            case R.id.btn_submit:
                subRenZhengInfo();
                break;
        }
    }

    /**
     * 获取验证
     */
    private void getVerifyCode() {
        String phoneNum = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            showToast(getString(R.string.input_right_phone));
            return;
        }
//        if (!TextPhoneNumber.isPhone(phoneNum)) {
//            showToast(getString(R.string.input_right_phone));
//            return;
//        }

        String str = tv_quhao.getText().toString();
        str = str.substring(str.lastIndexOf("+") + 1, str.length());
        phoneNum = str + phoneNum;

        OkHttpUtils.get().url(UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.GET_CODE)
                .addParams("mobile", phoneNum)
                .build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                showToast(getString(R.string.validate_code_sent_failure));
            }

            @Override
            public void onSucess(String data) {
                showToast(getString(R.string.validate_code_sent));
                mTimer.start();
                tvGetVerifyCode.setEnabled(false);
            }
        });
    }


    /**
     * 提交认证信息
     */
    private void subRenZhengInfo() {
        ConcurrentHashMap<String, String> params = new ConcurrentHashMap<>();

        String id = appUser.getId();
        params.put("Id", id);

        String name = etName.getText().toString().trim();
        params.put("realName", name);

        String phone = etPhone.getText().toString().trim();
//        if (!TextPhoneNumber.isPhone(phone)) {
//            showToast(getString(R.string.input_right_phone));
//            return;
//        }
        if (TextUtils.isEmpty(phone)) {
            showToast(getString(R.string.input_right_phone));
            return;
        }
        String str = tv_quhao.getText().toString();
        str = str.substring(str.lastIndexOf("+") + 1, str.length());
        phone = str + phone;

        params.put("phoneNum", phone);


        String verCode = etVerifyCode.getText().toString().trim();
        if (android.text.TextUtils.isEmpty(verCode)) {
            showToast(getString(R.string.input_right_validate));
            return;
        }
        params.put("verCode", verCode);

        String idNo = etIdcardNum.getText().toString().trim();
//        if (!RegexUtils.isIDCard15(idNo) && !RegexUtils.isIDCard18(idNo)) {
//            showToast(getString(R.string.input_right_id_number));
//            return;
//        }
        if (TextUtils.isEmpty(idNo)) {
            showToast(getString(R.string.input_right_id_number));
            return;
        }
        params.put("IDNo", idNo);

        if (android.text.TextUtils.isEmpty(idImageNetPath)) {
            showToast(getString(R.string.uploading_id_number_phone));
            return;
        }
        params.put("IDFrontPic", idImageNetPath);

        if (android.text.TextUtils.isEmpty(handIdImageNetPath)) {
            showToast(getString(R.string.uploading_take_id_number_phone));
            return;
        }
        params.put("IDbackPic", handIdImageNetPath);

        httpDatas.getNewDataCharServer("上传认证信息", true, Request.Method.POST, UrlBuilder.AUTHENTICATION, params, mHandler, RequestCode.REAL_NAME_VERTIFY, TAG);
    }

    /**
     * 获取本地图片
     */
    private void takeLocalImage() {
        PermisionUtils.newInstance().checkWriteStoragePermission(this, new PermisionUtils.OnPermissionGrantedLintener() {
            @Override
            public void permissionGranted() {
                Intent intentFromGallery = new Intent();
                // 设置文件类型
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, LOCAL_PICTURE);
            }
        });
    }



    /**
     * 拍照
     */
    private void camera() {
        PermisionUtils.newInstance().checkCallPhonePermission(this, new PermisionUtils.OnPermissionGrantedLintener() {
            @Override
            public void permissionGranted() {
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = String.valueOf(System.currentTimeMillis());
                mFile = new File(SDPATH, fileName + ".JPEG");
                if (mFile.exists()) {
                    mFile.delete();
                }
                Uri imageUri = Uri.fromFile(mFile);
                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 200:
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getStringExtra("stateCode"))) {
                        tv_quhao.setText(data.getStringExtra("stateCode"));
                    }
                }
                break;
            case TAKE_PICTURE:
                ImageCompressUtils.newInstance().compress(this, mFile, new ImageCompressUtils.CompressResultListener() {
                    @Override
                    public void onError() {
                    }

                    @Override
                    public void onSuccess(File resultFile) {
                        switch (mFlag) {
                            case ID_CARD:
                                ID_IMAGE_PATH = mFile.getPath();
                                getAliyunIDImageUrl();

                                break;
                            case HAND_ID_CARD:
                                HAND_ID_IMAGE_PATH = mFile.getPath();
                                getAliyunHandIDImageUrl();

                                break;
                        }
                        PicassoUtil.newInstance().onLocadThumbnail(mContext, resultFile.getPath(), 540, 360, mFlag == PICTURE_FLAG.ID_CARD ? ivIdCard : ivHandCard);
                    }
                });
                break;
            case LOCAL_PICTURE:
                Uri uri = data.getData();
                String path = MiPictureHelper.getPath(mContext, uri);
//                    ContentResolver resolver = getContentResolver();
                Bitmap photo = null;

                if (uri == null) {
                    Bundle bundle = data.getExtras();
                    Set<String> strings = bundle.keySet();
                    for (String key : strings) {
                    }
                    if (bundle != null) {
                        photo = (Bitmap) bundle.get("data"); //get bitmap
                    } else {
                        showToast(getString(R.string.uselect_phone_failure));
                    }
                } else {
                    try {
                        photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    } catch (FileNotFoundException e) {
                        showToast(getString(R.string.uselect_phone_failure));
                    }
                }

                Bitmap bitmap = ThumbnailUtils.extractThumbnail(photo, 540, 360);

                if (mFlag == PICTURE_FLAG.ID_CARD) {
                    ivIdCard.setImageBitmap(bitmap);
                } else {
                    ivHandCard.setImageBitmap(bitmap);
                }


                switch (mFlag) {
                    case ID_CARD:
                        ID_IMAGE_PATH = path;
                        getAliyunIDImageUrl();
                        break;
                    case HAND_ID_CARD:
                        HAND_ID_IMAGE_PATH = path;
                        getAliyunHandIDImageUrl();
                        break;
                }

                break;
        }
    }

    private void getAliyunHandIDImageUrl() {
        File certifationFile = new File(HAND_ID_IMAGE_PATH);
        Bitmap bitmap1 = null;
        File file1 = null;
        try {
            bitmap1 = BitmpTools.revitionImageSize(certifationFile.getAbsolutePath());
            file1 = compressImage(bitmap1);
            String HandabsolutePath = file1.getAbsolutePath();
            AliYunImageUtils.newInstance().uploadImage(this, HandabsolutePath, new ResultListener() {
                @Override
                public void onSucess(String data) {
                    uploadImageCount++;
                    handIdImageNetPath = data;
                }

                @Override
                public void onFaild() {
                    showToast(getString(R.string.uploading_take_id_number_phone_failure));
                    uploadImageCount = 0;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getAliyunIDImageUrl() {

        File idCardfile = new File(ID_IMAGE_PATH);
        Bitmap bitmap1 = null;
        File file1 = null;
        try {
            bitmap1 = BitmpTools.revitionImageSize(idCardfile.getAbsolutePath());
            file1 = compressImage(bitmap1);
            String IDImageabsolutePath = file1.getAbsolutePath();
            AliYunImageUtils.newInstance().uploadImage(this, IDImageabsolutePath, new ResultListener() {
                @Override
                public void onSucess(String data) {
                    idImageNetPath = data;
                    uploadImageCount++;

                }

                @Override
                public void onFaild() {
                    showToast(getString(R.string.uploading_id_number_phone_failure));
                    uploadImageCount = 0;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static File compressImage(Bitmap image) {
        String filePath = Environment.getExternalStorageDirectory().toString();
        String fileName = System.currentTimeMillis() + ".jpg";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        return byte2File(baos.toByteArray(), filePath, fileName);

    }

    public static File byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 照片标记
     * <p>
     * NONE:未选择
     * <p>
     * ID_CARD：身份证照片
     * <p>
     * HAND_ID_CARD：手持身份证照片
     */
    enum PICTURE_FLAG {
        NONE, ID_CARD, HAND_ID_CARD
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

}
