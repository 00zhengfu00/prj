package com.physicmaster.net.response.ranking;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2016/11/26.
 */
public class GetRankingResponse extends Response {


    /**
     * data : {"friendTotal":{"userRank":{"id":1001,"m":0,"n":"强子","p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","s":"第7名","v":14948},"rankList":[{"id":2324848,"m":0,"n":"於楠大師","o":1,"p":"http://img.thelper.cn/dt/u/3rnaow/23248481484355793126.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":42391},{"id":1001,"m":0,"n":"强子","o":7,"p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","v":14948},{"id":2327648,"m":0,"n":"柠檬","o":100,"p":"http://img.thelper.cn/dt/u/3rt6f4/1484358800028.png%40160w_160h_100Q_1e_1c_2o.png","v":180}]},"friendWeek":{"userRank":{"id":1001,"m":0,"n":"强子","p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","s":"暂无排名","v":0},"rankList":[{"id":1005,"m":0,"n":"华仔","o":1,"p":"http://imgtest.thelper.cn/dt/u/23zu/10051496717176243.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":135},{"id":1882381,"m":0,"n":"缺了蜂蜜的维尼熊","o":2,"p":"http://imgtest.thelper.cn/dt/u/31twje/18823811499677007539.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":60}]},"total":{"userRank":{"id":1001,"m":0,"n":"强子","p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","s":"第18名","v":14948},"rankList":[{"id":2324848,"m":0,"n":"於楠大師","o":1,"p":"http://img.thelper.cn/dt/u/3rnaow/23248481484355793126.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":42391},{"id":1001,"m":0,"n":"强子","o":18,"p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","v":14948},{"id":2350282,"m":0,"n":"汪莉","o":100,"p":"http://img.thelper.cn/dt/u/3t4pxw/1484557310098.png%40160w_160h_100Q_1e_1c_2o.png","v":6765}]},"week":{"userRank":{"id":1001,"m":0,"n":"强子","p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","s":"暂无排名","v":0},"rankList":[{"id":1005,"m":0,"n":"华仔","o":1,"p":"http://imgtest.thelper.cn/dt/u/23zu/10051496717176243.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":135},{"id":1882381,"m":0,"n":"缺了蜂蜜的维尼熊","o":4,"p":"http://imgtest.thelper.cn/dt/u/31twje/18823811499677007539.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":60}]}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * friendTotal : {"userRank":{"id":1001,"m":0,"n":"强子","p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","s":"第7名","v":14948},"rankList":[{"id":2324848,"m":0,"n":"於楠大師","o":1,"p":"http://img.thelper.cn/dt/u/3rnaow/23248481484355793126.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":42391},{"id":1001,"m":0,"n":"强子","o":7,"p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","v":14948},{"id":2327648,"m":0,"n":"柠檬","o":100,"p":"http://img.thelper.cn/dt/u/3rt6f4/1484358800028.png%40160w_160h_100Q_1e_1c_2o.png","v":180}]}
         * friendWeek : {"userRank":{"id":1001,"m":0,"n":"强子","p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","s":"暂无排名","v":0},"rankList":[{"id":1005,"m":0,"n":"华仔","o":1,"p":"http://imgtest.thelper.cn/dt/u/23zu/10051496717176243.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":135},{"id":1882381,"m":0,"n":"缺了蜂蜜的维尼熊","o":2,"p":"http://imgtest.thelper.cn/dt/u/31twje/18823811499677007539.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":60}]}
         * total : {"userRank":{"id":1001,"m":0,"n":"强子","p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","s":"第18名","v":14948},"rankList":[{"id":2324848,"m":0,"n":"於楠大師","o":1,"p":"http://img.thelper.cn/dt/u/3rnaow/23248481484355793126.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":42391},{"id":1001,"m":0,"n":"强子","o":18,"p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","v":14948},{"id":2350282,"m":0,"n":"汪莉","o":100,"p":"http://img.thelper.cn/dt/u/3t4pxw/1484557310098.png%40160w_160h_100Q_1e_1c_2o.png","v":6765}]}
         * week : {"userRank":{"id":1001,"m":0,"n":"强子","p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","s":"暂无排名","v":0},"rankList":[{"id":1005,"m":0,"n":"华仔","o":1,"p":"http://imgtest.thelper.cn/dt/u/23zu/10051496717176243.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":135},{"id":1882381,"m":0,"n":"缺了蜂蜜的维尼熊","o":4,"p":"http://imgtest.thelper.cn/dt/u/31twje/18823811499677007539.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":60}]}
         */

        public RankBean friendTotal;
        public RankBean friendWeek;
        public RankBean total;
        public RankBean week;

        public static class RankBean implements Parcelable {
            /**
             * userRank : {"id":1001,"m":0,"n":"强子","p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","s":"第7名","v":14948}
             * rankList : [{"id":2324848,"m":0,"n":"於楠大師","o":1,"p":"http://img.thelper.cn/dt/u/3rnaow/23248481484355793126.jpeg%40160w_160h_100Q_1e_1c_2o.jpg","v":42391},{"id":1001,"m":0,"n":"强子","o":7,"p":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40160w_160h_100Q_1e_1c_2o.jpg","v":14948},{"id":2327648,"m":0,"n":"柠檬","o":100,"p":"http://img.thelper.cn/dt/u/3rt6f4/1484358800028.png%40160w_160h_100Q_1e_1c_2o.png","v":180}]
             */

            public UserRankBean       userRank;
            public List<RankListBean> rankList;

            public static class UserRankBean implements Parcelable {


                public int    id;
                public int    m;
                public String n;
                public String p;
                public String s;
                public int    v;

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.id);
                    dest.writeInt(this.m);
                    dest.writeString(this.n);
                    dest.writeString(this.p);
                    dest.writeString(this.s);
                    dest.writeInt(this.v);
                }

                public UserRankBean() {
                }

                protected UserRankBean(Parcel in) {
                    this.id = in.readInt();
                    this.m = in.readInt();
                    this.n = in.readString();
                    this.p = in.readString();
                    this.s = in.readString();
                    this.v = in.readInt();
                }

                public static final Creator<UserRankBean> CREATOR = new Creator<UserRankBean>() {
                    @Override
                    public UserRankBean createFromParcel(Parcel source) {
                        return new UserRankBean(source);
                    }

                    @Override
                    public UserRankBean[] newArray(int size) {
                        return new UserRankBean[size];
                    }
                };
            }

            public static class RankListBean implements Parcelable {


                public int    id;
                public int    m;
                public String n;
                public int    o;
                public String p;
                public int    v;

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.id);
                    dest.writeInt(this.m);
                    dest.writeString(this.n);
                    dest.writeInt(this.o);
                    dest.writeString(this.p);
                    dest.writeInt(this.v);
                }

                public RankListBean() {
                }

                protected RankListBean(Parcel in) {
                    this.id = in.readInt();
                    this.m = in.readInt();
                    this.n = in.readString();
                    this.o = in.readInt();
                    this.p = in.readString();
                    this.v = in.readInt();
                }

                public static final Creator<RankListBean> CREATOR = new Creator<RankListBean>() {
                    @Override
                    public RankListBean createFromParcel(Parcel source) {
                        return new RankListBean(source);
                    }

                    @Override
                    public RankListBean[] newArray(int size) {
                        return new RankListBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeParcelable(this.userRank, flags);
                dest.writeList(this.rankList);
            }

            public RankBean() {
            }

            protected RankBean(Parcel in) {
                this.userRank = in.readParcelable(UserRankBean.class.getClassLoader());
                this.rankList = new ArrayList<RankListBean>();
                in.readList(this.rankList, RankListBean.class.getClassLoader());
            }

            public static final Parcelable.Creator<RankBean> CREATOR = new Parcelable.Creator<RankBean>() {
                @Override
                public RankBean createFromParcel(Parcel source) {
                    return new RankBean(source);
                }

                @Override
                public RankBean[] newArray(int size) {
                    return new RankBean[size];
                }
            };
        }
    }
}
