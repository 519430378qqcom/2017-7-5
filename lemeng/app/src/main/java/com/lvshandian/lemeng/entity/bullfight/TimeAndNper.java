package com.lvshandian.lemeng.entity.bullfight;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/5/26
 * version: 1.0
 * desc   : 牛牛游戏获取倒计时和期数的bean
 */

public class TimeAndNper {

    /**
     * success : true
     * code : 1
     * msg : 获取成功
     * obj : {"time":19,"perid":12}
     */

    private boolean success;
    private int code;
    private String msg;
    private ObjBean obj;

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

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * time : 19
         * perid : 12
         */

        private int time;
        private int perid;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getPerid() {
            return perid;
        }

        public void setPerid(int perid) {
            this.perid = perid;
        }
    }
}
