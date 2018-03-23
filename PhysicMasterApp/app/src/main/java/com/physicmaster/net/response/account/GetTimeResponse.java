package com.physicmaster.net.response.account;


import com.physicmaster.net.response.Response;

public class GetTimeResponse extends Response {

    public DataBean data;

    public class DataBean {
        public long currentTimeMillis; // 服务器的UNIX毫秒时间戳，类型为long/int64
        public int sync;// 客户端是否需要启用时间同步机制
    }
}
