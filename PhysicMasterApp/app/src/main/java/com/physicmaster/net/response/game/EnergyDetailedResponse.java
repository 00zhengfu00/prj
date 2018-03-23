package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

public class EnergyDetailedResponse extends Response  {


    /**
     * data : {"userGameEnergy":{"energyValue":30,"maxEnergyValue":32,"recoverFullTime":"10:54"},"propList":[{"priceGoldCoin":0,"propId":11,"propImg":"","propName":"恢复1点精力值","propValue":1},{"priceGoldCoin":4,"propId":12,"propImg":"","propName":"恢复5点精力值","propValue":5},{"priceGoldCoin":20,"propId":13,"propImg":"","propName":"恢复30点精力值","propValue":30}]}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * userGameEnergy : {"energyValue":30,"maxEnergyValue":32,"recoverFullTime":"10:54"}
         * propList : [{"priceGoldCoin":0,"propId":11,"propImg":"","propName":"恢复1点精力值","propValue":1},{"priceGoldCoin":4,"propId":12,"propImg":"","propName":"恢复5点精力值","propValue":5},{"priceGoldCoin":20,"propId":13,"propImg":"","propName":"恢复30点精力值","propValue":30}]
         */

        public UserGameEnergyBean userGameEnergy;
        public List<PropListBean> propList;

        public static class UserGameEnergyBean {
            /**
             * energyValue : 30
             * maxEnergyValue : 32
             * recoverFullTime : 10:54
             */

            public int energyValue;
            public int    maxEnergyValue;
            public String recoverFullTime;
        }

        public static class PropListBean implements Serializable{
            /**
             * priceGoldCoin : 0
             * propId : 11
             * propImg :
             * propName : 恢复1点精力值
             * propValue : 1
             */

            public int priceGoldCoin;
            public int    propId;
            public String propImg;
            public String propName;
            public String propIntro;
            public int    propValue;
        }
    }
}
