package com.physicmaster.net.response.course;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2016/11/21.
 */
public class MemberOrderResponse extends Response{


    /**
     * data : {"orderId":"8173851719270098"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * orderId : 8173851719270098
         */

        public String orderId;
    }
}
