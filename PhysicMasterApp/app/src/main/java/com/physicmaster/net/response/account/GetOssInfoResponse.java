package com.physicmaster.net.response.account;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2018-01-25.
 */

public class GetOssInfoResponse extends Response {

    public DataBean data;

    public static class DataBean {

        public OssConfigBean ossConfig;
        public OssTokenBean ossToken;

        public static class OssConfigBean {
            public String hostId;
            public String bucketName;
            public String videoPath;
            public String imgPath;
        }

        public static class OssTokenBean {
            public int expiration;
            public String securityToken;
            public String tempAk;
            public String tempSk;
        }
    }
}
