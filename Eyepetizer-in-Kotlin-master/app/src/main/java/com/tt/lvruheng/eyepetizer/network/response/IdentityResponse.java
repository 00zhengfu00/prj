package com.tt.lvruheng.eyepetizer.network.response;

import com.tt.lvruheng.eyepetizer.network.Response;

/**
 * Created by huashigen on 2018-03-01.
 */

public class IdentityResponse extends Response {

    public DataBean data;

    public static class DataBean {
        public String name;
        public String idNo;
    }
}
