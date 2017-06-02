package com.lvshandian.lemeng.moudles.index.live;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.adapter.FamilyMemberAdapter;
import com.lvshandian.lemeng.adapter.GridViewAdapter;
import com.lvshandian.lemeng.adapter.ViewPageGridViewAdapter;
import com.lvshandian.lemeng.base.BarrageDateBean;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.base.CustomStringCallBack;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.bean.BlBean;
import com.lvshandian.lemeng.bean.CustomGiftBean;
import com.lvshandian.lemeng.bean.CustomLianmaiBean;
import com.lvshandian.lemeng.bean.CustomdateBean;
import com.lvshandian.lemeng.bean.DianBoDateBean;
import com.lvshandian.lemeng.bean.GiftBean;
import com.lvshandian.lemeng.bean.IfAttentionBean;
import com.lvshandian.lemeng.bean.LastAwardBean;
import com.lvshandian.lemeng.bean.LiveBean;
import com.lvshandian.lemeng.bean.LiveEven;
import com.lvshandian.lemeng.bean.LiveFamilyMemberBean;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.bean.RoomUserBean;
import com.lvshandian.lemeng.bean.RoomUserDataBean;
import com.lvshandian.lemeng.gles.FBO;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.httprequest.SdkHttpResultSuccess;
import com.lvshandian.lemeng.moudles.index.CustomNotificationType;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BankerBalance;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BankerInfo;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BetResult;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BullfightInterface;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BullfightPresenter;
import com.lvshandian.lemeng.moudles.index.live.bullfight.GameResult;
import com.lvshandian.lemeng.moudles.index.live.bullfight.PokerResult;
import com.lvshandian.lemeng.moudles.index.live.bullfight.StartResult;
import com.lvshandian.lemeng.moudles.index.live.bullfight.TimeAndNper;
import com.lvshandian.lemeng.moudles.index.live.gift.GiftFrameLayout;
import com.lvshandian.lemeng.moudles.index.live.gift.GiftSendModel;
import com.lvshandian.lemeng.moudles.index.live.utils.AnchorVideo;
import com.lvshandian.lemeng.moudles.index.live.utils.LiveVideo;
import com.lvshandian.lemeng.moudles.mine.my.ContributionActivity;
import com.lvshandian.lemeng.moudles.mine.my.OtherPersonHomePageActivity;
import com.lvshandian.lemeng.moudles.mine.my.adapter.OnItemClickListener;
import com.lvshandian.lemeng.moudles.mine.my.adapter.RoomUsersDataAdapter;
import com.lvshandian.lemeng.utils.AnimationUtils;
import com.lvshandian.lemeng.utils.ChannelToLiveBean;
import com.lvshandian.lemeng.utils.Config;
import com.lvshandian.lemeng.utils.CountUtils;
import com.lvshandian.lemeng.utils.DESUtil;
import com.lvshandian.lemeng.utils.DateUtils;
import com.lvshandian.lemeng.utils.FastBlur;
import com.lvshandian.lemeng.utils.FramesSequenceAnimation;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.lvshandian.lemeng.utils.JavaBeanMapUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.KeyBoardUtils;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SendRoomMessageUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.ThreadManager;
import com.lvshandian.lemeng.utils.ToastUtils;
import com.lvshandian.lemeng.view.BarrageView;
import com.lvshandian.lemeng.view.CameraLivePreviewFrameView;
import com.lvshandian.lemeng.view.LoadingDialog;
import com.lvshandian.lemeng.view.RotateLayout;
import com.lvshandian.lemeng.view.RoundDialog;
import com.lvshandian.lemeng.view.ScrollRelativeLayout;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment.ChatRoomMessageFragment;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.helper.ChatRoomMemberCache;
import com.lvshandian.lemeng.wangyiyunxin.live.fragment.ChatRoomSessionListFragment;
import com.lvshandian.lemeng.wangyiyunxin.live.fragment.LiveMessageFragment;
import com.lvshandian.lemeng.wangyiyunxin.main.helper.SystemMessageUnreadManager;
import com.lvshandian.lemeng.wangyiyunxin.main.reminder.ReminderItem;
import com.lvshandian.lemeng.wangyiyunxin.main.reminder.ReminderManager;
import com.lvshandian.lemeng.wangyiyunxin.main.reminder.ReminderSettings;
import com.lvshandian.lemeng.widget.AvatarView;
import com.lvshandian.lemeng.widget.TimeCountDownLayout;
import com.lvshandian.lemeng.widget.myrecycler.RefreshRecyclerView;
import com.lvshandian.lemeng.widget.myrecycler.manager.RecyclerMode;
import com.lvshandian.lemeng.widget.myrecycler.manager.RecyclerViewManager;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomStatusChangeData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.AudioSourceCallback;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingPreviewCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.SurfaceTextureCallback;
import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.squareup.okhttp.MediaType;
import com.squareup.picasso.Picasso;
import com.tandong.bottomview.view.BottomView;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import xiao.free.horizontalrefreshlayout.HorizontalRefreshLayout;
import xiao.free.horizontalrefreshlayout.RefreshCallBack;
import xiao.free.horizontalrefreshlayout.refreshhead.LoadingRefreshHeader;

/**
 * 观看直播页面
 * Created by 张振 on 2016/11/13.
 */

public class WatchLiveActivity extends BaseActivity implements ReminderManager
        .UnreadNumChangedCallback,
        View.OnLayoutChangeListener, StreamStatusCallback,
        StreamingPreviewCallback, SurfaceTextureCallback, AudioSourceCallback,
        StreamingSessionListener, BullfightInterface,
        StreamingStateChangedListener, CameraLivePreviewFrameView.Listener, RefreshCallBack {

    private VerticalViewPager mViewPager;
    private RelativeLayout mRoomContainer;
    private PagerAdapter mPagerAdapter;
    private int mCurrentItem;
    private int mRoomId = -1;
    private List<LiveListBean> listBeenList = new ArrayList<>();
    private int position = 0;//当前直播位置
    private boolean isFirst = true;
    private boolean isCanStop = false;//是否可以停止刷新

    /**
     * 用于左右滑清屏
     */
    float DownX;
    float DownY;
    //是否隐藏直播界面内容
    private boolean isHindRcView = false;

    ImageView rlLoading;
    RelativeLayout ll_buttom_mun;
    ImageView ruanjianpanW;
    ImageView zhoubangW;
    TextView tv_lianmai;
    AvatarView liveHead;
    AvatarView liveLianmaiHead;
    AvatarView liveLianmaiHeadBg1;
    AvatarView liveLianmaiHeadBg2;
    TextView liveName;
    TextView liveNum;
    AutoLinearLayout llTangpiao;
    TextView liveJinpiao;
    TextView liveId;
    ImageView btnAttention;
    ImageView ivLivePrivatechat;
    ImageView ivLiveGift;
    //    ImageView ivLiveShare;
    GiftFrameLayout giftFrameLayout1;
    GiftFrameLayout giftFrameLayout2;
    ImageView liveClose;
    RecyclerView rlvListLiveAudiences;
    HorizontalRefreshLayout horizontalRefreshLayout;
    AutoRelativeLayout mRoot;
    ImageView tabNewIndicator;
    DropFake tabNewMsgLabel;
    SurfaceView videoLian;
    BarrageView barrageview;
    AutoFrameLayout flPull;
    AutoFrameLayout flPlug;
    ImageView watchRoomJaizu;
    RelativeLayout mSendGiftLian;
    ScrollRelativeLayout scrl;
    RelativeLayout rlHvView;
    TextView btn_timer;
    AvatarView liveHeadImg;
    SurfaceView mSurfaceView;

    ImageView ivBig;
    TextView tvBig;
    ImageView ivSamll;
    TextView tvSamll;
    ImageView ivSinge;
    TextView tvSinge;
    ImageView ivDouble;
    TextView tvDouble;
    ImageView ivBigSigle;
    TextView tvBigSigle;
    ImageView ivSamllSinge;
    TextView tvSamllSinge;
    ImageView ivBigDouble;
    TextView tvBigDouble;
    ImageView ivSamllDouble;
    TextView tvSamllDouble;
    ImageView ivMoreBig;
    TextView tvMoreBig;
    ImageView ivMoreSamll;
    TextView tvMoreSamll;

    AutoLinearLayout live_game;
    FrameLayout watch_room_message_fragment_chat;

    ImageView tv_rule;

    ImageView smallAdd;
    TextView samllNumber;
    ImageView smallSubtract;
    ImageView doubleSubtract;
    TextView doubleNumber;
    ImageView doubleAdd;
    ImageView ivTouzhu;

    ImageView iv_trend;

    TextView all_lepiao;

    TextView tv_periods;
    TextView frist_num;
    TextView second_num;
    TextView third_num;
    TextView all_num;
    TextView tv_ds;
    TimeCountDownLayout tv_game_next_open_time;
    LinearLayout rl_kp;

    private static final String TAG = "WatchLiveActivity";

    /**
     * 分享的地址
     */
    private final String share_url = "http://app.lemenglive.com/video/share.html";


    /**
     * 乐檬开始动画
     */
    private FramesSequenceAnimation framesSequenceAnimation;
    public static LoadingDialog mLoading;//进入直播间loading

    /**
     * 网易云信消息
     */
    private ChatRoomMessage message;

    /**
     *
     */
    private GoogleApiClient client;

    /**
     * 退出对话框
     */
    private RoundDialog mQuitDialog;

    /**
     * 主播对象实体类
     */
    private LiveListBean liveListBean;
    private LiveBean liveBean; //主播bean
    private CustomdateBean zhuBo;

    /**
     * 判断主播是否被关注过
     */
    private IfAttentionBean ifAttentionBean;

    /**
     * 网易云信的roomid
     */
    private String roomId;

    /**
     * 拉流地址
     */
    private String mVideoPath = "";

    /**
     * 初始化播放器
     */
    private AnchorVideo anchorVideo;

    /**
     * 定时器
     */
    private Timer timer = new Timer();

    /**
     * 根据头像列表个数,显示观看主播人数
     */
    private int liveOnLineNums;

    /**
     * 左下角消息模块显示消息布局
     */
    private ChatRoomMessageFragment messageFragment;


    /**
     * 登录网易云信聊天室
     */
    private AbortableFuture<EnterChatRoomResultData> enterRequest;

    /**
     * 私信的Fragment布局
     */
    private ChatRoomSessionListFragment sessionListFragment;

    /**
     * 点击游客资料中的私聊布局
     */
    private LiveMessageFragment liveMessageFragment;

    /**
     * Fragment管理
     */
    private FragmentManager fragmentManager;

    /**
     * Fragment业务
     */
    private FragmentTransaction transaction;

    /**
     * 是否第一次点赞,只有第一次点赞提示点赞消息
     */
    private boolean isfirstclick = true;

    /**
     * 游客信息Dialog
     */
    private Dialog dialogForSelect;

    /**
     * 头像列表当前页数
     */
    private int page = 1;
    /**
     * 头像列表总页数
     */
    private int totalPages = 1;

    /**
     * 是否是刷新的标记
     */
    private boolean isRefresh = true;

    /**
     * 头像数据内容
     */
    private List<RoomUserBean> mDatas = new LinkedList<>();

    /**
     * 头像列表适配器
     */
    private RoomUsersDataAdapter mAdapter;

    /**
     * RecyclerView布局管理器
     */
    private GridLayoutManager mLayoutManager;

    /**
     * 观众观看第二主播
     */
    private LiveVideo liveVideo;

    /**
     * 自己是否被禁言
     */
    public static boolean isJinyan;

    /**
     * ************************* 以下注释关于礼物 ********************************
     * <p>
     * /**
     * 界面用于显示礼物的ViewPager
     */
    private ViewPager mVpGiftView;

    /**
     * 礼物面板中的乐票数量
     */
    private TextView mUserCoin;

    /**
     * 礼物列表viewPager
     */
    private ViewPageGridViewAdapter mVpGiftAdapter;

    /**
     * 礼物服务端返回数据
     */
    private String mGiftResStr;

    /**
     * 显示礼物的PopupWindow
     */
    private PopupWindow popupWindow;

    /**
     * 获取到礼物面板的集合
     */
    private List<GiftBean> mGiftList = new ArrayList<>();

    /**
     * 每一页的礼物集合
     */
    private List<GridView> mGiftViews = new ArrayList<>();

    /**
     * 礼物面板上显示金币的布局
     */
    private LinearLayout ll_user_coin;

    /**
     * 点击发送后的礼物
     */
    private GiftBean mSendGiftItem;

    /**
     * 发送礼物的按钮
     */
    private Button mSendGiftBtn;

    /**
     * 发送礼物的集合
     */
    private List<GiftBean> mSendGiftList = new ArrayList<>();

    /**
     * 发送礼物连击次数的集合
     */
    private List<String> mSendGiftNumList = new ArrayList<>();

    /**
     * 连击的次数
     */
    private int SEND_NUM;


    /**
     * 发送礼物时，礼物的图片地址
     */
    private String staticIcon;

    /**
     * 礼物动画列表
     */
    private List<GiftSendModel> giftSendModelList = new ArrayList<>();

    /**
     * 是不是第一次连麦
     */
    private boolean isFristLianmai = true;

    /**
     * 礼物连点计时器
     */
    private CountDownTimer giftTimer;

    /**
     * 左上角主播收到金币数
     */
    private Long zhuboReceve;

    /**
     * 自己金币数
     */
    private Long myGoldCoin;

    private long mCountDownTotalTime;

    /**
     * 连麦人信息
     */
    private CustomLianmaiBean customLianmaiBean;


    private int tzNumber = 10;
    private int jbNumber = 1;

    private boolean isPlayerRoom;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                //礼物列表
                case RequestCode.GET_GIFT:
                    mGiftResStr = json;
                    mGiftList = JsonUtil.json2BeanList(mGiftResStr, GiftBean.class);
                    break;
                //关注请求接收数据
                case RequestCode.ATTENTION_USER:
                    btnAttention.setVisibility(View.GONE);
                    Map<String, Object> map0 = new HashMap<>();
                    map0.put("level", appUser.getLevel());
                    SendRoomMessageUtils.onCustomMessageGuanzhu("199", messageFragment, liveListBean.getRooms()
                            .getRoomId() + "", map0);
                    break;
                //赠送礼物
                case RequestCode.SEND_GIFT:
                    if (!data.getString(HttpDatas.error).equals(getString(R.string.pay_success))) {
                        showToast("支付失败");
                        return;
                    }

                    if (mSendGiftList.size() > 0) {
                        GiftBean mSendGiftItem = mSendGiftList.get(0);
                        mSendGiftList.remove(0);
                        String num = mSendGiftNumList.get(0);
                        mSendGiftNumList.remove(0);

                        Map<String, Object> map = new HashMap<>();
                        map.put("gift_item_index", mSendGiftItem.getId());
                        map.put("gift_item_number", "1");
                        map.put("gift_coinnumber_index", mSendGiftItem.getAnchorReceive());
                        map.put("gift_item_message", "赠送了" + "1个" + mSendGiftItem.getName());
                        map.put("vip", appUser.getVip());
                        map.put("userId", appUser.getId());
                        map.put("gift_Grand_Prix_number", null);
                        map.put("gift_Grand_Prix", null);
                        map.put("gift_giftCoinNumber", mSendGiftItem.getMemberConsume());
                        map.put("receveUserId", liveListBean.getId() + "");
                        map.put("gift_Type1", mSendGiftItem.getWinFlag());
                        map.put("level", appUser.getLevel());
                        map.put("RepeatGiftNumber", num);
                        SendRoomMessageUtils.onCustomMessageSendGift(messageFragment,
                                liveListBean.getRooms().getRoomId() + "", map);

                        myGoldCoin = myGoldCoin - Long.parseLong(mSendGiftItem.getMemberConsume());
                        String myCoin = CountUtils.getCount(myGoldCoin);
                        all_lepiao.setText(myCoin);

                    }
                    break;
                //获取主播的信息,是否关注主播
                case RequestCode.IF_ATTENTION:
                    LogUtils.i(json.toString());
                    zhuBo = JSON.parseObject(data.getString(HttpDatas.obj).toString(),
                            CustomdateBean.class);
                    ifAttentionBean = JsonUtil.json2Bean(json, IfAttentionBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Integer.parseInt(ifAttentionBean.getFollow()) == 1) {
                                btnAttention.setVisibility(View.GONE);
                            } else {
                                btnAttention.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    break;
                //请求用户信息
                case RequestCode.REQUEST_USER_INFO:
                    CustomdateBean customdateBean = JSON.parseObject(json, CustomdateBean.class);
                    showDialogForCallOther(customdateBean);
                    break;
                case RequestCode.ROOM_LIANMAI:
                    LogUtils.i("请求接口成功，可以连麦");
                    break;
                case RequestCode.TIMERLIVE:
                    LogUtils.i("主播隔30s时间刷新状态" + json.toString());
                    break;
                case 10001:
                    httpDatas.getDataDialog("主播隔一段时间刷新状态", false, urlBuilder.TimerUserLive
                                    (liveListBean.getRoomId() + "", liveListBean.getRooms().getId() + "")
                            , myHandler, RequestCode.TIMERLIVE);
                    break;
                //主播请求与观众连麦，观众同意后，生成推拉流地址，并向所有人发送推拉流
                case RequestCode.REQUEST_LIANMAI_LIVE:
                    LogUtils.i("获取推拉流地址成功" + json.toString());
                    //请求连麦数据
                    if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(json.toString())) {
                        if (json.toString().length() > 4) {
                            LiveEven liveEven = JsonUtil.json2Bean(json.toString(), LiveEven.class);
                            try {
                                LogUtils.e("地址地址推流地址" + DESUtil.decrypt(liveEven.getPublishUrl()));
                                LogUtils.e("地址地址拉流地址" + DESUtil.decrypt(liveEven.getBroadcastUrl()));
                                startLive(DESUtil.decrypt(liveEven.getPublishUrl()));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            initPaly();
                            mWl.acquire();
                            if (null != mWl) {
//                                mWl.acquire();
                                mMediaStreamingManager.resume();
                            }
                            Toast.makeText(mContext, "连麦成功", Toast.LENGTH_SHORT).show();

                            //发通知别人获取连麦推拉流
                            Map<String, Object> map1 = new HashMap<>();
                            map1.put("watch_private_flag", "0");
                            map1.put("userId", liveEven.getUserId());
                            map1.put("vip", "0");
                            map1.put("picUrl", liveEven.getUserAvatar());
                            String video = "";
                            try {
                                video = DESUtil.decrypt(liveEven.getBroadcastUrl());
                                map1.put("broadcastUrl", video);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            SendRoomMessageUtils.onCustomMessageLianmai(SendRoomMessageUtils.MESSAGE_WATCHER_CONNECT,
                                    messageFragment, liveListBean.getRooms().getRoomId() + "", map1);

                            showLianmaiView();
                            tv_lianmai.setVisibility(View.VISIBLE);

                            if (!TextUtils.isEmpty(appUser.getPicUrl())) {
                                Picasso.with(mContext).load(appUser.getPicUrl()).placeholder(R.mipmap.head_default)
                                        .error(R.mipmap.head_default).resize(50, 50).into(liveLianmaiHead);
                            }
                        }
                    }

                    break;
                //观众进入房间时，查询是否有被连麦人
                case RequestCode.IS_REQUEST_LIANMAI_LIVE:
                    LogUtil.e("正在连麦中", json.toString());
                    List<CustomLianmaiBean> lianmaiBeanList = JsonUtil.json2BeanList(json.toString(), CustomLianmaiBean.class);
                    if (lianmaiBeanList != null && lianmaiBeanList.size() > 0) {
                        try {
                            customLianmaiBean = lianmaiBeanList.get(0);
                            String video = DESUtil.decrypt(customLianmaiBean.getBroadcastUrl());
                            if (!TextUtils.isEmpty(customLianmaiBean.getUserAvatar())) {
                                Picasso.with(mContext).load(customLianmaiBean.getUserAvatar()).placeholder(R.mipmap.head_default)
                                        .error(R.mipmap.head_default).resize(50, 50).into(liveLianmaiHead);
                            }
                            showLianmaiView();
                            WatchLive(video);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case RequestCode.SELECT_USER:
                    LogUtils.e("查询个人信息返回json: " + json);
                    AppUser userZhubo = JsonUtil.json2Bean(json, AppUser.class);
                    String receivedGoldCoin = userZhubo.getReceivedGoldCoin();

                    zhuboReceve = Long.parseLong(receivedGoldCoin);
                    receivedGoldCoin = CountUtils.getCount(Long.parseLong(receivedGoldCoin));
                    liveJinpiao.setText(receivedGoldCoin); //显示左上角主播收到乐票数量
                    break;
                case RequestCode.SELECT_USER_MY:
                    LogUtils.e("查询个人信息返回json: " + json);
                    AppUser userMy = JsonUtil.json2Bean(json, AppUser.class);
                    SharedPreferenceUtils.saveUserInfo(mContext, userMy);
                    appUser = SharedPreferenceUtils.getUserInfo(mContext);

                    String myCoin = appUser.getGoldCoin();
                    myGoldCoin = Long.parseLong(myCoin);
                    myCoin = CountUtils.getCount(Long.parseLong(myCoin));
                    all_lepiao.setText(myCoin);
                    break;
                case RequestCode.REQUEST_REPORT:
                    showToast("已成功举报该用户");
                    break;
                case 10000:
                    LogUtil.e("mCountDownTotalTime", "mCountDownTotalTime" + mCountDownTotalTime);
                    mCountDownTotalTime = mCountDownTotalTime - 1000;
                    String time = DateUtils.millisToDateString(mCountDownTotalTime > 0 ? mCountDownTotalTime : 0, "mm:ss");
                    if (tv_game_next_open_time != null) {
                        tv_game_next_open_time.setText("00:" + time);
                    }
                    if (mCountDownTotalTime > 1000) {
                        myHandler.sendEmptyMessageDelayed(10000, 1000);
                    } else {
                        //获取近期开奖数据
                        getTimenumber();
                    }
                    break;
                case BULLFIGHT_TIME:
                    bullfightTimer();
                    break;
                case WAIT_NEXT_START:
                    bullfightPresenter.getTimeAndNper(liveListBean.getRoomId() + "");
                    break;
            }
        }
    };
    private String nper;
    private int intQh;
    private String countryType;
    private String strJinBi;
    private boolean isTouZhu;//是否可以投注
    private ImageView iv_game;
    private TextView tv_hz;
    //<start------------斗牛游戏部分-------------->
    RelativeLayout rl_game_container;
    ImageView iv_bullfight;
    AutoLinearLayout live_game_bullfight;
    RelativeLayout rl_game_info;
    AutoRelativeLayout rl_bullfight_banker;
    CircleImageView iv_bullfight_banker_head;
    TextView tv_bullfight_bankername;
    TextView tv_bullfight_banker_money;
    ImageView iv_poker_banker1;
    ImageView iv_poker_banker2;
    ImageView iv_poker_banker3;
    ImageView iv_poker_banker4;
    ImageView iv_poker_banker5;
    TextView tv_bullfight_totlanum1;
    TextView tv_bullfight_minenum1;
    AutoRelativeLayout rl_bullfight_betting_container1;
    ImageView iv_poker_palyer11;
    ImageView iv_poker_palyer12;
    ImageView iv_poker_palyer13;
    ImageView iv_poker_palyer14;
    ImageView iv_poker_palyer15;
    AutoRelativeLayout rl_bullfight1;
    TextView tv_bullfight_totlanum2;
    TextView tv_bullfight_minenum2;
    AutoRelativeLayout rl_bullfight_betting_container2;
    ImageView iv_poker_palyer21;
    ImageView iv_poker_palyer22;
    ImageView iv_poker_palyer23;
    ImageView iv_poker_palyer24;
    ImageView iv_poker_palyer25;
    AutoRelativeLayout rl_bullfight2;
    TextView tv_bullfight_totlanum3;
    TextView tv_bullfight_minenum3;
    AutoRelativeLayout rl_bullfight_betting_container3;
    ImageView iv_poker_palyer31;
    ImageView iv_poker_palyer32;
    ImageView iv_poker_palyer33;
    ImageView iv_poker_palyer34;
    ImageView iv_poker_palyer35;
    AutoRelativeLayout rl_bullfight3;
    TextView tv_timing;
    TextView tv_bullfight_result1;
    TextView tv_bullfight_result2;
    TextView tv_bullfight_result3;
    AutoLinearLayout ll_bullfight_result;
    AutoRelativeLayout rl_timing;
    TextView tv_bullfight_lepiao;
    TextView tv_bullfight_top_up;
    ImageView iv_10;
    ImageView iv_50;
    ImageView iv_100;
    ImageView iv_1000;
    ImageView iv_10000;
    ImageView iv_bull_amount0;
    ImageView iv_bull_amount1;
    ImageView iv_bull_amount2;
    ImageView iv_bull_amount3;
    AutoRelativeLayout rl_poker_banker_container;
    AutoRelativeLayout rl_poker_player_container1;
    AutoRelativeLayout rl_poker_player_container2;
    AutoRelativeLayout rl_poker_player_container3;
    /**
     * 标识直播间当前开启的游戏类型；1（彩票）;2（斗牛）
     */
    private int gameType;
    /**
     * 被选中的投注icon动画
     */
    private ValueAnimator valueAnimator;
    /**
     * 投注池支持显示投注视图的最大容量
     */
    private final int BETTING_POOL_VIEWS_CAPACITY = 30;
    /**
     * 剩余多长时间结束
     */
    private int nextTime;
    /**
     * 期数
     */
    private int uper;
    /**
     * 发送斗牛倒计时的消息标识
     */
    private final int BULLFIGHT_TIME = 233;
    /**
     * 等待下一局开始
     */
    private final int WAIT_NEXT_START = 234;

    private BullfightPresenter bullfightPresenter;
    /**
     * 投注金额
     */
    private int betBalance;
    /**
     * 投注位置
     */
    private int betPosition;
    /**
     * 是否等待下一局
     */
    private boolean isWait;
    //<end------------斗牛游戏部分-------------->

    @Override
    protected int getLayoutId() {
        return R.layout.activity_watchlive;
    }

    @Override
    protected void initialized() {
        mLoading = new LoadingDialog(mContext);
//        mLoading.show();

        mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);

        mRoomContainer = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.view_room_container, null);

        rlLoading = (ImageView) mRoomContainer.findViewById(R.id.iv_loading);
        ll_buttom_mun = (RelativeLayout) mRoomContainer.findViewById(R.id.ll_buttom_mun);
        ruanjianpanW = (ImageView) mRoomContainer.findViewById(R.id.ruanjianpanW);
        zhoubangW = (ImageView) mRoomContainer.findViewById(R.id.zhoubangW);
        tv_lianmai = (TextView) mRoomContainer.findViewById(R.id.tv_lianmai);
        liveHead = (AvatarView) mRoomContainer.findViewById(R.id.live_head);
        liveLianmaiHead = (AvatarView) mRoomContainer.findViewById(R.id.live_lianmai_head);
        liveLianmaiHeadBg1 = (AvatarView) mRoomContainer.findViewById(R.id.live_lianmai_head_bg1);
        liveLianmaiHeadBg2 = (AvatarView) mRoomContainer.findViewById(R.id.live_lianmai_head_bg2);
        liveName = (TextView) mRoomContainer.findViewById(R.id.live_name);
        liveNum = (TextView) mRoomContainer.findViewById(R.id.live_num);
        llTangpiao = (AutoLinearLayout) mRoomContainer.findViewById(R.id.ll_tp_labe);
        liveJinpiao = (TextView) mRoomContainer.findViewById(R.id.live_jinpiao);
        liveId = (TextView) mRoomContainer.findViewById(R.id.live_id);
        btnAttention = (ImageView) mRoomContainer.findViewById(R.id.btn_attention);
        ivLivePrivatechat = (ImageView) mRoomContainer.findViewById(R.id.iv_live_privatechat);
        ivLiveGift = (ImageView) mRoomContainer.findViewById(R.id.iv_live_gift);
//        ivLiveShare = (ImageView) mRoomContainer.findViewById(R.id.iv_live_share);
        iv_game = (ImageView) mRoomContainer.findViewById(R.id.iv_game);
        giftFrameLayout1 = (GiftFrameLayout) mRoomContainer.findViewById(R.id.gift_layout1);
        giftFrameLayout2 = (GiftFrameLayout) mRoomContainer.findViewById(R.id.gift_layout2);
        liveClose = (ImageView) mRoomContainer.findViewById(R.id.live_close);
        horizontalRefreshLayout = (HorizontalRefreshLayout) mRoomContainer.findViewById(R.id.horizontalRefreshLayout);
        rlvListLiveAudiences = (RecyclerView) mRoomContainer.findViewById(R.id.rlv_list_live_audiences);
        mRoot = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_live_root);
        tabNewIndicator = (ImageView) mRoomContainer.findViewById(R.id.tab_new_indicator);
        tabNewMsgLabel = (DropFake) mRoomContainer.findViewById(R.id.tab_new_msg_label);
        videoLian = (SurfaceView) mRoomContainer.findViewById(R.id.video_lian);
        barrageview = (BarrageView) mRoomContainer.findViewById(R.id.barrageview);
        flPull = (AutoFrameLayout) mRoomContainer.findViewById(R.id.fl_pull);
        flPlug = (AutoFrameLayout) mRoomContainer.findViewById(R.id.fl_plug);
        watchRoomJaizu = (ImageView) mRoomContainer.findViewById(R.id.watch_room_jiaZu);
        mSendGiftLian = (RelativeLayout) mRoomContainer.findViewById(R.id.iv_show_send_gift_lian);
        scrl = (ScrollRelativeLayout) mRoomContainer.findViewById(R.id.scrl);
        rlHvView = (RelativeLayout) mRoomContainer.findViewById(R.id.rl_hv_view);
        btn_timer = (TextView) mRoomContainer.findViewById(R.id.btn_timer);
        liveHeadImg = (AvatarView) mRoomContainer.findViewById(R.id.live_head_img);
        mSurfaceView = (SurfaceView) mRoomContainer.findViewById(R.id.SurfaceView);


        ivBig = (ImageView) mRoomContainer.findViewById(R.id.iv_big);
        tvBig = (TextView) mRoomContainer.findViewById(R.id.tv_big);
        ivSamll = (ImageView) mRoomContainer.findViewById(R.id.iv_samll);
        tvSamll = (TextView) mRoomContainer.findViewById(R.id.tv_samll);
        ivSinge = (ImageView) mRoomContainer.findViewById(R.id.iv_singe);
        tvSinge = (TextView) mRoomContainer.findViewById(R.id.tv_singe);
        ivDouble = (ImageView) mRoomContainer.findViewById(R.id.iv_double);
        tvDouble = (TextView) mRoomContainer.findViewById(R.id.tv_double);
        ivBigSigle = (ImageView) mRoomContainer.findViewById(R.id.iv_big_sigle);
        tvBigSigle = (TextView) mRoomContainer.findViewById(R.id.tv_big_sigle);
        ivSamllSinge = (ImageView) mRoomContainer.findViewById(R.id.iv_samll_singe);
        tvSamllSinge = (TextView) mRoomContainer.findViewById(R.id.tv_samll_singe);
        ivBigDouble = (ImageView) mRoomContainer.findViewById(R.id.iv_big_double);
        tvBigDouble = (TextView) mRoomContainer.findViewById(R.id.tv_big_double);
        ivSamllDouble = (ImageView) mRoomContainer.findViewById(R.id.iv_samll_double);
        tvSamllDouble = (TextView) mRoomContainer.findViewById(R.id.tv_samll_double);
        ivMoreBig = (ImageView) mRoomContainer.findViewById(R.id.iv_more_big);
        tvMoreBig = (TextView) mRoomContainer.findViewById(R.id.tv_more_big);
        ivMoreSamll = (ImageView) mRoomContainer.findViewById(R.id.iv_more_samll);
        tvMoreSamll = (TextView) mRoomContainer.findViewById(R.id.tv_more_samll);
        live_game = (AutoLinearLayout) mRoomContainer.findViewById(R.id.live_game);
        watch_room_message_fragment_chat = (FrameLayout) mRoomContainer.findViewById(R.id.watch_room_message_fragment_chat);

        tv_rule = (ImageView) mRoomContainer.findViewById(R.id.tv_rule);


        smallAdd = (ImageView) mRoomContainer.findViewById(R.id.small_add);
        samllNumber = (TextView) mRoomContainer.findViewById(R.id.samll_number);
        smallSubtract = (ImageView) mRoomContainer.findViewById(R.id.small_subtract);
        doubleSubtract = (ImageView) mRoomContainer.findViewById(R.id.double_subtract);
        doubleNumber = (TextView) mRoomContainer.findViewById(R.id.double_number);
        doubleAdd = (ImageView) mRoomContainer.findViewById(R.id.double_add);
        ivTouzhu = (ImageView) mRoomContainer.findViewById(R.id.iv_touzhu);
        iv_trend = (ImageView) mRoomContainer.findViewById(R.id.iv_trend);

        all_lepiao = (TextView) mRoomContainer.findViewById(R.id.all_lepiao);

        tv_periods = (TextView) mRoomContainer.findViewById(R.id.tv_periods);
        frist_num = (TextView) mRoomContainer.findViewById(R.id.frist_num);
        second_num = (TextView) mRoomContainer.findViewById(R.id.second_num);
        third_num = (TextView) mRoomContainer.findViewById(R.id.third_num);
        all_num = (TextView) mRoomContainer.findViewById(R.id.all_num);
        tv_ds = (TextView) mRoomContainer.findViewById(R.id.tv_ds);
        tv_game_next_open_time = (TimeCountDownLayout) mRoomContainer.findViewById(R.id.tv_game_next_open_time);
        rl_kp = (LinearLayout) mRoomContainer.findViewById(R.id.rl_kp);
        tv_hz = (TextView) mRoomContainer.findViewById(R.id.tv_hz);
        //斗牛部分
        rl_game_container = (RelativeLayout) mRoomContainer.findViewById(R.id.rl_game_container);
        iv_bullfight = (ImageView) mRoomContainer.findViewById(R.id.iv_bullfight);
        live_game_bullfight = (AutoLinearLayout) mRoomContainer.findViewById(R.id.live_game_bullfight);
        rl_game_info = (RelativeLayout) mRoomContainer.findViewById(R.id.rl_game_info);
        rl_bullfight_banker = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_bullfight_banker);
        iv_bullfight_banker_head = (CircleImageView) mRoomContainer.findViewById(R.id.iv_bullfight_banker_head);
        tv_bullfight_bankername = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_bankername);
        tv_bullfight_banker_money = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_banker_money);
        iv_poker_banker1 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_banker1);
        iv_poker_banker2 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_banker2);
        iv_poker_banker3 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_banker3);
        iv_poker_banker4 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_banker4);
        iv_poker_banker5 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_banker5);
        tv_bullfight_totlanum1 = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_totlanum1);
        tv_bullfight_minenum1 = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_minenum1);
        tv_bullfight_totlanum2 = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_totlanum2);
        tv_bullfight_minenum2 = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_minenum2);
        tv_bullfight_totlanum3 = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_totlanum3);
        tv_bullfight_minenum3 = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_minenum3);
        rl_bullfight_betting_container1 = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_bullfight_betting_container1);
        rl_bullfight_betting_container2 = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_bullfight_betting_container2);
        rl_bullfight_betting_container3 = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_bullfight_betting_container3);
        iv_poker_palyer11 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer11);
        iv_poker_palyer12 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer12);
        iv_poker_palyer13 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer13);
        iv_poker_palyer14 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer14);
        iv_poker_palyer15 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer15);
        iv_poker_palyer21 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer21);
        iv_poker_palyer22 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer22);
        iv_poker_palyer23 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer23);
        iv_poker_palyer24 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer24);
        iv_poker_palyer25 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer25);
        iv_poker_palyer31 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer31);
        iv_poker_palyer32 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer32);
        iv_poker_palyer33 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer33);
        iv_poker_palyer34 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer34);
        iv_poker_palyer35 = (ImageView) mRoomContainer.findViewById(R.id.iv_poker_palyer35);
        rl_bullfight1 = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_bullfight1);
        rl_bullfight2 = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_bullfight2);
        rl_bullfight3 = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_bullfight3);
        tv_timing = (TextView) mRoomContainer.findViewById(R.id.tv_timing);
        tv_bullfight_result1 = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_result1);
        tv_bullfight_result2 = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_result2);
        tv_bullfight_result3 = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_result3);
        ll_bullfight_result = (AutoLinearLayout) mRoomContainer.findViewById(R.id.ll_bullfight_result);
        rl_timing = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_timing);
        tv_bullfight_lepiao = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_lepiao);
        tv_bullfight_top_up = (TextView) mRoomContainer.findViewById(R.id.tv_bullfight_top_up);
        iv_10 = (ImageView) mRoomContainer.findViewById(R.id.iv_10);
        iv_50 = (ImageView) mRoomContainer.findViewById(R.id.iv_50);
        iv_100 = (ImageView) mRoomContainer.findViewById(R.id.iv_100);
        iv_1000 = (ImageView) mRoomContainer.findViewById(R.id.iv_1000);
        iv_10000 = (ImageView) mRoomContainer.findViewById(R.id.iv_10000);
        iv_bull_amount0 = (ImageView) mRoomContainer.findViewById(R.id.iv_bull_amount0);
        iv_bull_amount1 = (ImageView) mRoomContainer.findViewById(R.id.iv_bull_amount1);
        iv_bull_amount2 = (ImageView) mRoomContainer.findViewById(R.id.iv_bull_amount2);
        iv_bull_amount3 = (ImageView) mRoomContainer.findViewById(R.id.iv_bull_amount3);
        rl_poker_banker_container = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_poker_banker_container);
        rl_poker_player_container1 = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_poker_player_container1);
        rl_poker_player_container2 = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_poker_player_container2);
        rl_poker_player_container3 = (AutoRelativeLayout) mRoomContainer.findViewById(R.id.rl_poker_player_container3);

        List<LiveListBean> list = (List<LiveListBean>) getIntent().getSerializableExtra("LIVELIST");
        position = getIntent().getIntExtra("position", 0);

        if (list != null) {
            listBeenList.addAll(list);
            liveListBean = listBeenList.get(position);
        }

        mCurrentItem = position;

        anchorVideo = new AnchorVideo();
        fragmentManager = getSupportFragmentManager();

        //获取礼物列表
        getGiftList();

        initRecycle();

        // 注册监听
        registerObservers(true);
        //注册监听自定义通知
        registerReceiveCustom(true);
        //注册广播接收器
        registerReceiverWatchLiveActivity();

        //网易云信消息未读数
        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(10001);
            }
        }, 30000, 30000);

        mPagerAdapter = new PagerAdapter();
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
            }
        });

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                ViewGroup viewGroup = (ViewGroup) page;
                Log.e(TAG, "page.id == " + page.getId() + ", position == " + position);

                if ((position < 0 && viewGroup.getId() != mCurrentItem)) {
                    View roomContainer = viewGroup.findViewById(R.id.rl_live_root);
                    if (roomContainer != null && roomContainer.getParent() != null && roomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (roomContainer.getParent())).removeView(roomContainer);
                        LogUtil.e("切换直播间", "切换1");
                        ifEnterTwo(listBeenList, mCurrentItem);
//                        finish();
                    }

                }

                // 满足此种条件，表明需要加载直播视频，以及聊天室了
                if (viewGroup.getId() == mCurrentItem && position == 0 && mCurrentItem != mRoomId) {
                    if (mRoomContainer.getParent() != null && mRoomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (mRoomContainer.getParent())).removeView(mRoomContainer);
                        LogUtil.e("切换直播间", "切换2");
                        ifEnterTwo(listBeenList, mCurrentItem);
//                        finish();
                    }

                    if (isFirst) {
                        LogUtil.e("切换直播间", "切换3");
                        loadVideoAndChatRoom(viewGroup, mCurrentItem);
                    }

                }
            }
        });
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(position);
        initQuitDialog("确定离开");

        samllNumber.setText(String.valueOf(tzNumber));
        doubleNumber.setText(String.valueOf(jbNumber));
        initSelectStatus();
        initBlInfos();

        /**
         * 请求个人信息
         */
        initMyInfo();
    }

    private void initBlInfos() {
        String url = UrlBuilder.serverUrl + UrlBuilder.getBl;
        LogUtils.e("roomId::" + liveListBean.getRooms().getId() + "");
        OkHttpUtils.post().url(url).addParams("roomId", liveListBean.getRooms().getId() + "").addParams("type", "0").build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("response :" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("1")) {
                        String obj = jsonObject.getString("obj");
                        List<BlBean> blBeen = JsonUtil.json2BeanList(obj, BlBean.class);
                        BlBean blBean = blBeen.get(0);
                        tvBig.setText("1:" + blBean.getBig());
                        tvSamll.setText("1:" + blBean.getSmall());
                        tvSinge.setText("1:" + blBean.getSingle());
                        tvDouble.setText("1:" + blBean.getDoubles());
                        tvBigSigle.setText("1:" + blBean.getBigSingle());
                        tvSamllSinge.setText("1:" + blBean.getSmallSingle());
                        tvBigDouble.setText("1:" + blBean.getBigDouble());
                        tvSamllDouble.setText("1:" + blBean.getSmallDouble());
                        tvMoreBig.setText("1:" + blBean.getMoreBig());
                        tvMoreSamll.setText("1:" + blBean.getMoreSmall());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initSelectStatus() {
        restStatus();
        ivBig.setImageResource(R.mipmap.icon_big_select);
        selectStatus = "大";
        tv_hz.setText("和值大于13即中奖");

    }

    private void loadVideoAndChatRoom(ViewGroup viewGroup, int currentItem) {
        isFirst = false;
        viewGroup.addView(mRoomContainer);
        loadVideo(currentItem);
        mRoomId = currentItem;
    }

    private void loadVideo(int currentItem) {

        liveListBean = listBeenList.get(currentItem);

        liveHead.setAvatarUrl(liveListBean.getPicUrl());
        liveHeadImg.setAvatarUrl(liveListBean.getPicUrl());

        String picUrl = liveListBean.getPicUrl();
        if (TextUtils.isEmpty(picUrl)) {
            picUrl = UrlBuilder.HEAD_DEFAULT;
        }
        Picasso.with(mContext).load(picUrl).error(R.mipmap.zhan_live).into(rlLoading);
//        final String finalPicUrl = picUrl;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final Bitmap blurBitmap2 = FastBlurUtil.GetUrlBitmap(finalPicUrl, 4);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        rlLoading.setImageBitmap(blurBitmap2);
//                    }
//                });
//            }
//        }).start();

        if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(liveListBean.getPicUrl())) {
            ImageLoader.getInstance().loadImage(liveListBean.getPicUrl(), new
                    ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                        }

                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            if (bitmap != null) {
                                try {
                                    bitmap = FastBlur.doBlur(bitmap, 5, true);
                                    BitmapDrawable drawable = new BitmapDrawable(bitmap);
                                    rlLoading.setBackground(drawable);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                        }
                    });
        }
        if (liveListBean.getNickName().length() > 4) {
            liveName.setText(liveListBean.getNickName().substring(0, 4) + "...");
        } else {
            liveName.setText(liveListBean.getNickName());
        }

        liveId.setText("乐檬号:" + liveListBean.getId());

        //请求主播信息,得到主播乐票数量
        initUser();
        //请求主播信息并且附上金票显示隐藏关注按钮
        ifattention("请求主播信息", liveListBean.getId() + "", RequestCode.IF_ATTENTION);

        //进入房间
//        requestNet();
        //請求是否有被连麦人
        iSrequstLiveValid();
        roomId = liveListBean.getRooms().getRoomId() + "";

        // 登录聊天室
        enterRoom();

        if (!TextUtils.isEmpty(liveListBean.getRooms().getUserId() + "")) {
            SharedPreferenceUtils.put(this, "ZhuBoId", liveListBean.getRooms().getUserId() + "");
        } else {
            SharedPreferenceUtils.put(this, "isZhuBo", false);
        }

        mVideoPath = liveListBean.getRooms().getBroadcastUrl();

        LogUtils.e("拉流---" + mVideoPath);

        anchorVideo.initLive(mVideoPath, WatchLiveActivity.this, mSurfaceView, rlLoading);
        //隐藏视频地址
        flPull.setVisibility(View.GONE);
        flPlug.setVisibility(View.GONE);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);


        dialogForSelect = new Dialog(this, R.style.homedialog);

        join();
        startloading();

        if (liveListBean.getRooms().getRoomsType().equals("1")) {
            isPlayerRoom = true;
            getTimenumber();
            showPlayView(1);
        } else if ("2".equals(liveListBean.getRooms().getRoomsType())) {
            if (bullfightPresenter == null) {
                bullfightPresenter = new BullfightPresenter(this);
            }
            isWait = true;
            bullfightPresenter.getTimeAndNper(liveListBean.getRoomId() + "");
        } else {
            isPlayerRoom = false;
            if (gameType >= 1) {
                hidePlayView(gameType);
            }
        }
    }


    class PagerAdapter extends android.support.v4.view.PagerAdapter {

        @Override
        public int getCount() {
            return listBeenList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_room_item, null);
            view.setId(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewById(position));
        }
    }

    @Override
    protected void initListener() {
        iv_trend.setOnClickListener(this);
        tv_rule.setOnClickListener(this);
        iv_game.setOnClickListener(this);
        smallAdd.setOnClickListener(this);
        smallSubtract.setOnClickListener(this);
        doubleSubtract.setOnClickListener(this);
        doubleAdd.setOnClickListener(this);
        ivTouzhu.setOnClickListener(this);


        live_game.setOnClickListener(this);
        ivBig.setOnClickListener(this);
        ivSamll.setOnClickListener(this);
        ivSinge.setOnClickListener(this);
        ivDouble.setOnClickListener(this);
        ivBigSigle.setOnClickListener(this);
        ivSamllSinge.setOnClickListener(this);
        ivBigDouble.setOnClickListener(this);
        ivSamllDouble.setOnClickListener(this);
        ivMoreBig.setOnClickListener(this);
        ivMoreSamll.setOnClickListener(this);


        mSendGiftLian.setOnClickListener(this);
        liveHead.setOnClickListener(this);
        liveLianmaiHead.setOnClickListener(this);
        liveLianmaiHeadBg1.setOnClickListener(this);
        liveLianmaiHeadBg2.setOnClickListener(this);
        ivLivePrivatechat.setOnClickListener(this);
        ivLiveGift.setOnClickListener(this);
//        ivLiveShare.setOnClickListener(this);
        llTangpiao.setOnClickListener(this);
        liveClose.setOnClickListener(this);
        btnAttention.setOnClickListener(this);
        watchRoomJaizu.setOnClickListener(this);
        liveHeadImg.setOnClickListener(this);
        ruanjianpanW.setOnClickListener(this);
        zhoubangW.setOnClickListener(this);
        tv_lianmai.setOnClickListener(this);
        ll_buttom_mun.setOnClickListener(this);

        rl_bullfight1.setOnClickListener(this);
        rl_bullfight2.setOnClickListener(this);
        rl_bullfight3.setOnClickListener(this);
        tv_bullfight_top_up.setOnClickListener(this);
        iv_10.setOnClickListener(this);
        iv_50.setOnClickListener(this);
        iv_100.setOnClickListener(this);
        iv_1000.setOnClickListener(this);
        iv_10000.setOnClickListener(this);
        //点赞
        mRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hidePlayView(gameType);
                ll_buttom_mun.setVisibility(View.VISIBLE);
                if (sessionListFragment != null) {
                    sessionListFragment.hide();
                }
                if (liveMessageFragment != null)
                    liveMessageFragment.hide();
                if (messageFragment != null) {
                    messageFragment.hideEditText();
                }

                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        DownX = event.getX();//float DownX
                        DownY = event.getY();//float DownY

                        if (isfirstclick) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("red", 0.700000);
                            map.put("green", 0.400000);
                            map.put("blue", 0.100000);
                            map.put("number", 1 + "");
                            map.put("vip", appUser.getVip());
                            map.put("userId", appUser.getId());
                            map.put("level", appUser.getLevel());
                            SendRoomMessageUtils.onCustomMessageDianzan(SendRoomMessageUtils
                                    .MESSAGE_LIKE_LIGHT, messageFragment, liveListBean.getRooms()
                                    .getRoomId() + "", map);
                            isfirstclick = false;
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            map.put("red", 0.700000);
                            map.put("green", 0.400000);
                            map.put("blue", 0.100000);
                            map.put("number", 1 + "");
                            map.put("vip", appUser.getVip());
                            map.put("userId", appUser.getId());
                            map.put("level", appUser.getLevel());
                            SendRoomMessageUtils.onCustomMessageDianzan(SendRoomMessageUtils
                                    .MESSAGE_LIKE_CLICK, messageFragment, liveListBean.getRooms()
                                    .getRoomId() + "", map);
//                        showLit();
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                        float movepX = event.getX() - DownX;//X轴距离
                        float movepY = event.getY() - DownY;//y轴距离
                        Log.e("homepr", "ACTION_UP------" + "moveX = " + movepX + "moveY  = " + movepY);

                        /**
                         * 如果X轴移动绝对值大于Y轴移动绝对值
                         */
                        if (Math.abs(movepX) > Math.abs(movepY)) {
                            /**
                             * 左右清屏
                             */
                            if (movepX > 0 && ((Math.abs(movepX)) > 150) && !isHindRcView) {
                                //隐藏
                                scrl.beginScrollFromLeft();
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        rlHvView.setVisibility(View.INVISIBLE);
                                        isHindRcView = true;
                                    }
                                }, 300);
                            } else if (movepX < 0 && ((Math.abs(movepX)) > 150) && isHindRcView) {
                                //显示
                                rlHvView.setVisibility(View.VISIBLE);
                                scrl.beginScrollFromRight();
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        isHindRcView = false;
                                    }
                                }, 300);

                            }
                        }
                        break;
                }
                return true;

            }
        });

        //点击头像列表
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RoomUserBean bean = mDatas.get(position);
                ifattention("请求用户信息", bean.getUserId(), RequestCode.REQUEST_USER_INFO);
            }
        });

    }

    private void restStatus() {
        ivBig.setImageResource(R.mipmap.icon_big_unselect);
        ivSamll.setImageResource(R.mipmap.icon_small_unselect);
        ivSinge.setImageResource(R.mipmap.icon_single_unselect);
        ivDouble.setImageResource(R.mipmap.icon_double_unselect);
        ivBigSigle.setImageResource(R.mipmap.icon_big_single_unselect);
        ivSamllSinge.setImageResource(R.mipmap.icon_small_single_unselect);
        ivBigDouble.setImageResource(R.mipmap.icon_big_double_unselect);
        ivSamllDouble.setImageResource(R.mipmap.icon_small_double_unselect);
        ivMoreBig.setImageResource(R.mipmap.icon_big_more_unselect);
        ivMoreSamll.setImageResource(R.mipmap.icon_small_more_unselect);
    }

    private String selectStatus;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_trend: //走势
                showTrendPop();
                break;
            case R.id.tv_rule: //规则
                getRulePopup();
                break;

            case R.id.small_add:  //最小投注加
                if (tzNumber == 160) {  //达到最大的投注
                    return;
                }
                tzNumber = tzNumber * 2;
                samllNumber.setText(String.valueOf(tzNumber));
                break;
            case R.id.small_subtract:  //最小投注减
                String strNumber = samllNumber.getText().toString();
                if (Integer.valueOf(strNumber) == 10) {
                    return;
                } else {
                    tzNumber = Integer.valueOf(strNumber) / 2;
                    samllNumber.setText(String.valueOf(tzNumber));
                }

                break;
            case R.id.double_add:  //加倍投注加
                if (jbNumber == 1000) {
                    return;
                }
                jbNumber = jbNumber * 10;
                doubleNumber.setText(String.valueOf(jbNumber));
                break;
            case R.id.double_subtract: //加倍投注减
                String strdouNumber = doubleNumber.getText().toString();
                if (Integer.valueOf(strdouNumber) == 1) {
                    return;
                } else {
                    jbNumber = Integer.valueOf(strdouNumber) / 10;
                    doubleNumber.setText(String.valueOf(jbNumber));
                }
                break;
            case R.id.iv_touzhu:  //投注
                if (isTouZhu) {
                    showTouZhuPop(selectStatus, jbNumber, tzNumber);
                } else {
                    showToast("没有开奖信息,请稍候再试");
                }
                break;

            case R.id.iv_big: //大
                restStatus();
                ivBig.setImageResource(R.mipmap.icon_big_select);
                selectStatus = "大";
                tv_hz.setText("和值大于13即中奖");
                break;
            case R.id.iv_samll: //小
                restStatus();
                ivSamll.setImageResource(R.mipmap.icon_small_select);
                selectStatus = "小";
                tv_hz.setText("和值小于14即中奖");

                break;
            case R.id.iv_singe: //单
                restStatus();
                ivSinge.setImageResource(R.mipmap.icon_single_select);
                selectStatus = "单";
                tv_hz.setText("和值为奇数即中奖");

                break;
            case R.id.iv_double: //双
                restStatus();
                ivDouble.setImageResource(R.mipmap.icon_double_select);
                selectStatus = "双";
                tv_hz.setText("和值为偶数即中奖");

                break;
            case R.id.iv_big_sigle: //大单
                restStatus();
                ivBigSigle.setImageResource(R.mipmap.icon_big_single_select);
                selectStatus = "大单";
                tv_hz.setText("和值为15,17,19,21,23,25,27即中奖");

                break;
            case R.id.iv_samll_singe: //小单
                restStatus();
                ivSamllSinge.setImageResource(R.mipmap.icon_small_single_select);
                selectStatus = "小单";
                tv_hz.setText("和值为1,3,5,7,9,11,13即中奖即中奖");

                break;
            case R.id.iv_big_double: //大双
                restStatus();
                ivBigDouble.setImageResource(R.mipmap.icon_big_double_select);
                selectStatus = "大双";
                tv_hz.setText("和值为14,16,18,20,22,24,26即中奖");

                break;
            case R.id.iv_samll_double: //小双
                restStatus();
                ivSamllDouble.setImageResource(R.mipmap.icon_small_double_select);
                selectStatus = "小双";
                tv_hz.setText("和值为0,2,4,6,8,10,12即中奖");

                break;
            case R.id.iv_more_big: //更大
                restStatus();
                ivMoreBig.setImageResource(R.mipmap.icon_big_more_select);
                selectStatus = "极大";
                tv_hz.setText("和值大于21即中奖");

                break;
            case R.id.iv_more_samll: //更小
                restStatus();
                ivMoreSamll.setImageResource(R.mipmap.icon_small_more_select);
                selectStatus = "极小";
                tv_hz.setText("和值小于6即中奖");
                break;

            case R.id.ruanjianpanW:
                hidePlayView(gameType);
                messageFragment.inputTypeOnClick();
                break;
            case R.id.ll_buttom_mun://游戏
                if (isPlayerRoom) {
                    if (rl_game_container.getVisibility() == View.VISIBLE) {
                        hidePlayView(gameType);
                    } else {
                        showPlayView(gameType);
                    }

                } else {
                    showToast("主播未开启游戏");
                }
                break;
            case R.id.iv_game:
                if (isPlayerRoom) {
                    if (rl_game_container.getVisibility() == View.VISIBLE) {
                        hidePlayView(gameType);
                    } else {
                        showPlayView(gameType);
                    }

                } else {
                    showToast("主播未开启游戏");
                }
                break;

            case R.id.zhoubangW:
                messageFragment.ivRankingOnClick();
                break;
            /**
             * 关闭推流小窗口
             */
            case R.id.tv_lianmai://结束连麦按钮
                Map<String, Object> map = new HashMap<>();
                map.put("watch_private_flag", "0");
                map.put("vip", "0");
                SendRoomMessageUtils.onCustomMessageLianmai(SendRoomMessageUtils.MESSAGE_WATCHER_DISCONNECT,
                        messageFragment, liveListBean.getRooms().getRoomId() + "", map);
                roomLiveExit();
                break;
            case R.id.live_close:
                //退出登录
                if (mQuitDialog != null && !mQuitDialog.isShowing()) {
                    mQuitDialog.show();
                }
                break;
            //私信
            case R.id.iv_live_privatechat:
//                watch_room_message_fragment_chat.setVisibility(View.VISIBLE);
                sessionListFragment = new ChatRoomSessionListFragment();
                sessionListFragment.init(getSupportFragmentManager());
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.watch_room_message_fragment_chat, sessionListFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            //礼物
            case R.id.iv_live_gift:
                getGiftPopup();
                break;
//            //分享
//            case R.id.iv_live_share:
//                UMUtils.umShare(this, liveListBean.getNickName(), liveListBean.getLivePicUrl(),
//                        share_url + "?userId=" + liveListBean.getId());
//                break;
            //跳转到排行榜
            case R.id.ll_tp_labe:
                Intent intent = new Intent(this, ContributionActivity.class);
                startActivity(intent);
                break;
            //请求主播信息
            case R.id.live_head:
                ifattention("请求主播信息", liveListBean.getId() + "", RequestCode.REQUEST_USER_INFO);
                break;
            //请求主播信息
            case R.id.live_lianmai_head_bg1:
                ifattention("请求主播信息", liveListBean.getId() + "", RequestCode.REQUEST_USER_INFO);
                break;
            case R.id.live_lianmai_head://连麦人的头像
                if (customLianmaiBean == null) {
                    ifattention("请求用户信息", appUser.getId(), RequestCode.REQUEST_USER_INFO);
                } else {
                    ifattention("请求用户信息", customLianmaiBean.getUserId(), RequestCode.REQUEST_USER_INFO);
                }

                break;
            case R.id.live_lianmai_head_bg2://连麦人的头像
                if (customLianmaiBean == null) {
                    ifattention("请求用户信息", appUser.getId(), RequestCode.REQUEST_USER_INFO);
                } else {
                    ifattention("请求用户信息", customLianmaiBean.getUserId(), RequestCode.REQUEST_USER_INFO);
                }

                break;
            //关注主播
            case R.id.btn_attention:
                ConcurrentHashMap<String, String> map1 = new ConcurrentHashMap<>();
                map1.put("followUserId", liveListBean.getId() + "");
                map1.put("userId", appUser.getId());
                httpDatas.getDataForJsoNoloading("关注用户", Request.Method.POST, UrlBuilder
                        .ATTENTION_USER, map1, myHandler, RequestCode.ATTENTION_USER);
                break;
            //家族
            case R.id.watch_room_jiaZu:
                getFamilyMember();
                break;
            //守护
            case R.id.live_head_img:
                showDialogForCallOther();
                break;
            case R.id.iv_show_send_gift_lian:
                SEND_NUM++;
                onClickSendGift();
                break;
            case R.id.rl_bullfight1:
                betBullfight(1);
                break;
            case R.id.rl_bullfight2:
                betBullfight(2);
                break;
            case R.id.rl_bullfight3:
                betBullfight(3);
                break;
            case R.id.tv_bullfight_top_up:
                break;
            case R.id.iv_10:
                checkBettingBalance(10);
                break;
            case R.id.iv_50:
                checkBettingBalance(50);
                break;
            case R.id.iv_100:
                checkBettingBalance(100);
                break;
            case R.id.iv_1000:
                checkBettingBalance(1000);
                break;
            case R.id.iv_10000:
                checkBettingBalance(10000);
                break;
        }
    }


    //---------------------------斗牛代码---------------------------


    /**
     * 投注斗牛
     *
     * @param position 位置（1，2，3）
     */
    private void betBullfight(int position) {
        betPosition = position;
        if (betBalance > 0) {
            if (betBalance > myGoldCoin) {
                ToastUtils.showMessageDefault(WatchLiveActivity.this,getResources().getString(R.string.balance_not_enough));
            } else {
                bullfightPresenter.betSuccess(Integer.parseInt(appUser.getId()), liveListBean.getRoomId(), betBalance, position, uper, 0);
            }
        } else {
            ToastUtils.showMessageDefault(WatchLiveActivity.this,getResources().getString(R.string.no_select_betbalance));
        }
    }

    /**
     * 添加投注池视图的显示
     *
     * @param position(1,2,3)
     * @param isAnimation     是否需要显示添加动画
     */
    private void addBettingView(int position, int betSum, boolean isAnimation) {
        final ImageView imageView = new ImageView(this);
        RelativeLayout bettingPoolView = null;
        switch (betSum) {
            case 10:
                imageView.setImageResource(R.mipmap.ic_bullfight_10_light);
                break;
            case 50:
                imageView.setImageResource(R.mipmap.ic_bullfight_50_light);
                break;
            case 100:
                imageView.setImageResource(R.mipmap.ic_bullfight_100_light);
                break;
            case 1000:
                imageView.setImageResource(R.mipmap.ic_bullfight_1000_light);
                break;
            case 10000:
                imageView.setImageResource(R.mipmap.ic_bullfight_10000_light);
                break;
        }
        switch (position) {
            case 1:
                bettingPoolView = rl_bullfight_betting_container1;
                int total1 = Integer.parseInt(tv_bullfight_totlanum1.getText().toString()) + betSum;
                tv_bullfight_totlanum1.setText(total1 + "");
                break;
            case 2:
                bettingPoolView = rl_bullfight_betting_container2;
                int total2 = Integer.parseInt(tv_bullfight_totlanum2.getText().toString()) + betSum;
                tv_bullfight_totlanum2.setText(total2 + "");
                break;
            case 3:
                bettingPoolView = rl_bullfight_betting_container3;
                int total3 = Integer.parseInt(tv_bullfight_totlanum3.getText().toString()) + betSum;
                tv_bullfight_totlanum3.setText(total3 + "");
                break;
        }
        RelativeLayout.LayoutParams layoutParams = new AutoRelativeLayout.LayoutParams(iv_100.getWidth(), iv_100.getHeight());
        layoutParams.leftMargin = (int) (Math.random() * (rl_bullfight_betting_container1.getWidth() - layoutParams.width));
        layoutParams.topMargin = (int) (Math.random() * (rl_bullfight_betting_container1.getHeight() - layoutParams.height));
        if (bettingPoolView.getChildCount() >= BETTING_POOL_VIEWS_CAPACITY) {
            bettingPoolView.removeViewAt(0);
        }
//        if(isAnimation) {
//            imageView.setVisibility(View.GONE);
//        }else {
//            imageView.setVisibility(View.VISIBLE);
//        }
        bettingPoolView.addView(imageView, layoutParams);
        if (isAnimation) {
//            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
//            valueAnimator.setDuration(1000);
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                }
//            });
        }
    }

    /**
     * 选择投注金额
     */
    private void checkBettingBalance(int betSum) {
        betBalance = betSum;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(800);
        View targetView = null;
        switch (betSum) {
            case 10:
                targetView = iv_10;
                break;
            case 50:
                targetView = iv_50;
                break;
            case 100:
                targetView = iv_100;
                break;
            case 1000:
                targetView = iv_1000;
                break;
            case 10000:
                targetView = iv_10000;
                break;
        }
        valueAnimator.setTarget(targetView);
        final View finalTargetView = targetView;
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                finalTargetView.setAlpha(1);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                finalTargetView.setAlpha(1 - (Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }


    /**
     * 开启斗牛游戏
     */
    private void startBullfightGame() {
    }

    /**
     * 斗牛倒计时
     */
    private void bullfightTimer() {
        Log.e("TAG",nextTime+"");
        if (nextTime >= 10) {
            switchTimer();
        }
        nextTime--;
        myHandler.sendEmptyMessageDelayed(BULLFIGHT_TIME, 1000);
        switch (nextTime) {
            case 10://获取扑克牌结果
                setBetPoolEnable(false);
                rl_timing.setVisibility(View.GONE);
                bullfightPresenter.getPokerResult(liveListBean.getRoomId() + "");
                break;
            case 5://开奖
                bullfightPresenter.getGameResult(liveListBean.getRoomId() + "", uper + "", appUser.getId());
                bullfightPresenter.updateBankerBalance();
                break;
            case 1://主播更新倒计时
//                bullfightPresenter.initGameTimer(liveListBean.getRoomId() + "", uper + "", appUser.getId() + "");
                break;
            case 0://再次请求下一局倒计时
                myHandler.removeMessages(BULLFIGHT_TIME);
                bullfightPresenter.getTimeAndNper(liveListBean.getRoomId() + "");
                break;
        }
    }

    /**
     * 设置点击投注区域的可用性
     */
    private void setBetPoolEnable(boolean enable) {
        if (enable) {
            rl_bullfight1.setEnabled(true);
            rl_bullfight2.setEnabled(true);
            rl_bullfight3.setEnabled(true);
        } else {
            rl_bullfight1.setEnabled(false);
            rl_bullfight2.setEnabled(false);
            rl_bullfight3.setEnabled(false);
        }
    }

    /**
     * 斗牛结果显示 参数为空不显示
     *
     * @param arg1 第一条
     * @param arg2 第二天
     * @param arg3 第三条
     */
    private void bullfightResultShow(String arg1, String arg2, String arg3) {
        ll_bullfight_result.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(arg1)) {
            tv_bullfight_result1.setVisibility(View.INVISIBLE);
        } else {
            tv_bullfight_result1.setVisibility(View.VISIBLE);
            tv_bullfight_result1.setText(arg1);
        }
        if (TextUtils.isEmpty(arg2)) {
            tv_bullfight_result2.setVisibility(View.INVISIBLE);
        } else {
            tv_bullfight_result2.setVisibility(View.VISIBLE);
            tv_bullfight_result2.setText(arg2);
        }
        if (TextUtils.isEmpty(arg3)) {
            tv_bullfight_result3.setVisibility(View.INVISIBLE);
        } else {
            tv_bullfight_result3.setVisibility(View.VISIBLE);
            tv_bullfight_result3.setText(arg3);
        }
    }

    /**
     * 倒计时显示
     * param isShow
     */
    private void switchTimer() {
        int time = nextTime - 10;
        tv_timing.setText(time + "S");
    }

    @Override
    public void getGameResult(GameResult gameResult) {
        if (gameResult.isSuccess()) {
            switch (gameResult.getCode()) {
                case 0://没用中奖
                    bullfightResultShow(null, "未中奖", null);
                    break;
                case 1://中奖
                    bullfightResultShow(getResources().getString(R.string.the_result), getResources().getString(R.string.the_user)
                            +gameResult.getObj().getMount(),null);
                    myGoldCoin += gameResult.getObj().getAmount();
                    tv_bullfight_lepiao.setText(CountUtils.getCount(myGoldCoin));
                    break;
                case 2://异常

                    break;
            }
        }
    }

    @Override
    public void startBullGame(StartResult result) {
    }

    @Override
    public void getBankerInfo(BankerInfo bankerInfo) {
        if (bankerInfo.isSuccess()) {
            tv_bullfight_bankername.setText(bankerInfo.getObj().getNickName());
            String count = CountUtils.getCount(bankerInfo.getObj().getGoldCoin());
            tv_bullfight_banker_money.setText(count);
            Glide.with(this).load(bankerInfo.getObj().getLivePicUrl()).into(iv_bullfight_banker_head);
        }
    }

    @Override
    public void getTimeAndNPer(TimeAndNper timeAndNper) {
        if (timeAndNper.isSuccess()) {
            setBetPoolEnable(true);
            ll_bullfight_result.setVisibility(View.GONE);
            rl_timing.setVisibility(View.VISIBLE);
            rl_bullfight_betting_container1.removeAllViews();
            rl_bullfight_betting_container2.removeAllViews();
            rl_bullfight_betting_container3.removeAllViews();
            tv_bullfight_totlanum1.setText("0");
            tv_bullfight_totlanum2.setText("0");
            tv_bullfight_totlanum3.setText("0");
            tv_bullfight_minenum1.setText("0");
            tv_bullfight_minenum2.setText("0");
            tv_bullfight_minenum3.setText("0");
            if (myGoldCoin == null) {
                myHandler.sendEmptyMessageDelayed(WAIT_NEXT_START, 1000);
                return;
            }
            tv_bullfight_lepiao.setText(CountUtils.getCount(myGoldCoin));
            nextTime = timeAndNper.getObj().getTime() + 1;
            nextTime = nextTime > 30 ? 30 : nextTime;
            uper = timeAndNper.getObj().getPerid();
            showPlayView(2);
            updateBettingEnable(myGoldCoin);
            switchBullNum(false,-1);
            isPlayerRoom = true;
            if (isWait) {
                isWait = false;
                setBetPoolEnable(false);
                bullfightResultShow(null, getResources().getString(R.string.wait_next_start), null);
                myHandler.sendEmptyMessageDelayed(WAIT_NEXT_START, nextTime * 1000);
                return;
            }
            myHandler.sendEmptyMessage(BULLFIGHT_TIME);
            switchAllPoker(false,-1);
            sendPokerAnimator();
            bullfightPresenter.getBankerInfo();
        } else {
        }
    }

    @Override
    public void betSuccess(BetResult betResult, int amount, int type) {
        int code = betResult.getCode();
        switch (code) {
            case 0://为异常
                ToastUtils.showMessageDefault(WatchLiveActivity.this,getResources().getString(R.string.bet_fail));
                break;
            case 1://为成功
                myGoldCoin -= amount;
                tv_bullfight_lepiao.setText(CountUtils.getCount(myGoldCoin));
                updateBettingEnable(myGoldCoin);
                addBettingView(type, amount, true);
                switch (type) {
                    case 1:
                        int amount1 = Integer.parseInt(tv_bullfight_minenum1.getText().toString()) + amount;
                        tv_bullfight_minenum1.setText(amount1 + "");
                        break;
                    case 2:
                        int amount2 = Integer.parseInt(tv_bullfight_minenum2.getText().toString()) + amount;
                        tv_bullfight_minenum2.setText(amount2 + "");
                        break;
                    case 3:
                        int amount3 = Integer.parseInt(tv_bullfight_minenum3.getText().toString()) + amount;
                        tv_bullfight_minenum3.setText(amount3 + "");
                        break;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("vip", appUser.getVip());
                map.put("userId", appUser.getId());
                map.put("level", appUser.getLevel());
                switch (type) {
                    case 1:
                        map.put("NIM_TOUZHU_GOLD_SELECT_1", amount+"");
                        break;
                    case 2:
                        map.put("NIM_TOUZHU_GOLD_SELECT_2", amount+"");
                        break;
                    case 3:
                        map.put("NIM_TOUZHU_GOLD_SELECT_3", amount+"");
                        break;
                }
                SendRoomMessageUtils.onCustomMessagePlay("3030", messageFragment, roomId, map);
                break;
            case 2://为钱币不够赔
                break;
            case 3://余额不足
                ToastUtils.showMessageDefault(WatchLiveActivity.this,getResources().getString(R.string.balance_not_enough));
                break;
        }
    }

    @Override
    public void updataBankerBalance(BankerBalance BankerBalance) {
        int balance = BankerBalance.getObj();
        String count = CountUtils.getCount(balance);
        tv_bullfight_banker_money.setText(count);
    }

    @Override
    public void getPokerResult(PokerResult pokerResult) {
        if (pokerResult.isSuccess()) {
            final PokerResult.ObjBean.PlayerPokerMapBean playerPokerMap = pokerResult.getObj().getPlayerPokerMap();
            final ObjectAnimator animator3 = ObjectAnimator.ofFloat(rl_poker_player_container3, "scaleX", 0f, 1f);
            animator3.setDuration(1000);
            animator3.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    List<PokerResult.ObjBean.PlayerPokerMapBean.PokersBean> pokers3 = playerPokerMap.getPoker3().getPokers();
                    iv_poker_palyer31.setImageResource(getPokerId(pokers3.get(0).getColor(), pokers3.get(0).getValue()));
                    iv_poker_palyer32.setImageResource(getPokerId(pokers3.get(1).getColor(), pokers3.get(1).getValue()));
                    iv_poker_palyer33.setImageResource(getPokerId(pokers3.get(2).getColor(), pokers3.get(2).getValue()));
                    iv_poker_palyer34.setImageResource(getPokerId(pokers3.get(3).getColor(), pokers3.get(3).getValue()));
                    iv_poker_palyer35.setImageResource(getPokerId(pokers3.get(4).getColor(), pokers3.get(4).getValue()));
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    iv_bull_amount3.setImageResource(getBullSumId(playerPokerMap.getPoker3().getResult()));
                    switchBullNum(true,3);
                    rl_poker_banker_container.clearAnimation();
                    rl_poker_player_container1.clearAnimation();
                    rl_poker_player_container2.clearAnimation();
                    rl_poker_player_container3.clearAnimation();
                }
            });
            final ObjectAnimator animator2 = ObjectAnimator.ofFloat(rl_poker_player_container2, "scaleX", 0f, 1f);
            animator2.setDuration(1000);
            animator2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    List<PokerResult.ObjBean.PlayerPokerMapBean.PokersBean> pokers2 = playerPokerMap.getPoker2().getPokers();
                    iv_poker_palyer21.setImageResource(getPokerId(pokers2.get(0).getColor(), pokers2.get(0).getValue()));
                    iv_poker_palyer22.setImageResource(getPokerId(pokers2.get(1).getColor(), pokers2.get(1).getValue()));
                    iv_poker_palyer23.setImageResource(getPokerId(pokers2.get(2).getColor(), pokers2.get(2).getValue()));
                    iv_poker_palyer24.setImageResource(getPokerId(pokers2.get(3).getColor(), pokers2.get(3).getValue()));
                    iv_poker_palyer25.setImageResource(getPokerId(pokers2.get(4).getColor(), pokers2.get(4).getValue()));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator3.start();
                    iv_bull_amount2.setImageResource(getBullSumId(playerPokerMap.getPoker2().getResult()));
                    switchBullNum(true,2);
                }
            });
            final ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_poker_player_container1, "scaleX", 0f, 1f);
            animator1.setDuration(1000);
            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    List<PokerResult.ObjBean.PlayerPokerMapBean.PokersBean> pokers1 = playerPokerMap.getPoker1().getPokers();
                    iv_poker_palyer11.setImageResource(getPokerId(pokers1.get(0).getColor(), pokers1.get(0).getValue()));
                    iv_poker_palyer12.setImageResource(getPokerId(pokers1.get(1).getColor(), pokers1.get(1).getValue()));
                    iv_poker_palyer13.setImageResource(getPokerId(pokers1.get(2).getColor(), pokers1.get(2).getValue()));
                    iv_poker_palyer14.setImageResource(getPokerId(pokers1.get(3).getColor(), pokers1.get(3).getValue()));
                    iv_poker_palyer15.setImageResource(getPokerId(pokers1.get(4).getColor(), pokers1.get(4).getValue()));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator2.start();
                    iv_bull_amount1.setImageResource(getBullSumId(playerPokerMap.getPoker1().getResult()));
                    switchBullNum(true,1);
                }
            });
            final ObjectAnimator animator = ObjectAnimator.ofFloat(rl_poker_banker_container, "scaleX", 0f, 1f);
            animator.setDuration(1000);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    List<PokerResult.ObjBean.PlayerPokerMapBean.PokersBean> pokers = playerPokerMap.getPoker0().getPokers();
                    iv_poker_banker1.setImageResource(getPokerId(pokers.get(0).getColor(), pokers.get(0).getValue()));
                    iv_poker_banker2.setImageResource(getPokerId(pokers.get(1).getColor(), pokers.get(1).getValue()));
                    iv_poker_banker3.setImageResource(getPokerId(pokers.get(2).getColor(), pokers.get(2).getValue()));
                    iv_poker_banker4.setImageResource(getPokerId(pokers.get(3).getColor(), pokers.get(3).getValue()));
                    iv_poker_banker5.setImageResource(getPokerId(pokers.get(4).getColor(), pokers.get(4).getValue()));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator1.start();
                    iv_bull_amount0.setImageResource(getBullSumId(playerPokerMap.getPoker0().getResult()));
                    switchBullNum(true,0);
                }
            });
            animator.start();
        }
    }

    /**
     * 获取牛几的id
     *
     * @return
     */
    private int getBullSumId(int sum) {
        switch (sum) {
            case 0:
                return R.mipmap.bull0;
            case 1:
                return R.mipmap.bull1;
            case 2:
                return R.mipmap.bull2;
            case 3:
                return R.mipmap.bull3;
            case 4:
                return R.mipmap.bull4;
            case 5:
                return R.mipmap.bull5;
            case 6:
                return R.mipmap.bull6;
            case 7:
                return R.mipmap.bull7;
            case 8:
                return R.mipmap.bull8;
            case 9:
                return R.mipmap.bull9;
            case 10:
                return R.mipmap.bullbull;
        }
        return 0;
    }

    /**
     * 根据颜色和数值获取图片id
     *
     * @return
     */
    private int getPokerId(int color, int value) {
        switch (color) {
            case 1:
                switch (value) {
                    case 1:
                        return R.mipmap.poker_1_1;
                    case 2:
                        return R.mipmap.poker_1_2;
                    case 3:
                        return R.mipmap.poker_1_3;
                    case 4:
                        return R.mipmap.poker_1_4;
                    case 5:
                        return R.mipmap.poker_1_5;
                    case 6:
                        return R.mipmap.poker_1_6;
                    case 7:
                        return R.mipmap.poker_1_7;
                    case 8:
                        return R.mipmap.poker_1_8;
                    case 9:
                        return R.mipmap.poker_1_9;
                    case 10:
                        return R.mipmap.poker_1_10;
                    case 11:
                        return R.mipmap.poker_1_11;
                    case 12:
                        return R.mipmap.poker_1_12;
                    case 13:
                        return R.mipmap.poker_1_13;
                }
            case 2:
                switch (value) {
                    case 1:
                        return R.mipmap.poker_2_1;
                    case 2:
                        return R.mipmap.poker_2_2;
                    case 3:
                        return R.mipmap.poker_2_3;
                    case 4:
                        return R.mipmap.poker_2_4;
                    case 5:
                        return R.mipmap.poker_2_5;
                    case 6:
                        return R.mipmap.poker_2_6;
                    case 7:
                        return R.mipmap.poker_2_7;
                    case 8:
                        return R.mipmap.poker_2_8;
                    case 9:
                        return R.mipmap.poker_2_9;
                    case 10:
                        return R.mipmap.poker_2_10;
                    case 11:
                        return R.mipmap.poker_2_11;
                    case 12:
                        return R.mipmap.poker_2_12;
                    case 13:
                        return R.mipmap.poker_2_13;
                }
            case 3:
                switch (value) {
                    case 1:
                        return R.mipmap.poker_3_1;
                    case 2:
                        return R.mipmap.poker_3_2;
                    case 3:
                        return R.mipmap.poker_3_3;
                    case 4:
                        return R.mipmap.poker_3_4;
                    case 5:
                        return R.mipmap.poker_3_5;
                    case 6:
                        return R.mipmap.poker_3_6;
                    case 7:
                        return R.mipmap.poker_3_7;
                    case 8:
                        return R.mipmap.poker_3_8;
                    case 9:
                        return R.mipmap.poker_3_9;
                    case 10:
                        return R.mipmap.poker_3_10;
                    case 11:
                        return R.mipmap.poker_3_11;
                    case 12:
                        return R.mipmap.poker_3_12;
                    case 13:
                        return R.mipmap.poker_3_13;
                }
            case 4:
                switch (value) {
                    case 1:
                        return R.mipmap.poker_4_1;
                    case 2:
                        return R.mipmap.poker_4_2;
                    case 3:
                        return R.mipmap.poker_4_3;
                    case 4:
                        return R.mipmap.poker_4_4;
                    case 5:
                        return R.mipmap.poker_4_5;
                    case 6:
                        return R.mipmap.poker_4_6;
                    case 7:
                        return R.mipmap.poker_4_7;
                    case 8:
                        return R.mipmap.poker_4_8;
                    case 9:
                        return R.mipmap.poker_4_9;
                    case 10:
                        return R.mipmap.poker_4_10;
                    case 11:
                        return R.mipmap.poker_4_11;
                    case 12:
                        return R.mipmap.poker_4_12;
                    case 13:
                        return R.mipmap.poker_4_13;
                }
        }
        return 0;
    }

    /**
     * 更新投注金额的选择按钮
     */
    private void updateBettingEnable(long balance) {
        if (balance >= 10000) {
            iv_10.setEnabled(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setEnabled(true);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_light);
            iv_100.setEnabled(true);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_light);
            iv_1000.setEnabled(true);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_light);
            iv_10000.setEnabled(true);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_light);
        } else if (balance >= 1000) {
            iv_10.setEnabled(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setEnabled(true);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_light);
            iv_100.setEnabled(true);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_light);
            iv_1000.setEnabled(true);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_light);
            iv_10000.setEnabled(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        } else if (balance >= 100) {
            iv_10.setEnabled(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setEnabled(true);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_light);
            iv_100.setEnabled(true);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_light);
            iv_1000.setEnabled(false);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_dark);
            iv_10000.setEnabled(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        } else if (balance >= 50) {
            iv_10.setEnabled(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setEnabled(true);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_light);
            iv_100.setEnabled(false);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_dark);
            iv_1000.setEnabled(false);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_dark);
            iv_10000.setEnabled(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        } else if (balance >= 10) {
            iv_10.setEnabled(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setEnabled(false);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_dark);
            iv_100.setEnabled(false);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_dark);
            iv_1000.setEnabled(false);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_dark);
            iv_10000.setEnabled(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        } else {
            iv_10.setEnabled(false);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_dark);
            iv_50.setEnabled(false);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_dark);
            iv_100.setEnabled(false);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_dark);
            iv_1000.setEnabled(false);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_dark);
            iv_10000.setEnabled(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        }
    }

    /**
     * 显示扑克牌
     *
     * @param x （0，庄家；1，天；2，地；3，人）
     * @param y （第几张poker牌）
     */
    private void showPoker(int x, int y) {

    }

    /**
     * 发牌动画
     */
    private void sendPokerAnimator() {
        final ObjectAnimator animator3 = ObjectAnimator.ofFloat(rl_poker_player_container3, "scaleX", 0f, 1f);
        animator3.setDuration(1000);
        animator3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                switchAllPoker(true,3);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rl_poker_banker_container.clearAnimation();
                rl_poker_player_container1.clearAnimation();
                rl_poker_player_container2.clearAnimation();
                rl_poker_player_container3.clearAnimation();
            }
        });
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(rl_poker_player_container2, "scaleX", 0f, 1f);
        animator2.setDuration(1000);
        animator2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                switchAllPoker(true,2);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animator3.start();
            }
        });
        final ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_poker_player_container1, "scaleX", 0f, 1f);
        animator1.setDuration(1000);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                switchAllPoker(true,1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animator2.start();
            }
        });
        final ObjectAnimator animator = ObjectAnimator.ofFloat(rl_poker_banker_container, "scaleX", 0f, 1f);
        animator.setDuration(1000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                switchAllPoker(true,0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animator1.start();
            }
        });
        animator.start();
    }

    /**
     * 初始化扑克牌（显示背面）
     */
    private void initPokerImg() {
        iv_poker_banker1.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_banker2.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_banker3.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_banker4.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_banker5.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer11.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer12.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer13.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer14.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer15.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer21.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer22.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer23.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer24.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer25.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer31.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer32.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer33.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer34.setImageResource(R.mipmap.bullfight_poker_nagetive);
        iv_poker_palyer35.setImageResource(R.mipmap.bullfight_poker_nagetive);
    }

    /**
     * 牛数显示隐藏
     *
     * @param isShow
     * @param position -1一起显示
     */
    private void switchBullNum(boolean isShow,int position) {
        if (isShow) {
            switch (position){
                case -1:
                    iv_bull_amount0.setVisibility(View.VISIBLE);
                    iv_bull_amount1.setVisibility(View.VISIBLE);
                    iv_bull_amount2.setVisibility(View.VISIBLE);
                    iv_bull_amount3.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    iv_bull_amount0.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    iv_bull_amount1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    iv_bull_amount2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    iv_bull_amount3.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            iv_bull_amount0.setVisibility(View.GONE);
            iv_bull_amount1.setVisibility(View.GONE);
            iv_bull_amount2.setVisibility(View.GONE);
            iv_bull_amount3.setVisibility(View.GONE);
        }
    }

    /**
     * 所有扑克牌显示隐藏
     * @param isShow 是否显示
     * @param position 再显示的基础上显示哪个区域（-1一起显示，0显示庄家，1显示天，2显示地，3显示人）
     */
    private void switchAllPoker(boolean isShow,int position) {
        if (isShow) {
            switch (position){
                case -1:
                    iv_poker_banker1.setVisibility(View.VISIBLE);
                    iv_poker_banker2.setVisibility(View.VISIBLE);
                    iv_poker_banker3.setVisibility(View.VISIBLE);
                    iv_poker_banker4.setVisibility(View.VISIBLE);
                    iv_poker_banker5.setVisibility(View.VISIBLE);
                    iv_poker_palyer11.setVisibility(View.VISIBLE);
                    iv_poker_palyer12.setVisibility(View.VISIBLE);
                    iv_poker_palyer13.setVisibility(View.VISIBLE);
                    iv_poker_palyer14.setVisibility(View.VISIBLE);
                    iv_poker_palyer15.setVisibility(View.VISIBLE);
                    iv_poker_palyer21.setVisibility(View.VISIBLE);
                    iv_poker_palyer22.setVisibility(View.VISIBLE);
                    iv_poker_palyer23.setVisibility(View.VISIBLE);
                    iv_poker_palyer24.setVisibility(View.VISIBLE);
                    iv_poker_palyer25.setVisibility(View.VISIBLE);
                    iv_poker_palyer31.setVisibility(View.VISIBLE);
                    iv_poker_palyer32.setVisibility(View.VISIBLE);
                    iv_poker_palyer33.setVisibility(View.VISIBLE);
                    iv_poker_palyer34.setVisibility(View.VISIBLE);
                    iv_poker_palyer35.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    iv_poker_banker1.setVisibility(View.VISIBLE);
                    iv_poker_banker2.setVisibility(View.VISIBLE);
                    iv_poker_banker3.setVisibility(View.VISIBLE);
                    iv_poker_banker4.setVisibility(View.VISIBLE);
                    iv_poker_banker5.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    iv_poker_palyer11.setVisibility(View.VISIBLE);
                    iv_poker_palyer12.setVisibility(View.VISIBLE);
                    iv_poker_palyer13.setVisibility(View.VISIBLE);
                    iv_poker_palyer14.setVisibility(View.VISIBLE);
                    iv_poker_palyer15.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    iv_poker_palyer21.setVisibility(View.VISIBLE);
                    iv_poker_palyer22.setVisibility(View.VISIBLE);
                    iv_poker_palyer23.setVisibility(View.VISIBLE);
                    iv_poker_palyer24.setVisibility(View.VISIBLE);
                    iv_poker_palyer25.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    iv_poker_palyer31.setVisibility(View.VISIBLE);
                    iv_poker_palyer32.setVisibility(View.VISIBLE);
                    iv_poker_palyer33.setVisibility(View.VISIBLE);
                    iv_poker_palyer34.setVisibility(View.VISIBLE);
                    iv_poker_palyer35.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            iv_poker_banker1.setVisibility(View.GONE);
            iv_poker_banker1.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_banker2.setVisibility(View.GONE);
            iv_poker_banker2.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_banker3.setVisibility(View.GONE);
            iv_poker_banker3.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_banker4.setVisibility(View.GONE);
            iv_poker_banker4.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_banker5.setVisibility(View.GONE);
            iv_poker_banker5.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer11.setVisibility(View.GONE);
            iv_poker_palyer11.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer12.setVisibility(View.GONE);
            iv_poker_palyer12.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer13.setVisibility(View.GONE);
            iv_poker_palyer13.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer14.setVisibility(View.GONE);
            iv_poker_palyer14.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer15.setVisibility(View.GONE);
            iv_poker_palyer15.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer21.setVisibility(View.GONE);
            iv_poker_palyer21.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer22.setVisibility(View.GONE);
            iv_poker_palyer22.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer23.setVisibility(View.GONE);
            iv_poker_palyer23.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer24.setVisibility(View.GONE);
            iv_poker_palyer24.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer25.setVisibility(View.GONE);
            iv_poker_palyer25.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer31.setVisibility(View.GONE);
            iv_poker_palyer31.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer32.setVisibility(View.GONE);
            iv_poker_palyer32.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer33.setVisibility(View.GONE);
            iv_poker_palyer33.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer34.setVisibility(View.GONE);
            iv_poker_palyer34.setImageResource(R.mipmap.bullfight_poker_nagetive);
            iv_poker_palyer35.setVisibility(View.GONE);
            iv_poker_palyer35.setImageResource(R.mipmap.bullfight_poker_nagetive);
        }
    }

    /**
     * 显示游戏界面
     *
     * @param gameType 1（彩票）;2（斗牛）
     */
    private void showPlayView(int gameType) {
        this.gameType = gameType;
        //游戏内容视图
        rl_game_container.setVisibility(View.VISIBLE);
        //游戏左上角视图
        rl_game_info.setVisibility(View.VISIBLE);
        //走势图标
        iv_trend.setVisibility(View.VISIBLE);
        switch (gameType) {
            case 1://彩票
                rl_kp.setVisibility(View.VISIBLE);
                rl_bullfight_banker.setVisibility(View.GONE);
                live_game.setVisibility(View.VISIBLE);
                live_game_bullfight.setVisibility(View.GONE);
                iv_trend.setImageResource(R.mipmap.trend_img);
                break;
            case 2://斗牛
                rl_bullfight_banker.setVisibility(View.VISIBLE);
                rl_kp.setVisibility(View.GONE);
                live_game_bullfight.setVisibility(View.VISIBLE);
                live_game.setVisibility(View.GONE);
                iv_trend.setImageResource(R.mipmap.ic_bullfight_record);
                break;
        }
    }

    /**
     * 影藏游戏界面
     *
     * @param gameType 1（彩票）;2（斗牛）
     */
    private void hidePlayView(int gameType) {
        //游戏内容视图
        rl_game_container.setVisibility(View.GONE);
        //游戏左上角视图
        rl_game_info.setVisibility(View.GONE);
        //走势图标
        iv_trend.setVisibility(View.GONE);
        switch (gameType) {
            case 1://彩票
                rl_kp.setVisibility(View.GONE);
                live_game.setVisibility(View.GONE);
                break;
            case 2://斗牛
                rl_bullfight_banker.setVisibility(View.GONE);
                live_game_bullfight.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 家族
     * userId 主播ID
     *
     * @author sll
     * @time 2016/12/22 16:11
     */
    private void getFamilyMember() {
        String url = UrlBuilder.chargeServerUrl + UrlBuilder.selectFamilyMember;
        LogUtils.i("家族url: " + url);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", liveListBean.getRooms().getUserId() + "");
        String json = new JSONObject(hashMap).toString();
        LogUtils.i("家族Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        LogUtils.i("家族onError: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("家族onResponse: " + response);

                        SdkHttpResultSuccess sdkHttpResultSuccess = JsonUtil.json2Bean(response
                                .toString(), SdkHttpResultSuccess.class);


                        if (!TextUtils.isEmpty(sdkHttpResultSuccess.getObj())) {
                            LiveFamilyMemberBean liveFamilyMember = JsonUtil.json2Bean(response,
                                    LiveFamilyMemberBean.class);
                            showFamilyList(liveFamilyMember);
                        } else {
                            ToastUtils.showMessageCenter(mContext, sdkHttpResultSuccess.getMsg());
                        }


                    }
                });
    }

    /**
     * 展示家族列表
     *
     * @author sll
     * @time 2016/12/23 11:22
     */
    private void showFamilyList(LiveFamilyMemberBean liveFamilyMember) {
        final List<LiveListBean> mList = new ArrayList<>();
        for (LiveFamilyMemberBean.ObjBean o : liveFamilyMember.getObj()) {
            if (o.getRooms() != null)
                mList.add(ChannelToLiveBean.getLiveBeanFromObj(o));
        }
        LogUtils.e("ChannelToLiveBean" + mList.toString());
        BottomView familySelectView = new BottomView(this, R.style.BottomViewTheme_Transparent, R
                .layout.view_show_familyr_members);
        familySelectView.setAnimation(R.style.BottomToTopAnim);
        familySelectView.showBottomView(true);

        View mFamilyView = familySelectView.getView();
        RefreshRecyclerView familyMembers = (RefreshRecyclerView) mFamilyView.findViewById(R.id
                .view_family_members_recycler);
        FamilyMemberAdapter adapter = new FamilyMemberAdapter(1, this, mList, liveListBean
                .getRooms().getUserId() + "");
        GridLayoutManager mLayoutManager = new GridLayoutManager(WatchLiveActivity.this, 2,
                GridLayoutManager.HORIZONTAL, false);
        familyMembers.setLayoutManager(mLayoutManager);
        familyMembers.setAdapter(adapter);
        RecyclerViewManager.with(adapter, mLayoutManager)
                .setMode(RecyclerMode.NONE)
                .into(familyMembers, this);
    }


    /**
     * 显示乐檬的开始loading动画
     */
    private void startloading() {
        rlLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != mWl) {
            mWl.acquire();
            mMediaStreamingManager.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShutterButtonPressed = false;
        if (mWl != null) {
            try {
                mWl.release();
                mHandler.removeCallbacksAndMessages(null);
                mMediaStreamingManager.pause();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bullfightPresenter != null) {
            bullfightPresenter.detach();
        }
        isJinyan = false;
        if (flPlug.getVisibility() == View.VISIBLE) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("watch_private_flag", "0");
            map1.put("vip", "0");
            SendRoomMessageUtils.onCustomMessageLianmai(SendRoomMessageUtils.MESSAGE_WATCHER_DISCONNECT,
                    messageFragment, liveListBean.getRooms().getRoomId() + "", map1);
        }

        roomLiveExit();
        if (null != mMediaStreamingManager) {
            mMediaStreamingManager.destroy();
        }
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("userId", appUser.getId());
        map.put("roomId", liveListBean.getRooms().getId() + "");
        httpDatas.getDataForJsoNoloading("退出直播间", Request.Method.POST, UrlBuilder.roomExit, map,
                myHandler, RequestCode.REQUEST_ROOM_EXIT);

        timer.cancel();
        if (anchorVideo != null) {
            anchorVideo.release();
        }
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
        registerObservers(false);
        registerReceiveCustom(false);
        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
        unregisterReceiver(broadcastReceiver);
        barrageview.setSentenceList(new ArrayList<BarrageDateBean>());

        myHandler.removeMessages(10000);
        myHandler.removeCallbacks(timenNumber);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出登录
            if (mQuitDialog != null && !mQuitDialog.isShowing()) {
                mQuitDialog.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 注册网易云信广播接收器
     */
    public void registerReceiverWatchLiveActivity() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WatchLiveActivity");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 广播接收，收到网易云信收到或发出的自定义消息
     *
     * @author sll
     * @time 2016/11/1 17:44
     */

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String userId = intent.getStringExtra("FromAccount");
            LogUtils.i("WangYi_gift", "userId:" + userId);
//            customGiftBean = (CustomGiftBean) intent.getSerializableExtra("CustomGiftBean");
            message = (ChatRoomMessage) intent.getSerializableExtra("ChatRoomMessage");
            Map<String, Object> remote = null;
            if (message != null) {
                remote = message.getRemoteExtension();

                LogUtil.i("WangYi_gift", "收到礼物广播message != null" + message.getSessionId());
            }
            if (message != null && remote != null && !TextUtils.isEmpty((String) remote.get
                    ("type"))) {
                int type = Integer.parseInt((String) remote.get("type"));
                switch (type) {
                    case 105://进入房间
                        RoomUserBean roomUserBean = JavaBeanMapUtils.mapToBean((Map) message.
                                getRemoteExtension().get("data"), RoomUserBean.class);
                        liveNum.setText(++liveOnLineNums + "");
                        if (roomUserBean != null) {
                            mDatas.add(roomUserBean);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 106://离开房间
                        LogUtil.e("有人离开直播间", message.getRemoteExtension().get("data").toString());
                        RoomUserBean leaveRoom = JavaBeanMapUtils.mapToBean((Map) message.
                                getRemoteExtension().get("data"), RoomUserBean.class);
                        liveNum.setText(--liveOnLineNums + "");
                        mDatas.remove(leaveRoom);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 107:
                        //点亮
                        break;
                    case 108:
                        //点赞
//                        int number = Integer.parseInt((String) message.getRemoteExtension().get("number"));
//                        for (int i = 0; i < number; i++) {
//                            showLit();
//                        }
                        showLit();
                        break;
                    case 109:
//                        收到 109 礼物消息 发送过来广播
                        CustomGiftBean customGiftBean = JavaBeanMapUtils.mapToBean(remote,
                                CustomGiftBean.class);
                        if (Integer.parseInt(customGiftBean.getGift_item_number()) < 1) {
                            return;
                        }
                        LogUtils.e("customGiftBean" + customGiftBean.toString());
                        final UserInfoProvider.UserInfo userInfo = NimUIKit.getUserInfoProvider()
                                .getUserInfo(message.getFromAccount());
                        String url = userInfo != null ? userInfo.getAvatar() : null;
                        String name;
                        if (mGiftList == null) {
                            return;
                        }

                        changeJinpiao(customGiftBean);

                        if (message.getChatRoomMessageExtension() == null) {
                            name = appUser.getNickName();
                        } else {
                            name = message.getChatRoomMessageExtension().getSenderNick();
                        }

                        GiftBean giftBean = new GiftBean();
                        for (int i = 0, j = mGiftList.size(); i < j; i++) {
                            if (mGiftList.get(i).getId().equals(customGiftBean.getGift_item_index
                                    ())) {
                                staticIcon = mGiftList.get(i).getStaticIcon();
                                giftBean = mGiftList.get(i);
                            }
                        }


                        GiftSendModel giftSendModel = new GiftSendModel(Integer.parseInt
                                (customGiftBean.getGift_item_number()), url, name, customGiftBean
                                .getGift_item_message(), staticIcon, customGiftBean.getRepeatGiftNumber()
                                , customGiftBean.getGift_item_index(), customGiftBean.getUserId());
                        if (giftBean.getWinFlag().equals("2") && !customGiftBean
                                .getGift_item_message().contains("占领了热一")) {
                            /**
                             * 礼物特效
                             */
                            AnimationUtils.getAnimation().SwichAnimation(customGiftBean,
                                    WatchLiveActivity.this, mRoot, 1);
                        }
                        if (null != customGiftBean.getGift_Grand_Prix()) {
                            String[] Gift_prix = customGiftBean.getGift_Grand_Prix();
                            /**
                             * 中奖特效
                             */
                            if (Gift_prix.length > 0) {
                                for (int i = 0, j = Gift_prix.length; i < j; i++) {
                                    if (Gift_prix[i].equals("500")) {
                                        AnimationUtils.getAnimation().SwichAnimation
                                                (customGiftBean, WatchLiveActivity.this, mRoot, 2);
                                        break;
                                    }
                                }
                            }
                        }
                        starGiftAnimation(giftSendModel);
                        giftSendModel = null;
                        giftBean = null;
                        break;
                    case 202://主播离开房间
                        LogUtils.e("202主播离开房间");
//                        initQuitDialog("主播已离开直播间", false);
                        Intent intent1 = new Intent(mContext, WatchQuitLiveActivity.class);
                        intent1.putExtra("liveListBean", liveListBean);
                        startActivity(intent1);
                        finish();
                        break;
                    case 110:
                        LogUtil.e("主播连麦成功", message.getRemoteExtension().toString());
                        showLianmaiView();
                        customLianmaiBean = JavaBeanMapUtils.mapToBean(remote,
                                CustomLianmaiBean.class);
                        if (!customLianmaiBean.getUserId().equals(appUser.getId())) {
                            String video = customLianmaiBean.getBroadcastUrl();
                            WatchLive(video);
                        }

                        if (!TextUtils.isEmpty(customLianmaiBean.getPicUrl())) {
                            Picasso.with(mContext).load(customLianmaiBean.getPicUrl()).placeholder(R.mipmap.head_default)
                                    .error(R.mipmap.head_default).resize(50, 50).into(liveLianmaiHead);
                        }
                        break;
                    case 111:
                        LogUtil.e("断开连麦成功", message.getRemoteExtension().toString());
                        showToast("主播断开连麦");
                        roomLiveExit();
                        hindSmallVideo();
                        break;
                    case 114:
                        if (message.getRemoteExtension().get("jinyanId").equals(appUser.getId())) {
                            if (message.getRemoteExtension().get("isJinyan").equals("1")) {
                                isJinyan = true;
                            } else {
                                isJinyan = false;
                            }
                        }
                        break;
                    case 112:
                        anchorVideo.prepare();
                        break;
                    case 113:

                        break;
                    case 2828:
                        isPlayerRoom = true;
                        getTimenumber();
                        showPlayView(1);
                        break;
                    case 2929://主播开启斗牛游戏
                        isPlayerRoom = true;
                        bullfightPresenter = new BullfightPresenter(WatchLiveActivity.this);
                        bullfightPresenter.getTimeAndNper(liveListBean.getRoomId() + "");
                        break;
                    case 3030://有人投注
                        int betPosition = 0;
                        int amount = 0;
                        if(remote.get("NIM_TOUZHU_GOLD_SELECT_1")!=null) {
                            betPosition = 1;
                            amount = Integer.valueOf((String) remote.get("NIM_TOUZHU_GOLD_SELECT_1"));
                        }else if(remote.get("NIM_TOUZHU_GOLD_SELECT_2")!=null) {
                            betPosition = 2;
                            amount = Integer.valueOf((String) remote.get("NIM_TOUZHU_GOLD_SELECT_2"));
                        }else if(remote.get("NIM_TOUZHU_GOLD_SELECT_3")!= null) {
                            betPosition = 3;
                            amount = Integer.valueOf((String) remote.get("NIM_TOUZHU_GOLD_SELECT_3"));
                        }
                        addBettingView(betPosition,amount,false);
                        break;
                    case 10009:
                        LogUtil.e("升级", message.getRemoteExtension().toString());
                        AppUser mAppUser = JavaBeanMapUtils.mapToBean((Map) message.getRemoteExtension().get("data"),
                                AppUser.class);
                        if (mAppUser.getId().equals(appUser.getId())) {
                            appUser.setLevel(mAppUser.getLevel());
                            SharedPreferenceUtils.saveUserInfo(mContext, appUser);
                            appUser = SharedPreferenceUtils.getUserInfo(mContext);
                        }
                        break;
                    default:
                        break;
                }
                LogUtil.i("WangYi_gift", "聊天室，发礼物广播:收到礼物消息" + remote.get("gift_item_message"));
            } else if (message != null && remote != null && !TextUtils.isEmpty((String) remote
                    .get("danmu")) && remote.get("danmu").equals("true")) {
                final UserInfoProvider.UserInfo userInfo = NimUIKit.getUserInfoProvider()
                        .getUserInfo(message.getFromAccount());
                String url = userInfo != null ? userInfo.getAvatar() : null;
//                ArrayList<BarrageDateBean> data = new ArrayList();
                BarrageDateBean barrageDateBean = new BarrageDateBean();
                barrageDateBean.setContent(message.getContent());
                if (message.getChatRoomMessageExtension() != null) {
                    barrageDateBean.setNickName("@" + message.getChatRoomMessageExtension()
                            .getSenderNick());
                } else {
                    barrageDateBean.setNickName("@" + appUser.getNickName());
                }
                barrageDateBean.setPicUrl(url);
//                data.add(barrageDateBean);
                barrageview.addSentence(barrageDateBean);
                if (message.getFromAccount().equals("miu_" + appUser.getId())) {
                    myGoldCoin = myGoldCoin - 1;
                    String myCoin = CountUtils.getCount(myGoldCoin);
                    all_lepiao.setText(myCoin);
                }
            } else if (userId != null && userId.indexOf("miu_") != -1) {
                userId = userId.substring(4);
                ifattention("请求用户信息", userId, RequestCode.REQUEST_USER_INFO);
            }

        }
    };


// **************************** 头像列表开始****************************************** //

    /**
     * RecycleView初始
     */
    private void initRecycle() {
        horizontalRefreshLayout.setRefreshCallback(this);
        horizontalRefreshLayout.setRefreshHeader(new LoadingRefreshHeader(this), HorizontalRefreshLayout.LEFT);
        horizontalRefreshLayout.setRefreshHeader(new LoadingRefreshHeader(this), HorizontalRefreshLayout.RIGHT);

        mLayoutManager = new GridLayoutManager(mContext, 1, LinearLayoutManager.HORIZONTAL, false);
        rlvListLiveAudiences.setLayoutManager(mLayoutManager);
        mAdapter = new RoomUsersDataAdapter(mContext, mDatas);
        rlvListLiveAudiences.setAdapter(mAdapter);
        //进入页面自动刷新
        requestNet();
    }

    @Override
    public void onLeftRefreshing() {
        isRefresh = true;
        isCanStop = true;
        requestNet();
    }

    @Override
    public void onRightRefreshing() {
        isRefresh = false;
        isCanStop = true;
        if (page < totalPages) {
            requestNet();
        } else {
            finshRefresh();
        }

    }

    /**
     * 取消刷新和加载
     */
    private void finshRefresh() {
        horizontalRefreshLayout.onRefreshComplete();
    }

    /**
     * 聊天室头像列表
     */
    private void requestNet() {
        page = isRefresh ? 1 : ++page;
        String url = UrlBuilder.serverUrl + UrlBuilder.room;
        if (appUser != null) {
            url += liveListBean.getRooms().getId();
            url += "/users?pageNum=" + page;
            LogUtils.i("聊天室头像列表(url):" + url);
            OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack(mContext,
                    HttpDatas.KEY_CODE) {
                @Override
                public void onFaild() {
                    finshRefresh();
                    showToast(isRefresh ? "刷新失败 " : "加载失败");
                    if (isCanStop) {
                        finshRefresh();
                    }
                }

                @Override
                public void onSucess(String data) {
                    LogUtils.i("聊天室头像列表(data):" + data);
                    if (isCanStop) {
                        finshRefresh();
                    }
                    RoomUserDataBean visitor = JsonUtil.json2Bean(data, RoomUserDataBean.class);
                    totalPages = visitor.getTotalPages();
                    liveOnLineNums = visitor.getTotalItems();
                    liveNum.setText(liveOnLineNums + "");
                    hanlderVisitors(visitor);
                }
            });
        }
    }


    /**
     * 直播观众头像列表
     *
     * @param visitor
     */
    private void hanlderVisitors(RoomUserDataBean visitor) {
        if (visitor != null) {
            List<RoomUserBean> result = visitor.getResult();
            if (result != null) {
                if (isRefresh) {
                    //下拉刷新需要清除数据
                    mDatas.clear();
                }
//                for (int i = 0 ,j = result.size(); i < j; i++) {
//                    if (result.get(i).getUserId().equals(liveListBean.getId()+"")){
//                        result.remove(i);
//                        liveNum.setText(--liveOnLineNums + "");
//                    }
//                }
                mDatas.addAll(result);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    // **************************** 头像列表结束 ****************************************** //


    // ********************************* 聊天室开始 *************************************** //

    /**
     * 登录聊天室
     *
     * @author sll
     * @time 2016/11/18 11:21
     */
    private void enterRoom() {
        EnterChatRoomData data = new EnterChatRoomData(roomId);
        enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
        enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData result) {
                LogUtils.i("WangYi", "Success");
                onLoginDone();
                initMessageFragment();
            }

            @Override
            public void onFailed(int code) {
                LogUtils.i("WangYi", "onFailed");
                // test
                LogUtil.ui("enter chat room failed, callback code=" + code);
                onLoginDone();
                if (code == ResponseCode.RES_CHATROOM_BLACKLIST) {
                    LogUtils.i("WangYi", "你已被拉入黑名单，不能再进入");
                } else if (code == ResponseCode.RES_ENONEXIST) {
                    LogUtils.i("WangYi", "聊天室不存在");
                } else {
                    LogUtils.i("WangYi", "enter chat room failed, code=" + code);
                }
                initMessageFragment();
            }

            @Override
            public void onException(Throwable exception) {
                onLoginDone();
//                Toast.makeText(WatchLiveActivity.this, "enter chat room exception, e=" +
// exception.getMessage(), Toast.LENGTH_SHORT).show();
                LogUtils.i("WangYi", "enter chat room exception, e=" + exception.getMessage());
                finish();
            }
        });
    }

    private static Handler handler;

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    private void initMessageFragment() {
        messageFragment = (ChatRoomMessageFragment) getSupportFragmentManager().findFragmentById
                (R.id.watch_room_message_fragment);
        if (messageFragment != null) {
            messageFragment.init(roomId);
        } else {
            // 如果Fragment还未Create完成，延迟初始化
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initMessageFragment();
                }
            }, 50);
        }

    }

    private void onLoginDone() {
        enterRequest = null;
        DialogMaker.dismissProgressDialog();
    }
    // ********************************* 聊天室结束 *************************************** //


    // ***************************** 礼物相关开始 **************************************//

    /**
     * 打开礼物界面
     */
    public void getGiftPopup() {
        //隐藏连送按钮
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSendGiftLian.setVisibility(View.GONE);
                hidePlayView(gameType);
            }
        }, 200);

        popupWindow = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.view_show_viewpager, null);
        popupWindow.setContentView(view);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(rlvListLiveAudiences, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopOnDismissListner());

        mUserCoin = (TextView) view.findViewById(R.id.tv_show_select_user_coin);
        ll_user_coin = (LinearLayout) view.findViewById(R.id.gift_coin_ll);
        ll_user_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mContext, ChargeCoinsActivity.class).putExtra("yanpiao",
//                        SharedPreferenceUtils.getGoldCoin(mContext)));
//                startActivity(new Intent(mContext, PayOrderActivity.class));
             /*   Intent intent = new Intent(mContext, ExplainWebViewActivity.class);
                intent.putExtra("flag", 1000);
                startActivity(intent);*/
                showToast("暂时不支持充值");
            }
        });

        String myCoin = CountUtils.getCount(myGoldCoin);
        mUserCoin.setText(myCoin);

        mVpGiftView = (ViewPager) view.findViewById(R.id.vp_gift_page);
        mSendGiftBtn = (Button) view.findViewById(R.id.btn_show_send_gift);
        mSendGiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEND_NUM = 1;
                onClickSendGift();
                popupWindow.dismiss();
            }
        });
        //表示已经请求过数据不再向下执行
        if (mGiftViews != null) {
            fillGift();
            return;
        }
    }

    /**
     * 设置activity背景
     *
     * @param alpha
     */
    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = alpha;
        this.getWindow().setAttributes(params);
    }

    /**
     * PopupWindow Dismiss监听
     */
    private class PopOnDismissListner implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
            changeSendGiftBtnStatue(false);
            for (int i = 0; i < mGiftViews.size(); i++) {
                for (int j = 0; j < mGiftViews.get(i).getChildCount(); j++) {
                    mGiftViews.get(i).getChildAt(j).findViewById(R.id.rl_bg)
                            .setBackgroundResource(0);
                }
            }
        }

    }


    /**
     * @dw 点击赠送礼物按钮
     */
    private void onClickSendGift() {
        if (myGoldCoin < Long.valueOf(mSendGiftItem.getMemberConsume())) {
            showToast("金币不足，请充值");
            return;
        }
        sendGift();

        if (giftTimer != null) {
            giftTimer.cancel();
        }

        mSendGiftLian.setVisibility(View.VISIBLE);

        giftTimer = new CountDownTimer(3500, 1000) {//连送礼物
            @Override
            public void onTick(long millisUntilFinished) {
                btn_timer.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                recoverySendGiftBtnLayout();
            }
        };
        giftTimer.start();

    }

    private void sendGift() {
        mSendGiftList.add(mSendGiftItem);
        mSendGiftNumList.add(SEND_NUM + "");
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("userId", appUser.getId());
        map.put("roomId", liveListBean.getRooms().getId() + "");
        map.put("amount", mSendGiftItem.getMemberConsume());
        map.put("giftId", mSendGiftItem.getId());
        map.put("toUserId", liveListBean.getId() + "");
        httpDatas.DataNoloadingAdmin("赠送礼物", Request.Method.POST,
                UrlBuilder.SEND_GIFT, map, myHandler, RequestCode.SEND_GIFT);
    }

    /**
     * @dw 礼物列表填充
     */
    private void fillGift() {
        if (null == mVpGiftAdapter && null != mGiftResStr) {
            //礼物item填充
            List<View> mGiftViewList = new ArrayList<>();
            int index = 0;
            int g = mGiftList.size() % 8 == 0 ? mGiftList.size() / 8 : mGiftList.size() / 8 + 1;
            for (int i = 0; i < g; i++) {
                View v = getLayoutInflater().inflate(R.layout.view_show_gifts_gv, null);
                mGiftViewList.add(v);
                List<GiftBean> l = new ArrayList<>();
                for (int j = 0; j < 8; j++) {
                    if (index >= mGiftList.size()) {
                        break;
                    }
                    l.add(mGiftList.get(index));
                    index++;
                }
                mGiftViews.add((GridView) v.findViewById(R.id.gv_gift_list));
                mGiftViews.get(i).setAdapter(new GridViewAdapter(l, getContext()));
                mGiftViews.get(i).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long
                            id) {
                        giftItemClick(parent, view, position);
                    }
                });
            }
            mVpGiftAdapter = new ViewPageGridViewAdapter(mGiftViewList);
        }
        mVpGiftView.setAdapter(mVpGiftAdapter);
    }

    /**
     * @dw 赠送礼物单项被选中
     */
    private void giftItemClick(AdapterView<?> parent, View view, int position) {
        mSendGiftItem = (GiftBean) parent.getItemAtPosition(position);
        //点击其他礼物
        changeSendGiftBtnStatue(true);
        for (int i = 0; i < mGiftViews.size(); i++) {
            for (int j = 0; j < mGiftViews.get(i).getChildCount(); j++) {
                mGiftViews.get(i).getChildAt(j).findViewById(R.id.rl_bg)
                        .setBackgroundResource(0);
            }
        }
        /**
         * 送礼物界面的点击效果
         */
        view.findViewById(R.id.rl_bg).setBackgroundResource(R.drawable.gift_tv_bg1);

    }

    //连送按钮隐藏
    private void recoverySendGiftBtnLayout() {
        mSendGiftLian.setVisibility(View.GONE);
    }

    /**
     * @param statue 开启or关闭
     * @dw 赠送礼物按钮状态修改
     */
    private void changeSendGiftBtnStatue(boolean statue) {
        if (statue) {
            mSendGiftBtn.setBackgroundColor(getResources().getColor(R.color.crimson));
            mSendGiftBtn.setEnabled(true);
        } else {
            mSendGiftBtn.setBackgroundColor(getResources().getColor(R.color.light_gray2));
            mSendGiftBtn.setEnabled(false);
        }
    }

    public String frame1ShowUserId = "";//跑道1正在显示动画送礼物人的id
    public String frame1ShowGiftId = "";//跑道1正在显示动画送礼礼物的id
    public String frame2ShowUserId = "";//跑道2正在显示动画送礼物人的id
    public String frame2ShowGiftId = "";//跑道2正在显示动画送礼礼物的id

    private void starGiftAnimation(GiftSendModel model) {
        if (!giftFrameLayout1.isShowing() || (frame1ShowUserId.equals(model.getFromUserId()) && frame1ShowGiftId.equals(model.getGiftId()))) {
            frame1ShowUserId = model.getFromUserId();
            frame1ShowGiftId = model.getGiftId();
            sendGiftAnimation(giftFrameLayout1, model);
        } else if (!giftFrameLayout2.isShowing() || (frame2ShowUserId.equals(model.getFromUserId()) && frame2ShowGiftId.equals(model.getGiftId()))) {
            frame2ShowUserId = model.getFromUserId();
            frame2ShowGiftId = model.getGiftId();
            sendGiftAnimation(giftFrameLayout2, model);
        } else {
            giftSendModelList.add(model);
        }
    }

    private void sendGiftAnimation(final GiftFrameLayout view, GiftSendModel model) {
        view.setModel(model);
        AnimatorSet animatorSet = view.startAnimation(model.getGiftCount());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                synchronized (giftSendModelList) {
                    if (giftSendModelList.size() > 0) {
                        sendGiftAnimation(view, giftSendModelList.get(0));
                        giftSendModelList.remove(0);
                    }
                }
            }
        });
    }

    /**
     * 当收到礼物信息时，界面上金票数据改变
     *
     * @param customGiftBean
     */
    private void changeJinpiao(CustomGiftBean customGiftBean) {
        //如果收礼物的是主播,界面上显示主播的金币数改变
        zhuboReceve = zhuboReceve + (Integer.parseInt(customGiftBean.getGift_item_number())
                * Integer.parseInt(customGiftBean.getGift_coinnumber_index()));
        String receivedGoldCoin = CountUtils.getCount(zhuboReceve);
        liveJinpiao.setText(receivedGoldCoin); //显示主播乐票数量
    }

    // ***************************** 礼物相关结束 **************************************//


    // ******************************* 设置网易云信注册等内容开始 ***************************//

    /**
     * 自定义通知接收
     *
     * @author sll
     * @time 2016/12/6 18:08
     */
    private void registerReceiveCustom(boolean register) {
        if (register)
            LogUtils.i("WangYi_CN", "Watch接收自定义通知:开启");
        else
            LogUtils.i("WangYi_CN", "Watch接收自定义通知:关闭");
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeCustomNotification(observer, register);
        service.observeReceiveMessage(incomingMessageObserver, register);
    }

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            int unread = NIMClient.getService(MsgService.class).getTotalUnreadCount() + 1;
            LogUtils.i("WangYi", "消息接收观察者unread:" + unread);
            tabNewMsgLabel.setVisibility(unread > 0 ? View.VISIBLE : View.GONE);
            if (unread > 0) {
                tabNewMsgLabel.setText(String.valueOf(ReminderSettings.unreadMessageShowRule(unread)));
            }
        }
    };

    Observer<CustomNotification> observer = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            // 在这里处理自定义通知。
            LogUtils.i("WangYi_CN", "Watch接收自定义通知(SessionId):" + message.getSessionId());
            LogUtils.i("WangYi_CN", "Watch接收自定义通知(fromAccount):" + message.getFromAccount());
            LogUtils.i("WangYi_CN", "Watch接收自定义通知(SessionType):" + message.getSessionType());
            LogUtils.i("WangYi_CN", "Watch接收自定义通知(ApnsText):" + message.getApnsText());
            LogUtils.i("WangYi_CN", "Watch接收自定义通知(Content):" + message.getContent());
            if (message.getPushPayload() != null)
                LogUtils.i("WangYi_CN", "Watch接收自定义通知(pushPayload):" + message.getPushPayload()
                        .toString());

            DianBoDateBean dianBoDateBean = JSON.parseObject(message.getContent(), DianBoDateBean
                    .class);
            if (dianBoDateBean != null) {
                String type = dianBoDateBean.getType();
                if (type != null) {
                    if (type.equals(CustomNotificationType.IM_P2P_TYPE_SUBLIVE_PUBLIC)) {
                        //主播请求连麦和观众连麦
                        shouLianmaiPop();
                    } else {
                        if (lianmai_request_pop.isShowing()) {
                            lianmai_request_pop.dismiss();
                        }
                    }
                }
            }
        }
    };

    /**
     * 注册监听
     *
     * @author sll
     * @time 2016/11/18 11:21
     */
    private void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineStatus,
                register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver,
                register);
    }

    Observer<ChatRoomStatusChangeData> onlineStatus = new Observer<ChatRoomStatusChangeData>() {
        @Override
        public void onEvent(ChatRoomStatusChangeData chatRoomStatusChangeData) {
            if (!chatRoomStatusChangeData.roomId.equals(roomId)) {
                return;
            }
            if (chatRoomStatusChangeData.status == StatusCode.CONNECTING) {
                LogUtils.i("WangYi", "连接中...");
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINING) {
                LogUtils.i("WangYi", "登录中...");
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINED) {
                LogUtils.i("WangYi", "检测在线");
            } else if (chatRoomStatusChangeData.status == StatusCode.UNLOGIN) {
                LogUtils.i("WangYi", "检测不在线");
                int code = NIMClient.getService(ChatRoomService.class).getEnterErrorCode(roomId);
                LogUtils.d(TAG, "chat room enter error code:" + code);
                if (code != ResponseCode.RES_ECONNECTION) {
                    LogUtils.i("WangYi", "未登录,code=" + code);
                }
            } else if (chatRoomStatusChangeData.status == StatusCode.NET_BROKEN) {
                Toast.makeText(WatchLiveActivity.this, R.string.net_broken, Toast.LENGTH_SHORT)
                        .show();
            }
            LogUtils.i(TAG, "chat room online status changed to " + chatRoomStatusChangeData
                    .status.name());
        }
    };
    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
        @Override
        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
            clearChatRoom();
        }
    };

    public void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 未读消息数量观察者实现
     */
    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        int unread = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        if (item == null || tabNewMsgLabel == null || tabNewIndicator == null) {
            if (tabNewMsgLabel != null)
                tabNewMsgLabel.setVisibility(View.GONE);
            if (tabNewIndicator != null)
                tabNewIndicator.setVisibility(View.GONE);
            return;
        }
        boolean indicator = item.indicator();
        tabNewMsgLabel.setVisibility(unread > 0 ? View.VISIBLE : View.GONE);
        tabNewIndicator.setVisibility(indicator ? View.VISIBLE : View.GONE);
        if (unread > 0) {
            tabNewMsgLabel.setText(String.valueOf(ReminderSettings.unreadMessageShowRule(unread)));
        }
    }

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange
                (sysMsgUnreadCountChangedObserver,
                        register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class)
                .querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    // ******************************* 设置网易云信注册等内容结束 ***************************//


    // *********************************** 连麦内容开始 ********************************** //

    protected StreamingProfile mProfile;
    private int mCurrentCamFacingIndex;
    protected CameraStreamingSetting mCameraStreamingSetting;
    protected MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    protected MediaStreamingManager mMediaStreamingManager;
    private static final int ZOOM_MINIMUM_WAIT_MILLIS = 33; //ms
    private static final int MSG_START_STREAMING = 0;
    private static final int MSG_STOP_STREAMING = 1;
    private static final int MSG_SET_ZOOM = 2;
    protected boolean mShutterButtonPressed = false;
    private int mCurrentZoom = 0;
    private boolean mOrientationChanged = false;
    private int mMaxZoom = 0;
    protected boolean mIsReady = false;
    private String mStatusMsgContent;
    private String mLogContent = "\n";
    protected PowerManager.WakeLock mWl;


    /**
     * 连麦拉流小窗口
     *
     * @param twoVideo
     */
    private void WatchLive(final String twoVideo) {
        LogUtils.e("twoVideo--twoVideo" + twoVideo);
        if (videoLian.getVisibility() == View.INVISIBLE || videoLian.getVisibility() == View.GONE) {
            videoLian.setVisibility(View.VISIBLE);
        }
        flPull.setVisibility(View.VISIBLE);
        videoLian.getHolder().setFormat(PixelFormat.TRANSPARENT);
        videoLian.setZOrderMediaOverlay(true);
        liveVideo = new LiveVideo();
        liveVideo.initLive(twoVideo, WatchLiveActivity.this, videoLian);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                liveVideo.prepare();
            }
        }, 1000);
    }

    /**
     * 关闭了连麦，隐藏拉流小窗口
     */
    private void hindSmallVideo() {
        hideLianmaiView();
        if (liveVideo != null) {
            liveVideo.release();
            videoLian.getHolder().getSurface().release();
            videoLian.setVisibility(View.INVISIBLE);
            flPull.setVisibility(View.GONE);
            liveVideo = null;
        }
    }

    /**
     * 连麦
     *
     * @dw 初始化直播播放器
     */
    private void initLivePlay(String tuiliu) {

        mProfile = new StreamingProfile();

        try {

            mProfile.setPublishUrl(tuiliu);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        StreamingProfile.AudioProfile aProfile = new StreamingProfile.AudioProfile(44100, 96 *
                1024);
        StreamingProfile.VideoProfile vProfile = new StreamingProfile.VideoProfile(30, 1000 *
                1024, 48);
        StreamingProfile.AVProfile avProfile = new StreamingProfile.AVProfile(vProfile, aProfile);

        mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_MEDIUM2)
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
//                .setAVProfile(avProfile)
//                .setPreferredVideoEncodingSize(960, 544)
                .setEncodingSizeLevel(Config.ENCODING_LEVEL)
                .setEncoderRCMode(StreamingProfile.EncoderRCModes.BITRATE_PRIORITY)
                .setDnsManager(getMyDnsManager())
                .setAdaptiveBitrateEnable(true)
                .setFpsControllerEnable(true)
                .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))
//                .setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT)
                .setSendingBufferProfile(new StreamingProfile.
                        SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));

        CameraStreamingSetting.CAMERA_FACING_ID cameraFacingId = chooseCameraFacingId();
        mCurrentCamFacingIndex = cameraFacingId.ordinal();
        mCameraStreamingSetting = new CameraStreamingSetting();
        mCameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
                .setCameraFacingId(cameraFacingId)
//                .setCameraSourceImproved(true)
                .setResetTouchFocusDelayInMs(3000)
//                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setBuiltInFaceBeautyEnabled(true)
                .setFaceBeautySetting(new CameraStreamingSetting.
                        FaceBeautySetting(1.0f, 1.0f, 0.8f))
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);

        mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
        mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);
        initUIs();

    }

    AspectFrameLayout afl;
    CameraLivePreviewFrameView cameraPreviewFrameView;
    WatermarkSetting watermarksetting;
    PowerManager mPm;


    private void initPaly() {
        isFristLianmai = false;
        if (mPm == null) {
            mPm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWl = mPm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MyTag");
        }
        requestPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (afl == null) {
            afl = (AspectFrameLayout) mRoomContainer.findViewById(R.id.cameraPreview_afl);
            afl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        }
        LogUtils.e("CameraLivePreviewFrameView" + "11ew");
//        if (cameraPreviewFrameView == null) {
        cameraPreviewFrameView =
                (CameraLivePreviewFrameView) mRoomContainer.findViewById(R.id
                        .cameraPreview_live_surfaceView);
        LogUtils.e("CameraLivePreviewFrameView" + "22ew");
        cameraPreviewFrameView.setListener(null);
        cameraPreviewFrameView.setZOrderMediaOverlay(true);
        cameraPreviewFrameView.setVisibility(View.VISIBLE);
//        }else {
//            cameraPreviewFrameView.setVisibility(View.VISIBLE);
//        }
        if (watermarksetting == null) {
            watermarksetting = new WatermarkSetting(this);
            watermarksetting.setResourceId(R.drawable.umeng_socialize_facebook)
                    .setSize(WatermarkSetting.WATERMARK_SIZE.MEDIUM)
                    .setAlpha(100)
                    .setCustomPosition(0.5f, 0.5f);
        }
//
//        if (mMediaStreamingManager != null) {
//            LogUtils.e("CameraLivePreviewFrameView" + "mMediaStreamingManager");
//            mMediaStreamingManager = null;
//        }
//        mMediaStreamingManager = new MediaStreamingManager(this, afl, cameraPreviewFrameView,
//                AVCodecType.HW_VIDEO_WITH_HW_AUDIO_CODEC); // hw codec
//        mMediaStreamingManager.prepare(mCameraStreamingSetting, mMicrophoneStreamingSetting,
//                null, mProfile);
//        mMediaStreamingManager.setStreamingStateListener(this);
//        mMediaStreamingManager.setSurfaceTextureCallback(this);
//        mMediaStreamingManager.setStreamingSessionListener(this);
//        mMediaStreamingManager.setStreamStatusCallback(this);
//        mMediaStreamingManager.switchCamera(CameraStreamingSetting.CAMERA_FACING_ID
//                .CAMERA_FACING_FRONT);
        LogUtils.e("CameraLivePreviewFrameView" + "33ew");
        if (mMediaStreamingManager == null) {
            mMediaStreamingManager = new MediaStreamingManager(this, afl, cameraPreviewFrameView,
                    AVCodecType.HW_VIDEO_WITH_HW_AUDIO_CODEC); // hw codec
            try {
                mMediaStreamingManager.prepare(mCameraStreamingSetting,
                        mMicrophoneStreamingSetting, null, mProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMediaStreamingManager.setStreamingStateListener(this);
            mMediaStreamingManager.setSurfaceTextureCallback(this);
            mMediaStreamingManager.setStreamingSessionListener(this);
            mMediaStreamingManager.setStreamStatusCallback(this);
            mMediaStreamingManager.switchCamera(CameraStreamingSetting.CAMERA_FACING_ID
                    .CAMERA_FACING_FRONT);
        }

    }

    private RotateLayout mRotateLayout;

    protected void setFocusAreaIndicator() {
        if (mRotateLayout == null) {
            mRotateLayout = (RotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
            mMediaStreamingManager.setFocusAreaIndicator(mRotateLayout,
                    mRotateLayout.findViewById(R.id.focus_indicator));
        }
    }

    private static DnsManager getMyDnsManager() {
        IResolver r0 = new DnspodFree();
        IResolver r1 = AndroidDnsServer.defaultResolver();
        IResolver r2 = null;
        try {
            r2 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }

    private CameraStreamingSetting.CAMERA_FACING_ID chooseCameraFacingId() {
        if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID
                .CAMERA_FACING_3RD)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID
                .CAMERA_FACING_FRONT)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
        }
    }

    private void initUIs() {
        View rootView = mRoomContainer.findViewById(R.id.fl_plug);
        rootView.addOnLayoutChangeListener(this);
    }

    protected void startStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_START_STREAMING), 50);
    }

    protected void stopStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_STOP_STREAMING), 50);
    }

    /**
     * PopupWindow
     * =======
     *
     * @param num 倒数时间
     * @dw 开始直播倒数计时
     */
    private FBO mFBO = new FBO();

    @Override
    public boolean onPreviewFrame(byte[] bytes, int i, int i1, int i2, int i3, long l) {
        return true;
    }

    @Override
    public void onSurfaceCreated() {
        mFBO.initialize(this);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        mFBO.updateSurfaceSize(width, height);
    }

    @Override
    public void onSurfaceDestroyed() {
        mFBO.release();
    }

    @Override
    public int onDrawFrame(int texId, int texWidth, int texHeight, float[] transformMatrix) {
        // newTexId should not equal with texId. texId is from the SurfaceTexture.
        // Otherwise, there is no filter effect.
        int newTexId = mFBO.drawFrame(texId, texWidth, texHeight);
        return newTexId;
    }

    @Override
    public void onAudioSourceAvailable(ByteBuffer byteBuffer, int i, long l, boolean b) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp X:" + e.getX() + ",Y:" + e.getY());

        if (mIsReady) {
            setFocusAreaIndicator();
            mMediaStreamingManager.doSingleTapUp((int) e.getX(), (int) e.getY());
            return true;
        }
        return false;
    }

    @Override
    public boolean onZoomValueChanged(float factor) {
        if (mIsReady && mMediaStreamingManager.isZoomSupported()) {
            mCurrentZoom = (int) (mMaxZoom * factor);
            mCurrentZoom = Math.min(mCurrentZoom, mMaxZoom);
            mCurrentZoom = Math.max(0, mCurrentZoom);

            Log.d(TAG, "zoom ongoing, scale: " + mCurrentZoom + ",factor:" + factor + ",maxZoom:"
                    + mMaxZoom);
            if (!mHandler.hasMessages(MSG_SET_ZOOM)) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ZOOM),
                        ZOOM_MINIMUM_WAIT_MILLIS);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onRecordAudioFailedHandled(int i) {
        mMediaStreamingManager.updateEncodingType(AVCodecType.SW_VIDEO_CODEC);
        mMediaStreamingManager.startStreaming();
        return true;
    }

    @Override
    public boolean onRestartStreamingHandled(int i) {
        return mMediaStreamingManager.startStreaming();
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        Camera.Size size = null;
        if (list != null) {
            for (Camera.Size s : list) {
                if (s.height >= 480) {
                    size = s;
                    break;
                }
            }
        }
        return size;
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_STREAMING:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // disable the shutter button before startStreaming
                            boolean res = mMediaStreamingManager.startStreaming();
                            mShutterButtonPressed = true;
                            Log.i(TAG, "res:" + res);
                            if (!res) {
                                mShutterButtonPressed = false;
                            }
                        }
                    }).start();
                    break;
                case MSG_STOP_STREAMING:
                    if (mShutterButtonPressed) {
                        // disable the shutter button before stopStreaming
                        boolean res = mMediaStreamingManager.stopStreaming();
                        if (!res) {
                            mShutterButtonPressed = true;
                        }
                    }
                    break;
                case MSG_SET_ZOOM:
                    mMediaStreamingManager.setZoomValue(mCurrentZoom);
                    break;
                default:
                    Log.e(TAG, "Invalid message");
                    break;
            }
        }
    };

    @Override
    public void onStateChanged(StreamingState streamingState, Object extra) {
        Log.i(TAG, "StreamingState streamingState:" + streamingState + ",extra:" + extra);

        switch (streamingState) {
            case PREPARING:
                mStatusMsgContent = getString(R.string.string_state_preparing);
                break;
            case READY:
                mIsReady = true;
                mMaxZoom = mMediaStreamingManager.getMaxZoom();
                mStatusMsgContent = getString(R.string.string_state_ready);
                startStreaming();
                break;
            case CONNECTING:
                mStatusMsgContent = getString(R.string.string_state_connecting);
                break;
            case STREAMING:
                mStatusMsgContent = getString(R.string.string_state_streaming);
                break;
            case SHUTDOWN:
                mStatusMsgContent = getString(R.string.string_state_ready);
                if (mOrientationChanged) {
                    mOrientationChanged = false;
                    startStreaming();
                }
                break;
            case IOERROR:
                mLogContent += "IOERROR\n";
                mStatusMsgContent = getString(R.string.string_state_ready);
                break;
            case UNKNOWN:
                mStatusMsgContent = getString(R.string.string_state_ready);
                break;
            case SENDING_BUFFER_EMPTY:
                break;
            case SENDING_BUFFER_FULL:
                break;
            case AUDIO_RECORDING_FAIL:
                break;
            case OPEN_CAMERA_FAIL:
                Log.e(TAG, "Open Camera Fail. id:" + extra);
                break;
            case DISCONNECTED:
                mLogContent += "DISCONNECTED\n";
                break;
            case INVALID_STREAMING_URL:
                Log.e(TAG, "Invalid streaming url:" + extra);
                break;
            case UNAUTHORIZED_STREAMING_URL:
                Log.e(TAG, "Unauthorized streaming url:" + extra);
                mLogContent += "Unauthorized Url\n";
                break;
        }


    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int
            oldTop, int oldRight, int oldBottom) {
    }

    @Override
    public void notifyStreamStatusChanged(StreamingProfile.StreamStatus streamStatus) {
    }


    /**
     * 观众开启连麦功能
     */
    private void startLive(String liveVideo) {
        //隐藏观看第二主播布局，显示自己开启主播页面
        flPull.setVisibility(View.GONE);
        flPlug.setVisibility(View.VISIBLE);
        //初始化主播控件
        try {
            initLivePlay(liveVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private PopupWindow lianmai_request_pop;

    /**
     * 展示游客详情
     */
    public void shouLianmaiPop() {
        lianmai_request_pop = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.lianmai_request_pop, null);
        lianmai_request_pop.setContentView(view);
        lianmai_request_pop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        lianmai_request_pop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        lianmai_request_pop.setFocusable(true);
        lianmai_request_pop.setBackgroundDrawable(new BitmapDrawable());
        lianmai_request_pop.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        lianmai_request_pop.setAnimationStyle(R.style.mypopwindow_anim_style);
        lianmai_request_pop.showAtLocation(doubleAdd, Gravity.BOTTOM, 0, 0);
        lianmai_request_pop.update();
        lianmai_request_pop.setOnDismissListener(new RulePopOnDismissListner());

        AvatarView civ_image = (AvatarView) view.findViewById(R.id.civ_image);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_accept = (TextView) view.findViewById(R.id.tv_accept);
        TextView tv_refuse = (TextView) view.findViewById(R.id.tv_refuse);

        if (!TextUtils.isEmpty(liveListBean.getPicUrl())) {
            Picasso.with(mContext).load(liveListBean.getPicUrl()).placeholder(R.mipmap.head_default)
                    .error(R.mipmap.head_default).into(civ_image);
        }

        tv_name.setText(liveListBean.getNickName());

        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requstLianPush();
                lianmai_request_pop.dismiss();
            }
        });

        tv_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCustomNotificationForLive("miu_" + liveListBean.getId(), appUser.getId(),
                        appUser.getNickName(), appUser.getPicUrl(), appUser.getVip(), "观众拒绝了你的连麦请求", "2",
                        CustomNotificationType.IM_P2P_TYPE_SUBLIVE_PUBLIC, "2");
                lianmai_request_pop.dismiss();
            }
        });
    }


    /**
     * 可以同主播连麦请求推拉流信息
     *
     * @param
     */
    private void requstLianPush() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        httpDatas.getDataForJsoNoloading("主播连麦请求推拉流信息", Request.Method.GET, UrlBuilder.roomLive
                        (liveListBean.getRooms().getId() + "", appUser.getId()), map, myHandler,
                RequestCode.REQUEST_LIANMAI_LIVE);
    }


    /**
     * 获取连麦的内容
     *
     * @param
     */
    private void iSrequstLiveValid() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        httpDatas.getDataForJsoNoloading("是否有被连麦的观众", Request.Method.GET, UrlBuilder
                .roomLiveValid(liveListBean.getRooms().getId() + ""), map, myHandler, RequestCode
                .IS_REQUEST_LIANMAI_LIVE);
    }


    /**
     * 主播退出连线，关闭推流窗口
     *
     * @param
     */
    private void roomLiveExit() {
        tv_lianmai.setVisibility(View.GONE);
        hideLianmaiView();
        if (mWl != null) {
            try {
                mWl.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            mHandler.removeCallbacksAndMessages(null);
            mMediaStreamingManager.pause();
        }
//        if (null != mMediaStreamingManager) {
//            mMediaStreamingManager.destroy();
//        }
        if (null != cameraPreviewFrameView) {
            cameraPreviewFrameView.getHolder().getSurface().release();
            cameraPreviewFrameView.setVisibility(View.GONE);
            flPull.setVisibility(View.GONE);
            flPlug.setVisibility(View.GONE);
        }

        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        httpDatas.getDataForJsoNoloading("观众退出连线", Request.Method.GET, UrlBuilder.roomLiveExit
                        (liveListBean.getRooms().getId() + "", appUser.getId()), map, myHandler,
                RequestCode.ROOMLIVEEXIT);
    }

    // *********************************** 连麦内容结束 ********************************** //


    /**
     * @dw 获取礼物列表
     */
    private void getGiftList() {
        httpDatas.getNewDataCharServerCode("礼物列表", Request.Method.GET, UrlBuilder.GET_GIFT, null,
                myHandler, RequestCode.GET_GIFT);
    }


    /**
     * 请求主播信息
     */
    private void initUser() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", liveListBean.getId() + "");
        httpDatas.getNewDataCharServerCodeNoLoading("查询用户信息", Request.Method
                .POST, UrlBuilder.selectUserInfo, map, myHandler, RequestCode.SELECT_USER);
    }

    /**
     * 请求主播信息
     */
    private void initMyInfo() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId() + "");
        httpDatas.getNewDataCharServerCodeNoLoading("查询用户信息", Request.Method
                .POST, UrlBuilder.selectUserInfo, map, myHandler, RequestCode.SELECT_USER_MY);
    }

    /**
     * 获取用户信息
     *
     * @param details     打印接口信息
     * @param liveid      查询用户的id
     * @param handlerCode
     */
    private void ifattention(String details, String liveid, int handlerCode) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("currentUserId", appUser.getId());
        map.put("userId", liveid);
        httpDatas.getNewDataCharServerNoLoading(details, Request.Method.POST, UrlBuilder.IF_ATTENTION,
                map, myHandler, handlerCode);
    }


    /**
     * 展示游客详情
     *
     * @param customdateBean
     */
    public void showDialogForCallOther(final CustomdateBean customdateBean) {
        final PopupWindow otherPop = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_video_room, null);
        otherPop.setContentView(view);
        otherPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        otherPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        otherPop.setFocusable(true);
        otherPop.setBackgroundDrawable(new BitmapDrawable());
        otherPop.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        otherPop.setAnimationStyle(R.style.mypopwindow_anim_style);
        otherPop.showAtLocation(doubleAdd, Gravity.BOTTOM, 0, 0);
        otherPop.update();
        otherPop.setOnDismissListener(new RulePopOnDismissListner());

        AvatarView civ_image = (AvatarView) view.findViewById(R.id.civ_image);
        LinearLayout buttom_layout = (LinearLayout) view.findViewById(R.id.buttom_layout);
        TextView mySelf = (TextView) view.findViewById(R.id.mySelf);
        final ImageView iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
        ImageView iv_grade = (ImageView) view.findViewById(R.id.iv_grade);
        TextView tvID = (TextView) view.findViewById(R.id.tv_id);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_sign = (TextView) view.findViewById(R.id.tv_sign);
        TextView tv_focus_num = (TextView) view.findViewById(R.id.tv_focus_num);
        TextView tv_funs_num = (TextView) view.findViewById(R.id.tv_funs_num);
        TextView tv_send_num = (TextView) view.findViewById(R.id.tv_send_num);
        TextView tv_gold_coin = (TextView) view.findViewById(R.id.tv_gold_coin);
        TextView tv_changkong = (TextView) view.findViewById(R.id.tv_changkong);
        TextView tv_reply = (TextView) view.findViewById(R.id.tv_reply);
        LinearLayout ll_report = (LinearLayout) view.findViewById(R.id.ll_report);
        LinearLayout ll_banned = (LinearLayout) view.findViewById(R.id.ll_banned);
        View line = view.findViewById(R.id.line);

        if (!TextUtils.isEmpty(customdateBean.getId()) && customdateBean.getId().equals(appUser
                .getId())) {
            buttom_layout.setVisibility(View.GONE);
            ll_banned.setVisibility(View.INVISIBLE);
            ll_report.setVisibility(View.INVISIBLE);
            mySelf.setVisibility(View.VISIBLE);
        } else {
            buttom_layout.setVisibility(View.VISIBLE);
            ll_banned.setVisibility(View.INVISIBLE);
            ll_report.setVisibility(View.VISIBLE);
            tv_changkong.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            mySelf.setVisibility(View.GONE);
        }


        if (customdateBean.getGender().equals("0")) {
            iv_sex.setImageResource(R.mipmap.female);
        } else {
            iv_sex.setImageResource(R.mipmap.male);
        }

        tvID.setText("乐檬号:" + customdateBean.getId());

        if (!TextUtils.isEmpty(customdateBean.getPicUrl())) {
            Picasso.with(mContext).load(customdateBean.getPicUrl()).placeholder(R.mipmap.head_default)
                    .error(R.mipmap.head_default).into(civ_image);
        }
        civ_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                intent.putExtra(getString(R.string.visiti_person), customdateBean.getId());
                startActivity(intent);
                otherPop.dismiss();
            }
        });

        iv_grade.setImageResource(GrademipmapUtils.LevelImg[Integer.valueOf(customdateBean
                .getLevel()) - 1]);
        tv_name.setText(customdateBean.getNickName());

        //签名
        if (TextUtils.isEmpty(customdateBean.getSignature())) {
            tv_sign.setText("这个家伙很懒，什么都没留下");
        } else {
            tv_sign.setText(customdateBean.getSignature());
        }

        tv_focus_num.setText(customdateBean.getFollowNum());
        tv_funs_num.setText(customdateBean.getFansNum());

        String spendGoldCoin = customdateBean.getSpendGoldCoin();
        spendGoldCoin = CountUtils.getCount(Long.parseLong(spendGoldCoin));
        tv_send_num.setText(spendGoldCoin);

        String receivedGoldCoin = customdateBean.getReceivedGoldCoin();
        receivedGoldCoin = CountUtils.getCount(Long.parseLong(receivedGoldCoin));
        tv_gold_coin.setText(receivedGoldCoin);


        focus((TextView) view.findViewById(R.id.tv_foucs), customdateBean);
        //关注
        view.findViewById(R.id.tv_foucs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFollow((TextView) view.findViewById(R.id.tv_foucs), customdateBean);
            }
        });

        //私信
        view.findViewById(R.id.tv_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Extras.EXTRA_ACCOUNT, "miu_" + customdateBean.getId());
                intent.putExtra("SESSION_NAME", customdateBean.getNickName());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Bundle arguments = intent.getExtras();
                arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
                liveMessageFragment = new LiveMessageFragment();
                liveMessageFragment.init(getSupportFragmentManager());
                liveMessageFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id
                        .watch_room_message_fragment_chat, liveMessageFragment).setTransition
                        (FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                otherPop.dismiss();
            }

        });

        //回复
        tv_reply.setText("@TA");
        tv_reply.findViewById(R.id.tv_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageFragment.showEditText();
                EditText editContent = (EditText) findViewById(com.netease.nim.uikit.R.id
                        .editTextMessage);
                String str = "回复" + customdateBean.getNickName() + ":";
                editContent.setText(str);
                editContent.setSelection(str.length());
                otherPop.dismiss();
            }
        });

        //举报
        ll_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherPop.dismiss();
                reportDialog(customdateBean.getId());
            }
        });

        //我的主页
        mySelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                intent.putExtra(getString(R.string.visiti_person), customdateBean.getId());
                startActivity(intent);
                otherPop.dismiss();
            }
        });

        //我的场控
        tv_changkong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("暂缓开通");
                otherPop.dismiss();
            }
        });
    }


    /**
     * 展示守护信息
     *
     * @author sll
     * @time 2016/11/25 9:40
     */
    public void showDialogForCallOther() {
        final View view = View.inflate(this, R.layout.dialog_guard_room, null);
        final RelativeLayout rl_guard18 = (RelativeLayout) view.findViewById(R.id.rl_guard18);
        final RelativeLayout rl_guard68 = (RelativeLayout) view.findViewById(R.id.rl_guard68);
        final RelativeLayout rl_guard128 = (RelativeLayout) view.findViewById(R.id.rl_guard128);
        TextView tv_become_guard = (TextView) view.findViewById(R.id.tv_become_guard);
        AvatarView civ_image = (AvatarView) view.findViewById(R.id.civ_image);
        civ_image.setAvatarUrl(liveListBean.getPicUrl());
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name.setText(liveListBean.getNickName());

        rl_guard18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_guard18.setBackgroundResource(R.drawable.gift_tv_bg_main);
                rl_guard68.setBackgroundResource(R.drawable.gift_tv_bg);
                rl_guard128.setBackgroundResource(R.drawable.gift_tv_bg);
            }
        });
        rl_guard68.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_guard18.setBackgroundResource(R.drawable.gift_tv_bg);
                rl_guard68.setBackgroundResource(R.drawable.gift_tv_bg_main);
                rl_guard128.setBackgroundResource(R.drawable.gift_tv_bg);

            }
        });
        rl_guard128.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_guard18.setBackgroundResource(R.drawable.gift_tv_bg);
                rl_guard68.setBackgroundResource(R.drawable.gift_tv_bg);
                rl_guard128.setBackgroundResource(R.drawable.gift_tv_bg_main);
            }
        });
        //成为守护
        tv_become_guard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("暂缓开通");
            }
        });
        dialogForSelect.setCanceledOnTouchOutside(true);
        dialogForSelect.setContentView(view);
        dialogForSelect.show();
    }


    /**
     * 举报
     *
     * @author sll
     * @time 2016/12/24 16:01
     */
    private void reportDialog(final String userId) {
        final Dialog dialog = new Dialog(this, R.style.homedialog);
        final View view = View.inflate(this, R.layout.dialog_join_secret_pwd, null);
        final EditText pwdEdit = (EditText) view.findViewById(R.id.join_secret_pwd_edit);
        final TextView promptText = (TextView) view.findViewById(R.id.dialog_prompt_text);
        promptText.setVisibility(View.GONE);
        pwdEdit.setHint("请输入举报内容");
        //取消
        view.findViewById(R.id.join_secret_pwd_cancel).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(pwdEdit, mContext);
                dialog.dismiss();
            }
        });
        //确定
        view.findViewById(R.id.join_secret_pwd_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pwdEdit.getText().toString())) {
                    showToast("请输入举报内容");
                }
                report(userId, pwdEdit.getText().toString());
                KeyBoardUtils.closeKeybord(pwdEdit, mContext);
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 举报
     * reportUserId 自己的id
     * userId 被举报人的id
     * content 举报内容
     *
     * @author sll
     * @time 2016/12/24 15:54
     */
    private void report(String userId, String content) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("reportUserId", appUser.getId());
        map.put("userId", userId);
        map.put("content", content);
        httpDatas.getDataForJsoNoloading("举报", Request.Method.POST, UrlBuilder.report, map,
                myHandler, RequestCode.REQUEST_REPORT);

    }

    /**
     * 关注(取消关注)
     */
    private void changeFollow(final TextView tvFoucs, final CustomdateBean customdateBean) {
        final String follow = customdateBean.getFollow();
        Map<String, String> params = new HashMap<>();
        params.put("userId", appUser.getId());
        params.put("followUserId", customdateBean.getId());
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
                    customdateBean.setFollow("0");
                } else {
                    customdateBean.setFollow("1");
                }
                focus(tvFoucs, customdateBean);
                if (customdateBean.getId().equals(liveListBean.getId() + "")) {
                    if (TextUtils.equals(follow, "1")) {
                        btnAttention.setVisibility(View.VISIBLE);
                    } else {
                        btnAttention.setVisibility(View.GONE);
                        Map<String, Object> map0 = new HashMap<>();
                        map0.put("level", appUser.getLevel());
                        SendRoomMessageUtils.onCustomMessageGuanzhu("199", messageFragment, liveListBean.getRooms()
                                .getRoomId() + "", map0);
                    }
                }
            }
        });
    }


    /**
     * 显示关注(未关注)
     */
    private void focus(TextView tvFouce, CustomdateBean bean) {
        String follow = bean.getFollow();
        if (TextUtils.equals("0", follow)) {
            tvFouce.setText("+ 关注");
            tvFouce.setTextColor(getResources().getColor(R.color.main));
        } else if (TextUtils.equals("1", follow)) {
            tvFouce.setText("已关注");
            tvFouce.setTextColor(getResources().getColor(R.color.line_bf));
        }
    }


    /**
     * 进入直播间
     *
     * @author sll
     * @time 2016/12/16 17:14
     */
    private void join() {
        String url = UrlBuilder.chargeServerUrl + UrlBuilder.join;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("roomId", liveListBean.getRooms().getId() + "");
        hashMap.put("userId", appUser.getId());
        String json = new JSONObject(hashMap).toString();
        LogUtils.e("Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                    }
                });
    }


    /**
     * 点赞
     */
    protected void showLit() {
        ThreadManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
//                int[] heartImg = new int[]{R.mipmap.plane_heart_cyan, R.mipmap
// .plane_heart_pink, R.mipmap.plane_heart_red, R.mipmap.plane_heart_yellow};
                int[] heartImg = new int[]{R.mipmap.plane_heart_1, R.mipmap.plane_heart_2, R
                        .mipmap.plane_heart_3, R.mipmap.plane_heart_4, R.mipmap.plane_heart_5, R
                        .mipmap.plane_heart_6, R.mipmap.plane_heart_7, R.mipmap.plane_heart_8, R
                        .mipmap.plane_heart_9};
                final Random ran = new Random();
                final ImageView heart = new ImageView(WatchLiveActivity.this);
                //点亮的背景图片
                heart.setBackgroundResource(heartImg[ran.nextInt(9)]);
                //尺寸参数
                final int screenW = getWindowManager().getDefaultDisplay().getWidth();

                heart.setLayoutParams(new AutoRelativeLayout.LayoutParams(screenW / 10, screenW /
                        10));

                //x位置
                heart.setX(screenW - screenW / 4);
                //y位置
                heart.setY(getWindowManager().getDefaultDisplay().getHeight() - getWindowManager
                        ().getDefaultDisplay().getHeight() / 8);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRoot.addView(heart);
                        //点亮xy 平移动画
                        ObjectAnimator translationX = ObjectAnimator.ofFloat(heart,
                                "translationX", ran.nextInt(500) + (screenW - 200) - (screenW / 3));
                        ObjectAnimator translationY = ObjectAnimator.ofFloat(heart,
                                "translationY", ran.nextInt(getWindowManager().getDefaultDisplay
                                        ().getHeight() / 2) + 200);
                        //渐变动画
                        ObjectAnimator alpha = ObjectAnimator.ofFloat(heart, "alpha", 0f);
                        //xy放大动画
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(heart, "scaleX", 0.8f, 1f);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(heart, "scaleY", 0.8f, 1f);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(translationX, translationY, alpha, scaleX, scaleY);
                        set.setDuration(5000);
                        set.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (null != mRoot) {
                                    mRoot.removeView(heart);
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        set.start();
                    }
                });
            }
        });
    }


    /**
     * 退出直播间Dialog
     *
     * @param str dialog上显示的文案
     */
    private void initQuitDialog(String str) {
        View view = getLayoutInflater().inflate(R.layout.dialog_quit_login, null);
        mQuitDialog = new RoundDialog(this, view, R.style.dialog, 0.66f, 0.2f);
//        mQuitDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        mQuitDialog.setCanceledOnTouchOutside(false);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvTitle.setText(str);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuitDialog != null && mQuitDialog.isShowing()) {
                    mQuitDialog.dismiss();
                }
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuitDialog != null && mQuitDialog.isShowing()) {
                    mQuitDialog.dismiss();
                }
                videoPlayEnd();
            }
        });
//        mQuitDialog.show();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("WatchLive Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    //直播结束释放资源
    public void videoPlayEnd() {
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (null != anchorVideo) {
            anchorVideo.release();
            anchorVideo = null;
        }
        if (null != mMediaStreamingManager) {
            mMediaStreamingManager.destroy();
        }

        for (Activity activity : MyApplication.listActivity) {
            if (activity instanceof WatchLiveActivity) {
                activity.finish();
            }
        }
    }


    /**
     * 规则pop
     */
    public void getRulePopup() {
        View view = getLayoutInflater().inflate(R.layout.pop_rule_web, null);
        dialogForSelect.setCanceledOnTouchOutside(true);
        dialogForSelect.setContentView(view);
        dialogForSelect.show();
        ImageView colse_trend = (ImageView) view.findViewById(R.id.colse_trend);
        colse_trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForSelect.dismiss();
            }
        });

        final WebView webView = (WebView) view.findViewById(R.id.webView);
        final ProgressBar iv_include_loading = (ProgressBar) view.findViewById(R.id.iv_include_loading);
        WebSettings webSettings = webView.getSettings();
        webSetting(webSettings);
        webView.loadUrl("http://47.88.229.22:8080/protocol/rule.html");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                iv_include_loading.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                iv_include_loading.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("shouldOverrideUrlLoading", "url = " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    /**
     * RulePopupWindow Dismiss监听
     */
    private class RulePopOnDismissListner implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }


    private void showTrendPop() {
        String url = "";
        switch (liveListBean.getRooms().getRoomsType()){
            case "1":
                url = "http://47.88.229.22:8080/lucky/trend.html";
                break;
            case "2":
                url = "http://60.205.114.36:8080/cow/cowResult.html?roomId="+String.valueOf(liveListBean.getRoomId());
                break;
        }
        View view = getLayoutInflater().inflate(R.layout.pop_trend, null);
        dialogForSelect.setCanceledOnTouchOutside(true);
        dialogForSelect.setContentView(view);
        dialogForSelect.show();
        ImageView colse_trend = (ImageView) view.findViewById(R.id.colse_trend);
        colse_trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForSelect.dismiss();
            }
        });
        final WebView webView = (WebView) view.findViewById(R.id.webView);
        final ProgressBar iv_include_loading = (ProgressBar) view.findViewById(R.id.iv_include_loading);
        WebSettings webSettings = webView.getSettings();
        webSetting(webSettings);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                iv_include_loading.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                iv_include_loading.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("shouldOverrideUrlLoading", "url = " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void webSetting(WebSettings webSettings) {
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true);// 是否可以缩放，默认true
//        webSettings.setBuiltInZoomControls(true);// 是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
//        webSettings.setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true);// 是否使用缓存
        webSettings.setDomStorageEnabled(true);// DOM Storage
    }


    private void showTouZhuPop(String selectStatus, int jbNumber, int tzNumber) {
        final PopupWindow rulePop = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.pop_tou_zhu, null);
        rulePop.setContentView(view);
        rulePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        rulePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        rulePop.setFocusable(true);
        rulePop.setBackgroundDrawable(new BitmapDrawable());
        rulePop.setOutsideTouchable(true);

        backgroundAlpha(0.5f);

        rulePop.showAtLocation(doubleAdd, Gravity.CENTER, 0, 0);
        rulePop.update();
        rulePop.setOnDismissListener(new RulePopOnDismissListner());
        TextView tv_tzqh = (TextView) view.findViewById(R.id.tv_tzqh);//投注期号
        TextView tv_ds = (TextView) view.findViewById(R.id.tv_ds);  //大小单双
        TextView tv_xzjf = (TextView) view.findViewById(R.id.tv_xzjf);  //下注积分
        ImageView sure_tz = (ImageView) view.findViewById(R.id.sure_tz);  //确定投注
        ImageView colse_rule = (ImageView) view.findViewById(R.id.colse_rule);  //关闭弹框

        tv_ds.setText("大小单双：" + selectStatus);
        strJinBi = String.valueOf(jbNumber * tzNumber);
        tv_xzjf.setText("投注乐票：" + strJinBi);
        intQh = Integer.valueOf(nper) + 1;
        tv_tzqh.setText("投注期号：" + intQh);

        colse_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulePop.dismiss();
            }
        });
        sure_tz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myGoldCoin < Long.parseLong(strJinBi)) {
                    showToast("您的乐票不足,请充值");
                    rulePop.dismiss();
                } else {
                    sureTz(rulePop);
                }
            }
        });
    }

    private void sureTz(final PopupWindow rulePop) {
        String url = UrlBuilder.chargeServerUrl + UrlBuilder.reciveAmount;

        OkHttpUtils.get().url(url)
                .addParams("userId", appUser.getId())
                .addParams("roomId", liveListBean.getRooms().getId() + "")
                .addParams("periods", intQh + "")
                .addParams("amount", tzNumber + "")
                .addParams("acountTimes", jbNumber + "")
                .addParams("gameType", "1")
                .addParams("buyType", selectStatus)
                .addParams("countryType", countryType).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("response :" + response);
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        if (code.equals("1")) {
                            showToast("投注成功");
                            /**
                             * 设置游戏布局的金币数量
                             */
                            myGoldCoin = myGoldCoin - Long.parseLong(strJinBi);
                            String myCoin = CountUtils.getCount(myGoldCoin);
                            all_lepiao.setText(myCoin);
                            rulePop.dismiss();

                            Map<String, Object> map = new HashMap<>();
                            map.put("vip", appUser.getVip());
                            map.put("userId", appUser.getId());
                            map.put("level", appUser.getLevel());
                            map.put("inputMsg", intQh + "期 " + selectStatus + " 投注" + strJinBi + "乐票");
                            SendRoomMessageUtils.onCustomMessagePlay("1818", messageFragment, liveListBean.getRooms()
                                    .getRoomId() + "", map);
                        } else {
                            showToast("投注失败");
                            rulePop.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * @dw 获取上期开奖数据
     */
    private void getTimenumber() {
        String url = UrlBuilder.chargeServerUrl + UrlBuilder.getTimenumber + "?userId=" + appUser.getId();
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {


            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {
                showToast("网络错误");
            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("response :" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        String obj = jsonObject.getString("obj");
                        LogUtil.e("obj", obj);
                        LastAwardBean lastAwardBean = JsonUtil.json2Bean(obj, LastAwardBean.class);
                        if (lastAwardBean != null) {
                            isTouZhu = true;
                            nper = lastAwardBean.getNper();
                            countryType = lastAwardBean.getCountryType();
                            tv_periods.setText("第" + lastAwardBean.getNper() + "期");
                            frist_num.setText(lastAwardBean.getFirstNum() + "");
                            second_num.setText(lastAwardBean.getSecondNum() + "");
                            third_num.setText(lastAwardBean.getThirdNum() + "");
                            all_num.setText(lastAwardBean.getSum() + "");
                            tv_ds.setText(lastAwardBean.getType());

                            long now = System.currentTimeMillis();
                            mCountDownTotalTime = Long.parseLong(lastAwardBean.getDateLine()) - now;

                            if (mCountDownTotalTime < 0) {
                                tv_game_next_open_time.setText("等待:开奖:中.");
                                myHandler.postDelayed(timenNumber, 30000);
                            } else {
                                myHandler.sendEmptyMessage(10000);
                            }

                            if (lastAwardBean.getWinStatus().equals("1")) {
                                getZhonaJiangTZ(lastAwardBean.getNper(), lastAwardBean.getWinAmountAll(), "1");

                                /**
                                 * 设置游戏布局的金币数量
                                 */
                                myGoldCoin = myGoldCoin + Long.parseLong(lastAwardBean.getWinAmountAll());
                                String myCoin = CountUtils.getCount(myGoldCoin);
                                all_lepiao.setText(myCoin);
                            } else if (lastAwardBean.getWinStatus().equals("0")) {
                                getZhonaJiangTZ(lastAwardBean.getNper(), lastAwardBean.getWinAmountAll(), "0");
                            }

                        }
                    } else if (code.equals("1")) {
                        isTouZhu = false;
                        String obj = jsonObject.getString("obj");
                        LogUtil.e("obj", obj);
                        LastAwardBean lastAwardBean = JsonUtil.json2Bean(obj, LastAwardBean.class);
                        if (lastAwardBean != null) {
                            nper = lastAwardBean.getNper();
                            countryType = lastAwardBean.getCountryType();
                            tv_periods.setText("第" + lastAwardBean.getNper() + "期");
                            frist_num.setText(lastAwardBean.getFirstNum() + "");
                            second_num.setText(lastAwardBean.getSecondNum() + "");
                            third_num.setText(lastAwardBean.getThirdNum() + "");
                            all_num.setText(lastAwardBean.getSum() + "");
                            tv_ds.setText(lastAwardBean.getType());
                            tv_game_next_open_time.setText("等待:开奖:中.");

                            myHandler.postDelayed(timenNumber, 30000);
                        }
                    } else {
                        isTouZhu = false;
                        tv_game_next_open_time.setText("等待:开奖:中.");
                        myHandler.postDelayed(timenNumber, 30000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Runnable timenNumber = new Runnable() {
        @Override
        public void run() {
            getTimenumber();
        }
    };

    private void getZhonaJiangTZ(String nper, String winAmountAll, String type) {
        initDialog();
        String content = "";
        if (type.equals("1")) {
            content = "提示" + "\n" + "\n" + "您在" + nper + "期中,获得乐票" + winAmountAll;
        } else {
            content = "提示" + "\n" + "\n" + "您在" + nper + "期中未中奖";
        }
        baseDialogTitle.setText(content);
        baseDialogLeft.setVisibility(View.GONE);
        baseDialogLine.setVisibility(View.GONE);
        baseDialogRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseDialog.dismiss();
            }
        });
    }

    private void showLianmaiView() {
        liveLianmaiHead.setVisibility(View.VISIBLE);
        liveLianmaiHeadBg1.setVisibility(View.VISIBLE);
        liveLianmaiHeadBg2.setVisibility(View.VISIBLE);
    }

    private void hideLianmaiView() {
        liveLianmaiHead.setVisibility(View.GONE);
        liveLianmaiHeadBg1.setVisibility(View.GONE);
        liveLianmaiHeadBg2.setVisibility(View.GONE);
        customLianmaiBean = null;
    }
}