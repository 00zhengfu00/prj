package com.physicmaster.net.response.excercise;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GetMemoResponse extends Response {

    /**
     * data : {"userMemoList":[{"content":"通过电阻R的电流强度为I时，在t时间内产生的热量为Q，若电阻为2R，电流强度为I/2，则在时间t内产生的热量为（  ） ","createTime":"1970-06-18","knowledgeName":"长度的测量","memoId":"214121241asfaf","quId":210903}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<UserMemoListBean> userMemoList;
        public int nextPageMemoId;

        public static class UserMemoListBean {
            /**
             * content : 通过电阻R的电流强度为I时，在t时间内产生的热量为Q，若电阻为2R，电流强度为I/2，则在时间t内产生的热量为（  ）
             * createTime : 1970-06-18
             * knowledgeName : 长度的测量
             * memoId : 214121241asfaf
             * quId : 210903
             */

            public String content;
            public String createTime;
            public String knowledgeName;
            public String memoId;
            public int    quId;
        }
    }
}
