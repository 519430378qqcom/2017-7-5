package com.lvshandian.lemeng.activity.mine;

import android.view.View;
import android.widget.Toast;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;

/**
 * 系统消息
 * Created by gjj on 2016/11/21.
 */

public class SystemMessageActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_message;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        initTitle("", "自定义系统通知", "清空");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
            case R.id.tv_titlebar_right:
                Toast.makeText(mContext, "清空", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
