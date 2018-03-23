package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

import java.util.List;

public class FreshResponse extends Response {


    /**
     * data : {"freshNewList":[{"createTime":"2016-12-14 13:26:53","dtUserId":170433,"id":6,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","title":"完成了全对闯关10次任务"},{"createTime":"2016-12-14 13:25:13","dtUserId":170433,"id":5,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","title":"3星闯关"},{"createTime":"2016-12-14 10:30:30","dtUserId":170433,"id":3,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","title":"完成了浮力的学习"},{"createTime":"2016-12-14 10:30:23","dtUserId":170433,"id":2,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","title":"完成了压强的学习"},{"createTime":"2016-12-14 10:30:02","dtUserId":170432,"id":1,"nickname":"哈哈","portrait":"","title":"完成了运动和力的学习"}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<FreshNewListBean> freshNewList;

        public static class FreshNewListBean {
            /**
             * createTime : 2016-12-14 13:26:53
             * dtUserId : 170433
             * id : 6
             * nickname : ryy
             * portrait : http://imgtest.thelper.cn/i/170433/1480756342.jpg
             * title : 完成了全对闯关10次任务
             */

            public String createTime;
            public int    dtUserId;
            public int    newsid;
            public String nickname;
            public String portrait;
            public String title;
        }
    }
}
