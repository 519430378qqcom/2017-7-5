package com.lvshandian.lemeng.moudles.index.live.redpackage;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/20
 * version: 1.0
 * desc   : 抢到的红包
 */
public class GrabPackageBean {
    /**
     * 0 为获取红包成功 1为异常 2 已经抢过红包 3 红包已经抢完
     */
    private int code;
    /**
     * 抢到红包的金额
     */
    private int data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
