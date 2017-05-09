package com.lvshandian.lemeng.moudles.index.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.MainActivity;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseFragment;
import com.lvshandian.lemeng.bean.LiveBean;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.index.adapter.AttentionListAadapter;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.TextUtils;
import com.lvshandian.lemeng.widget.FullyLinearLayoutManager;
import com.lvshandian.lemeng.widget.MyRecyclerView;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.tandong.sa.json.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 关注界面
 */
public class AttentionFragment extends BaseFragment implements View.OnClickListener, SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener {
    @Bind(R.id.lv_live_room)
    MyRecyclerView attentionRecyclerView;
    @Bind(R.id.rl_empty)
    RelativeLayout rl_empty;
    @Bind(R.id.to_loke)
    TextView to_loke;
    @Bind(R.id.iv_empty)
    ImageView iv_empty;
    @Bind(R.id.mrl_layout)
    SwipeRefreshLayout mrlLayout;

    private AttentionListAadapter attentionListAadapter;
    private int page = 1;

    private List<LiveBean> mUserList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);
            LogUtils.e("关注数据" + json.toString());

            switch (msg.what) {
                //关注请求接收数据
                case RequestCode.ATTENTION_LIVE:
                    if (mrlLayout == null)
                        return;
                    finishRefresh();
                    String res = json.toString();
                    try {
                        JSONObject resJson = new JSONObject(res);
                        String res2 = resJson.getString("result");
                        LogUtils.i(res2);
                        if (TextUtils.isEmpty(res2)) {
                            return;
                        } else {
                            JSONArray resJa = new JSONArray(res2);
                            LogUtils.i("集合长度" + resJa.length() + "");
                            if (resJa.length() > 0) {
                                if (page == 1) {
                                    mUserList.clear();
                                    for (int i = 0; i < resJa.length(); i++) {
                                        LiveBean user = new Gson().fromJson(resJa.getJSONObject(i).toString(), LiveBean.class);
                                        mUserList.add(user);
                                    }
                                    attentionListAadapter.notifyDataSetChanged();
                                } else {
                                    for (int i = 0; i < resJa.length(); i++) {
                                        LiveBean user = new Gson().fromJson(resJa.getJSONObject(i).toString(), LiveBean.class);
                                        mUserList.add(user);
                                    }
                                    attentionListAadapter.notifyDataSetChanged();
                                }
                            } else {
                                if (page == 1) {
                                    mUserList.clear();
                                    attentionListAadapter.notifyDataSetChanged();
                                }
                            }
                            if (mUserList.size() == 0) {
                                rl_empty.setVisibility(View.VISIBLE);
                            } else {
                                rl_empty.setVisibility(View.GONE);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        to_loke.setOnClickListener(this);
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

        attentionListAadapter = new AttentionListAadapter(mContext, mUserList);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
        attentionRecyclerView.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        attentionRecyclerView.setLayoutManager(layoutManager);
        attentionRecyclerView.setAdapter(attentionListAadapter);

        attentionListAadapter.setOnRecyclerClickListener(new AttentionListAadapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerClick(int position) {
                List<LiveListBean> liveListBeen = new ArrayList<>();
                for (int i = 0, j = mUserList.size(); i < j; i++) {
                    liveListBeen.add(transformLiveListBeen(mUserList.get(i)));
                }
//              进入直播间
                ifEnter(liveListBeen, position);
            }
        });

    }


    public void getLiveList(int page) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("pageNum", page + "");
        httpDatas.getDataNoLoading("获取关注人直播的接口", Request.Method.GET, UrlBuilder.myattention(appUser.getId()), map, mHandler, RequestCode.ATTENTION_LIVE, mrlLayout);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_loke:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.intentIndexPager();
                break;
        }
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
