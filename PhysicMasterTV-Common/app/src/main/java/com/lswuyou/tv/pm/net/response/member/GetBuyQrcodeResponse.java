package com.lswuyou.tv.pm.net.response.member;

import com.lswuyou.tv.pm.net.response.Response;

/**
 * Created by huashigen on 2017-09-08.
 */

public class GetBuyQrcodeResponse extends Response {

    /**
     * data : {"cfg":{"expireSeconds":300,"token":"Klt8Kqb20ylsCqPjeQml45WFPdxBfym0"}}
     */
    public DataBean data;

    public static class DataBean {
        /**
         * cfg : {"expireSeconds":300,"token":"Klt8Kqb20ylsCqPjeQml45WFPdxBfym0"}
         */
        public CfgBean cfg;

        public static class CfgBean {
            /**
             * expireSeconds : 300
             * token : Klt8Kqb20ylsCqPjeQml45WFPdxBfym0
             */
            public int expireSeconds;
            public String token;
        }
    }
}
