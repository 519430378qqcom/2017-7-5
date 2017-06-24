package com.lvshandian.lemeng.moudles.mine.fragment;

import android.support.v7.widget.RecyclerView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseFragment;

import butterknife.Bind;

/**
 * 取款记录
 */
public class WithdrawRecordFragment extends BaseFragment {

    @Bind(R.id.withdrawal_record)
    RecyclerView recyclerView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_withdrawal_record;
    }


    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {

    }
}
