package com.lvshandian.lemeng.moudles.mine.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.utils.KeyBoardUtils;

import butterknife.Bind;

/**
 * 修改昵称界面
 */
public class PersonNameActivity extends BaseActivity {
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.iv_delete)
    ImageView ivDelete;

    private static final int CHANGE_PERSON_NAME = 124;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_name;
    }

    @Override
    protected void initListener() {
        ivDelete.setOnClickListener(this);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDelete.setVisibility(View.GONE);
                } else {
                    ivDelete.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void initialized() {
        initTitle("", "昵称", "保存");
        etName.setText(getIntent().getStringExtra("nickName"));
        etName.setSelection(getIntent().getStringExtra("nickName").length());
        if (getIntent().getStringExtra("nickName").length() > 0)
            ivDelete.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.openKeybord(etName, mContext);
            }
        }, 300);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                KeyBoardUtils.closeKeybord(etName, mContext);
                defaultFinish();
                break;
            case R.id.tv_titlebar_right:
                String strName = etName.getText().toString().trim();
                if (TextUtils.isEmpty(strName)) {
                    showToast("昵称不能为空");
                } else {
                    KeyBoardUtils.closeKeybord(etName, mContext);
                    Intent intent = new Intent();
                    intent.putExtra("personName", strName);
                    setResult(CHANGE_PERSON_NAME, intent);
                    finish();
                }
                break;
            case R.id.iv_delete:
                etName.setText("");
                break;
        }
    }
}
