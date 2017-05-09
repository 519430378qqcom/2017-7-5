package com.lvshandian.lemeng.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;

import butterknife.Bind;

/**
 * Created by JiaZhenPeng on 2016/7/26.
 */
public class TimeCountDownLayout extends LinearLayout {


    @Bind(R.id.tv_minute_one)
    TextView mTvMinuteOne;
    @Bind(R.id.tv_minute_two)
    TextView mTvMinuteTwo;
    @Bind(R.id.tv_second_one)
    TextView mTvSecondOne;
    @Bind(R.id.tv_second_two)
    TextView mTvSecondTwo;

    public TimeCountDownLayout(Context context) {
        this(context, null);
    }

    public TimeCountDownLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeCountDownLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view =
                LayoutInflater.from(context).inflate(R.layout.item_game_time_layout, this, true);
        mTvMinuteOne = (TextView) view.findViewById(R.id.tv_minute_one);
        mTvMinuteTwo = (TextView) view.findViewById(R.id.tv_minute_two);
        mTvSecondOne = (TextView) view.findViewById(R.id.tv_second_one);
        mTvSecondTwo = (TextView) view.findViewById(R.id.tv_second_two);
    }

    public void setText(String time){
        if (time == null) {
            return;
        }
        for (int i=0;i<time.length();i++){
            String s = time.charAt(i)+"";
            if (i==0){
                mTvMinuteOne.setText(s);
            }
            if (i==1){
                mTvMinuteTwo.setText(s);
            }
            if (i==3){
                mTvSecondOne.setText(s);
            }
            if (i==4){
                mTvSecondTwo.setText(s);
            }
        }
    }
}
