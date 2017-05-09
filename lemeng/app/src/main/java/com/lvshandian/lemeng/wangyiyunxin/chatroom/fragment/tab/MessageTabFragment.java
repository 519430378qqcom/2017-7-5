package com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.constant.ChatRoomTab;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment.ChatRoomMessageFragment;
import com.lvshandian.lemeng.wangyiyunxin.chatroom.fragment.ChatRoomMessageFragmentGift;


/**
 * 直播互动基类fragment
 * Created by hzxuwen on 2015/12/14.
 */
public class MessageTabFragment extends ChatRoomTabFragment {
    private ChatRoomMessageFragment fragment;
    private ChatRoomMessageFragmentGift fragmentGift;

    public MessageTabFragment() {
        this.setContainerId(ChatRoomTab.CHAT_ROOM_MESSAGE.fragmentId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }

    @Override
    protected void onInit() {
        findViews();
    }

    @Override
    public void onCurrent() {
        super.onCurrent();
    }

    @Override
    public void onLeave() {
        super.onLeave();
        if (fragment != null) {
            fragment.onLeave();
        }
    }

    private void findViews() {
        fragment = (ChatRoomMessageFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.chat_room_message_fragment);
        fragmentGift = (ChatRoomMessageFragmentGift) getActivity().getSupportFragmentManager().findFragmentById(R.id.chat_room_message_fragment);
    }
}
