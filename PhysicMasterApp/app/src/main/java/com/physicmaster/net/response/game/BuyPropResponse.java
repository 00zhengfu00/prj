package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

public class BuyPropResponse extends Response {


    /**
     * data : {"goldValue":"51429　　　"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * goldValue : 51429　　　
         */

        public String goldValue;
    }
}
