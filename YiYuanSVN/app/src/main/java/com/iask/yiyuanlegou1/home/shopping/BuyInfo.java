package com.iask.yiyuanlegou1.home.shopping;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/1.
 */
public class BuyInfo implements Parcelable ,Serializable{
    // 商品id
    public Integer id;
    // 购买个数
    public Integer num;

    public static final Creator<BuyInfo> CREATOR = new Creator<BuyInfo>() {

        @Override
        public BuyInfo createFromParcel(Parcel in) {
            return new BuyInfo(in);
        }

        @Override
        public BuyInfo[] newArray(int size) {
            return new BuyInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public BuyInfo() {

    }

    public BuyInfo(Parcel in) {
        id = in.readInt();
        num = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(num);
    }
}
