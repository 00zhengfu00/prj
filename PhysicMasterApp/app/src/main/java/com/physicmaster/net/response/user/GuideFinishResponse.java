package com.physicmaster.net.response.user;


import com.physicmaster.net.response.Response;

public class GuideFinishResponse extends Response {


    /**
     * nonce : X7omWHEXSG3osZot
     * data : {"loginVo":{"cellphone":"13565652525","createTime":"2016-11-18 09:17:12","eduGrade":8,"gender":0,"isInitial":1,"isSetPassword":1,"nickname":"哈哈","portrait":"","portraitSmall":""}}
     */

    public String   nonce;
    public DataBean data;


    public static class DataBean {
        /**
         * loginVo : {"cellphone":"13565652525","createTime":"2016-11-18 09:17:12","eduGrade":8,"gender":0,"isInitial":1,"isSetPassword":1,"nickname":"哈哈","portrait":"","portraitSmall":""}
         */

        public LoginVoBean loginVo;


        public static class LoginVoBean {
            /**
             * cellphone : 13565652525
             * createTime : 2016-11-18 09:17:12
             * eduGrade : 8
             * gender : 0
             * isInitial : 1
             * isSetPassword : 1
             * nickname : 哈哈
             * portrait :
             * portraitSmall :
             */

            public String cellphone;
            public String createTime;
            public int    eduGrade;
            public int    gender;
            public int    isInitial;
            public int    isSetPassword;
            public String nickname;
            public String portrait;
            public String portraitSmall;


        }
    }
}
