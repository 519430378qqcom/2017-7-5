package com.lvshandian.lemeng.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ssb on 2017/5/11 17:00.
 * company: lvshandian
 */

public class LastAwardBean implements Serializable {

    /**
     * number : 07,18,29,30,31,32,34,46,48,49,50,53,54,56,62,69,72,74,79,80,02
     * dateLine : 2017-05-11 11:00:21
     * secondNum : 0
     * sum : 14
     * nper : 822610
     * type : 大,双
     * thirdNum : 7
     * firstNum : 7
     */

    private String number;
    private String dateLine;
    private int secondNum;
    private int sum;
    private String nper;
    private String type;
    private int thirdNum;
    private int firstNum;
    private String winAmountAll; //中奖金额
    private String winStatus;//1中奖 0未中奖  2未投注
    private List<RoomRanksBean> roomRanks;

    public String getWinAmountAll() {
        return winAmountAll;
    }

    public void setWinAmountAll(String winAmountAll) {
        this.winAmountAll = winAmountAll;
    }

    public String getWinStatus() {
        return winStatus;
    }

    public void setWinStatus(String winStatus) {
        this.winStatus = winStatus;
    }

    public String getCountryType() {
        return countryType;
    }

    public void setCountryType(String countryType) {
        this.countryType = countryType;
    }

    private String countryType;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDateLine() {
        return dateLine;
    }

    public void setDateLine(String dateLine) {
        this.dateLine = dateLine;
    }

    public int getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(int secondNum) {
        this.secondNum = secondNum;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getNper() {
        return nper;
    }

    public void setNper(String nper) {
        this.nper = nper;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getThirdNum() {
        return thirdNum;
    }

    public void setThirdNum(int thirdNum) {
        this.thirdNum = thirdNum;
    }

    public int getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(int firstNum) {
        this.firstNum = firstNum;
    }

    public List<RoomRanksBean> getRoomRanks() {
        return roomRanks;
    }

    public void setRoomRanks(List<RoomRanksBean> roomRanks) {
        this.roomRanks = roomRanks;
    }

    public static class RoomRanksBean implements Serializable {
        private String nickName;
        private String picurl;
        private String amount;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
