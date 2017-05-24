package com.lvshandian.lemeng.moudles.mine.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.utils.KeyBoardUtils;
import com.lvshandian.lemeng.utils.TextUtils;
import com.nostra13.universalimageloader.utils.L;

import butterknife.Bind;

/**
 * 更改签名
 * Created by Administrator on 2017/1/19.
 */

public class UpdatePersonActivity extends BaseActivity {
    @Bind(R.id.et_reply_content)
    EditText content;
    @Bind(R.id.tv_num)
    TextView tv_num;
    private int numbers = 30;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_person;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        initTitle("", "签名", "完成");
        String qianming = getIntent().getStringExtra("name");
        content.setText(qianming);
        if (qianming.length() > 30) {
            content.setSelection(30);
        } else {
            content.setSelection(getIntent().getStringExtra("name").length());
        }
        int textnum = numbers - getIntent().getStringExtra("name").length();
        tv_num.setText("(" + textnum + ")");
        content.addTextChangedListener(textWatcher);
        content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.openKeybord(content, mContext);
            }
        }, 300);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                KeyBoardUtils.closeKeybord(content, mContext);
                defaultFinish();
                break;
            case R.id.tv_titlebar_right:
                Intent intent = new Intent();
                String qianming = content.getText().toString().trim();
                if (!TextUtils.isEmpty(qianming)) {
                    KeyBoardUtils.closeKeybord(content, mContext);
                    intent.putExtra("qianming", qianming);
                    setResult(2, intent);
                    finish();
                }

                break;
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        private String temp;


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s.toString();
            L.i("before :" + temp);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            int length = text.length();
            int textnum = numbers - length;
            tv_num.setText("(" + textnum + ")");

        }
    };
}
