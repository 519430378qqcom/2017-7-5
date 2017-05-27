package com.lvshandian.lemeng.moudles.index.live.bullfight;

/**
 * Created by dong on 2017/5/26.
 */

public class StartResult {

    /**
     * success : false
     * code : 0
     * msg : 获取失败
     * obj : null
     */

    private boolean success;
    private int code;
    private String msg;
    private Object obj;

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

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
