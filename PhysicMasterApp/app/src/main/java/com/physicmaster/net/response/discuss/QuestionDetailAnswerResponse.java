package com.physicmaster.net.response.discuss;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2016/11/24.
 */
public class QuestionDetailAnswerResponse extends Response implements Serializable {


    /**
     * data : {"answerList":[{"answerId":2615,"answerTime":"刚刚","commentCount":0,"content":"啦啦啦","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg"},{"answerId":2614,"answerTime":"刚刚","commentCount":0,"content":"爱上无法爱无法服务","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg"},{"answerId":2613,"answerTime":"刚刚","commentCount":0,"content":"hahahah哈哈我","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg"},{"answerId":2612,"answerTime":"刚刚","commentCount":0,"content":"啊啊啊啊啊啊啊啊啊啊啊哇打我费","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg"}],"nextPageNo":1}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * answerList : [{"answerId":2615,"answerTime":"刚刚","commentCount":0,"content":"啦啦啦","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg"},{"answerId":2614,"answerTime":"刚刚","commentCount":0,"content":"爱上无法爱无法服务","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg"},{"answerId":2613,"answerTime":"刚刚","commentCount":0,"content":"hahahah哈哈我","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg"},{"answerId":2612,"answerTime":"刚刚","commentCount":0,"content":"啊啊啊啊啊啊啊啊啊啊啊哇打我费","imgUrl":"http://img.thelper.cn/app/help_pay/2.jpg","likeCount":0,"portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg"}]
         * nextPageNo : 1
         */

        public int                  nextPageNo;
        public List<AnswerListBean> answerList;

        public static class AnswerListBean {
            /**
             * answerId : 2615
             * answerTime : 刚刚
             * commentCount : 0
             * content : 啦啦啦
             * imgUrl : http://img.thelper.cn/app/help_pay/2.jpg
             * likeCount : 0
             * portrait : http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg
             */

            public int       answerId;
            public int       status;
            public String    answerTime;
            public String       commentCount;
            public String    content;
            public ImgVoBean imgVo;
            public String       likeCount;
            public int       isLike;
            public String    portrait;
            public String    nickname;

            public  AnswerListBean(){}

            public AnswerListBean(int answerId, int status, String answerTime, String commentCount, String content, ImgVoBean imgVo, String likeCount, int isLike, String portrait, String nickname) {
                this.answerId = answerId;
                this.status = status;
                this.answerTime = answerTime;
                this.commentCount = commentCount;
                this.content = content;
                this.imgVo = imgVo;
                this.likeCount = likeCount;
                this.isLike = isLike;
                this.portrait = portrait;
                this.nickname = nickname;
            }

            public boolean isLike() {
                return isLike==1;
            }

            public void setLike(int isLike) {
                this.isLike = isLike;
            }

            public String getLikeNum() {
                return likeCount;
            }

            public void setLikeNum(String likeCount) {
                this.likeCount = likeCount;
            }
        }

        public static class ImgVoBean {
            /**
             * h : 312
             * s : 1111111
             * u : http://imgtest.thelper.cn/dt/u/4m06o/1494911521082%40672w_312h_95Q_1e_1c_2o.src
             * w : 672
             */

            public int    h;
            public int    s;
            public String u;
            public int    w;
        }

    }
}
