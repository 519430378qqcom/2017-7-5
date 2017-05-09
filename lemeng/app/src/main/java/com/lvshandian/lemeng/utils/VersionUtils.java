package com.lvshandian.lemeng.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by gjj on 2016/9/22.
 * 获取当前应用版本号和版本名称
 */
public class VersionUtils {

    /**
     * 返回当前程序版本号
     */
    public static int getVersionCode(Context context) {
        String versionName = "";//版本名称
        int vsersionCode;//版本号
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            vsersionCode = pi.versionCode;
            return vsersionCode;
        } catch (Exception e) {

        }
        return 1;
    }

    /**
     * 获取当前程序版本名称
     *
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";//版本名称
        int vsersionCode;//版本号
        try {
            PackageManager pm =context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            vsersionCode = pi.versionCode;
            return versionName;
        } catch (Exception e) {

        }
        return "";
    }

    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }
}
