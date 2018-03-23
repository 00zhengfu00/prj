package com.physicmaster.net.response.im;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2016/11/24.
 */
public class FindFriendV2Response extends Response {

    /**
     * data : {"nextId":1005,"nextRange":0,"userVoList":[{"intro":"八年级下","dtUserId":2455505,
     * "friendState":3,"gender":0,"nickname":"华仔","portrait":"http://img.thelper
     * .cn/dt/u/3z9qma/1486029750069.jpg%4050w_50h_100Q_1e_1c_2o.jpg","userLevel":1}]}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * nextId : 1005
         * nextRange : 0
         * userVoList : [{"intro":"八年级下","dtUserId":2455505,"friendState":3,"gender":0,
         * "nickname":"华仔","portrait":"http://img.thelper
         * .cn/dt/u/3z9qma/1486029750069.jpg%4050w_50h_100Q_1e_1c_2o.jpg","userLevel":1}]
         */

        public int nextId;
        public int nextRange;
        public List<UserVoListBean> userVoList;

        public static class UserVoListBean {
            /**
             * intro : 八年级下
             * dtUserId : 2455505
             * friendState : 3
             * gender : 0
             * nickname : 华仔
             * portrait : http://img.thelper.cn/dt/u/3z9qma/1486029750069.jpg%4050w_50h_100Q_1e_1c_2o.jpg
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
