package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

public class ShowEndeavorResponse extends Response {


    /**
     * data : {"effortVo":{"attack":0,"defense":0,"maxAttributeValue":252,"specialAttack":0,"specialDefense":0,"speed":0}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * effortVo : {"attack":0,"defense":0,"maxAttributeValue":252,"specialAttack":0,"specialDefense":0,"speed":0}
         */
        public int propCount;
        public int propId;
        public EffortVoBean effortVo;

        public static class EffortVoBean {
            /**
             * attack : 0
             * defense : 0
             * maxAttributeValue : 252
             * specialAttack : 0
             * specialDefense : 0
             * speed : 0
             */

            public int attack;
            public int defense;
            public int maxAttributeValue;
            public int specialAttack;
            public int specialDefense;
            public int speed;
        }
    }
}
