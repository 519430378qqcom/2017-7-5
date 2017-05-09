package com.lvshandian.lemeng.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

import com.lvshandian.lemeng.moudles.index.live.PrapareVedioActivity;


/**
 * 自定义的tabhost
 */
public class MyFragmentTabHost extends FragmentTabHost {

    private String mCurrentTag;
    private String mNoTabChangedTag = "1";
    private Context context;

    public MyFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    public void onTabChanged(String tag) {
        if (tag.equals(mNoTabChangedTag)) {
            Intent intent = new Intent(context, PrapareVedioActivity.class);
            context.startActivity(intent);
            setCurrentTabByTag(mCurrentTag);
        } else {
            super.onTabChanged(tag);
            mCurrentTag = tag;
        }
    }

    public void setNoTabChangedTag(String tag) {
        this.mNoTabChangedTag = tag;
    }
}
