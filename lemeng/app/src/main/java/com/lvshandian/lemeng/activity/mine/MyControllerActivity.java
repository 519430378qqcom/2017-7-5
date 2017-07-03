package com.lvshandian.lemeng.activity.mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.adapter.mine.ControllerBaseAdapter;
import com.lvshandian.lemeng.entity.ControllerBean;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.net.RequestCode;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.utils.JsonUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的场控
 */
public class MyControllerActivity extends BaseActivity {
    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.refresh)
    MaterialRefreshLayout refresh;
    @Bind(R.id.tv_titlebar_left)
    TextView tv_titlebar_left;
    @Bind(R.id.tv_titlebar_right)
    ImageView tv_titlebar_right;

    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_controller;
    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.obj);
            switch (msg.what) {
                case RequestCode.MYCONTROLLER://我的場控
                    List<ControllerBean> list = JsonUtil.json2BeanList(json.toString(), ControllerBean.class);
                    if (null == list || list.size() == 0) {
                        if (page == 1) {
                            controllerList.clear();
                            refresh.finishRefreshing();
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            refresh.finishRefreshLoadMore();
                        }
                        return;
                    }
                    if (page == 1) {
                        controllerList.clear();
                        controllerList.addAll(list);
                        commentAdapter = new ControllerBaseAdapter(controllerList, mContext, appUser.getId(), null);
                        lv.setAdapter(commentAdapter);
                        refresh.finishRefreshing();
                    } else {
                        controllerList.addAll(list);
                        commentAdapter.notifyDataSetChanged();
                        refresh.finishRefreshLoadMore();
                    }


                    break;
            }

        }
    };

    List<ControllerBean> controllerList = new ArrayList<ControllerBean>();
    ControllerBaseAdapter commentAdapter;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initialized() {
        initView();
        commentAdapter = new ControllerBaseAdapter(controllerList, mContext, appUser.getId(), null);
        lv.setAdapter(commentAdapter);
        request();
    }

    private void initView() {
        int[] colors = new int[1];
        colors[0] = getResources().getColor(R.color.main);
        refresh.setProgressColors(colors);
        refresh.setLoadMore(false);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                /**
                 * 刷新
                 */
                page = 1;
                request();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                // TODO Auto-generated method stub
                super.onRefreshLoadMore(materialRefreshLayout);
                page++;
                request();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ControllerBean userBean = controllerList.get(position);
                String id1 = userBean.getId() + "";
                Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                intent.putExtra(getString(R.string.visit_person), id1);
                startActivity(intent);
            }
        });
    }

    private void request() {
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        map.put("page", page + "");
        map.put("rows", "10");
        map.put("userId", appUser.getId());
        httpDatas.getNewDataCharServer("我的场控列表", true, Request.Method.GET, UrlBuilder.MY_CONTROLLER, map, mHandler, RequestCode.MYCONTROLLER, TAG);

    }

    @Override
    protected void initListener() {
        tv_titlebar_left.setOnClickListener(this);
        tv_titlebar_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 退出
             */
            case R.id.tv_titlebar_left:
                finish();
                break;
            /**
             * 搜索
             */
            case R.id.tv_titlebar_right:
                startActivity(new Intent(mContext, SearchControllerActivity.class));
                break;

        }
    }

    //接受方：
    @Subscribe //第2步:注册一个在后台线程执行的方法,用于接收事件
    public void onEventMainThread(String event) {
        if (event.equals("reflash")) {
            request();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
