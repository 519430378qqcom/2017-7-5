package com.lvshandian.lemeng.utils;

import android.util.Log;

/**
 * Log打印Utils
 *
 * @author zhang
 */
public class LogUtils {

    private LogUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static final String TAG = "lsd";

    //可以全局控制是否打印log日志
    private static int LOG_MAXLENGTH = 2000;

    public static void LogAll(String msg) {
        if (Constant.LOGDEBUG) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e("logall___" + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e("logall___" + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void LogAll(String type, String msg) {

        if (Constant.LOGDEBUG) {

            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e(type + "___" + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e(type + "___" + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }


    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (Constant.LOGDEBUG)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (Constant.LOGDEBUG)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (Constant.LOGDEBUG)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (Constant.LOGDEBUG)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (Constant.LOGDEBUG)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (Constant.LOGDEBUG)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (Constant.LOGDEBUG)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (Constant.LOGDEBUG)
            Log.i(tag, msg);
    }

    public static void j(String msg) {
        if (Constant.LOGDEBUG)
            Log.d("Jooper", msg);
    }

    public static void n(String msg) {
        if (Constant.LOGDEBUG)
            Log.d("zyn", msg);
    }
}