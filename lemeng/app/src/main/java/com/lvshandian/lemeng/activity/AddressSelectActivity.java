package com.lvshandian.lemeng.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.adapter.mine.RecycleViewDivider;
import com.lvshandian.lemeng.interfaces.ItemClickListener;
import com.lvshandian.lemeng.entity.ProvinceCity;
import com.lvshandian.lemeng.adapter.ProvinceRecycleAdapter;
import com.lvshandian.lemeng.utils.Constant;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * 地址选择
 */
public class AddressSelectActivity extends BaseActivity {
    @Bind(R.id.rlv_recycler)
    RecyclerView rlvRecycler;
    @Bind(R.id.rb_women)
    RadioButton rbWomen;
    @Bind(R.id.rb_all)
    RadioButton rbAll;
    @Bind(R.id.rb_men)
    RadioButton rbMen;
    @Bind(R.id.rg_group)
    RadioGroup rgGroup;
    private List<ProvinceCity> mDatas = new ArrayList<>();
    private ProvinceRecycleAdapter mAdapter;
    public static AddressSelectActivity addressSelectActivity;

    private boolean only = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_select;
    }

    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onItemClick(int position) {
            ProvinceCity provinceCity = mDatas.get(position);
            Intent intent = new Intent(mContext, CityListActivity.class);
            switch (rgGroup.getCheckedRadioButtonId()) {
                case R.id.rb_women:
                    intent.putExtra(getString(R.string.province_gender), 0);
                    break;
                case R.id.rb_men:
                    intent.putExtra(getString(R.string.province_gender), 1);
                    break;
                case R.id.rb_all:
                    intent.putExtra(getString(R.string.province_gender), 2);
                    break;
            }
            intent.putExtra(getString(R.string.province_city), provinceCity);
            startActivity(intent);
            finish();
        }
    };

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String text = getString(R.string.looks_all);
            switch (checkedId) {
                case R.id.rb_women:
                    SharedPreferenceUtils.put(mContext, Constant.GENDER, 0);
                    text = getString(R.string.look_female);
                    break;
                case R.id.rb_all:
                    SharedPreferenceUtils.put(mContext, Constant.GENDER, 2);
                    text = getString(R.string.looks_all);
                    break;
                case R.id.rb_men:
                    SharedPreferenceUtils.put(mContext, Constant.GENDER, 1);
                    text = getString(R.string.look_male);
                    break;
            }
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(itemClickListener);
        rgGroup.setOnCheckedChangeListener(checkedChangeListener);
    }


    @Override
    protected void initialized() {
        only = getIntent().getBooleanExtra("only", false);
        if (only) {
            rgGroup.setVisibility(View.GONE);
        } else {
            rgGroup.setVisibility(View.VISIBLE);
        }

        addressSelectActivity = this;
        initTitle("", null, null);
        switch ((Integer) SharedPreferenceUtils.get(this, Constant.GENDER, 2)) {
            case 0:
                rbWomen.setChecked(true);
                break;
            case 1:
                rbMen.setChecked(true);
                break;
            case 2:
                rbAll.setChecked(true);
                break;
        }
        readAssets();
        initRecycler();
    }

    private void initRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        rlvRecycler.setLayoutManager(gridLayoutManager);
        RecycleViewDivider divider = new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.divider);
        rlvRecycler.addItemDecoration(divider);
        mAdapter = new ProvinceRecycleAdapter(this, mDatas);
        rlvRecycler.setAdapter(mAdapter);
    }

    private void readAssets() {
        String fromAssets = getFromAssets("city.json");
        LogUtils.e("fromAssets: " + fromAssets);
        List<ProvinceCity> cities = JsonUtil.json2BeanList(fromAssets, ProvinceCity.class);
        LogUtils.e("cities: " + cities);
        if (cities != null) {
            mDatas.addAll(cities);
        }
    }

    //从assets 文件夹中获取文件并读取数据
    public String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "encoding");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
