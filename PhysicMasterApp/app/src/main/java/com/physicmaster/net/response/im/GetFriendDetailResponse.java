package com.physicmaster.net.response.im;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2016/11/24.
 */
public class GetFriendDetailResponse extends Response {

    public DataBean data;

    public static class DataBean {

        public UserDetailVoBean userDetailVo;

        public static class UserDetailVoBean {
            public static final int USER_SELF = -1;
            public static final int NO_RELATION = 0;
            public static final int FRIEND = 1;
            public static final int CONFIRMED = 2;
            public static final int BE_CONFIRM = 3;

            public int dtUserId;
            public String imUserId;
            // friendState = -1：发起接口请求的用户查看的是自己的信息；
            // friendState = 0：无任何关系；
            // friendState = 1：已是好友；只有此项，才可以通过IM发消息
            // friendState = 2：请同意：待自己同意（dtUserId向requestDtUserId发起了添加好友的请求，等待requestDtUserId同意）；
            // friendState = 3：待验证：待对方同意（requestDtUserId向dtUserId发起了添加好友的请求，等待dtUserId同意）；
            public int friendState;
            public int gender;
            public String intro;
            public String nickname;
            public String portrait;
            public String portraitLg;
            public UserStudyInfoBean userStudyInfo;
            public List<MedalListBean> medalList;
            public int userLevel;

            public static class UserStudyInfoBean {

                public String completeCourseCount;
                public String studyDays;
                public String studyTime;
            }

            public static class MedalListBean {

                public int medalId;
                public String medalImg;
                public String medalName;
            }
        }
    }
}
