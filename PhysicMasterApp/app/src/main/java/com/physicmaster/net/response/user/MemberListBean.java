package com.physicmaster.net.response.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class MemberListBean implements Parcelable {

    /**
     * isMember : 0
     * items : [{"isRecommend":1,"memberItemId":112,"monthPrice":"59.66","title":"358元/6个月","validDays":180},{"isRecommend":0,"memberItemId":111,"title":"69元/1个月","validDays":30},{"isRecommend":0,"memberItemId":113,"monthPrice":"58.16","title":"698元/12个月","validDays":365}]
     * poster : http://img.thelper.cn/app/member/1d.jpg
     * subjectId : 1
     * title : 初中物理会员
     * expiryDate : 2024-06-24
     */

    public int             isMember;
    public String          poster;
    public int             subjectId;
    public String          title;
    public String          expiryDate;
    public List<ItemsBean> items;

    public static class ItemsBean implements Parcelable {
        /**
         * isRecommend : 1
         * memberItemId : 112
         * monthPrice : 59.66
         * title : 358元/6个月
         * validDays : 180
         */

        public int    isRecommend;
        public int    memberItemId;
        public String monthPrice;
        public String title;
        public int    validDays;

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

        public static final Creator<ItemsBean> CREATOR = new Creator<ItemsBean>() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isMember);
        dest.writeString(this.poster);
        dest.writeInt(this.subjectId);
        dest.writeString(this.title);
        dest.writeString(this.expiryDate);
        dest.writeList(this.items);
    }

    public MemberListBean() {
    }

    protected MemberListBean(Parcel in) {
        this.isMember = in.readInt();
        this.poster = in.readString();
        this.subjectId = in.readInt();
        this.title = in.readString();
        this.expiryDate = in.readString();
        this.items = new ArrayList<ItemsBean>();
        in.readList(this.items, ItemsBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<MemberListBean> CREATOR = new Parcelable.Creator<MemberListBean>() {
        @Override
        public MemberListBean createFromParcel(Parcel source) {
            return new MemberListBean(source);
        }

        @Override
        public MemberListBean[] newArray(int size) {
            return new MemberListBean[size];
        }
    };
}



