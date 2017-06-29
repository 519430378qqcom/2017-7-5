package com.lvshandian.lemeng.moudles.mine.bean;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/29
 * version: 1.0
 * desc   :
 */
public class BankCardInfo {
    private String cardName;
    private String cardType;
    private String cardNum;

    public BankCardInfo() {
    }

    public BankCardInfo(String cardName, String cardType, String cardNum) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.cardNum = cardNum;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}
