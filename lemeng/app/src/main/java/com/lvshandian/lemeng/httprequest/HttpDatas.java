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
import com.lvshandian.lemeng.view.LoadingDialog;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;

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

    private boolean isDialog = true;

    public HttpDatas(Context context, View view) {
        this.view = view;
        this.context = context;
        urlBuilder = new UrlBuilder();
        mDialog = new LoadingDialog(context);
    }

    private FastJsonRequest<SdkHttpResult> fastJsonRequest;
    private FastJsonRequest<NewSdkHttpResult> newfastJsonRequest;

    /**
     * @param details     接口名
     * @param url         接口地址
     * @param handler
     * @param handlerCode
     */
    public void getData(String details, String url, final Handler handler, final int handlerCode) {
        url = urlBuilder.serverUrl + url;
        LogUtils.i(details + ":" + url);
        if (!mDialog.isShowing()) {
            mDialog.show();
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
                    ToastUtils.showMessageDefault(context, "服务器内部错误");
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        addRequestQueue();
    }

    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getData(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode) {
        url = urlBuilder.build(url, map);
        LogUtils.i(details + ":" + url);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        fastJsonRequest = new FastJsonRequest<SdkHttpResult>(method, url, SdkHttpResult.class, map, new Response.Listener<SdkHttpResult>() {
            @Override
            public void onResponse(SdkHttpResult response) {

                String s = response.toString();
                LogUtils.i(details + "返回内容:" + response.toString());
                if (response.getCode().equals(SUCCESS_CODE)) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    LogUtils.i(details + "返回内容:" + response.toString());
                    data.putString(info, response.getData());
                    message.setData(data);
                    message.what = handlerCode;
                    handler.sendMessage(message);
                } else if (response.getCode().equals(SERVERS_CODE)) {
                    LogUtils.i("服务器内部错误:" + response.getMessage());
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        addRequestQueue();
    }

    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getDataNoLoading(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, final SwipeRefreshLayout refreshLayout) {
        url = urlBuilder.build(url, map);
        LogUtils.i(details + ":" + url);
        fastJsonRequest = new FastJsonRequest<SdkHttpResult>(method, url, SdkHttpResult.class, map, new Response.Listener<SdkHttpResult>() {
            @Override
            public void onResponse(SdkHttpResult response) {

                String s = response.toString();
                LogUtils.e("WXPAY： " + s);
                if (response.getCode().equals(SUCCESS_CODE)) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    LogUtils.i(details + "返回内容:" + response.toString());
                    data.putString(info, response.getData());
                    message.setData(data);
                    message.what = handlerCode;
                    handler.sendMessage(message);
                } else if (response.getCode().equals(SERVERS_CODE)) {
                    LogUtils.i("服务器内部错误:" + response.getMessage());
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setPullUpRefreshing(false);
                    ToastUtils.showMessageDefault(context, "查询失败");
                } else {
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setPullUpRefreshing(false);
                    ToastUtils.showMessageDefault(context, "查询失败");

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
        addRequestQueue();
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
    public void getDataForJson(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode) {
        LogUtils.i(details + ":" + url);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        map.put("type", "0");
        // map.put("Charset", "UTF-8");
        //  map.put("Content-Type", "application/json");
//        map.put("Accept-Encoding", "gzip,deflate");
        JSONObject params = new JSONObject(map);
        //第二个参数我们传了user=zhangqi。则请求方法就为post
        String url1 = UrlBuilder.serverUrl + url;
        LogUtils.e("url1: " + url1);
        BaseJsonRequest objRequest = new BaseJsonRequest(method, url1, params,
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
//                            ToastUtils.showSnackBar(view, response.getMessage());
                            ToastUtils.showMessageDefault(context, response.getMessage());
//                            AppMsg.makeText((Activity) context, response.getMessage(), new AppMsg.Style(1000, R.mipmap.toast_background)).show();
                            LogUtils.i("服务器内部错误:" + response.getMessage());
                        } else {
//                            AppMsg.makeText((Activity) context, response.getMessage(), new AppMsg.Style(1000, R.mipmap.toast_background)).show();
                            ToastUtils.showMessageDefault(context, response.getMessage());
                        }
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                }, errorListener);
        // 将这个request加入到requestQueue中，就可以执行了
        MyApplication.requestQueueiInstance().add(objRequest);
    }


    /**
     * 拼接json字符串作为参数上传  noloading
     *
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getDataForJsoNoloading(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode) {
        LogUtils.i(details + ":" + url);

        map.put("type", "0");
        JSONObject params = new JSONObject(map);
        LogUtils.i("JSONObject:" + params.toString());
        //第二个参数我们传了user=zhangqi。则请求方法就为post
        BaseJsonRequest objRequest = new BaseJsonRequest(method, UrlBuilder.serverUrl + url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        SdkHttpResult response = JSON.parseObject(obj.toString(), SdkHttpResult.class);
                        LogUtils.i(details + ":" + response.toString());

                        if (response.getCode().equals(SUCCESS_CODE)) {
                            Message message = new Message();
                            Bundle data = new Bundle();
                            data.putString(info, response.getData());
                            message.setData(data);
                            message.what = handlerCode;
                            handler.sendMessage(message);
                        } else if (response.getCode().equals(SERVERS_CODE)) {
                            ToastUtils.showMessageDefault(context, response.getMessage());
                            LogUtils.i("服务器内部错误:" + response.getMessage());
                        } else if (response.getMessage() != null && response.getMessage().equals("账户余额不足")) {
                            //账户余额不足,跳转到充值页面
//                            context.startActivity(new Intent(context, ChargeCoinsActivity.class));
//                            context.startActivity(new Intent(context, PayOrderActivity.class));

                            ToastUtils.showMessageDefault(context,"账户余额不足");
//                            Intent intent = new Intent(context, ExplainWebViewActivity.class);
//                            intent.putExtra("flag", 1000);
//                            context.startActivity(intent);
                        } else {
                        }
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                }, errorListener);
        // 将这个request加入到requestQueue中，就可以执行了
        MyApplication.requestQueueiInstance().add(objRequest);
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
    public void DataJsonAdmin(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode) {
        LogUtils.i(details + ":" + url);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        JSONObject params = new JSONObject(map);
        LogUtils.i("JSONObject:" + params.toString());
        //第二个参数我们传了user=zhangqi。则请求方法就为post
        BaseJsonRequest objRequest = new BaseJsonRequest(method, UrlBuilder.chargeServerUrl + url, params,
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
        // 将这个request加入到requestQueue中，就可以执行了
        MyApplication.requestQueueiInstance().add(objRequest);
    }

    /**
     * 拼接json字符串作为参数上传  noloading
     *
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void DataNoloadingAdmin(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode) {
        LogUtils.i(details + ":" + url);
        JSONObject params = new JSONObject(map);
        LogUtils.i("JSONObject:" + params.toString());
        //第二个参数我们传了user=zhangqi。则请求方法就为post
        BaseJsonRequest objRequest = new BaseJsonRequest(method, UrlBuilder.chargeServerUrl + url, params,
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
        // 将这个request加入到requestQueue中，就可以执行了
        MyApplication.requestQueueiInstance().add(objRequest);
    }


    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            LogUtils.i("返回内容:" + "请求失败");
            LogUtils.i("error:" + error.toString());
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    };

    private void addRequestQueue() {
        if (fastJsonRequest != null) {
            MyApplication.requestQueueiInstance().add(fastJsonRequest);
        }
    }

    //多加一个tag参数，用于指定取消
    public void getData(String details, String url, final Handler handler, final int handlerCode, String tag) {
        LogUtils.e(details + ":" + url);
        if (!mDialog.isShowing()) {
            mDialog.show();
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
                    ToastUtils.showMessageDefault(context, "服务器内部错误");
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        addRequestQueue(tag);
    }

    private void addRequestQueue(String tag) {
        if (fastJsonRequest != null) {
            fastJsonRequest.setTag(tag);
            MyApplication.requestQueueiInstance().add(fastJsonRequest);
        }
    }


    public void getDataDialog(String details, final boolean isDialog, String url, final Handler handler, final int handlerCode) {
        url = urlBuilder.serverUrl + url;
        LogUtils.i(details + ":" + url);
        this.isDialog = isDialog;
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
                    ToastUtils.showMessageDefault(context, "服务器内部错误");
                }

                if (isDialog) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }

            }
        }, errorListener);
        addRequestQueue();
    }


    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getNewDataCharServer(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode) {
        url = urlBuilder.buildChaServer(url, map);
        LogUtils.i(details + ":" + url);
        if (!mDialog.isShowing()) {
            mDialog.show();
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
//                    ToastUtils.showMessageDefault(context, response.getMsg());
                    ToastUtils.showMessageDefault(context, response.getMsg());
                    LogUtils.i("服务器内部错误:" + response.getMsg());
                    LogUtils.i("服务器内部错误:" + response.toString());
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        newaddRequestQueue();
    }

    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getNewDataCharServerNoLoading(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode) {
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
//                    ToastUtils.showMessageDefault(context, response.getMsg());
                    LogUtils.i("服务器内部错误:" + response.getMsg());
                    LogUtils.i("服务器内部错误:" + response.toString());
                }
            }
        }, errorListener);
        newaddRequestQueue();
    }

    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getNewDataCharServerRefresh(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode, final SwipeRefreshLayout refreshLayout) {
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
//                    ToastUtils.showMessageDefault(context, response.getMsg());
                    LogUtils.i("服务器内部错误:" + response.getMsg());
                    LogUtils.i("服务器内部错误:" + response.toString());
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setPullUpRefreshing(false);
                    ToastUtils.showMessageDefault(context, "查询失败");
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
        newaddRequestQueue();
    }


    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getNewDataCharServerCode(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode) {
        url = urlBuilder.buildChaServer(url, map);
        LogUtils.i(details + ":" + url);
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
                    LogUtils.i("服务器内部错误:" + response.getMsg());
                }
            }
        }, errorListener);
        newaddRequestQueue();
    }

    /**
     * @param details     接口名
     * @param method      请求方式
     * @param url         接口地址
     * @param map         参数
     * @param handler
     * @param handlerCode
     */
    public void getNewDataCharServerCodeNoLoading(final String details, int method, String url, Map<String, String> map, final Handler handler, final int handlerCode) {
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
                    LogUtils.i("服务器内部错误:" + response.getMsg());
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, errorListener);
        newaddRequestQueue();
    }


    private void newaddRequestQueue() {
        if (newfastJsonRequest != null) {
            MyApplication.requestQueueiInstance().add(newfastJsonRequest);
        }
    }
}

