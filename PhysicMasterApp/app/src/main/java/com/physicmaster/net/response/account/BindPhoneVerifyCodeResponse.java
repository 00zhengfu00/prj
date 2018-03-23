package com.physicmaster.net.response.account;


import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

public class BindPhoneVerifyCodeResponse extends Response {


    /**
     * data : {"loginVo":{"cellphone":"18989898989","createTime":"2016-12-15 18:03:54","eduGrade":3,"gender":0,"inviteCode":"M2B9Y5","isInitial":1,"isSetPassword":0,"nickname":"GG","portrait":"http://img.thelper.cn/icon/portrait/5.png","portraitSmall":"http://img.thelper.cn/icon/portrait/5.png%40120w_120h_100Q_1e_1c_2o.png","userBinds":[{"bindType":4,"tpNickname":"GG"},{"bindType":0,"tpNickname":"GG"}]}}
     */

    public DataBean data;

    public static class DataBean implements Serializable{
        /**
         * loginVo : {"cellphone":"18989898989","createTime":"2016-12-15 18:03:54","eduGrade":3,"gender":0,"inviteCode":"M2B9Y5","isInitial":1,"isSetPassword":0,"nickname":"GG","portrait":"http://img.thelper.cn/icon/portrait/5.png","portraitSmall":"http://img.thelper.cn/icon/portrait/5.png%40120w_120h_100Q_1e_1c_2o.png","userBinds":[{"bindType":4,"tpNickname":"GG"},{"bindType":0,"tpNickname":"GG"}]}
         */

        public LoginVoBean loginVo;

        public static class LoginVoBean implements Serializable{
            /**
             * cellphone : 18989898989
             * createTime : 2016-12-15 18:03:54
             * eduGrade : 3
             * gender : 0
             * inviteCode : M2B9Y5
             * isInitial : 1
             * isSetPassword : 0
             * nickname : GG
             * portrait : http://img.thelper.cn/icon/portrait/5.png
             * portraitSmall : http://img.thelper.cn/icon/portrait/5.png%40120w_120h_100Q_1e_1c_2o.png
             * userBinds : [{"bindType":4,"tpNickname":"GG"},{"bindType":0,"tpNickname":"GG"}]
             */

            public String cellphone;
            public String              createTime;
            public int                 eduGrade;
            public int                 gender;
            public String              inviteCode;
            public int                 isInitial;
            public int                 isSetPassword;
            public String              nickname;
            public String              portrait;
            public String              portraitSmall;
            public List<UserBindsBean> userBinds;

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
