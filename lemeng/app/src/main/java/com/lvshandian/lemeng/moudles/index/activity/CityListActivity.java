package com.lvshandian.lemeng.moudles.index.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.ProvinceCity;
import com.lvshandian.lemeng.bean.lemeng.Args;
import com.lvshandian.lemeng.interf.ItemClickListener;
import com.lvshandian.lemeng.moudles.index.adapter.CityRecycleAdapter;
import com.lvshandian.lemeng.moudles.mine.my.adapter.RecycleViewDivider;
import com.lvshandian.lemeng.utils.Constant;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 城市选择
 * Created by gjj on 2016/12/20.
 */

public class CityListActivity extends BaseActivity {
    @Bind(R.id.rlv_recycler)
    RecyclerView rlvRecycler;
    private List<String> mDatas = new ArrayList<>();
    private CityRecycleAdapter mAdapter;
    private String name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_list;
    }

    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onItemClick(int position) {
            String city = mDatas.get(position);

            Args args = new Args();
            args.setGender(10);
            args.setCity(city);
            if (!TextUtils.isEmpty(city)) {
                EventBus.getDefault().post(args);
                finish();
            }
            SharedPreferenceUtils.put(mContext, Constant.ADDRESS, city);
        }
    };

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void initialized() {
        initTitle("", null, null);
        initIntent();
        initRecycler();
    }

    private void initIntent() {
        Intent intent = getIntent();
        ProvinceCity province = (ProvinceCity) intent.getSerializableExtra(getString(R.string.province_city));
        if (province != null) {
            List<String> cities = province.getCities();
            name = province.getName();
            if (cities != null) {
                mDatas.addAll(cities);
            }
        }
    }

    private void initRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        rlvRecycler.setLayoutManager(gridLayoutManager);
        RecycleViewDivider divider = new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.divider);
        rlvRecycler.addItemDecoration(divider);

        mAdapter = new CityRecycleAdapter(this, mDatas);
        rlvRecycler.setAdapter(mAdapter);
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
