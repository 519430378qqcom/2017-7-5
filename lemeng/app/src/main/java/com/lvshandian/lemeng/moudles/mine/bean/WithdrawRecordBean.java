package com.lvshandian.lemeng.moudles.mine.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ssb on 2017/6/24 15:59.
 * company: lvshandian
 */

public class WithdrawRecordBean implements Serializable {

    /**
     * total : 2
     * rows : [{"amount":53,"createdTime":"2017-06-20 18:52:49"},{"amount":23,"createdTime":"2017-06-20 18:51:49"}]
     * allPages : 1
     */

    private int total;
    private int allPages;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * amount : 53
         * createdTime : 2017-06-20 18:52:49
         */

        private int amount;
        private String createdTime;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }
    }
}
