package com.lvshandian.lemeng.moudles.mine.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.utils.CacheUtils;
import com.lvshandian.lemeng.utils.PicassoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 我的等级界面
 * Created by Administrator on 2017/1/17.
 */

public class MyGradeActivity extends BaseActivity {
    @Bind(R.id.my_head)
    ImageView myHead;
    @Bind(R.id.experienceStart)
    TextView tvStart;
    @Bind(R.id.experienceEnd)
    TextView tvEnd;
    @Bind(R.id.level)
    TextView tvLevel;
    @Bind(R.id.levelStart)
    TextView tvLevelStart;
    @Bind(R.id.levelEnd)
    TextView tvLevelEnd;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    private String startPoints;
    private String level;
    private String endPoints;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                case RequestCode.SELECT_USER:
                    appUser = JSON.parseObject(json, AppUser.class);
                    CacheUtils.saveObject(MyGradeActivity.this, appUser, CacheUtils.USERINFO);
                    startPoints = appUser.getPoints();
                    level = appUser.getLevel();
                    queryLevel();
                    break;
                case RequestCode.SELECT_LEVEL:
                    try {
                        JSONObject obj = new JSONObject(json);
                        endPoints = obj.getString("endPoint");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    upDateView();
                    break;
            }
        }
    };

    private void upDateView() {
        tvLevelStart.setText("Lv." + level);
        tvLevelEnd.setText("Lv." + (Integer.valueOf(level)+1));
        tvStart.setText("我的经验值:" + startPoints);
        tvEnd.setText("升级还需经验值:" + (Integer.valueOf(endPoints) - Integer.valueOf(startPoints)));
        tvLevel.setText("Lv." + level);
        progressBar.setMax(Integer.valueOf(endPoints));
        progressBar.setProgress(Integer.valueOf(startPoints));
    }

    /**
     * 查询等级信息,所在级数升级所需经验等
     */
    private void queryLevel() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("level", level);
        httpDatas.getNewDataCharServer("查询等级信息", Request.Method.POST, UrlBuilder.selectLevel, map, mHandler, RequestCode.SELECT_LEVEL);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_grade;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        initTitle("", "我的等级", null);
        initInfo();
    }

    private void initInfo() {
        String picUrl = appUser.getPicUrl();
        PicassoUtil.newInstance().onRoundnessImage(mContext, picUrl, myHead);

        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId());
        httpDatas.getNewDataCharServer("查询个人信息", Request.Method.POST, UrlBuilder.selectUserInfo, map, mHandler, RequestCode.SELECT_USER);

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
