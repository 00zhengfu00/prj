package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

import java.util.List;

public class FreeEnergyResponse extends Response {

    /**
     * data : {"appUserFriendInfoVoList":[{"decs":"爱迪外国语学校-初二下","dtUserId":170423,"nickname":"路飞","point":0,"portraitSmall":"http://imgtest.thelper.cn/i/170423/1480589747556%40120w_120h_100Q_1e_1c_2o.src","schoolName":"爱迪外国语学校","userLevel":0}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<AppUserFriendInfoVoListBean> appUserFriendInfoVoList;

        public static class AppUserFriendInfoVoListBean {
            /**
             * decs : 爱迪外国语学校-初二下
             * dtUserId : 170423
             * nickname : 路飞
             * point : 0
             * portraitSmall : http://imgtest.thelper.cn/i/170423/1480589747556%40120w_120h_100Q_1e_1c_2o.src
             * schoolName : 爱迪外国语学校
             * userLevel : 0
             */

            public String decs;
            public int    dtUserId;
            public String nickname;
            public int    point;
            public String portraitSmall;
            public String schoolName;
            public int    userLevel;
        }
    }
}
