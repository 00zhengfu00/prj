package com.physicmaster.net.response.discuss;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2016/11/24.
 */
public class AnswerListResponse extends Response implements Serializable{


    /**
     * data : {"answerList":[{"answerId":2615,"commentCount":0,"content":"啦啦啦","createTimeMs":"今天 16:09","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"qid":1217,"title":"test"},{"answerId":2613,"commentCount":0,"content":"hahahah哈哈我","createTimeMs":"今天 16:08","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"qid":1217,"title":"test"},{"answerId":2612,"commentCount":0,"content":"啊啊啊啊啊啊啊啊啊啊啊哇打我费","createTimeMs":"今天 16:08","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"qid":1217,"title":"test"}],"nextAnswerId":2612}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * answerList : [{"answerId":2615,"commentCount":0,"content":"啦啦啦","createTimeMs":"今天 16:09","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"qid":1217,"title":"test"},{"answerId":2613,"commentCount":0,"content":"hahahah哈哈我","createTimeMs":"今天 16:08","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"qid":1217,"title":"test"},{"answerId":2612,"commentCount":0,"content":"啊啊啊啊啊啊啊啊啊啊啊哇打我费","createTimeMs":"今天 16:08","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"qid":1217,"title":"test"}]
         * nextAnswerId : 2612
         */

        public int nextAnswerId;
        public List<AnswerListBean> answerList;

        public static class AnswerListBean implements Serializable{
            /**
             * answerId : 2615
             * commentCount : 0
             * content : 啦啦啦
             * createTimeMs : 今天 16:09
             * imgUrl : http://img.thelper.cn/app/help_pay/2.jpg
             * likeCount : 0
             * qid : 1217
             * title : test
             */

            public int status;
            public int answerId;
            public String    commentCount;
            public String content;
            public String createTimeMs;
            public ImgVoBean imgVo;
            public String    likeCount;
            public int    qid;
            public String title;
        }
        public static class ImgVoBean {
            /**
             * h : 312
             * s : 1111111
             * u : http://imgtest.thelper.cn/dt/u/4m06o/1494911521082%40672w_312h_95Q_1e_1c_2o.src
             * w : 672
             */

            public int h;
            public int    s;
            public String u;
            public int    w;
        }
    }
}
