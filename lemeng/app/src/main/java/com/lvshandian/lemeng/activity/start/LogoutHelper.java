package com.lvshandian.lemeng.activity.start;

import com.lvshandian.lemeng.third.wangyiyunxin.chatroom.helper.ChatRoomHelper;
import com.lvshandian.lemeng.third.wangyiyunxin.config.DemoCache;
import com.lvshandian.lemeng.third.wangyiyunxin.config.preference.Preferences;
import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.NimUIKit;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.clearCache();
        ChatRoomHelper.logout();
        DemoCache.clear();
        LoginSyncDataStatusObserver.getInstance().reset();
        Preferences.saveAppLogin("0");
        Preferences.saveWyyxLogin("0");
        //未读消息红点
//        DropManager.getInstance().destroy();
    }
}
