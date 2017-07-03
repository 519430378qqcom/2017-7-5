package com.lvshandian.lemeng.entity.bullfight;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/5/26
 * version: 1.0
 * desc   : 牛牛游戏投注返回是否成功的bean类
 */

public class BetResult {

    /**
     * success : true
     * code : 1
     * msg : 购买成功
     */
    private boolean success;
    private int code;
    private String msg;

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
}
