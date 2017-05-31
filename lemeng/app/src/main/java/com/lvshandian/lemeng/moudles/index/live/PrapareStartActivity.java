package com.lvshandian.lemeng.moudles.index.live;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.CreatReadyBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.NetWorkUtil;
import com.lvshandian.lemeng.utils.PermisionUtils;
import com.lvshandian.lemeng.utils.UMUtils;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

public class PrapareStartActivity extends BaseActivity {

    @Bind(R.id.iv_bg)
    ImageView ivBg;
    @Bind(R.id.iv_colse)
    ImageView ivColse;
    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.et_title)
    TextView etTitle;
    @Bind(R.id.tv_start_live)
    TextView tvStartLive;
    @Bind(R.id.wechat)
    ImageView wechat;
    @Bind(R.id.qq)
    ImageView qq;
    @Bind(R.id.qq_zone)
    ImageView qq_zone;
    @Bind(R.id.wechat_circle)
    ImageView wechat_circle;

    private String address = "火星";


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);
            switch (msg.what) {
                case RequestCode.START_LIVE:
                    CreatReadyBean creatReadyBean = JsonUtil.json2Bean(json, CreatReadyBean.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("START", creatReadyBean);
                    LogUtils.e("START: " + creatReadyBean.toString());
                    Intent intent = new Intent(mContext, StartLiveActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_prapare_start;
    }

    @Override
    protected void initListener() {
        wechat.setOnClickListener(this);
        qq.setOnClickListener(this);
        qq_zone.setOnClickListener(this);
        wechat_circle.setOnClickListener(this);
        ivColse.setOnClickListener(this);
        tvStartLive.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        if (!TextUtils.isEmpty(appUser.getPicUrl())) {
            Picasso.with(mContext).load(appUser.getPicUrl()).placeholder(R.mipmap.head_default)
                    .error(R.mipmap.head_default).into(ivHead);
            Picasso.with(mContext).load(appUser.getPicUrl()).error(R.mipmap.head_default).into(ivBg);
        }
        PermisionUtils.newInstance().checkLocationPermission(this, new PermisionUtils.OnPermissionGrantedLintener() {
            @Override
            public void permissionGranted() {
                if (TextUtils.isEmpty(MyApplication.city)) {
                    address = "火星";
                } else {
                    address = MyApplication.city;
                }
                tvAddress.setText(address);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_colse:
                finish();
                break;
            case R.id.tv_start_live:
                if (NetWorkUtil.getConnectedType(mContext) == 0) {
                    initDialog();
                    baseDialogTitle.setText("当前为移动网络,是否开启直播");
                    baseDialogLeft.setText("取消直播");
                    baseDialogRight.setText("继续直播");
                    baseDialogLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (baseDialog != null && baseDialog.isShowing()) {
                                baseDialog.dismiss();
                            }
                        }
                    });
                    baseDialogRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (baseDialog != null && baseDialog.isShowing()) {
                                baseDialog.dismiss();
                            }
                            startLive();
                        }
                    });
                } else {
                    startLive();
                }
                break;
            case R.id.wechat:
                UMUtils.umShareSingle(this, appUser.getNickName(), appUser.getPicUrl(),
                        "http://app.lemenglive.com/video/share.html?userId=" + appUser.getId(), SHARE_MEDIA.WEIXIN);
                break;
            case R.id.wechat_circle:
                UMUtils.umShareSingle(this, appUser.getNickName(), appUser.getPicUrl(),
                        "http://app.lemenglive.com/video/share.html?userId=" + appUser.getId(), SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.qq:
                UMUtils.umShareSingle(this, appUser.getNickName(), appUser.getPicUrl(),
                        "http://app.lemenglive.com/video/share.html?userId=" + appUser.getId(), SHARE_MEDIA.QQ);
                break;
            case R.id.qq_zone:
                UMUtils.umShareSingle(this, appUser.getNickName(), appUser.getPicUrl(),
                        "http://app.lemenglive.com/video/share.html?userId=" + appUser.getId(), SHARE_MEDIA.QZONE);
                break;

        }
    }

    private void startLive() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("name", appUser.getNickName());
        map.put("city", address);
        map.put("userId", appUser.getId());
        map.put("privateChat", "1");
        map.put("payForChat", appUser.getPayForVideoChat());
        map.put("livePicUrl", appUser.getPicUrl());
        httpDatas.getDataForJson("开启直播", Request.Method.POST, UrlBuilder.START, map, mHandler, RequestCode.START_LIVE);
    }

}
