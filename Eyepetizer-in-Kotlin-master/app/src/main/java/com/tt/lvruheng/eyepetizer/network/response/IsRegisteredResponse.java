package com.tt.lvruheng.eyepetizer.network.response;

import com.tt.lvruheng.eyepetizer.network.Response;

/**
 * Created by huashigen on 2018-01-31.
 */

public class IsRegisteredResponse extends Response {
    public DataBean data;
    public static class DataBean{
        public String registered;
    }
}
