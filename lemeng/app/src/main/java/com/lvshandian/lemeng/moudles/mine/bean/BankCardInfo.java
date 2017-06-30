package com.lvshandian.lemeng.moudles.mine.bean;

import java.io.Serializable;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/29
 * version: 1.0
 * desc   :
 */
public class BankCardInfo implements Serializable {

    /**
     * id : 6
     * userId : 100089
     * username : 冯骏
     * mobile : 18500390777
     * cardNum : 62221231231231231231312
     * cardType : 中国建设银⾏ 储蓄卡
     * bankAddress : 天通苑⽀⾏
     * createdTime : 2017-06-29 19:40:30
     * deleted : 0
     */

    private int id;
    private int userId;
    private String username;
    private String mobile;
    private String cardNum;
    private String cardType;
    private String bankAddress;
    private String createdTime;
    private int deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "BankCardInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", cardType='" + cardType + '\'' +
                ", bankAddress='" + bankAddress + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
