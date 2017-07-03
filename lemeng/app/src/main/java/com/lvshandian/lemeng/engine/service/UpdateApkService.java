package com.lvshandian.lemeng.engine.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.interfaces.Constant;
import com.lvshandian.lemeng.entity.VersionUpdateBean;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.widget.view.RoundDialog;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.NetWorkUtil;
import com.lvshandian.lemeng.utils.ToastUtils;
import com.lvshandian.lemeng.utils.VersionUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.lvshandian.lemeng.R.style.dialog;

/**
 * Created by shangshuaibo on 2016/12/27 18:49
 */
public class UpdateApkService extends Service {
    private VersionUpdateBean versionUpdateBean;
    private Activity mContext = MyApplication.getActivityByName("MainActivity");
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private NotificationManager manager;
    private Notification notif;

    private RoundDialog netWorkDialog;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        versionUpdate();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    notif.contentView.setTextViewText(R.id.content_view_text1, mContext.getString(R.string.download_is_in_progress) +
                            progress + "%");
                    notif.contentView.setProgressBar(R.id.content_view_progress, 100, progress,
                            false);
                    manager.notify(0, notif);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    notif.contentView.setTextViewText(R.id.content_view_text1, mContext.getString(R.string.download_finish));
                    notif.contentView.setProgressBar(R.id.content_view_progress, 100, 100, false);
                    manager.notify(0, notif);
//                    manager.cancel(0);
                    installApk();
                    break;
            }
        }
    };


    /**
     * 检测版本更新
     */
    private void versionUpdate() {
        String url = UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.VERSION_UPDATE;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("UpdateApkService:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("1")) {
                        String obj = jsonObject.getString("obj");
                        versionUpdateBean = JsonUtil.json2Bean(obj, VersionUpdateBean.class);
                        if (versionUpdateBean == null)
                            return;
                        if (versionUpdateBean.getVersionCode() > VersionUtils.getVersionCode(getApplicationContext())) {
                            showNoticeDialog();
                        } else {
                            // 没有新版本,停止服务
                            stopSelf();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.the_new_version_is_detected));
        builder.setMessage(versionUpdateBean.getUpdateContent());
        // 更新
        builder.setPositiveButton(mContext.getString(R.string.update_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //强制更新
                if (versionUpdateBean.getForceUpdate().equals("1")) {

                } else if (versionUpdateBean.getForceUpdate().equals("0")) {
                    //非强制更新
                    dialog.dismiss();
                }
                if (NetWorkUtil.getConnectedType(mContext) == 1) {
                    ToastUtils.showMessageDefault(mContext, mContext.getString(R.string.background_updates));
                    showDownloadDialog();
                } else if (NetWorkUtil.getConnectedType(mContext) == 0) {
                    isMobileNetwork();
                } else {
                    ToastUtils.showMessageDefault(mContext, mContext.getString(R.string.network_error));
                }
            }
        });
        // 暂不更新
        builder.setNegativeButton(mContext.getString(R.string.not_update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //强制更新
                if (versionUpdateBean.getForceUpdate().equals("1")) {
                    dialog.dismiss();
                    mContext.finish();
                    stopSelf();
                    System.exit(0);
                } else if (versionUpdateBean.getForceUpdate().equals("0")) {
                    //非强制更新
                    dialog.dismiss();
                    stopSelf();
                }
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.setCancelable(false);
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.show();

    }


    private void isMobileNetwork() {
        View view = mContext.getLayoutInflater().inflate(R.layout.dialog_quit_login, null);
        if (netWorkDialog == null) {
            netWorkDialog = new RoundDialog(this, view, dialog, 0.66f, 0.2f);
        }
        netWorkDialog.setCanceledOnTouchOutside(false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvTitle.setText(mContext.getString(R.string.is_mobile_network_if_updates));
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netWorkDialog.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadDialog();
                netWorkDialog.dismiss();
            }
        });
        if (!netWorkDialog.isShowing()) {
            netWorkDialog.show();
        }

    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notif = new Notification();
        notif.icon = R.drawable.ic_launcher;
        notif.tickerText = mContext.getString(R.string.update_notification);
        //通知栏显示所用到的布局文件
        notif.contentView = new RemoteViews(mContext.getPackageName(), R.layout.notify_layout);
        notif.flags |= Notification.FLAG_INSISTENT;
        manager.notify(0, notif);
        // 下载文件
        downloadApk();
    }


    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    int downloadCount = 0;

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    mSavePath = Constant.APP_PATH + "download";
                    URL url = new URL(versionUpdateBean.getUpdateUrl());
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File apkFile = new File(mSavePath, "lemeng.apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        //为了防止频繁的通知导致应用吃紧，百分比增加5才通知一次
                        if (downloadCount == 0 || progress - 5 > downloadCount) {
                            downloadCount += 5;
                            mHandler.sendEmptyMessage(DOWNLOAD);
                        }
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkFile = new File(mSavePath, "lemeng.apk");
        if (!apkFile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android" +
                ".package-archive");
        mContext.startActivity(intent);

    }
}
