package com.lvshandian.lemeng.activity;

import android.view.View;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.third.wangyiyunxin.main.activity.WangYiBaseActivity;
import com.lvshandian.lemeng.third.wangyiyunxin.main.fragment.SessionListFragment;

/**
 * Created by sll on 2016/11/21.
 */

public class SessionListActivity extends WangYiBaseActivity {
    SessionListFragment sessionListFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_session_list;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        sessionListFragment = (SessionListFragment) getSupportFragmentManager().findFragmentById(R.id.messages_fragment);
        initTitle("", "消息中心", null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_titlebar_left:
                finish();
                break;
        }
    }

}
