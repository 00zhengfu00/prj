package com.physicmaster.net.response.notebook;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2018-01-06.
 */

public class GetPoolListRespose extends Response {
    public DataBean data;

    public static class DataBean {
        public int next;
        public String listJsonStr;
        public int subjectId;
        public int totalCount;
    }
}
