package com.lvshandian.lemeng.moudles.index.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseFragment;
import com.lvshandian.lemeng.bean.BannerBean;
import com.lvshandian.lemeng.bean.ContributionBeanBack;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.index.adapter.HotListAadapter;
import com.lvshandian.lemeng.moudles.index.adapter.RankingListAadapter;
import com.lvshandian.lemeng.moudles.mine.my.ContributionActivity;
import com.lvshandian.lemeng.utils.GlideImageLoader;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.widget.FullyLinearLayoutManager;
import com.lvshandian.lemeng.widget.MyRecyclerView;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 热门Fragment
 */
public class HotFragment extends BaseFragment implements SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener {
    @Bind(R.id.lv_live_room)
    MyRecyclerView hotRecyclerView;
    @Bind(R.id.rankingList)
    MyRecyclerView rankingList;
    @Bind(R.id.iv_empty)
    ImageView iv_empty;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.mrl_layout)
    SwipeRefreshLayout mrlLayout;

    private boolean isfirst = true;
    private HotListAadapter hotListAadapter;
    private RankingListAadapter rankingAdapter;
    private int page = 1;
    /**
     * 热门数据集合
     */
    private List<LiveListBean> liveListBeen = new ArrayList<>();
    /**
     * 排行榜数据集合
     */
    private List<ContributionBeanBack> rankingListBean = new ArrayList<>();
    /**
     * 图片地址集合
     */
    private List<String> images = new ArrayList<>();
    /**
     * 图片标题集合
     */
    // private List<String> titles = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.obj);
            switch (msg.what) {
                //热门
                case RequestCode.HOT_LIVE:
                    if (mrlLayout == null)
                        return;
                    finishRefresh();
                    List<LiveListBean> liveListBeen1 = JsonUtil.json2BeanList(json.toString(), LiveListBean.class);
                    if (null == liveListBeen1 || liveListBeen1.size() == 0) {
                        if (page == 1) {
                            liveListBeen.clear();
                            hotListAadapter.notifyDataSetChanged();
                        }
                        return;
                    }
                    if (page == 1) {
                        liveListBeen.clear();
                    }
                    liveListBeen.addAll(liveListBeen1);
                    hotListAadapter.notifyDataSetChanged();
                    break;
                case RequestCode.HOME_BANNER:
                    List<BannerBean> beanList = JsonUtil.json2BeanList(json.toString(), BannerBean.class);
                    images.clear();
                    for (int i = 0, j = beanList.size(); i < j; i++) {
                        images.add(beanList.get(i).getPicUrl());
                    }
                    setBanner();
                    break;
                case RequestCode.REQUEST_RANK:
                    rankingListBean.clear();
                    List<ContributionBeanBack> listBean = JsonUtil.json2BeanList(json, ContributionBeanBack.class);
                    rankingListBean.addAll(listBean);
                    rankingListBean.add(new ContributionBeanBack());
                    rankingAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hall;
    }

    @Override
    protected void initListener() {
        initList();
    }

    @Override
    protected void initialized() {
        isfirst = false;
        setBanner();
        hotRecyclerView.setEmptyView(iv_empty);
    }

    private void setBanner() {
        if (banner == null)
            return;
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
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
        getBannerList();
        requestContribution();
    }

    public void initList() {
        //设置刷新逻辑
        mrlLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        mrlLayout.setOnRefreshListener(this);
        mrlLayout.setOnPullUpRefreshListener(this);
        mrlLayout.setColorSchemeColors(getResources().getColor(R.color.main));

        /**
         * 热门数据
         */
        hotListAadapter = new HotListAadapter(mContext, liveListBeen);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
        hotRecyclerView.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hotRecyclerView.setLayoutManager(layoutManager);
        hotRecyclerView.setAdapter(hotListAadapter);

        hotListAadapter.setOnRecyclerClickListener(new HotListAadapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerClick(int position) {
//              进入直播间
                ifEnter(liveListBeen.get(position).getRooms().getRoomId() + "",liveListBeen.get(position).getRooms().getBroadcastUrl());
            }
        });

        /**
         * 排行榜数据
         */
        rankingAdapter = new RankingListAadapter(mContext, rankingListBean);
//        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        FullyLinearLayoutManager layoutManager1 = new FullyLinearLayoutManager(getActivity());
        hotRecyclerView.setNestedScrollingEnabled(false);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rankingList.setLayoutManager(layoutManager1);
        rankingList.setAdapter(rankingAdapter);
        rankingAdapter.setOnRecyclerClickListener(new RankingListAadapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerClick(int position) {
                Intent intent = new Intent(getActivity(), ContributionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getLiveList(int page) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("page", page + "");
        map.put("type", "1");
        httpDatas.getNewDataCharServerRefresh("获取直播热门接口列表", Request.Method.GET, UrlBuilder.appRooms, map, mHandler, RequestCode.HOT_LIVE, mrlLayout);

    }

    /**
     * 查询周榜排行榜
     */
    private void requestContribution() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("rows", "50");
        map.put("page", "1");
        httpDatas.getNewDataCharServerNoLoading("查询周榜排行榜", Request.Method.GET, UrlBuilder.WEEK_CONTRIBUTION, map, mHandler, RequestCode.REQUEST_RANK);
    }

    /**
     * 查询轮播图
     */
    private void getBannerList() {
        httpDatas.getNewDataCharServerNoLoading("获取轮播图接口列表", Request.Method.GET, UrlBuilder.getBanner, null, mHandler, RequestCode.HOME_BANNER);
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
