package com.lvshandian.lemeng.entity.bullfight;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/5/26
 * version: 1.0
 * desc   : 获取牛牛游戏结果的bean类
 */

public class GameResult {


    /**
     * success : true
     * code : 1
     * msg : 开奖记录
     * obj : {"amount":0,"mount":0}
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
         * amount : 0
         * mount : 0
         */
        /**
         * 本金加中奖金额
         */
        private int amount;
        /**
         * 中奖金额
         */
        private int mount;
        /**
         * 庄家输赢
         */
        private int tmount;

        public int getTmount() {
            return tmount;
        }

        public void setTmount(int tmount) {
            this.tmount = tmount;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getMount() {
            return mount;
        }

        public void setMount(int mount) {
            this.mount = mount;
        }
    }
}
