package com.physicmaster.net.response.user;


import com.physicmaster.net.response.Response;

import java.io.Serializable;

public class MainInfoResponse extends Response {

    /**
     * data : {"maxEnergyValue":32,"petVo":{"attrAttack":7,"attrDefense":7,"attrHp":14,
     * "attrSpecialAttack":7,"attrSpecialDefense":8,"attrSpeed":5,"characterName":"急躁",
     * "isUpStage":0,"maxPetPoint":999,"nature1":"草","nature2":"毒","petImg":"http://img.thelper
     * .cn/pet/201/1.gif","petLevel":2,"petName":"小伢蛙","petPoint":571,"petStage":1,
     * "promoteAttrStr":"速度+10%","suppressAttrStr":"速度-10%","userPetId":4},"energyValue":32}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * maxEnergyValue : 32
         * petVo : {"attrAttack":7,"attrDefense":7,"attrHp":14,"attrSpecialAttack":7,
         * "attrSpecialDefense":8,"attrSpeed":5,"characterName":"急躁","isUpStage":0,
         * "maxPetPoint":999,"nature1":"草","nature2":"毒","petImg":"http://img.thelper
         * .cn/pet/201/1.gif","petLevel":2,"petName":"小伢蛙","petPoint":571,"petStage":1,
         * "promoteAttrStr":"速度+10%","suppressAttrStr":"速度-10%","userPetId":4}
         * energyValue : 32
         */

        public int maxEnergyValue;
        public PetVoBean petVo;
        public int energyValue;
        public int hasNewMsg;
        public NewMsgCountVoBean newMsgCountVo;

        public static class PetVoBean implements Serializable {
            /**
             * attrAttack : 7
             * attrDefense : 7
             * attrHp : 14
             * attrSpecialAttack : 7
             * attrSpecialDefense : 8
             * attrSpeed : 5
             * characterName : 急躁
             * isUpStage : 0
             * maxPetPoint : 999
             * nature1 : 草
             * nature2 : 毒
             * petImg : http://img.thelper.cn/pet/201/1.gif
             * petLevel : 2
             * petName : 小伢蛙
             * petPoint : 571
             * petStage : 1
             * promoteAttrStr : 速度+10%
             * suppressAttrStr : 速度-10%
             * userPetId : 4
             */

            public int attrAttack;
            public int attrDefense;
            public int attrHp;
            public int attrSpecialAttack;
            public int attrSpecialDefense;
            public int attrSpeed;
            public int maxPetPoint;
            public String nature1;
            public String nature2;
            public String petImg;
            public int petLevel;
            public String petName;
            public int petPoint;
            public int isUpStage;
            public int petStage;
            public String characterName;
            public String promoteAttrStr;
            public String suppressAttrStr;
            public int userPetId;
            public int minPetPoint;

        }

        public static class NewMsgCountVoBean implements Serializable {
            /**
             * all : 0
             * freshNews : 0
             * gameEnergyRequest : 0
             * relationInvitation : 0
             */

            public int all;
            public int freshNews;
            public int gameEnergyRequest;
            public int relationInvitation;
        }
    }
}
