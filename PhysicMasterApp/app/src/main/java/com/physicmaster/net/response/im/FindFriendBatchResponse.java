package com.physicmaster.net.response.im;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2017/5/16.
 */

public class FindFriendBatchResponse extends Response {

    /**
     * data : {"userVoList":[{"intro":"[手机联系人] 李四","dtUserId":2557801,"friendState":0,
     * "gender":0,"nickname":"用户_mwbJP5","portrait":"http://img.thelper
     * .cn/icon/portrait/3.png%4050w_50h_100Q_1e_1c_2o.png","userLevel":1},{"intro":"[手机联系人] 张三",
     * "dtUserId":1001,"friendState":0,"gender":1,"nickname":"强子","portrait":"http://img.thelper
     * .cn/dt/u/23oy/1488593780054.jpg%4050w_50h_100Q_1e_1c_2o.jpg","userLevel":8}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<UserVoListBean> userVoList;

        public static class UserVoListBean {
            /**
             * intro : [手机联系人] 李四
             * dtUserId : 2557801
             * friendState : 0
             * gender : 0
             * nickname : 用户_mwbJP5
             * portrait : http://img.thelper.cn/icon/portrait/3.png%4050w_50h_100Q_1e_1c_2o.png
             * userLevel : 1
             */

            public String intro;
            public int dtUserId;
            public int friendState;
            public int gender;
            public String nickname;
            public String portrait;
            public int userLevel;
            public boolean added;
        }
    }
}
