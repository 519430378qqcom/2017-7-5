package com.lvshandian.lemeng.interfaces;

import android.os.Environment;

/**
 * Created by gjj on 2016/12/9.
 */

public class Constant {
    public static final String TWITTER_APPID = "ya5x9703zlnsjexnes0IxHbpQ";
    public static final String TWITTER_SECRET = "EDnxF6yfwfZUuBKMTxOuC9gRDzRoKoEoZGnTHfHMKadMBdXk9T";

    public static final String WX_APPID = "wx73481b57dca86667";
    public static final String WX_SECRET = "abe3e20489202a133f8ae11ef3e265f5";

    public static final String APP_PATH = Environment.getExternalStorageDirectory() + "/lemeng/";
    /**
     * 是否可以开直播
     */
    public static int anchorState = 1; //0不可  1可
    /**
     * 是否可以开私信
     */
    public static int privateState = 1; //0不可  1可
    /**
     * 是否可以开游戏
     */
    public static int gameState = 1; //0不可  1可
}
