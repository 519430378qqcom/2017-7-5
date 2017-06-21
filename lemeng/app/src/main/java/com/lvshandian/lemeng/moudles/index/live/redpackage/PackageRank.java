package com.lvshandian.lemeng.moudles.index.live.redpackage;

import java.util.List;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/20
 * version: 1.0
 * desc   : 红包排行
 */
public class PackageRank {

    /**
     * code : 0
     * data : [{"picUrl":"http://omfoe17e1.bkt.clouddn.com/image/headImg1111003506509.jpg","amount":1013,"nickName":"霓红超跑"},{"picUrl":"http://omfoe17e1.bkt.clouddn.com/image/headImg1111003610456.jpg","amount":880,"nickName":"绿树姐姐"},{"picUrl":"http://omfoe17e1.bkt.clouddn.com/image/headImg1111003520546.jpg","amount":836,"nickName":"秋采薇翔"},{"picUrl":"http://omfoe17e1.bkt.clouddn.com/image/headImg1111003529638.jpg","amount":140,"nickName":"Snoo**不含蓄奇淑婉"}]
     */

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * picUrl : http://omfoe17e1.bkt.clouddn.com/image/headImg1111003506509.jpg
         * amount : 1013
         * nickName : 霓红超跑
         */

        private String picUrl;
        private int amount;
        private String nickName;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
