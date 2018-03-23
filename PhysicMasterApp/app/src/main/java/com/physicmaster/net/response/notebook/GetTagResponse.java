package com.physicmaster.net.response.notebook;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2017-12-22.
 */

public class GetTagResponse extends Response {

    /**
     * data : {"poolCount":62,"dirList":[{"name":"自然界的水","dirId":10007,"co":0}]}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * poolCount : 62
         * dirList : [{"name":"自然界的水","dirId":10007,"co":0}]
         */

        public int poolCount;
        public List<DirListBean> dirList;

        public static class DirListBean {
            /**
             * name : 自然界的水
             * dirId : 10007
             * co : 0
             */

            public String name;
            public int dirId;
            public int co;
            public boolean rmEnable;
        }
    }
}
