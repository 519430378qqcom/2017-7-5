package com.lvshandian.lemeng.entity;

/**
 * Created by ssb on 2017/7/4 17:20.
 * company: lvshandian
 * 控制音乐开始暂停、歌词显示
 */

public class PlayerStatus {
    private String isPlayer; // 1播放  2暂停
    private String isShowLrc;  // 1显示  2隐藏
    private String lrc;  //歌词

    public String getIsShowLrc() {
        return isShowLrc;
    }

    public void setIsShowLrc(String isShowLrc) {
        this.isShowLrc = isShowLrc;
    }

    public String getIsPlayer() {
        return isPlayer;
    }

    public void setIsPlayer(String isPlayer) {
        this.isPlayer = isPlayer;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    @Override
    public String toString() {
        return "PlayerStatus{" +
                "isPlayer='" + isPlayer + '\'' +
                ", isShowLrc='" + isShowLrc + '\'' +
                ", lrc='" + lrc + '\'' +
                '}';
    }
}
