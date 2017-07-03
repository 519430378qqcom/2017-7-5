package com.lvshandian.lemeng.activity.mine;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

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
                    if(card_numbers.length()<12) {
                        showToast(getString(R.string.card_number_error));
                        return;
                    }
                    addbankcard(tv_cardholder_name, tv_card_numbers, tv_card_type, tv_bank_address, tv_phone_number);
                }

                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
        }
    }

    private void addbankcard(String s1, String s2, String s3, String s4, String s5) {
        String url = UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.ADD_BANK_CARD;
        OkHttpUtils.post().url(url).addParams("userId", appUser.getId()).addParams("username", s1)
                .addParams("cardNum", s2).addParams("cardType", s3).addParams("mobile", s5)
                .addParams("bankAddress", s4).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("1".equals(obj.getString("code"))) {
                        finish();
                    } else {
                        //
                    }
                    showToast(obj.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
