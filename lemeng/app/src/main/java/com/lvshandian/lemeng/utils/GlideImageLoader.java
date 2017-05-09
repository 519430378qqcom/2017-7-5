package com.lvshandian.lemeng.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * banner的工具类
 * Created by ssb on 2017/3/10 15:11
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage((String) path,
                imageView);
    }
}
