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
import android.widget.PopupWindow;
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
import com.lvshandian.lemeng.moudles.mine.bean.OtherPersonBean;
import com.lvshandian.lemeng.moudles.mine.bean.PhotoBean;
import com.lvshandian.lemeng.moudles.mine.bean.VideoBean;
import com.lvshandian.lemeng.moudles.mine.fragment.adapter.PhotoAdapter;
import com.lvshandian.lemeng.moudles.mine.fragment.adapter.VideoAdapter;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.view.ExpandGridView;
import com.lvshandian.lemeng.view.HeadZoomScrollView;
import com.lvshandian.lemeng.wangyiyunxin.session.SessionHelper;
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
    @Bind(R.id.tv_foucs)
    TextView tvFoucs;
    @Bind(R.id.tv_gz)
    TextView tv_gz;
    @Bind(R.id.tv_fs)
    TextView tv_fs;
    @Bind(R.id.tv_address)
    TextView tv_address;
    @Bind(R.id.rl_focus)
    RelativeLayout rlFocus;
    @Bind(R.id.rl_talk)
    RelativeLayout rlTalk;
    @Bind(R.id.iv_focus)
    ImageView iv_focus;
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
    private PopupWindow rightPopup;
    /**
     * 举报弹框
     */
    private PopupWindow reportPopup;
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
                    showToast("举报成功");
                    break;
                case RequestCode.MY_PHOTO_LOAD://图片请求列表
                    List<PhotoBean> listAdd = JsonUtil.json2BeanList(json.toString(), PhotoBean.class);

                    imgList.clear();
                    for (int i = 0 , j = listAdd.size(); i < j; i++) {
                        imgList.add(listAdd.get(i).getUrl());
                    }

                    tv_phone_num.setText(listAdd.size() + "张照片");
                    list.clear();
                    list.addAll(listAdd);
                    adapter.notifyDataSetChanged();

                    mygrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Intent intent = new Intent(getContext(), PhotoDetails.class);
//                            intent.putExtra("photo", list.get(position));
//                            intent.putExtra("isShow", "notShow");
//                            startActivity(intent);
                            startActivity(new Intent(mContext, BigImageActivity.class).putStringArrayListExtra("imageList", (ArrayList<String>) imgList).putExtra("clickPosition",position));
                        }
                    });
                    break;
                case RequestCode.MY_VIDEO_LOAD://视频请求列表
                    final List<VideoBean> listAdds = JsonUtil.json2BeanList(json.toString(), VideoBean.class);

                    tv_video_num.setText(listAdds.size() + "个短片");
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
        tv_gz.setOnClickListener(this);
        tv_fs.setOnClickListener(this);
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
                        avHeader.setLayoutParams(lp);
                        break;
                    case MotionEvent.ACTION_UP:
//                        AutoRelativeLayout.LayoutParams lp1 = new AutoRelativeLayout.LayoutParams(AutoRelativeLayout.LayoutParams.MATCH_PARENT, 520);
//                        myHead.setLayoutParams(lp1);

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DisplayMetrics dm = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                int width = dm.widthPixels;
                                int height = width * 520 / 750;
                                AutoRelativeLayout.LayoutParams lp1 = new AutoRelativeLayout.LayoutParams(width, height);
                                avHeader.setLayoutParams(lp1);
                            }
                        },400);

                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initialized() {
        userId = getIntent().getStringExtra(getString(R.string.visiti_person));
        //判断是否拉黑
        black = NIMClient.getService(FriendService.class).isInBlackList("miu_" + userId);
        if (black) {
            isBlackList = "取消拉黑";
        } else {
            isBlackList = "拉黑";
        }
        adapter = new PhotoAdapter(mContext, list, "notShow");
        mygrid.setAdapter(adapter);
        adaptervideo = new VideoAdapter(mContext, listvideo, "notShow");
        mygrid_samll_video.setAdapter(adaptervideo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestVisitorInfo();
        requestPhoto();
        requestVideo();
        parent_scrollView.smoothScrollTo(0, 0);
    }

    public static void start(Context context, String userId) {
        Intent intent = new Intent(context, OtherPersonHomePageActivity.class);
        intent.putExtra(context.getResources().getString(R.string.visiti_person), userId);
        context.startActivity(intent);
    }


    private void initInfo() {
        if (mOtherBean != null) {
            final String picUrl = mOtherBean.getPicUrl();
//            PicassoUtil.newInstance().onRoundnessImage(mContext, picUrl, avHeader);
            Picasso.with(mContext).load(picUrl).into(avHeader);

            String nickName = mOtherBean.getNickName();
            String id = mOtherBean.getId();
            String online = mOtherBean.getOnline();

            tvName.setText(nickName + "[" + (online.endsWith("0") ? "离线" : "在线") + "]");
            tvId.setText("乐檬号:" + id);
            String gender = mOtherBean.getGender();
            ivSex.setImageResource(TextUtils.equals(gender, "1") ? R.mipmap.male : R.mipmap.female);

            String gradeSatisfied = mOtherBean.getGradeSatisfied();
            int i = Integer.parseInt(gradeSatisfied);
            ivGrade.setImageResource(GrademipmapUtils.LevelImg[i]);

            String signature = mOtherBean.getSignature();
            tvSign.setText(signature);

            String fansNum = mOtherBean.getFansNum();
            tvFanse.setText(fansNum);

            String followNum = mOtherBean.getFollowNum();
            tvFoucs.setText(followNum);

            focus();
            avHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, BigImageActivity.class).putExtra("imageStr", picUrl));
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
        } else if (TextUtils.equals("1", follow)) {
            iv_focus.setImageResource(R.mipmap.icon_focus);
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
            httpDatas.getNewDataCharServerNoLoading("查询个人主页", Request.Method.POST, UrlBuilder.IF_ATTENTION, map, mHandler, RequestCode.REQUEST_USER_INFO);
        }

    }


    /**
     * 请求相册
     */
    private void requestPhoto() {
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        httpDatas.getDataForJsoNoloading("图片请求列表", Request.Method.GET, UrlBuilder.myPhoto(userId), map, mHandler, RequestCode.MY_PHOTO_LOAD);
    }


    /**
     * 请求短片
     */
    private void requestVideo() {
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        httpDatas.getDataForJsoNoloading("视频图片请求列表", Request.Method.GET, UrlBuilder.myVideo(userId), map, mHandler, RequestCode.MY_VIDEO_LOAD);

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
        httpDatas.getDataForJson("举报信息", Request.Method.POST, UrlBuilder.report, map, mHandler, RequestCode.REQUEST_REPORT);
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
                showToast("加入黑名单成功");
                isBlackList = "取消拉黑";
                black = NIMClient.getService(FriendService.class).isInBlackList(account);
            }

            @Override
            public void onFailed(int code) {
                showToast("加入黑名单失败,code:" + code);
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
                showToast("移出黑名单成功");
                isBlackList = "拉黑";
                black = NIMClient.getService(FriendService.class).isInBlackList(account);
            }

            @Override
            public void onFailed(int code) {
                showToast("移出黑名单失败，错误码：" + code);
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
        String url = UrlBuilder.serverUrl + UrlBuilder.ATTENTION_USER;
        LogUtils.e("ulr: " + url);
        OkHttpUtils.postString().url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json"))
                .build().execute(new CustomStringCallBack(mContext, HttpDatas.KEY_CODE) {
            @Override
            public void onFaild() {
                String toast;
                if (TextUtils.equals(follow, "1")) {
                    toast = "取消关注失败";
                } else {
                    toast = "关注失败";
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
        rightPopup = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.report_blacklist_popup, null);
        rightPopup.setContentView(view);
        rightPopup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        rightPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        rightPopup.setFocusable(true);
        rightPopup.setBackgroundDrawable(new BitmapDrawable());
        rightPopup.setOutsideTouchable(true);

        backgroundAlpha(0.5f);

        rightPopup.setAnimationStyle(R.style.mypopwindow_anim_style);
        rightPopup.showAtLocation(rlFocus, Gravity.BOTTOM, 0, 0);
        rightPopup.update();
        rightPopup.setOnDismissListener(new PopOnDismissListner());

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
        reportPopup = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.report_popupwindow, null);
        reportPopup.setContentView(view);
        reportPopup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        reportPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        reportPopup.setFocusable(true);
        reportPopup.setBackgroundDrawable(new BitmapDrawable());
        reportPopup.setOutsideTouchable(true);

        backgroundAlpha(0.5f);

        reportPopup.setAnimationStyle(R.style.mypopwindow_anim_style);
        reportPopup.showAtLocation(rlFocus, Gravity.BOTTOM, 0, 0);
        reportPopup.update();
        reportPopup.setOnDismissListener(new PopOnDismissListner());

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

    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = alpha;
        this.getWindow().setAttributes(params);
    }


    private class PopOnDismissListner implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
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
            case R.id.tv_fs://查看他的粉丝
                Intent intent = new Intent(mContext, FunseListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.tv_gz://查看他的关注
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
                report("低俗色情");
                reportPopup.dismiss();
                break;
            case R.id.btn_ljgg:
                report("垃圾广告");
                reportPopup.dismiss();
                break;
            case R.id.btn_wfxx:
                report("违法信息");
                reportPopup.dismiss();
                break;
            case R.id.btn_qzpq:
                report("欺诈骗钱");
                reportPopup.dismiss();
                break;
            case R.id.btn_other:
                report("其它");
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
