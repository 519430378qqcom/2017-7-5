package com.lvshandian.lemeng.moudles.mine.my;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;

import butterknife.Bind;

/**
 * 认证提交,正在审核界面
 */

public class RealNameVertifyActivity extends BaseActivity {
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.tv_content)
    TextView tvContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_real_name_vertify;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.my_authentication), null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }
}
