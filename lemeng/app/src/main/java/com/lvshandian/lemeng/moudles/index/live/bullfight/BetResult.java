package com.lvshandian.lemeng.moudles.index.live.bullfight;

/**
 * Created by dong on 2017/5/26.
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
