package com.lvshandian.lemeng.moudles.mine.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;

import butterknife.Bind;

public class AddBankCardActivity extends BaseActivity {
    @Bind(R.id.cardholder_name)
    EditText cardholder_name;
    @Bind(R.id.card_numbers)
    EditText card_numbers;
    @Bind(R.id.card_type)
    EditText card_type;
    @Bind(R.id.bank_address)
    EditText bank_address;
    @Bind(R.id.phone_number)
    EditText phone_number;
    @Bind(R.id.confirm)
    TextView confirm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_bank_card;
    }

    @Override
    protected void initListener() {
        confirm.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.add_bank_card), null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                String tv_cardholder_name = cardholder_name.getText().toString().trim();
                String tv_card_numbers = card_numbers.getText().toString().trim();
                String tv_card_type = card_type.getText().toString().trim();
                String tv_bank_address = bank_address.getText().toString().trim();
                String tv_phone_number = phone_number.getText().toString().trim();
                if (TextUtils.isEmpty(tv_cardholder_name) ||
                        TextUtils.isEmpty(tv_card_numbers) ||
                        TextUtils.isEmpty(tv_card_type) ||
                        TextUtils.isEmpty(tv_bank_address) ||
                        TextUtils.isEmpty(tv_phone_number)) {
                    showToast(getString(R.string.incomplete_information));
                } else {

                }

                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
        }
    }
}
