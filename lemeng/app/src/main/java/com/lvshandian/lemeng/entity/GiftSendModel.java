package com.lvshandian.lemeng.entity;

/**
 * Created by Administrator on 2016/12/1.
 */

public class GiftSendModel {

    private int giftCount;   //个数
    private String userAvatarRes;
    private String nickname;//昵称
    private String sig;   //
    private int giftRes;
    private String gift_id;  //礼物
    private int star;
    private String RepeatGiftNumber;//连点的次数
    private String fromUserId;//送礼物人ID
    private String giftId;//礼物ID

    public GiftSendModel(int giftCount, String userAvatarRes, String nickname, String sig, String gift_id, String RepeatGiftNumber, String fromUserId, String giftId) {
        this.giftCount = giftCount;
        this.userAvatarRes = userAvatarRes;
        this.nickname = nickname;
        this.sig = sig;
        this.gift_id = gift_id;
        this.RepeatGiftNumber = RepeatGiftNumber;
        this.fromUserId = fromUserId;
        this.giftId = giftId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getRepeatGiftNumber() {
        return RepeatGiftNumber;
    }

    public void setRepeatGiftNumber(String RepeatGiftNumber) {
        this.RepeatGiftNumber = RepeatGiftNumber;
    }

    public GiftSendModel(int giftCount) {
        this.giftCount = giftCount;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public String getUserAvatarRes() {
        return userAvatarRes;
    }

    public void setUserAvatarRes(String userAvatarRes) {
        this.userAvatarRes = userAvatarRes;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public int getGiftRes() {
        return giftRes;
    }

    public void setGiftRes(int giftRes) {
        this.giftRes = giftRes;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
