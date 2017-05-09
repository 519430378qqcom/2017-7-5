package com.lvshandian.lemeng.bean;

import java.io.Serializable;

/**
 * 同意连麦自定义消息
 * Created by Administrator on 2017/3/20.
 */

public class CustomLianmaiBean implements Serializable {
    private String watch_private_flag;
    private String userId;
    private String type;
    private String vip;
    private String broadcastUrl;

    public String getWatch_private_flag() {
        return watch_private_flag;
    }

    public void setWatch_private_flag(String watch_private_flag) {
        this.watch_private_flag = watch_private_flag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getBroadcastUrl() {
        return broadcastUrl;
    }

    public void setBroadcastUrl(String broadcastUrl) {
        this.broadcastUrl = broadcastUrl;
    }

    @Override
    public String toString() {
        return "CustomLianmaiBean{" +
                "watch_private_flag='" + watch_private_flag + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", vip='" + vip + '\'' +
                ", broadcastUrl='" + broadcastUrl + '\'' +
                '}';
    }
}
