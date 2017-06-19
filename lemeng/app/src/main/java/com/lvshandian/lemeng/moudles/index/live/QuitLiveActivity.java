package com.lvshandian.lemeng.moudles.index.live;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.utils.CountUtils;
import com.lvshandian.lemeng.utils.FastBlurUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 退出直播后显示本次直播信息的界面
 */
public class QuitLiveActivity extends BaseActivity {

    @Bind(R.id.live_time)
    TextView liveTime;
    @Bind(R.id.receive_yanpiao)
    TextView receiveYanpiao;
    @Bind(R.id.watch_count)
    TextView watchCount;
    @Bind(R.id.add_fense)
    TextView addFense;
    @Bind(R.id.quit)
    TextView quit;
    @Bind(R.id.live_time_all)
    TextView liveTimeAll;
    @Bind(R.id.receive_yanpiao_all)
    TextView receiveYanpiaoAll;
    @Bind(R.id.img_bg)
    ImageView img_bg;
    /**
     * 直播开启时间
     */
    private long startTime;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.obj);

            switch (msg.what) {
                case RequestCode.SELECTE_QUIT:
                    long endTime = System.currentTimeMillis();
                    long time = endTime - startTime;
                    long second = time / 1000;
                    String secondTime = (second / 3600 < 10 ? "0" + second / 3600 : second / 3600) + ":"
                            + (second % 3600 / 60 < 10 ? "0" + second % 3600 / 60 : second % 3600 / 60) + ":"
                            + (second % 60 < 10 ? "0" + second % 60 : second % 60);
                    liveTime.setText(secondTime);
                    JSONObject obj = null;
                    if (json.contains("goldAddNum")) {
                        try {
                            obj = new JSONObject(json);
                            addFense.setText(obj.getInt("fanAddNum") + "");

                            String goldAddNum = obj.getLong("goldAddNum") + "";
                            goldAddNum = CountUtils.getCount(Long.parseLong(goldAddNum));
                            receiveYanpiao.setText(goldAddNum);

                            JSONObject obj1 = new JSONObject(obj.getString("rooms"));
                            watchCount.setText(obj1.getLong("onlineUserNum") + "");

                            String goldCoin = obj.getLong("goldCoin") + "";
                            goldCoin = CountUtils.getCount(Long.parseLong(goldCoin));
                            receiveYanpiaoAll.setText(goldCoin);

                            long liveTime = obj.getLong("liveTime") / 1000;
                            String allTime = (liveTime / 3600 < 10 ? "0" + liveTime / 3600 : liveTime / 3600) + ":"
                                    + (liveTime % 3600 / 60 < 10 ? "0" + liveTime % 3600 / 60 : liveTime % 3600 / 60) + ":"
                                    + (liveTime % 60 < 10 ? "0" + liveTime % 60 : liveTime % 60);
                            liveTimeAll.setText(allTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            obj = new JSONObject(json);
                            addFense.setText(obj.getInt("fanAddNum") + "");
                            JSONObject obj1 = new JSONObject(obj.getString("rooms"));
                            watchCount.setText(obj1.getInt("onlineUserNum") + "");

                            String goldCoin = obj.getLong("goldCoin") + "";
                            goldCoin = CountUtils.getCount(Long.parseLong(goldCoin));
                            receiveYanpiaoAll.setText(goldCoin);

                            long liveTime = obj.getLong("liveTime") / 1000;
                            String allTime = (liveTime / 3600 < 10 ? "0" + liveTime / 3600 : liveTime / 3600) + ":"
                                    + (liveTime % 3600 / 60 < 10 ? "0" + liveTime % 3600 / 60 : liveTime % 3600 / 60) + ":"
                                    + (liveTime % 60 < 10 ? "0" + liveTime % 60 : liveTime % 60);
                            liveTimeAll.setText(allTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_quit_live;
    }

    @Override
    protected void initListener() {
        quit.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        String roomId = getIntent().getStringExtra("roomId");
        startTime = getIntent().getLongExtra("startTime", 0);

        final String picUrl = appUser.getPicUrl();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap blurBitmap2 = FastBlurUtil.GetUrlBitmap(picUrl, 4);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img_bg.setImageBitmap(blurBitmap2);
                    }
                });
            }
        }).start();
        initQuitInfo(roomId);
    }

    /**
     * 获取主播退出后的信息
     */
    private void initQuitInfo(String roomId) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("userId", appUser.getId());
        map.put("roomId", roomId);
        httpDatas.getNewDataCharServer("查询主播离开房间信息", Request.Method.GET, UrlBuilder.SELECT_QUITE_INFO, map, mHandler, RequestCode.SELECTE_QUIT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit:
                finish();
                break;
        }
    }


}
