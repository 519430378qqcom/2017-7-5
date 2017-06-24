package com.lvshandian.lemeng.moudles.mine.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class FloatingRecordBean {


    /**
     * code : 0
     * page : 1
     * PageCount : 12
     * data : [{"amount":"-10","refreshTimes":"2017-06-21 17:03:39","state":"乐檬牛牛","userId":"100018"},{"amount":"-10","refreshTimes":"2017-06-21 17:03:37","state":"乐檬牛牛","userId":"100018"},{"amount":"-10","refreshTimes":"2017-06-21 17:03:36","state":"乐檬牛牛","userId":"100018"},{"amount":"-10000","refreshTimes":"2017-06-21 15:59:52","state":"乐檬牛牛","userId":"100018"},{"amount":"-10000","refreshTimes":"2017-06-21 15:59:51","state":"乐檬牛牛","userId":"100018"},{"amount":"-10000","refreshTimes":"2017-06-21 15:59:50","state":"乐檬牛牛","userId":"100018"},{"amount":"10","refreshTimes":"2017-06-21 15:24:15","state":"乐檬牛牛","userId":"100018"},{"amount":"10","refreshTimes":"2017-06-21 15:24:14","state":"乐檬牛牛","userId":"100018"},{"amount":"10","refreshTimes":"2017-06-21 15:24:13","state":"乐檬牛牛","userId":"100018"},{"amount":"-10","refreshTimes":"2017-06-21 15:24:13","state":"乐檬牛牛","userId":"100018"}]
     * inputMoney : 263470
     * incomeMoney : -27709
     */

    private int code;
    private int page;
    private int PageCount;
    private int inputMoney;
    private int incomeMoney;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int PageCount) {
        this.PageCount = PageCount;
    }

    public int getInputMoney() {
        return inputMoney;
    }

    public void setInputMoney(int inputMoney) {
        this.inputMoney = inputMoney;
    }

    public int getIncomeMoney() {
        return incomeMoney;
    }

    public void setIncomeMoney(int incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * amount : -10
         * refreshTimes : 2017-06-21 17:03:39
         * state : 乐檬牛牛
         * userId : 100018
         */

        private String amount;
        private String refreshTimes;
        private String state;
        private String userId;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRefreshTimes() {
            return refreshTimes;
        }

        public void setRefreshTimes(String refreshTimes) {
            this.refreshTimes = refreshTimes;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
