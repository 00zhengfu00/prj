package com.physicmaster.net.response.notebook;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GetWrongWhyResponse extends Response {


    /**
     * data : {"quWrongTags":[{"name":"测试数据6","tagId":7},{"name":"测试数据7","tagId":8},{"name":"测试数据8","tagId":9},{"name":"点错了","tagId":11},{"name":"5555","tagId":13},{"name":"1","tagId":16},{"name":"2","tagId":17},{"name":"粗心大意","tagId":18}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<QuWrongTagsBean> quWrongTags;

        public static class QuWrongTagsBean {
            /**
             * name : 测试数据6
             * tagId : 7
             */

            public String name;
            public int tagId;
        }
    }
}
