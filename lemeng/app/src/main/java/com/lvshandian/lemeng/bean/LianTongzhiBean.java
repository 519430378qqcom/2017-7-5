package com.lvshandian.lemeng.bean;

import java.io.Serializable;

/**
 * Created by ssb on 2017/4/5 10:18.
 * company: lvshandian
 */

public class LianTongzhiBean implements Serializable {

    /**
     * content : 主播请求与您连麦
     * id : 2
     * result : 1
     * type : 2
     * userAvatar : http://haiyan-news.oss-cn-beijing.aliyuncs.com/29077.png
     * userId : 1000004
     * userName : 奔波儿灞
     * vip : 0
     */

    private String content;
    private String id;
    private String result;
    private String type;
    private String userAvatar;
    private String userId;
    private String userName;
    private String vip;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }
}
