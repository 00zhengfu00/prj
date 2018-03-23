package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

public class CheckMedalResponse extends Response {


    /**
     * data : {"medalNewGetList":[{"isClaimed":1,"medalDesc":"连续7天早起签到","medalImg":"http://img.thelper.cn/icon/medal/101-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/101-b.png","medalName":"早起鸟"}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<MedalNewGetListBean> medalNewGetList;

        public static class MedalNewGetListBean implements Serializable{
            /**
             * isClaimed : 1
             * medalDesc : 连续7天早起签到
             * medalImg : http://img.thelper.cn/icon/medal/101-a.png
             * medalImgBlack : http://img.thelper.cn/icon/medal/101-b.png
             * medalName : 早起鸟
             */

            public int isClaimed;
            public String medalDesc;
            public String medalImg;
            public String medalImgBlack;
            public String medalName;
        }
    }
}
