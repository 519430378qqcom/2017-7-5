package com.lvshandian.lemeng.widget.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


import com.lvshandian.lemeng.R;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by Administrator on 2016/3/14.
 */
public class LoadUrlImageView extends ImageView {
    private Activity aty;

    public LoadUrlImageView(Context context) {
        super(context);
        init(context);
    }

    public LoadUrlImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        aty = (Activity) context;

    }

    public LoadUrlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageLoadUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            setImageResource(R.mipmap.head_default);
            return;
        }
        // 由于头像地址默认加了一段参数需要去掉
        int end = url.indexOf('?');
        final String headUrl;
        if (end > 0) {
            headUrl = url.substring(0, end);
        } else {
            headUrl = url;
        }

        Core.getKJBitmap().display(this, headUrl, R.mipmap.head_default, 0, 0,
                new BitmapCallBack() {
                    @Override
                    public void onFailure(Exception e) {
                        super.onFailure(e);
                        if (aty != null) {
                            aty.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setImageResource(R.mipmap.head_default);
                                }
                            });
                        }
                        setImageResource(R.mipmap.head_default);
                    }
                });
    }
}
