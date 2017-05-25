package com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.module.ChatRoomMsgListPanelGift;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nim.uikit.session.module.ModuleProxy;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * 聊天室直播礼物互动fragment
 * Created by shangshuaibo on 2017/3/1.
 */
public class ChatRoomMessageFragmentGift extends TFragment implements ModuleProxy {

    private View rootView;
    protected ChatRoomMsgListPanelGift messageListPanel;
    private String roomId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chat_room_message_fragment_gift, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (messageListPanel != null) {
            messageListPanel.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (messageListPanel != null) {
            messageListPanel.onResume();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        if (messageListPanel != null) {
            messageListPanel.onDestroy();
        }
    }

    public void init(String roomId) {
        this.roomId = roomId;
        registerObservers(true);
        findViews();
    }

    AppUser appUser;

    private void findViews() {
//        appUser = (AppUser) CacheUtils.readObject(getContext(), CacheUtils.USERINFO);
        appUser = SharedPreferenceUtils.getUserInfo(getContext());
        Container container = new Container(getActivity(), roomId, SessionTypeEnum.ChatRoom, this);
        if (messageListPanel == null) {
            messageListPanel = new ChatRoomMsgListPanelGift(container, rootView);
        }
    }

    @Override
    public boolean sendMessage(IMMessage msg) {
        final ChatRoomMessage message = (ChatRoomMessage) msg;
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                .setCallback(requestCallback);
        messageListPanel.onMsgSend(message);
        return true;
    }

    RequestCallback requestCallback = new RequestCallback<Void>() {
        @Override
        public void onSuccess(Void param) {
            LogUtil.i("发送礼物", "--onSuccess");
        }

        @Override
        public void onFailed(int code) {
            LogUtil.i("发送礼物", "--onFailed");
        }

        @Override
        public void onException(Throwable exception) {
            LogUtil.i("发送礼物", "--onException");
        }

    };

    private void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
    }

    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            messageListPanel.onIncomingMessage(messages);
        }
    };

    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }
}
