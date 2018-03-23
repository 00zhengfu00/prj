package com.physicmaster.net.response.game;


import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

import java.util.List;

public class GoldMallResponse extends Response {

    /**
     * data : {"appProps":[{"priceGoldCoin":1,"propId":11,"propImg":"","propName":"恢复1点精力值","propValue":1},{"priceGoldCoin":4,"propId":12,"propImg":"","propName":"恢复5点精力值","propValue":5},{"priceGoldCoin":20,"propId":13,"propImg":"","propName":"恢复30点精力值","propValue":30},{"priceGoldCoin":50,"propId":14,"propImg":"","propName":"双倍积分","propValue":2},{"priceGoldCoin":10,"propId":15,"propImg":"","propName":"去掉1个错误选项","propValue":1},{"priceGoldCoin":10,"propId":16,"propImg":"","propName":"免错1次","propValue":1}],"goldValue":0}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * appProps : [{"priceGoldCoin":1,"propId":11,"propImg":"","propName":"恢复1点精力值","propValue":1},{"priceGoldCoin":4,"propId":12,"propImg":"","propName":"恢复5点精力值","propValue":5},{"priceGoldCoin":20,"propId":13,"propImg":"","propName":"恢复30点精力值","propValue":30},{"priceGoldCoin":50,"propId":14,"propImg":"","propName":"双倍积分","propValue":2},{"priceGoldCoin":10,"propId":15,"propImg":"","propName":"去掉1个错误选项","propValue":1},{"priceGoldCoin":10,"propId":16,"propImg":"","propName":"免错1次","propValue":1}]
         * goldValue : 0
         */

        public int goldValue;
        public List<AppPropsBean> appProps;

        public static class AppPropsBean implements Parcelable {
            /**
             * priceGoldCoin : 1
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.priceGoldCoin);
                dest.writeInt(this.propId);
                dest.writeString(this.propImg);
                dest.writeString(this.propName);
                dest.writeString(this.propIntro);
                dest.writeInt(this.propValue);
            }

            public AppPropsBean() {
            }

            protected AppPropsBean(Parcel in) {
                this.priceGoldCoin = in.readInt();
                this.propId = in.readInt();
                this.propImg = in.readString();
                this.propName = in.readString();
                this.propIntro = in.readString();
                this.propValue = in.readInt();
            }

            public static final Parcelable.Creator<AppPropsBean> CREATOR = new Parcelable.Creator<AppPropsBean>() {
                @Override
                public AppPropsBean createFromParcel(Parcel source) {
                    return new AppPropsBean(source);
                }

                @Override
                public AppPropsBean[] newArray(int size) {
                    return new AppPropsBean[size];
                }
            };
        }
    }
}
