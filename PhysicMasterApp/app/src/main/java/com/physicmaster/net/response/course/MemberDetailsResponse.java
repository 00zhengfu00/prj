package com.physicmaster.net.response.course;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2016/11/21.
 */
public class MemberDetailsResponse extends Response{


    /**
     * data : {"orderDetail":{"expiryDate":"2029-07-23","giftItemList":[{"giftItemId":1,"imgUrl":"http://img.thelper.cn/dt/w/1009/1009.jpg%40192w_256h_90Q_1e_1c_2o.jpg","title":"赠送1"},{"giftItemId":2,"imgUrl":"http://img.thelper.cn/dt/w/1009/1009.jpg%40192w_256h_90Q_1e_1c_2o.jpg","title":"赠送2"}],"memberItemId":113,"priceDesc":"￥: 0元/12个月","title":"初中物理年会员"}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * orderDetail : {"expiryDate":"2029-07-23","giftItemList":[{"giftItemId":1,"imgUrl":"http://img.thelper.cn/dt/w/1009/1009.jpg%40192w_256h_90Q_1e_1c_2o.jpg","title":"赠送1"},{"giftItemId":2,"imgUrl":"http://img.thelper.cn/dt/w/1009/1009.jpg%40192w_256h_90Q_1e_1c_2o.jpg","title":"赠送2"}],"memberItemId":113,"priceDesc":"￥: 0元/12个月","title":"初中物理年会员"}
         */

        public OrderDetailBean orderDetail;

        public static class OrderDetailBean {
            /**
             * expiryDate : 2029-07-23
             * giftItemList : [{"giftItemId":1,"imgUrl":"http://img.thelper.cn/dt/w/1009/1009.jpg%40192w_256h_90Q_1e_1c_2o.jpg","title":"赠送1"},{"giftItemId":2,"imgUrl":"http://img.thelper.cn/dt/w/1009/1009.jpg%40192w_256h_90Q_1e_1c_2o.jpg","title":"赠送2"}]
             * memberItemId : 113
             * priceDesc : ￥: 0元/12个月
             * title : 初中物理年会员
             */

            public String expiryDate;
            public int                    memberItemId;
            public String                 priceDesc;
            public String                 title;
            public List<GiftItemListBean> giftItemList;

            public static class GiftItemListBean {
                /**
                 * giftItemId : 1
                 * imgUrl : http://img.thelper.cn/dt/w/1009/1009.jpg%40192w_256h_90Q_1e_1c_2o.jpg
                 * title : 赠送1
                 */

                public int giftItemId;
                public String imgUrl;
                public String title;
            }
        }
    }
}
