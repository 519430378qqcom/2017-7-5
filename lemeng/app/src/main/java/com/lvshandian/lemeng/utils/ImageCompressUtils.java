package com.lvshandian.lemeng.utils;

import android.app.Activity;


import com.lvshandian.lemeng.widget.view.LoadingDialog;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by gjj on 2016/12/23.
 * <p>
 * 文件(图片)压缩工具
 */

public class ImageCompressUtils {
    private static ImageCompressUtils mUtils;

    private ImageCompressUtils() {
    }

    public static ImageCompressUtils newInstance() {
        if (mUtils == null) {
            synchronized (ImageCompressUtils.class) {
                if (mUtils == null) {
                    mUtils = new ImageCompressUtils();
                }
            }
        }
        return mUtils;
    }

    /**
     * 压缩，采用鲁班压缩(第三方),加入loading图
     *
     * @param activity
     */
    public void compress(Activity activity, File mFile, final CompressResultListener listener) {
        final LoadingDialog loadingDialog = new LoadingDialog(activity);
        Luban.get(activity).putGear(Luban.THIRD_GEAR).load(mFile).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {
                if (loadingDialog != null && !loadingDialog.isShowing()) {
                    loadingDialog.show();
                    loadingDialog.setText("正在压缩~");
                }
            }

            @Override
            public void onSuccess(File file) {

                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (listener != null) {
                    listener.onSuccess(file);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }

                if (listener != null) {
                    listener.onError();
                }
            }
        }).launch();
    }

    /**
     * 压缩结果
     */
    public interface CompressResultListener {
        void onError();

        void onSuccess(File resultFile);
    }
}
