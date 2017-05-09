package com.lvshandian.lemeng.moudles.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.moudles.mine.my.BaseTextActivity;
import com.lvshandian.lemeng.utils.VersionUtils;

import butterknife.Bind;

/**
 * 关于海颜界面
 * Created by Administrator on 2017/1/18.
 */

public class AboutHaiyanActivity extends BaseActivity {
    @Bind(R.id.tv_version_name)
    TextView tvVersionName;
    @Bind(R.id.ll_community)
    LinearLayout llCommunity;
    @Bind(R.id.ll_secret)
    LinearLayout llSecret;
    @Bind(R.id.ll_service)
    LinearLayout llService;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_haiyan;
    }

    @Override
    protected void initListener() {
        llCommunity.setOnClickListener(this);
        llSecret.setOnClickListener(this);
        llService.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", "关于海颜", null);
        String versionName = VersionUtils.getVersionName(mContext);
        tvVersionName.setText("当前版本" + versionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
            case R.id.ll_community:
                Intent intent1 = new Intent(mContext, BaseTextActivity.class);
                intent1.putExtra("type","社区公约");
                startActivity(intent1);
                break;
            case R.id.ll_secret:
                Intent intent2 = new Intent(mContext, BaseTextActivity.class);
                intent2.putExtra("type","隐私条款");
                startActivity(intent2);
                break;
            case R.id.ll_service:
                Intent intent3 = new Intent(mContext, BaseTextActivity.class);
                intent3.putExtra("type","服务条款");
                startActivity(intent3);
                break;
        }
    }
}
