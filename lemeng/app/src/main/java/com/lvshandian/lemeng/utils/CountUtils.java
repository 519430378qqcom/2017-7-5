package com.lvshandian.lemeng.utils;

import java.math.RoundingMode;

/**
 * Created by ssb on 2017/4/21 21:07.
 * company: lvshandian
 */

public class CountUtils {

    /**
     * 计算
     * @param number
     * @return
     */
    public static String getCount(long number) {
        String str = "";
        if (number > 100000000) {
            str = DecimalUtils.divideWithRoundingModeAndScale(number + "", "100000000", RoundingMode.DOWN, 2) + "亿";
        } else if (number > 10000) {
            str = DecimalUtils.divideWithRoundingModeAndScale(number + "", "10000", RoundingMode.DOWN, 2) + "万";
        } else {
            str = number + "";
        }
        return str;
    }
}
