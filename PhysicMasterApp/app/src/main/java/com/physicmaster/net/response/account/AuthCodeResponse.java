package com.physicmaster.net.response.account;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2017/2/22.
 */

public class AuthCodeResponse extends Response {

    /**
     * data : {"authCode":"ywLYfLKlv9fPDoqS8qHjSFElZnjPMXS0"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * authCode : ywLYfLKlv9fPDoqS8qHjSFElZnjPMXS0
         */

        public String authCode;
    }
}
