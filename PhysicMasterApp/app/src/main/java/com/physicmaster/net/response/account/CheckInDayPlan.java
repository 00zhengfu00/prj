package com.physicmaster.net.response.account;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huashigen on 2017/1/18.
 */

public class CheckInDayPlan implements Parcelable {
    public int awardGoldCoin;  //奖励金币数
    public int awardPoint;  //奖励积分
    public int awardProp1Count; //奖励道具1
    public String awardProp1ImgUrl; // 奖励道具1图片
    public int awardProp2Count;
    public String awardProp2ImgUrl; // 奖励道具1图片
    public int awardProp3Count;
    public String awardProp3ImgUrl; // 奖励道具1图片
    public int dayNum;  //签到排序
    public int isCheckIn;  //是否签到

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.awardGoldCoin);
        dest.writeInt(this.awardPoint);
        dest.writeInt(this.awardProp1Count);
        dest.writeString(this.awardProp1ImgUrl);
        dest.writeInt(this.awardProp2Count);
        dest.writeString(this.awardProp2ImgUrl);
        dest.writeInt(this.awardProp3Count);
        dest.writeString(this.awardProp3ImgUrl);
        dest.writeInt(this.dayNum);
        dest.writeInt(this.isCheckIn);
    }

    public CheckInDayPlan() {
    }

    protected CheckInDayPlan(Parcel in) {
        this.awardGoldCoin = in.readInt();
        this.awardPoint = in.readInt();
        this.awardProp1Count = in.readInt();
        this.awardProp1ImgUrl = in.readString();
        this.awardProp2Count = in.readInt();
        this.awardProp2ImgUrl = in.readString();
        this.awardProp3Count = in.readInt();
        this.awardProp3ImgUrl = in.readString();
        this.dayNum = in.readInt();
        this.isCheckIn = in.readInt();
    }

    public static final Parcelable.Creator<CheckInDayPlan> CREATOR = new Parcelable.Creator<CheckInDayPlan>() {
        @Override
        public CheckInDayPlan createFromParcel(Parcel source) {
            return new CheckInDayPlan(source);
        }

        @Override
        public CheckInDayPlan[] newArray(int size) {
            return new CheckInDayPlan[size];
        }
    };
}
