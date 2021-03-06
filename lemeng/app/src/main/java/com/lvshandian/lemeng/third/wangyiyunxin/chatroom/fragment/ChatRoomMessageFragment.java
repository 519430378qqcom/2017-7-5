package com.lvshandian.lemeng.third.wangyiyunxin.chatroom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.interfaces.CustomStringCallBack;
import com.lvshandian.lemeng.entity.AppUser;
import com.lvshandian.lemeng.net.HttpDatas;
import com.lvshandian.lemeng.utils.JavaBeanMapUtils;
import com.lvshandian.lemeng.utils.KeyBoardUtils;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.ToastUtils;
import com.lvshandian.lemeng.third.wangyiyunxin.chatroom.helper.ChatRoomMemberCache;
import com.lvshandian.lemeng.third.wangyiyunxin.chatroom.module.ChatRoomMsgListPanel;
import com.lvshandian.lemeng.third.wangyiyunxin.config.DemoCache;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nim.uikit.session.module.ModuleProxy;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天室直播互动fragment
 * Created by hzxuwen on 2015/12/16.
 */
public class ChatRoomMessageFragment extends TFragment implements ModuleProxy {
    private View rootView;
    private LinearLayout messageInputLayout;
    // modules
    protected ChatRoomInputPanel inputPanel;
    protected ChatRoomMsgListPanel messageListPanel;
    private String roomId;
    private LayoutInflater inflater;
    private EditText editText;

    protected View snackView;
    protected HttpDatas httpDatas;

    /**
     * 点击其他地方隐藏软键盘输入框
     *
     * @author sll
     * @time 2016/12/13 10:41
     */
    public void hideEditText() {
        if (messageInputLayout.getVisibility() == View.VISIBLE) {
            messageInputLayout.setVisibility(View.GONE);
//            messageInputLayout.setVisibility(View.INVISIBLE);
//            inputType.setVisibility(View.VISIBLE);
            KeyBoardUtils.closeKeybord(editText, MyApplication.mContext);
        }
    }

    /**
     * 显示软键盘输入框
     *
     * @author ssb
     * @time 2017/3/1 10:41
     */
    public void showEditText() {
        if (messageInputLayout.getVisibility() != View.VISIBLE) {
            messageInputLayout.setVisibility(View.VISIBLE);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            editText.findFocus();

            boolean toggleState = inputPanel.getToggleState();
            if (toggleState) {
                editText.setHint(rootView.getContext().getString(com.netease.nim.uikit.R.string.one_lepiao_one_tanmu));
            } else {
                editText.setHint(rootView.getContext().getString(com.netease.nim.uikit.R.string.say_something));
            }
            KeyBoardUtils.openKeybord(editText, MyApplication.mContext);

//            inputType.setVisibility(View.INVISIBLE);
        } else {
            hideEditText();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        rootView = inflater.inflate(R.layout.chat_room_message_fragment, container, false);
        editText = (EditText) rootView.findViewById(com.netease.nim.uikit.R.id.editTextMessage);
        messageInputLayout = (LinearLayout) rootView.findViewById(R.id.messageActivityBottomLayout);
        snackView = getActivity().getWindow().getDecorView().getRootView();
        httpDatas = new HttpDatas(getActivity(), snackView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (inputPanel != null) {
            inputPanel.onPause();
        }
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

    public boolean onBackPressed() {
        if (inputPanel != null && inputPanel.collapse(true)) {
            return true;
        }
        if (messageListPanel != null && messageListPanel.onBackPressed()) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        if (messageListPanel != null) {
            messageListPanel.onDestroy();
        }
    }

    public void onLeave() {
        if (inputPanel != null) {
            inputPanel.collapse(false);
        }
    }

    public void init(String roomId) {
        this.roomId = roomId;
        registerObservers(true);
        findViews();
        messageListPanel.addHeadView(inflater);
    }

    AppUser appUser;

    private void findViews() {
//        appUser = (AppUser) CacheUtils.readObject(getContext(), CacheUtils.USERINFO);
        appUser = SharedPreferenceUtils.getUserInfo(getContext());
        Map<String, Object> date = JavaBeanMapUtils.beanToMap(appUser);
        date.put("userId", appUser.getId());

        Container container = new Container(getActivity(), roomId, SessionTypeEnum.ChatRoom, this);
        if (messageListPanel == null) {
            messageListPanel = new ChatRoomMsgListPanel(container, rootView);
        }
        if (inputPanel == null) {
            inputPanel = new ChatRoomInputPanel(container, rootView, getActionList(), false);
            inputPanel.setEmojiButtonVisibility(View.GONE);
            inputPanel.setInputToggleButtonVisibility(appUser.getId(), appUser.getVip());
//            SendRoomMessageUtils.onCustomMessage(this, container, SendRoomMessageUtils.MESSAGE_JOIN_ROOM, date);
        } else {
            inputPanel.reload(container, null);
        }
    }

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

    /**************************
     * Module proxy
     ***************************/
    RequestCallback requestCallback = new RequestCallback<Void>() {
        @Override
        public void onSuccess(Void param) {
//            LogUtils.i("WangYi", "消息发送onSuccess:" + message.getMsgType());
        }

        @Override
        public void onFailed(int code) {
            if (code == ResponseCode.RES_CHATROOM_MUTED) {
                LogUtils.i("WangYi", "用户被禁言");
//                            Toast.makeText(DemoCache.getContext(), "用户被禁言", Toast.LENGTH_SHORT).show();
            } else if (code == ResponseCode.RES_CHATROOM_ROOM_MUTED) {
                LogUtils.i("WangYi", "全体禁言");
//                            Toast.makeText(DemoCache.getContext(), "全体禁言", Toast.LENGTH_SHORT).show();
            } else {
                LogUtils.i("WangYi", "消息发送失败：code:" + code);
//                            Toast.makeText(DemoCache.getContext(), "消息发送失败：code:" + code, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onException(Throwable exception) {
            LogUtils.i("WangYi", "消息发送失败！");
//                        Toast.makeText(DemoCache.getContext(), "消息发送失败！", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean sendMessage(IMMessage msg) {
        final ChatRoomMessage message = (ChatRoomMessage) msg;
        if (message.getMsgType() != MsgTypeEnum.custom) {
            Map<String, Object> ext = new HashMap<>();
            ext.put("userId", appUser.getId());
            ext.put("vip", appUser.getVip());
            ext.put("danmu", inputPanel.getToggleState() + "");
            ext.put("level", SharedPreferenceUtils.getLevel(getContext()));

            ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, DemoCache.getAccount());
            LogUtils.i("WangYi", "Account:" + DemoCache.getAccount());
            if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                ext.put("type", chatRoomMember.getMemberType().getValue());
            }
            message.setRemoteExtension(ext);
        }
        ///api/v1/barrage/%@", userId
        String url = UrlBuilder.SERVER_URL + UrlBuilder.BARRAGE;
        url += appUser.getId();
        if (inputPanel.getToggleState()) {
            OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack(getActivity(), HttpDatas.KEY_CODE) {
                @Override
                public void onFaild() {
                    ToastUtils.showMessageDefault(getActivity(), "余额不足，发送弹幕消息失败");
                }

                @Override
                public void onSucess(String data) {
                    if (data != null && data.equals("true")) {
                        NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                                .setCallback(requestCallback);
                        messageListPanel.onMsgSend(message);
                    }
                }
            });
        } else {
            NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                    .setCallback(requestCallback);
            messageListPanel.onMsgSend(message);
        }

        //发送后隐藏输入框
        hideEditText();
        return true;
    }


    public void sendLianmaiMessage(IMMessage msg) {
        ChatRoomMessage message = (ChatRoomMessage) msg;
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                .setCallback(requestCallback);
        messageListPanel.onMsgSend(msg);

    }

    /**
     * 发送本地消息，只在自己的聊天列表中展示，用于点播表演的
     *
     * @author sll
     * @time 2016/12/9 11:44
     */
    public void sendLocalMessage(IMMessage msg) {
        messageListPanel.onMsgSend(msg);
    }

    @Override
    public void onInputPanelExpand() {
        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputPanel.isRecording();
    }

    // 操作面板集合
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
//        actions.add(new GuessAction());
        return actions;
    }


}
