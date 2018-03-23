package com.physicmaster.net.response.video;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2017-07-21.
 */

public class CheckAuthResponse extends Response {

    /**
     * data : {"canPlay":1,"rand":"123456789123456789123456789123456789"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * canPlay : 1
         * rand : 123456789123456789123456789123456789
         */

        public int canPlay;
        public String rand;
    }
}
