package com.lvshandian.lemeng.moudles.index.live.bullfight;

/**
 * Created by dong on 2017/5/26.
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

        private int amount;
        private int mount;

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
