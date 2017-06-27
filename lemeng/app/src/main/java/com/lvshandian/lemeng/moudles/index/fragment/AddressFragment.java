package com.lvshandian.lemeng.moudles.index.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseFragment;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.index.adapter.HotListAadapter;
import com.lvshandian.lemeng.utils.Constant;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.lvshandian.lemeng.widget.view.EmptyRecyclerView;
import com.lvshandian.lemeng.widget.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 地址Fragment
 */
public class AddressFragment extends BaseFragment implements SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener {
    @Bind(R.id.lv_live_room)
    EmptyRecyclerView addressRecyclerView;
    @Bind(R.id.iv_empty)
    LinearLayout iv_empty;
    @Bind(R.id.mrl_layout)
    SwipeRefreshLayout mrlLayout;

    private boolean isfirst = true;
    List<LiveListBean> liveListBeen = new ArrayList<>();
    private HotListAadapter addressListAadapter;
    private int page = 1;

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
                case RequestCode.HOT_LIVE:
                    if (mrlLayout == null)
                        return;
                    finishRefresh();
                    List<LiveListBean> liveListBeen1 = JsonUtil.json2BeanList(json.toString(), LiveListBean.class);
                    if (null == liveListBeen1 || liveListBeen1.size() == 0) {
                        if (page == 1) {
                            liveListBeen.clear();
                            addressListAadapter.notifyDataSetChanged();
                        }
                        return;
                    }
                    if (page == 1) {
                        liveListBeen.clear();
                    }
                    liveListBeen.addAll(liveListBeen1);
                    addressListAadapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new;
    }

    @Override
    protected void initListener() {
        initList();
    }

    @Override
    protected void initialized() {
        isfirst = false;
        addressRecyclerView.setEmptyView(iv_empty);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isfirst) {
            if (getUserVisibleHint()) {
//                getLiveList(page);
            }
        }
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

        addressListAadapter = new HotListAadapter(mContext, liveListBeen);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
        addressRecyclerView.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        addressRecyclerView.setLayoutManager(layoutManager);
        addressRecyclerView.setAdapter(addressListAadapter);

        addressListAadapter.setOnRecyclerClickListener(new HotListAadapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerClick(int position) {
//              进入直播间
                ifEnter(liveListBeen.get(position).getRooms().getRoomId() + "", liveListBeen.get(position).getRooms().getBroadcastUrl());
            }
        });

    }

    public void getLiveList(int page) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        String city = (String) SharedPreferenceUtils.get(getActivity(), Constant.ADDRESS, "北京");
        map.put("city", city);
        map.put("gender", (Integer) SharedPreferenceUtils.get(getContext(), Constant.GENDER, 2) + "");
        map.put("page", page + "");
        map.put("type", "3");
        httpDatas.getNewDataCharServerRefresh("获取地区直播接口列表", Request.Method.GET, UrlBuilder.APP_ROOMS_LIST, map, mHandler, RequestCode.HOT_LIVE, mrlLayout, TAG);
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
