package com.lvshandian.lemeng.moudles.index.live.redpackage;

import com.alibaba.fastjson.JSON;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.utils.LogUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/20
 * version: 1.0
 * desc   : 红包功能的presenter
 */
public class RedPackagePresenter implements RedPackageContract.Presenter{
    private RedPackageContract.IView iView;

    public RedPackagePresenter(RedPackageContract.IView iView) {
        this.iView = iView;
    }

    public void detach() {
        iView = null;
    }

    @Override
    public void grabPackage(String userId,String redenvelope) {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.GRAB_RED_PACKET + "?userId="+userId +"&redenvelope="+redenvelope;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e(response);
                GrabPackageBean grabPackageBean = null;
                try {
                    grabPackageBean = JSON.parseObject(response, GrabPackageBean.class);
                } catch (Exception e) {
                    return;
                }
                if (iView != null) {
                    iView.grabPackage(grabPackageBean);
                }
            }
        });
    }

    @Override
    public void packageRank(String redenvelope) {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.OTHERS_RED_PACKETS + "?redenvelope="+redenvelope;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e(response);
                PackageRank packageRank = null;
                try {
                    packageRank = JSON.parseObject(response, PackageRank.class);
                } catch (Exception e) {
                    return;
                }
                if (iView != null) {
                    iView.packageRank(packageRank);
                }
            }
        });
    }
}
