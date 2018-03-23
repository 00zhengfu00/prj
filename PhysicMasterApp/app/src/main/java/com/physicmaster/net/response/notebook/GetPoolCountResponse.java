package com.physicmaster.net.response.notebook;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2018-01-22.
 */

public class GetPoolCountResponse extends Response {


    /**
     * data : {"s_1":50,"s_2":0,"s_4":0}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * s_1 : 50
         * s_2 : 0
         * s_4 : 0
         */

        public int s_1;
        public int s_2;
        public int s_4;
    }
}
