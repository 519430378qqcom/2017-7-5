package com.lvshandian.lemeng.moudles.index.live;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.adapter.FamilyMemberAdapter;
import com.lvshandian.lemeng.base.BarrageDateBean;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.base.CustomStringCallBack;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.bean.BlBean;
import com.lvshandian.lemeng.bean.ControllerBean;
import com.lvshandian.lemeng.bean.CreatReadyBean;
import com.lvshandian.lemeng.bean.CustomGiftBean;
import com.lvshandian.lemeng.bean.CustomLianmaiBean;
import com.lvshandian.lemeng.bean.CustomdateBean;
import com.lvshandian.lemeng.bean.DianBoDateBean;
import com.lvshandian.lemeng.bean.GiftBean;
import com.lvshandian.lemeng.bean.LastAwardBean;
import com.lvshandian.lemeng.bean.LianMaiDateBean;
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
import com.lvshandian.lemeng.moudles.index.adapter.LianmaiListAadapter;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BankerBalances;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BankerInfo;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BetResult;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BullfightAudio;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BullfightInterface;
import com.lvshandian.lemeng.moudles.index.live.bullfight.BullfightPresenter;
import com.lvshandian.lemeng.moudles.index.live.bullfight.GameResult;
import com.lvshandian.lemeng.moudles.index.live.bullfight.PokerResult;
import com.lvshandian.lemeng.moudles.index.live.bullfight.StartResult;
import com.lvshandian.lemeng.moudles.index.live.bullfight.TimeAndNper;
import com.lvshandian.lemeng.moudles.index.live.gift.GiftFrameLayout;
import com.lvshandian.lemeng.moudles.index.live.gift.GiftSendModel;
import com.lvshandian.lemeng.moudles.index.live.utils.AnchorVideoOne;
import com.lvshandian.lemeng.moudles.mine.bean.Funse;
import com.lvshandian.lemeng.moudles.mine.bean.FunseBean;
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
import com.lvshandian.lemeng.utils.DateUtils;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.lvshandian.lemeng.utils.JavaBeanMapUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.KeyBoardUtils;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.QosThread;
import com.lvshandian.lemeng.utils.SendRoomMessageUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.ThreadManager;
import com.lvshandian.lemeng.utils.UMUtils;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment.ChatRoomMessageFragment;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.helper.ChatRoomMemberCache;
import com.lvshandian.lemeng.wangyiyunxin.live.fragment.ChatRoomSessionListFragment;
import com.lvshandian.lemeng.wangyiyunxin.live.fragment.LiveMessageFragment;
import com.lvshandian.lemeng.wangyiyunxin.main.helper.SystemMessageUnreadManager;
import com.lvshandian.lemeng.wangyiyunxin.main.reminder.ReminderItem;
import com.lvshandian.lemeng.wangyiyunxin.main.reminder.ReminderManager;
import com.lvshandian.lemeng.wangyiyunxin.main.reminder.ReminderSettings;
import com.lvshandian.lemeng.widget.lrcview.LrcView;
import com.lvshandian.lemeng.widget.myrecycler.RefreshRecyclerView;
import com.lvshandian.lemeng.widget.myrecycler.manager.RecyclerMode;
import com.lvshandian.lemeng.widget.myrecycler.manager.RecyclerViewManager;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.lvshandian.lemeng.widget.view.AvatarView;
import com.lvshandian.lemeng.widget.view.BarrageView;
import com.lvshandian.lemeng.widget.view.CustomPopWindow;
import com.lvshandian.lemeng.widget.view.RotateLayout;
import com.lvshandian.lemeng.widget.view.RoundDialog;
import com.lvshandian.lemeng.widget.view.TimeCountDownLayout;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
        StreamingStateChangedListener, com.lvshandian.lemeng.widget.view.CameraPreviewFrameView
        .Listener, MediaPlayer.OnCompletionListener
        , SeekBar.OnSeekBarChangeListener
        , BullfightInterface {
    @Bind(R.id.live_head)
    AvatarView liveHead;
    @Bind(R.id.live_lianmai_head)
    AvatarView liveLianmaiHead;
    @Bind(R.id.live_lianmai_head_bg1)
    AvatarView liveLianmaiHeadBg1;
    @Bind(R.id.live_lianmai_head_bg2)
    AvatarView liveLianmaiHeadBg2;
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
    @Bind(R.id.iv_live_privatechat)
    ImageView ivLivePrivatechat;
    @Bind(R.id.iv_live_switch)
    ImageView ivLiveSwitch;
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
    @Bind(R.id.barrageview)
    BarrageView barrageview;
    @Bind(R.id.start_room_jiaZu)
    ImageView startRoomJaiZu;
    @Bind(R.id.game_more_btn)
    ImageView game_more_btn;
    @Bind(R.id.game)
    ImageView game;
    @Bind(R.id.ruanjianpan)
    ImageView ruanjianpan;
    @Bind(R.id.room_lianmai)
    ImageView room_lianmai;
    @Bind(R.id.tv_lianmai)
    TextView tv_lianmai;
    @Bind(R.id.ll_game)
    LinearLayout ll_game;
    @Bind(R.id.iv_xy)
    ImageView iv_xy;
    @Bind(R.id.live_game)
    AutoLinearLayout live_game;
    @Bind(R.id.watch_room_message_fragment_chat)
    FrameLayout watch_room_message_fragment_chat;
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
    @Bind(R.id.iv_touzhu)
    ImageView ivTouzhu;
    @Bind(R.id.lucky_bet)
    EditText etLuckyBet;
    @Bind(R.id.recharge)
    TextView recharge;
    @Bind(R.id.tv_rule)
    ImageView tv_rule;
    @Bind(R.id.ll_trendOrHistory)
    LinearLayout ll_trendOrHistory;
    @Bind(R.id.rl_record)
    RelativeLayout rl_record;
    @Bind(R.id.rl_trend)
    RelativeLayout rl_trend;
    @Bind(R.id.all_lepiao)
    TextView all_lepiao;

    @Bind(R.id.tv_periods)
    TextView tv_periods;
    @Bind(R.id.frist_num)
    TextView frist_num;
    @Bind(R.id.second_num)
    TextView second_num;
    @Bind(R.id.third_num)
    TextView third_num;
    @Bind(R.id.all_num)
    TextView all_num;
    @Bind(R.id.tv_ds)
    TextView tv_ds;
    @Bind(R.id.tv_game_next_open_time)
    TimeCountDownLayout tv_game_next_open_time;
    @Bind(R.id.tv_next)
    TextView tv_next;
    @Bind(R.id.rl_kp)
    LinearLayout rl_kp;
    @Bind(R.id.tv_hz)
    TextView tv_hz;
    //<start------------斗牛游戏部分-------------->
    @Bind(R.id.rl_game_container)
    RelativeLayout rl_game_container;
    @Bind(R.id.iv_bullfight)
    ImageView iv_bullfight;
    @Bind(R.id.live_game_bullfight)
    RelativeLayout live_game_bullfight;
    @Bind(R.id.rl_game_info)
    RelativeLayout rl_game_info;
    @Bind(R.id.rl_bullfight_banker)
    AutoRelativeLayout rl_bullfight_banker;
    @Bind(R.id.iv_bullfight_banker_head)
    CircleImageView iv_bullfight_banker_head;
    @Bind(R.id.tv_bullfight_bankername)
    TextView tv_bullfight_bankername;
    @Bind(R.id.tv_bullfight_banker_money)
    TextView tv_bullfight_banker_money;
    @Bind(R.id.iv_poker_banker1)
    ImageView iv_poker_banker1;
    @Bind(R.id.iv_poker_banker2)
    ImageView iv_poker_banker2;
    @Bind(R.id.iv_poker_banker3)
    ImageView iv_poker_banker3;
    @Bind(R.id.iv_poker_banker4)
    ImageView iv_poker_banker4;
    @Bind(R.id.iv_poker_banker5)
    ImageView iv_poker_banker5;
    @Bind(R.id.tv_bullfight_totlanum1)
    TextView tv_bullfight_totlanum1;
    @Bind(R.id.tv_bullfight_minenum1)
    TextView tv_bullfight_minenum1;
    @Bind(R.id.rl_bullfight_betting_container1)
    AutoRelativeLayout rl_bullfight_betting_container1;
    @Bind(R.id.iv_poker_palyer11)
    ImageView iv_poker_palyer11;
    @Bind(R.id.iv_poker_palyer12)
    ImageView iv_poker_palyer12;
    @Bind(R.id.iv_poker_palyer13)
    ImageView iv_poker_palyer13;
    @Bind(R.id.iv_poker_palyer14)
    ImageView iv_poker_palyer14;
    @Bind(R.id.iv_poker_palyer15)
    ImageView iv_poker_palyer15;
    @Bind(R.id.rl_bullfight1)
    AutoRelativeLayout rl_bullfight1;
    @Bind(R.id.tv_bullfight_totlanum2)
    TextView tv_bullfight_totlanum2;
    @Bind(R.id.tv_bullfight_minenum2)
    TextView tv_bullfight_minenum2;
    @Bind(R.id.rl_bullfight_betting_container2)
    AutoRelativeLayout rl_bullfight_betting_container2;
    @Bind(R.id.iv_poker_palyer21)
    ImageView iv_poker_palyer21;
    @Bind(R.id.iv_poker_palyer22)
    ImageView iv_poker_palyer22;
    @Bind(R.id.iv_poker_palyer23)
    ImageView iv_poker_palyer23;
    @Bind(R.id.iv_poker_palyer24)
    ImageView iv_poker_palyer24;
    @Bind(R.id.iv_poker_palyer25)
    ImageView iv_poker_palyer25;
    @Bind(R.id.rl_bullfight2)
    AutoRelativeLayout rl_bullfight2;
    @Bind(R.id.tv_bullfight_totlanum3)
    TextView tv_bullfight_totlanum3;
    @Bind(R.id.tv_bullfight_minenum3)
    TextView tv_bullfight_minenum3;
    @Bind(R.id.rl_bullfight_betting_container3)
    AutoRelativeLayout rl_bullfight_betting_container3;
    @Bind(R.id.iv_poker_palyer31)
    ImageView iv_poker_palyer31;
    @Bind(R.id.iv_poker_palyer32)
    ImageView iv_poker_palyer32;
    @Bind(R.id.iv_poker_palyer33)
    ImageView iv_poker_palyer33;
    @Bind(R.id.iv_poker_palyer34)
    ImageView iv_poker_palyer34;
    @Bind(R.id.iv_poker_palyer35)
    ImageView iv_poker_palyer35;
    @Bind(R.id.rl_bullfight3)
    AutoRelativeLayout rl_bullfight3;
    @Bind(R.id.tv_timing)
    TextView tv_timing;
    @Bind(R.id.tv_bullfight_result1)
    TextView tv_bullfight_result1;
    @Bind(R.id.tv_bullfight_result2)
    TextView tv_bullfight_result2;
    @Bind(R.id.tv_bullfight_result3)
    TextView tv_bullfight_result3;
    @Bind(R.id.ll_bullfight_result)
    AutoLinearLayout ll_bullfight_result;
    @Bind(R.id.rl_timing)
    AutoRelativeLayout rl_timing;
    @Bind(R.id.tv_bullfight_lepiao)
    TextView tv_bullfight_lepiao;
    @Bind(R.id.tv_bullfight_top_up)
    TextView tv_bullfight_top_up;
    @Bind(R.id.iv_10)
    ImageView iv_10;
    @Bind(R.id.iv_50)
    ImageView iv_50;
    @Bind(R.id.iv_100)
    ImageView iv_100;
    @Bind(R.id.iv_1000)
    ImageView iv_1000;
    @Bind(R.id.iv_10000)
    ImageView iv_10000;
    @Bind(R.id.iv_bull_amount0)
    ImageView iv_bull_amount0;
    @Bind(R.id.iv_bull_amount1)
    ImageView iv_bull_amount1;
    @Bind(R.id.iv_bull_amount2)
    ImageView iv_bull_amount2;
    @Bind(R.id.iv_bull_amount3)
    ImageView iv_bull_amount3;
    @Bind(R.id.rl_poker_banker_container)
    AutoRelativeLayout rl_poker_banker_container;
    @Bind(R.id.rl_poker_player_container1)
    AutoRelativeLayout rl_poker_player_container1;
    @Bind(R.id.rl_poker_player_container2)
    AutoRelativeLayout rl_poker_player_container2;
    @Bind(R.id.rl_poker_player_container3)
    AutoRelativeLayout rl_poker_player_container3;
    @Bind(R.id.iv_gray_bg1)
    ImageView iv_gray_bg1;
    @Bind(R.id.iv_gray_bg2)
    ImageView iv_gray_bg2;
    @Bind(R.id.iv_gray_bg3)
    ImageView iv_gray_bg3;
    @Bind(R.id.iv_bullcoin)
    ImageView iv_bullcoin;
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
     * 斗牛期数
     */
    private int uper;
    /**
     * 发送斗牛倒计时的消息标识
     */
    private final int BULLFIGHT_TIME = 233;

    private BullfightPresenter bullfightPresenter;
    /**
     * 选中的投注金额
     */
    private int betBalance;
    /**
     * 投注位置
     */
    private int betPosition;
    /**
     * 斗牛音效
     */
    private BullfightAudio bullfightAudio;
    //<end------------斗牛游戏部分-------------->

    private int luckySet;

    private ArrayList<Integer> JbList = new ArrayList<>();

    /**
     * 直播开启时间
     */
    private long startTime;

    /**
     * 计时器
     */
    private Timer timer = new Timer();

    /**
     * 连麦邀请计时器
     */
    private CountDownTimer lianmaiTimer;

    /**
     * 网易云信生成的id
     */
    private String wy_Id = "";

    /**
     * 后台生成的id
     */
    private String room_Id = "";

    /**
     * 拉流地址
     */
    private String mVideoPath = "";

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
     * 连麦人的Id
     */
    private String lianId = null;

    /**
     * 获取到礼物面板的集合
     */
    private List<GiftBean> mGiftList = new ArrayList<>();

    /**
     * 发送礼物时，礼物的图片地址
     */
    private String staticIcon;

    /**
     * 左上角主播收到金币数
     */
    private Long zhuboReceve;

    /**
     * 自己金币数
     */
    private Long myGoldCoin;

    /**
     * 礼物动画列表
     */
    private List<GiftSendModel> giftSendModelList = new ArrayList<>();

    /**
     * 连麦人信息
     */
    private CustomLianmaiBean customLianmaiBean;


    private long mCountDownTotalTime;

    /**
     * 是否为游戏直播间
     */
    private boolean gameIsStart = false;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                //礼物列表
                case RequestCode.GET_GIFT:
                    mGiftList = JsonUtil.json2BeanList(json, GiftBean.class);
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
//                    finish();
//                    startActivity(new Intent(StartLiveActivity.this, QuitLiveActivity.class).putExtra
//                            ("roomId", room_Id).putExtra("startTime", startTime));
                    break;
                case RequestCode.TIMERLIVE:
                    LogUtils.i("主播隔一段时间刷新状态" + json.toString());
                    break;
                case 1000:
                    LogUtils.i("主播隔一段时间刷新状态");
                    httpDatas.getDataDialog("主播隔一段时间刷新状态", false, urlBuilder.TimerLive(room_Id),
                            myHandler, RequestCode.TIMERLIVE);
                    break;
                case RequestCode.SELECT_USER:
                    LogUtils.e("查询个人信息返回json: " + json);
                    AppUser userZhubo = JsonUtil.json2Bean(json, AppUser.class);
                    String receivedGoldCoin = userZhubo.getReceivedGoldCoin();
                    String goldCoin = userZhubo.getGoldCoin();

                    zhuboReceve = Long.parseLong(receivedGoldCoin);
                    myGoldCoin = Long.parseLong(goldCoin);
                    receivedGoldCoin = CountUtils.getCount(Long.parseLong(receivedGoldCoin));
                    liveJinpiao.setText(receivedGoldCoin); //显示左上角主播收到乐票数量
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
            }
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String nper;
    private int intQh;
    private String countryType;
    private boolean isTouZhu;//是否可以投注


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
        }, 10000, 10000);

        getGiftList();
        //进入房间
//        requestNet();
        PowerManager mPm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWl = mPm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MyTag");
        requestPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        try {
            initLivePlay(DESUtil.decrypt(creatReadyBean.getPublishUrl()));
            LogUtils.i("推流地址　：" + DESUtil.decrypt(creatReadyBean.getPublishUrl()));
            mVideoPath = DESUtil.decrypt(creatReadyBean.getBroadcastUrl());
            LogUtils.i("拉流地址　：" + mVideoPath + "---wy_Id=" + wy_Id);
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

        liveId.setText(getString(R.string.lemeng_id, creatReadyBean.getCreator().getId()));
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

        initSelectStatus();
    }

    private void initSelectStatus() {
        restStatus();
        ivBig.setImageResource(R.mipmap.icon_big_select);
        selectStatus = getString(R.string.big);
        tv_hz.setText(getString(R.string.big_info));
    }

    @Override
    protected void initListener() {
        rl_trend.setOnClickListener(this);
        rl_record.setOnClickListener(this);
        tv_rule.setOnClickListener(this);
        ivTouzhu.setOnClickListener(this);
        recharge.setOnClickListener(this);

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

        llTangpiao.setOnClickListener(this);
        liveHead.setOnClickListener(this);
        liveLianmaiHead.setOnClickListener(this);
        liveLianmaiHeadBg1.setOnClickListener(this);
        liveLianmaiHeadBg2.setOnClickListener(this);
        game.setOnClickListener(this);
        ruanjianpan.setOnClickListener(this);
        room_lianmai.setOnClickListener(this);
        tv_lianmai.setOnClickListener(this);
        ll_game.setOnClickListener(this);
        iv_xy.setOnClickListener(this);
        liveClose.setOnClickListener(this);
        ivLiveSwitch.setOnClickListener(this);
        ivLivePrivatechat.setOnClickListener(this);
        startRoomJaiZu.setOnClickListener(this);
        roomShowId.setOnClickListener(this);
        game_more_btn.setOnClickListener(this);
        ivTouzhu.setOnClickListener(this);
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
            case R.id.recharge: //幸运28充值
                showToast(getString(R.string.stay_open));
                break;
            case R.id.rl_trend: //走势
                showHistoryDialog(1);
                break;
            case R.id.rl_record: //记录
                showHistoryDialog(2);
                break;

            case R.id.tv_rule: //规则
                showHistoryDialog(3);
                break;
            case R.id.iv_touzhu:  //投注
                if (isTouZhu) {
                    if (TextUtils.isEmpty(etLuckyBet.getText().toString().trim())) {
                        showToast(getString(R.string.lucky_set_empty));
                        return;
                    }
                    luckySet = Integer.parseInt(etLuckyBet.getText().toString().trim());
                    if (luckySet < 10) {
                        showToast(getString(R.string.must_more_10));
                        return;
                    }
                    if (luckySet > 1600000) {
                        showToast(getString(R.string.must_small_160000));
                        return;
                    }
                    showTouZhuPop(selectStatus, luckySet);
                } else {
                    showToast(getString(R.string.no_info_try_again_later));
                }
                break;

            case R.id.iv_big: //大
                restStatus();
                ivBig.setImageResource(R.mipmap.icon_big_select);
                selectStatus = getString(R.string.big);
                tv_hz.setText(getString(R.string.big_info));
                break;
            case R.id.iv_samll: //小
                restStatus();
                ivSamll.setImageResource(R.mipmap.icon_small_select);
                selectStatus = getString(R.string.samll);
                tv_hz.setText(getString(R.string.samll_info));
                break;
            case R.id.iv_singe: //单
                restStatus();
                ivSinge.setImageResource(R.mipmap.icon_single_select);
                selectStatus = getString(R.string.singe);
                tv_hz.setText(getString(R.string.singe_info));
                break;
            case R.id.iv_double: //双
                restStatus();
                ivDouble.setImageResource(R.mipmap.icon_double_select);
                selectStatus = getString(R.string.double_);
                tv_hz.setText(getString(R.string.double_info));
                break;
            case R.id.iv_big_sigle: //大单
                restStatus();
                ivBigSigle.setImageResource(R.mipmap.icon_big_single_select);
                selectStatus = getString(R.string.big_sigle);
                tv_hz.setText(getString(R.string.big_sigle_info));
                break;
            case R.id.iv_samll_singe: //小单
                restStatus();
                ivSamllSinge.setImageResource(R.mipmap.icon_small_single_select);
                selectStatus = getString(R.string.samll_singe);
                tv_hz.setText(getString(R.string.samll_singe_info));
                break;
            case R.id.iv_big_double: //大双
                restStatus();
                ivBigDouble.setImageResource(R.mipmap.icon_big_double_select);
                selectStatus = getString(R.string.big_double);
                tv_hz.setText(getString(R.string.big_double_info));
                break;
            case R.id.iv_samll_double: //小双
                restStatus();
                ivSamllDouble.setImageResource(R.mipmap.icon_small_double_select);
                selectStatus = getString(R.string.samll_double);
                tv_hz.setText(getString(R.string.samll_double_info));
                break;
            case R.id.iv_more_big: //极大
                restStatus();
                ivMoreBig.setImageResource(R.mipmap.icon_big_more_select);
                selectStatus = getString(R.string.more_big);
                tv_hz.setText(getString(R.string.more_big_info));
                break;
            case R.id.iv_more_samll: //极小
                restStatus();
                ivMoreSamll.setImageResource(R.mipmap.icon_small_more_select);
                selectStatus = getString(R.string.more_samll);
                tv_hz.setText(getString(R.string.more_samll_info));
                break;
            case R.id.iv_xy:
                showXYGame();
                break;
            case R.id.ruanjianpan:
                messageFragment.showEditText();
                break;
            case R.id.room_lianmai://邀请连麦按钮
                showLianmaiPop();
                break;
            /**
             * 关闭拉流小窗口关闭拉流地址
             */
            case R.id.tv_lianmai://结束连麦按钮
                if (tv_lianmai.getText().toString().equals(getString(R.string.over_the_mic))) {
                    hindSmallVideo();
                    Map<String, Object> map = new HashMap<>();
                    map.put("watch_private_flag", "0");
                    map.put("vip", "0");
                    SendRoomMessageUtils.onCustomMessageLianmai(SendRoomMessageUtils.MESSAGE_WATCHER_DISCONNECT, messageFragment, wy_Id,
                            map);
                }
                break;
            case R.id.game:  //游戏
                if (gameIsStart == false) {
                    if (ll_game.getVisibility() == View.VISIBLE) {
                        ll_game.setVisibility(View.GONE);
                    } else {
                        ll_game.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (!gameHide) {
                        hidePlayView(gameType);
                    } else {
                        showPlayView(gameType);
                    }
                }
                break;
            case R.id.game_more_btn:
                View popupView = LayoutInflater.from(this).inflate(R.layout.view_more_button, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
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
                        UMUtils.umShare(StartLiveActivity.this, getString(R.string.share_live_title, appUser.getNickName()),
                                getString(R.string.share_live_content, appUser.getNickName()), creatReadyBean.getLivePicUrl(),
                                String.format(UrlBuilder.SHARE_VIDEO_URL, wy_Id, appUser.getId(), mVideoPath));
                        popupWindow.dismiss();
                    }
                });
                popupView.findViewById(R.id.audio_player).setOnClickListener(new View.OnClickListener() { //音乐
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, AudioPlayerActivity.class));
                        popupWindow.dismiss();
                    }
                });

                popupView.findViewById(R.id.iv_live_meiyan).setOnClickListener(new View.OnClickListener() { //美颜
                    @Override
                    public void onClick(View v) {
                        getMeiyanPopup();
                        popupWindow.dismiss();
                    }
                });

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
            //摄像头旋转
            case R.id.iv_live_switch:
                mHandler.removeCallbacks(mSwitcher);
                mHandler.postDelayed(mSwitcher, 100);
                break;
            case R.id.live_head://主播头像
                ifattention("请求用户信息", appUser.getId(), RequestCode.REQUEST_USER_INFO);
                break;
            case R.id.live_lianmai_head_bg1://主播头像
                ifattention("请求用户信息", appUser.getId(), RequestCode.REQUEST_USER_INFO);
                break;
            case R.id.live_lianmai_head://连麦人的头像
                ifattention("请求用户信息", customLianmaiBean.getUserId(), RequestCode.REQUEST_USER_INFO);
                break;
            case R.id.live_lianmai_head_bg2://连麦人的头像
                ifattention("请求用户信息", customLianmaiBean.getUserId(), RequestCode.REQUEST_USER_INFO);
                break;
            //家族
            case R.id.start_room_jiaZu:
                getFamilyMember();
                break;
            //場控
            case R.id.room_id:
                request();
                break;
        }
    }
    //---------------------------斗牛代码---------------------------

    /**
     * 牛牛点击事件
     */
    @OnClick({R.id.iv_bullfight, R.id.rl_bullfight1, R.id.rl_bullfight2, R.id.rl_bullfight3,
            R.id.tv_bullfight_top_up, R.id.iv_10, R.id.iv_50, R.id.iv_100, R.id.iv_1000, R.id.iv_10000})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_bullfight:
                startBullfightGame();
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

    /**
     * 投注斗牛
     *
     * @param position 位置（1，2，3）
     */
    private void betBullfight(int position) {
        betPosition = position;
        if (betBalance > 0) {
            if (betBalance > myGoldCoin) {
                showToast(getString(R.string.balance_not_enough));
            } else {
                bullfightPresenter.betSuccess(Integer.parseInt(appUser.getId()), Integer.parseInt(room_Id), betBalance, position, uper, 0);
            }
        } else {
            showToast(getString(R.string.no_select_betbalance));
        }
    }

    private int total1;
    private int total2;
    private int total3;

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
                total1 += betSum;
                tv_bullfight_totlanum1.setText(CountUtils.getCount(total1));
                break;
            case 2:
                bettingPoolView = rl_bullfight_betting_container2;
                total2 += betSum;
                tv_bullfight_totlanum2.setText(CountUtils.getCount(total2));
                break;
            case 3:
                bettingPoolView = rl_bullfight_betting_container3;
                total3 += betSum;
                tv_bullfight_totlanum3.setText(CountUtils.getCount(total3));
                break;
        }
        RelativeLayout.LayoutParams layoutParams = new AutoRelativeLayout.LayoutParams(iv_10.getWidth(), iv_10.getHeight());
        layoutParams.leftMargin = (int) (Math.random() * (rl_bullfight_betting_container1.getWidth() - layoutParams.width));
        layoutParams.topMargin = (int) (Math.random() * (rl_bullfight_betting_container1.getHeight() - layoutParams.height));
        if (bettingPoolView.getChildCount() >= BETTING_POOL_VIEWS_CAPACITY) {
            bettingPoolView.removeViewAt(0);
        }
        bettingPoolView.addView(imageView, layoutParams);
        if (isAnimation) {
            imageView.setVisibility(View.GONE);
            ImageView iv_start = iv_10;
            int imgId = R.mipmap.ic_bullfight_10_light;
            switch (betSum) {
                case 10:
                    iv_start = iv_10;
                    imgId = R.mipmap.ic_bullfight_10_light;
                    break;
                case 50:
                    iv_start = iv_50;
                    imgId = R.mipmap.ic_bullfight_50_light;
                    break;
                case 100:
                    iv_start = iv_100;
                    imgId = R.mipmap.ic_bullfight_100_light;
                    break;
                case 1000:
                    iv_start = iv_1000;
                    imgId = R.mipmap.ic_bullfight_1000_light;
                    break;
                case 10000:
                    iv_start = iv_10000;
                    imgId = R.mipmap.ic_bullfight_10000_light;
                    break;
            }
            final RelativeLayout parent = (RelativeLayout) iv_start.getParent().getParent().getParent();
            int[] startLocation = new int[2];
            int[] endLocation = new int[2];
            int[] parentLocation = new int[2];
            iv_start.getLocationOnScreen(startLocation);
            imageView.getLocationOnScreen(endLocation);
            endLocation[0] = endLocation[0] + layoutParams.leftMargin;
            endLocation[1] = endLocation[1] + layoutParams.topMargin;
            parent.getLocationOnScreen(parentLocation);
            final ImageView imageView1 = new ImageView(this);
            imageView1.setImageResource(imgId);
            RelativeLayout.LayoutParams layoutParams1 = new AutoRelativeLayout.LayoutParams(iv_10.getWidth(), iv_10.getHeight());
            layoutParams1.leftMargin = startLocation[0] - parentLocation[0];
            layoutParams1.topMargin = startLocation[1] - parentLocation[1];
            final int dx = startLocation[0] - endLocation[0];
            final int dy = startLocation[1] - endLocation[1];
            parent.addView(imageView1, layoutParams1);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1);
            valueAnimator.setTarget(imageView1);
            valueAnimator.setDuration(500);
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    parent.removeView(imageView1);
                    imageView.setVisibility(View.VISIBLE);
                }
            });
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    imageView1.setTranslationX(-dx * animatedValue);
                    imageView1.setTranslationY(-dy * animatedValue);
                }
            });
            valueAnimator.start();
        }
    }

    /**
     * 选择投注金额
     *
     * @param betSum 投注数
     */
    private void checkBettingBalance(int betSum) {
        if (betSum > myGoldCoin) {
            showToast(getString(R.string.balance_not_enough));
            return;
        }
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
        bullfightPresenter = new BullfightPresenter(this);
        bullfightPresenter.startBullGame(room_Id);
    }

    /**
     * 斗牛倒计时
     */
    private void bullfightTimer() {
        myHandler.sendEmptyMessageDelayed(BULLFIGHT_TIME, 1000);
        if (nextTime >= 15) {
            switchTimer();
        }
        if (nextTime <= 5 && nextTime > 0) {
            bullfightResultShow(null, null, getString(R.string.take_a_rest) + nextTime + "S");
        }
        switch (nextTime) {
            case 30://主播初始化游戏结果
                bullfightPresenter.initGameResult(room_Id);
                break;
            case 15://获取扑克牌结果
                setBetPoolEnable(false);
                rl_timing.setVisibility(View.GONE);
                bullfightPresenter.getPokerResult(room_Id);
                bullfightPresenter.computeAllResult(room_Id, uper + "");
                break;
            case 8://开奖
                bullfightPresenter.getGameResult(room_Id, uper + "", appUser.getId());
                bullfightPresenter.updateBankerBalance();
                break;
            case 0://主播更新倒计时
                myHandler.removeMessages(BULLFIGHT_TIME);
                bullfightPresenter.initGameTimer(room_Id, uper + "", appUser.getId() + "");
                break;
        }
        nextTime--;
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
            tv_bullfight_result1.setVisibility(View.GONE);
        } else {
            tv_bullfight_result1.setVisibility(View.VISIBLE);
            tv_bullfight_result1.setText(arg1);
        }
        if (TextUtils.isEmpty(arg2)) {
            tv_bullfight_result2.setVisibility(View.GONE);
        } else {
            tv_bullfight_result2.setVisibility(View.VISIBLE);
            tv_bullfight_result2.setText(arg2);
        }
        if (TextUtils.isEmpty(arg3)) {
            tv_bullfight_result3.setVisibility(View.GONE);
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
        int time = nextTime - 15;
        tv_timing.setText(time + "S");
    }

    @Override
    public void getGameResult(GameResult gameResult) {
        if (gameResult.isSuccess()) {
            switch (gameResult.getCode()) {
                case 0://没用中奖
                    break;
                case 1://中奖
                    int mount = gameResult.getObj().getMount();
                    String mine = mount >= 0 ? "+" + mount : mount + "";
                    String banker = gameResult.getObj().getTmount() >= 0 ? "+" + gameResult.getObj().getTmount() : gameResult.getObj().getTmount() + "";
                    bullfightResultShow(getString(R.string.the_result), getString(R.string.the_user) +
                            mine, getString(R.string.banker) + banker);
                    if (mount > 0) {
                        bullfightAudio.play(bullfightAudio.WIN);
                        bullfightAudio.play(bullfightAudio.FALLING_COIN);
                        fallingCoinAnimation(true, 0);
                        fallingCoinAnimation(true, 100);
                        fallingCoinAnimation(true, 200);
                        fallingCoinAnimation(true, 300);
                        fallingCoinAnimation(true, 400);
                        fallingCoinAnimation(true, 500);
                    } else if (mount < 0) {
                        bullfightAudio.play(bullfightAudio.FAIL);
                        bullfightAudio.play(bullfightAudio.FALLING_COIN);
                        fallingCoinAnimation(false, 0);
                        fallingCoinAnimation(false, 100);
                        fallingCoinAnimation(false, 200);
                        fallingCoinAnimation(false, 300);
                        fallingCoinAnimation(false, 400);
                        fallingCoinAnimation(false, 500);
                    }
                    myGoldCoin += gameResult.getObj().getAmount();
                    tv_bullfight_lepiao.setText(CountUtils.getCount(myGoldCoin));
                    break;
                case 2://异常

                    break;
            }
        }
    }

    /**
     * 金币掉落的动画
     *
     * @param isFalling true为掉落动画false反动画
     * @param delay     动画延迟执行
     */
    private void fallingCoinAnimation(final boolean isFalling, long delay) {
        final RelativeLayout parent = (RelativeLayout) iv_bullcoin.getParent().getParent();
        int[] startLocation = new int[2];
        int[] endLocation = new int[2];
        int[] parentLocation = new int[2];
        ll_bullfight_result.getLocationOnScreen(startLocation);
        iv_bullcoin.getLocationOnScreen(endLocation);
        parent.getLocationOnScreen(parentLocation);
        startLocation[0] = startLocation[0] + ll_bullfight_result.getWidth() / 2 - iv_bullcoin.getWidth() / 2;
        startLocation[1] = startLocation[1] + ll_bullfight_result.getHeight() / 2 - iv_bullcoin.getHeight() / 2;
        final int dx = startLocation[0] - endLocation[0];
        final int dy = startLocation[1] - endLocation[1];
        final ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.niu_jinbi);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(iv_bullcoin.getWidth(), iv_bullcoin.getHeight());
        if (isFalling) {
            layoutParams.leftMargin = startLocation[0] - parentLocation[0];
            layoutParams.topMargin = startLocation[1] - parentLocation[1];
        } else {
            layoutParams.leftMargin = endLocation[0] - parentLocation[0];
            layoutParams.topMargin = endLocation[1] - parentLocation[1];
        }
        parent.addView(imageView, layoutParams);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1);
        valueAnimator.setTarget(imageView);
        valueAnimator.setDuration(500);
        valueAnimator.setStartDelay(delay);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                parent.removeView(imageView);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if (isFalling) {
                    animatedValue = -animatedValue;
                }
                imageView.setTranslationX(animatedValue * dx);
                imageView.setTranslationY(animatedValue * dy);
            }
        });
        valueAnimator.start();
    }

    @Override
    public void startBullGame(StartResult result) {
        if (result.isSuccess()) {
            bullfightPresenter.getTimeAndNper(room_Id);
            bullfightPresenter.getBankerInfo();
            Map<String, Object> map = new HashMap<>();
            map.put("vip", appUser.getVip());
            map.put("userId", appUser.getId());
            map.put("level", appUser.getLevel());
            map.put("NIM_BEGIN_GAME_NIUNIU", getString(R.string.start_niuniu_game));
            SendRoomMessageUtils.onCustomMessagePlay("2929", messageFragment, wy_Id, map);
        } else {
            Toast.makeText(StartLiveActivity.this, R.string.game_start_fail, Toast.LENGTH_SHORT).show();
        }
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
    public void initGameTimer() {
        bullfightPresenter.getTimeAndNper(room_Id);
        Map<String, Object> map = new HashMap<>();
        map.put("vip", appUser.getVip());
        map.put("userId", appUser.getId());
        map.put("level", appUser.getLevel());
        map.put("NIM_CONTER_TIMER_MPER", uper + "");
        SendRoomMessageUtils.onCustomMessagePlay("3131", messageFragment, wy_Id, map);
    }

    @Override
    public void getTimeAndNPer(TimeAndNper timeAndNper) {
        if (timeAndNper.isSuccess()) {
            setBetPoolEnable(true);
            ll_bullfight_result.setVisibility(View.INVISIBLE);
            rl_timing.setVisibility(View.VISIBLE);
            rl_bullfight_betting_container1.removeAllViews();
            rl_bullfight_betting_container2.removeAllViews();
            rl_bullfight_betting_container3.removeAllViews();
            iv_gray_bg1.setVisibility(View.GONE);
            iv_gray_bg2.setVisibility(View.GONE);
            iv_gray_bg3.setVisibility(View.GONE);
            amount1 = 0;
            amount2 = 0;
            amount3 = 0;
            total1 = 0;
            total2 = 0;
            total3 = 0;
            tv_bullfight_totlanum1.setText("0");
            tv_bullfight_totlanum2.setText("0");
            tv_bullfight_totlanum3.setText("0");
            tv_bullfight_minenum1.setText("0");
            tv_bullfight_minenum2.setText("0");
            tv_bullfight_minenum3.setText("0");
            nextTime = timeAndNper.getObj().getTime();
            uper = timeAndNper.getObj().getPerid();
            ll_game.setVisibility(View.GONE);
            if (!gameIsStart) {
                showPlayView(2);
            }
            gameIsStart = true;
            updateBettingEnable(myGoldCoin);
            switchBullNum(false, -1);
            myHandler.sendEmptyMessage(BULLFIGHT_TIME);
            switchAllPoker(false, -1);
            sendPokerAnimator();
            if (bullfightAudio == null) {
                bullfightAudio = new BullfightAudio(getApplicationContext());
            }
            if (betBalance < 10 && myGoldCoin >= 10) {
                checkBettingBalance(10);
            }
        } else {
        }
    }

    private long amount1;
    private long amount2;
    private long amount3;

    @Override
    public void betSuccess(BetResult betResult, int amount, int type) {
        int code = betResult.getCode();
        switch (code) {
            case 0://为异常
                showToast(getString(R.string.bet_fail));
                break;
            case 1://为成功
                bullfightAudio.play(bullfightAudio.BET);
                myGoldCoin -= amount;
                tv_bullfight_lepiao.setText(CountUtils.getCount(myGoldCoin));
                updateBettingEnable(myGoldCoin);
                addBettingView(type, amount, true);
                switch (type) {
                    case 1:
                        amount1 += amount;
                        tv_bullfight_minenum1.setText(CountUtils.getCount(amount1));
                        break;
                    case 2:
                        amount2 += amount;
                        tv_bullfight_minenum2.setText(CountUtils.getCount(amount2));
                        break;
                    case 3:
                        amount3 += amount;
                        tv_bullfight_minenum3.setText(CountUtils.getCount(amount3));
                        break;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("vip", appUser.getVip());
                map.put("userId", appUser.getId());
                map.put("level", appUser.getLevel());
                switch (type) {
                    case 1:
                        map.put("NIM_TOUZHU_GOLD_SELECT_1", amount + "");
                        break;
                    case 2:
                        map.put("NIM_TOUZHU_GOLD_SELECT_2", amount + "");
                        break;
                    case 3:
                        map.put("NIM_TOUZHU_GOLD_SELECT_3", amount + "");
                        break;
                }
                SendRoomMessageUtils.onCustomMessagePlay("3030", messageFragment, wy_Id, map);
                break;
            case 2://为钱币不够赔
                break;
            case 3://余额不足
                showToast(getString(R.string.balance_not_enough));
                break;
        }
    }

    @Override
    public void updataBankerBalance(BankerBalances BankerBalance) {
        int balance = BankerBalance.getObj();
        String count = CountUtils.getCount(balance);
        tv_bullfight_banker_money.setText(count);
    }

    @Override
    public void getPokerResult(PokerResult pokerResult) {
        if (pokerResult.isSuccess()) {
            final PokerResult.ObjBean.PlayerPokerMapBean playerPokerMap = pokerResult.getObj().getPlayerPokerMap();
            final int result = playerPokerMap.getPoker0().getResult();
            final int result1 = playerPokerMap.getPoker1().getResult();
            final int result2 = playerPokerMap.getPoker2().getResult();
            final int result3 = playerPokerMap.getPoker3().getResult();
            final ObjectAnimator animator3 = ObjectAnimator.ofFloat(rl_poker_player_container3, "scaleX", 0f, 1f);
            animator3.setDuration(1000);
            animator3.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    List<PokerResult.ObjBean.PlayerPokerMapBean.PokersBean> pokers3 = playerPokerMap.getPoker3().getPokers();
                    iv_poker_palyer31.setImageResource(bullfightPresenter.getPokerId(pokers3.get(0).getColor(), pokers3.get(0).getValue()));
                    iv_poker_palyer32.setImageResource(bullfightPresenter.getPokerId(pokers3.get(1).getColor(), pokers3.get(1).getValue()));
                    iv_poker_palyer33.setImageResource(bullfightPresenter.getPokerId(pokers3.get(2).getColor(), pokers3.get(2).getValue()));
                    iv_poker_palyer34.setImageResource(bullfightPresenter.getPokerId(pokers3.get(3).getColor(), pokers3.get(3).getValue()));
                    iv_poker_palyer35.setImageResource(bullfightPresenter.getPokerId(pokers3.get(4).getColor(), pokers3.get(4).getValue()));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    bullfightAudio.play(result3);
                    iv_bull_amount3.setImageResource(bullfightPresenter.getBullSumId(result3));
                    switchBullNum(true, 3);
                    if (result >= result3) {
                        iv_gray_bg3.setVisibility(View.VISIBLE);
                    }
                    if (result3 == 10) {
                        showBullAnimation();
                    }
                }
            });
            final ObjectAnimator animator2 = ObjectAnimator.ofFloat(rl_poker_player_container2, "scaleX", 0f, 1f);
            animator2.setDuration(1000);
            animator2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    List<PokerResult.ObjBean.PlayerPokerMapBean.PokersBean> pokers2 = playerPokerMap.getPoker2().getPokers();
                    iv_poker_palyer21.setImageResource(bullfightPresenter.getPokerId(pokers2.get(0).getColor(), pokers2.get(0).getValue()));
                    iv_poker_palyer22.setImageResource(bullfightPresenter.getPokerId(pokers2.get(1).getColor(), pokers2.get(1).getValue()));
                    iv_poker_palyer23.setImageResource(bullfightPresenter.getPokerId(pokers2.get(2).getColor(), pokers2.get(2).getValue()));
                    iv_poker_palyer24.setImageResource(bullfightPresenter.getPokerId(pokers2.get(3).getColor(), pokers2.get(3).getValue()));
                    iv_poker_palyer25.setImageResource(bullfightPresenter.getPokerId(pokers2.get(4).getColor(), pokers2.get(4).getValue()));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator3.start();
                    bullfightAudio.play(result2);
                    iv_bull_amount2.setImageResource(bullfightPresenter.getBullSumId(result2));
                    switchBullNum(true, 2);
                    if (result >= result2) {
                        iv_gray_bg2.setVisibility(View.VISIBLE);
                    }
                    if (result2 == 10) {
                        showBullAnimation();
                    }
                }
            });
            final ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_poker_player_container1, "scaleX", 0f, 1f);
            animator1.setDuration(1000);
            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    List<PokerResult.ObjBean.PlayerPokerMapBean.PokersBean> pokers1 = playerPokerMap.getPoker1().getPokers();
                    iv_poker_palyer11.setImageResource(bullfightPresenter.getPokerId(pokers1.get(0).getColor(), pokers1.get(0).getValue()));
                    iv_poker_palyer12.setImageResource(bullfightPresenter.getPokerId(pokers1.get(1).getColor(), pokers1.get(1).getValue()));
                    iv_poker_palyer13.setImageResource(bullfightPresenter.getPokerId(pokers1.get(2).getColor(), pokers1.get(2).getValue()));
                    iv_poker_palyer14.setImageResource(bullfightPresenter.getPokerId(pokers1.get(3).getColor(), pokers1.get(3).getValue()));
                    iv_poker_palyer15.setImageResource(bullfightPresenter.getPokerId(pokers1.get(4).getColor(), pokers1.get(4).getValue()));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator2.start();
                    bullfightAudio.play(result1);
                    iv_bull_amount1.setImageResource(bullfightPresenter.getBullSumId(result1));
                    switchBullNum(true, 1);
                    if (result >= result1) {
                        iv_gray_bg1.setVisibility(View.VISIBLE);
                    }
                    if (result1 == 10) {
                        showBullAnimation();
                    }
                }
            });
            final ObjectAnimator animator = ObjectAnimator.ofFloat(rl_poker_banker_container, "scaleX", 0f, 1f);
            animator.setDuration(1000);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    List<PokerResult.ObjBean.PlayerPokerMapBean.PokersBean> pokers = playerPokerMap.getPoker0().getPokers();
                    iv_poker_banker1.setImageResource(bullfightPresenter.getPokerId(pokers.get(0).getColor(), pokers.get(0).getValue()));
                    iv_poker_banker2.setImageResource(bullfightPresenter.getPokerId(pokers.get(1).getColor(), pokers.get(1).getValue()));
                    iv_poker_banker3.setImageResource(bullfightPresenter.getPokerId(pokers.get(2).getColor(), pokers.get(2).getValue()));
                    iv_poker_banker4.setImageResource(bullfightPresenter.getPokerId(pokers.get(3).getColor(), pokers.get(3).getValue()));
                    iv_poker_banker5.setImageResource(bullfightPresenter.getPokerId(pokers.get(4).getColor(), pokers.get(4).getValue()));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator1.start();
                    bullfightAudio.play(result);
                    iv_bull_amount0.setImageResource(bullfightPresenter.getBullSumId(result));
                    switchBullNum(true, 0);
                    if (result == 10) {
                        showBullAnimation();
                    }
                }
            });
            animator.start();
        }
    }

    /**
     * 显示牛牛动画
     */
    private void showBullAnimation() {
        final ImageView bullbullbg = new ImageView(this);
        bullbullbg.setImageResource(R.mipmap.bullbullbg);
        final ImageView bull_alert = new ImageView(this);
        bull_alert.setImageResource(R.mipmap.bull_alert);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        bullbullbg.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
        bull_alert.setLayoutParams(layoutParams1);
        mRoot.addView(bullbullbg);
        mRoot.addView(bull_alert);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(bullbullbg, "rotation", 0f, 180f);
        rotation.setDuration(1000);
        rotation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRoot.removeView(bullbullbg);
                mRoot.removeView(bull_alert);
            }
        });
        rotation.start();
    }

    /**
     * 更新投注金额的选择按钮
     */
    private void updateBettingEnable(long balance) {
        if (balance >= 10000) {
            iv_10.setTag(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setTag(true);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_light);
            iv_100.setTag(true);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_light);
            iv_1000.setTag(true);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_light);
            iv_10000.setTag(true);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_light);
        } else if (balance >= 1000) {
            iv_10.setTag(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setTag(true);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_light);
            iv_100.setTag(true);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_light);
            iv_1000.setTag(true);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_light);
            iv_10000.setTag(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        } else if (balance >= 100) {
            iv_10.setTag(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setTag(true);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_light);
            iv_100.setTag(true);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_light);
            iv_1000.setTag(false);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_dark);
            iv_10000.setTag(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        } else if (balance >= 50) {
            iv_10.setTag(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setTag(true);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_light);
            iv_100.setTag(false);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_dark);
            iv_1000.setTag(false);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_dark);
            iv_10000.setTag(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        } else if (balance >= 10) {
            iv_10.setTag(true);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_light);
            iv_50.setTag(false);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_dark);
            iv_100.setTag(false);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_dark);
            iv_1000.setTag(false);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_dark);
            iv_10000.setTag(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        } else {
            iv_10.setTag(false);
            iv_10.setImageResource(R.mipmap.ic_bullfight_10_dark);
            iv_50.setTag(false);
            iv_50.setImageResource(R.mipmap.ic_bullfight_50_dark);
            iv_100.setTag(false);
            iv_100.setImageResource(R.mipmap.ic_bullfight_100_dark);
            iv_1000.setTag(false);
            iv_1000.setImageResource(R.mipmap.ic_bullfight_1000_dark);
            iv_10000.setTag(false);
            iv_10000.setImageResource(R.mipmap.ic_bullfight_10000_dark);
        }
    }

    /**
     * 发牌动画
     */
    private void sendPokerAnimator() {
        switchAllPoker(true, -1);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(rl_poker_player_container3, "scaleX", 0f, 1f);
        animator3.setDuration(1000);
        animator3.start();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(rl_poker_player_container2, "scaleX", 0f, 1f);
        animator2.setDuration(1000);
        animator2.start();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_poker_player_container1, "scaleX", 0f, 1f);
        animator1.setDuration(1000);
        animator1.start();
        ObjectAnimator animator = ObjectAnimator.ofFloat(rl_poker_banker_container, "scaleX", 0f, 1f);
        animator.setDuration(1000);
        animator.start();
    }

    /**
     * 牛数显示隐藏
     *
     * @param isShow
     * @param position -1一起显示
     */
    private void switchBullNum(boolean isShow, int position) {
        if (isShow) {
            switch (position) {
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
     *
     * @param isShow   是否显示
     * @param position 再显示的基础上显示哪个区域（-1一起显示，0显示庄家，1显示天，2显示地，3显示人）
     */
    private void switchAllPoker(boolean isShow, int position) {
        if (isShow) {
            switch (position) {
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
     * 标记游戏是否隐藏
     */
    private boolean gameHide = true;
    /**
     * 游戏布局的高度
     */
    private int gameHeight;

    /**
     * 显示游戏界面
     *
     * @param gameType 1（彩票）;2（斗牛）
     */
    private void showPlayView(int gameType) {
        gameHide = false;
        this.gameType = gameType;
        if (rl_game_container.getVisibility() == View.GONE) {
            //游戏内容视图
            rl_game_container.setVisibility(View.VISIBLE);
            //游戏左上角视图
            rl_game_info.setVisibility(View.VISIBLE);
            //走势图标
            ll_trendOrHistory.setVisibility(View.VISIBLE);
            switch (gameType) {
                case 1://彩票
                    rl_kp.setVisibility(View.VISIBLE);
                    rl_bullfight_banker.setVisibility(View.GONE);
                    live_game.setVisibility(View.VISIBLE);
                    live_game_bullfight.setVisibility(View.GONE);
                    break;
                case 2://斗牛
                    rl_bullfight_banker.setVisibility(View.VISIBLE);
                    rl_kp.setVisibility(View.GONE);
                    live_game_bullfight.setVisibility(View.VISIBLE);
                    live_game.setVisibility(View.GONE);
                    break;
            }
        } else {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(rl_game_info, "alpha", 0, 1);
            alpha.setDuration(500);
            alpha.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    rl_game_info.setVisibility(View.VISIBLE);
                }
            });
            alpha.start();
            ObjectAnimator alpha1 = ObjectAnimator.ofFloat(ll_trendOrHistory, "alpha", 0, 1);
            alpha1.setDuration(500);
            alpha1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    ll_trendOrHistory.setVisibility(View.VISIBLE);
                }
            });
            alpha1.start();
            ValueAnimator animator = ValueAnimator.ofFloat(1);
            animator.setTarget(rl_game_container);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = rl_game_container.getLayoutParams();
                    layoutParams.height = (int) (animatedValue * gameHeight);
                    LogUtils.e("TAG", "layoutParams.height==" + layoutParams.height);
                    rl_game_container.setLayoutParams(layoutParams);
                }
            });
            animator.start();
        }
        String myCoin = CountUtils.getCount(myGoldCoin);
        if (gameType == 1) {
            all_lepiao.setText(myCoin);
        } else if (gameType == 2) {
            tv_bullfight_lepiao.setText(myCoin);
        }
    }

    /**
     * 影藏游戏界面
     *
     * @param gameType 1（彩票）;2（斗牛）
     */
    private void hidePlayView(int gameType) {
        gameHide = true;
        if (rl_game_container.getVisibility() == View.GONE) {
            //游戏内容视图
            rl_game_container.setVisibility(View.GONE);
            //游戏左上角视图
            rl_game_info.setVisibility(View.GONE);
            //走势图标
            ll_trendOrHistory.setVisibility(View.GONE);
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
        } else {
            if (gameHeight == 0) {
                gameHeight = rl_game_container.getHeight();
            }
            ObjectAnimator alpha = ObjectAnimator.ofFloat(rl_game_info, "alpha", 1, 0);
            alpha.setDuration(500);
            alpha.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rl_game_info.setVisibility(View.GONE);
                }
            });
            alpha.start();
            ObjectAnimator alpha1 = ObjectAnimator.ofFloat(ll_trendOrHistory, "alpha", 1, 0);
            alpha1.setDuration(500);
            alpha1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ll_trendOrHistory.setVisibility(View.GONE);
                }
            });
            alpha1.start();
            ValueAnimator animator = ValueAnimator.ofFloat(1);
            animator.setTarget(rl_game_container);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = rl_game_container.getLayoutParams();
                    layoutParams.height = (int) ((1 - animatedValue) * gameHeight);
                    rl_game_container.setLayoutParams(layoutParams);
                }
            });
            animator.start();
        }
    }

    private void showXYGame() {
        String url = UrlBuilder.SERVER_URL + UrlBuilder.START_LUCK_GAME;

        OkHttpUtils.post().url(url).addParams("roomId", room_Id).addParams("type", "1").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

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
                        ll_game.setVisibility(View.GONE);
                        showPlayView(1);
                        gameIsStart = true;

                        getTimenumber();

                        Map<String, Object> map = new HashMap<>();
                        map.put("vip", appUser.getVip());
                        map.put("userId", appUser.getId());
                        map.put("level", appUser.getLevel());
                        map.put("palyMsg", getString(R.string.start_lucy28_game));
                        SendRoomMessageUtils.onCustomMessagePlay("2828", messageFragment, wy_Id, map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
        String url = UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.SELECT_FAMILY_MEMBER;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", creatReadyBean.getCreator().getId());
        String json = new JSONObject(hashMap).toString();
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        SdkHttpResultSuccess sdkHttpResultSuccess = JsonUtil.json2Bean(response
                                .toString(), SdkHttpResultSuccess.class);

                        if (!TextUtils.isEmpty(sdkHttpResultSuccess.getObj())) {
                            LiveFamilyMemberBean liveFamilyMember = JsonUtil.json2Bean(response,
                                    LiveFamilyMemberBean.class);
                            showFamilyList(liveFamilyMember);
                        } else {
                            showToast(sdkHttpResultSuccess.getMsg());
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
        RequestParams params = new RequestParams(UrlBuilder.CHARGE_SERVER_URL + UrlBuilder
                .MY_CONTROLLER);
        params.addQueryStringParameter("page", 1 + "");
        params.addQueryStringParameter("rows", "10");
        params.addQueryStringParameter("userId", appUser.getId());
        x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        NewSdkHttpResult newSdkhttpResult = JsonUtil.json2Bean(result.toString(),
                                NewSdkHttpResult.class);
                        if (newSdkhttpResult.getCode() == 1) {
                            List<ControllerBean> list = JsonUtil.json2BeanList(newSdkhttpResult
                                    .getObj().toString(), ControllerBean.class);

                            if (null == list || list.size() == 0) {
                                showToast(getString(R.string.no_control));
                            } else {
                                showContrllerList(list);
                            }
                        } else {
                            showToast(getString(R.string.winning_failure));
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

        //设置虚拟按键变成三个小点
        final Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        window.setAttributes(params);

        /**
         * 虚拟键盘弹出来监听
         */
        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setHideVirtualKey(window);
            }
        });
    }

    private boolean isFirstJoin = true;//是否第一次调onResume

    public void onResume() {
        super.onResume();
        mWl.acquire();
        mMediaStreamingManager.resume();

        if (!isFirstJoin) {
            Map<String, Object> map = new HashMap<>();
            map.put("level", appUser.getLevel());
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
        map.put("level", appUser.getLevel());
        SendRoomMessageUtils.onCustomMessageQiehuan("113", messageFragment, wy_Id + "",
                map);

    }

    @Override
    protected void onDestroy() {
//        httpDatas.getDataDialog("关闭直播间", false, urlBuilder.cloesAnchor(room_Id), myHandler,
//                RequestCode.REQUEST_ROOM_CLOES);
        if (bullfightPresenter != null) {
            bullfightPresenter.detach();
        }
        timer.cancel();
        if (lianmaiTimer != null) {
            lianmaiTimer.cancel();
        }
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
        if (bullfightAudio != null) {
            bullfightAudio.release();
        }
//        myHandler.removeMessages(10000);
//        myHandler.removeCallbacks(timenNumber);
        myHandler.removeCallbacksAndMessages(null);
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
                        LogUtil.e("有人进入直播间", message.getRemoteExtension().get("data").toString());
                        RoomUserBean enterRoom = JavaBeanMapUtils.mapToBean((Map) message.
                                getRemoteExtension().get("data"), RoomUserBean.class);
                        if (enterRoom != null && !enterRoom.getUserId().equals(appUser.getId())) {
                            liveNum.setText(++liveOnLineNums + "");
                            mDatas.add(enterRoom);
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
                        if (giftBean.getWinFlag().equals("2")) {
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
                        changeJinpiao(customGiftBean);
                        starGiftAnimation(giftSendModel);
                        break;
                    case 110:
                        //连麦成功
                        showLianmaiView();
                        tv_lianmai.setText(getString(R.string.over_the_mic));
                        lianmaiTimer.cancel();
                        LogUtil.e("主播连麦成功", message.getRemoteExtension().toString());
                        customLianmaiBean = JavaBeanMapUtils.mapToBean(remote,
                                CustomLianmaiBean.class);
                        String video = customLianmaiBean.getBroadcastUrl();

                        if (!TextUtils.isEmpty(customLianmaiBean.getPicUrl())) {
                            Picasso.with(mContext).load(customLianmaiBean.getPicUrl()).placeholder(R.mipmap.head_default)
                                    .error(R.mipmap.head_default).resize(50, 50).into(liveLianmaiHead);
                        }

                        WatchLive(video);
                        break;
                    case 111:
                        LogUtil.e("观众断开连麦", message.getRemoteExtension().toString());
                        showToast(getString(R.string.audience_break_the_mic));
                        hindSmallVideo();
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
                    case 3030://有人投注
                        int betPosition = 0;
                        int amount = 0;
                        if (remote.get("NIM_TOUZHU_GOLD_SELECT_1") != null) {
                            betPosition = 1;
                            amount = Integer.valueOf((String) remote.get("NIM_TOUZHU_GOLD_SELECT_1"));
                        } else if (remote.get("NIM_TOUZHU_GOLD_SELECT_2") != null) {
                            betPosition = 2;
                            amount = Integer.valueOf((String) remote.get("NIM_TOUZHU_GOLD_SELECT_2"));
                        } else if (remote.get("NIM_TOUZHU_GOLD_SELECT_3") != null) {
                            betPosition = 3;
                            amount = Integer.valueOf((String) remote.get("NIM_TOUZHU_GOLD_SELECT_3"));
                        }
                        addBettingView(betPosition, amount, false);
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
                    barrageDateBean.setNickName(message.getChatRoomMessageExtension()
                            .getSenderNick());
                } else {
                    barrageDateBean.setNickName(appUser.getNickName());
                }
                barrageDateBean.setPicUrl(url);
                barrageview.addSentence(barrageDateBean);
                if (message.getFromAccount().equals("miu_" + appUser.getId())) {
                    myGoldCoin = myGoldCoin - 1;
                    String myCoin = CountUtils.getCount(myGoldCoin);
                    if (gameType == 1) {
                        all_lepiao.setText(myCoin);
                    } else if (gameType == 2) {
                        tv_bullfight_lepiao.setText(myCoin);
                    }

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
        String url = UrlBuilder.SERVER_URL + UrlBuilder.ROOM_HEAD_LIST;
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
            OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack(mContext,
                    HttpDatas.KEY_CODE) {
                @Override
                public void onFaild() {
                }

                @Override
                public void onSucess(String data) {
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
            messageFragment.init(wy_Id);
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
                        if (dianBoDateBean.getResult() != null && dianBoDateBean.getResult().equals("2")) {//观众拒绝连麦
                            showToast(getString(R.string.turn_down_the_mic, dianBoDateBean.getUserName()));
                            room_lianmai.setVisibility(View.VISIBLE);
                            tv_lianmai.setVisibility(View.GONE);
                            lianmaiTimer.cancel();
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
            showToast(getString(R.string.chat_room_kickout_event, chatRoomKickOutEvent.getReason()));
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
        lmFm.setVisibility(View.VISIBLE);
        if (mVideoSurfaceView.getVisibility() == View.GONE || mVideoSurfaceView.getVisibility() == View.INVISIBLE) {
            mVideoSurfaceView.setVisibility(View.VISIBLE);
        }
        mVideoSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mVideoSurfaceView.setZOrderMediaOverlay(true);
        liveVideo = new AnchorVideoOne();
        liveVideo.initLive(twoVideo, StartLiveActivity.this, mVideoSurfaceView);
        showToast(getString(R.string.the_mic_succeed));
    }

    /**
     * 隐藏拉流小窗口
     */
    private void hindSmallVideo() {
        hideLianmaiView();
        if (!TextUtils.isEmpty(lianId) && lmFm.getVisibility() == View.VISIBLE) {
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
            httpDatas.getDataForJsoNoloading("主播退出连线", com.android.volley.Request.Method.GET, UrlBuilder.roomLiveExit
                            (room_Id, lianId.substring(4)), map, myHandler,
                    RequestCode.ROOMLIVEEXIT);
        }

        if (liveVideo != null) {
            liveVideo.release();
            mVideoSurfaceView.getHolder().getSurface().release();
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
                        FaceBeautySetting(0.5f, 0.5f, 0.5f))
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


    float DownX;
    float DownY;

    private void initPaly() {
        AspectFrameLayout afl = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        afl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        com.lvshandian.lemeng.widget.view.CameraPreviewFrameView cameraPreviewFrameView =
                (com.lvshandian.lemeng.widget.view.CameraPreviewFrameView) findViewById(R.id
                        .cameraPreview_surfaceView);
        cameraPreviewFrameView.setListener(null);
        cameraPreviewFrameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                        float movepX = event.getX() - DownX;//X轴距离
                        float movepY = event.getY() - DownY;//y轴距离

                        if (movepY > 0 && ((Math.abs(movepY)) > 150)) {
                            /**
                             * 下滑动
                             */
                            if (gameIsStart) {
                                if (!gameHide) {
                                    hidePlayView(gameType);
                                }
                            } else {
                                if (ll_game.getVisibility() == View.VISIBLE) {
                                    ll_game.setVisibility(View.GONE);
                                }
                            }

                        } else if (movepY < 0 && ((Math.abs(movepY)) > 150)) {
                            /**
                             * 上滑动
                             */
                            if (gameIsStart) {
                                if (gameHide) {
                                    showPlayView(gameType);
                                }
                            } else {
                                if (ll_game.getVisibility() == View.GONE) {
                                    ll_game.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        break;
                }
                return true;
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
                    break;
                case MSG_ENCODING_MIRROR:
                    mIsEncodingMirror = !mIsEncodingMirror;
                    mMediaStreamingManager.setEncodingMirror(mIsEncodingMirror);
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

        String url = UrlBuilder.SERVER_URL + UrlBuilder.livepush(wy_Id);
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
        scaleX.setRepeatCount(2);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvNum, "scaleY", 5f, 1f);
        scaleY.setRepeatCount(2);
        scaleY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                tvNum.setText(Integer.valueOf(tvNum.getText().toString()) - 1 + "");
            }
        });
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(1000);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRoot.removeView(tvNum);
            }
        });
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
                .POST, UrlBuilder.SELECT_USER_INFO, map, myHandler, RequestCode.SELECT_USER);
    }


    private int funsePage = 1;
    private int funseTotalPages = 1;
    private boolean funseIsRefresh = true;
    private LianmaiListAadapter lianmaiListAadapter;
    private List<FunseBean> funseBeanList = new ArrayList<>();
    private FunseBean inviteFunse;

    /**
     * 展示连麦pop
     */
    public void showLianmaiPop() {
        funsePage = 1;
        funseIsRefresh = true;
        inviteFunse = null;
        final CustomPopWindow lianmai_search = new CustomPopWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.lianmai_search, null);
        lianmai_search.setContentView(view);
        lianmai_search.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        lianmai_search.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        lianmai_search.setFocusable(true);
        lianmai_search.setBackgroundDrawable(new BitmapDrawable());
        lianmai_search.setOutsideTouchable(true);
        lianmai_search.setAnimationStyle(R.style.mypopwindow_anim_style);
        lianmai_search.showAtLocation(liveHead, Gravity.BOTTOM, 0, 0);

        final EditText et_search_input = (EditText) view.findViewById(R.id.et_search_input);
        ImageView iv_search = (ImageView) view.findViewById(R.id.iv_search);
        RecyclerView lianmai_recycler = (RecyclerView) view.findViewById(R.id.lianmai_recycler);
        final TextView tv_invite = (TextView) view.findViewById(R.id.tv_invite);
        final SwipeRefreshLayout lianmai_refresh = (SwipeRefreshLayout) view.findViewById(R.id.lianmai_refresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lianmai_recycler.setLayoutManager(layoutManager);

        lianmaiListAadapter = new LianmaiListAadapter(mContext, funseBeanList);
        lianmai_recycler.setAdapter(lianmaiListAadapter);
        requestFunse(lianmai_refresh, "");

        //设置刷新逻辑
        lianmai_refresh.setMode(SwipeRefreshLayout.Mode.BOTH);
        lianmai_refresh.setColorSchemeColors(getResources().getColor(R.color.main));
        lianmai_refresh.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                funseIsRefresh = true;
                requestFunse(lianmai_refresh, et_search_input.getText().toString().trim());
            }
        });

        lianmai_refresh.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                funseIsRefresh = false;
                if (funsePage < funseTotalPages) {
                    requestFunse(lianmai_refresh, et_search_input.getText().toString().trim());
                } else {
                    lianmai_refresh.setRefreshing(false);
                    lianmai_refresh.setPullUpRefreshing(false);
                    showToast(getString(R.string.no_more));
                }
            }
        });


        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_search_input.getText().toString().trim())) {
                    showToast(getString(R.string.input_id_or_nickname));
                } else {
                    funseIsRefresh = true;
                    requestFunse(lianmai_refresh, et_search_input.getText().toString().trim());
                }
            }
        });

        lianmaiListAadapter.setOnRecyclerClickListener(new LianmaiListAadapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerClick(int position) {
                for (int i = 0, j = funseBeanList.size(); i < j; i++) {
                    if (i != position) {
                        funseBeanList.get(i).setChecked(false);
                    }
                }
                if (funseBeanList.get(position).isChecked()) {
                    funseBeanList.get(position).setChecked(false);
                    tv_invite.setTextColor(getResources().getColor(R.color.tv_color9));
                    inviteFunse = null;
                } else {
                    funseBeanList.get(position).setChecked(true);
                    tv_invite.setTextColor(getResources().getColor(R.color.tv_color3));
                    inviteFunse = funseBeanList.get(position);
                }
                lianmaiListAadapter.notifyDataSetChanged();
            }
        });

        tv_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inviteFunse == null) {
                    showToast(getString(R.string.no_selecte_mic));
                } else {
                    lianId = "miu_" + inviteFunse.getUserId();
                    sendCustomNotificationForLive(lianId, appUser.getId(),
                            appUser.getNickName(), appUser.getPicUrl(), appUser.getVip(), "主播请求与您连麦", "2",
                            CustomNotificationType.IM_P2P_TYPE_SUBLIVE_PUBLIC, "1");
                    lianmai_search.dismiss();
                    room_lianmai.setVisibility(View.GONE);
                    tv_lianmai.setVisibility(View.VISIBLE);
                    lianmaiTimer = new CountDownTimer(15500, 1000) {//连送礼物
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tv_lianmai.setText(getString(R.string.waiting, String.valueOf(millisUntilFinished / 1000)));
                        }

                        @Override
                        public void onFinish() {
                            room_lianmai.setVisibility(View.VISIBLE);
                            tv_lianmai.setVisibility(View.GONE);
                            showToast(getString(R.string.no_accept_the_mic, inviteFunse.getNickName()));
                            sendCustomNotificationForLive(lianId, appUser.getId(),
                                    appUser.getNickName(), appUser.getPicUrl(), appUser.getVip(), "主播断开与您连麦", "2",
                                    CustomNotificationType.IM_P2P_TYPE_ORDERSHOW, "1");
                        }
                    };
                    lianmaiTimer.start();
                }
            }
        });
    }


    /**
     * 请求粉丝列表
     */
    private void requestFunse(final SwipeRefreshLayout refresh, String content) {
        funsePage = funseIsRefresh ? 1 : ++funsePage;
        String url = UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.ROOM_FUNSE;
        OkHttpUtils.get().url(url).
                addParams("page", String.valueOf(funsePage)).
                addParams("roomId", room_Id).
                addParams("userIdOrNickName", content).
                build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                refresh.setRefreshing(false);
                refresh.setPullUpRefreshing(false);
                showToast(getString(R.string.winning_failure));
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("1")) {
                        String obj = jsonObject.getString("obj");
                        refresh.setRefreshing(false);
                        refresh.setPullUpRefreshing(false);
                        handlerJson(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取数据
     *
     * @param data
     */
    private void handlerJson(String data) {
        Funse funse = JsonUtil.json2Bean(data, Funse.class);
        if (funse != null) {
            funseTotalPages = funse.getTotalPages();
            List<FunseBean> result = funse.getResult();
            if (funseIsRefresh) {
                funseBeanList.clear();
            } else {
                if (result == null && result.size() == 0) {
                    funsePage--;
                }
            }
            funseBeanList.addAll(result);
            lianmaiListAadapter.notifyDataSetChanged();
        }
    }


    /**
     * 展示游客详情
     *
     * @param customdateBean
     */
    public void showDialogForCallOther(final CustomdateBean customdateBean) {
        final CustomPopWindow otherPop = new CustomPopWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_video_room, null);
        otherPop.setContentView(view);
        otherPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        otherPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        otherPop.setFocusable(true);
        otherPop.setBackgroundDrawable(new BitmapDrawable());
        otherPop.setOutsideTouchable(true);
        otherPop.setAnimationStyle(R.style.mypopwindow_anim_style);
        otherPop.showAtLocation(liveHead, Gravity.BOTTOM, 0, 0);

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
        TextView tv_banned = (TextView) view.findViewById(R.id.tv_banned);
        TextView tv_changkong = (TextView) view.findViewById(R.id.tv_changkong);
        TextView tv_reply = (TextView) view.findViewById(R.id.tv_reply);
        LinearLayout ll_report = (LinearLayout) view.findViewById(R.id.ll_report);
        LinearLayout ll_banned = (LinearLayout) view.findViewById(R.id.ll_banned);
        ImageView civ_image_bg = (ImageView) view.findViewById(R.id.civ_image_bg);

        if (!TextUtils.isEmpty(customdateBean.getId()) && customdateBean.getId().equals(appUser
                .getId())) {
            buttom_layout.setVisibility(View.GONE);
            ll_banned.setVisibility(View.INVISIBLE);
            ll_report.setVisibility(View.INVISIBLE);
            mySelf.setVisibility(View.VISIBLE);
        } else {
            buttom_layout.setVisibility(View.VISIBLE);
            ll_banned.setVisibility(View.VISIBLE);
            ll_report.setVisibility(View.VISIBLE);
            mySelf.setVisibility(View.GONE);
        }


        //显示界面提示是禁言还是解除禁言
        for (int i = 0, j = mutedList.size(); i < j; i++) {
            if (mutedList.get(i).equals(customdateBean.getId())) {
                tv_banned.setText(getString(R.string.remove_banned));
            }
        }

        if (customdateBean.getGender().equals("0")) {
            iv_sex.setImageResource(R.mipmap.female);
        } else {
            iv_sex.setImageResource(R.mipmap.male);
        }

        tvID.setText(getString(R.string.lemeng_id, customdateBean.getId()));

        if (!TextUtils.isEmpty(customdateBean.getPicUrl())) {
            Picasso.with(mContext).load(customdateBean.getPicUrl()).placeholder(R.mipmap.head_default)
                    .error(R.mipmap.head_default).into(civ_image);
        }
        civ_image_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                intent.putExtra(getString(R.string.visit_person), customdateBean.getId());
                startActivity(intent);
                otherPop.dismiss();
            }
        });

        iv_grade.setImageResource(GrademipmapUtils.LevelImg[Integer.valueOf(customdateBean
                .getLevel()) - 1]);
        tv_name.setText(customdateBean.getNickName());

        //签名
        if (TextUtils.isEmpty(customdateBean.getSignature())) {
            tv_sign.setText(getString(R.string.default_sign));
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

        //禁言
        view.findViewById(R.id.ll_banned).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banned(customdateBean.getId(), customdateBean.getNickName());
                otherPop.dismiss();
            }

        });

        focus((TextView) view.findViewById(R.id.tv_attention), customdateBean);
        //关注
        view.findViewById(R.id.tv_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFollow((TextView) view.findViewById(R.id.tv_attention), customdateBean);
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
        tv_reply.setText("@" + getString(R.string.he));
        tv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageFragment.showEditText();
                EditText editContent = (EditText) findViewById(com.netease.nim.uikit.R.id
                        .editTextMessage);
                String str = getString(R.string.reply, customdateBean.getNickName());
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
                intent.putExtra(getString(R.string.visit_person), customdateBean.getId());
                startActivity(intent);
                otherPop.dismiss();
            }
        });

        //我的场控
        tv_changkong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getString(R.string.stay_open));
                otherPop.dismiss();
            }
        });
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
        pwdEdit.setHint(getString(R.string.intput_report_content));
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
                    showToast(getString(R.string.intput_report_content));
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
        httpDatas.getDataForJsoNoloading("举报", com.android.volley.Request.Method.POST, UrlBuilder.REPORT, map,
                myHandler, RequestCode.REQUEST_REPORT);

    }


    /**
     * 禁言成功失败
     */
    private void banned(final String stopUserId, final String nickNane) {
        RequestParams params = new RequestParams(UrlBuilder.CHARGE_SERVER_URL + UrlBuilder
                .APP_USER_CONTROL);

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
                                showToast(getString(R.string.remove_banned_succeed));
                                Map<String, Object> map = new HashMap<>();
                                map.put("vip", appUser.getVip());
                                map.put("userId", appUser.getId());
                                map.put("level", appUser.getLevel());
                                map.put("isJinyan", "0");
                                map.put("jinyanId", stopUserId);
                                map.put("jinyan", getString(R.string.remove_deny, nickNane));
                                SendRoomMessageUtils.onCustomMessageJinyan(SendRoomMessageUtils
                                        .MESSAGE_JIN_YAN, messageFragment, wy_Id, map);
                                mutedList.remove(stopUserId);
                            } else if (newSdkhttpResult.getObj().equals("1")) {
                                showToast(getString(R.string.banned_to_post_succeed));
                                Map<String, Object> map = new HashMap<>();
                                map.put("vip", appUser.getVip());
                                map.put("userId", appUser.getId());
                                map.put("level", appUser.getLevel());
                                map.put("isJinyan", "1");
                                map.put("jinyanId", stopUserId);
                                map.put("jinyan", getString(R.string.deny, nickNane));
                                SendRoomMessageUtils.onCustomMessageJinyan(SendRoomMessageUtils
                                        .MESSAGE_JIN_YAN, messageFragment, wy_Id, map);
                                mutedList.add(stopUserId);
                            }
                        } else if (newSdkhttpResult.getCode() == 0) {
                            showToast(getString(R.string.operation_failure));
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
        String url = UrlBuilder.SERVER_URL + UrlBuilder.ATTENTION_USER;
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
            tvFouce.setText(getString(R.string.add_attention));
            tvFouce.setTextColor(getResources().getColor(R.color.main));
        } else if (TextUtils.equals("1", follow)) {
            tvFouce.setText(getString(R.string.already_attention));
            tvFouce.setTextColor(getResources().getColor(R.color.line_bf));
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
        tvTitle.setText(getString(R.string.if_close_live));
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
                finish();
                startActivity(new Intent(StartLiveActivity.this, QuitLiveActivity.class).putExtra
                        ("roomId", room_Id).putExtra("startTime", startTime));
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
     * 显示走势
     * type1:走势图   2:历史记录   3:规则
     */
    private void showHistoryDialog(int type) {
        String url = "";
        switch (gameType) {
            case 1:
                if (type == 1) {
                    url = UrlBuilder.LUCKY28_TREND;
                } else if (type == 2) {
                    url = String.format(UrlBuilder.LUCKY28_HISTORY, room_Id, appUser.getId());
                } else {
                    url = UrlBuilder.LUCKY28_RULE;
                }
                break;
            case 2:
                if (type == 1) {
                    url = String.format(UrlBuilder.NIUNIU_TREND, room_Id);
                } else if (type == 2) {
                    url = String.format(UrlBuilder.NIUNIU_HISTORY, room_Id, appUser.getId());
                }
                break;
        }

        View view = getLayoutInflater().inflate(R.layout.pop_trend, null);
        dialogForSelect.setCanceledOnTouchOutside(true);
        dialogForSelect.setContentView(view);
        dialogForSelect.show();
        ImageView colse_trend = (ImageView) view.findViewById(R.id.colse_trend);
        TextView content_type = (TextView) view.findViewById(R.id.content_type);
        if (type == 1) {
            content_type.setText(getString(R.string.trend_frame));
        } else if (type == 2) {
            content_type.setText(getString(R.string.history_frame));
        } else {
            content_type.setText(getString(R.string.rule_frame));
        }

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


    private void showTouZhuPop(String selectStatus, final int luckySet) {
        final CustomPopWindow rulePop = new CustomPopWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.pop_bet, null);
        rulePop.setContentView(view);
        rulePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        rulePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        rulePop.setFocusable(true);
        rulePop.setBackgroundDrawable(new BitmapDrawable());
        rulePop.setOutsideTouchable(true);

        rulePop.showAtLocation(liveHead, Gravity.CENTER, 0, 0);
        TextView tv_tzqh = (TextView) view.findViewById(R.id.tv_tzqh);//投注期号
        TextView tv_ds = (TextView) view.findViewById(R.id.tv_ds);  //大小单双
        TextView tv_xzjf = (TextView) view.findViewById(R.id.tv_xzjf);  //下注积分
        TextView sure_tz = (TextView) view.findViewById(R.id.sure_tz);  //确定投注
        ImageView colse_rule = (ImageView) view.findViewById(R.id.colse_rule);  //关闭弹框

        tv_ds.setText(getString(R.string.lucy_bet_type, selectStatus));
        tv_xzjf.setText(getString(R.string.lucy_bet_lepiao, String.valueOf(luckySet)));
        intQh = Integer.valueOf(nper) + 1;
        tv_tzqh.setText(getString(R.string.lucy_bet_date, String.valueOf(intQh)));

        sure_tz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myGoldCoin < luckySet) {
                    showToast(getString(R.string.balance_not_enough));
                    rulePop.dismiss();
                } else {
                    sureTz(rulePop, luckySet);
                }
            }
        });

        colse_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulePop.dismiss();
            }
        });

    }


    private void sureTz(final PopupWindow rulePop, final int luckySet) {
        String url = UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.RECIVE_AMOUNT;
        OkHttpUtils.get().url(url)
                .addParams("userId", appUser.getId())
                .addParams("roomId", room_Id)
                .addParams("periods", intQh + "")
                .addParams("amount", luckySet + "")
                .addParams("acountTimes", "1")
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
                            showToast(getString(R.string.bet_succeed));
                            /**
                             * 设置游戏布局的金币数量
                             */
                            myGoldCoin = myGoldCoin - luckySet;
                            String myCoin = CountUtils.getCount(myGoldCoin);
                            all_lepiao.setText(myCoin);
                            rulePop.dismiss();

                            Map<String, Object> map = new HashMap<>();
                            map.put("vip", appUser.getVip());
                            map.put("userId", appUser.getId());
                            map.put("level", appUser.getLevel());
                            map.put("inputMsg", getString(R.string.bet_info, String.valueOf(intQh), selectStatus, String.valueOf(luckySet)));
                            SendRoomMessageUtils.onCustomMessagePlay("1818", messageFragment, wy_Id, map);
                        } else {
                            showToast(jsonObject.getString("msg"));
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
        String url = UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.GET_TIME_NUMBER + "?userId=" + appUser.getId();
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                showToast(getString(R.string.network_error));
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
                            tv_periods.setText(getString(R.string.bet_date_number, lastAwardBean.getNper()));
                            frist_num.setText(lastAwardBean.getFirstNum() + "");
                            second_num.setText(lastAwardBean.getSecondNum() + "");
                            third_num.setText(lastAwardBean.getThirdNum() + "");
                            all_num.setText(lastAwardBean.getSum() + "");
                            tv_ds.setText(lastAwardBean.getType());
                            tv_next.setText(getString(R.string.distance_bet_date_number, String.valueOf((Integer.parseInt(lastAwardBean.getNper()) + 1))));

                            long now = System.currentTimeMillis();
                            mCountDownTotalTime = Long.parseLong(lastAwardBean.getDateLine()) - now;

                            if (mCountDownTotalTime < 0) {
                                tv_game_next_open_time.setText(getString(R.string.wait_lottery));
                                myHandler.postDelayed(timenNumber, 30000);
                            } else {
                                myHandler.sendEmptyMessage(10000);
                            }

                            if (lastAwardBean.getWinStatus().equals("1")) {
                                getZhongJiangTZ(lastAwardBean.getNper(), lastAwardBean.getWinAmountAll(), "1");

                                /**
                                 * 设置游戏布局的金币数量
                                 */
                                myGoldCoin = myGoldCoin + Long.parseLong(lastAwardBean.getWinAmountAll());
                                String myCoin = CountUtils.getCount(myGoldCoin);
                                all_lepiao.setText(myCoin);
                            } else if (lastAwardBean.getWinStatus().equals("0")) {
                                getZhongJiangTZ(lastAwardBean.getNper(), lastAwardBean.getWinAmountAll(), "0");
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
                            tv_periods.setText(getString(R.string.bet_date_number, lastAwardBean.getNper()));
                            frist_num.setText(lastAwardBean.getFirstNum() + "");
                            second_num.setText(lastAwardBean.getSecondNum() + "");
                            third_num.setText(lastAwardBean.getThirdNum() + "");
                            all_num.setText(lastAwardBean.getSum() + "");
                            tv_ds.setText(lastAwardBean.getType());
                            tv_next.setText(getString(R.string.distance_bet_date_number, String.valueOf((Integer.parseInt(lastAwardBean.getNper()) + 1))));
                            tv_game_next_open_time.setText(getString(R.string.wait_lottery));

                            myHandler.postDelayed(timenNumber, 30000);
                        }
                    } else {
                        isTouZhu = false;
                        tv_game_next_open_time.setText(getString(R.string.wait_lottery));
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

    private void getZhongJiangTZ(String nper, String winAmountAll, String type) {
        initDialog();
        String content = "";
        if (type.equals("1")) {
            content = getString(R.string.winning_hint, nper, winAmountAll);
        } else {
            content = getString(R.string.no_winning_hint, nper);
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


    private float mPinkValue = 0.5f;
    private float mWhitenValue = 0.5f;
    private float mReddenValue = 0.5f;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seek_pink: {
                if (fromUser) {
                    mPinkValue = progress / 100f;

                    CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                    fbSetting.beautyLevel = mPinkValue;
                    fbSetting.whiten = mWhitenValue;
                    fbSetting.redden = mReddenValue;

                    mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
                }
            }
            break;
            case R.id.seek_whiten: {
                if (fromUser) {
                    mWhitenValue = progress / 100f;

                    CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                    fbSetting.beautyLevel = mPinkValue;
                    fbSetting.whiten = mWhitenValue;
                    fbSetting.redden = mReddenValue;

                    mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
                }
            }
            break;
            case R.id.seek_redden: {
                if (fromUser) {
                    mReddenValue = progress / 100f;

                    CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                    fbSetting.beautyLevel = mPinkValue;
                    fbSetting.whiten = mWhitenValue;
                    fbSetting.redden = mReddenValue;

                    mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
                }
            }
            break;
            case R.id.seek_soften: {
                if (fromUser) {
//                    mPinkValue = progress / 100f;

                    CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                    fbSetting.beautyLevel = mPinkValue;
                    fbSetting.whiten = mWhitenValue;
                    fbSetting.redden = mReddenValue;

                    mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
                }
            }
            break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 打开美颜界面
     */
    public void getMeiyanPopup() {
        PopupWindow meiyanPopup = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_meiyan, null);
        meiyanPopup.setContentView(view);
        meiyanPopup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        meiyanPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        meiyanPopup.setFocusable(true);
        meiyanPopup.setBackgroundDrawable(new BitmapDrawable());
        meiyanPopup.setOutsideTouchable(true);
        meiyanPopup.setAnimationStyle(R.style.mypopwindow_anim_style);
        meiyanPopup.showAtLocation(rlvListLiveAudiences, Gravity.BOTTOM, 0, 0);

        SeekBar m_Seekpink = (SeekBar) view.findViewById(R.id.seek_pink);
        m_Seekpink.setOnSeekBarChangeListener(this);
        m_Seekpink.setProgress((int) (mPinkValue * 100));

        SeekBar m_Seekwhiten = (SeekBar) view.findViewById(R.id.seek_whiten);
        m_Seekwhiten.setOnSeekBarChangeListener(this);
        m_Seekwhiten.setProgress((int) (mWhitenValue * 100));

        SeekBar m_Seekredden = (SeekBar) view.findViewById(R.id.seek_redden);
        m_Seekredden.setOnSeekBarChangeListener(this);
        m_Seekredden.setProgress((int) (mReddenValue * 100));

        SeekBar m_Seeksoften = (SeekBar) view.findViewById(R.id.seek_soften);
        m_Seeksoften.setOnSeekBarChangeListener(this);
        m_Seeksoften.setProgress((int) (mPinkValue * 100));
    }

    private void showLianmaiView() {
        room_lianmai.setVisibility(View.GONE);
        liveLianmaiHead.setVisibility(View.VISIBLE);
        liveLianmaiHeadBg1.setVisibility(View.VISIBLE);
        liveLianmaiHeadBg2.setVisibility(View.VISIBLE);
        tv_lianmai.setVisibility(View.VISIBLE);
    }

    private void hideLianmaiView() {
        room_lianmai.setVisibility(View.VISIBLE);
        liveLianmaiHead.setVisibility(View.GONE);
        liveLianmaiHeadBg1.setVisibility(View.GONE);
        liveLianmaiHeadBg2.setVisibility(View.GONE);
        tv_lianmai.setVisibility(View.GONE);
        customLianmaiBean = null;
    }
}
