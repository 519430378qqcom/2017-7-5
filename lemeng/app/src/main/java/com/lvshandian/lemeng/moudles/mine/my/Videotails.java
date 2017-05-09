package com.lvshandian.lemeng.moudles.mine.my;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.bean.VideoBean;
import com.lvshandian.lemeng.view.DialogView;
import com.lvshandian.lemeng.view.FullScreenVideoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * Created by zhang on 2016/11/21.
 * 创建短视屏详情界面
 */

public class Videotails extends BaseActivity {

    @Bind(R.id.image_back)
    ImageView imageBack;
    @Bind(R.id.image_delete)
    ImageView imageDelete;
    @Bind(R.id.rl_part)
    AutoRelativeLayout rlPart;
    @Bind(R.id.videoView)
    FullScreenVideoView videoView;
    @Bind(R.id.img_pic)
    ImageView ImageHead;

    private MediaController mediaController;
    private VideoBean videoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.my_activity_videodetails;
    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);
            switch (msg.what) {
                case RequestCode.MY_PHOTO_DELETE_CODE://图片请求列表
                    //发送至OrtherVideoMyFragment
                    VideoBean videoBean = new VideoBean();
                    videoBean.setId("yes");
                    EventBus.getDefault().post(videoBean);
                    //图片删除成功EventBus.getDefault().post(videoBean);
                    Videotails.this.finish();
                    break;
            }

        }
    };

    @Override
    protected void initListener() {
        imageDelete.setOnClickListener(this);
        imageBack.setOnClickListener(this);
    }


    @Override
    protected void initialized() {
        videoBean = (VideoBean) getIntent().getSerializableExtra("video");
        if (getIntent().getStringExtra("isShow").equals("notShow")) {
            imageDelete.setVisibility(View.GONE);
        } else {
            imageDelete.setVisibility(View.VISIBLE);
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        AutoRelativeLayout.LayoutParams lp = new AutoRelativeLayout.LayoutParams(width, width);
        lp.addRule(AutoRelativeLayout.CENTER_VERTICAL);
        ImageHead.setLayoutParams(lp);
        videoView.setLayoutParams(lp);

        String videoPath = videoBean.getUrl();
        ImageLoader.getInstance().displayImage(videoBean.getThumbnailUrl(), ImageHead);
        play(videoPath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back://返回
                finish();
                break;
            case R.id.image_delete://删除
                new DialogView(getContext(), rlPart, "", "相机", "删除", "取消",
                        new DialogView.MyCameraCallback() {
                            @Override
                            public void refreshCallback(int tag) {
                                switch (tag) {
                                    //调用相机
                                    case 1:
                                        break;
                                    //调用相册
                                    case 2:
                                        ConcurrentHashMap map = new ConcurrentHashMap<>();
                                        httpDatas.getDataForJson("删除视频", Request.Method.DELETE, UrlBuilder.photoDelete(videoBean.getId()), map, mHandler, RequestCode.MY_PHOTO_DELETE_CODE);
                                        break;
                                    //取消
                                    case 3:
                                        break;
                                }
                            }
                        });
                break;
        }

    }


    private void play(final String path) {
        mediaController = new MediaController(this);
        videoView.setVideoPath(path);
        // 设置VideView与MediaController建立关联
        videoView.setMediaController(mediaController);
        // 设置MediaController与VideView建立关联
        mediaController.setMediaPlayer(videoView);
        mediaController.setVisibility(View.INVISIBLE);
        // 让VideoView获取焦点
        // videoView.requestFocus();
        // 开始播放
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                ImageHead.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                mp.setLooping(true);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoPath(path);
                videoView.start();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

}
