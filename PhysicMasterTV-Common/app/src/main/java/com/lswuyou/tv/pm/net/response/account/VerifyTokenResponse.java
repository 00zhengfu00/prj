package com.lswuyou.tv.pm.net.response.account;

import com.lswuyou.tv.pm.net.response.Response;

/**
 * Created by huashigen on 2017-09-11.
 */

public class VerifyTokenResponse extends Response {

    /**
     * data : {"verifyTokenResultVo":{"status":0}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * verifyTokenResultVo : {"status":0}
         */

        public VerifyTokenResultVoBean verifyTokenResultVo;
        public static class VerifyTokenResultVoBean {
            /**
             * status : 0
             */

            public int status;
            public LoginUserInfo loginInfo;
        }
    }
}
