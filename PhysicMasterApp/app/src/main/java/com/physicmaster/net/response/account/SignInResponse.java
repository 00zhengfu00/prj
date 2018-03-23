package com.physicmaster.net.response.account;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2017/1/19.
 */

public class SignInResponse extends Response{

    /**
     * data : {"checkInDayPlan":{"awardGoldCoin":0,"awardPoint":10,"awardProp1Count":4,
     * "awardProp1ImgUrl":"http://img.thelper.cn/prop/17.png","awardProp2Count":1,
     * "awardProp2ImgUrl":"http://img.thelper.cn/prop/13.png","awardProp3Count":1,
     * "awardProp3ImgUrl":"http://img.thelper.cn/prop/14.png","dayNum":28,"isCheckIn":1},
     * "checkStatus":2}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * checkInDayPlan : {"awardGoldCoin":0,"awardPoint":10,"awardProp1Count":4,
         * "awardProp1ImgUrl":"http://img.thelper.cn/prop/17.png","awardProp2Count":1,
         * "awardProp2ImgUrl":"http://img.thelper.cn/prop/13.png","awardProp3Count":1,
         * "awardProp3ImgUrl":"http://img.thelper.cn/prop/14.png","dayNum":28,"isCheckIn":1}
         * checkStatus : 2
         */

        public CheckInDayPlan checkInDayPlan;
        public int checkStatus;
    }
}
