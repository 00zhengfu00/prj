package com.physicmaster.net.response.discuss;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2016/11/24.
 */
public class QuestionListResponse extends Response implements Serializable{


    /**
     * data : {"nextModifySequence":102,"qaQuestionVos":[{"answerCount":0,"content":"啦啦啦啦 我是学霸哈哈哈","gradeSubject":"物理七年级","imgVo":{"h":312,"s":1111111,"u":"http://imgtest.thelper.cn/dt/u/4m06o/1494911521082%40672w_312h_95Q_1e_1c_2o.src","w":672},"qid":1217,"releaseTime":"今天 15:07","title":"test"}]}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * nextModifySequence : 102
         * qaQuestionVos : [{"answerCount":0,"content":"啦啦啦啦 我是学霸哈哈哈","gradeSubject":"物理七年级","imgVo":{"h":312,"s":1111111,"u":"http://imgtest.thelper.cn/dt/u/4m06o/1494911521082%40672w_312h_95Q_1e_1c_2o.src","w":672},"qid":1217,"releaseTime":"今天 15:07","title":"test"}]
         */

        public int newsCount;
        public int nextId;
        public List<QaQuestionVosBean> qaQuestionVos;

        public static class QaQuestionVosBean {
            /**
             * answerCount : 0
             * content : 啦啦啦啦 我是学霸哈哈哈
             * gradeSubject : 物理七年级
             * imgVo : {"h":312,"s":1111111,"u":"http://imgtest.thelper.cn/dt/u/4m06o/1494911521082%40672w_312h_95Q_1e_1c_2o.src","w":672}
             * qid : 1217
             * releaseTime : 今天 15:07
             * title : test
             */

            public String answerCount;
            public String    content;
            public String    gradeSubject;
            public ImgVoBean imgVo;
            public int       qid;
            public String    releaseTime;
            public String    title;

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
}
