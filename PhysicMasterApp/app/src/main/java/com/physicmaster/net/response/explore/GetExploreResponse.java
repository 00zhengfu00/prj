package com.physicmaster.net.response.explore;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2016/11/21.
 */
public class GetExploreResponse  extends Response {


    public DataBean data;

    public static class DataBean {
        /**
         * banner : [{"bannerId":3,"imgUrl":"http://img.thelper.cn/app/banner/b170310/banner.jpg","pageUrl":"http://m.lswuyou.com/c/p/b/b170310"},{"bannerId":2,
         * "imgUrl":"http://img.thelper.cn/dt/e/1488534740953265jwoyh.jpg","pageUrl":"http://a.nowhb.com/c/p/article/d/TN23FT2NTMOZD1MXMFBPTZHENTMSE3SX"},{"bannerId":1,
         * "imgUrl":"http://img.thelper.cn/dt/a/1484379331416g54o7bet.jpg","pageUrl":"http://m.lswuyou.com/c/p/article/d/NJYFORPE71NHBVXPSQOAHDCXLFOTVXOV"}]
         * superMember : {"items":[{"isRecommend":1,"memberItemId":51,"monthPrice":"0.00","title":"0.01元/12个月","validDays":365},{"isRecommend":0,"memberItemId":52,
         * "monthPrice":"0.00","title":"0.01元/6个月","validDays":180}],"poster":"http://img.thelper.cn/app/v3/member/superBuyPoster.jpg"}
         * newVideoList : [{"posterUrl":"http://img.thelper.cn/dt/c/1477639550200locppmk0.jpg","title":"海峰挖坟挖","videoId":1001}]
         */

        public SuperMemberBean superMember;
        public List<BannerBean> banner;
        public List<NewVideoListBean> newVideoList;

        public static class SuperMemberBean implements Parcelable {
            /**
             * items : [{"isRecommend":1,"memberItemId":51,"monthPrice":"0.00","title":"0.01元/12个月","validDays":365},{"isRecommend":0,"memberItemId":52,"monthPrice":"0.00",
             * "title":"0.01元/6个月","validDays":180}]
             * poster : http://img.thelper.cn/app/v3/member/superBuyPoster.jpg
             */

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
                dest.writeString(this.poster);
                dest.writeList(this.items);
            }

            public SuperMemberBean() {
            }

            protected SuperMemberBean(Parcel in) {
                this.poster = in.readString();
                this.items = new ArrayList<ItemsBean>();
                in.readList(this.items, ItemsBean.class.getClassLoader());
            }

            public static final Parcelable.Creator<SuperMemberBean> CREATOR = new Parcelable.Creator<SuperMemberBean>() {
                @Override
                public SuperMemberBean createFromParcel(Parcel source) {
                    return new SuperMemberBean(source);
                }

                @Override
                public SuperMemberBean[] newArray(int size) {
                    return new SuperMemberBean[size];
                }
            };
        }

        public static class BannerBean {
            /**
             * bannerId : 3
             * imgUrl : http://img.thelper.cn/app/banner/b170310/banner.jpg
             * pageUrl : http://m.lswuyou.com/c/p/b/b170310
             */

            public int bannerId;
            public String imgUrl;
            public String pageUrl;
        }

        public static class NewVideoListBean implements Parcelable {
            /**
             * posterUrl : http://img.thelper.cn/dt/c/1477639550200locppmk0.jpg
             * title : 海峰挖坟挖
             * videoId : 1001
             */

            public String posterUrl;
            public String title;
            public int videoId;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.posterUrl);
                dest.writeString(this.title);
                dest.writeInt(this.videoId);
            }

            public NewVideoListBean() {
            }

            protected NewVideoListBean(Parcel in) {
                this.posterUrl = in.readString();
                this.title = in.readString();
                this.videoId = in.readInt();
            }

            public static final Parcelable.Creator<NewVideoListBean> CREATOR = new Parcelable.Creator<NewVideoListBean>() {
                @Override
                public NewVideoListBean createFromParcel(Parcel source) {
                    return new NewVideoListBean(source);
                }

                @Override
                public NewVideoListBean[] newArray(int size) {
                    return new NewVideoListBean[size];
                }
            };
        }
    }
}
