package com.lvshandian.lemeng.moudles.index.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseFragment;
import com.lvshandian.lemeng.bean.JoinRoomBean;
import com.lvshandian.lemeng.bean.LiveBean;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.index.adapter.HotListAadapter;
import com.lvshandian.lemeng.moudles.index.live.WatchLiveActivity;
import com.lvshandian.lemeng.utils.Constant;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.widget.FullyLinearLayoutManager;
import com.lvshandian.lemeng.widget.MyRecyclerView;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 地址Fragment
 */
public class AddressFragment extends BaseFragment implements SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener {
    @Bind(R.id.lv_live_room)
    MyRecyclerView addressRecyclerView;
    @Bind(R.id.iv_empty)
    ImageView iv_empty;
    @Bind(R.id.mrl_layout)
    SwipeRefreshLayout mrlLayout;

    private boolean isfirst = true;
    List<LiveListBean> liveListBeen = new ArrayList<>();
    private HotListAadapter addressListAadapter;
    private int page = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.obj);
            if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(json)) {
                return;
            }
            switch (msg.what) {
                //最新
                case RequestCode.HOT_LIVE:
                    if (mrlLayout == null)
                        return;
                    finishRefresh();
                    List<LiveListBean> liveListBeen1 = JsonUtil.json2BeanList(json.toString(), LiveListBean.class);
                    if (null == liveListBeen1 || liveListBeen1.size() == 0) {
                        if (page == 1) {
                            liveListBeen.clear();
                            addressListAadapter.notifyDataSetChanged();
                        }
                        return;
                    }
                    if (page == 1) {
                        liveListBeen.clear();
                    }
                    liveListBeen.addAll(liveListBeen1);
                    addressListAadapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new;
    }

    @Override
    protected void initListener() {
        initList();
    }

    @Override
    protected void initialized() {
        isfirst = false;
        addressRecyclerView.setEmptyView(iv_empty);
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
    }

    public void initList() {
        //设置刷新逻辑
        mrlLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        mrlLayout.setOnRefreshListener(this);
        mrlLayout.setOnPullUpRefreshListener(this);
        mrlLayout.setColorSchemeColors(getResources().getColor(R.color.main));

        addressListAadapter = new HotListAadapter(mContext, liveListBeen);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
        addressRecyclerView.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        addressRecyclerView.setLayoutManager(layoutManager);
        addressRecyclerView.setAdapter(addressListAadapter);

        addressListAadapter.setOnRecyclerClickListener(new HotListAadapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerClick(int position) {
//              进入直播间
                ifEnter(liveListBeen.get(position));
            }
        });

    }

    public void getLiveList(int page) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        String city = (String) SharedPreferenceUtils.get(getActivity(), Constant.ADDRESS, "北京");
        map.put("city", city);
        map.put("gender", (Integer) SharedPreferenceUtils.get(getContext(), Constant.GENDER, 2) + "");
        map.put("page", page + "");
        map.put("type", "3");
        httpDatas.getNewDataCharServerRefresh("获取地区直播接口列表", Request.Method.GET, UrlBuilder.appRooms, map, mHandler, RequestCode.HOT_LIVE, mrlLayout);
    }


    /**
     * 跳转到观看页面
     *
     * @author sll
     * @time 2016/12/16 16:58
     */
    private void startActivityToWatch(LiveBean live) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("LIVE", live);
        Intent intent = new Intent(getActivity(), WatchLiveActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 判断是否进入过该私密直播
     *
     * @author sll
     * @time 2016/12/16 17:14
     */
    public void ifEnter(final LiveBean live) {
        String url = UrlBuilder.chargeServerUrl + UrlBuilder.ifEnter;
        LogUtils.e("WangYi_secret", "url: " + url);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("roomId", live.getId());
        hashMap.put("userId", appUser.getId());
        String json = new JSONObject(hashMap).toString();
        LogUtils.e("Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        LogUtils.i("WangYi_secret", "是否进入过该私密直播: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("WangYi_secret", "是否进入过该私密直播: " + response);
                        JoinRoomBean joinRoom = JsonUtil.json2Bean(response, JoinRoomBean.class);
                        if (joinRoom != null && joinRoom.isSuccess() && joinRoom.getCode() != 1) {
//                            //第一次进入
//                            if (mUserList != null && live != null && !TextUtils.isEmpty(live.getPrivateFlag()) && live.getPrivateFlag().equals("1")) {
//                                joinSecret(live);
//                            } else {
//                                startActivityToWatch(live);
//                            }
                        } else {
                            //不是第一次，直接进入
                            startActivityToWatch(live);
                        }
                    }
                });
    }

    /**
     * 支付进入该私密直播
     * "createrId":createId , @"entrantId":userId ,@"coinNum":coinNum
     *
     * @author sll
     * @time 2016/12/16 17:14
     */
    private void updateCoin(final LiveBean live) {
        String url = UrlBuilder.chargeServerUrl + UrlBuilder.updateCoin;
        LogUtils.e("WangYi_secret", "url: " + url);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("createrId", live.getCreator().getId());
        hashMap.put("entrantId", appUser.getId());
        hashMap.put("coinNum", live.getRoomPay());
        String json = new JSONObject(hashMap).toString();
        LogUtils.e("Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        LogUtils.i("WangYi_secret", "付进入该私密直播: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("WangYi_secret", "付进入该私密直播: " + response);
                        JoinRoomBean joinRoom = JsonUtil.json2Bean(response, JoinRoomBean.class);
                        if (joinRoom != null && joinRoom.isSuccess() && joinRoom.getCode() == 1) {
                            startActivityToWatch(live);
                        } else {
                            //不是第一次，直接进入
                            showToast("账户余额不足");
                        }
                    }
                });

    }

    /**
     * 私密直播时弹出，须支付或输入密码
     *
     * @author sll
     * @time 2016/12/16 15:59
     */
    private void joinSecret(final LiveBean live) {
        final Dialog dialog = new Dialog(getActivity(), R.style.homedialog);
        final View view = View.inflate(getActivity(), R.layout.dialog_join_secret, null);
        final TextView joinSecretPrompt = (TextView) view.findViewById(R.id.join_secret_prompt);
        final LinearLayout joinSecretCancel = (LinearLayout) view.findViewById(R.id.join_secret_cancel);
        joinSecretPrompt.setText("需要密码或" + live.getRoomPay() + "金币进入该房间");
        //支付
        view.findViewById(R.id.join_secret_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCoin(live);
                dialog.dismiss();
            }
        });
        //密码
        view.findViewById(R.id.join_secret_pw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinForPw(live);
                dialog.dismiss();
            }
        });
        //取消
        joinSecretCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 支付密码进入私密直播
     *
     * @author sll
     * @time 2016/12/16 16:34
     */
    private void joinForPw(final LiveBean live) {
        final Dialog dialog = new Dialog(getActivity(), R.style.homedialog);
        final View view = View.inflate(getActivity(), R.layout.dialog_join_secret_pwd, null);
        final EditText pwdEdit = (EditText) view.findViewById(R.id.join_secret_pwd_edit);
        //取消
        view.findViewById(R.id.join_secret_pwd_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确定
        view.findViewById(R.id.join_secret_pwd_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pwdEdit.getText().toString())) {
                    showToast("请输入密码");
                } else if (pwdEdit.getText().toString().equals(live.getRoomPw())) {
                    //密码正确，进入直播间
                    startActivityToWatch(live);
                    dialog.dismiss();
                } else {
                    showToast("密码错误，请确认后再输入");
                }
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
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
