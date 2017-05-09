package com.lvshandian.lemeng.moudles.mine.my;

import android.view.View;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;

import butterknife.Bind;

/**
 * 显示文本的Activity
 */
public class BaseTextActivity extends BaseActivity {
    @Bind(R.id.tv_text)
    TextView tvText;
    /**
     * 根据传过来type显示内容
     */
    String type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_text;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        type = getIntent().getStringExtra("type");
        initTitle("", type, null);
        initView(type);
    }

    private void initView(String type) {
        if (type.equals("社区公约")) {
            tvText.setText("社区公约");
        } else if (type.equals("用户协议") || type.equals("直播公约")) {
            tvText.setText("用户协议||直播公约");
        } else if (type.equals("服务条款")) {
            tvText.setText("服务条款");
        } else if (type.equals("隐私条款")) {
            tvText.setText("隐私条款");
        }
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
