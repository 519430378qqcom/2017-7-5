package com.lvshandian.lemeng.moudles.mine.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.ContributionBeanBack;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.my.adapter.ContributionListAdapter;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.PicassoUtil;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 周榜排行榜
 */

public class ContributionActivity extends BaseActivity {

    @Bind(R.id.parent_layout)
    LinearLayout parentLayout;
    @Bind(R.id.my_head)
    ImageView imgHead;
    @Bind(R.id.gender)
    ImageView imgGender;
    @Bind(R.id.level)
    ImageView imglevel;
    @Bind(R.id.name)
    TextView tvName;
    @Bind(R.id.devote)
    TextView tvDevote;
    @Bind(R.id.my_list)
    ListView myList;
    @Bind(R.id.mrl_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.iv_empty)
    ImageView iv_empty;

    private List<ContributionBeanBack> mDatas = new ArrayList<>();
    private ContributionListAdapter listAdapter;
    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 总页数
     */
//    private int totalPages = 1;
    /**
     * 是否是刷新或者加载
     */
    private boolean isRefresh = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.obj);
            finishRefresh();
            switch (msg.what) {
                //关注请求接收数据
                case RequestCode.REQUEST_RANK:
                    LogUtils.LogAll("周榜排行榜返回内容json:" + json);
                    List<ContributionBeanBack> listBean = JsonUtil.json2BeanList(json, ContributionBeanBack.class);
                    handlerContribution(listBean);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contribution;
    }


    @Override
    protected void initListener() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main));
        refreshLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        refreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                requestContribution();

            }
        });
        // 加载监听器
        refreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {

            @Override
            public void onPullUpRefresh() {
                isRefresh = false;
                requestContribution();
            }
        });

    }

    @Override
    protected void initialized() {
        initTitle("", "周榜排行榜", null);
        requestContribution();
        scrollView.smoothScrollTo(0, 0);
    }


    private void initListView() {
        if (listAdapter == null) {
            listAdapter = new ContributionListAdapter(mContext, mDatas, R.layout.item_paihang_list);
            myList.setAdapter(listAdapter);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                    intent.putExtra(getString(R.string.visiti_person), mDatas.get(position).getId());
                    startActivity(intent);
                }
            });
        }
    }


    /**
     * 查询周榜排行榜
     */
    private void requestContribution() {
        page = isRefresh ? 1 : ++page;
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("rows", "10");
        map.put("page", String.valueOf(page));
        httpDatas.getNewDataCharServerRefresh("查询周榜排行榜", Request.Method.GET, UrlBuilder.weekContribution, map, mHandler, RequestCode.REQUEST_RANK, refreshLayout);
    }


    /**
     * 数据处理
     *
     * @param result
     */
    private void handlerContribution(List<ContributionBeanBack> result) {
        if (result != null && result.size() == 1) {
            if (isRefresh) {
                mDatas.clear();
                mDatas.addAll(result);
                initTitleInfo(result.get(0));
            } else {
                mDatas.addAll(result);
                listAdapter.notifyDataSetChanged();
            }
        } else if (result != null && result.size() > 1) {
            if (isRefresh) {
                mDatas.clear();
                initTitleInfo(result.get(0));
                result.remove(0);
            }
            mDatas.addAll(result);
            initListView();
            listAdapter.notifyDataSetChanged();
        } else {
            if (!isRefresh)
                page--;
        }

        if (mDatas.size() > 0) {
            parentLayout.setVisibility(View.VISIBLE);
            iv_empty.setVisibility(View.GONE);
        } else {
            parentLayout.setVisibility(View.GONE);
            iv_empty.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 显示第一名界面
     */
    private void initTitleInfo(final ContributionBeanBack oneData) {
        PicassoUtil.newInstance().onRoundnessImage(mContext, oneData.getPicUrl(), imgHead);
        String gender = oneData.getGender();
        imgGender.setImageResource(android.text.TextUtils.equals(gender, "1") ? R.mipmap.male : R.mipmap.female);
        int level = oneData.getLevel();
        imglevel.setImageResource(GrademipmapUtils.LevelImg[level - 1]);
        tvName.setText(oneData.getNickName());
        tvDevote.setText(oneData.getSumAmount() + "");
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                intent.putExtra(getString(R.string.visiti_person), oneData.getId());
                startActivity(intent);
            }
        });
    }

    private void finishRefresh() {
        refreshLayout.setRefreshing(false);
        refreshLayout.setPullUpRefreshing(false);
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
