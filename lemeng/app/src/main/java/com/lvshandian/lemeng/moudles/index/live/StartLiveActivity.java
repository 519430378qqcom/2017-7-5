package com.lvshandian.lemeng.moudles.index.live;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.adapter.FamilyMemberAdapter;
import com.lvshandian.lemeng.adapter.GridViewAdapter;
import com.lvshandian.lemeng.adapter.ViewPageGridViewAdapter;
import com.lvshandian.lemeng.base.BarrageDateBean;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.base.CustomStringCallBack;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.bean.ControllerBean;
import com.lvshandian.lemeng.bean.CreatReadyBean;
import com.lvshandian.lemeng.bean.CustomGiftBean;
import com.lvshandian.lemeng.bean.CustomLianmaiBean;
import com.lvshandian.lemeng.bean.CustomdateBean;
import com.lvshandian.lemeng.bean.DianBoDateBean;
import com.lvshandian.lemeng.bean.GiftBean;
import com.lvshandian.lemeng.bean.LianMaiDateBean;
import com.lvshandian.lemeng.bean.LianTongzhiBean;
import com.lvshandian.lemeng.bean.LiveEven;
import com.lvshandian.lemeng.bean.LiveFamilyMemberBean;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.bean.RoomUserBean;
import com.lvshandian.lemeng.bean.RoomUserDataBean;
import com.lvshandian.lemeng.gles.FBO;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.NewSdkHttpResult;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.httprequest.SdkHttpResultSuccess;
import com.lvshandian.lemeng.moudles.index.CustomNotificationType;
import com.lvshandian.lemeng.moudles.index.live.gift.GiftFrameLayout;
import com.lvshandian.lemeng.moudles.index.live.gift.GiftSendModel;
import com.lvshandian.lemeng.moudles.index.live.utils.AnchorVideoOne;
import com.lvshandian.lemeng.moudles.mine.activity.ExplainWebViewActivity;
import com.lvshandian.lemeng.moudles.mine.my.ContributionActivity;
import com.lvshandian.lemeng.moudles.mine.my.OtherPersonHomePageActivity;
import com.lvshandian.lemeng.moudles.mine.my.adapter.ControllerBaseAdapter;
import com.lvshandian.lemeng.moudles.mine.my.adapter.OnItemClickListener;
import com.lvshandian.lemeng.moudles.mine.my.adapter.RoomUsersDataAdapter;
import com.lvshandian.lemeng.service.VoiceService;
import com.lvshandian.lemeng.utils.AnimationUtils;
import com.lvshandian.lemeng.utils.ChannelToLiveBean;
import com.lvshandian.lemeng.utils.Config;
import com.lvshandian.lemeng.utils.CountUtils;
import com.lvshandian.lemeng.utils.DESUtil;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.lvshandian.lemeng.utils.JavaBeanMapUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.QosThread;
import com.lvshandian.lemeng.utils.SendRoomMessageUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.ThreadManager;
import com.lvshandian.lemeng.utils.ToastUtils;
import com.lvshandian.lemeng.utils.UMUtils;
import com.lvshandian.lemeng.view.BarrageView;
import com.lvshandian.lemeng.view.RotateLayout;
import com.lvshandian.lemeng.view.RoundDialog;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment.ChatRoomMessageFragment;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment.ChatRoomMessageFragmentGift;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.helper.ChatRoomMemberCache;
import com.lvshandian.lemeng.wangyiyunxin.live.fragment.ChatRoomSessionListFragment;
import com.lvshandian.lemeng.wangyiyunxin.live.fragment.LiveMessageFragment;
import com.lvshandian.lemeng.wangyiyunxin.main.helper.SystemMessageUnreadManager;
import com.lvshandian.lemeng.wangyiyunxin.main.reminder.ReminderItem;
import com.lvshandian.lemeng.wangyiyunxin.main.reminder.ReminderManager;
import com.lvshandian.lemeng.wangyiyunxin.main.reminder.ReminderSettings;
import com.lvshandian.lemeng.widget.AvatarView;
import com.lvshandian.lemeng.widget.lrcview.LrcView;
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
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.AudioSourceCallback;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.FrameCapturedCallback;
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
import com.squareup.okhttp.Request;
import com.tandong.bottomview.view.BottomView;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.lvshandian.lemeng.service.VoiceService.mediaPlayer;

/**
 * Created by Administrator on 2016/11/21.
 */

public class StartLiveActivity extends BaseActivity implements
        ReminderManager.UnreadNumChangedCallback,
        View.OnLayoutChangeListener,
        StreamStatusCallback,
        StreamingPreviewCallback,
        SurfaceTextureCallback,
        AudioSourceCallback,
        CameraPreviewFrameView.Listener,
        StreamingSessionListener,
        StreamingStateChangedListener, com.lvshandian.lemeng.view.CameraPreviewFrameView
        .Listener, MediaPlayer.OnCompletionListener {
    @Bind(R.id.live_head)
    AvatarView liveHead;
    @Bind(R.id.live_name)
    TextView liveName;
    @Bind(R.id.live_num)
    TextView liveNum;
    @Bind(R.id.live_jinpiao)
    TextView liveJinpiao;
    @Bind(R.id.ll_tp_labe)
    AutoLinearLayout llTangpiao;
    @Bind(R.id.live_id)
    TextView liveId;
    @Bind(R.id.room_id)
    TextView roomShowId;
    @Bind(R.id.live_close)
    ImageView liveClose;
    @Bind(R.id.gift_layout1)
    GiftFrameLayout giftFrameLayout1;
    @Bind(R.id.gift_layout2)
    GiftFrameLayout giftFrameLayout2;
    @Bind(R.id.iv_live_mei)
    ImageView ivLiveMei;
    @Bind(R.id.iv_live_privatechat)
    ImageView ivLivePrivatechat;
    @Bind(R.id.iv_live_switch)
    ImageView ivLiveSwitch;
    @Bind(R.id.watch_room_message)
    AutoFrameLayout watchRoomMessage;
    @Bind(R.id.rl_live_root)
    AutoRelativeLayout mRoot;
    @Bind(R.id.rlv_list_live_audiences)
    RecyclerView rlvListLiveAudiences;
    @Bind(R.id.tab_new_indicator)
    ImageView tabNewIndicator;
    @Bind(R.id.tab_new_msg_label)
    DropFake tabNewMsgLabel;
    @Bind(R.id.video_lian)
    SurfaceView mVideoSurfaceView;
    @Bind(R.id.lm_fm)
    AutoFrameLayout lmFm;
    @Bind(R.id.small_coles)
    ImageView smallColes;
    @Bind(R.id.barrageview)
    BarrageView barrageview;
    @Bind(R.id.iv_live_gift)
    ImageView ivLiveGift;
    @Bind(R.id.start_room_jaiZu)
    ImageView startRoomJaiZu;
    @Bind(R.id.iv_show_send_gift_lian)
    RelativeLayout mSendGiftLian;
    @Bind(R.id.btn_timer)
    TextView btn_timer;
    @Bind(R.id.game_more_btn)
    ImageView game_more_btn;
    @Bind(R.id.game)
    ImageView game;
    @Bind(R.id.ruanjianpan)
    ImageView ruanjianpan;
    @Bind(R.id.zhoubang)
    ImageView zhoubang;
    @Bind(R.id.ll_game)
    LinearLayout ll_game;
    @Bind(R.id.ll_buttom_mun)
    RelativeLayout ll_buttom_mun;
    @Bind(R.id.watch_room_message_fragment_parent)
    AutoFrameLayout watch_room_message_fragment_parent;
    @Bind(R.id.iv_xy)
    ImageView iv_xy;
    @Bind(R.id.live_game)
    AutoLinearLayout live_game;
    public static LrcView mLrcView;
    private static final String TAG = "StartLiveActivity";
    @Bind(R.id.song_LrcView)
    LrcView songLrcView;
    @Bind(R.id.iv_big)
    ImageView ivBig;
    @Bind(R.id.tv_big)
    TextView tvBig;
    @Bind(R.id.iv_samll)
    ImageView ivSamll;
    @Bind(R.id.tv_samll)
    TextView tvSamll;
    @Bind(R.id.iv_singe)
    ImageView ivSinge;
    @Bind(R.id.tv_singe)
    TextView tvSinge;
    @Bind(R.id.iv_double)
    ImageView ivDouble;
    @Bind(R.id.tv_double)
    TextView tvDouble;
    @Bind(R.id.iv_big_sigle)
    ImageView ivBigSigle;
    @Bind(R.id.tv_big_sigle)
    TextView tvBigSigle;
    @Bind(R.id.iv_samll_singe)
    ImageView ivSamllSinge;
    @Bind(R.id.tv_samll_singe)
    TextView tvSamllSinge;
    @Bind(R.id.iv_big_double)
    ImageView ivBigDouble;
    @Bind(R.id.tv_big_double)
    TextView tvBigDouble;
    @Bind(R.id.iv_samll_double)
    ImageView ivSamllDouble;
    @Bind(R.id.tv_samll_double)
    TextView tvSamllDouble;
    @Bind(R.id.iv_more_big)
    ImageView ivMoreBig;
    @Bind(R.id.tv_more_big)
    TextView tvMoreBig;
    @Bind(R.id.iv_more_samll)
    ImageView ivMoreSamll;
    @Bind(R.id.tv_more_samll)
    TextView tvMoreSamll;
    @Bind(R.id.small_add)
    ImageView smallAdd;
    @Bind(R.id.samll_number)
    TextView samllNumber;
    @Bind(R.id.small_subtract)
    ImageView smallSubtract;
    @Bind(R.id.double_subtract)
    ImageView doubleSubtract;
    @Bind(R.id.double_number)
    TextView doubleNumber;
    @Bind(R.id.double_add)
    ImageView doubleAdd;
    @Bind(R.id.iv_touzhu)
    ImageView ivTouzhu;
    @Bind(R.id.tv_rule)
    ImageView tv_rule;
    @Bind(R.id.iv_trend)
    ImageView iv_trend;
    @Bind(R.id.all_lepiao)
    TextView all_lepiao;

    private int tzNumber = 10;
    private int jbNumber = 1;

    private ArrayList<Integer> JbList = new ArrayList<>();
    /**
     * 分享的地址
     */
    private final String share_url = "http://app.haiyanlive.com/video/share.html";

    /**
     * 直播开启时间
     */
    private long startTime;

    /**
     * 计时器
     */
    private Timer timer = new Timer();

    /**
     * 网易云信生成的id
     */
    private String wy_Id = "";

    /**
     * 后台生成的id
     */
    private String room_Id = "";

    /**
     * 退出对话框
     */
    private RoundDialog mQuitDialog;

    /**
     * 网易云信消息
     */
    private ChatRoomMessage message;

    /**
     * 开直播时返回的实体类，包含所有主播信息
     */
    private CreatReadyBean creatReadyBean;

    /**
     * 根据头像列表个数,显示观看主播人数
     */
    private int liveOnLineNums;

    /**
     * 游客信息Dialog
     */
    private Dialog dialogForSelect;

    /**
     * 头像列表页数
     */
    private int page = 1;

    /**
     * 是否是刷新的标记
     */
    private boolean isRefresh = true;

    /**
     * 头像数据内容
     */
    private List<RoomUserBean> mDatas = new ArrayList<>();

    /**
     * 头像列表适配器
     */
    private RoomUsersDataAdapter mAdapter;

    /**
     * RecyclerView布局管理器
     */
    private GridLayoutManager mLayoutManager;

    /**
     * 左下角消息模块显示非礼物消息布局
     */
    private ChatRoomMessageFragment messageFragment;

    /**
     * 左下角消息模块显示礼物消息布局
     */
    private ChatRoomMessageFragmentGift messageFragmentGift;

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
     * 记录禁言的人
     */
    private List<String> mutedList = new ArrayList<>();

    /**
     * 主播端连麦拉流
     */
    private QosThread mQosThread;

    /**
     * 主播观看第二主播
     */
    private AnchorVideoOne liveVideo;
    /**
     * 是否有连麦人返回的数据
     */
    private List<LiveEven> liveEven;
    /**
     * 连麦人的Id
     */
    private String lianId = null;
    /**
     * 连麦人的name
     */
    private String lianName = "";

    /**
     * ************************* 以下注释关于礼物 ********************************
     * <p>
     * /**
     * 界面用于显示礼物的ViewPager
     */
    private ViewPager mVpGiftView;

    /**
     * 礼物列表中的乐票数量
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
     * 接收礼物人的Id
     */
    private String toUserId = "";

    /**
     * 接收礼物人的名字
     */
    private String toUserName = "";

    /**
     * 发送礼物时，礼物的图片地址
     */
    private String staticIcon;

    /**
     * 礼物动画列表
     */
    private List<GiftSendModel> giftSendModelList = new ArrayList<GiftSendModel>();

    /**
     * 礼物连点计时器
     */
    private CountDownTimer giftTimer;

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
                    LogUtils.e("礼物列表长度=" + mGiftList.size() + "");
                    LogUtils.LogAll("礼物列表", mGiftList.toString());
                    break;
                case RequestCode.REQUEST_USER_INFO:
                    //请求用户信息
                    LogUtils.i(json.toString());
                    CustomdateBean customdateBean = JSON.parseObject(json, CustomdateBean.class);
                    showDialogForCallOther(customdateBean);
                    break;
                //主播获取全部申请(连麦)列表
                case RequestCode.REQUEST_ROOM_LIVE:
                    LogUtils.i(json.toString());
                    lianMaiDateList = JSON.parseArray(json, LianMaiDateBean.class);
                    break;
                case RequestCode.REQUEST_ROOM_CLOES:
                    LogUtils.i("主播关闭直播间" + json.toString());
                    finish();
                    startActivity(new Intent(StartLiveActivity.this, QuitLiveActivity.class).putExtra
                            ("roomId", room_Id).putExtra("startTime", startTime));
                    break;
                case RequestCode.TIMERLIVE:
                    LogUtils.i("主播隔一段时间刷新状态" + json.toString());
                    break;
                case 1000:
                    LogUtils.i("主播隔一段时间刷新状态");
                    httpDatas.getDataDialog("主播隔一段时间刷新状态", false, urlBuilder.TimerLive(room_Id),
                            myHandler, RequestCode.TIMERLIVE);
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
                        map.put("gift_item_message", "赠送了" + "1个" + mSendGiftItem.getName()
                                + "给" + toUserName);
                        map.put("vip", appUser.getVip());
                        map.put("userId", appUser.getId());
                        map.put("gift_Grand_Prix_number", null);
                        map.put("gift_Grand_Prix", null);
                        map.put("gift_giftCoinNumber", mSendGiftItem.getMemberConsume());
                        map.put("receveUserId", toUserId);
                        map.put("gift_Type1", mSendGiftItem.getWinFlag());
                        map.put("level", creatReadyBean.getCreator().getLevel());
                        map.put("RepeatGiftNumber", num);
                        SendRoomMessageUtils.onCustomMessageSendGift(messageFragmentGift, wy_Id + "",
                                map);

                        SharedPreferenceUtils.saveGoldCoin(mContext, (Long.parseLong
                                (SharedPreferenceUtils.getGoldCoin(mContext)) - Integer.valueOf
                                (mSendGiftItem.getGiftConsume())) + "");

                    }
                    break;
                //获取连麦人的Id
                case RequestCode.REQUEST_LIANMAI_ID:
                    LogUtils.i("请求接口成功，得到连麦人的" + json.toString());
                    //显示连麦人
                    if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(json.toString())) {
                        if (json.toString().length() > 4) {
                            liveEven = JsonUtil.json2BeanList(json.toString(), LiveEven.class);
                            liveEven.toString();
                            String id = liveEven.get(0).getId();
                            String status = liveEven.get(0).getStatus();
                            requstLianmai(id, status);
                        }
                    }
                    break;
                case RequestCode.ACCEPT_LIANMAI:
                    LogUtils.i("是否获取推拉流地址请求成功" + json.toString());
                    //请求连麦数据
                    if (!com.lvshandian.lemeng.utils.TextUtils.isEmpty(json.toString())) {
                        //如果不为空则发起连麦
//                        WatchLive(liveEven.getBroadcastUrl());
                        if (json.toString().equals("true")) {
                            LogUtils.i("是否获取推拉流地址请求成功" + json.toString());
                            //主播同意观众连线
                            sendCustomNotificationForLive(lianId, appUser.getId(), appUser.getNickName(), appUser.getPicUrl(),
                                    appUser.getVip(), "主播同意与您连麦", "2", CustomNotificationType.IM_P2P_TYPE_SUBLIVE_ACK, "1");
                        }
                    }

                    break;
                case RequestCode.SELECT_USER:
                    LogUtils.e("查询个人信息返回json: " + json);
                    AppUser userZhubo = JsonUtil.json2Bean(json, AppUser.class);

                    String receivedGoldCoin = userZhubo.getReceivedGoldCoin();
                    SharedPreferenceUtils.saveZhuboGoldCoin(mContext, receivedGoldCoin);
                    receivedGoldCoin = CountUtils.getCount(Long.parseLong(receivedGoldCoin));
                    liveJinpiao.setText(receivedGoldCoin); //显示主播乐票数量
                    break;
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_startlive;
    }

    @Override
    protected void initialized() {
        mLrcView = (LrcView) findViewById(R.id.song_LrcView);
        startTime = System.currentTimeMillis();
        startAnimation(3);
        creatReadyBean = (CreatReadyBean) getIntent().getSerializableExtra("START");
        //网易云信房间的id
        wy_Id = creatReadyBean.getRoomId();
        //后台生成房间的Id
        room_Id = creatReadyBean.getId();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(1000);
            }
        }, 30000, 30000);

        //获取礼物列表
        getGiftList();
        //进入房间
//        requestNet();
        PowerManager mPm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWl = mPm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MyTag");
        requestPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        try {
            initLivePlay(DESUtil.decrypt(creatReadyBean.getPublishUrl()));
            LogUtils.i("推流地址　：" + DESUtil.decrypt(creatReadyBean.getPublishUrl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        lmFm.setVisibility(View.GONE);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initPaly();
        liveHead.setAvatarUrl(creatReadyBean.getCreator().getPicUrl());
        if (creatReadyBean.getCreator().getNickName().length() > 4) {
            liveName.setText(creatReadyBean.getCreator().getNickName().substring(0, 4) + "...");
        } else {
            liveName.setText(creatReadyBean.getCreator().getNickName());
        }

        liveOnLineNums = Integer.parseInt(creatReadyBean.getOnlineUserNum());
        liveNum.setText(liveOnLineNums + "");

        liveId.setText("乐檬号:" + creatReadyBean.getCreator().getId());
//        roomShowId.setText("房间号:" + room_Id);
        initUser();
        SharedPreferenceUtils.put(this, "ZhuBoId", appUser.getId());
        SharedPreferenceUtils.put(this, "isZhuBo", true);
        registerReceiverStartLiveActivity();
        // 注册监听
        registerObservers(true);
        //注册监听自定义通知
        registerReceiveCustom(true);
        // 登录聊天室
        enterRoom();

        initRecycle();

        //网易云信消息未读数
        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();

        dialogForSelect = new Dialog(this, R.style.homedialog);
        initQuitDialog();

        samllNumber.setText(String.valueOf(tzNumber));
        doubleNumber.setText(String.valueOf(jbNumber));
        initSelectStatus();

        /**
         * 设置游戏布局的金币数量
         */
        String myCoin = SharedPreferenceUtils.getGoldCoin(mContext);
        myCoin = CountUtils.getCount(Long.parseLong(myCoin));
        all_lepiao.setText(myCoin);
    }

    private void initSelectStatus() {
        restStatus();
        ivBig.setImageResource(R.mipmap.icon_big_select);
        selectStatus = "大";
    }

    @Override
    protected void initListener() {
        iv_trend.setOnClickListener(this);
        tv_rule.setOnClickListener(this);
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
        llTangpiao.setOnClickListener(this);
        liveHead.setOnClickListener(this);
        game.setOnClickListener(this);
        ruanjianpan.setOnClickListener(this);
        zhoubang.setOnClickListener(this);
        ll_game.setOnClickListener(this);
        iv_xy.setOnClickListener(this);
        liveClose.setOnClickListener(this);
        ivLiveMei.setOnClickListener(this);
        ivLiveSwitch.setOnClickListener(this);
        ivLivePrivatechat.setOnClickListener(this);
        smallColes.setOnClickListener(this);
        ivLiveGift.setOnClickListener(this);
        startRoomJaiZu.setOnClickListener(this);
        roomShowId.setOnClickListener(this);
        game_more_btn.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RoomUserBean bean = mDatas.get(position);
                ifattention("请求用户信息", bean.getUserId(), RequestCode.REQUEST_USER_INFO);
            }
        });

    }

    private boolean gameIsStart = false;

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

        smallAdd.setOnClickListener(this);
        smallSubtract.setOnClickListener(this);
        doubleSubtract.setOnClickListener(this);
        doubleAdd.setOnClickListener(this);
        ivTouzhu.setOnClickListener(this);


        switch (v.getId()) {
            case R.id.iv_trend: //走势
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
                }else {
                    tzNumber = Integer.valueOf(strNumber)/2;
                    samllNumber.setText(String.valueOf(tzNumber));
                }

                break;
            case R.id.double_add:  //加倍投注加
                if (jbNumber == 1000){
                    return;
                }
                jbNumber = jbNumber*10;
                doubleNumber.setText(String.valueOf(jbNumber));
                break;
            case R.id.double_subtract: //加倍投注减

                String strdouNumber = doubleNumber.getText().toString();
                if (Integer.valueOf(strdouNumber) == 1) {
                    return;
                }else {
                    jbNumber = Integer.valueOf(strdouNumber)/10;
                    doubleNumber.setText(String.valueOf(jbNumber));
                }
                break;
            case R.id.iv_touzhu:  //投注
                showTouZhuPop(selectStatus,jbNumber,tzNumber);
                break;

            case R.id.iv_big: //大
                restStatus();
                ivBig.setImageResource(R.mipmap.icon_big_select);
                selectStatus = "大";
                break;
            case R.id.iv_samll: //小
                restStatus();
                ivSamll.setImageResource(R.mipmap.icon_small_select);
                selectStatus = "小";
                break;
            case R.id.iv_singe: //单
                restStatus();
                ivSinge.setImageResource(R.mipmap.icon_single_select);
                selectStatus = "单";
                break;
            case R.id.iv_double: //双
                restStatus();
                ivDouble.setImageResource(R.mipmap.icon_double_select);
                selectStatus = "双";
                break;
            case R.id.iv_big_sigle: //大单
                restStatus();
                ivBigSigle.setImageResource(R.mipmap.icon_big_single_select);
                selectStatus = "大单";
                break;
            case R.id.iv_samll_singe: //小单
                restStatus();
                ivSamllSinge.setImageResource(R.mipmap.icon_small_single_select);
                selectStatus = "小单";
                break;
            case R.id.iv_big_double: //大双
                restStatus();
                ivBigDouble.setImageResource(R.mipmap.icon_big_double_select);
                selectStatus = "大双";
                break;
            case R.id.iv_samll_double: //小双
                restStatus();
                ivSamllDouble.setImageResource(R.mipmap.icon_small_double_select);
                selectStatus = "小双";
                break;
            case R.id.iv_more_big: //更大
                restStatus();
                ivMoreBig.setImageResource(R.mipmap.icon_big_more_select);
                selectStatus = "极大";
                break;
            case R.id.iv_more_samll: //更小
                restStatus();
                ivMoreSamll.setImageResource(R.mipmap.icon_small_more_select);
                selectStatus = "极小";
                break;

            case R.id.iv_xy:
                ll_game.setVisibility(View.GONE);
                live_game.setVisibility(View.VISIBLE);
                gameIsStart = true;
                break;

            case R.id.ruanjianpan:
                if (gameIsStart == false) {
                    ll_game.setVisibility(View.GONE);
                    messageFragment.inputTypeOnClick();
                } else {
                    live_game.setVisibility(View.GONE);
                    messageFragment.inputTypeOnClick();

                }

                break;

            case R.id.zhoubang:
                messageFragment.ivRankingOnClick();
                break;
            case R.id.game:  //游戏
                if (gameIsStart == false) {
                    if (ll_game.getVisibility() == View.VISIBLE) {
                        ll_game.setVisibility(View.GONE);
                    } else {
                        ll_game.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (live_game.getVisibility() == View.VISIBLE) {
                        live_game.setVisibility(View.GONE);
                    } else {
                        live_game.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.game_more_btn:
                View popupView = LayoutInflater.from(this).inflate(R.layout.view_more_button, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int popupWidth = popupView.getMeasuredWidth();
                int popupHeight = popupView.getMeasuredHeight();
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2,
                        location[1] - popupHeight);
                popupView.findViewById(R.id.iv_live_share).setOnClickListener(new View.OnClickListener() {  //分享
                    @Override
                    public void onClick(View v) {
                        UMUtils.umShare(StartLiveActivity.this, creatReadyBean.getCreator().getNickName(), creatReadyBean
                                .getLivePicUrl(), share_url + "?userId=" + appUser.getId());
                    }
                });
                popupView.findViewById(R.id.audio_player).setOnClickListener(new View.OnClickListener() { //音乐
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, AudioPlayerActivity.class));
                    }
                });

                break;

            /**
             * 关闭拉流小窗口关闭拉流地址
             */
            case R.id.small_coles:
                hindSmallVideo();
                Map<String, Object> map = new HashMap<>();
                map.put("watch_private_flag", "0");
                map.put("vip", "0");
                SendRoomMessageUtils.onCustomMessageLianmai(SendRoomMessageUtils.MESSAGE_WATCHER_DISCONNECT, messageFragment, wy_Id,
                        map);
                break;
            //跳转到排行榜
            case R.id.ll_tp_labe:
                Intent intent = new Intent(this, ContributionActivity.class);
                startActivity(intent);
                break;
            //关闭直播
            case R.id.live_close:
                //退出登录
                if (mQuitDialog != null && !mQuitDialog.isShowing()) {
                    mQuitDialog.show();
                }
                break;

            //美颜
            case R.id.iv_live_mei:
                if (!mHandler.hasMessages(MSG_FB)) {
                    mHandler.sendEmptyMessage(MSG_FB);
                }
                break;
            //私信
            case R.id.iv_live_privatechat:
                if (gameIsStart == false) {
                    ll_game.setVisibility(View.GONE);
                } else {
                    live_game.setVisibility(View.GONE);
                }
                ll_game.setVisibility(View.GONE);
                ll_buttom_mun.setVisibility(View.GONE);

                sessionListFragment = new ChatRoomSessionListFragment();
                sessionListFragment.init(getSupportFragmentManager(), ll_buttom_mun);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.watch_room_message_fragment_parent, sessionListFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            //摄像头旋转
            case R.id.iv_live_switch:
                mHandler.removeCallbacks(mSwitcher);
                mHandler.postDelayed(mSwitcher, 100);
                break;
            //分享
            case R.id.iv_live_share:
                UMUtils.umShare(this, creatReadyBean.getCreator().getNickName(), creatReadyBean
                        .getLivePicUrl(), share_url + "?userId=" + appUser.getId());
                break;
            case R.id.iv_live_gift://点击给自己送礼按钮
                getGiftPopup(appUser.getNickName(), appUser.getId());
                break;
            case R.id.live_head:
                ifattention("请求用户信息", appUser.getId(), RequestCode.REQUEST_USER_INFO);
                break;
            //家族
            case R.id.start_room_jaiZu:
                getFamilyMember();
                break;
            //場控
            case R.id.room_id:
                request();
                break;
            //播放音乐
            case R.id.audio_player:
                startActivity(new Intent(mContext, AudioPlayerActivity.class));
                break;
            case R.id.iv_show_send_gift_lian:
                SEND_NUM++;
                onClickSendGift();
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
        hashMap.put("userId", creatReadyBean.getCreator().getId());
        String json = new JSONObject(hashMap).toString();
        LogUtils.i("家族Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
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
     * 展示场控
     *
     * @author sll
     * @time 2016/12/23 11:22
     */
    ControllerBaseAdapter commentAdapter;
    List<ControllerBean> controllerList = new ArrayList<>();

    private void showContrllerList(final List<ControllerBean> llerList) {
        controllerList.clear();
        controllerList.addAll(llerList);
        BottomView familySelectView = new BottomView(this, R.style.BottomViewTheme_Transparent, R
                .layout.view_show_contrller);
        familySelectView.setAnimation(R.style.BottomToTopAnim);
        familySelectView.showBottomView(true);
        View mFamilyView = familySelectView.getView();
        ListView listView = (ListView) mFamilyView.findViewById(R.id.lv_contrller);
        commentAdapter = new ControllerBaseAdapter(controllerList, mContext, appUser.getId(),
                familySelectView);
        listView.setAdapter(commentAdapter);
    }

    /**
     * 请求场控列表
     */


    private void request() {
        RequestParams params = new RequestParams(UrlBuilder.chargeServerUrl + UrlBuilder
                .myController);
        params.addQueryStringParameter("page", 1 + "");
        params.addQueryStringParameter("rows", "10");
        params.addQueryStringParameter("userId", appUser.getId());
        x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        LogUtils.e("我的场控列表" + result.toString());
                        NewSdkHttpResult newSdkhttpResult = JsonUtil.json2Bean(result.toString(),
                                NewSdkHttpResult.class);
                        if (newSdkhttpResult.getCode() == 1) {
                            List<ControllerBean> list = JsonUtil.json2BeanList(newSdkhttpResult
                                    .getObj().toString(), ControllerBean.class);

                            if (null == list || list.size() == 0) {
                                ToastUtils.showMessageCenter(mContext, "您暂时没有场控");
                            } else {
                                showContrllerList(list);
                            }
                        } else {
                            ToastUtils.showMessageCenter(mContext, "请求数据失败");

                        }


                    }

                    //请求异常后的回调方法
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                    }

                    //主动调用取消请求的回调方法
                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                }

        );


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
        BottomView familySelectView = new BottomView(this, R.style.BottomViewTheme_Transparent, R
                .layout.view_show_familyr_members);
        familySelectView.setAnimation(R.style.BottomToTopAnim);
        familySelectView.showBottomView(true);

        View mFamilyView = familySelectView.getView();
        RefreshRecyclerView familyMembers = (RefreshRecyclerView) mFamilyView.findViewById(R.id
                .view_family_members_recycler);
        FamilyMemberAdapter adapter = new FamilyMemberAdapter(0, this, mList, creatReadyBean
                .getCreator().getId());
        GridLayoutManager mLayoutManager = new GridLayoutManager(StartLiveActivity.this, 2,
                GridLayoutManager.HORIZONTAL, false);
        familyMembers.setLayoutManager(mLayoutManager);
        familyMembers.setAdapter(adapter);
        RecyclerViewManager.with(adapter, mLayoutManager)
                .setMode(RecyclerMode.NONE)
                .into(familyMembers, this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocationOi
        ButterKnife.bind(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean isFirstJoin = true;//是否第一次调onResume

    public void onResume() {
        super.onResume();
        mWl.acquire();
        mMediaStreamingManager.resume();

        if (!isFirstJoin) {
            Map<String, Object> map = new HashMap<>();
            map.put("level", creatReadyBean.getCreator().getLevel());
            SendRoomMessageUtils.onCustomMessageQiehuan("112", messageFragment, wy_Id + "",
                    map);
        }
        isFirstJoin = false;
    }

    protected boolean mIsReady = false;

    @Override
    public void onPause() {
        super.onPause();
        mWl.release();
        mShutterButtonPressed = false;
        mHandler.removeCallbacksAndMessages(null);
        mMediaStreamingManager.pause();

        Map<String, Object> map = new HashMap<>();
        map.put("level", creatReadyBean.getCreator().getLevel());
        SendRoomMessageUtils.onCustomMessageQiehuan("113", messageFragment, wy_Id + "",
                map);
    }

    @Override
    protected void onDestroy() {
//        httpDatas.getDataDialog("关闭直播间", false, urlBuilder.cloesAnchor(room_Id), myHandler,
//                RequestCode.REQUEST_ROOM_CLOES);
        timer.cancel();
        hindSmallVideo();
        stopLive();
        videoPlayEnd();
        super.onDestroy();
        registerReceiveCustom(false);
        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
        unregisterReceiver(broadcastReceiver);
        barrageview.setSentenceList(new ArrayList<BarrageDateBean>());
        mMediaStreamingManager.destroy();

        AudioPlayerActivity.playingId = "";
        AudioPlayerActivity.pauseingId = "";
        lrcHandler.removeCallbacks(runnable);
        Intent intent = new Intent(mContext, VoiceService.class);
        stopService(intent);

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

    @Override
    public void onBackPressed() {
        stopLive();
        super.onBackPressed();
    }


    /**
     * 注册网易云信广播接收器
     */
    public void registerReceiverStartLiveActivity() {
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
//                        requestNet();
                        RoomUserBean roomUserBean = JavaBeanMapUtils.mapToBean((Map) message.
                                getRemoteExtension().get("data"), RoomUserBean.class);
                        if (roomUserBean != null && !roomUserBean.getUserId().equals(appUser.getId())) {
                            liveNum.setText(++liveOnLineNums + "");
                            mDatas.add(roomUserBean);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 107:
                        //点亮
                        break;
                    case 108:
                        //点赞
//                        LogUtils.e("点赞返回", message.getRemoteExtension().toString());
//                       int number = Integer.parseInt((String) message.getRemoteExtension().get("number"));
//                        for (int i = 0; i < number; i++) {
//                            showLit();
//                        }
                        showLit();
                        break;
                    case 109:
                        //收到 109 礼物消息 发送过来广播
                        CustomGiftBean customGiftBean = JavaBeanMapUtils.mapToBean(remote,
                                CustomGiftBean.class);
                        LogUtils.e("customGiftBean", customGiftBean.toString());
                        if (Integer.parseInt(customGiftBean.getGift_item_number()) < 1) {
                            return;
                        }
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
                             * 有特效
                             */
                            AnimationUtils.getAnimation().SwichAnimation(customGiftBean,
                                    StartLiveActivity.this, mRoot, 1);
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
                                                (customGiftBean, StartLiveActivity.this, mRoot, 2);
                                        break;
                                    }
                                }

                            }
                        }
                        starGiftAnimation(giftSendModel);
                        break;
                    case 110:
                        //连麦成功
                        LogUtil.e("主播连麦成功", message.getRemoteExtension().toString());
                        CustomLianmaiBean customLianmaiBean = JavaBeanMapUtils.mapToBean(remote,
                                CustomLianmaiBean.class);
                        String video = customLianmaiBean.getBroadcastUrl();
                        WatchLive(video);
                        break;
                    case 111:
                        LogUtil.e("观众断开连麦", message.getRemoteExtension().toString());
                        showToast("观众断开连麦");
                        hindSmallVideo();
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
                BarrageDateBean barrageDateBean = new BarrageDateBean();
                barrageDateBean.setContent(message.getContent());
                if (message.getChatRoomMessageExtension() != null) {
                    barrageDateBean.setNickName("@" + message.getChatRoomMessageExtension()
                            .getSenderNick());
                } else {
                    barrageDateBean.setNickName("@" + appUser.getNickName());
                }
                barrageDateBean.setPicUrl(url);
                barrageview.addSentence(barrageDateBean);
                if (message.getFromAccount().equals("miu_" + appUser.getId())) {
                    SharedPreferenceUtils.saveGoldCoin(mContext, (Long.parseLong
                            (SharedPreferenceUtils.getGoldCoin(mContext)) - 1) + "");
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
        mLayoutManager = new GridLayoutManager(mContext, 1, LinearLayoutManager.HORIZONTAL, false);
        rlvListLiveAudiences.setLayoutManager(mLayoutManager);
        mAdapter = new RoomUsersDataAdapter(mContext, mDatas);
        rlvListLiveAudiences.setAdapter(mAdapter);

//        requestNet();
    }

    /**
     * 聊天室头像列表
     */
    private void requestNet() {
        String url = UrlBuilder.serverUrl + UrlBuilder.room;
        if (appUser != null) {
            url += room_Id;
            if (isRefresh) {
                //下拉刷新,从第一页开始，重新请求
                page = 1;
            } else {
                //上拉加载，从下一页开始加载
                page++;
            }
            url += "/users?pageNum=" + page;
            LogUtils.i("聊天室头像列表(url):" + url);
            OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack(mContext,
                    HttpDatas.KEY_CODE) {
                @Override
                public void onFaild() {
                }

                @Override
                public void onSucess(String data) {
                    LogUtils.i("聊天室头像列表(data):" + data);
                    RoomUserDataBean visitor = JsonUtil.json2Bean(data, RoomUserDataBean.class);
                    hanlderVisitors(visitor);
                }
            });
        }
    }

    /**
     * 取消刷新和加载
     */
    private void finshRefresh() {
    }

    /**
     * 直播观众头像列表
     *
     * @param visitor
     */
    private void hanlderVisitors(RoomUserDataBean visitor) {
//        liveNum.setText(liveOnLineNums++ + "");
        if (visitor != null) {
            List<RoomUserBean> result = visitor.getResult();
            if (result != null) {
                if (isRefresh) {
                    //下拉刷新需要清除数据
                    mDatas.clear();
                }
                mDatas.addAll(result);
                mAdapter.notifyDataSetChanged();
            }
        }

        finshRefresh();
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
        EnterChatRoomData data = new EnterChatRoomData(wy_Id);
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
            messageFragment.init(wy_Id, room_Id);
        } else {
            // 如果Fragment还未Create完成，延迟初始化
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initMessageFragment();
                }
            }, 50);
        }

        messageFragmentGift = (ChatRoomMessageFragmentGift) getSupportFragmentManager()
                .findFragmentById(R.id.watch_room_message_fragment_gift);
        if (messageFragmentGift != null) {
            messageFragmentGift.init(wy_Id);
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
     * type 1.自己赠送礼物，2.游客赠送礼物
     */
    public void getGiftPopup(String name, String id) {
        //隐藏连送按钮
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSendGiftLian.setVisibility(View.GONE);
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
        /**
         *礼物列表点点击充值，跳转进行金币的充值
         */
        ll_user_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mContext, ChargeCoinsActivity.class).putExtra("yanpiao",
//                        SharedPreferenceUtils.getGoldCoin(mContext)));
//                startActivity(new Intent(mContext, PayOrderActivity.class));
                startActivity(new Intent(mContext, ExplainWebViewActivity.class));
            }
        });
        String myCoin = SharedPreferenceUtils.getGoldCoin(mContext);
        myCoin = CountUtils.getCount(Long.parseLong(myCoin));
        mUserCoin.setText(myCoin);

        mVpGiftView = (ViewPager) view.findViewById(R.id.vp_gift_page);
        toUserId = id;
        toUserName = name;
        mSendGiftBtn = (Button) view.findViewById(R.id.btn_show_send_gift);
        /**
         * 赠送礼物按钮进行礼物的赠送
         */
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
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("StartLive Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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
        if (Long.valueOf(SharedPreferenceUtils.getGoldCoin(mContext)) < Long.valueOf(mSendGiftItem
                .getMemberConsume())) {
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
                giftTimer.cancel();
            }
        };
        giftTimer.start();
    }

    private void sendGift() {
        mSendGiftList.add(mSendGiftItem);
        mSendGiftNumList.add(SEND_NUM + "");
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("userId", appUser.getId());
        map.put("roomId", room_Id);
        map.put("amount", mSendGiftItem.getMemberConsume());
        map.put("giftId", mSendGiftItem.getId());
        map.put("toUserId", toUserId);
        httpDatas.DataNoloadingAdmin("赠送礼物", com.android.volley.Request.Method.POST,
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
        /**
         * 判断是否是连送礼物界面
         */
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


    private void sendGiftAnimation(final GiftFrameLayout view, final GiftSendModel model) {
        view.setModel(model);
        AnimatorSet animatorSet = view.startAnimation(model.getGiftCount());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                synchronized (giftSendModelList) {
                    if (giftSendModelList.size() > 0) {
                        sendGiftAnimation(view, giftSendModelList.get(0));
//                        starGiftAnimation(giftSendModelList.get(0));
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
        //如果收礼物是自己，金币数要增加
        if (customGiftBean.getReceveUserId() != null && customGiftBean.getReceveUserId().equals(appUser.getId())) {
            String receivedGoldCoin = (Long.parseLong(SharedPreferenceUtils.getZhuboGoldCoin(mContext)) +
                    ((Integer.parseInt(customGiftBean.getGift_item_number()) *
                            Integer.parseInt(customGiftBean.getGift_coinnumber_index())))) + "";
            SharedPreferenceUtils.saveZhuboGoldCoin(mContext, receivedGoldCoin);
            receivedGoldCoin = CountUtils.getCount(Long.parseLong(receivedGoldCoin));
            liveJinpiao.setText(receivedGoldCoin); //显示主播乐票数量


            SharedPreferenceUtils.saveGoldCoin(mContext, (Long.parseLong(SharedPreferenceUtils
                    .getGoldCoin(mContext)) + ((Integer.parseInt(customGiftBean.getGift_item_number()) *
                    Integer.parseInt(customGiftBean.getGift_coinnumber_index())))) + "");
        }

        //如果中奖的是自己,金币数要增加
        if (customGiftBean.getUserId() != null && customGiftBean.getGift_Grand_Prix() != null
                && customGiftBean.getUserId().equals(appUser.getId())) {
            String[] Gift_prix = customGiftBean.getGift_Grand_Prix();
            if (Gift_prix.length > 0) {
                long num = Long.parseLong(SharedPreferenceUtils.getZhuboGoldCoin(mContext));
                long num1 = Long.parseLong(SharedPreferenceUtils.getGoldCoin(mContext));
                for (int i = 0, j = Gift_prix.length; i < j; i++) {
                    num += Integer.valueOf(Gift_prix[i]) * Integer.valueOf(customGiftBean
                            .getGift_coinnumber_index());
                    num1 += Integer.valueOf(Gift_prix[i]) * Integer.valueOf(customGiftBean
                            .getGift_coinnumber_index());
                }

                String receivedGoldCoin = num + "";
                SharedPreferenceUtils.saveZhuboGoldCoin(mContext, receivedGoldCoin);
                receivedGoldCoin = CountUtils.getCount(Long.parseLong(receivedGoldCoin));
                liveJinpiao.setText(receivedGoldCoin);

                SharedPreferenceUtils.saveGoldCoin(mContext, num1 + "");
            }
        }
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
            LogUtils.i("WangYi_CN", "接收自定义通知:开启");
        else
            LogUtils.i("WangYi_CN", "接收自定义通知:结束");
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
                tabNewMsgLabel.setText(String.valueOf(ReminderSettings.unreadMessageShowRule
                        (unread)));
            }
        }
    };

    Observer<CustomNotification> observer = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            // 在这里处理自定义通知。
            LogUtils.i("WangYi_CN", "Start接收自定义通知(SessionId):" + message.getSessionId());
            LogUtils.i("WangYi_CN", "Start接收自定义通知(fromAccount):" + message.getFromAccount());
            LogUtils.i("WangYi_CN", "Start接收自定义通知(SessionType):" + message.getSessionType());
            LogUtils.i("WangYi_CN", "Start接收自定义通知(ApnsText):" + message.getApnsText());
            LogUtils.i("WangYi_CN", "Start接收自定义通知(Content):" + message.getContent());
            if (message.getPushPayload() != null)
                LogUtils.i("WangYi_CN", "Start接收自定义通知(pushPayload):" + message.getPushPayload()
                        .toString());
            DianBoDateBean dianBoDateBean = JSON.parseObject(message.getContent(), DianBoDateBean
                    .class);
            if (dianBoDateBean != null) {
                String type = dianBoDateBean.getType();
                if (type != null) {
                    if (type.equals(CustomNotificationType.IM_P2P_TYPE_SUBLIVE_PUBLIC)) {

                        if (dianBoDateBean.getResult() != null && dianBoDateBean.getResult().equals("1")) {//观众请求连麦
                            lianId = message.getSessionId();
                            LianTongzhiBean lianTongzhiBean = JsonUtil.json2Bean(message.getContent(), LianTongzhiBean.class);
                            lianName = lianTongzhiBean.getUserName();
                            showLianDalog(lianName);
                        } else if (dianBoDateBean.getResult() != null && dianBoDateBean.getResult().equals("2")) {//观众拒绝连麦
                            showToast("观众拒绝了连麦");
                        } else {//忘了当时为什么写这个了，可能是怕漏掉什么吧，先不管他，正常情况下应该不会走这个
                            lianId = message.getSessionId();
                            LianTongzhiBean lianTongzhiBean = JsonUtil.json2Bean(message.getContent(), LianTongzhiBean.class);
                            lianName = lianTongzhiBean.getUserName();
                            showLianDalog(lianName);
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
            if (!chatRoomStatusChangeData.roomId.equals(wy_Id)) {
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
                int code = NIMClient.getService(ChatRoomService.class).getEnterErrorCode(wy_Id);
                LogUtils.d(TAG, "chat room enter error code:" + code);
                if (code != ResponseCode.RES_ECONNECTION) {
                    LogUtils.i("WangYi", "未登录,code=" + code);
                }
            } else if (chatRoomStatusChangeData.status == StatusCode.NET_BROKEN) {
                Toast.makeText(StartLiveActivity.this, R.string.net_broken, Toast.LENGTH_SHORT)
                        .show();
            }
            LogUtils.i(TAG, "chat room online status changed to " + chatRoomStatusChangeData
                    .status.name());
        }
    };
    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
        @Override
        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
            Toast.makeText(StartLiveActivity.this, "被踢出聊天室，原因:" + chatRoomKickOutEvent.getReason
                    (), Toast.LENGTH_SHORT).show();
            clearChatRoom();
        }
    };

    public void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(wy_Id);
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


    // *********************************** 初始化直播/连麦内容开始 ********************************** //
    //七牛
    protected Button mShutterButton;
    private Button mMuteButton;
    private Button mTorchBtn;
    private Button mCameraSwitchBtn;
    private Button mCaptureFrameBtn;
    private Button mEncodingOrientationSwitcherBtn;
    private Button mFaceBeautyBtn;
    protected TextView mStatusTextView;
    protected MediaStreamingManager mMediaStreamingManager;
    private static final int ZOOM_MINIMUM_WAIT_MILLIS = 33; //ms
    private boolean mIsTorchOn = false;
    private boolean mIsNeedMute = false;
    private boolean mIsNeedFB = false;
    private boolean mIsEncOrientationPort = true;
    private boolean mIsPreviewMirror = false;
    protected TextView mLogTextView;
    protected TextView mStatView;
    private Switcher mSwitcher = new Switcher();
    private boolean mIsEncodingMirror = false;
    private static final int MSG_START_STREAMING = 0;
    private static final int MSG_STOP_STREAMING = 1;
    private static final int MSG_SET_ZOOM = 2;
    private static final int MSG_MUTE = 3;
    private static final int MSG_FB = 4;
    private static final int MSG_PREVIEW_MIRROR = 5;
    private static final int MSG_ENCODING_MIRROR = 6;
    protected boolean mShutterButtonPressed = false;
    private int mCurrentZoom = 0;
    private boolean mOrientationChanged = false;
    private int mMaxZoom = 0;
    private ScreenShooter mScreenShooter = new ScreenShooter();
    ///////////////////////////////////
    protected StreamingProfile mProfile;
    private int mCurrentCamFacingIndex;
    protected CameraStreamingSetting mCameraStreamingSetting;
    protected MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    private String mStatusMsgContent;
    private String mLogContent = "\n";
    private final static int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;
    protected PowerManager.WakeLock mWl;
    protected Handler mMainHandler = new Handler();

    private List<LianMaiDateBean> lianMaiDateList = new ArrayList<>();

    /**
     * 连麦拉流小窗口
     *
     * @param twoVideo
     */
    private void WatchLive(String twoVideo) {
        LogUtils.e("twoVideo--twoVideo" + twoVideo);

        lmFm.setVisibility(View.VISIBLE);
        if (mVideoSurfaceView.getVisibility() == View.GONE || mVideoSurfaceView.getVisibility() == View.INVISIBLE) {
            mVideoSurfaceView.setVisibility(View.VISIBLE);
        }
        mVideoSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mVideoSurfaceView.setZOrderMediaOverlay(true);
        liveVideo = new AnchorVideoOne();
        liveVideo.initLive(twoVideo, StartLiveActivity.this, mVideoSurfaceView);
        Toast.makeText(mContext, "连麦成功", Toast.LENGTH_SHORT).show();

    }

    /**
     * 隐藏拉流小窗口
     */
    private void hindSmallVideo() {

        if (!TextUtils.isEmpty(lianId) && lmFm.getVisibility() == View.VISIBLE) {
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
            httpDatas.getDataForJsoNoloading("主播退出连线", com.android.volley.Request.Method.GET, UrlBuilder.roomLiveExit
                            (room_Id, lianId.substring(4)), map, myHandler,
                    RequestCode.ROOMLIVEEXIT);
        }

        if (liveVideo != null) {
            liveVideo.release();
//            mVideoSurfaceView.getHolder().getSurface().release();
            mVideoSurfaceView.setVisibility(View.INVISIBLE);
            lmFm.setVisibility(View.GONE);
            liveVideo = null;
        }

    }


    /**
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
                .setSendingBufferProfile(new StreamingProfile
                        .SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));

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

        mIsNeedFB = true;
        mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
        mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);

        initUIs();
        push(room_Id);

    }

    private void switchCamera() {
        mHandler.removeCallbacks(mSwitcher);
        mHandler.postDelayed(mSwitcher, 100);
    }

    private void initPaly() {
        AspectFrameLayout afl = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        afl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        com.lvshandian.lemeng.view.CameraPreviewFrameView cameraPreviewFrameView =
                (com.lvshandian.lemeng.view.CameraPreviewFrameView) findViewById(R.id
                        .cameraPreview_surfaceView);
        cameraPreviewFrameView.setListener(null);
        cameraPreviewFrameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gameIsStart == false) {
                    ll_game.setVisibility(View.GONE);
                } else {
                    live_game.setVisibility(View.GONE);
                }
                ll_game.setVisibility(View.GONE);
                ll_buttom_mun.setVisibility(View.VISIBLE);
                if (sessionListFragment != null) {
                    sessionListFragment.hide();
                }
                if (liveMessageFragment != null)
                    liveMessageFragment.hide();
                if (messageFragment != null) {
                    messageFragment.hideEditText();
                }
                return false;
            }
        });
        WatermarkSetting watermarksetting = new WatermarkSetting(this);
        watermarksetting.setResourceId(R.drawable.umeng_socialize_facebook)
                .setSize(WatermarkSetting.WATERMARK_SIZE.MEDIUM)
                .setAlpha(100)
                .setCustomPosition(0.5f, 0.5f);

        mMediaStreamingManager = new MediaStreamingManager(this, afl, cameraPreviewFrameView,
                AVCodecType.HW_VIDEO_WITH_HW_AUDIO_CODEC); // hw codec
        mMediaStreamingManager.prepare(mCameraStreamingSetting, mMicrophoneStreamingSetting,
                null, mProfile);
        mMediaStreamingManager.setStreamingStateListener(this);
        mMediaStreamingManager.setSurfaceTextureCallback(this);
        mMediaStreamingManager.setStreamingSessionListener(this);
        mMediaStreamingManager.setStreamStatusCallback(this);
        setFocusAreaIndicator();
    }

    private EncodingOrientationSwitcher mEncodingOrientationSwitcher = new
            EncodingOrientationSwitcher();

    private class EncodingOrientationSwitcher implements Runnable {

        @Override
        public void run() {
            Log.i(TAG, "mIsEncOrientationPort:" + mIsEncOrientationPort);
            stopStreaming();
            mOrientationChanged = !mOrientationChanged;
            mIsEncOrientationPort = !mIsEncOrientationPort;
            mProfile.setEncodingOrientation(mIsEncOrientationPort ? StreamingProfile
                    .ENCODING_ORIENTATION.PORT : StreamingProfile.ENCODING_ORIENTATION.LAND);
            mMediaStreamingManager.setStreamingProfile(mProfile);
            setRequestedOrientation(mIsEncOrientationPort ? ActivityInfo
                    .SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mMediaStreamingManager.notifyActivityOrientationChanged();
            updateOrientationBtnText();
            Log.i(TAG, "EncodingOrientationSwitcher -");
        }
    }

    private class ScreenShooter implements Runnable {
        @Override
        public void run() {
            final String fileName = "PLStreaming_" + System.currentTimeMillis() + ".jpg";
            mMediaStreamingManager.captureFrame(100, 100, new FrameCapturedCallback() {
                private Bitmap bitmap;

                @Override
                public void onFrameCaptured(Bitmap bmp) {
                    if (bmp == null) {
                        return;
                    }
                    bitmap = bmp;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                saveToSDCard(fileName, bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (bitmap != null) {
                                    bitmap.recycle();
                                    bitmap = null;
                                }
                            }
                        }
                    }).start();
                }
            });
        }
    }

    private void saveToSDCard(String filename, Bitmap bmp) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
                bmp.recycle();
                bmp = null;
            } finally {
                if (bos != null) bos.close();
            }

            final String info = "Save frame to:" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, info, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private class Switcher implements Runnable {
        @Override
        public void run() {
            mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting
                    .getNumberOfCameras();

            CameraStreamingSetting.CAMERA_FACING_ID facingId;
            if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID
                    .CAMERA_FACING_BACK.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
            } else if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID
                    .CAMERA_FACING_FRONT.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
            } else {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
            }
            Log.i(TAG, "switchCamera:" + facingId);
            mMediaStreamingManager.switchCamera(facingId);
        }
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
                            setShutterButtonEnabled(false);
                            boolean res = mMediaStreamingManager.startStreaming();
                            mShutterButtonPressed = true;
                            Log.i(TAG, "res:" + res);
                            if (!res) {
                                mShutterButtonPressed = false;
                                setShutterButtonEnabled(true);
                            }
                            setShutterButtonPressed(mShutterButtonPressed);
                        }
                    }).start();
                    break;
                case MSG_STOP_STREAMING:
                    if (mShutterButtonPressed) {
                        // disable the shutter button before stopStreaming
                        setShutterButtonEnabled(false);
                        boolean res = mMediaStreamingManager.stopStreaming();
                        if (!res) {
                            mShutterButtonPressed = true;
                            setShutterButtonEnabled(true);
                        }
                        setShutterButtonPressed(mShutterButtonPressed);
                    }
                    break;
                case MSG_SET_ZOOM:
                    mMediaStreamingManager.setZoomValue(mCurrentZoom);
                    break;
                case MSG_MUTE:
                    mIsNeedMute = !mIsNeedMute;
                    mMediaStreamingManager.mute(mIsNeedMute);
                    updateMuteButtonText();
                    break;
                case MSG_FB:
                    mIsNeedFB = !mIsNeedFB;
                    mMediaStreamingManager.setVideoFilterType(mIsNeedFB ?
                            CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY
                            : CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_NONE);
                    updateFBButtonText();
                    break;
                case MSG_PREVIEW_MIRROR:
                    mIsPreviewMirror = !mIsPreviewMirror;
                    mMediaStreamingManager.setPreviewMirror(mIsPreviewMirror);
                    Toast.makeText(mContext, "镜像成功", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_ENCODING_MIRROR:
                    mIsEncodingMirror = !mIsEncodingMirror;
                    mMediaStreamingManager.setEncodingMirror(mIsEncodingMirror);
                    Toast.makeText(mContext, "镜像成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.e(TAG, "Invalid message");
                    break;
            }
        }
    };

    private void updateCameraSwitcherButtonText(int camId) {
        if (mCameraSwitchBtn == null) {
            return;
        }
        if (camId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCameraSwitchBtn.setText("Back");
        } else {
            mCameraSwitchBtn.setText("Front");
        }
    }

    private void initButtonText() {
        updateFBButtonText();
        updateCameraSwitcherButtonText(mCameraStreamingSetting.getReqCameraId());
        mCaptureFrameBtn.setText("Capture");
        updateFBButtonText();
        updateMuteButtonText();
        updateOrientationBtnText();
    }

    private void updateOrientationBtnText() {
        if (mIsEncOrientationPort) {
            mEncodingOrientationSwitcherBtn.setText("Land");
        } else {
            mEncodingOrientationSwitcherBtn.setText("Port");
        }
    }

    private void updateFBButtonText() {
        if (mFaceBeautyBtn != null) {
            mFaceBeautyBtn.setText(mIsNeedFB ? "FB Off" : "FB On");
        }
    }

    private void updateMuteButtonText() {
        if (mMuteButton != null) {
            mMuteButton.setText(mIsNeedMute ? "Unmute" : "Mute");
        }
    }

    protected void setShutterButtonPressed(final boolean pressed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShutterButtonPressed = pressed;
                mShutterButton.setPressed(pressed);
            }
        });
    }

    protected void setShutterButtonEnabled(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShutterButton.setFocusable(enable);
                mShutterButton.setClickable(enable);
                mShutterButton.setEnabled(enable);
            }
        });
    }

    private RotateLayout mRotateLayout;

    protected void setFocusAreaIndicator() {
        if (mRotateLayout == null) {
            mRotateLayout = (RotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
            mMediaStreamingManager.setFocusAreaIndicator(mRotateLayout,
                    mRotateLayout.findViewById(R.id.focus_indicator));
        }
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

    private void initUIs() {
        View rootView = findViewById(R.id.content);
        rootView.addOnLayoutChangeListener(this);
        mMuteButton = (Button) findViewById(R.id.mute_btn);
        mShutterButton = (Button) findViewById(R.id.toggleRecording_button);
        mTorchBtn = (Button) findViewById(R.id.torch_btn);
        mCameraSwitchBtn = (Button) findViewById(R.id.camera_switch_btn);
        mCaptureFrameBtn = (Button) findViewById(R.id.capture_btn);
        mFaceBeautyBtn = (Button) findViewById(R.id.fb_btn);
        mStatusTextView = (TextView) findViewById(R.id.streamingStatus);
        Button previewMirrorBtn = (Button) findViewById(R.id.preview_mirror_btn);
        Button encodingMirrorBtn = (Button) findViewById(R.id.encoding_mirror_btn);
        mLogTextView = (TextView) findViewById(R.id.log_info);
        mStatView = (TextView) findViewById(R.id.stream_status);

        mFaceBeautyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHandler.hasMessages(MSG_FB)) {
                    mHandler.sendEmptyMessage(MSG_FB);
                }
            }
        });

        mMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHandler.hasMessages(MSG_MUTE)) {
                    mHandler.sendEmptyMessage(MSG_MUTE);
                }
            }
        });

        previewMirrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHandler.hasMessages(MSG_PREVIEW_MIRROR)) {
                    mHandler.sendEmptyMessage(MSG_PREVIEW_MIRROR);
                }
            }
        });

        encodingMirrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHandler.hasMessages(MSG_ENCODING_MIRROR)) {
                    mHandler.sendEmptyMessage(MSG_ENCODING_MIRROR);
                }
            }
        });

        mShutterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShutterButtonPressed) {
                    stopStreaming();
                } else {
                    startStreaming();
                }
            }
        });

        mTorchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsTorchOn) {
                            mIsTorchOn = true;
                            mMediaStreamingManager.turnLightOn();
                        } else {
                            mIsTorchOn = false;
                            mMediaStreamingManager.turnLightOff();
                        }
                        setTorchEnabled(mIsTorchOn);
                    }
                }).start();
            }
        });

        mCameraSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mSwitcher);
                mHandler.postDelayed(mSwitcher, 100);
            }
        });

        mCaptureFrameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mScreenShooter);
                mHandler.postDelayed(mScreenShooter, 100);
            }
        });


        mEncodingOrientationSwitcherBtn = (Button) findViewById(R.id.orientation_btn);
        mEncodingOrientationSwitcherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(mEncodingOrientationSwitcher);
                mHandler.post(mEncodingOrientationSwitcher);
            }
        });

        SeekBar seekBarBeauty = (SeekBar) findViewById(R.id.beautyLevel_seekBar);
        seekBarBeauty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting
                        .getFaceBeautySetting();
                fbSetting.beautyLevel = progress / 100.0f;
                fbSetting.whiten = progress / 100.0f;
                fbSetting.redden = progress / 100.0f;

                mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        initButtonText();
    }

    private void setTorchEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String flashlight = enabled ? getString(R.string.flash_light_off) : getString(R
                        .string.flash_light_on);
                mTorchBtn.setText(flashlight);
            }
        });
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
     * 通知网易云信，开直播间
     *
     * @param roomid
     */
    private void push(String roomid) {

        String url = UrlBuilder.serverUrl + UrlBuilder.livepush(wy_Id);
        LogUtils.i(url);
        OkHttpUtils.get()
                .url(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA_AUDIOREC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //播放
                } else {
                    Log.e(TAG, "No CAMERA or AudioRecord permission");
                }
                break;
            }
        }
    }


    /**
     * PopupWindow
     * =======
     *
     * @param num 倒数时间
     * @dw 开始直播倒数计时
     */
    private FBO mFBO = new FBO();

    private void startAnimation(final int num) {
        final TextView tvNum = new TextView(this);
        tvNum.setTextColor(getResources().getColor(R.color.white));
        tvNum.setText(num + "");
        tvNum.setTextSize(30);
        mRoot.addView(tvNum);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvNum.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvNum.setLayoutParams(params);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvNum, "scaleX", 5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvNum, "scaleY", 5f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRoot.removeView(tvNum);
                if (num == 1) {
//                    mStreamer.startStream();

                    return;
                }
                startAnimation(num == 3 ? 2 : 1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.setDuration(1000);
        animatorSet.start();

    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int
            oldTop, int oldRight, int oldBottom) {

    }

    @Override
    public void onAudioSourceAvailable(ByteBuffer byteBuffer, int i, long l, boolean b) {

    }

    @Override
    public void notifyStreamStatusChanged(StreamingProfile.StreamStatus streamStatus) {

    }

    @Override
    public boolean onPreviewFrame(byte[] bytes, int i, int i1, int i2, int i3, long l) {
        return true;
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
                setShutterButtonEnabled(true);
                setShutterButtonPressed(true);
                break;
            case SHUTDOWN:
                mStatusMsgContent = getString(R.string.string_state_ready);
                setShutterButtonEnabled(true);
                setShutterButtonPressed(false);
                if (mOrientationChanged) {
                    mOrientationChanged = false;
                    startStreaming();
                }
                break;
            case IOERROR:
                mLogContent += "IOERROR\n";
                mStatusMsgContent = getString(R.string.string_state_ready);
                setShutterButtonEnabled(true);
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
            case CAMERA_SWITCHED:
//                mShutterButtonPressed = false;
                if (extra != null) {
                    Log.i(TAG, "current camera id:" + (Integer) extra);
                }
                Log.i(TAG, "camera switched");
                final int currentCamId = (Integer) extra;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateCameraSwitcherButtonText(currentCamId);
                    }
                });
                break;
            case TORCH_INFO:
                if (extra != null) {
                    final boolean isSupportedTorch = (Boolean) extra;
                    Log.i(TAG, "isSupportedTorch=" + isSupportedTorch);
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isSupportedTorch) {
                                mTorchBtn.setVisibility(View.VISIBLE);
                            } else {
                                mTorchBtn.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                break;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLogTextView != null) {
                    mLogTextView.setText(mLogContent);
                }
                mStatusTextView.setText(mStatusMsgContent);
            }
        });


    }


    /**
     * 观众同意连麦请求后弹框，需主播确认后才能连麦成功
     */
    public void showLianDalog(String lianName) {
        final Dialog dialogForOrdershowAck = new Dialog(this, R.style.homedialog);
        final View view = View.inflate(this, R.layout.dialog_video_room_show_ack, null);
        TextView showAckText = (TextView) view.findViewById(R.id.dialog_show_ack_text);
        showAckText.setText("是否接受" + lianName + "的连麦");
        view.findViewById(R.id.dialog_show_ack_x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCustomNotificationForLive(lianId, appUser.getId(), appUser.getNickName(), appUser.getPicUrl(),
                        appUser.getVip(), "主播拒绝连麦请求", "2", CustomNotificationType.IM_P2P_TYPE_ORDERSHOW, "1");
                dialogForOrdershowAck.dismiss();
            }

        });
        view.findViewById(R.id.dialog_show_ack_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendCustomNotificationForLive(lianId, appUser.getId(), appUser.getNickName(), appUser.getPicUrl(),
//                        appUser.getVip(), "主播同意与您连麦", "2", CustomNotificationType.IM_P2P_TYPE_SUBLIVE_PUBLIC, "1");
                requstLian();
                dialogForOrdershowAck.dismiss();
            }
        });
        dialogForOrdershowAck.setCanceledOnTouchOutside(false);
        dialogForOrdershowAck.setCancelable(false);
        dialogForOrdershowAck.setContentView(view);
        dialogForOrdershowAck.show();
    }


    /**
     * 接受与主播的连麦请求，并且請求生成Id
     *
     * @param
     */
    private void requstLian() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        httpDatas.getDataForJsoNoloading("接受与主播的连麦生成id", com.android.volley.Request.Method.GET, UrlBuilder
                        .RequestIdlianmai(room_Id), map, myHandler,
                RequestCode.REQUEST_LIANMAI_ID);
    }

    /**
     * 接受主播的连麦请求想主播请求连麦
     */
    private void requstLianmai(String id, String status) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", id);
//        map.put("status", status);
        map.put("status", "1");
        httpDatas.getDataForJsoNoloading("接受与主播的连麦",
                com.android.volley.Request.Method.POST, UrlBuilder.acceptLianmai,
                map, myHandler, RequestCode.ACCEPT_LIANMAI);
    }
    // *********************************** 连麦内容结束 ********************************** //


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
        httpDatas.getNewDataCharServer(details, com.android.volley.Request.Method.POST,
                UrlBuilder.IF_ATTENTION, map, myHandler, handlerCode);
    }

    /**
     * @dw 获取礼物列表
     */
    private void getGiftList() {
        httpDatas.getNewDataCharServerCode("礼物列表", com.android.volley.Request.Method.GET,
                UrlBuilder.GET_GIFT, null, myHandler, RequestCode.GET_GIFT);
    }


    /**
     * 请求主播信息
     */
    private void initUser() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId() + "");
        httpDatas.getNewDataCharServerCodeNoLoading("查询用户信息", com.android.volley.Request.Method
                .POST, UrlBuilder.selectUserInfo, map, myHandler, RequestCode.SELECT_USER);
    }


    /**
     * 展示游客详情
     *
     * @param customdateBean 用户信息
     * @author sll
     * @time 2016/11/25 9:40
     */
    public void showDialogForCallOther(final CustomdateBean customdateBean) {
        final View view = View.inflate(this, R.layout.dialog_video_room, null);
        AvatarView civ_image = (AvatarView) view.findViewById(R.id.civ_image);
        LinearLayout buttom_layout = (LinearLayout) view.findViewById(R.id.buttom_layout);
        final ImageView iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
        ImageView iv_grade = (ImageView) view.findViewById(R.id.iv_grade);
        TextView tvID = (TextView) view.findViewById(R.id.tv_id);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_renzheng = (TextView) view.findViewById(R.id.tv_renzheng);
        TextView tv_sign = (TextView) view.findViewById(R.id.tv_sign);
        TextView tv_focus_num = (TextView) view.findViewById(R.id.tv_focus_num);
        TextView tv_funs_num = (TextView) view.findViewById(R.id.tv_funs_num);
        TextView tv_send_num = (TextView) view.findViewById(R.id.tv_send_num);
        TextView tv_gold_coin = (TextView) view.findViewById(R.id.tv_gold_coin);
        TextView tv_banned = (TextView) view.findViewById(R.id.tv_banned);
        TextView tv_report = (TextView) view.findViewById(R.id.tv_report);
        TextView tv_lianmai = (TextView) view.findViewById(R.id.tv_lianmai);

        if (!TextUtils.isEmpty(customdateBean.getId()) && customdateBean.getId().equals(appUser
                .getId())) {
            buttom_layout.setVisibility(View.GONE);
            tv_banned.setVisibility(View.GONE);
            tv_report.setVisibility(View.GONE);
        } else {
            buttom_layout.setVisibility(View.VISIBLE);
            tv_lianmai.setVisibility(View.VISIBLE);
            tv_banned.setVisibility(View.VISIBLE);
            tv_report.setVisibility(View.GONE);
        }

        //认证
        String verified = customdateBean.getVerified();
        if (verified.equals("2")) {
            tv_renzheng.setText("已认证");
        } else {
            tv_renzheng.setText("未认证");
        }

        //显示界面提示是禁言还是解除禁言
        for (int i = 0, j = mutedList.size(); i < j; i++) {
            if (mutedList.get(i).equals(customdateBean.getId())) {
                tv_banned.setText("解除禁言");
            }
        }

        if (customdateBean.getGender().equals("0")) {
            iv_sex.setImageResource(R.mipmap.female);
        } else {
            iv_sex.setImageResource(R.mipmap.male);
        }

        tvID.setText("ID:" + customdateBean.getId());

        civ_image.setAvatarUrl(customdateBean.getPicUrl());
        civ_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                intent.putExtra(getString(R.string.visiti_person), customdateBean.getId());
                startActivity(intent);
                dialogForSelect.dismiss();
            }
        });

        iv_grade.setImageResource(GrademipmapUtils.LevelImg[Integer.valueOf(customdateBean
                .getLevel()) - 1]);
        tv_name.setText(customdateBean.getNickName());
        //签名
        tv_sign.setText(customdateBean.getSignature());
        tv_focus_num.setText(customdateBean.getFollowNum());
        tv_funs_num.setText(customdateBean.getFansNum());

        String spendGoldCoin = customdateBean.getSpendGoldCoin();
        spendGoldCoin = CountUtils.getCount(Long.parseLong(spendGoldCoin));
        tv_send_num.setText(spendGoldCoin);

        String myCoin = customdateBean.getGoldCoin();
        myCoin = CountUtils.getCount(Long.parseLong(myCoin));
        tv_gold_coin.setText(myCoin);

        //禁言
        view.findViewById(R.id.tv_banned).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banned(customdateBean.getId(), customdateBean.getNickName());
                dialogForSelect.dismiss();
            }

        });
        //关闭
        view.findViewById(R.id.iv_x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogForSelect.dismiss();
            }
        });

        focus((TextView) view.findViewById(R.id.tv_foucs), customdateBean);
        //关注
        view.findViewById(R.id.tv_foucs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFollow((TextView) view.findViewById(R.id.tv_foucs), customdateBean);
            }
        });

        //私聊
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
                liveMessageFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id
                        .watch_room_message_fragment_parent, liveMessageFragment).setTransition
                        (FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                dialogForSelect.dismiss();
            }

        });

        //回复
        view.findViewById(R.id.tv_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageFragment.showEditText();
                EditText editContent = (EditText) findViewById(com.netease.nim.uikit.R.id
                        .editTextMessage);
                String str = "回复" + customdateBean.getNickName() + ":";
                editContent.setText(str);
                editContent.setSelection(str.length());
                dialogForSelect.dismiss();
            }
        });

        //连麦
        tv_lianmai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (com.lvshandian.haiyan.utils.TextUtils.isEmpty(appUser.getLevel()) || Integer
//                        .parseInt(appUser.getLevel()) < 50) {
//                    ToastUtils.showMessageCenter(mContext, "主播的等级不得低于50级");
//                    return;
//                }
//                if (com.lvshandian.haiyan.utils.TextUtils.isEmpty(customdateBean.getLevel()) ||
//                        Integer.parseInt(customdateBean.getLevel()) < 70) {
//                    ToastUtils.showMessageCenter(mContext, "观众的等级不得低于70级");
//                    return;
//                }
                lianId = "miu_" + customdateBean.getId();
                sendCustomNotificationForLive("miu_" + customdateBean.getId(), appUser.getId(),
                        appUser.getNickName(), appUser.getPicUrl(), appUser.getVip(), "主播请求与您连麦", "2",
                        CustomNotificationType.IM_P2P_TYPE_SUBLIVE_PUBLIC, "1");
                dialogForSelect.dismiss();
            }
        });

        //送礼
        view.findViewById(R.id.tv_give).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogForSelect.dismiss();
                getGiftPopup(customdateBean.getNickName(), customdateBean.getId());
            }
        });

        dialogForSelect.setCanceledOnTouchOutside(true);
        dialogForSelect.setContentView(view);
        dialogForSelect.show();
    }


    /**
     * 禁言成功失败
     */
    private void banned(final String stopUserId, final String nickNane) {
        RequestParams params = new RequestParams(UrlBuilder.chargeServerUrl + UrlBuilder
                .appUserControl);

        params.addQueryStringParameter("userId", appUser.getId());
        params.addQueryStringParameter("stopUserId", stopUserId);
        params.addQueryStringParameter("roomId", wy_Id);
        LogUtils.e("params" + params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        LogUtils.e("requestControllerDelete" + result.toString());
                        NewSdkHttpResult newSdkhttpResult = JsonUtil.json2Bean(result.toString(),
                                NewSdkHttpResult.class);
                        if (newSdkhttpResult.getCode() == 0) {
                            if (newSdkhttpResult.getObj().equals("0")) {
                                showToast("解除禁言成功");
                                Map<String, Object> map = new HashMap<>();
                                map.put("vip", appUser.getVip());
                                map.put("userId", appUser.getId());
                                map.put("level", appUser.getLevel());
                                map.put("isJinyan", "0");
                                map.put("jinyanId", stopUserId);
                                map.put("jinyan", nickNane + "被解除禁言");
                                SendRoomMessageUtils.onCustomMessageJinyan(SendRoomMessageUtils
                                        .MESSAGE_JIN_YAN, messageFragment, wy_Id, map);
                                mutedList.remove(stopUserId);
                            } else if (newSdkhttpResult.getObj().equals("1")) {
                                showToast("禁言成功");
                                Map<String, Object> map = new HashMap<>();
                                map.put("vip", appUser.getVip());
                                map.put("userId", appUser.getId());
                                map.put("level", appUser.getLevel());
                                map.put("isJinyan", "1");
                                map.put("jinyanId", stopUserId);
                                map.put("jinyan", nickNane + "被禁言");
                                SendRoomMessageUtils.onCustomMessageJinyan(SendRoomMessageUtils
                                        .MESSAGE_JIN_YAN, messageFragment, wy_Id, map);
                                mutedList.add(stopUserId);
                            }
                        } else if (newSdkhttpResult.getCode() == 0) {
                            showToast("操作失败");
                        }
                    }

                    //请求异常后的回调方法
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                    }

                    //主动调用取消请求的回调方法
                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                    }
                }

        );

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
            }
        });
    }


    /**
     * 显示关注(未关注)
     */
    private void focus(TextView tvFouce, CustomdateBean bean) {
        String follow = bean.getFollow();
        if (TextUtils.equals("0", follow)) {
            tvFouce.setText("未关注");
        } else if (TextUtils.equals("1", follow)) {
            tvFouce.setText("已关注");
        }
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
                final ImageView heart = new ImageView(StartLiveActivity.this);
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
     * 确认退出对话框
     */
    private void initQuitDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_quit_login, null);
        mQuitDialog = new RoundDialog(this, view, R.style.dialog, 0.66f, 0.2f);
        mQuitDialog.setCanceledOnTouchOutside(false);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvTitle.setText("确定离开");
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
                httpDatas.getDataDialog("关闭直播间", false, urlBuilder.cloesAnchor(room_Id), myHandler,
                        RequestCode.REQUEST_ROOM_CLOES);
            }
        });
    }

    //直播结束释放资源
    public void videoPlayEnd() {

        if (mQosThread != null) {
            mQosThread.stopThread();
            mQosThread = null;
        }
    }

    //关闭直播
    private void stopLive() {
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mLrcView.onDrag(0);
    }

    private static Handler lrcHandler = new Handler();

    public static void showLrc(String lrc) {
        mLrcView.loadLrc(getLrcText(lrc));
        if (!mediaPlayer.isPlaying()) {
            lrcHandler.post(runnable);
        } else {
            lrcHandler.removeCallbacks(runnable);
        }
    }

    private static String getLrcText(String fileName) {
        String lrcText = null;
        try {
            InputStream is = new FileInputStream(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                long time = mediaPlayer.getCurrentPosition();
                mLrcView.updateTime(time);
            }

            lrcHandler.postDelayed(this, 100);
        }
    };

    /**
     * 规则pop
     */
    public void getRulePopup() {
        final PopupWindow rulePop = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.pop_rule, null);
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

        view.findViewById(R.id.colse_rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulePop.dismiss();
            }
        });
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setText("幸运28分为白天和夜场两种模式：\n" +
                "\n" +
                "白天采用北京快乐8数据（09：00-24：00）：\n" +
                "\n" +
                "北京快乐8开奖结果来源于国家福利彩票北京快乐8开奖号码，每五分钟一期，不停开奖。\n" +
                "北京快乐8每期开奖共开出20个号码，并按照从小到大的时序一次排列。\n" +
                "取其1-6位开奖号码并进行相加，和值的末位数作为幸运28开奖的第一位数值；\n" +
                "取其7-12位开奖号码并进行相加，和值的末位数作为幸运28开奖的第二位数值；\n" +
                "取其13-18位开奖号码并进行相加，和值的末位数作为幸运28开奖的第三位数值；\n" +
                "将三位数相加，所得的结果即为幸运28的开奖结果。\n" +
                "\n" +
                "夜场采用的是加拿大28数据（00：00-9：00）：\n" +
                "\n" +
                "加拿大28开奖结果来源于加拿大福利彩票的加拿大幸运28开奖结果\n" +
                "加拿大28每期开奖共开出20个号码，并按照从小到大的时序一次排列。\n" +
                "取其2、5、8、11、14、17位数进行相加，和值的末位数作为幸运28开奖的第一位数值；\n" +
                "取其3、6、9、12、15位数进行相加，和值的末位数作为幸运28开奖的第二位数值；\n" +
                "取其4、7、10、13、16、19位数进行相加，和值的末位数作为幸运28开奖的第三位数值；\n" +
                "将三位数相加，所得的结果即为幸运28的开奖结果。\n" +
                "\n" +
                "幸运28的玩法：\n" +
                "\n" +
                "28个号码，抽中即可获得奖励。玩法类型共有以下玩法：\n" +
                "\n" +
                "1、大、小、单、双\n" +
                "\n" +
                "2、小单、小双、大单、大双\n" +
                "\n" +
                "3、极小值（0-5）、极大值（22-27）\n" +
                "\n" +
                "4、28个号码定位\n" +
                "\n" +
                "5、红、绿、蓝、豹子");
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
        colse_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulePop.dismiss();
            }
        });

        tv_ds.setText("大小单双："+selectStatus);
        tv_xzjf.setText("下注积分："+String.valueOf(jbNumber*tzNumber)+"分");
    }


}
