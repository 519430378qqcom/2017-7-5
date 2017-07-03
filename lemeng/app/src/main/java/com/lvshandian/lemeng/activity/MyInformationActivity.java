package com.lvshandian.lemeng.activity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.fragment.MyInformationFragment;

/**
 * 我的个人中心
 */
public class MyInformationActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_infomation_my;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.my_information, new MyInformationFragment());
        ft.commit();
    }

    @Override
    public void onClick(View v) {

    }
}
