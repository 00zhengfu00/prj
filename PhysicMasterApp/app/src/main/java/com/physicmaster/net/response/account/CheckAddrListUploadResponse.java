package com.physicmaster.net.response.account;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2017/5/26.
 */

public class CheckAddrListUploadResponse extends Response {

    /**
     * data : {"isUpload":1}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * isUpload : 1
         */

        public int isUpload;
    }
}
