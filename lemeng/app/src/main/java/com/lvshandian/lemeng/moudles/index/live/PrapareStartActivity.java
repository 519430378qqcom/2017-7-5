package com.lvshandian.lemeng.moudles.index.live;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.base.Constant;
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
import com.lvshandian.lemeng.widget.view.CameraPreviewFrameView;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

import static com.lvshandian.lemeng.base.Constant.gameState;

public class PrapareStartActivity extends BaseActivity {

    @Bind(R.id.surfaceView)
    SurfaceView surfaceView;
    @Bind(R.id.cameraPreview_surfaceView)
    SurfaceView cameraPreview_surfaceView;
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
    @Bind(R.id.twitter)
    ImageView twitter;
    @Bind(R.id.facebook)
    ImageView facebook;
    @Bind(R.id.googleplus)
    ImageView googleplus;
    @Bind(R.id.wechat_circle)
    ImageView wechat_circle;

    private String address;
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
                    bundle.putInt("gameState", gameState);
                    LogUtils.e("START: " + creatReadyBean.toString());
                    Intent intent = new Intent(mContext, StartLiveActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_prapare_start;
    }

    @Override
    protected void initListener() {
        wechat.setOnClickListener(this);
        wechat_circle.setOnClickListener(this);
        twitter.setOnClickListener(this);
        facebook.setOnClickListener(this);
        googleplus.setOnClickListener(this);
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
                    address = getString(R.string.spark);
                } else {
                    address = MyApplication.city;
                }
                tvAddress.setText(address);
            }
        });
//        init();
        initPrapare();
    }

    private StreamingProfile mProfile;
    private CameraStreamingSetting mCameraStreamingSetting;
    private MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    protected MediaStreamingManager mMediaStreamingManager;

    private void initPrapare() {
        mProfile = new StreamingProfile();
        mCameraStreamingSetting = new CameraStreamingSetting();
        mCameraStreamingSetting.setCameraId(mCameraId)
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
//                .setCameraSourceImproved(true)
                .setResetTouchFocusDelayInMs(3000)
//                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setBuiltInFaceBeautyEnabled(true)
                .setFaceBeautySetting(new CameraStreamingSetting.
                        FaceBeautySetting(1f, 1f, 0.8f))
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);
        mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
        mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);

        AspectFrameLayout afl = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        afl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        com.lvshandian.lemeng.widget.view.CameraPreviewFrameView cameraPreviewFrameView =
                (CameraPreviewFrameView) findViewById(R.id
                        .cameraPreview_surfaceView);

        mMediaStreamingManager = new MediaStreamingManager(this, afl, cameraPreviewFrameView,
                AVCodecType.HW_VIDEO_WITH_HW_AUDIO_CODEC); // hw codec
        mMediaStreamingManager.prepare(mCameraStreamingSetting, mMicrophoneStreamingSetting,
                null, mProfile);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mMediaStreamingManager.startStreaming();
            }
        }).start();
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
                    address = getString(R.string.spark);
                } else {
                    address = MyApplication.city;
                }
                tvAddress.setText(address);
                break;
            case R.id.tv_start_live:
                if (Constant.anchorState == 1) {
                    getNetWork();
                } else {
                    showToast(getString(R.string.close_live_function));
                }
                break;
            case R.id.wechat:
                UMUtils.umShareSingle(this, getString(R.string.share_download_title),
                        getString(R.string.share_download_content), appUser.getPicUrl(),
                        UrlBuilder.SHARE_DOWNLOAD_URL, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.wechat_circle:
                UMUtils.umShareSingle(this, getString(R.string.share_download_title),
                        getString(R.string.share_download_content), appUser.getPicUrl(),
                        UrlBuilder.SHARE_DOWNLOAD_URL, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.twitter:
                UMUtils.umShareSingle(this, getString(R.string.share_download_title),
                        getString(R.string.share_download_content), appUser.getPicUrl(),
                        UrlBuilder.SHARE_DOWNLOAD_URL, SHARE_MEDIA.TWITTER);
                break;
            case R.id.facebook:
                UMUtils.umShareSingle(this, getString(R.string.share_download_title),
                        getString(R.string.share_download_content), appUser.getPicUrl(),
                        UrlBuilder.SHARE_DOWNLOAD_URL, SHARE_MEDIA.FACEBOOK);
                break;
            case R.id.googleplus:
                UMUtils.umShareSingle(this, getString(R.string.share_download_title),
                        getString(R.string.share_download_content), appUser.getPicUrl(),
                        UrlBuilder.SHARE_DOWNLOAD_URL, SHARE_MEDIA.GOOGLEPLUS);
                break;

        }
    }

    private void getNetWork() {
        if (NetWorkUtil.getConnectedType(mContext) == 0) {
            initDialog();
            baseDialogTitle.setText(getString(R.string.if_start_live));
            baseDialogLeft.setText(getString(R.string.cancel));
            baseDialogRight.setText(getString(R.string.confirm));
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
    }

    private void startLive() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("name", etTitle.getText().toString().trim());
        map.put("city", address);
        map.put("userId", appUser.getId());
        map.put("privateChat", "1");
        map.put("payForChat", appUser.getPayForVideoChat());
        map.put("livePicUrl", appUser.getPicUrl());
        httpDatas.getDataForJson("开启直播", true, Request.Method.POST, UrlBuilder.START_LIVE, map, mHandler, RequestCode.START_LIVE, TAG);
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
//        if (mCamera == null) {
//            mCamera = getCamera(mCameraId);
//            if (mHolder != null && mCamera != null) {
//                startPreview(mCamera, mHolder);
//            }
//        }
        mMediaStreamingManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        releaseCamera();
        mMediaStreamingManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        releaseCamera();
        mMediaStreamingManager.destroy();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PrapareStart Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
