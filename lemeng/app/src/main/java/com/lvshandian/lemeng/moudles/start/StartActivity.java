package com.lvshandian.lemeng.moudles.start;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.lvshandian.lemeng.MainActivity;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.wangyiyunxin.config.preference.Preferences;


/**
 * 启动界面 on 2016/10/20.
 */
public class StartActivity extends BaseActivity {

    private Thread thread;
    private int zhuan = 1;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (Preferences.getAppLogin().equals("1") && Preferences.getWyyxLogin().equals("1")) {
                    goToMain();
                } else {
                    Intent intent = new Intent(StartActivity.this, LoginSelectActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start_layout;
    }


    @Override
    protected void initialized() {
        thread = new Thread(new ThreadShow());
        thread.start();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View v) {

    }

    // 线程类
    class ThreadShow implements Runnable {
        @Override
        public void run() {
            while (zhuan == 1) {
                try {
                    Thread.sleep(2000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    zhuan = 2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 去主界面activity
     */
    private void goToMain() {
        if (appUser == null) {
            Intent intent = new Intent(StartActivity.this, LoginSelectActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        }
        defaultFinish();
    }

}
