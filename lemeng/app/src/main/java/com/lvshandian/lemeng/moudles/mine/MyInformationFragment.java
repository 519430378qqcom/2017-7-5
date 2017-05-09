package com.lvshandian.lemeng.moudles.mine;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseFragment;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.bean.MyContributionBeanBack;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.activity.BigImageActivity;
import com.lvshandian.lemeng.moudles.mine.activity.MyCoinsActicity;
import com.lvshandian.lemeng.moudles.mine.activity.MyGradeActivity;
import com.lvshandian.lemeng.moudles.mine.activity.SettingPerson;
import com.lvshandian.lemeng.moudles.mine.bean.PhotoBean;
import com.lvshandian.lemeng.moudles.mine.bean.VideoBean;
import com.lvshandian.lemeng.moudles.mine.fragment.adapter.PhotoAdapter;
import com.lvshandian.lemeng.moudles.mine.fragment.adapter.VideoAdapter;
import com.lvshandian.lemeng.moudles.mine.my.AllPhoneActivity;
import com.lvshandian.lemeng.moudles.mine.my.AllVideoActivity;
import com.lvshandian.lemeng.moudles.mine.my.AuthenticationActivity;
import com.lvshandian.lemeng.moudles.mine.my.FollowListActivity;
import com.lvshandian.lemeng.moudles.mine.my.FunseListActivity;
import com.lvshandian.lemeng.moudles.mine.my.MyContributionActivity;
import com.lvshandian.lemeng.moudles.mine.my.RealNameVertifyActivity;
import com.lvshandian.lemeng.moudles.mine.my.SettingActivity;
import com.lvshandian.lemeng.moudles.mine.my.Videotails;
import com.lvshandian.lemeng.utils.CacheUtils;
import com.lvshandian.lemeng.utils.CountUtils;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.view.ExpandGridView;
import com.lvshandian.lemeng.view.HeadZoomScrollView;
import com.lvshandian.lemeng.widget.AvatarView;
import com.maiml.wechatrecodervideolibrary.recoder.WechatRecoderActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 登录用户中心Fragment页面
 */
public class MyInformationFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.parent_scrollView)
    HeadZoomScrollView parent_scrollView;
    @Bind(R.id.iv_edit)
    ImageView ivEdit;
    @Bind(R.id.my_head)
    ImageView myHead;
    @Bind(R.id.sex)
    ImageView sex;
    @Bind(R.id.iv_level)
    ImageView ivLevel;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.id)
    TextView id;
    @Bind(R.id.fanse)
    TextView fanse;
    @Bind(R.id.attention)
    TextView attention;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.setting)
    TextView setting;
    @Bind(R.id.tv_coin)
    TextView tvCoin;
    @Bind(R.id.ll_coin)
    AutoRelativeLayout llCoin;
    @Bind(R.id.ll_contribution)
    AutoRelativeLayout llContribution;
    @Bind(R.id.ll_myLevel)
    AutoRelativeLayout llMylevel;
    @Bind(R.id.ll_earnest)
    AutoRelativeLayout llEarnest;
    @Bind(R.id.my_level)
    TextView my_level;
    @Bind(R.id.verified)
    TextView tvVerified;
    @Bind(R.id.tv_phone_num)
    TextView tv_phone_num;
    @Bind(R.id.tv_video_num)
    TextView tv_video_num;
    @Bind(R.id.tv_gz)
    TextView tv_gz;
    @Bind(R.id.tv_fs)
    TextView tv_fs;
    @Bind(R.id.iv_1)
    AvatarView iv_1;
    @Bind(R.id.iv_2)
    AvatarView iv_2;
    @Bind(R.id.iv_3)
    AvatarView iv_3;
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
     * 展示图片适配器
     */
    private PhotoAdapter adapter;
    private VideoAdapter adaptervideo;

    /**
     * 从图库中上传图片的requestCode
     */
    protected static final int CHOOSE_PICTURE = 120;
    private static final int REQ_CODE = 10001;

    public List<PhotoBean> list = new ArrayList<>();
    public List<VideoBean> listvideo = new ArrayList<>();
    public List<String> imgList = new ArrayList<>();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                case RequestCode.SELECT_USER:
                    AppUser appUser = JsonUtil.json2Bean(json, AppUser.class);
                    CacheUtils.saveObject(getActivity(), appUser, CacheUtils.USERINFO);
                    initUserInfo(appUser);
                    break;
                case RequestCode.MY_PHOTO_LOAD://图片请求列表
                    List<PhotoBean> listAdd = JsonUtil.json2BeanList(json.toString(), PhotoBean.class);

                    imgList.clear();
                    for (int i = 0, j = listAdd.size(); i < j; i++) {
                        imgList.add(listAdd.get(i).getUrl());
                    }

                    if (tv_phone_num == null)
                        return;
                    tv_phone_num.setText(listAdd.size() + "张照片");
                    list.clear();
                    list.addAll(listAdd);
                    list.add(new PhotoBean());
                    adapter.notifyDataSetChanged();

                    mygrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (list.size() < 4) {
                                if (position == list.size() - 1) {
                                    //进行头像的上传  调用相册权限
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    getActivity().startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    //不对图片进行裁剪
//                                getActivity().startActivityForResult(openAlbumIntent, CROP_SMALL_PICTURE);
                                } else {
//                                    Intent intent = new Intent(getContext(), PhotoDetails.class);
//                                    intent.putExtra("photo", list.get(position));
//                                    intent.putExtra("isShow", "isShow");
//                                    startActivity(intent);
                                    startActivity(new Intent(getActivity(), BigImageActivity.class).putStringArrayListExtra("imageList", (ArrayList<String>) imgList).putExtra("clickPosition", position));
                                }
                            } else {
                                if (position == 3) {
                                    //进行头像的上传  调用相册权限
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    getActivity().startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    //不对图片进行裁剪
//                                getActivity().startActivityForResult(openAlbumIntent, CROP_SMALL_PICTURE);

                                } else {
//                                    Intent intent = new Intent(getContext(), PhotoDetails.class);
//                                    intent.putExtra("photo", list.get(position));
//                                    intent.putExtra("isShow", "isShow");
//                                    startActivity(intent);
                                    startActivity(new Intent(getActivity(), BigImageActivity.class).putStringArrayListExtra("imageList", (ArrayList<String>) imgList).putExtra("clickPosition", position));
                                }
                            }
                        }
                    });
                    break;
                case RequestCode.MY_VIDEO_LOAD://视频请求列表
                    List<VideoBean> listAdds = JsonUtil.json2BeanList(json.toString(), VideoBean.class);

                    if (tv_video_num == null)
                        return;
                    tv_video_num.setText(listAdds.size() + "个短片");
                    listvideo.clear();
                    listvideo.addAll(listAdds);
                    listvideo.add(new VideoBean());
                    adaptervideo.notifyDataSetChanged();

                    mygrid_samll_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (listvideo.size() < 4) {
                                if (position == listvideo.size() - 1) {
                                    //进行视频的录制
                                    WechatRecoderActivity.launchActivity(getActivity(), REQ_CODE);
                                } else {
                                    Intent intent = new Intent(getContext(), Videotails.class);
                                    intent.putExtra("video", listvideo.get(position));
                                    intent.putExtra("isShow", "isShow");
                                    getActivity().startActivity(intent);
                                }
                            } else {
                                if (position == 3) {
                                    //进行视频的录制
                                    WechatRecoderActivity.launchActivity(getActivity(), REQ_CODE);
                                } else {
                                    Intent intent = new Intent(getContext(), Videotails.class);
                                    intent.putExtra("video", listvideo.get(position));
                                    intent.putExtra("isShow", "isShow");
                                    getActivity().startActivity(intent);
                                }
                            }

                        }
                    });
                    break;
                //贡献榜
                case RequestCode.REQUEST_RANK:
                    List<MyContributionBeanBack> listBean = JsonUtil.json2BeanList(json, MyContributionBeanBack.class);
                    handlerContribution(listBean);
                    break;

            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_information;
    }

    @Override
    protected void initListener() {
        llCoin.setOnClickListener(this);
        llEarnest.setOnClickListener(this);
        llContribution.setOnClickListener(this);
        llMylevel.setOnClickListener(this);
        tv_watch_all_phone.setOnClickListener(this);
        tv_watch_all_video.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        tv_fs.setOnClickListener(this);
        tv_gz.setOnClickListener(this);
        setting.setOnClickListener(this);

        parent_scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();

                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        AutoRelativeLayout.LayoutParams lp = new AutoRelativeLayout.LayoutParams(AutoRelativeLayout.LayoutParams.MATCH_PARENT, AutoRelativeLayout.LayoutParams.MATCH_PARENT);
                        myHead.setLayoutParams(lp);

                        break;
                    case MotionEvent.ACTION_UP:

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DisplayMetrics dm = new DisplayMetrics();
                                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                                int width = dm.widthPixels;
                                int height = width * 520 / 750;
                                AutoRelativeLayout.LayoutParams lp1 = new AutoRelativeLayout.LayoutParams(width, height);
                                myHead.setLayoutParams(lp1);
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
        adapter = new PhotoAdapter(mContext, list, "isShow");
        mygrid.setAdapter(adapter);
        adaptervideo = new VideoAdapter(mContext, listvideo, "isShow");
        mygrid_samll_video.setAdapter(adaptervideo);
        initUserInfo(appUser);
        parent_scrollView.smoothScrollTo(0, 0);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUser();
        requestPhoto();
        requestVideo();
        requestContribution();
    }

    /**
     * 请求用户信息
     */
    private void initUser() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId());
        httpDatas.getNewDataCharServerCodeNoLoading("查询用户信息", Request.Method.POST, UrlBuilder.selectUserInfo, map, mHandler, RequestCode.SELECT_USER);
    }

    /**
     * 查询我的贡献榜
     */
    private void requestContribution() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("userId", appUser.getId());
        map.put("rows", "10");
        map.put("page", "1");
        httpDatas.getNewDataCharServerNoLoading("查询排我的贡献榜", Request.Method.GET, UrlBuilder.myContribution, map, mHandler, RequestCode.REQUEST_RANK);
    }

    /**
     * 请求图片列表
     */
    private void requestPhoto() {
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        httpDatas.getDataForJsoNoloading("图片请求列表", Request.Method.GET, UrlBuilder.myPhoto(appUser.getId()), map, mHandler, RequestCode.MY_PHOTO_LOAD);
    }

    /**
     * 请求小视频列表
     */
    private void requestVideo() {
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        httpDatas.getDataForJsoNoloading("视频图片请求列表", Request.Method.GET, UrlBuilder.myVideo(appUser.getId()), map, mHandler, RequestCode.MY_VIDEO_LOAD);
    }


    @Subscribe
    public void onEventMainThread(PhotoBean photoBean) {
        if ("yes".equals(photoBean.getId())) {
            requestPhoto();
        }
    }

    //接受方：
    @Subscribe //第2步:注册一个在后台线程执行的方法,用于接收事件
    public void onEventMainThread(VideoBean videoBean) {
        if ("yes".equals(videoBean.getId())) {
            requestVideo();
        }
    }


    /**
     * 回显用户信息
     *
     * @param userInfo
     */
    private void initUserInfo(AppUser userInfo) {
        if (userInfo != null) {
            if (name == null || id == null || fanse == null || attention == null || sex == null || ivLevel == null
                    || tvSign == null || myHead == null || tvCoin == null || my_level == null || tvVerified == null)
                return;
            String nickName = userInfo.getNickName();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(nickName))
                name.setText(nickName);

            String idInfo = userInfo.getId();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(idInfo))
                id.setText("海颜号:" + idInfo);

            String fansNum = userInfo.getFansNum();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(fansNum)) {
                fanse.setText(fansNum);
            } else {
                fanse.setText(String.valueOf(0));
            }

            String points = userInfo.getFollowNum();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(points)) {
                attention.setText(points);
            } else {
                attention.setText(String.valueOf(0));
            }

            String gender = userInfo.getGender();
            if (TextUtils.equals("0", gender)) {
                sex.setImageResource(R.mipmap.female);
            } else if (TextUtils.equals("1", gender)) {
                sex.setImageResource(R.mipmap.male);
            } else {
                sex.setVisibility(View.GONE);
            }

            String gradeSatisfied = userInfo.getLevel();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(gradeSatisfied)) {
                ivLevel.setImageResource(GrademipmapUtils.LevelImg[Integer.valueOf(gradeSatisfied) - 1]);
            } else {
                ivLevel.setImageResource(GrademipmapUtils.LevelImg[0]);
            }

            String signature = userInfo.getSignature();
            if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(signature)) {
                tvSign.setText(signature);
            } else {
                tvSign.setText("这个家伙很懒，什么都没留下");
            }

            final String picUrl = userInfo.getPicUrl();
            Picasso.with(mContext).load(picUrl).into(myHead);
            myHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), BigImageActivity.class).putExtra("imageStr", picUrl));
                }
            });

            String goldCoin = SharedPreferenceUtils.getGoldCoin(mContext);
            if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(goldCoin))
                goldCoin = "0";
            goldCoin = CountUtils.getCount(Long.parseLong(goldCoin));
            tvCoin.setText(goldCoin);

            String level = userInfo.getLevel();
            if (com.lvshandian.lemeng.utils.TextUtils.isEmpty(level))
                level = "0";
            my_level.setText(level);

            if (TextUtils.isEmpty(userInfo.getVerified())) {
                tvVerified.setText("未认证");
                return;
            }
            String verified = userInfo.getVerified();
            if (verified.equals("0")) {
                tvVerified.setText("未认证");
            } else if (verified.equals("1")) {
                tvVerified.setText("认证中");
            } else if (verified.equals("2")) {
                tvVerified.setText("已认证");
            } else if (verified.equals("3")) {
                tvVerified.setText("认证失败");
            }
        }

    }

    /**
     * 排行榜数据填充
     *
     * @param listBean
     */
    private void handlerContribution(List<MyContributionBeanBack> listBean) {
        if (iv_1 == null || iv_2 == null || iv_3 == null)
            return;
        if (listBean.size() == 0) {
            iv_1.setVisibility(View.GONE);
            iv_2.setVisibility(View.GONE);
            iv_3.setVisibility(View.GONE);
        } else if (listBean.size() == 1) {
            iv_1.setVisibility(View.GONE);
            iv_2.setVisibility(View.GONE);
            iv_3.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(listBean.get(0).getPicUrl(), iv_3);
        } else if (listBean.size() == 2) {
            iv_1.setVisibility(View.GONE);
            iv_2.setVisibility(View.VISIBLE);
            iv_3.setVisibility(View.VISIBLE);
            ImageLoader instance = ImageLoader.getInstance();
            instance.displayImage(listBean.get(0).getPicUrl(), iv_2);
            instance.displayImage(listBean.get(1).getPicUrl(), iv_3);
        } else if (listBean.size() >= 3) {
            iv_1.setVisibility(View.VISIBLE);
            iv_2.setVisibility(View.VISIBLE);
            iv_3.setVisibility(View.VISIBLE);
            ImageLoader instance = ImageLoader.getInstance();
            instance.displayImage(listBean.get(0).getPicUrl(), iv_1);
            instance.displayImage(listBean.get(1).getPicUrl(), iv_2);
            instance.displayImage(listBean.get(2).getPicUrl(), iv_3);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_coin://我的颜票
                Intent intentCoins = new Intent(mContext, MyCoinsActicity.class);
                startActivity(intentCoins);
                break;
            case R.id.ll_earnest://认证
                AppUser userInfo = (AppUser) CacheUtils.readObject(mContext, CacheUtils.USERINFO);
                String verified = userInfo.getVerified();
                if (TextUtils.equals(verified, "1")) {
                    //已提交认证
                    gotoActivity(RealNameVertifyActivity.class, false);
                } else if (TextUtils.equals(verified, "2")) {
                    showToast("您已认证通过了哦!");
                } else {
                    //未认证
                    gotoActivity(AuthenticationActivity.class, false);
                }
                break;
            case R.id.ll_myLevel://我的等级
                gotoActivity(MyGradeActivity.class, false);
                break;
            case R.id.ll_contribution://我的贡献榜
                Intent intent = new Intent(mContext, MyContributionActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_watch_all_phone:
                getActivity().startActivity(new Intent(mContext, AllPhoneActivity.class).putExtra("userId", appUser.getId()).putExtra("isShow", "isShow"));
                break;
            case R.id.tv_watch_all_video:
                getActivity().startActivity(new Intent(mContext, AllVideoActivity.class).putExtra("userId", appUser.getId()).putExtra("isShow", "isShow"));
                break;
            case R.id.iv_edit:
                startActivity(new Intent(getContext(), SettingPerson.class).putExtra("isRegister", "unRegister"));
                break;
            case R.id.tv_fs:
                Intent intent1 = new Intent(getActivity(), FunseListActivity.class);
                intent1.putExtra("userId", appUser.getId());
                startActivity(intent1);
                break;
            case R.id.tv_gz:
                Intent intent2 = new Intent(getActivity(), FollowListActivity.class);
                intent2.putExtra("userId", appUser.getId());
                startActivity(intent2);
                break;
            case R.id.setting:
                gotoActivity(SettingActivity.class, false);
                break;
        }

    }

}
