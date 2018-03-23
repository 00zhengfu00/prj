package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GetBackpackResponse extends Response {

    /**
     * data : {"packPropList":[{"priceGoldCoin":1,"propCount":5,"propId":11,"propImg":"","propName":"恢复1点精力值","propValue":1}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<PackPropListBean> packPropList;

        public static class PackPropListBean {
            /**
             * priceGoldCoin : 1
             * propCount : 5
             * propId : 11
             * propImg :
             * propName : 恢复1点精力值
             * propValue : 1
             */

            public int    priceGoldCoin;
            public int    propCount;
            public int    propId;
            public String propImg;
            public String propName;
            public int    propValue;
            public String propIntro;
            public int    propType;
        }
    }
}
