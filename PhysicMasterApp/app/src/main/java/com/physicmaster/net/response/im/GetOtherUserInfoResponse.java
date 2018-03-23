package com.physicmaster.net.response.im;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2017/5/18.
 */

public class GetOtherUserInfoResponse extends Response {

    public DataBean data;

    public static class DataBean {
        public UserDetailVoBean userVo;

        public static class UserDetailVoBean {
            public int dtUserId;
            public int friendState;
            public int gender;
            public String imUserId;
            public String intro;
            public String nickname;
            public String portrait;
            public int userLevel;
        }
    }
}
