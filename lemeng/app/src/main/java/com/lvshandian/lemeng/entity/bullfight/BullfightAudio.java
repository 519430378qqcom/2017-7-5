package com.lvshandian.lemeng.entity.bullfight;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.lvshandian.lemeng.R;

import java.util.HashMap;
import java.util.Map;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/7
 * version: 1.0
 * desc   : 牛牛游戏音效的管理类
 */

public class BullfightAudio {

    private SoundPool soundPool;
    /**
     * 音效id对应关系
     */
    private Map<String, Integer> soundMap;
    public final String Bull0 = "bull0";
    public final String Bull1 = "bull1";
    public final String Bull2 = "bull2";
    public final String Bull3 = "bull3";
    public final String Bull4 = "bull4";
    public final String Bull5 = "bull5";
    public final String Bull6 = "bull6";
    public final String Bull7 = "bull7";
    public final String Bull8 = "bull8";
    public final String Bull9 = "bull9";
    public final String Bull10 = "bullbull";
    public final String WIN = "win";
    public final String FAIL = "fail";
    public final String BET = "bet";
    public final String BET_COIN = "coin";
    public final String FALLING_COIN = "coin";

    public BullfightAudio(Context context) {
        SoundPool.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new SoundPool.Builder();
            builder.setMaxStreams(10);
            soundPool = builder.build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_ALARM, 0);
        }
        soundMap = new HashMap<>();
        int load = soundPool.load(context, R.raw.bet_coin, 1);
        int load1 = soundPool.load(context, R.raw.bull0, 1);
        int load2 = soundPool.load(context, R.raw.bull1, 1);
        int load3 = soundPool.load(context, R.raw.bull2, 1);
        int load4 = soundPool.load(context, R.raw.bull3, 1);
        int load5 = soundPool.load(context, R.raw.bull4, 1);
        int load6 = soundPool.load(context, R.raw.bull5, 1);
        int load7 = soundPool.load(context, R.raw.bull6, 1);
        int load8 = soundPool.load(context, R.raw.bull7, 1);
        int load9 = soundPool.load(context, R.raw.bull8, 1);
        int load10 = soundPool.load(context, R.raw.bull9, 1);
        int load11 = soundPool.load(context, R.raw.bull10, 1);
        int load12 = soundPool.load(context, R.raw.bull_win, 1);
        int load13 = soundPool.load(context, R.raw.bull_lose, 1);
        int load14 = soundPool.load(context, R.raw.win_coin, 1);
        int load15 = soundPool.load(context, R.raw.falling_coin, 1);
        soundMap.put(BET, load);
        soundMap.put(Bull0,load1);
        soundMap.put(Bull1,load2);
        soundMap.put(Bull2,load3);
        soundMap.put(Bull3,load4);
        soundMap.put(Bull4,load5);
        soundMap.put(Bull5,load6);
        soundMap.put(Bull6,load7);
        soundMap.put(Bull7,load8);
        soundMap.put(Bull8,load9);
        soundMap.put(Bull9,load10);
        soundMap.put(Bull10,load11);
        soundMap.put(WIN,load12);
        soundMap.put(FAIL,load13);
        soundMap.put(BET_COIN,load14);
        soundMap.put(FALLING_COIN,load15);
    }
    public void play(String audioName){
        soundPool.play(soundMap.get(audioName),1,1,0,0,1);
    }
    public void play(int bullSum){
        String audioName = "";
        switch (bullSum){
            case 0:
                audioName = Bull0;
                break;
            case 1:
                audioName = Bull1;
                break;
            case 2:
                audioName = Bull2;
                break;
            case 3:
                audioName = Bull3;
                break;
            case 4:
                audioName = Bull4;
                break;
            case 5:
                audioName = Bull5;
                break;
            case 6:
                audioName = Bull6;
                break;
            case 7:
                audioName = Bull7;
                break;
            case 8:
                audioName = Bull8;
                break;
            case 9:
                audioName = Bull9;
                break;
            case 10:
                audioName = Bull10;
                break;
        }
        play(audioName);
    }
    public void release(){
        soundPool.release();
    }
}
