package com.lvshandian.lemeng.net;

/**
 * Created by zhang on 2016/10/11.
 */
public class NewSdkHttpResult {


    /**
     * success : true
     * code : 1
     * msg : 查询成功
     * obj : 0
     */

    private boolean success;
    private int code;
    private String msg;
    private String obj;

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

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }


    @Override
    public String toString() {
        return "NewSdkHttpResult{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", obj='" + obj + '\'' +
                '}';
    }

}
