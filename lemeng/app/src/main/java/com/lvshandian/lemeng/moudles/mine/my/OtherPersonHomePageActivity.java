package com.lvshandian.lemeng.moudles.mine.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.base.CustomStringCallBack;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.activity.BigImageActivity;
import com.lvshandian.lemeng.moudles.mine.adapter.PhotoAdapter;
import com.lvshandian.lemeng.moudles.mine.adapter.VideoAdapter;
import com.lvshandian.lemeng.moudles.mine.bean.OtherPersonBean;
import com.lvshandian.lemeng.moudles.mine.bean.PhotoBean;
import com.lvshandian.lemeng.moudles.mine.bean.VideoBean;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.wangyiyunxin.session.SessionHelper;
import com.lvshandian.lemeng.widget.view.CustomPopWindow;
import com.lvshandian.lemeng.widget.view.ExpandGridView;
import com.lvshandian.lemeng.widget.view.HeadZoomScrollView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.squareup.okhttp.MediaType;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * Created by gjj on 2016/11/23.
 */

public class OtherPersonHomePageActivity extends BaseActivity {
    @Bind(R.id.parent_scrollView)
    HeadZoomScrollView parent_scrollView;
    @Bind(R.id.back_left)
    TextView backLeft;
    @Bind(R.id.tv_report_right)
    TextView reportRight;
    @Bind(R.id.av_header)
    ImageView avHeader;
    @Bind(R.id.iv_sex)
    ImageView ivSex;
    @Bind(R.id.iv_grade)
    ImageView ivGrade;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_id)
    TextView tvId;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.tv_fanse)
    TextView tvFanse;
    @Bind(R.id.tv_attention)
    TextView tvAttention;
    @Bind(R.id.ll_attention)
    LinearLayout llAttention;
    @Bind(R.id.ll_fans)
    LinearLayout llFans;
    @Bind(R.id.tv_address)
    TextView tv_address;
    @Bind(R.id.rl_focus)
    RelativeLayout rlFocus;
    @Bind(R.id.rl_talk)
    RelativeLayout rlTalk;
    @Bind(R.id.iv_focus)
    ImageView iv_focus;
    @Bind(R.id.tv_focus)
    TextView tv_focus;
    @Bind(R.id.tv_phone_num)
    TextView tv_phone_num;
    @Bind(R.id.tv_video_num)
    TextView tv_video_num;
    @Bind(R.id.tv_watch_all_phone)
    TextView tv_watch_all_phone;
    @Bind(R.id.tv_watch_all_video)
    TextView tv_watch_all_video;
    /**
     * 用于展示小视频的GridView
     */
    @Bind(R.id.mygrid_samll_video)
    ExpandGridView mygrid_samll_video;
    /**
     * 用于展示图片的GridView
     */
    @Bind(R.id.mygrid)
    ExpandGridView mygrid;

    /**
     * 别人主页的id
     */
    private String userId;
    /**
     * 别人主页的bean
     */
    private OtherPersonBean mOtherBean;
    /**
     * 右上角弹框
     */
    private CustomPopWindow rightPopup;
    /**
     * 举报弹框
     */
    private CustomPopWindow reportPopup;
    /**
     * 标记是否被拉黑
     */
    private boolean black;

    public List<PhotoBean> list = new ArrayList<PhotoBean>();
    public List<VideoBean> listvideo = new ArrayList<VideoBean>();
    public List<String> imgList = new ArrayList<>();

    /**
     * 展示图片适配器
     */
    private PhotoAdapter adapter;
    private VideoAdapter adaptervideo;

    /**
     * 控制界面显示拉黑还是取消拉黑
     */
    private String isBlackList = "";

    private int width;
    private int height;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);
            switch (msg.what) {
                //关注请求接收数据
                case RequestCode.REQUEST_USER_INFO:
                    mOtherBean = JsonUtil.json2Bean(json, OtherPersonBean.class);
                    initInfo();
                    break;
                //关注请求接收数据
                case RequestCode.REQUEST_REPORT:
                    showToast(getString(R.string.report_succeed));
                    break;
                case RequestCode.MY_PHOTO_LOAD://图片请求列表
                    List<PhotoBean> listAdd = JsonUtil.json2BeanList(json.toString(), PhotoBean.class);

                    imgList.clear();
                    for (int i = 0, j = listAdd.size(); i < j; i++) {
                        imgList.add(listAdd.get(i).getUrl());
                    }
                    tv_phone_num.setText(getString(R.string.phone_amount, String.valueOf(listAdd.size())));
                    list.clear();
                    list.addAll(listAdd);
                    adapter.notifyDataSetChanged();

                    mygrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(mContext, BigImageActivity.class).putStringArrayListExtra("imageList", (ArrayList<String>) imgList).putExtra("clickPosition", position));
                        }
                    });

                    parent_scrollView.smoothScrollTo(0, 0);
                    break;
                case RequestCode.MY_VIDEO_LOAD://视频请求列表
                    final List<VideoBean> listAdds = JsonUtil.json2BeanList(json.toString(), VideoBean.class);

                    tv_phone_num.setText(getString(R.string.small_video_amount, String.valueOf(listAdds.size())));
                    listvideo.clear();
                    listvideo.addAll(listAdds);
                    adaptervideo.notifyDataSetChanged();


                    mygrid_samll_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), Videotails.class);
                            intent.putExtra("video", listvideo.get(position));
                            intent.putExtra("isShow", "notShow");
                            startActivity(intent);
                        }
                    });

                    parent_scrollView.smoothScrollTo(0, 0);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_other_person_page;
    }

    @Override
    protected void initListener() {
        backLeft.setOnClickListener(this);
        reportRight.setOnClickListener(this);
        rlFocus.setOnClickListener(this);
        rlTalk.setOnClickListener(this);
        llAttention.setOnClickListener(this);
        llFans.setOnClickListener(this);
        tv_watch_all_phone.setOnClickListener(this);
        tv_watch_all_video.setOnClickListener(this);

        parent_scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();

                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        AutoRelativeLayout.LayoutParams lp = new AutoRelativeLayout.LayoutParams(AutoRelativeLayout.LayoutParams.MATCH_PARENT, AutoRelativeLayout.LayoutParams.MATCH_PARENT);
                        if (avHeader != null) {
                            avHeader.setLayoutParams(lp);
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AutoRelativeLayout.LayoutParams lp1 = new AutoRelativeLayout.LayoutParams(width, height);
                                if (avHeader != null) {
                                    avHeader.setLayoutParams(lp1);
                                }
                            }
                        }, 400);

                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initialized() {
        userId = getIntent().getStringExtra(getString(R.string.visit_person));
        //判断是否拉黑
        black = NIMClient.getService(FriendService.class).isInBlackList("miu_" + userId);
        if (black) {
            isBlackList = getString(R.string.cancel_blacklist);
        } else {
            isBlackList = getString(R.string.blacklist);
        }
        adapter = new PhotoAdapter(mContext, list, "notShow");
        mygrid.setAdapter(adapter);
        adaptervideo = new VideoAdapter(mContext, listvideo, "notShow");
        mygrid_samll_video.setAdapter(adaptervideo);

        if (avHeader == null)
            return;
        avHeader.post(new Runnable() {
            @Override
            public void run() {
                if (avHeader != null) {
                    width = avHeader.getWidth();
                    height = avHeader.getHeight();
                } else {
                    DisplayMetrics dm = new DisplayMetrics();
                    if (getWindowManager() == null)
                        return;
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    width = dm.widthPixels;
                    height = width * 520 / 750;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestVisitorInfo();
        requestPhoto();
        requestVideo();

    }

    public static void start(Context context, String userId) {
        Intent intent = new Intent(context, OtherPersonHomePageActivity.class);
        intent.putExtra(context.getResources().getString(R.string.visit_person), userId);
        context.startActivity(intent);
    }


    private void initInfo() {
        if (mOtherBean != null) {
            String picUrl = mOtherBean.getPicUrl();
            if (TextUtils.isEmpty(picUrl)) {
                picUrl = UrlBuilder.HEAD_DEFAULT;
            }
            Picasso.with(mContext).load(picUrl).placeholder(R.mipmap.zhan_da)
                    .error(R.mipmap.zhan_da).into(avHeader);

            String nickName = mOtherBean.getNickName();
            String id = mOtherBean.getId();
//            String online = mOtherBean.getOnline();

            tvName.setText(nickName);
            tvId.setText(getString(R.string.lemeng_id, id));
            String gender = mOtherBean.getGender();
            ivSex.setImageResource(TextUtils.equals(gender, "1") ? R.mipmap.male : R.mipmap.female);

            String gradeSatisfied = mOtherBean.getGradeSatisfied();
            int i = Integer.parseInt(gradeSatisfied);
            ivGrade.setImageResource(GrademipmapUtils.LevelImg[i]);


            String signature = mOtherBean.getSignature();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(signature)) {
                tvSign.setText(signature);
            } else {
                tvSign.setText(getString(R.string.default_sign));
            }

            String fansNum = mOtherBean.getFansNum();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(fansNum)) {
                tvFanse.setText(fansNum);
            } else {
                tvFanse.setText(String.valueOf(0));
            }

            String followNum = mOtherBean.getFollowNum();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(followNum)) {
                tvAttention.setText(followNum);
            } else {
                tvAttention.setText(String.valueOf(0));
            }

            focus();
            final String finalPicUrl = picUrl;
            avHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, BigImageActivity.class).putExtra("imageStr", finalPicUrl));
                }
            });

            tv_address.setText(mOtherBean.getAddress());
        }
    }

    /**
     * 显示关注(未关注)
     */
    private void focus() {
        String follow = mOtherBean.getFollow();
        if (TextUtils.equals("0", follow)) {
            iv_focus.setImageResource(R.mipmap.icon_unfocus);
            tv_focus.setText(getString(R.string.attention));
            tvFanse.setText(String.valueOf(Integer.parseInt(tvFanse.getText().toString()) - 1));
        } else if (TextUtils.equals("1", follow)) {
            iv_focus.setImageResource(R.mipmap.icon_focus);
            tv_focus.setText(getString(R.string.already_attention));
            tvFanse.setText(String.valueOf(Integer.parseInt(tvFanse.getText().toString()) + 1));
        }
    }

    /**
     * 获取其他人信息
     */
    private void requestVisitorInfo() {
        if (userId != null) {
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
            map.put("userId", userId);
            map.put("currentUserId", appUser.getId());
            httpDatas.getNewDataCharServer("查询个人主页", false, Request.Method.POST, UrlBuilder.IF_ATTENTION, map, mHandler, RequestCode.REQUEST_USER_INFO, TAG);
        }

    }


    /**
     * 请求相册
     */
    private void requestPhoto() {
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        httpDatas.getDataForJson("图片请求列表", false, Request.Method.GET, UrlBuilder.myPhoto(userId), map, mHandler, RequestCode.MY_PHOTO_LOAD, TAG);
    }


    /**
     * 请求短片
     */
    private void requestVideo() {
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        httpDatas.getDataForJson("视频图片请求列表", false, Request.Method.GET, UrlBuilder.myVideo(userId), map, mHandler, RequestCode.MY_VIDEO_LOAD, TAG);

    }

    /**
     * 举报
     *
     * @param str
     */
    private void report(String str) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("reportUserId", appUser.getId());
        map.put("userId", userId);
        map.put("content", str);
        httpDatas.getDataForJson("举报信息", true, Request.Method.POST, UrlBuilder.REPORT, map, mHandler, RequestCode.REQUEST_REPORT, TAG);
    }


    /**
     * 加入黑名单
     *
     * @author sll
     * @time 2016/11/30 17:40
     */
    private void addUserToBlackList(final String account) {
        NIMClient.getService(FriendService.class).addToBlackList(account).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                showToast(getString(R.string.add_blacklist_succeed));
                isBlackList = getString(R.string.cancel_blacklist);
                black = NIMClient.getService(FriendService.class).isInBlackList(account);
            }

            @Override
            public void onFailed(int code) {
                showToast(getString(R.string.add_blacklist_failure));
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    /**
     * 移出黑名单
     *
     * @param account
     */
    private void removeUserToBlackList(final String account) {
        NIMClient.getService(FriendService.class).removeFromBlackList(account).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                showToast(getString(R.string.remove_blacklist_succeed));
                isBlackList = getString(R.string.blacklist);
                black = NIMClient.getService(FriendService.class).isInBlackList(account);
            }

            @Override
            public void onFailed(int code) {
                showToast(getString(R.string.remove_blacklist_failure));
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }


    /**
     * 关注(取消关注)
     */
    private void changeFollow() {
        final String follow = mOtherBean.getFollow();
        Map<String, String> params = new HashMap<>();
        params.put("userId", appUser.getId());
        params.put("followUserId", mOtherBean.getId());
        JSONObject jsonObject = new JSONObject(params);
        String json = jsonObject.toString();
        String url = UrlBuilder.SERVER_URL + UrlBuilder.ATTENTION_USER;
        LogUtils.e("ulr: " + url);
        OkHttpUtils.postString().url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json"))
                .build().execute(new CustomStringCallBack(mContext, HttpDatas.KEY_CODE) {
            @Override
            public void onFaild() {
                String toast;
                if (TextUtils.equals(follow, "1")) {
                    toast = getString(R.string.cancel_attention_failure);
                } else {
                    toast = getString(R.string.attention_failure);
                }
                showToast(toast);
            }

            @Override
            public void onSucess(String data) {
                if (TextUtils.equals(follow, "1")) {
                    mOtherBean.setFollow("0");
                } else {
                    mOtherBean.setFollow("1");
                }
                focus();
            }
        });
    }


    /**
     * 右上角举报和加入黑名单弹框
     */
    public void getRightPopup() {
        rightPopup = new CustomPopWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.report_blacklist_popup, null);
        rightPopup.setContentView(view);
        rightPopup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        rightPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        rightPopup.setFocusable(true);
        rightPopup.setBackgroundDrawable(new BitmapDrawable());
        rightPopup.setOutsideTouchable(true);

        rightPopup.setAnimationStyle(R.style.mypopwindow_anim_style);
        rightPopup.showAtLocation(rlFocus, Gravity.BOTTOM, 0, 0);

        Button btn_add_blacklist = (Button) view.findViewById(R.id.btn_add_blacklist);
        Button btn_report = (Button) view.findViewById(R.id.btn_report);
        Button btn_cancel_1 = (Button) view.findViewById(R.id.btn_cancel_1);
        btn_add_blacklist.setText(isBlackList);
        btn_add_blacklist.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        btn_cancel_1.setOnClickListener(this);
    }


    /**
     * 举报弹框
     */
    public void getReportPopup() {
        reportPopup = new CustomPopWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.report_popupwindow, null);
        reportPopup.setContentView(view);
        reportPopup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        reportPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        reportPopup.setFocusable(true);
        reportPopup.setBackgroundDrawable(new BitmapDrawable());
        reportPopup.setOutsideTouchable(true);

        reportPopup.setAnimationStyle(R.style.mypopwindow_anim_style);
        reportPopup.showAtLocation(rlFocus, Gravity.BOTTOM, 0, 0);

        Button btn_dssq = (Button) view.findViewById(R.id.btn_dssq);
        Button btn_ljgg = (Button) view.findViewById(R.id.btn_ljgg);
        Button btn_wfxx = (Button) view.findViewById(R.id.btn_wfxx);
        Button btn_qzpq = (Button) view.findViewById(R.id.btn_qzpq);
        Button btn_other = (Button) view.findViewById(R.id.btn_other);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_dssq.setOnClickListener(this);
        btn_ljgg.setOnClickListener(this);
        btn_wfxx.setOnClickListener(this);
        btn_qzpq.setOnClickListener(this);
        btn_other.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.tv_report_right://举报
                getRightPopup();
                break;
            case R.id.rl_focus://点击关注/取消关注
                changeFollow();
                break;
            case R.id.rl_talk://点击聊天
                SessionHelper.startP2PSession(this, "miu_" + mOtherBean.getId());
                break;
            case R.id.ll_fans://查看他的粉丝
                Intent intent = new Intent(mContext, FunseListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.ll_attention://查看他的关注
                intent = new Intent(mContext, FollowListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.btn_add_blacklist:
                if (black) {
                    removeUserToBlackList("miu_" + mOtherBean.getId());
                } else {
                    addUserToBlackList("miu_" + mOtherBean.getId());
                }
                rightPopup.dismiss();
                break;
            case R.id.btn_report:
                rightPopup.dismiss();
                getReportPopup();
                break;
            case R.id.btn_cancel_1:
                rightPopup.dismiss();
                break;
            case R.id.btn_dssq:
                report(getString(R.string.dssq));
                reportPopup.dismiss();
                break;
            case R.id.btn_ljgg:
                report(getString(R.string.ljgg));
                reportPopup.dismiss();
                break;
            case R.id.btn_wfxx:
                report(getString(R.string.wfxx));
                reportPopup.dismiss();
                break;
            case R.id.btn_qzpq:
                report(getString(R.string.qzpq));
                reportPopup.dismiss();
                break;
            case R.id.btn_other:
                report(getString(R.string.other));
                reportPopup.dismiss();
                break;
            case R.id.btn_cancel:
                reportPopup.dismiss();
                break;
            case R.id.tv_watch_all_phone:
                startActivity(new Intent(mContext, AllPhoneActivity.class).putExtra("userId", userId).putExtra("isShow", "notShow"));
                break;
            case R.id.tv_watch_all_video:
                startActivity(new Intent(mContext, AllVideoActivity.class).putExtra("userId", userId).putExtra("isShow", "notShow"));
                break;
        }
    }
}
