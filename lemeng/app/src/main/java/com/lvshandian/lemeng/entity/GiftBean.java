package com.lvshandian.lemeng.entity;

/**
 * 礼物实体类
 * Created by Administrator on 2016/11/25.
 */

public class GiftBean {

    /**
     * id : 4
     * name : 点赞   名称
     * memberConsume : 80  所需金币会员消耗
     * anchorReceive : 32  主播得到
     * giftConsume : 48    礼物消耗
     * staticIcon : http://www.partylive.cn/tangren/goodjob20161221.png     礼物图片
     * currencyAmount : null
     * combo : 0        //是否連點
     * visible : 1     是否现实 1显示  0否则隐藏
     * createTime : 2016-11-30 16:32:11
     * winFlag : 0              中奖标志   //0不可中奖 1 可中奖 2 有特效
     * giftType : 0             道具类型
     * giftDesc : 给主播点赞     道具描述
     * sortNumber : 1
     */

    private String id;
    private String name;
    private String memberConsume;
    private String anchorReceive;
    private String giftConsume;
    private String staticIcon;
    private String currencyAmount;
    private String combo;
    private String visible;
    private String createTime;
    private String winFlag;
    private String giftType;
    private String giftDesc;
    private String sortNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberConsume() {
        return memberConsume;
    }

    public void setMemberConsume(String memberConsume) {
        this.memberConsume = memberConsume;
    }

    public String getAnchorReceive() {
        return anchorReceive;
    }

    public void setAnchorReceive(String anchorReceive) {
        this.anchorReceive = anchorReceive;
    }

    public String getGiftConsume() {
        return giftConsume;
    }

    public void setGiftConsume(String giftConsume) {
        this.giftConsume = giftConsume;
    }

    public String getStaticIcon() {
        return staticIcon;
    }

    public void setStaticIcon(String staticIcon) {
        this.staticIcon = staticIcon;
    }

    public String getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(String currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getWinFlag() {
        return winFlag;
    }

    public void setWinFlag(String winFlag) {
        this.winFlag = winFlag;
    }

    public String getGiftType() {
        return giftType;
    }

    public void setGiftType(String giftType) {
        this.giftType = giftType;
    }

    public String getGiftDesc() {
        return giftDesc;
    }

    public void setGiftDesc(String giftDesc) {
        this.giftDesc = giftDesc;
    }

    public String getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(String sortNumber) {
        this.sortNumber = sortNumber;
    }

    @Override
    public String toString() {
        return "GiftBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", memberConsume='" + memberConsume + '\'' +
                ", anchorReceive='" + anchorReceive + '\'' +
                ", giftConsume='" + giftConsume + '\'' +
                ", staticIcon='" + staticIcon + '\'' +
                ", currencyAmount='" + currencyAmount + '\'' +
                ", combo='" + combo + '\'' +
                ", visible='" + visible + '\'' +
                ", createTime='" + createTime + '\'' +
                ", winFlag='" + winFlag + '\'' +
                ", giftType='" + giftType + '\'' +
                ", giftDesc='" + giftDesc + '\'' +
                ", sortNumber='" + sortNumber + '\'' +
                '}';
    }
}
