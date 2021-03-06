package com.lvshandian.lemeng.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.live.PrapareStartActivity;
import com.lvshandian.lemeng.activity.start.LoginActivity;
import com.lvshandian.lemeng.activity.start.LogoutHelper;
import com.lvshandian.lemeng.engine.service.UpdateApkService;
import com.lvshandian.lemeng.entity.GlobalSwitch;
import com.lvshandian.lemeng.entity.LiveStatusBean;
import com.lvshandian.lemeng.entity.QuitApp;
import com.lvshandian.lemeng.entity.QuitLogin;
import com.lvshandian.lemeng.entity.mine.PhotoBean;
import com.lvshandian.lemeng.entity.mine.VideoBean;
import com.lvshandian.lemeng.fragment.AttentionFragment;
import com.lvshandian.lemeng.fragment.HomeChatFragment;
import com.lvshandian.lemeng.fragment.IndexPagerFragment;
import com.lvshandian.lemeng.fragment.MyInformationFragment;
import com.lvshandian.lemeng.interfaces.Constant;
import com.lvshandian.lemeng.interfaces.ResultListener;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.net.RequestCode;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.third.wangyiyunxin.config.preference.Preferences;
import com.lvshandian.lemeng.third.wangyiyunxin.main.helper.SystemMessageUnreadManager;
import com.lvshandian.lemeng.third.wangyiyunxin.main.reminder.ReminderItem;
import com.lvshandian.lemeng.third.wangyiyunxin.main.reminder.ReminderManager;
import com.lvshandian.lemeng.third.wangyiyunxin.main.reminder.ReminderSettings;
import com.lvshandian.lemeng.widget.view.CustomPopWindow;
import com.lvshandian.lemeng.widget.view.LoadingDialog;
import com.lvshandian.lemeng.utils.AliYunImageUtils;
import com.lvshandian.lemeng.utils.DateUtils;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.maiml.wechatrecodervideolibrary.recoder.WechatRecoderActivity;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.session.module.input.InputPanel;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 主界面Activity
 */
public class MainActivity extends BaseActivity implements
        TabHost.OnTabChangeListener,
        View.OnTouchListener, ReminderManager.UnreadNumChangedCallback {

    @Bind(R.id.realtabcontent)
    FrameLayout realtabcontent;
    @Bind(R.id.ll_home)
    LinearLayout ll_home;
    @Bind(R.id.ll_follow)
    LinearLayout ll_follow;
    @Bind(R.id.ll_startLive)
    LinearLayout ll_startLive;
    @Bind(R.id.ll_chat)
    RelativeLayout ll_chat;
    @Bind(R.id.ll_myself)
    LinearLayout ll_myself;
    @Bind(R.id.iv_home)
    ImageView iv_home;
    @Bind(R.id.iv_follow)
    ImageView iv_follow;
    @Bind(R.id.iv_startLive)
    ImageView iv_startLive;
    @Bind(R.id.iv_chat)
    ImageView iv_chat;
    @Bind(R.id.iv_myself)
    ImageView iv_myself;
    @Bind(R.id.unread_cover)
    DropCover unreadCover;
    @Bind(R.id.tab_new_msg_label)
    DropFake tabNewMsgLabel;
    @Bind(R.id.tab_new_indicator)
    ImageView tabNewIndicator;

    private int index = 0;
    /**
     * 选择相册的requestCode
     */
    protected static final int CHOOSE_PICTURE = 120;

    /**
     * 图片裁剪的requestCode
     */
    private static final int CROP_SMALL_PICTURE = 123;

    /**
     * 创建录制视频的requestCode
     */
    private static final int REQ_CODE = 10001;

    /**
     * 裁剪后的地址
     */
    protected Uri imageUri;

    /**
     * 记录第一次点击返回键的时间
     */
    private long firstPressed;

    /**
     * 连续点击两次返回键退出应用间隔时间
     */
    private long dowableClick = 1000 * 2;

    /**
     * 请求Loading
     */
    private LoadingDialog mLoading;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);
            switch (msg.what) {
                /**
                 * 图片上传成功
                 */
                case RequestCode.MY_PHOTO_UPLOAD_CODE:
                    showToast(getString(R.string.uploading_succeed));
                    PhotoBean photoBean = new PhotoBean();
                    photoBean.setId("yes");
                    EventBus.getDefault().post(photoBean);
                    break;
                /**
                 * 上传视频成功
                 */
                case RequestCode.MY_VIDEO_UPLOAD:
                    VideoBean videoBean = new VideoBean();
                    videoBean.setId("yes");
                    EventBus.getDefault().post(videoBean);
                    intentMyInformationFragment();
                    showToast(getString(R.string.uploading_succeed));
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initListener() {
        ll_home.setOnClickListener(this);
        ll_follow.setOnClickListener(this);
        ll_startLive.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_myself.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
//        startAppVersionUpdate();
        initFragments();
        registerObservers(true);
        //注册监听自定义通知
        registerReceiveCustom(true);
        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();
        initUnreadCover();
        getStatus();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getStatus();
            }
        }, 1000, 30000);
    }

    private void initFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.realtabcontent, new IndexPagerFragment());
        ft.commit();
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.ll_home:
                if (index == 0)
                    return;
                index = 0;
                ft.replace(R.id.realtabcontent, new IndexPagerFragment());
                break;
            case R.id.ll_follow:
                if (index == 1)
                    return;
                index = 1;
                ft.replace(R.id.realtabcontent, new AttentionFragment());
                break;
            case R.id.ll_startLive:
                index = 2;
                quickStartLivePop();
                return;
            case R.id.ll_chat:
                if (index == 3)
                    return;
                index = 3;
                ft.replace(R.id.realtabcontent, new HomeChatFragment());
                break;
            case R.id.ll_myself:
                if (index == 4)
                    return;
                index = 4;
                ft.replace(R.id.realtabcontent, new MyInformationFragment());
                break;
        }
        ft.commit();
        updateBottom(index);
    }

    private void updateBottom(int position) {
        switch (position) {
            case 0:
                iv_home.setImageResource(R.mipmap.ic_home_2);
                iv_follow.setImageResource(R.mipmap.ic_follow_1);
                iv_startLive.setImageResource(R.mipmap.ic_startlive);
                iv_chat.setImageResource(R.mipmap.ic_chat_1);
                iv_myself.setImageResource(R.mipmap.ic_myself_1);
                break;
            case 1:
                iv_home.setImageResource(R.mipmap.ic_home_1);
                iv_follow.setImageResource(R.mipmap.ic_follow_2);
                iv_startLive.setImageResource(R.mipmap.ic_startlive);
                iv_chat.setImageResource(R.mipmap.ic_chat_1);
                iv_myself.setImageResource(R.mipmap.ic_myself_1);
                break;
            case 2:

                break;
            case 3:
                iv_home.setImageResource(R.mipmap.ic_home_1);
                iv_follow.setImageResource(R.mipmap.ic_follow_1);
                iv_startLive.setImageResource(R.mipmap.ic_startlive);
                iv_chat.setImageResource(R.mipmap.ic_chat_2);
                iv_myself.setImageResource(R.mipmap.ic_myself_1);
                break;
            case 4:
                iv_home.setImageResource(R.mipmap.ic_home_1);
                iv_follow.setImageResource(R.mipmap.ic_follow_1);
                iv_startLive.setImageResource(R.mipmap.ic_startlive);
                iv_chat.setImageResource(R.mipmap.ic_chat_1);
                iv_myself.setImageResource(R.mipmap.ic_myself_2);
                break;
        }
    }

    /**
     * 跳到MyInformationFragment
     */
    public void intentMyInformationFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MyInformationFragment myInformationFragment = new MyInformationFragment();
        ft.replace(R.id.realtabcontent, myInformationFragment);
        ft.commit();
        updateBottom(4);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableMsgNotification(false);
        showToast("我是热更新");
    }

    @Override
    protected void onPause() {
        super.onPause();
        enableMsgNotification(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerReceiveCustom(false);
        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PICTURE:
                if (null != data && null != data.getData()) {
                    LogUtil.e("相册选择", "data=" + data.toString());
                    File imageFile = new File(Constant.APP_PATH + "image/");
                    if (!imageFile.exists()) {
                        imageFile.mkdirs();
                    }
                    imageUri = Uri.fromFile(new File(imageFile, "IMG_" + DateUtils.currentTime() + ".jpg"));
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                }
                break;
            case CROP_SMALL_PICTURE:
                if (resultCode == RESULT_OK) {
                    aliyunIdKey();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            if (requestCode == REQ_CODE) {
                final String videoPath = data.getStringExtra(WechatRecoderActivity.VIDEO_PATH);
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        mLoading = new LoadingDialog(mContext);
                        mLoading.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(videoPath);
                        Bitmap bitmap = media.getFrameAtTime();
                        String bitmapPhth = savePicture(bitmap);
                        //先上传背景图
                        AliYunImageUtils.newInstance().uploadImage(mContext, bitmapPhth, new ResultListener() {
                            @Override
                            public void onSucess(String data) {
                                aliyunIdKeyVideo(videoPath, data);
                            }

                            @Override
                            public void onFaild() {
                                if (mLoading != null && mLoading.isShowing()) {
                                    mLoading.dismiss();
                                }
                            }
                        });
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {

                    }
                }.execute();
            }

        }

    }

    /**
     * 上传视频封面图片和视频的路径
     *
     * @param videopath 视频封面图片
     * @param photo     视频的路径
     */
    private void aliyunIdKeyVideo(String videopath, final String photo) {
        AliYunImageUtils.newInstance().uploadVideo(mContext, videopath, new ResultListener() {
            @Override
            public void onSucess(String data) {
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
                LogUtils.e("videoPath--data" + data);

                ConcurrentHashMap map = new ConcurrentHashMap<>();
                map.put("userId", appUser.getId());
                map.put("thumbnailUrl", photo);//视频封面
                map.put("url", data);//视频路径
                httpDatas.getDataForJson("上传视频", false, Request.Method.POST, UrlBuilder.MY_VIDEO_UPLOAD, map, mHandler, RequestCode.MY_VIDEO_UPLOAD, TAG);
            }

            @Override
            public void onFaild() {
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        });
    }

    /**
     * 开始对图片进行裁剪处理
     *
     * @param uri 图片url
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }


    /**
     * 请求阿里云id、key，成功后上传头像
     *
     * @author sll
     * @time 2016/11/15 13:59
     */
    private void aliyunIdKey() {
        AliYunImageUtils.newInstance().uploadImage(mContext, imageUri.getPath(), new ResultListener() {
            @Override
            public void onSucess(String data) {
                ConcurrentHashMap map = new ConcurrentHashMap<>();
                map.put("userId", appUser.getId());
                LogUtils.e("headUrl" + data);
                map.put("url", data);
                httpDatas.getDataForJson("上传图库图片", true, Request.Method.POST, UrlBuilder.MY_PHOTO_UPLOAD, map, mHandler, RequestCode.MY_PHOTO_UPLOAD_CODE, TAG);
            }

            @Override
            public void onFaild() {
            }
        });
    }


    @SuppressLint("SdCardPath")
/**
 * 保存图片
 */
    public String savePicture(Bitmap bitmap) {
        String pictureName = "/mnt/sdcard/" + "lemeng" + ".jpg";
        File file = new File(pictureName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
            return pictureName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pictureName;
    }


    /**
     * 注册网易云信监听
     *
     * @param register
     */
    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }


    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            }
        }
    };


    /**
     * 退出登录（被挤下来了）
     *
     * @author sll
     * @time 2016/11/23 11:48
     */
    private void kickOut(StatusCode code) {
        Preferences.saveUserToken("");
        onLogout();
    }


    /**
     * 注销
     */
    private void onLogout() {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(LoginActivity.KICK_OUT, true);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Subscribe
    public void onEventMainThread(QuitLogin quit) {
        //接收自设置页面的 退出登录
        defaultFinish();
    }

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
//        MainTab tab = MainTab.fromReminderId(item.getId());
//        if (tab != null) {
//            tabs.updateTab(tab.tabIndex, item);
//        }
        int unread = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        if (item == null || tabNewMsgLabel == null || tabNewIndicator == null) {
            if (tabNewMsgLabel != null)
                tabNewMsgLabel.setVisibility(View.GONE);
            if (tabNewIndicator != null)
                tabNewIndicator.setVisibility(View.GONE);
            return;
        }
//        int unread = item.unread();
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
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
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
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    /**
     * 初始化未读红点动画
     */
    private void initUnreadCover() {
        DropManager.getInstance().init(getContext(), unreadCover,
                new DropCover.IDropCompletedListener() {
                    @Override
                    public void onCompleted(Object id, boolean explosive) {
                        if (id == null || !explosive) {
                            return;
                        }

                        if (id instanceof RecentContact) {
                            RecentContact r = (RecentContact) id;
                            NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                            LogUtil.i("HomeFragment", "clearUnreadCount, sessionId=" + r.getContactId());
                        } else if (id instanceof String) {
                            if (((String) id).contentEquals("0")) {
                                List<RecentContact> recentContacts = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
                                for (RecentContact r : recentContacts) {
                                    if (r.getUnreadCount() > 0) {
                                        NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                                    }
                                }
                                LogUtil.i("HomeFragment", "clearAllUnreadCount");
                            } else if (((String) id).contentEquals("1")) {
                                NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
                                LogUtil.i("HomeFragment", "clearAllSystemUnreadCount");
                            }
                        }
                    }
                });
    }

    private void enableMsgNotification(boolean enable) {
        if (enable) {
            /**
             * 设置最近联系人的消息为已读
             *
             * @param account,    聊天对象帐号，或者以下两个值：
             *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
             *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
             */
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }


    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if ((now - firstPressed <= dowableClick) && firstPressed != 0) {
            super.onBackPressed();
            EventBus.getDefault().post(new QuitApp());
            System.exit(0);
        } else {
            showToast(getString(R.string.again_press_quit));
            firstPressed = now;
        }
    }

    /**
     * 一键开播
     */
    public void quickStartLivePop() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.start_prapare_pop, null);
        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        final CustomPopWindow praparePop = new CustomPopWindow(view, width, height, this);
        praparePop.setFocusable(true);
        praparePop.setBackgroundDrawable(new BitmapDrawable());
        praparePop.setOutsideTouchable(true);
        praparePop.setAnimationStyle(R.style.mypopwindow_anim_style);
        praparePop.showAtLocation(ll_myself, Gravity.BOTTOM, 0, 0);

        ImageView iv_prapare_live = (ImageView) view.findViewById(R.id.iv_prapare_live);
        ImageView iv_prapare_video = (ImageView) view.findViewById(R.id.iv_prapare_video);
        ImageView iv_prapare_delete = (ImageView) view.findViewById(R.id.iv_prapare_delete);

        iv_prapare_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PrapareStartActivity.class));
                praparePop.dismiss();
            }
        });
        iv_prapare_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WechatRecoderActivity.launchActivity(mContext, REQ_CODE);
                praparePop.dismiss();
            }
        });
        iv_prapare_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praparePop.dismiss();
            }
        });
    }

    /**
     * 获取直播、私信、游戏开关状态
     */
    private void getStatus() {
        OkHttpUtils.get().url(UrlBuilder.START_LIVE_STATUS).build().execute(
                new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        showToast(getString(R.string.network_error));
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equals("0")) {
                                String obj = jsonObject.getString("obj");
                                LogUtil.e("开播准备", obj);
                                List<LiveStatusBean> list = JsonUtil.json2BeanList(obj, LiveStatusBean.class);
                                if (list != null && list.size() > 0) {
                                    for (int i = 0, j = list.size(); i < j; i++) {
                                        if (list.get(i).getFun().equals("anchor")) {
                                            int anchorState = list.get(i).getState();
                                            LogUtils.e("anchorState==" + anchorState);
                                            Constant.anchorState = anchorState;
                                        }
                                        if (list.get(i).getFun().equals("privateChat")) {
                                            int privateState = list.get(i).getState();
                                            LogUtils.e("privateState==" + privateState);
                                            Constant.privateState = privateState;
                                            InputPanel.privateState = privateState;
                                        }
                                        if (list.get(i).getFun().equals("game")) {
                                            int gameState = list.get(i).getState();
                                            LogUtils.e("gameState==" + gameState);
                                            Constant.gameState = gameState;
                                        }
                                    }
                                }
                                EventBus.getDefault().post(new GlobalSwitch());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

        );
    }

    /**
     * 启动应用版本更新服务
     */
    private void startAppVersionUpdate() {
        // 启动应用静默下载安装服务
        Intent intent = new Intent(this, UpdateApkService.class);
        this.startService(intent);
    }
}
