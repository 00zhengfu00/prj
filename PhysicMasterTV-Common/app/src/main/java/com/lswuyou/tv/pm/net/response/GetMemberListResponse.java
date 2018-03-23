package com.lswuyou.tv.pm.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by huashigen on 2017-09-07.
 */

public class GetMemberListResponse extends Response {

    public DataBean data;

    public static class DataBean {

        public SuperMemberBean superMember;
        public List<MemberListBean> memberList;

        public static class SuperMemberBean {
            public String poster;
            public List<ItemsBean> items;

            public static class ItemsBean implements Parcelable {
                /**
                 * isRecommend : 1
                 * memberItemId : 51
                 * monthPrice : 0.00
                 * title : 0.01元/12个月
                 * validDays : 365
                 */

                public int isRecommend;
                public int memberItemId;
                public String monthPrice;
                public String title;
                public int validDays;

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.isRecommend);
                    dest.writeInt(this.memberItemId);
                    dest.writeString(this.monthPrice);
                    dest.writeString(this.title);
                    dest.writeInt(this.validDays);
                }

                public ItemsBean() {
                }

                protected ItemsBean(Parcel in) {
                    this.isRecommend = in.readInt();
                    this.memberItemId = in.readInt();
                    this.monthPrice = in.readString();
                    this.title = in.readString();
                    this.validDays = in.readInt();
                }

                public static final Parcelable.Creator<ItemsBean> CREATOR = new Parcelable.Creator<ItemsBean>() {
                    @Override
                    public ItemsBean createFromParcel(Parcel source) {
                        return new ItemsBean(source);
                    }

                    @Override
                    public ItemsBean[] newArray(int size) {
                        return new ItemsBean[size];
                    }
                };
            }
        }

        public static class MemberListBean {

            public String expiryDate;
            public int isMember;
            public String poster;
            public int subjectId;
            public String title;
            public List<ItemsBeanX> items;

            public static class ItemsBeanX implements Parcelable {
                /**
                 * isRecommend : 1
                 * memberItemId : 112
                 * monthPrice : 0.00
                 * title : 0.01元/6个月
                 * validDays : 180
                 */

                public int isRecommend;
                public int memberItemId;
                public String monthPrice;
                public String title;
                public int validDays;

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.isRecommend);
                    dest.writeInt(this.memberItemId);
                    dest.writeString(this.monthPrice);
                    dest.writeString(this.title);
                    dest.writeInt(this.validDays);
                }

                public ItemsBeanX() {
                }

                protected ItemsBeanX(Parcel in) {
                    this.isRecommend = in.readInt();
                    this.memberItemId = in.readInt();
                    this.monthPrice = in.readString();
                    this.title = in.readString();
                    this.validDays = in.readInt();
                }

                public static final Parcelable.Creator<ItemsBeanX> CREATOR = new Parcelable.Creator<ItemsBeanX>() {
                    @Override
                    public ItemsBeanX createFromParcel(Parcel source) {
                        return new ItemsBeanX(source);
                    }

                    @Override
                    public ItemsBeanX[] newArray(int size) {
                        return new ItemsBeanX[size];
                    }
                };
            }
        }
    }
}
