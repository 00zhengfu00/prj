package com.physicmaster.net.response.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

import java.io.Serializable;

/**
 * Created by huashigen on 2017-07-10.
 */

public class PetInfoResponse extends Response {

    public DataBean data;

    public static class DataBean {

        public int hasNewMsg;
        public PetVoBean petVo;
        public String petJoke;
        public NewMsgCountVoBean newMsgCountVo;

        public static class PetVoBean implements Parcelable {
            /**
             * attrAttack : 12
             * attrDefense : 11
             * attrHp : 22
             * attrSpecialAttack : 12
             * attrSpecialDefense : 9
             * attrSpeed : 17
             * characterName : 马虎
             * isUpStage : 0
             * nature1 : 电
             * nature2 : 普
             * petImg : http://img.thelper.cn/pet/203/2.gif
             * petLevel : 5
             * petName : 流击喵
             * petPoint : 4056
             * petStage : 2
             * promoteAttrStr : 特攻+10%
             * suppressAttrStr : 特防-10%
             * userPetId : 1
             */

            public int attrAttack;
            public int attrDefense;
            public int attrHp;
            public int attrSpecialAttack;
            public int attrSpecialDefense;
            public int attrSpeed;
            public String characterName;
            public int isUpStage;
            public String nature1;
            public String nature2;
            public String petImg;
            public int petLevel;
            public String petName;
            public int petPoint;
            public int maxPetPoint;
            public int minPetPoint;
            public int petStage;
            public String promoteAttrStr;
            public String suppressAttrStr;
            public int userPetId;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.attrAttack);
                dest.writeInt(this.attrDefense);
                dest.writeInt(this.attrHp);
                dest.writeInt(this.attrSpecialAttack);
                dest.writeInt(this.attrSpecialDefense);
                dest.writeInt(this.attrSpeed);
                dest.writeString(this.characterName);
                dest.writeInt(this.isUpStage);
                dest.writeString(this.nature1);
                dest.writeString(this.nature2);
                dest.writeString(this.petImg);
                dest.writeInt(this.petLevel);
                dest.writeString(this.petName);
                dest.writeInt(this.petPoint);
                dest.writeInt(this.petStage);
                dest.writeString(this.promoteAttrStr);
                dest.writeString(this.suppressAttrStr);
                dest.writeInt(this.userPetId);
            }

            public PetVoBean() {
            }

            protected PetVoBean(Parcel in) {
                this.attrAttack = in.readInt();
                this.attrDefense = in.readInt();
                this.attrHp = in.readInt();
                this.attrSpecialAttack = in.readInt();
                this.attrSpecialDefense = in.readInt();
                this.attrSpeed = in.readInt();
                this.characterName = in.readString();
                this.isUpStage = in.readInt();
                this.nature1 = in.readString();
                this.nature2 = in.readString();
                this.petImg = in.readString();
                this.petLevel = in.readInt();
                this.petName = in.readString();
                this.petPoint = in.readInt();
                this.petStage = in.readInt();
                this.promoteAttrStr = in.readString();
                this.suppressAttrStr = in.readString();
                this.userPetId = in.readInt();
            }

            public static final Parcelable.Creator<PetVoBean> CREATOR = new Parcelable.Creator<PetVoBean>() {
                @Override
                public PetVoBean createFromParcel(Parcel source) {
                    return new PetVoBean(source);
                }

                @Override
                public PetVoBean[] newArray(int size) {
                    return new PetVoBean[size];
                }
            };
        }

        public static class NewMsgCountVoBean implements Serializable {
            /**
             * all : 2
             * freshNews : 0
             * gameEnergyRequest : 2
             * relationInvitation : 0
             */
            public int all;
            public int freshNews;
            public int gameEnergyRequest;
            public int relationInvitation;
        }
    }
}
