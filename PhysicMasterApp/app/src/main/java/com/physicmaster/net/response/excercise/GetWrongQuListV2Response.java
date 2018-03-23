package com.physicmaster.net.response.excercise;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2017/4/18.
 */

public class GetWrongQuListV2Response extends Response {


    public DataBean data;

    public static class DataBean {
        public String replacement1;
        public String replacement2;
    }
}
