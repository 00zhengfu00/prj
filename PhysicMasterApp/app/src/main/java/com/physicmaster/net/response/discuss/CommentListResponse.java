package com.physicmaster.net.response.discuss;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2016/11/24.
 */
public class CommentListResponse extends Response implements Serializable{


    /**
     * data : {"nextCommentId":3163,"commentList":[{"commentId":3168,"content":"kalalwfwfa","nickname":"强子","status":1,"portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyNickname":"levy","replyTime":"今天 16:45","replyPortrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg"},{"commentId":3163,"content":"哈哈哈哈","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:41"},{"commentId":3164,"content":"该条评论以被删除","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:42"},{"commentId":3165,"content":"gegegege","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:44"},{"commentId":3166,"content":"hahahah","nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:45"},{"commentId":3167,"content":"kalalwfwfa","nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:45"},{"commentId":3169,"content":"哈哈娃哈哈  回复你了","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyNickname":"levy","replyPortrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"刚刚"}],"currentComment":{"commentId":3168,"content":"kalalwfwfa","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%4080w_80h_100Q_1e_1c_2o.jpg","replyNickname":"levy","replyTime":"05月11日 16:45","status":1}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * nextCommentId : 3163
         * commentList : [{"commentId":3168,"content":"kalalwfwfa","nickname":"强子","status":1,"portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyNickname":"levy","replyTime":"今天 16:45"},{"commentId":3163,"content":"哈哈哈哈","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:41"},{"commentId":3164,"content":"该条评论以被删除","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:42"},{"commentId":3165,"content":"gegegege","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:44"},{"commentId":3166,"content":"hahahah","nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:45"},{"commentId":3167,"content":"kalalwfwfa","nickname":"levy","portrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"今天 16:45"},{"commentId":3169,"content":"哈哈娃哈哈  回复你了","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyNickname":"levy","replyPortrait":"http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg","replyTime":"刚刚"}]
         * currentComment : {"commentId":3168,"content":"kalalwfwfa","nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%4080w_80h_100Q_1e_1c_2o.jpg","replyNickname":"levy","replyTime":"05月11日 16:45","status":1}
         */

        public int nextCommentId;
        public CommentListBean    currentComment;
        public List<CommentListBean> commentList;


        public static class CommentListBean {
            /**
             * commentId : 3168
             * content : kalalwfwfa
             * nickname : 强子
             * status : 1
             * portrait : http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg
             * replyNickname : levy
             * replyTime : 今天 16:45
             * replyPortrait : http://img.thelper.cn/dt/u/23x4/1479383510079.jpg%40120w_120h_100Q_1e_1c_2o.jpg
             */

            public int dtUserId;
            public int commentId;
            public int canRemove;
            public String content;
            public String nickname;
            public int    status;
            public String portrait;
            public String replyNickname;
            public String replyTime;
            public String replyPortrait;

            public CommentListBean(){}

            public CommentListBean(int commentId, String content, String nickname, int status, String portrait, String replyNickname, String replyTime, String replyPortrait) {
                this.commentId = commentId;
                this.content = content;
                this.nickname = nickname;
                this.status = status;
                this.portrait = portrait;
                this.replyNickname = replyNickname;
                this.replyTime = replyTime;
                this.replyPortrait = replyPortrait;
            }
        }
    }
}
