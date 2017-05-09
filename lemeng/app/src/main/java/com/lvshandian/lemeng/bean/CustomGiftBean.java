package com.lvshandian.lemeng.bean;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 发送礼物自定义消息
 * Created by sll on 2016/11/30.
 */

public class CustomGiftBean implements Serializable {
    /**
     * gift_Grand_Prix_number=[] :   //中奖礼物位置的集合
     * gift_item_number : 1    //礼物的数量
     * userId : 12718     //送礼物人id
     * gift_item_index : 4     //礼物的Id
     * gift_item_message :     //礼物描述
     * gift_coinnumber_index : 72     //主播得到金币数
     * type : 109
     * gift_Grand_Prix=[] : //中奖倍数集合
     * receveUserId:14256   //收礼物人id
     * gift_Type1: 0    //礼物的类型，有没有特效
     * level:1   //送礼物人的等级
     * gift_giftCoinNumber: 180    //礼物的价格
     * RepeatGiftNumber 连击的次数
     */

    private String[] gift_Grand_Prix_number;
    private String gift_item_number;
    private String userId;
    private String gift_item_index;
    private String gift_item_message;
    private String gift_coinnumber_index;
    private String type;
    private String[] gift_Grand_Prix;
    private String vip;
    private String receveUserId;
    private String gift_Type1;
    private String level;
    private String gift_giftCoinNumber;
    private String RepeatGiftNumber;

    public String getRepeatGiftNumber() {
        return RepeatGiftNumber;
    }

    public void setRepeatGiftNumber(String RepeatGiftNumber) {
        this.RepeatGiftNumber = RepeatGiftNumber;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getReceveUserId() {
        return receveUserId;
    }

    public void setReceveUserId(String receveUserId) {
        this.receveUserId = receveUserId;
    }

    public String getGift_Type1() {
        return gift_Type1;
    }

    public void setGift_Type1(String gift_Type1) {
        this.gift_Type1 = gift_Type1;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGift_giftCoinNumber() {
        return gift_giftCoinNumber;
    }

    public void setGift_giftCoinNumber(String gift_giftCoinNumber) {
        this.gift_giftCoinNumber = gift_giftCoinNumber;
    }

    public String[] getGift_Grand_Prix_number() {
        return gift_Grand_Prix_number;
    }

    public void setGift_Grand_Prix_number(String[] gift_Grand_Prix_number) {
        this.gift_Grand_Prix_number = gift_Grand_Prix_number;
    }

    public String getGift_coinnumber_index() {
        return gift_coinnumber_index;
    }

    public void setGift_coinnumber_index(String gift_coinnumber_index) {
        this.gift_coinnumber_index = gift_coinnumber_index;
    }

    public String[] getGift_Grand_Prix() {
        return gift_Grand_Prix;
    }

    public void setGift_Grand_Prix(String[] gift_Grand_Prix) {
        this.gift_Grand_Prix = gift_Grand_Prix;
    }

    public String getGift_item_index() {
        return gift_item_index;
    }

    public void setGift_item_index(String gift_item_index) {
        this.gift_item_index = gift_item_index;
    }

    public String getGift_item_message() {
        return gift_item_message;
    }

    public void setGift_item_message(String gift_item_message) {
        this.gift_item_message = gift_item_message;
    }

    public String getGift_item_number() {
        return gift_item_number;
    }

    public void setGift_item_number(String gift_item_number) {
        this.gift_item_number = gift_item_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CustomGiftBean{" +
                "gift_Grand_Prix_number=" + Arrays.toString(gift_Grand_Prix_number) +
                ", gift_item_number='" + gift_item_number + '\'' +
                ", userId='" + userId + '\'' +
                ", gift_item_index='" + gift_item_index + '\'' +
                ", gift_item_message='" + gift_item_message + '\'' +
                ", gift_coinnumber_index='" + gift_coinnumber_index + '\'' +
                ", type='" + type + '\'' +
                ", gift_Grand_Prix=" + Arrays.toString(gift_Grand_Prix) +
                ", vip='" + vip + '\'' +
                ", receveUserId='" + receveUserId + '\'' +
                ", gift_Type1='" + gift_Type1 + '\'' +
                ", level='" + level + '\'' +
                ", gift_giftCoinNumber='" + gift_giftCoinNumber + '\'' +
                '}';
    }
}
