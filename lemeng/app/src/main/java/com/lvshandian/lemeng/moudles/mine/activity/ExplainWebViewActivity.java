package com.lvshandian.lemeng.moudles.mine.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.view.LoadingDialog;
import com.netease.nim.uikit.common.util.log.LogUtil;

import butterknife.Bind;

/**
 * webView公共界面
 * Created by shang on 2017/3/28.
 */
public class ExplainWebViewActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_titlebar_left)
    TextView tv_titlebar_left;
    @Bind(R.id.tv_titlebar_title)
    TextView tv_titlebar_title;
    @Bind(R.id.tv_titlebar_right)
    TextView tv_titlebar_right;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.error)
    TextView error;

    private int flag;
    private String url = "http://baidu.com";

    /**
     * 请求Loading
     */
    private LoadingDialog mLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_explain_web_view;
    }

    @Override
    protected void initListener() {
        tv_titlebar_left.setOnClickListener(this);
        error.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        mLoading = new LoadingDialog(mContext);
        mLoading.show();
        flag = getIntent().getExtras().getInt("flag");
        if (flag == 1000) {
            tv_titlebar_title.setText("乐票充值");
            url = String.format(UrlBuilder.YINLIAN_PAY_WEB, appUser.getId());
        }else if(flag == 2000){
            tv_titlebar_title.setText("用户协议");
            url = "http://60.205.114.36:8080/protocol/private.html";
        }else if(flag == 3000){
            tv_titlebar_title.setText("关于我们");
            url = "http://60.205.114.36:8080/protocol/about.html";
        }

        initView();
    }

    private void initView() {
        WebSettings webSettings = webView.getSettings();
        webSetting(webSettings);
        LogUtil.e("支付地址", url);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //加载完成
                error.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                error.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("shouldOverrideUrlLoading", "url = " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void webSetting(WebSettings webSettings) {
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(false);
//        webSettings.setBlockNetworkImage(false);
//        webSettings.setLoadsImagesAutomatically(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setAllowContentAccess(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setDefaultTextEncodingName("utf-8");

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true);// 是否可以缩放，默认true
//        webSettings.setBuiltInZoomControls(true);// 是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
//        webSettings.setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true);// 是否使用缓存
        webSettings.setDomStorageEnabled(true);// DOM Storage
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_titlebar_left:
                finish();
                break;
            case R.id.error:
                webView.loadUrl(url);
                break;
        }
    }
}
