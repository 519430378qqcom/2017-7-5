package com.lvshandian.lemeng.bean.lemeng;

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
    private final int KEY_CODE = 1;
    private final String SUCCESS_MSG = "obj";
    private final String CODE = "code";


    public CustomStringCallBack() {

     /*   mDialog = new LoadingDialog(context);
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);*/
    }

    @Override
    public void onBefore(Request request) {
      /*  if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }*/
    }

    @Override
    public void onAfter() {
      /*  if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }*/
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
            int code = jsonObject.getInt(CODE);
            if (code == KEY_CODE) {
                String data = jsonObject.getString(SUCCESS_MSG);
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
