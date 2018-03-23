package com.physicmaster.net.response.user;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2016/11/21.
 */
public class GetStudyInfoResponse extends Response{


    /**
     * data : {"userStudyInfo":{"completeCourseCount":"3章","studyDays":"0天","studyTime":"02小时04分"}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * userStudyInfo : {"completeCourseCount":"3章","studyDays":"0天","studyTime":"02小时04分"}
         */

        public UserStudyInfoBean userStudyInfo;

        public static class UserStudyInfoBean {
            /**
             * completeCourseCount : 3章
             * studyDays : 0天
             * studyTime : 02小时04分
             */

            public String completeCourseCount;
            public String studyDays;
            public String studyTime;
        }
    }
}
