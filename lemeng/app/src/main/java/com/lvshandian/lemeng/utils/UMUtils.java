package com.lvshandian.lemeng.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.lvshandian.lemeng.base.Constant;
import com.lvshandian.lemeng.bean.AppUser;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN_CIRCLE;


/**
 * 友盟分享工具类
 * Created by Administrator on 2017/2/23.
 */

public class UMUtils {


    private static Context mContext;
    private static Activity mActivity;

    public static void init(Context Context) {
        mContext = Context;
        //初始化友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(mContext);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 自定义消息的回调方法
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //统计自定义消息的打开
                            UTrack.getInstance(mContext).trackMsgClick(msg);
                        } else {
                            //统计自定义消息的忽略
                            UTrack.getInstance(mContext).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * 自定义通知样式
             */
//            @Override
//            public Notification getNotification(Context context,
//                                                UMessage msg) {
//                switch (msg.builder_id) {
//                    //自定义通知样式编号
//                    case 1:
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
//                        builder.setContent(myNotificationView);
//                        builder.setAutoCancel(true);
//                        Notification mNotification = builder.build();
//                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
//                        mNotification.contentView = myNotificationView;
//                        return mNotification;
//                    default:
//                        //默认为0，若填写的builder_id并不存在，也使用默认。
//                        return super.getNotification(context, msg);
//                }
//            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 自定义行为的回调处理
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * [url=http://dev.umeng.com/push/android/integration#1_6_2]http://dev.umeng.com/push/android/integration#1_6_2[/url]
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            //点击通知的自定义行为
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
//                if ("1".equals(msg.custom) || "2".equals(msg.custom) || "3".equals(msg.custom)) {
//                    Intent intent = new Intent(getApplicationContext(), WatchLiveActivity.class);
//                    intent.putExtra("title", msg.title);
//                    intent.putExtra("state", Integer.valueOf(msg.custom));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

//        AppUser appUser = (AppUser) CacheUtils.readObject(mContext, CacheUtils.USERINFO);
        AppUser appUser = SharedPreferenceUtils.getUserInfo(mContext);
        if (appUser != null) {
            mPushAgent.addExclusiveAlias("miu_" + appUser.getId(), "IM",
                    new UTrack.ICallBack() {
                        @Override
                        public void onMessage(boolean isSuccess, String message) {

                        }
                    });
        }
        Config.DEBUG = true;
        UMShareAPI.get(mContext);
        PlatformConfig.setTwitter(Constant.TWITTER_APPID, Constant.TWITTER_SECRET);
        PlatformConfig.setWeixin(Constant.WX_APPID, Constant.WX_SECRET);
        PlatformConfig.setQQZone(Constant.QQ_APPID, Constant.QQ_SECRET);

    }

    public static void umShare(Activity act, String share_title, String share_content, final String picUrl, String share_url) {
        mActivity = act;
        UMWeb web = new UMWeb(share_url);
        web.setTitle(share_title);//标题
        web.setThumb(new UMImage(act, picUrl));  //缩略图
        web.setDescription(share_content);//描述

        new ShareAction(act).withText(share_title)
//                .setDisplayList(FACEBOOK, TWITTER, GOOGLEPLUS, WEIXIN, QQ, WEIXIN_CIRCLE, QZONE)
                .setDisplayList(WEIXIN, WEIXIN_CIRCLE)
                .withMedia(web)
                .setCallback(umShareListener).open();

    }

    public static void umShareSingle(final Activity act, String share_title, String share_content, final String picUrl, String share_url, SHARE_MEDIA shareMedia) {
        mActivity = act;
        UMWeb web = new UMWeb(share_url);
        web.setTitle(share_title);//标题
        web.setThumb(new UMImage(act, picUrl));  //缩略图
        web.setDescription(share_content);//描述

        new ShareAction(act).withText(share_title)
                .setPlatform(shareMedia)
                .withMedia(web)
                .setCallback(umShareListener).share();

    }


    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
//            ToastUtils.showMessageDefault(mActivity,"分享成功");

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            ToastUtils.showMessageDefault(mActivity,"分享失败");
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            ToastUtils .showMessageDefault(mActivity,"分享取消");
        }
    };

}
