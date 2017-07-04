package com.lvshandian.lemeng.activity.mine;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.activity.MyInformationActivity;
import com.lvshandian.lemeng.adapter.mine.FunseListAdapter;
import com.lvshandian.lemeng.interfaces.CustomStringCallBack;
import com.lvshandian.lemeng.entity.mine.Funse;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.netease.nim.uikit.team.activity.FunseBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 关注列表
 */
public class FollowListActivity extends BaseActivity implements SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener {
    @Bind(R.id.lv_list)
    ListView lvList;
    @Bind(R.id.iv_empty_1)
    LinearLayout iv_empty;
    @Bind(R.id.mrl_layout)
    SwipeRefreshLayout mrlLayout;

    /**
     * 当前id
     */
    private String mUserId;
    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 总页数
     */
    private int totalPages;
    /**
     * 粉丝列表数据
     */
    private List<FunseBean> mDatas = new ArrayList<>();
    /**
     * 适配器
     */
    private FunseListAdapter mAdapter;
    /**
     * 判断是刷新还是加载
     */
    private boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_funse_list;
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.attention), null);
        mUserId = getIntent().getStringExtra("userId");
        mAdapter = new FunseListAdapter(mContext, mDatas, R.layout.item_attention_fans, false);
        lvList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRefresh = true;
        requestFunse();
    }

    @Override
    protected void initListener() {
        //设置刷新逻辑
        mrlLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        mrlLayout.setOnRefreshListener(this);
        mrlLayout.setOnPullUpRefreshListener(this);
        mrlLayout.setColorSchemeColors(getResources().getColor(R.color.main));

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FunseBean funseBean = mDatas.get(position);
                String userId = funseBean.getUserId();
                if (userId.equals(appUser.getId())) {
                    Intent intent = new Intent(mContext, MyInformationActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                    intent.putExtra(getString(R.string.visit_person), userId);
                    startActivity(intent);
                }

            }
        });
    }


    /**
     * 请求关注列表
     */
    private void requestFunse() {
        page = isRefresh ? 1 : ++page;
        String url = UrlBuilder.SERVER_URL + UrlBuilder.FUNSE_AND_FOLLOW;
        url += mUserId;
        url += "/follows?pageNum=" + page;
        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack(this, HttpDatas.KEY_CODE) {
            @Override
            public void onFaild() {
                finishRefresh();
            }

            @Override
            public void onSucess(String data) {
                handlerJson(data);
            }
        });
    }

    /**
     * 获取数据
     *
     * @param data
     */
    private void handlerJson(String data) {
        Funse funse = JsonUtil.json2Bean(data, Funse.class);
        if (funse != null) {
            totalPages = funse.getTotalPages();
            List<FunseBean> result = funse.getResult();
            appUser.setFollowNum(String.valueOf(result.size()));
//            CacheUtils.saveObject(FollowListActivity.this, appUser, CacheUtils.USERINFO);
            SharedPreferenceUtils.saveUserInfo(mContext, appUser);
            if (isRefresh) {
                mDatas.clear();
            } else {
                if (result == null && result.size() == 0) {
                    page--;
                }
            }
            mDatas.addAll(result);
            mAdapter.notifyDataSetChanged();

            if (mDatas.size() == 0) {
                iv_empty.setVisibility(View.VISIBLE);
            } else {
                iv_empty.setVisibility(View.GONE);
            }
        }
        finishRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;

        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        requestFunse();
    }

    @Override
    public void onPullUpRefresh() {
        isRefresh = false;
        if (page < totalPages) {
            requestFunse();
        } else {
            finishRefresh();
        }
    }

    private void finishRefresh() {
        mrlLayout.setRefreshing(false);
        mrlLayout.setPullUpRefreshing(false);
    }
}
