package com.lvshandian.lemeng.moudles.index.live;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.CreatReadyBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.utils.CameraUtil;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.MyCamPara;
import com.lvshandian.lemeng.utils.NetWorkUtil;
import com.lvshandian.lemeng.utils.PermisionUtils;
import com.lvshandian.lemeng.utils.UMUtils;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

public class PrapareStartActivity extends BaseActivity {

    //    @Bind(R.id.iv_bg)
//    ImageView ivBg;
    @Bind(R.id.surfaceView)
    SurfaceView surfaceView;
    @Bind(R.id.iv_colse)
    ImageView ivColse;
    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.tv_start_live)
    TextView tvStartLive;
    @Bind(R.id.wechat)
    ImageView wechat;
    @Bind(R.id.qq)
    ImageView qq;
    @Bind(R.id.qq_zone)
    ImageView qq_zone;
    @Bind(R.id.wechat_circle)
    ImageView wechat_circle;

    private String address = "火星";
    private boolean isHideAddress;

    /**
     * 屏幕宽
     */
    private int screenWidth;

    /**
     * 屏幕高
     */
    private int screenHeight;

    /**
     * 图片宽
     */
    private int picHeight;

    private SurfaceHolder mHolder;

    /**
     * 相机
     */
    private Camera mCamera;

    /**
     * 默认前置或者后置相机 这里暂时设置为前置
     */
    private int mCameraId = CAMERA_FRONT;
    private static final int CAMERA_FRONT = 1;
    private static final int CAMERA_BACK = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);
            switch (msg.what) {
                case RequestCode.START_LIVE:
                    CreatReadyBean creatReadyBean = JsonUtil.json2Bean(json, CreatReadyBean.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("START", creatReadyBean);
                    LogUtils.e("START: " + creatReadyBean.toString());
                    Intent intent = new Intent(mContext, StartLiveActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_prapare_start;
    }

    @Override
    protected void initListener() {
        wechat.setOnClickListener(this);
        qq.setOnClickListener(this);
        qq_zone.setOnClickListener(this);
        wechat_circle.setOnClickListener(this);
        ivColse.setOnClickListener(this);
        tvStartLive.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        if (!TextUtils.isEmpty(appUser.getPicUrl())) {
            Picasso.with(mContext).load(appUser.getPicUrl()).placeholder(R.mipmap.head_default)
                    .error(R.mipmap.head_default).into(ivHead);
//            Picasso.with(mContext).load(appUser.getPicUrl()).error(R.mipmap.head_default).into(ivBg);
        }
        PermisionUtils.newInstance().checkLocationPermission(this, new PermisionUtils.OnPermissionGrantedLintener() {
            @Override
            public void permissionGranted() {
                if (TextUtils.isEmpty(MyApplication.city)) {
                    address = "火星";
                } else {
                    address = MyApplication.city;
                }
                tvAddress.setText(address);
            }
        });
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_colse:
                finish();
                break;
            case R.id.tv_address:
                isHideAddress = !isHideAddress;
                if (isHideAddress) {
                    address = "火星";
                } else {
                    address = MyApplication.city;
                }
                tvAddress.setText(address);
                break;
            case R.id.tv_start_live:
                if (NetWorkUtil.getConnectedType(mContext) == 0) {
                    initDialog();
                    baseDialogTitle.setText("当前为移动网络,是否开启直播");
                    baseDialogLeft.setText("取消直播");
                    baseDialogRight.setText("继续直播");
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
                            startLive();
                        }
                    });
                } else {
                    startLive();
                }
                break;
            case R.id.wechat:
                UMUtils.umShareSingle(this, appUser.getNickName(), appUser.getPicUrl(),
                        "http://app.lemenglive.com/video/share.html?userId=" + appUser.getId(), SHARE_MEDIA.WEIXIN);
                break;
            case R.id.wechat_circle:
                UMUtils.umShareSingle(this, appUser.getNickName(), appUser.getPicUrl(),
                        "http://app.lemenglive.com/video/share.html?userId=" + appUser.getId(), SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.qq:
                UMUtils.umShareSingle(this, appUser.getNickName(), appUser.getPicUrl(),
                        "http://app.lemenglive.com/video/share.html?userId=" + appUser.getId(), SHARE_MEDIA.QQ);
                break;
            case R.id.qq_zone:
                UMUtils.umShareSingle(this, appUser.getNickName(), appUser.getPicUrl(),
                        "http://app.lemenglive.com/video/share.html?userId=" + appUser.getId(), SHARE_MEDIA.QZONE);
                break;

        }
    }

    private void startLive() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("name", etTitle.getText().toString().trim());
        map.put("city", address);
        map.put("userId", appUser.getId());
        map.put("privateChat", "1");
        map.put("payForChat", appUser.getPayForVideoChat());
        map.put("livePicUrl", appUser.getPicUrl());
        httpDatas.getDataForJson("开启直播", Request.Method.POST, UrlBuilder.START, map, mHandler, RequestCode.START_LIVE);
    }


    /**
     * 初始化
     */
    public void init() {
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(mCallBack);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //在surface创建的时候开启相机预览
            startPreview(mCamera, holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //在相机改变的时候调用此方法， 此时应该先停止预览， 然后重新启动
            mCamera.stopPreview();
            startPreview(mCamera, holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            //在destroy的时候释放相机资源
            releaseCamera();
        }
    };

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            //这里要设置相机的一些参数，下面会详细说下
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);

            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // Autofocus mode is supported 自动对焦
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                // parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }

            //这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
            Camera.Size previewSize = MyCamPara.getInstance().getPreviewSize(parameters.getSupportedPreviewSizes(), screenWidth);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            Camera.Size pictrueSize = MyCamPara.getInstance().getPictureSize(parameters.getSupportedPictureSizes(), screenWidth);
            parameters.setPictureSize(pictrueSize.width, pictrueSize.height);
            camera.setParameters(parameters);

            picHeight = screenWidth * previewSize.width / previewSize.height;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, picHeight);
            //这里当然可以设置拍照位置 比如居中 我这里就置顶了
//            params.gravity = Gravity.CENTER;
            surfaceView.setLayoutParams(params);
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null && mCamera != null) {
                startPreview(mCamera, mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        releaseCamera();
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return camera;
    }
}
