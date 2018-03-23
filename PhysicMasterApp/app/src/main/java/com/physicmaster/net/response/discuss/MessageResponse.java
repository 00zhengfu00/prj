package com.physicmaster.net.response.discuss;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2016/11/24.
 */
public class MessageResponse extends Response implements Serializable{


    /**
     * data : {"newsList":[{"comment":"巴巴啦啦啦","describe":"评论了我的回答","newsId":4,"newsTime":"今天 14:31","newsType":2,"nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","relatedId":3172,"relatedId2":2619},{"describe":"回答了我的问题","newsId":3,"newsTime":"今天 14:25","newsType":0,"nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","relatedId":1217,"relatedId2":2618},{"describe":"回答了我的问题","newsId":2,"newsTime":"今天 14:25","newsType":0,"nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","relatedId":1217,"relatedId2":2617},{"describe":"回答了我的问题","newsId":1,"newsTime":"今天 14:25","newsType":0,"nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","relatedId":1217,"relatedId2":2616}],"nextNewsId":1}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * newsList : [{"comment":"巴巴啦啦啦","describe":"评论了我的回答","newsId":4,"newsTime":"今天 14:31","newsType":2,"nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","relatedId":3172,"relatedId2":2619},{"describe":"回答了我的问题","newsId":3,"newsTime":"今天 14:25","newsType":0,"nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","relatedId":1217,"relatedId2":2618},{"describe":"回答了我的问题","newsId":2,"newsTime":"今天 14:25","newsType":0,"nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","relatedId":1217,"relatedId2":2617},{"describe":"回答了我的问题","newsId":1,"newsTime":"今天 14:25","newsType":0,"nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","relatedId":1217,"relatedId2":2616}]
         * nextNewsId : 1
         */

        public int nextNewsId;
        public List<NewsListBean> newsList;

        public static class NewsListBean {
            /**
             * comment : 巴巴啦啦啦
             * describe : 评论了我的回答
             * newsId : 4
             * newsTime : 今天 14:31
             * newsType : 2
             * nickname : levy
             * portrait : http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg
             * relatedId : 3172
             * relatedId2 : 2619
             */

            public int    commentRemoved;
            public int    relatedStatus;
            public String relatedErr;
            public String comment;
            public String describe;
            public int    newsId;
            public String newsTime;
            public int    newsType;
            public String nickname;
            public String portrait;
            public int    relatedId;
            public int    relatedId2;
        }
    }
}
