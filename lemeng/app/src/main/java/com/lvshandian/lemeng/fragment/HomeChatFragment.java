package com.lvshandian.lemeng.fragment;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.third.wangyiyunxin.main.fragment.SessionListFragment;

/**
 * 私信页面
 */
public class HomeChatFragment extends BaseFragment {
    SessionListFragment sessionListFragment;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_chat;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        sessionListFragment = (SessionListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.messages_fragment);
    }


}
