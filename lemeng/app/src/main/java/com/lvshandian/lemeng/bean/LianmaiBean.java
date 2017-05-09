package com.lvshandian.lemeng.bean;

import java.io.Serializable;

/**
 * Created by ssb on 2017/4/1 15:31.
 * company: lvshandian
 */

public class LianmaiBean implements Serializable {

    /**
     * amount : 0
     * applyTime : 2017-04-01 15:29:01
     * broadcastUrl : IwtWa+EvtZ/KUxJg7fGiekLKcUSQcXN6l9uYrG7kvL5h3TxuLvroa/IgHae86aK8qTEn8DnBfiRE
     EVgfhSxEqn/eZrsIjjtZRg4dGUWVGkw=
     * id : 332
     * nickName : 得天独厚
     * publishUrl : 5BZY+QU8ez6Nhq3w1lNOylSPrEY+njkX1RnXEuhP8TlWs3eHp3ykZAnS1f/rnsarp4k0Srv5L2yN
     bhgszzLeBDvxlA/beFjucTuBC1KX1uGIOEquGu2rouyboKB0LDtAJOTc5qpnh3toNuW8cIvETasK
     fo2aLwKIDAuZO4qPwlDTm0zD2yyzzQ1I5fGpAd1psIYfI7oRTBp9l5OlGXREBPonp9NdodMP
     * roomId : 5870
     * status : 1
     * type : 0
     * userAvatar : http://haiyan-news.oss-cn-beijing.aliyuncs.com/21054.png
     * userId : 1000005
     */

    private String amount;
    private String applyTime;
    private String broadcastUrl;
    private String id;
    private String nickName;
    private String publishUrl;
    private String roomId;
    private String status;
    private String type;
    private String userAvatar;
    private String userId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getBroadcastUrl() {
        return broadcastUrl;
    }

    public void setBroadcastUrl(String broadcastUrl) {
        this.broadcastUrl = broadcastUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPublishUrl() {
        return publishUrl;
    }

    public void setPublishUrl(String publishUrl) {
        this.publishUrl = publishUrl;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LianmaiBean{" +
                "amount='" + amount + '\'' +
                ", applyTime='" + applyTime + '\'' +
                ", broadcastUrl='" + broadcastUrl + '\'' +
                ", id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", publishUrl='" + publishUrl + '\'' +
                ", roomId='" + roomId + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
