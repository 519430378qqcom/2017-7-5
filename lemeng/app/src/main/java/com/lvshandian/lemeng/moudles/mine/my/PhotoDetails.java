package com.lvshandian.lemeng.moudles.mine.my;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.bean.PhotoBean;
import com.lvshandian.lemeng.view.DialogView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * Created by zhang on 2016/11/21.
 * 创建图片详情界面
 */

public class PhotoDetails extends BaseActivity {

    @Bind(R.id.image_back)
    ImageView imageBack;
    @Bind(R.id.image_delete)
    ImageView imageDelete;
    @Bind(R.id.videoView)
    ImageView ImageHead;
    @Bind(R.id.rl_part)
    AutoRelativeLayout rlPart;


    private PhotoBean photoBean;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);
            switch (msg.what) {
                case RequestCode.MY_PHOTO_DELETE_CODE://图片请求列表
                    PhotoBean photoBean = new PhotoBean();
                    photoBean.setId("yes");
                    EventBus.getDefault().post(photoBean);
                    //图片删除成功
                    PhotoDetails.this.finish();
                    break;
            }

        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.my_activity_photodetails;
    }

    @Override
    protected void initialized() {
        photoBean = (PhotoBean) getIntent().getSerializableExtra("photo");
        if (getIntent().getStringExtra("isShow").equals("notShow")) {
            imageDelete.setVisibility(View.GONE);
        } else {
            imageDelete.setVisibility(View.VISIBLE);
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        AutoRelativeLayout.LayoutParams lp = new AutoRelativeLayout.LayoutParams(width, width);
        lp.addRule(AutoRelativeLayout.CENTER_VERTICAL);
        ImageHead.setLayoutParams(lp);

        ImageLoader.getInstance().displayImage(photoBean.getUrl(), ImageHead);
    }

    @Override
    protected void initListener() {
        imageDelete.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        ImageHead.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoView://返回
                finish();
                break;
            case R.id.image_back://返回
                finish();
                break;
            case R.id.image_delete://删除
                new DialogView(getContext(), rlPart, "", "相机", "删除", "取消",
                        new DialogView.MyCameraCallback() {
                            @Override
                            public void refreshCallback(int tag) {
                                switch (tag) {
                                    //调用相机
                                    case 1:
                                        break;
                                    //调用相册
                                    case 2:
                                        ConcurrentHashMap map = new ConcurrentHashMap<>();
                                        httpDatas.getDataForJson("删除图片", Request.Method.DELETE, UrlBuilder.photoDelete(photoBean.getId()), map, mHandler, RequestCode.MY_PHOTO_DELETE_CODE);
                                        break;
                                    //取消
                                    case 3:
                                        break;
                                }
                            }
                        }
                );

                break;
        }

    }

}
