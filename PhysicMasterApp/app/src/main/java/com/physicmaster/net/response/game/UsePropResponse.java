package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

public class UsePropResponse extends Response {


    /**
     * data : {"propCount":41,"propId":11}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * propCount : 41
         * propId : 11
         */

        public int propCount;
        public int propId;
    }
}
