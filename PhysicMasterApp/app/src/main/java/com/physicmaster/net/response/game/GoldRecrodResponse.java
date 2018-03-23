package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GoldRecrodResponse extends Response {


    /**
     * data : {"nextPageMaxId":9,"transactionList":[{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"}]}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * nextPageMaxId : 9
         * transactionList : [{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"},{"coinValue":"+1","createTime":"2016-11-30","description":"购买道具：恢复1点精力值"}]
         */

        public int nextPageMaxId;
        public List<TransactionListBean> transactionList;

        public static class TransactionListBean {
            /**
             * coinValue : +1
             * createTime : 2016-11-30
             * description : 购买道具：恢复1点精力值
             */

            public String coinValue;
            public String createTime;
            public String description;
        }
    }
}
