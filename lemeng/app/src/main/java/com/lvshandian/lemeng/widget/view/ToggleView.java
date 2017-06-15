package com.lvshandian.lemeng.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.lvshandian.lemeng.R;


/**
 * Created by gjj on 2016/12/26.
 */

public class ToggleView extends ImageView {
    public boolean isOpen() {
        return isOpen;
    }

    private boolean isOpen;

    public ToggleView(Context context) {
        this(context, null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ToggleView);//TypedArray是一个数组容器
        isOpen = array.getBoolean(R.styleable.ToggleView_is_open, false);
        array.recycle();
        init();
    }

    private void init() {
        setImageResource(isOpen ? R.mipmap.handle : R.mipmap.handleguanbi);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
      /*  isOpen = !isOpen;
        init();*/

        float downX;
        float moveX;
        float upX;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();

                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                isOpen = !isOpen;
                init();
                break;
        }
        return true;
    }
}
