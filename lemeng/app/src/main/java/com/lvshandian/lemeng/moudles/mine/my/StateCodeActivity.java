package com.lvshandian.lemeng.moudles.mine.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.bean.StateCodeBean;
import com.lvshandian.lemeng.moudles.mine.my.adapter.StateCodeAadapter;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.widget.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * Created by ssb on 2017/5/9 18:31.
 * company: lvshandian
 */

public class StateCodeActivity extends BaseActivity {
    @Bind(R.id.myRecycler)
    RecyclerView myRecycler;

    private List<StateCodeBean> codes = new ArrayList<>();
    private StateCodeAadapter codeAadapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                //关注请求接收数据
                case RequestCode.GETSTATECODE:
                    List<StateCodeBean> codeBeenList = JsonUtil.json2BeanList(json, StateCodeBean.class);
                    codes.addAll(codeBeenList);
                    codeAadapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_code_state;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.state_code), null);

        getStateCode();

        codeAadapter = new StateCodeAadapter(mContext, codes);
        FullyLinearLayoutManager layoutManager1 = new FullyLinearLayoutManager(mContext);
        myRecycler.setNestedScrollingEnabled(false);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        myRecycler.setLayoutManager(layoutManager1);
        myRecycler.setAdapter(codeAadapter);

        codeAadapter.setOnRecyclerClickListener(new StateCodeAadapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerClick(int position) {
                Intent intent = new Intent();
                String stateCode = codes.get(position).getCountryName() + " +" + codes.get(position).getCountryTop();
                intent.putExtra("stateCode", stateCode);
                setResult(200, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }

    /**
     * 获得国家代码
     */
    private void getStateCode() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        httpDatas.getNewDataCharServer("获得国家代码", true, Request.Method.POST, UrlBuilder.GET_STATE_CODE, map, mHandler, RequestCode.GETSTATECODE, TAG);
    }
}
