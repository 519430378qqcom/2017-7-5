package com.lvshandian.lemeng.moudles.mine.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseFragment;
import com.lvshandian.lemeng.moudles.mine.adapter.WithdrawRecordAdapter;
import com.lvshandian.lemeng.moudles.mine.bean.WithdrawRecordBean;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.lvshandian.lemeng.widget.view.EmptyRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 存款记录
 */
public class DepositRecordFragment extends BaseFragment implements SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener {
    @Bind(R.id.ll_empty)
    LinearLayout ll_empty;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.withdrawal_record)
    EmptyRecyclerView recyclerView;
    private List<WithdrawRecordBean.RowsBean> rowsList = new ArrayList<>();
    private WithdrawRecordAdapter withdrawRecordAdapter;
    private int page = 1;
    private int totalPage = 1;
    /**
     * 判断是刷新还是加载
     */
    private boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_withdrawal_record;
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initialized() {
        //设置刷新逻辑
        refreshLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnPullUpRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main));

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        withdrawRecordAdapter = new WithdrawRecordAdapter(mContext, rowsList);
        recyclerView.setAdapter(withdrawRecordAdapter);
        recyclerView.setEmptyView(ll_empty);
        selecteWithDraw();
    }

    private void selecteWithDraw() {
        page = isRefresh ? 1 : ++page;
        String url = UrlBuilder.GAME_BASE + String.format(UrlBuilder.WITHDRAWAL_RECORD, String.valueOf(page), "10", appUser.getId(), "0");
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                WithdrawRecordBean withdrawRecordBean = JsonUtil.json2Bean(response, WithdrawRecordBean.class);
                totalPage = withdrawRecordBean.getAllPages();
                List<WithdrawRecordBean.RowsBean> list = withdrawRecordBean.getRows();
                if (isRefresh) {
                    rowsList.clear();
                } else {
                    if (list == null && list.size() == 0) {
                        page--;
                    }
                }
                rowsList.addAll(list);
                withdrawRecordAdapter.notifyDataSetChanged();
                finishRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        selecteWithDraw();
    }

    @Override
    public void onPullUpRefresh() {
        isRefresh = false;
        if (page < totalPage) {
            selecteWithDraw();
        } else {
            finishRefresh();
            showToast(getString(R.string.no_more));
        }
    }

    private void finishRefresh() {
        refreshLayout.setRefreshing(false);
        refreshLayout.setPullUpRefreshing(false);
    }
}
