package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

import java.util.List;

public class RankResponse extends Response {


    /**
     * data : {"AppRankingVo":{"appRankListVo":{"appUserPointVoList":[{"dtUserId":170420,"nickname":"路飞","portrait":"http://imgtest.thelper.cn/i/170420/1481504980666","rankNum":1,"userPoint":808},{"dtUserId":170418,"nickname":"222","portrait":"http://imgtest.thelper.cn/i/170418/1480929241495","rankNum":2,"userPoint":510},{"dtUserId":170446,"nickname":"得了","portrait":"","rankNum":3,"userPoint":270},{"dtUserId":170437,"nickname":"Henry","portrait":"http://imgtest.thelper.cn/i/170437/1480765357.jpg","rankNum":4,"userPoint":210},{"dtUserId":170433,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","rankNum":5,"userPoint":155},{"dtUserId":170430,"nickname":"彭睿","portrait":"","rankNum":6,"userPoint":105},{"dtUserId":170435,"nickname":"啦啦啦","portrait":"","rankNum":7,"userPoint":85},{"dtUserId":170434,"nickname":"八点","portrait":"","rankNum":8,"userPoint":85},{"dtUserId":170445,"nickname":"的","portrait":"","rankNum":9,"userPoint":40},{"dtUserId":170421,"nickname":"gh","portrait":"","rankNum":10,"userPoint":40}],"appuserGoldcoinVoList":[{"coinValue":49999052,"dtUserId":170418,"nickname":"222","portrait":"http://imgtest.thelper.cn/i/170418/1480929241495","rankNum":1},{"coinValue":49998516,"dtUserId":170420,"nickname":"路飞","portrait":"http://imgtest.thelper.cn/i/170420/1481504980666","rankNum":2},{"coinValue":27,"dtUserId":170446,"nickname":"得了","portrait":"","rankNum":3},{"coinValue":10,"dtUserId":170437,"nickname":"Henry","portrait":"http://imgtest.thelper.cn/i/170437/1480765357.jpg","rankNum":4},{"coinValue":9,"dtUserId":170433,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","rankNum":5},{"coinValue":6,"dtUserId":170434,"nickname":"八点","portrait":"","rankNum":6},{"coinValue":6,"dtUserId":170430,"nickname":"彭睿","portrait":"","rankNum":7},{"coinValue":6,"dtUserId":170435,"nickname":"啦啦啦","portrait":"","rankNum":8},{"coinValue":3,"dtUserId":170444,"nickname":"哈哈","portrait":"","rankNum":9},{"coinValue":0,"dtUserId":170432,"nickname":"哈哈","portrait":"","rankNum":10}]},"userCoin":49998516,"userPoint":808,"userjbRank":2,"userjfRank":1}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * AppRankingVo : {"appRankListVo":{"appUserPointVoList":[{"dtUserId":170420,"nickname":"路飞","portrait":"http://imgtest.thelper.cn/i/170420/1481504980666","rankNum":1,"userPoint":808},{"dtUserId":170418,"nickname":"222","portrait":"http://imgtest.thelper.cn/i/170418/1480929241495","rankNum":2,"userPoint":510},{"dtUserId":170446,"nickname":"得了","portrait":"","rankNum":3,"userPoint":270},{"dtUserId":170437,"nickname":"Henry","portrait":"http://imgtest.thelper.cn/i/170437/1480765357.jpg","rankNum":4,"userPoint":210},{"dtUserId":170433,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","rankNum":5,"userPoint":155},{"dtUserId":170430,"nickname":"彭睿","portrait":"","rankNum":6,"userPoint":105},{"dtUserId":170435,"nickname":"啦啦啦","portrait":"","rankNum":7,"userPoint":85},{"dtUserId":170434,"nickname":"八点","portrait":"","rankNum":8,"userPoint":85},{"dtUserId":170445,"nickname":"的","portrait":"","rankNum":9,"userPoint":40},{"dtUserId":170421,"nickname":"gh","portrait":"","rankNum":10,"userPoint":40}],"appuserGoldcoinVoList":[{"coinValue":49999052,"dtUserId":170418,"nickname":"222","portrait":"http://imgtest.thelper.cn/i/170418/1480929241495","rankNum":1},{"coinValue":49998516,"dtUserId":170420,"nickname":"路飞","portrait":"http://imgtest.thelper.cn/i/170420/1481504980666","rankNum":2},{"coinValue":27,"dtUserId":170446,"nickname":"得了","portrait":"","rankNum":3},{"coinValue":10,"dtUserId":170437,"nickname":"Henry","portrait":"http://imgtest.thelper.cn/i/170437/1480765357.jpg","rankNum":4},{"coinValue":9,"dtUserId":170433,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","rankNum":5},{"coinValue":6,"dtUserId":170434,"nickname":"八点","portrait":"","rankNum":6},{"coinValue":6,"dtUserId":170430,"nickname":"彭睿","portrait":"","rankNum":7},{"coinValue":6,"dtUserId":170435,"nickname":"啦啦啦","portrait":"","rankNum":8},{"coinValue":3,"dtUserId":170444,"nickname":"哈哈","portrait":"","rankNum":9},{"coinValue":0,"dtUserId":170432,"nickname":"哈哈","portrait":"","rankNum":10}]},"userCoin":49998516,"userPoint":808,"userjbRank":2,"userjfRank":1}
         */

        public AppRankingVoBean AppRankingVo;

        public static class AppRankingVoBean {
            /**
             * appRankListVo : {"appUserPointVoList":[{"dtUserId":170420,"nickname":"路飞","portrait":"http://imgtest.thelper.cn/i/170420/1481504980666","rankNum":1,"userPoint":808},{"dtUserId":170418,"nickname":"222","portrait":"http://imgtest.thelper.cn/i/170418/1480929241495","rankNum":2,"userPoint":510},{"dtUserId":170446,"nickname":"得了","portrait":"","rankNum":3,"userPoint":270},{"dtUserId":170437,"nickname":"Henry","portrait":"http://imgtest.thelper.cn/i/170437/1480765357.jpg","rankNum":4,"userPoint":210},{"dtUserId":170433,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","rankNum":5,"userPoint":155},{"dtUserId":170430,"nickname":"彭睿","portrait":"","rankNum":6,"userPoint":105},{"dtUserId":170435,"nickname":"啦啦啦","portrait":"","rankNum":7,"userPoint":85},{"dtUserId":170434,"nickname":"八点","portrait":"","rankNum":8,"userPoint":85},{"dtUserId":170445,"nickname":"的","portrait":"","rankNum":9,"userPoint":40},{"dtUserId":170421,"nickname":"gh","portrait":"","rankNum":10,"userPoint":40}],"appuserGoldcoinVoList":[{"coinValue":49999052,"dtUserId":170418,"nickname":"222","portrait":"http://imgtest.thelper.cn/i/170418/1480929241495","rankNum":1},{"coinValue":49998516,"dtUserId":170420,"nickname":"路飞","portrait":"http://imgtest.thelper.cn/i/170420/1481504980666","rankNum":2},{"coinValue":27,"dtUserId":170446,"nickname":"得了","portrait":"","rankNum":3},{"coinValue":10,"dtUserId":170437,"nickname":"Henry","portrait":"http://imgtest.thelper.cn/i/170437/1480765357.jpg","rankNum":4},{"coinValue":9,"dtUserId":170433,"nickname":"ryy","portrait":"http://imgtest.thelper.cn/i/170433/1480756342.jpg","rankNum":5},{"coinValue":6,"dtUserId":170434,"nickname":"八点","portrait":"","rankNum":6},{"coinValue":6,"dtUserId":170430,"nickname":"彭睿","portrait":"","rankNum":7},{"coinValue":6,"dtUserId":170435,"nickname":"啦啦啦","portrait":"","rankNum":8},{"coinValue":3,"dtUserId":170444,"nickname":"哈哈","portrait":"","rankNum":9},{"coinValue":0,"dtUserId":170432,"nickname":"哈哈","portrait":"","rankNum":10}]}
             * userCoin : 49998516
             * userPoint : 808
             * userjbRank : 2
             * userjfRank : 1
             */

            public AppRankListVoBean appRankListVo;
            public int userCoin;
            public int userPoint;
            public String userjbRank;
            public String userjfRank;

            public static class AppRankListVoBean {
                public List<AppUserPointVoListBean>    appUserPointVoList;
                public List<AppuserGoldcoinVoListBean> appuserGoldcoinVoList;

                public static class AppUserPointVoListBean {
                    /**
                     * dtUserId : 170420
                     * nickname : 路飞
                     * portrait : http://imgtest.thelper.cn/i/170420/1481504980666
                     * rankNum : 1
                     * userPoint : 808
                     */

                    public int dtUserId;
                    public String nickname;
                    public String portrait;
                    public int    rankNum;
                    public int    userPoint;
                }

                public static class AppuserGoldcoinVoListBean {
                    /**
                     * coinValue : 49999052
                     * dtUserId : 170418
                     * nickname : 222
                     * portrait : http://imgtest.thelper.cn/i/170418/1480929241495
                     * rankNum : 1
                     */

                    public int coinValue;
                    public int    dtUserId;
                    public String nickname;
                    public String portrait;
                    public int    rankNum;
                }
            }
        }
    }
}
