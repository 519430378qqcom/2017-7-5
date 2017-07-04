package com.lvshandian.lemeng.utils;

import android.Manifest;
import android.content.Context;

import com.lvshandian.lemeng.third.acp.Acp;
import com.lvshandian.lemeng.third.acp.AcpListener;
import com.lvshandian.lemeng.third.acp.AcpOptions;

import java.util.List;

/**
 * Created by gjj on 2016/10/28.
 */
public class PermisionUtils {
    private static PermisionUtils utils;

    private PermisionUtils() {
    }

    public static PermisionUtils newInstance() {
        if (utils == null) {
            synchronized (PermisionUtils.class) {
                if (utils == null) {
                    utils = new PermisionUtils();
                }
            }
        }
        return utils;
    }

    /**
     * 多个权限检查
     *
     * @param context
     * @param permission
     * @param listener
     */
    public void checkPermisson(Context context, String[] permission, AcpListener listener) {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(permission).build(), listener);
    }


    /**
     * 单个权限检查
     *
     * @param context
     * @param permission
     * @param listener
     */
    public void checkPermisson(Context context, String permission, AcpListener listener) {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(permission).build(), listener);
    }


    /**
     * camera权限检查
     *
     * @param lintener
     */
    public void checkCameraPermission(final Context context, final OnPermissionGrantedLintener lintener) {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA).build(), new AcpListener() {
            @Override
            public void onGranted() {
                if (lintener != null) {
                    lintener.permissionGranted();
                }
            }

            @Override
            public void onDenied(List<String> permissions) {
                com.lvshandian.lemeng.utils.ToastUtils.showMessageCenter(context, "摄像权限拒绝");
            }
        });
    }

    /**
     * 授权后回调
     */
    public interface OnPermissionGrantedLintener {
        void permissionGranted();
    }


    /**
     * CallPhone权限检查
     *
     * @param lintener
     */
    public void checkCallPhonePermission(final Context context, final OnPermissionGrantedLintener lintener) {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CALL_PHONE).build(), new AcpListener() {
            @Override
            public void onGranted() {
                if (lintener != null) {

                    lintener.permissionGranted();
                }
            }

            @Override
            public void onDenied(List<String> permissions) {
                com.lvshandian.lemeng.utils.ToastUtils.showMessageCenter(context, "拨打电话权限拒绝");
            }
        });
    }

    /**
     * CallPhone权限检查
     *
     * @param lintener
     */
    public void audioPermission(final Context context, final OnPermissionGrantedLintener lintener) {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.RECORD_AUDIO).build(), new AcpListener() {
            @Override
            public void onGranted() {
                if (lintener != null) {

                    lintener.permissionGranted();
                }
            }

            @Override
            public void onDenied(List<String> permissions) {
                com.lvshandian.lemeng.utils.ToastUtils.showMessageCenter(context,"录音权限拒绝");
            }
        });
    }

    /**
     * 写入sdk权限
     *
     * @param lintener
     */
    public void checkWriteStoragePermission(final Context context, final OnPermissionGrantedLintener lintener) {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build(), new AcpListener() {
            @Override
            public void onGranted() {
                if (lintener != null) {
                    lintener.permissionGranted();
                }
            }

            @Override
            public void onDenied(List<String> permissions) {
                com.lvshandian.lemeng.utils.ToastUtils.showMessageDefault(context, "读写SDK权限拒绝");
            }
        });
    }


    /**
     * 检查定位权限
     *
     * @param listener
     */
    public void checkLocationPermission(final Context context, final OnPermissionGrantedLintener listener) {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION).build(), new AcpListener() {
            @Override
            public void onGranted() {
                if (listener != null) {
                    listener.permissionGranted();
                }
            }

            @Override
            public void onDenied(List<String> permissions) {
                com.lvshandian.lemeng.utils.ToastUtils.showMessageCenter(context, "定位权限拒绝");
            }
        });
    }

}
