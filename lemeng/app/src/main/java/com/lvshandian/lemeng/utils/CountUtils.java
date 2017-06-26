package com.lvshandian.lemeng.utils;

import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.R;

import java.math.RoundingMode;

/**
 * Created by ssb on 2017/4/21 21:07.
 * company: lvshandian
 */

public class CountUtils {

    /**
     * 计算
     *
     * @param number
     * @return
     */
    public static String getCount(long number) {
        String str = "";
        if (number >= 100000000) {
            str = DecimalUtils.divideWithRoundingModeAndScale(number + "", "100000000", RoundingMode.UP, 2)
                    + MyApplication.mContext.getResources().getString(R.string.a_hundred_million);
        } else if (number >= 10000) {
            str = DecimalUtils.divideWithRoundingModeAndScale(number + "", "10000", RoundingMode.UP, 2)
                    + MyApplication.mContext.getResources().getString(R.string.ten_thousand);
        } else {
            str = number + "";
        }
        return str;
    }
}
