package com.lvshandian.lemeng.entity;

import java.io.Serializable;

/**
 * Created by ssb on 2017/6/29 13:24.
 * company: lvshandian
 */

public class VersionUpdateBean implements Serializable{

    /**
     * id : 1
     * versionCode : 14
     * forceUpdate :    1强制更新  0不强制更新
     * updateContent : 1.迭代数据
     * updateUrl : http://sad/asdaf
     */

    private int id;
    private int versionCode;
    private String forceUpdate;
    private String updateContent;
    private String updateUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
