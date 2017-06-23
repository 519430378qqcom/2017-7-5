package com.lvshandian.lemeng.base;

import android.os.Environment;

/**
 * Created by gjj on 2016/12/9.
 */

public class Constant {
    public static final String TWITTER_APPID = "ya5x9703zlnsjexnes0IxHbpQ";
    public static final String TWITTER_SECRET = "EDnxF6yfwfZUuBKMTxOuC9gRDzRoKoEoZGnTHfHMKadMBdXk9T";

    public static final String WX_APPID = "wxe61dc77fc858f0a7";
    public static final String WX_SECRET = "3fa0cd0209d77c405d4c9f945182f6ec";

    public static final String APP_PATH = Environment.getExternalStorageDirectory() + "/lemeng/";
    /**
     * 是否可以开直播
     */
    public static int anchorState; //0不可  1可
    /**
     * 是否可以开私信
     */
    public static int privateState; //0不可  1可
    /**
     * 是否可以开游戏
     */
    public static int gameState; //0不可  1可
}
