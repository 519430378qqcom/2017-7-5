package com.lvshandian.lemeng.third.wangyiyunxin.config.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.lvshandian.lemeng.third.wangyiyunxin.config.DemoCache;

/**
 * Created by hzxuwen on 2015/4/13.
 */
public class Preferences {
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_TOKEN = "token";
    private static final String APP_LOGIN = "appLogin";
    private static final String WYYX_LOGIN = "wyyxLogin";

    public static void saveAppLogin(String appLogin) {
        saveString(APP_LOGIN, appLogin);
    }

    public static String getAppLogin() {
        return getString(APP_LOGIN);
    }

    public static void saveWyyxLogin(String wyyxLogin) {
        saveString(WYYX_LOGIN, wyyxLogin);
    }

    public static String getWyyxLogin() {
        return getString(WYYX_LOGIN);
    }


    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    static SharedPreferences getSharedPreferences() {
        return DemoCache.getContext().getSharedPreferences("Demo", Context.MODE_PRIVATE);
    }
}
