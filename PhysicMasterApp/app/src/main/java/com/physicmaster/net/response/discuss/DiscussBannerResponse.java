package com.physicmaster.net.response.discuss;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2017/5/16.
 */

public class DiscussBannerResponse extends Response {

    public DataBean data;

    public static class DataBean {
        public int newsCount;
        public List<QaQuestionVosBean> qaQuestionVos;

        public static class QaQuestionVosBean {
            public String answerCount;
            public String content;
            public String gradeSubject;
            public ImgVoBean imgVo;
            public int qid;
            public String releaseTime;
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
