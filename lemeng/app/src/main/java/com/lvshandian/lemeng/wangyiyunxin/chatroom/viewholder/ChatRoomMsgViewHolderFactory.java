package com.lvshandian.lemeng.wangyiyunxin.chatroom.viewholder;

import android.content.Context;

import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.TextUtils;
import com.lvshandian.lemeng.wangyiyunxin.session.extension.GuessAttachment;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderFactory;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderUnknown;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天室消息项展示ViewHolder工厂类。
 */
public class ChatRoomMsgViewHolderFactory {

    private static HashMap<Class<? extends MsgAttachment>, Class<? extends MsgViewHolderBase>> viewHolders = new HashMap<>();

    static {
        // built in
        register(ChatRoomNotificationAttachment.class, ChatRoomMsgViewHolderNotification.class);
        register(GuessAttachment.class, ChatRoomMsgViewHolderGuess.class);
    }

    public static void register(Class<? extends MsgAttachment> attach, Class<? extends MsgViewHolderBase> viewHolder) {
        viewHolders.put(attach, viewHolder);
    }

    public static Class<? extends MsgViewHolderBase> getViewHolderByType(IMMessage message, Context context) {
        boolean isZhuBo = (boolean) SharedPreferenceUtils.get(context, "isZhuBo", false);
        String ZhuBoId = (String) SharedPreferenceUtils.get(context, "ZhuBoId", "");
        LogUtils.i("WangYi", "chatMsgType:" + message.getMsgType());
        if (message.getMsgType() == MsgTypeEnum.text) {
            return ChatRoomViewHolderText.class;
        } else if (message.getMsgType() == MsgTypeEnum.notification) {
            //通知消息
            return ChatRoomMsgViewHolderNotification.class;
        } else if (message.getMsgType() == MsgTypeEnum.custom) {
            //自定义消息
            Map<String, Object> remote = message.getRemoteExtension();
            String typeText = (String) remote.get("type");
            if (!TextUtils.isEmpty(typeText)) {
                int type = Integer.parseInt(typeText);
                switch (type) {
                    case 102:
                    case 103:
                    case 104:
                    case 105:
                    case 107:
                    case 109:
                    case 112:
                    case 113:
                    case 114:
                    case 199:
                    case 200:
                    case 2828:
                    case 1818:
                    case 2929:
                        return ChatRoomViewHolderCustom.class;
                    default:
                        return ChatRoomMsgViewHolderNotification.class;
                }
            } else {
                return ChatRoomMsgViewHolderNotification.class;
            }
        } else {
            Class<? extends MsgViewHolderBase> viewHolder = null;
            if (message.getAttachment() != null) {
                Class<? extends MsgAttachment> clazz = message.getAttachment().getClass();
                while (viewHolder == null && clazz != null) {
                    viewHolder = viewHolders.get(clazz);
                    if (viewHolder == null) {
                        clazz = MsgViewHolderFactory.getSuperClass(clazz);
                    }
                }
            }
            return viewHolder == null ? MsgViewHolderUnknown.class : viewHolder;
        }
    }

    public static int getViewTypeCount() {
        // plus text and unknown
        return viewHolders.size() + 2;
    }
}
