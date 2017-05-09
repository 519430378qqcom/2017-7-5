package com.lvshandian.lemeng.moudles.mine.activity;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;

import butterknife.Bind;

/**
 * 微信公众号提现提示
 * Created by Administrator on 2017/3/28.
 */

public class WeichatDraw extends BaseActivity {
    @Bind(R.id.text)
    TextView text;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weichat_draw;
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initialized() {
        initTitle("", "微信公众号提现", null);
        text.setText(Html.fromHtml(
                "<font color='#666666'>" + "请搜索微信公众号    " + "</font>" +
                        "<font color='#000000'><u><big>" + "乐檬LIVE" + "</big></u></font>"
                        + "<font color='#666666'>" + "    进行提现" + "</font>"));
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
