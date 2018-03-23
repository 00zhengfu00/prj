package com.physicmaster.net.response.user;


import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by songrui on 16/11/16.
 */

public class UserDataResponse extends Response {


    /**
     * nonce : 8FzyYU9XYAtyLj
     * data : {"keyVo":{"userKey":"4a952d43-99f7-4b65-a619-00d5c33e3ec6","userSecret":"R3hYdBY6AZk48OLj"},"loginVo":{"cellphone":"13525362536","createTime":"2016-11-17 17:58:00","eduGrade":0,"gender":0,"isInitial":0,"isSetPassword":1,"nickname":"用户_pbHuew","portrait":"","portraitSmall":""}}
     */

    public String       nonce;
    public UserDataBean data;


    public static class UserDataBean implements Serializable {
        /**
         * keyVo : {"userKey":"4a952d43-99f7-4b65-a619-00d5c33e3ec6","userSecret":"R3hYdBY6AZk48OLj"}
         * loginVo : {"cellphone":"13525362536","createTime":"2016-11-17 17:58:00","eduGrade":0,"gender":0,"isInitial":0,"isSetPassword":1,"nickname":"用户_pbHuew","portrait":"","portraitSmall":""}
         */

        public KeyVoBean   keyVo;
        public LoginVoBean loginVo;
        /**
         * loginVo2 : {"areaId":330108,"birthday":"2005-01-25","cellphone":"18605880127","cityId":330100,"createTime":"2017-01-05 16:23:26","dtUserId":1001,"eduGrade":1,"friendInviteCode":"S2H606","gender":1,"iapBalance":23100,"imToken":"h2gha229mi59cxmm0jlvrnvmln67ihtuowwyjpn6xovjn0bywiwlaj29h93","imUserId":"test@kqj3og@test","inviteCode":"Z5F41Z","isImAutoConnect":1,"isInitial":1,"isSetPassword":1,"isTourist":0,"nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg","portraitSmall":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","provinceId":330000,"schoolId":32449,"schoolName":"杭州二中滨江校区","studyEditionId":4,"studySubjectId":4,"userBinds":[{"bindType":4,"tpNickname":"186****0127"},{"bindType":0,"tpNickname":"强子"}]}
         */


        public static class KeyVoBean implements Serializable {
            /**
             * userKey : 4a952d43-99f7-4b65-a619-00d5c33e3ec6
             * userSecret : R3hYdBY6AZk48OLj
             */

            public String userKey;
            public String userSecret;

        }



        public static class LoginVoBean implements Serializable {
            /**
             * cellphone : 13525362536
             * createTime : 2016-11-17 17:58:00
             * eduGrade : 0
             * gender : 0
             * isInitial : 0
             * isSetPassword : 1
             * nickname : 用户_pbHuew
             * portrait :
             * portraitSmall :
             */
            public boolean isLoginByPhone;
//            public String cellphone;
//            public String createTime;
            public int    eduGrade;
            public int    gender;
            public int    isInitial;
            public int    isSetPassword;
            public String nickname;
            public String portrait;
            public  String                                                               portraitSmall;
            public  String                                                               birthday;
            public  String dtUserId;
//            public  double                                                               gisLatitude;
//            public  String                                                               gisLocation;
//            public  double                                                               gisLongitude;
//            public  int                                                                  provinceId;
//            public  int                                                                  cityId;
//            public  int                                                                  areaId;
//            public  int                                                                  schoolId;
            public  String                                                               schoolName;
            public  String                                                               friendInviteCode;
            public  String                                                               inviteCode;
            public  String                                                               imUserId;
            public  String                                                               imToken;
            public  int                                                                  isImAutoConnect;
            public int                             isTourist;
            public List<UserBindsBean> userBinds;
            public String memberHpUrl;
            public String bookName;
            public int bookId;

            public static class UserBindsBean implements Serializable{
                /**
                 * bindType : 4
                 * tpNickname : GG
                 */

                public int bindType;
                public String tpNickname;
            }
        }

    }
}
