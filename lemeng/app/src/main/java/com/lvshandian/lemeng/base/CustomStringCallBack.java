package com.lvshandian.lemeng.base;

import android.content.Context;

import com.lvshandian.lemeng.utils.LogUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gjj on 2016/11/18.
 * 按照自己的服务器返回数据封装OkHttpUtils回调
 */

public abstract class CustomStringCallBack extends StringCallback {
    /**
     * 请求成功后服务器返回的code值
     */
    private int keyCode;


    public CustomStringCallBack(Context context, int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public void onBefore(Request request) {
    }

    @Override
    public void onAfter() {
    }

    @Override
    public void onError(Request request, Exception e) {
        onFaild();
    }

    @Override
    public void onResponse(String response) {
        LogUtils.e("response: " + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == keyCode) {
                String data = jsonObject.getString("data");
                onSucess(data);
            } else {
                onFaild();
            }
        } catch (JSONException e) {
            onFaild();
        }
    }

    public abstract void onFaild();

    public abstract void onSucess(String data);
}
