package com.lvshandian.lemeng.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.lvshandian.lemeng.moudles.index.live.AudioPlayerActivity;
import com.lvshandian.lemeng.moudles.index.live.StartLiveActivity;

import java.io.IOException;


/**
 * Created by Administrator on 2017/3/7.
 */

public class VoiceService extends Service {

    private String mUrl = "";
    /**
     * 播放音乐，
     */
    public static MediaPlayer mediaPlayer;

    public MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer
            .OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            play();

        }
    };


    public MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    };
    public MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer
            .OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mUrl = "";
            AudioPlayerActivity.playingId = "";
            AudioPlayerActivity.pauseingId = "";
            StartLiveActivity.mLrcView.setVisibility(View.GONE);
            //发送广播通知歌曲播完,改变界面
            Intent intent = new Intent();
            intent.setAction("SONG_PLAYING_END");
            sendBroadcast(intent);
            next();
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "voiceService + onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("URL");
        String lrc = intent.getStringExtra("LRC");
        if (url.equals(mUrl)) {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                StartLiveActivity.mLrcView.setVisibility(View.GONE);
            }else{
                mediaPlayer.start();
                StartLiveActivity.mLrcView.setVisibility(View.VISIBLE);
            }
        } else {
            openAudio(url,lrc);
        }
        mUrl = url;

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG", "onBind()");
        return null;
    }

    /**
     * 音频播放
     */
    public void play() {
        mediaPlayer.start();
    }

    /**
     * 音频暂定
     */
    public void pause() {
        mediaPlayer.pause();
    }


    /**
     * 根据URL 播放音乐
     *
     * @param url
     */
    public void openAudio(String url,String lrc) {
        Log.e("TAG", "服务里传递的URL=" + url);
        if (mediaPlayer != null) {
            //把上一个音频资源释放
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        //设置准备，错误，播放完成的监听
        mediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mediaPlayer.setOnErrorListener(mOnErrorListener);
        mediaPlayer.setOnCompletionListener(mOnCompletionListener);


        //设置播放地址
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.start();
            StartLiveActivity.mLrcView.setVisibility(View.VISIBLE);
            StartLiveActivity.showLrc(lrc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 得到音频的总时长
     *
     * @return
     */
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 得到音频播放的当前进度
     *
     * @return
     */
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 得到音频名称
     *
     * @return
     */
    public String getAudioName() {
        return "";
    }

    /**
     * 是否在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * 得到播放地址
     *
     * @return
     */
    public String getAudioPath() {
//        if (url != null) {
//            return url;
//        }
        return "";
    }

    /**
     * 播放下一个
     */
    public void next() {

    }

    /**
     * 播放上一个
     */
    public void pre() {

    }

    /**
     * 根据具体的位置拖动音乐继续播放
     */
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("TAG", "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
        Log.e("TAG", "voiceService + onDestory()");
    }

}
