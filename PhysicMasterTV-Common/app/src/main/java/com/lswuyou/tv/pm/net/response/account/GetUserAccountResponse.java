package com.lswuyou.tv.pm.net.response.account;

import com.lswuyou.tv.pm.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class GetUserAccountResponse extends Response {

    public DataBean data;

    public static class DataBean implements Serializable{
        public int favCount;
        public LoginUserInfo userInfo;
        public int memberCount;
        public List<HistoryOrdersBean> historyOrders;

        public static class HistoryOrdersBean implements Serializable{
            public String buyTime;
            public String orderId;
            public String payType;
            public String priceYuan;
            public String status;
            public String title;
        }
    }
//    public UserAccountInfo data;
}
