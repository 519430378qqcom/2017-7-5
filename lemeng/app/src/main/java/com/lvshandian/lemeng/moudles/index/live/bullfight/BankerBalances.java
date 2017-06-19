package com.lvshandian.lemeng.moudles.index.live.bullfight;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/5/26
 * version: 1.0
 * desc   : 牛牛游戏获取庄家金币数的bean类
 */

public class BankerBalances {

    /**
     * success : true
     * code : 1
     * msg : 获取庄家金币数
     * obj : 998500
     */

    private boolean success;
    private int code;
    private String msg;
    private int obj;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getObj() {
        return obj;
    }

    public void setObj(int obj) {
        this.obj = obj;
    }
}