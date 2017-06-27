package com.lvshandian.lemeng.httprequest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.ToastUtils;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.lvshandian.lemeng.widget.view.LoadingDialog;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zhang on 2016/10/11.
 */
public class HttpDatas {
    private View view;
    private UrlBuilder urlBuilder;
    public static final int KEY_CODE = 0;//请求成功码SUCCESS_CODE的int类型
    public static final int KEY_CODE_SUCCESS = 1;//请求成功码SUCCESS_CODE的int类型
    private String SUCCESS_CODE = "0";//成功
    private String SERVERS_CODE = "500";//服务器内部错误
    public static final String info = "data";
    public static final String obj = "obj";
    public static final String error = "error";
    public static final boolean success = false;

    LoadingDialog mDialog;
    private Context context;

    public HttpDatas(Context context, View view) {
        this.view = view;
        this.context = context;
        urlBuilder = new UrlBuilder();
        mDialog = new LoadingDialog(context);
    }

    private FastJsonRequest<SdkHttpResult> fastJsonRequest;
    private FastJsonRequest<NewSdkHttpResult> newfastJsonRequest;
    private BaseJsonRequest baseJsonRequest;


    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getData(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, String tag) {
        url = urlBuilder.build(url, map);
        LogUtils.i(details + ":" + url);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        fastJsonRequest = new FastJsonRequest<SdkHttpResult>(method, url, SdkHttpResult.class, map, new Response.Listener<SdkHttpResult>() {
            @Override
            public void onResponse(SdkHttpResult response) {

                String s = response.toString();
                if (response.getCode().equals(SUCCESS_CODE)) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    LogUtils.i(details + "返回内容:" + response.toString());
                    data.putString(info, response.getData());
                    message.setData(data);
                    message.what = handlerCode;
                    handler.sendMessage(message);
                } else if (response.getCode().equals(SERVERS_CODE)) {
                    LogUtils.i(details + "请求错误:" + response.getMessage());
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        addRequestQueue(tag);
    }

    public void getDataDialog(final String details, boolean isDialog, String url, final Handler handler, final int handlerCode, String tag) {
        url = urlBuilder.SERVER_URL + url;
        LogUtils.i(details + ":" + url);
        if (isDialog) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
        fastJsonRequest = new FastJsonRequest<SdkHttpResult>(url, SdkHttpResult.class, new Response.Listener<SdkHttpResult>() {
            @Override
            public void onResponse(SdkHttpResult response) {
                if (response.getCode().equals(SUCCESS_CODE)) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString(info, response.getData());
                    message.setData(data);
                    message.what = handlerCode;
                    handler.sendMessage(message);
                } else if (response.getCode().equals(SERVERS_CODE)) {
                    LogUtils.i(details + "请求错误:" + response.getMessage());
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        addRequestQueue(tag);
    }

    /**
     * 拼接json字符串作为参数上传
     *
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getDataForJson(final String details, boolean isDialog, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, String tag) {
        LogUtils.i(details + ":" + url);
        if (isDialog) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
        map.put("type", "0");
        // map.put("Charset", "UTF-8");
        // map.put("Content-Type", "application/json");
        // map.put("Accept-Encoding", "gzip,deflate");
        JSONObject params = new JSONObject(map);
        //第二个参数我们传了user=zhangqi。则请求方法就为post
        String url1 = UrlBuilder.SERVER_URL + url;
        LogUtils.e("url1: " + url1);
        baseJsonRequest = new BaseJsonRequest(method, url1, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        SdkHttpResult response = JSON.parseObject(obj.toString(), SdkHttpResult.class);
                        LogUtils.i(details + "返回数据:" + response.toString());

                        if (response.getCode().equals(SUCCESS_CODE)) {
                            Message message = new Message();
                            Bundle data = new Bundle();
                            data.putString(info, response.getData());
                            message.setData(data);
                            message.what = handlerCode;
                            handler.sendMessage(message);
                        } else if (response.getCode().equals(SERVERS_CODE)) {
                            ToastUtils.showMessageDefault(context, response.getMessage());
                            LogUtils.i(details + "请求错误:" + response.getMessage());
                        } else {
                            ToastUtils.showMessageDefault(context, response.getMessage());
                        }
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                }, errorListener);
        addBaseRequestQueue(tag);
    }


    /**
     * 拼接json字符串作为参数上传
     *
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void DataJsonAdmin(final String details, boolean isDialog, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, String tag) {
        LogUtils.i(details + ":" + url);
        if (isDialog) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
        JSONObject params = new JSONObject(map);
        LogUtils.i("JSONObject:" + params.toString());
        //第二个参数我们传了user=zhangqi。则请求方法就为post
        baseJsonRequest = new BaseJsonRequest(method, UrlBuilder.CHARGE_SERVER_URL + url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        NewSdkHttpResult response = JSON.parseObject(obj.toString(), NewSdkHttpResult.class);
                        LogUtils.i(details + ":" + response.toString());

                        if (response.getCode() == KEY_CODE_SUCCESS) {
                            Message message = new Message();
                            Bundle data = new Bundle();
                            data.putString(info, response.getObj());
                            data.putString(error, response.getMsg());
                            message.setData(data);
                            message.what = handlerCode;
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showMessageDefault(context, "修改失败");
                        }
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                }, errorListener);
        addBaseRequestQueue(tag);
    }

    /**
     * 拼接json字符串作为参数上传
     *
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void DataNoloadingAdmin(final String details, boolean isDialog, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, String tag) {
        JSONObject params = new JSONObject(map);
        LogUtils.i("JSONObject:" + params.toString());
        if (isDialog) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
        //第二个参数我们传了user=zhangqi。则请求方法就为post
        baseJsonRequest = new BaseJsonRequest(method, UrlBuilder.CHARGE_SERVER_URL + url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        NewSdkHttpResult response = JSON.parseObject(obj.toString(), NewSdkHttpResult.class);
                        LogUtils.i(details + ":" + response.toString());

                        if (response.getCode() == Integer.parseInt(SUCCESS_CODE)) {
                            Message message = new Message();
                            Bundle data = new Bundle();
                            data.putString(info, response.getObj());
                            data.putString(error, response.getMsg());
                            message.setData(data);
                            message.what = handlerCode;
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showMessageDefault(context, "支付失败");
                        }
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                }, errorListener);
        addBaseRequestQueue(tag);
    }


    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getNewDataCharServer(final String details, boolean isDialog, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, String tag) {
        url = urlBuilder.buildChaServer(url, map);
        LogUtils.i(details + ":" + url);
        if (isDialog) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
        newfastJsonRequest = new FastJsonRequest<NewSdkHttpResult>(method, url, NewSdkHttpResult.class, map, new Response.Listener<NewSdkHttpResult>() {
            @Override
            public void onResponse(NewSdkHttpResult response) {
                if (response.getCode() == 1) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    LogUtils.i(details + "返回内容:" + response.toString());
                    data.putString(info, response.getObj());
                    data.putString(obj, response.getObj());
                    message.setData(data);
                    message.what = handlerCode;
                    handler.sendMessage(message);
                } else if (response.getCode() == 0 || response.getCode() == 401) {
                    ToastUtils.showMessageDefault(context, response.getMsg());
                    LogUtils.i(details + "请求错误:" + response.getMsg());
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        newaddRequestQueue(tag);
    }


    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getNewDataCharServerRefresh(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, final SwipeRefreshLayout refreshLayout, String tag) {
        url = urlBuilder.buildChaServer(url, map);
        LogUtils.i(details + ":" + url);
        newfastJsonRequest = new FastJsonRequest<NewSdkHttpResult>(method, url, NewSdkHttpResult.class, map, new Response.Listener<NewSdkHttpResult>() {
            @Override
            public void onResponse(NewSdkHttpResult response) {
                if (response.getCode() == 1) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    LogUtils.i(details + "返回内容:" + response.toString());
                    data.putString(info, response.getObj());
                    data.putString(obj, response.getObj());
                    message.setData(data);
                    message.what = handlerCode;
                    handler.sendMessage(message);
                } else if (response.getCode() == 0) {
                    ToastUtils.showMessageDefault(context, response.getMsg());
                    LogUtils.i(details + "请求错误:" + response.getMsg());
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setPullUpRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshLayout.setRefreshing(false);
                refreshLayout.setPullUpRefreshing(false);
                ToastUtils.showMessageDefault(context, "网络错误");
            }
        });
        newaddRequestQueue(tag);
    }


    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getNewDataCharServerCode(final String details, boolean isDialog, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, String tag) {
        url = urlBuilder.buildChaServer(url, map);
        LogUtils.i(details + ":" + url);
        if (isDialog) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
        newfastJsonRequest = new FastJsonRequest<NewSdkHttpResult>(method, url, NewSdkHttpResult.class, map, new Response.Listener<NewSdkHttpResult>() {
            @Override
            public void onResponse(NewSdkHttpResult response) {
                LogUtils.i(details + "返回内容:" + response.toString());
                if (response.getCode() == 0) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString(info, response.getObj());
                    data.putString(obj, response.getObj());
                    message.setData(data);
                    message.what = handlerCode;
                    handler.sendMessage(message);
                } else if (response.getCode() == 1) {
                    ToastUtils.showMessageDefault(context, response.getMsg());
                    LogUtils.i(details + "请求错误:" + response.getMsg());
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        newaddRequestQueue(tag);
    }

    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getNewDataCharServerCode1(final String details, boolean isDialog, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, String tag) {
        url = urlBuilder.buildChaServer(url, map);
        LogUtils.i(details + ":" + url);
        if (isDialog) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
        newfastJsonRequest = new FastJsonRequest<NewSdkHttpResult>(method, url, NewSdkHttpResult.class, map, new Response.Listener<NewSdkHttpResult>() {
            @Override
            public void onResponse(NewSdkHttpResult response) {
                LogUtils.i(details + "返回内容:" + response.toString());
                if (response.getCode() == 1) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString(info, response.getObj());
                    data.putString(obj, response.getObj());
                    message.setData(data);
                    message.what = handlerCode;
                    handler.sendMessage(message);
                } else if (response.getCode() == 0) {
                    ToastUtils.showMessageDefault(context, response.getMsg());
                    LogUtils.i(details + "请求错误:" + response.getMsg());
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        newaddRequestQueue(tag);
    }


    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            LogUtils.i("error:" + error.toString());
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    };


    private void newaddRequestQueue(String tag) {
        if (newfastJsonRequest != null) {
            newfastJsonRequest.setTag(tag);
            MyApplication.requestQueueiInstance().add(newfastJsonRequest);
        }
    }

    private void addRequestQueue(String tag) {
        if (fastJsonRequest != null) {
            fastJsonRequest.setTag(tag);
            MyApplication.requestQueueiInstance().add(fastJsonRequest);
        }
    }

    private void addBaseRequestQueue(String tag) {
        if (baseJsonRequest != null) {
            baseJsonRequest.setTag(tag);
            MyApplication.requestQueueiInstance().add(baseJsonRequest);
        }
    }
}

