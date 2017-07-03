package com.lvshandian.lemeng.fragment.mine;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.adapter.mine.FloatingRecordAdapter;
import com.lvshandian.lemeng.entity.mine.FloatingRecordBean;
import com.lvshandian.lemeng.fragment.BaseFragment;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.lvshandian.lemeng.widget.view.EmptyRecyclerView;
import com.lvshandian.lemeng.widget.view.FullyLinearLayoutManager;
import com.lvshandian.lemeng.widget.view.LWheelDialog;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * 盈亏记录
 */
public class FloatingRecordFragment extends BaseFragment implements View.OnClickListener,
        SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener, LWheelDialog.ConfirmListener {
    @Bind(R.id.ll_empty)
    LinearLayout ll_empty;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @Bind(R.id.fragment_date)
    TextView fragment_date;
    @Bind(R.id.floating_statistics)
    TextView floating_statistics;
    @Bind(R.id.floating_calendar)
    ImageView floating_calendar;


    private List<FloatingRecordBean.DataBean> floatingRecorcList = new ArrayList<>();
    private FloatingRecordAdapter recordAdapter;
    private int page = 1;
    private int totalPage = 1;
    private String starttime;
    private String endtime;
    private int inputMoney; //支出乐票
    private int incomeMoney;//收入乐票
    /**
     * 判断是刷新还是加载
     */
    private boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_floating_record;
    }

    @Override
    protected void initListener() {
        floating_calendar.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        //设置刷新逻辑
        refreshLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnPullUpRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main));

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recordAdapter = new FloatingRecordAdapter(mContext, floatingRecorcList);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setEmptyView(ll_empty);
        starttime = initDate(new Date(System.currentTimeMillis()));
        selecteFloating(appUser.getId(), page, starttime, endtime);
    }

    /**
     * 时间格式化
     *
     * @param data
     * @return
     */
    private String initDate(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        fragment_date.setText(df.format(data));
        String str = df.format(data) + "-01 00:00:00";
        return str;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_calendar:
                showDatePicker();
                break;
        }

    }

    /**
     * 获取盈亏记录
     *
     * @param userId
     * @param pagenum
     * @param starttime
     * @param endtime   可不传，写null即可
     */
    private void selecteFloating(String userId, int pagenum, String starttime, String endtime) {
        String url = null;
        page = isRefresh ? 1 : ++page;
        if (endtime == null || "".equals(endtime)) {
            url = UrlBuilder.GAME_BASE + String.format(UrlBuilder.PROFIT_AND_LOSS_RECORD, userId, 10, String.valueOf(page), starttime);
        } else {
            url = UrlBuilder.GAME_BASE + String.format(UrlBuilder.PROFIT_AND_LOSS_RECORD, userId, 10, String.valueOf(page), starttime) + "&endtime=" + endtime;
        }
        LogUtils.e(url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("response :" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("0")) {
                        FloatingRecordBean floatingRecord = JsonUtil.json2Bean(jsonObject.toString(), FloatingRecordBean.class);
                        totalPage = floatingRecord.getPageCount();
                        inputMoney = floatingRecord.getInputMoney();
                        incomeMoney = floatingRecord.getIncomeMoney();
                        if (floating_statistics != null) {
                            floating_statistics.setText(getString(R.string.floating_lepiao_hint, String.valueOf(inputMoney), String.valueOf(incomeMoney)));
                        }
                        List<FloatingRecordBean.DataBean> list = floatingRecord.getData();
                        if (isRefresh) {
                            floatingRecorcList.clear();
                        } else {
                            if (list == null && list.size() == 0) {
                                page--;
                            }
                        }
                        floatingRecorcList.addAll(list);
                        recordAdapter.notifyDataSetChanged();
                        finishRefresh();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        isRefresh = true;
        selecteFloating(appUser.getId(), page, starttime, endtime);
    }

    @Override
    public void onPullUpRefresh() {
        isRefresh = false;
        if (page < totalPage) {
            selecteFloating(appUser.getId(), page, starttime, endtime);
        } else {
            finishRefresh();
            showToast(getString(R.string.no_more));
        }
    }

    private void finishRefresh() {
        refreshLayout.setRefreshing(false);
        refreshLayout.setPullUpRefreshing(false);
    }

    public void showDatePicker() {
        LWheelDialog dialog = new LWheelDialog(mContext, LWheelDialog.LWheelDialogType.DATE);
        dialog.setConfirmListenter(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialogstyle_vertical); // 添加动画
        dialog.setCancelable(true);
        dialog.show();
        Activity activity = (Activity) mContext;
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() / 3);
        p.width = (int) (d.getWidth());
        dialog.onWindowAttributesChanged(p);
        window.setAttributes(p);
    }

    /**
     * 点击确定后获取到的时间
     *
     * @param year
     * @param month
     * @param day
     */
    @Override
    public void onConfirmListener(String year, String month, String day) {
        String month2 = month.length() == 2 ? month : "0" + month;
        String day2 = day.length() == 2 ? day : "0" + day;
        String s = year + "-" + month2 + "-" + day2;
        fragment_date.setText(s);
        starttime = s + " 00:00:00";
        endtime = s + " 23:59:59";
        isRefresh = true;
        selecteFloating(appUser.getId(), page, starttime, endtime);
    }

}
