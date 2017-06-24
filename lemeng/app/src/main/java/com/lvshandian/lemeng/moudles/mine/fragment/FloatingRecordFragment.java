package com.lvshandian.lemeng.moudles.mine.fragment;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;

/**
 * 盈亏记录
 */
public class FloatingRecordFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.linear_floating_fragment_first_gray)
    AutoLinearLayout linear_floating_fragment_first_gray;
    @Bind(R.id.floating_record_listview_fragment)
    RecyclerView recyclerView;
    @Bind(R.id.fragment_date_floating_record)
    TextView fragment_date_floating_record;
    @Bind(R.id.fragment_lepiao_floating_record)
    TextView fragment_lepiao_floating_record;
    @Bind(R.id.calendar_floating_fragment)
    ImageView calendar_floating_fragment;
    @Bind(R.id.activity_personal_center_floating_linear)
    AutoRelativeLayout autoRelativeLayout_floating;


    private List<FloatingRecordBean.DataBean> floatingRecorcList = new ArrayList<>();
    private FloatingRecordAdapter recordAdapter;
    private int page = 1;
    private int totalPage = 1;
    private String starttime;
    private String endtime;
    private int inputMoney; //支出乐票
    private int incomeMoney;//收入乐票

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_floating_record;
    }

    @Override
    protected void initListener() {
        calendar_floating_fragment.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recordAdapter = new FloatingRecordAdapter(mContext, floatingRecorcList);
        recyclerView.setAdapter(recordAdapter);
        selecteFloating();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.calendar_floating_fragment:
                showPopupWindow();
                break;
        }

    }

    private void selecteFloating() {
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
                        fragment_lepiao_floating_record.setText(getString(R.string.floating_lepiao_hint, String.valueOf(inputMoney), String.valueOf(incomeMoney)));
                        List<FloatingRecordBean.DataBean> list = floatingRecord.getData();
                        floatingRecorcList.addAll(list);
                        recordAdapter.notifyDataSetChanged();
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
                linear_floating_fragment_first_gray.setVisibility(View.INVISIBLE);
            }
        });

        textConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                linear_floating_fragment_first_gray.setVisibility(View.INVISIBLE);
                fragment_date_floating_record.setText(currentSet_year + mContext.getResources().getString(R.string.text_year) + currentSet_month + mContext.getResources().getString(R.string.text_month));
            }
        });

        popupWindow.showAtLocation(autoRelativeLayout_floating, Gravity.BOTTOM, 0, 0);

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


}
