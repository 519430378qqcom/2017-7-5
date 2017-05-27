package com.lvshandian.lemeng.moudles.index.live.bullfight;

/**
 * Created by dong on 2017/5/26.
 */

public class BankerInfo {

    /**
     * success : true
     * code : 1
     * msg : 获取庄家信息
     * obj : {"nickName":"箫乾","id":100000,"livePicUrl":"http://wx.qlogo.cn/mmopen/TFozZgRZn67ibCNI9kCpK1x8icFQg9eDft5Y4iaZy7TW9yqc3gIeG7VcXgmOWsJIosFf3vnYSuSiaORicvGAOAT0uBx8uj9uhVBm6/0","goldCoin":998500}
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
         * nickName : 箫乾
         * id : 100000
         * livePicUrl : http://wx.qlogo.cn/mmopen/TFozZgRZn67ibCNI9kCpK1x8icFQg9eDft5Y4iaZy7TW9yqc3gIeG7VcXgmOWsJIosFf3vnYSuSiaORicvGAOAT0uBx8uj9uhVBm6/0
         * goldCoin : 998500
         */

        private String nickName;
        private int id;
        private String livePicUrl;
        private int goldCoin;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLivePicUrl() {
            return livePicUrl;
        }

        public void setLivePicUrl(String livePicUrl) {
            this.livePicUrl = livePicUrl;
        }

        public int getGoldCoin() {
            return goldCoin;
        }

        public void setGoldCoin(int goldCoin) {
            this.goldCoin = goldCoin;
        }
    }
}
