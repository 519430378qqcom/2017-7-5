package com.lvshandian.lemeng.utils;

import android.util.Log;

import com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment.ChatRoomMessageFragment;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment.ChatRoomMessageFragmentGift;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sll on 2016/11/28.
 */

public class SendRoomMessageUtils {

    public static String MESSAGE_CREATE_SHOW = "101"; //创建表演
    public static String MESSAGE_WATCH_SHOW = "102"; //观看表演
    public static String MESSAGE_START_SHOW = "103";//开始表演
    public static String MESSAGE_END_SHOW = "104"; //结束自己创建表演
    public static String MESSAGE_JOIN_ROOM = "105";//进入房间
    public static String MESSAGE_LEAVE_ROOM = "106"; //离开房间
    public static String MESSAGE_LIKE_LIGHT = "107";//点亮
    public static String MESSAGE_LIKE_CLICK = "108"; //点赞
    public static String MESSAGE_REWARD_ROOM = "109"; //用户打赏
    public static String MESSAGE_WATCHER_CONNECT = "110"; //开始连线
    public static String MESSAGE_WATCHER_DISCONNECT = "111"; //断开连线
    public static String MESSAGE_ZHUBO_ENTERFOREGROUND = "112"; //主播恢复前台拍摄
    public static String MESSAGE_ZHUBO_RESIGNACTIVE = "113"; //主播切换到后台
    public static String MESSAGE_JIN_YAN = "114"; //禁言、取消禁言

    /**
     * 进来后发送进入room的扩展消息
     *
     * @author sll
     * @time 2016/11/22 20:44
     */
    public static void onCustomMessage(ChatRoomMessageFragment fragment, Container container, String type, Map<String, Object> date) {
        ChatRoomMessage msg = ChatRoomMessageBuilder.createChatRoomCustomMessage(container.account, new MsgAttachment() {
            @Override
            public String toJson(boolean b) {
                return "这是Custom消息，扩展消息";
            }
        });
        Map<String, Object> data = new HashMap<>();
        if (type.equals("105")) {
            data.put("data", date);
        } else {
            data.putAll(date);
        }
        data.put("type", type);
        msg.setRemoteExtension(data); // 设置服务器扩展字段
        Log.i("WangYi", "container.account" + container.account);
        fragment.sendMessage(msg);
    }


    /**
     * 主播切换/回来直播间
     *
     * @author sll
     * @time 2016/11/22 20:44
     */
    public static void onCustomMessageQiehuan(String type, ChatRoomMessageFragment fragment, String container, Map<String, Object> date) {
        ChatRoomMessage msg = ChatRoomMessageBuilder.createChatRoomCustomMessage(container, new MsgAttachment() {
            @Override
            public String toJson(boolean b) {
                return "这是Custom消息，扩展消息";
            }
        });
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.putAll(date);
        msg.setRemoteExtension(data); // 设置服务器扩展字段
        Log.i("WangYi", "container.account" + container);
        fragment.sendLianmaiMessage(msg);
    }

    /**
     * 连麦消息
     *
     * @author sll
     * @time 2016/11/22 20:44
     */
    public static void onCustomMessageLianmai(String type, ChatRoomMessageFragment fragment, String container, Map<String, Object> date) {
        ChatRoomMessage msg = ChatRoomMessageBuilder.createChatRoomCustomMessage(container, new MsgAttachment() {
            @Override
            public String toJson(boolean b) {
                return "这是Custom消息，扩展消息";
            }
        });
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.putAll(date);
        msg.setRemoteExtension(data); // 设置服务器扩展字段
        Log.i("WangYi", "container.account" + container);
        fragment.sendLianmaiMessage(msg);
    }


    /**
     * 打赏消息
     *
     * @author sll
     * @time 2016/11/22 20:44
     */
    public static void onCustomMessageSendGift(ChatRoomMessageFragmentGift fragment, String container, Map<String, Object> date) {
        ChatRoomMessage msg = ChatRoomMessageBuilder.createChatRoomCustomMessage(container, new MsgAttachment() {
            @Override
            public String toJson(boolean b) {
                return "这是Custom消息，扩展消息";
            }
        });
        Map<String, Object> data = new HashMap<>();
        data.put("type", MESSAGE_REWARD_ROOM);
        data.putAll(date);
        msg.setRemoteExtension(data); // 设置服务器扩展字段
        Log.i("WangYi", "container.account" + container);
        fragment.sendMessage(msg);
    }


    /**
     * 点赞 点亮
     *
     * @param fragment
     * @param container
     * @param date
     */
    public static void onCustomMessageDianzan(String type, ChatRoomMessageFragment fragment, String container, Map<String, Object> date) {
        ChatRoomMessage msg = ChatRoomMessageBuilder.createChatRoomCustomMessage(container, new MsgAttachment() {
            @Override
            public String toJson(boolean b) {
                return "这是Custom消息，扩展消息";
            }
        });
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.putAll(date);
        msg.setRemoteExtension(data); // 设置服务器扩展字段
        Log.i("WangYi", "container.account" + container);
        if (null != fragment) {
            fragment.sendMessage(msg);
        }

    }

    /**
     * 关注
     *
     * @param fragment
     * @param container
     * @param date
     */
    public static void onCustomMessageGuanzhu(String type, ChatRoomMessageFragment fragment, String container, Map<String, Object> date) {
        ChatRoomMessage msg = ChatRoomMessageBuilder.createChatRoomCustomMessage(container, new MsgAttachment() {
            @Override
            public String toJson(boolean b) {
                return "这是Custom消息，扩展消息";
            }
        });
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.putAll(date);
        msg.setRemoteExtension(data); // 设置服务器扩展字段
        Log.i("WangYi", "container.account" + container);
        if (null != fragment) {
            fragment.sendMessage(msg);
        }

    }

    /**
     * 禁言
     *
     * @param fragment
     * @param container
     * @param date
     */
    public static void onCustomMessageJinyan(String type, ChatRoomMessageFragment fragment, String container, Map<String, Object> date) {
        ChatRoomMessage msg = ChatRoomMessageBuilder.createChatRoomCustomMessage(container, new MsgAttachment() {
            @Override
            public String toJson(boolean b) {
                return "这是Custom消息，扩展消息";
            }
        });
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.putAll(date);
        msg.setRemoteExtension(data); // 设置服务器扩展字段
        Log.i("WangYi", "container.account" + container);
        if (null != fragment) {
            fragment.sendMessage(msg);
        }

    }

    /**
     * 发送本地消息，只在自己room聊天列表中展示
     *
     * @author sll
     * @time 2016/12/9 11:53
     */
    public static void sendMessageLocal(String type, ChatRoomMessageFragment fragment, String container, String text, String roomCID) {
        ChatRoomMessage msg = ChatRoomMessageBuilder.createChatRoomCustomMessage(container, new MsgAttachment() {
            @Override
            public String toJson(boolean b) {
                return "这是Custom消息，扩展消息";
            }
        });
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("text", text);
        msg.setRemoteExtension(data); // 设置服务器扩展字段
        msg.setFromAccount(roomCID);
        fragment.sendLocalMessage(msg);
    }
}
