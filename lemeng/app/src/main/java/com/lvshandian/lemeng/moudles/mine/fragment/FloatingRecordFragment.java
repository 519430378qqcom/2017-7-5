package com.lvshandian.lemeng.moudles.mine.fragment;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseFragment;
import com.lvshandian.lemeng.moudles.mine.adapter.FloatingRecordAdapter;
import com.lvshandian.lemeng.moudles.mine.bean.FloatingRecordBean;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.lvshandian.lemeng.widget.view.EmptyRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * 盈亏记录
 */
public class FloatingRecordFragment extends BaseFragment implements View.OnClickListener, SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener {
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recordAdapter = new FloatingRecordAdapter(mContext, floatingRecorcList);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setEmptyView(ll_empty);
        initDate();
        selecteFloating();
    }

    private void initDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(System.currentTimeMillis());
        String str = df.format(date);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_calendar:
                showPopupWindow();
                break;
        }

    }

    private void selecteFloating() {
        page = isRefresh ? 1 : ++page;
        String url = UrlBuilder.GAME_BASE + String.format(UrlBuilder.PROFIT_AND_LOSS_RECORD, "100018", String.valueOf(page), starttime, endtime);
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
                        floating_statistics.setText(getString(R.string.floating_lepiao_hint, String.valueOf(inputMoney), String.valueOf(incomeMoney)));
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

    int SetDate = 5;
    private NumberPicker np3;
    private int day = 9;
    private int month = 5;
    private int year = 2000;

    private int currentSet_year = 2000;
    private int currentSet_month = 5;
    private int currentSet_day = 18;

    public void showPopupWindow() {
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);
        currentSet_year = year;
        currentSet_month = month;
        currentSet_day = day;

        View contentView = getActivity().getLayoutInflater().inflate(R.layout.float_record_fragment_calendar, null);
        TextView textCancel = (TextView) contentView.findViewById(R.id.fragment_float_record_calendar_dismiss);
        TextView textConfirm = (TextView) contentView.findViewById(R.id.fragment_float_record_calendar_confirm);
        NumberPicker np1 = (NumberPicker) contentView.findViewById(R.id.fragment_floating_record_numberPicker1);
        NumberPicker np2 = (NumberPicker) contentView.findViewById(R.id.fragment_floating_record_numberPicker2);
        np3 = (NumberPicker) contentView.findViewById(R.id.fragment_floating_record_numberPicker3);

        np1.setMaxValue(3000);
        np1.setMinValue(1);
        np1.setValue(year);
        setNumberPickerDividerColor(np1);


        np2.setMaxValue(12);
        np2.setMinValue(1);
        np2.setValue(month);
        setNumberPickerDividerColor(np1);
        setNumberPickerDividerColor(np2);

        np1.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                SetDate = 1;
                String tmpStr = String.valueOf(value);
                if (value < 10) {
                    tmpStr = "0" + tmpStr;
                }
                return tmpStr;
            }
        });

        np2.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                String tmpStr = String.valueOf(value);
                if (value < 10) {
                    tmpStr = "0" + tmpStr;
                }
                return tmpStr;
            }
        });

        if (leapyear(year) == true) {
            SetDate = 29;
        } else {
            SetDate = 28;
        }

        np3.setMaxValue(SetDate);
        np3.setMinValue(1);
        np3.setValue(day);
        setNumberPickerDividerColor(np3);

        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                //    Toast.makeText(MainActivity.this,"oldVal"+newVal,Toast.LENGTH_LONG).show();

                if (leapyear(newVal) == true) {
                    SetDate = 29;
                } else {
                    SetDate = 28;
                }

                //  SetDate=newVal;
                currentSet_year = newVal;


                np3.setMaxValue(SetDate);
                np3.setMinValue(1);
                np3.setValue(day);


                setNumberPickerDividerColor(np3);

                np3.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int value) {
                        String tmpStr = String.valueOf(value);
                        if (value < 10) {
                            tmpStr = "0" + tmpStr;
                        }
                        return tmpStr;
                    }
                });

                np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        //      Toast.makeText(MainActivity.this,"oldVal"+newVal,Toast.LENGTH_LONG).show();
                        currentSet_day = newVal;
                    }
                });

            }
        });

        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                //     Toast.makeText(MainActivity.this,"oldVal"+newVal,Toast.LENGTH_LONG).show();
                currentSet_month = newVal;
            }
        });


        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSet_year = year;
                currentSet_month = month;
                currentSet_day = day;
                popupWindow.dismiss();
            }
        });

        textConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                fragment_date.setText(currentSet_year + mContext.getResources().getString(R.string.text_year) + currentSet_month + mContext.getResources().getString(R.string.text_month));
            }
        });

        popupWindow.showAtLocation(floating_calendar, Gravity.BOTTOM, 0, 0);

    }

    private boolean leapyear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
            return true;
        else
            return false;
    }

    /**
     * 自定义滚动框分隔线颜色
     */
    private void setNumberPickerDividerColor(NumberPicker number) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(number, new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.white)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        selecteFloating();
    }

    @Override
    public void onPullUpRefresh() {
        isRefresh = false;
        if (page < totalPage) {
            selecteFloating();
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
