package com.lvshandian.lemeng.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.adapter.mine.AllVideoAdapter;
import com.lvshandian.lemeng.entity.mine.VideoBean;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.net.RequestCode;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * Created by lsd on 2017/4/14 0014.
 */

public class AllVideoActivity extends BaseActivity {

    @Bind(R.id.mygrid)
    GridView mygrid;
    private AllVideoAdapter allVideoAdapter;
    public List<VideoBean> listvideo = new ArrayList<VideoBean>();

    private String userId;
    private String isShow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                case RequestCode.MY_VIDEO_LOAD://小视频请求列表
                    final List<VideoBean> listAdds = JsonUtil.json2BeanList(json.toString(), VideoBean.class);
                    listvideo.clear();
                    listvideo.addAll(listAdds);
                    allVideoAdapter.notifyDataSetChanged();

                    mygrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), Videotails.class);
                            intent.putExtra("video", listvideo.get(position));
                            intent.putExtra("isShow", isShow);
                            startActivity(intent);
                        }
                    });

                    break;

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_video;
    }

    @Override
    protected void initListener() {
        allVideoAdapter = new AllVideoAdapter(mContext, listvideo);
        mygrid.setAdapter(allVideoAdapter);
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.small_video), null);
        userId = getIntent().getStringExtra("userId");
        isShow = getIntent().getStringExtra("isShow");
        requestVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * 请求小视频列表
     */
    private void requestVideo() {
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        httpDatas.getDataForJson("视频图片请求列表", false, Request.Method.GET, UrlBuilder.myVideo(userId), map, mHandler, RequestCode.MY_VIDEO_LOAD, TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }

}
