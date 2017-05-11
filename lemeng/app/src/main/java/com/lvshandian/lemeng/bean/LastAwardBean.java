package com.lvshandian.lemeng.bean;

import java.io.Serializable;

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
}
