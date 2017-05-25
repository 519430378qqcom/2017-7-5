package com.lvshandian.lemeng.wangyiyunxin.chatroom.module;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.lvshandian.lemeng.wangyiyunxin.chatroom.viewholder.ChatRoomMsgViewHolderFactory;
import com.netease.nim.uikit.UserPreferences;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.ui.listview.AutoRefreshListView;
import com.netease.nim.uikit.common.ui.listview.ListViewUtil;
import com.netease.nim.uikit.common.ui.listview.MessageListView;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.session.audio.MessageAudioControl;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nim.uikit.session.module.list.MsgAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 聊天室消息收发模块
 * Created by huangjun on 2016/1/27.
 */
public class ChatRoomMsgListPanelGift implements TAdapterDelegate {
    private static final int MESSAGE_CAPACITY = 500;
    private Container container;
    private View rootView;
    private MessageListView messageListView;
    private LinkedList<IMMessage> items;
    private MsgAdapter adapter;

    public ChatRoomMsgListPanelGift(Container container, View rootView) {
        this.container = container;
        this.rootView = rootView;
        init();
    }

    public void onResume() {
        setEarPhoneMode(UserPreferences.isEarPhoneModeEnable());
    }

    public void onPause() {
        MessageAudioControl.getInstance(container.activity).stopAudio();
    }

    public void onDestroy() {
        registerObservers(false);
    }


    private void init() {
        initListView();
        registerObservers(true);
    }

    private void initListView() {
        items = new LinkedList<>();
        adapter = new MsgAdapter(container.activity, items, this);
        messageListView = (MessageListView) rootView.findViewById(com.netease.nim.uikit.R.id.messageListView);
        messageListView.requestDisallowInterceptTouchEvent(true);
        messageListView.setMode(AutoRefreshListView.Mode.START);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        // adapter
        messageListView.setAdapter(adapter);
    }


    public void onIncomingMessage(List<ChatRoomMessage> messages) {
        boolean needScrollToBottom = ListViewUtil.isLastMessageVisible(messageListView);
        boolean needRefresh = false;
        List<ChatRoomMessage> addedListItems = new ArrayList<>(messages.size());
        Map<String, Object> remote0 = null;
        for (ChatRoomMessage message : messages) {

            // 保证显示到界面上的消息，来自同一个聊天室
            if (isMyMessage(message)) {
                if (message != null) {
                    remote0 = message.getRemoteExtension();
                }
                if (remote0 != null && !TextUtils.isEmpty((String) remote0.get("type")) && remote0.get("type").equals("109")) {
                    saveMessage(message, false);
                    addedListItems.add(message);
                    needRefresh = true;
                }
            }
        }
        //如果是礼物消息，发送广播给直播室
        if (addedListItems != null && addedListItems.size() > 0) {
            for (int i = 0, j = addedListItems.size(); i < j; i++) {
                ChatRoomMessage newMessage = addedListItems.get(i);
                Map<String, Object> remote = null;
                if (newMessage != null) {
                    remote = newMessage.getRemoteExtension();
                }
                if (newMessage != null && remote != null && !TextUtils.isEmpty((String) remote.get("type")) && (remote.get("type").equals("109") || remote.get("type").equals("107") || remote.get("type").equals("108") || remote.get("type").equals("105") || remote.get("type").equals("101") || remote.get("type").equals("103") || remote.get("type").equals("104") || remote.get("type").equals("202"))) {
                    //收到 109 礼物消息 发送广播
                    LogUtil.i("WangYi_gift", "发礼物广播");
                    container.activity.sendBroadcast(new Intent().setAction("WatchLiveActivity").putExtra("ChatRoomMessage", newMessage));
                } else if (newMessage != null && remote != null && !TextUtils.isEmpty((String) remote.get("danmu")) && remote.get("danmu").equals("true")) {
                    //发送弹幕消息
                    LogUtil.i("WangYi_gift", "发弹幕消息广播");
                    container.activity.sendBroadcast(new Intent().setAction("WatchLiveActivity").putExtra("ChatRoomMessage", newMessage));
                }
            }

        }
        if (needRefresh) {
            adapter.notifyDataSetChanged();
        }
        // incoming messages tip
        ChatRoomMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg) && needScrollToBottom) {
            ListViewUtil.scrollToBottom(messageListView);
        }
    }


    // 发送消息后，更新本地消息列表
    public void onMsgSend(IMMessage message) {
        // add to listView and refresh
        saveMessage(message, false);
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);

        //如果是礼物消息，发送广播给直播室
        if (message instanceof ChatRoomMessage && addedListItems != null && addedListItems.size() > 0) {
            ChatRoomMessage newMessage = (ChatRoomMessage) addedListItems.get(addedListItems.size() - 1);
            Map<String, Object> remote = null;
            if (newMessage != null) {
                remote = newMessage.getRemoteExtension();
            }
            if (newMessage != null && remote != null && !TextUtils.isEmpty((String) remote.get("type")) && ((remote.get("type")).equals("109") || (remote.get("type")).equals("107") || (remote.get("type")).equals("108") || (remote.get("type")).equals("105"))) {
                //收到 109 礼物消息 发送广播
                LogUtil.i("WangYi_gift", "发礼物广播");
                container.activity.sendBroadcast(new Intent().setAction("WatchLiveActivity").putExtra("ChatRoomMessage", newMessage));
            } else if (newMessage != null && remote != null && !TextUtils.isEmpty((String) remote.get("danmu")) && remote.get("danmu").equals("true")) {
                //发送弹幕消息
                LogUtil.i("WangYi_gift", "发弹幕消息广播");
                container.activity.sendBroadcast(new Intent().setAction("WatchLiveActivity").putExtra("ChatRoomMessage", newMessage));
            }
        }
        adapter.notifyDataSetChanged();
        ListViewUtil.scrollToBottom(messageListView);
    }


    public void saveMessage(final IMMessage message, boolean addFirst) {
        if (message == null) {
            return;
        }
        if (items.size() >= MESSAGE_CAPACITY) {
            items.poll();
        }
        if (addFirst) {
            items.add(0, message);
        } else {
            items.add(message);
        }
    }

    /**
     * *************** implements TAdapterDelegate ***************
     */
    @Override
    public int getViewTypeCount() {
        return ChatRoomMsgViewHolderFactory.getViewTypeCount();
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position, Context context) {
        return ChatRoomMsgViewHolderFactory.getViewHolderByType(items.get(position), context);
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }


    /**
     * ************************* 观察者 ********************************
     */
    private void registerObservers(boolean register) {
        ChatRoomServiceObserver service = NIMClient.getService(ChatRoomServiceObserver.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
    }

    /**
     * 消息状态变化观察者
     */
    Observer<ChatRoomMessage> messageStatusObserver = new Observer<ChatRoomMessage>() {
        @Override
        public void onEvent(ChatRoomMessage message) {
            if (isMyMessage(message)) {
                onMessageStatusChange(message);
            }
        }
    };
    /**
     * 消息附件上传/下载进度观察者
     */
    Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress progress) {
            onAttachmentProgressChange(progress);
        }
    };

    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(message.getStatus());
            item.setAttachStatus(message.getAttachStatus());
            if (item.getAttachment() instanceof AVChatAttachment
                    || item.getAttachment() instanceof AudioAttachment) {
                item.setAttachment(message.getAttachment());
            }
            refreshViewHolderByIndex(index);
        }
    }

    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            adapter.putProgress(item, value);
            refreshViewHolderByIndex(index);
        }
    }

    public boolean isMyMessage(ChatRoomMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }

    /**
     * 刷新单条消息
     *
     * @param index
     */
    private void refreshViewHolderByIndex(final int index) {
        container.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (index < 0) {
                    return;
                }
                Object tag = ListViewUtil.getViewHolderByIndex(messageListView, index);
                if (tag instanceof MsgViewHolderBase) {
                    MsgViewHolderBase viewHolder = (MsgViewHolderBase) tag;
                    viewHolder.refreshCurrentItem();
                }
            }
        });
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }
        return -1;
    }


    private void setEarPhoneMode(boolean earPhoneMode) {
        UserPreferences.setEarPhoneModeEnable(earPhoneMode);
        MessageAudioControl.getInstance(container.activity).setEarPhoneModeEnable(earPhoneMode);
    }
}