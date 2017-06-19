package com.lvshandian.lemeng.moudles.index.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseFragment;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.index.adapter.HotListAadapter;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.lvshandian.lemeng.widget.view.EmptyRecyclerView;
import com.lvshandian.lemeng.widget.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 关注界面
 */
public class AttentionFragment extends BaseFragment implements View.OnClickListener, SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener {
    @Bind(R.id.lv_live_room)
    EmptyRecyclerView attentionRecyclerView;
    @Bind(R.id.iv_empty)
    LinearLayout iv_empty;
    @Bind(R.id.mrl_layout)
    SwipeRefreshLayout mrlLayout;

    private HotListAadapter attentionListAadapter;
    private int page = 1;

    private List<LiveListBean> liveListBeen = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.obj);
            if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(json)) {
                return;
            }
            switch (msg.what) {
                //最新
                case RequestCode.ATTENTION_LIVE:
                    if (mrlLayout == null)
                        return;
                    finishRefresh();
                    List<LiveListBean> liveListBeen1 = JsonUtil.json2BeanList(json.toString(), LiveListBean.class);
                    if (null == liveListBeen1 || liveListBeen1.size() == 0) {
                        if (page == 1) {
                            liveListBeen.clear();
                            attentionListAadapter.notifyDataSetChanged();
                        }
                        return;
                    }
                    if (page == 1) {
                        liveListBeen.clear();
                    }
                    liveListBeen.addAll(liveListBeen1);
                    attentionListAadapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attention;
    }

    @Override
    protected void initListener() {
        initList();
    }

    @Override
    protected void initialized() {
        attentionRecyclerView.setEmptyView(iv_empty);
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        getLiveList(page);
    }

    public void initList() {
        //设置刷新逻辑
        mrlLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        mrlLayout.setOnRefreshListener(this);
        mrlLayout.setOnPullUpRefreshListener(this);
        mrlLayout.setColorSchemeColors(getResources().getColor(R.color.main));

        attentionListAadapter = new HotListAadapter(mContext, liveListBeen);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
        attentionRecyclerView.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        attentionRecyclerView.setLayoutManager(layoutManager);
        attentionRecyclerView.setAdapter(attentionListAadapter);

        attentionListAadapter.setOnRecyclerClickListener(new HotListAadapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerClick(int position) {
//              进入直播间
                ifEnter(liveListBeen.get(position).getRooms().getRoomId() + "",liveListBeen.get(position).getRooms().getBroadcastUrl());
            }
        });

    }


    public void getLiveList(int page) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("page", page + "");
        map.put("userId", appUser.getId());
        map.put("type", "4");
        httpDatas.getNewDataCharServerRefresh("获取直播关注界面接口列表", Request.Method.GET, UrlBuilder.APP_ROOMS_LIST, map, mHandler, RequestCode.ATTENTION_LIVE, mrlLayout);
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {
        page = 1;
        getLiveList(page);
    }

    @Override
    public void onPullUpRefresh() {
        page++;
        getLiveList(page);
    }

    private void finishRefresh() {
        mrlLayout.setRefreshing(false);
        mrlLayout.setPullUpRefreshing(false);
    }
}
