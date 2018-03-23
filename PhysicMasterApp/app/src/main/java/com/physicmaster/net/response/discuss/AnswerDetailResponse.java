package com.physicmaster.net.response.discuss;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2016/11/24.
 */
public class AnswerDetailResponse extends Response implements Serializable{


    /**
     * data : {"answerDetail":{"commentCount":0,"content":"啊啊啊啊啊啊啊啊啊啊啊哇打我费","imgUrls":["http://img.thelper.cn/app/help_pay/2.jpg"],"isLike":0,"nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","title":"test"}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * answerDetail : {"commentCount":0,"content":"啊啊啊啊啊啊啊啊啊啊啊哇打我费","imgUrls":["http://img.thelper.cn/app/help_pay/2.jpg"],"isLike":0,"nickname":"强子","portrait":"http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg","title":"test"}
         */

        public AnswerDetailBean answerDetail;

        public static class AnswerDetailBean {
            /**
             * commentCount : 0
             * content : 啊啊啊啊啊啊啊啊啊啊啊哇打我费
             * imgUrls : ["http://img.thelper.cn/app/help_pay/2.jpg"]
             * isLike : 0
             * nickname : 强子
             * portrait : http://img.thelper.cn/dt/u/23oy/1488593780054.jpg%40120w_120h_100Q_1e_1c_2o.jpg
             * title : test
             */
            public int canRemove;
            public int dtUserId;
            public String commentCount;
            public String       content;
            public int          isLike;
            public String          likeCount;
            public String       nickname;
            public String       portrait;
            public String       title;
            public List<ImgVoBean> imgVos;

            public boolean isLike() {
                return isLike==1;
            }

            public void setLike(int isLike) {
                this.isLike = isLike;
            }

            public String getLikeNum() {
                return likeCount;
            }

            public void setLikeNum(String likeCount) {
                this.likeCount = likeCount;
            }
        }
        public static class ImgVoBean implements Parcelable{
            /**
             * h : 312
             * s : 1111111
             * u : http://imgtest.thelper.cn/dt/u/4m06o/1494911521082%40672w_312h_95Q_1e_1c_2o.src
             * w : 672
             */

            public int h;
            public int    s;
            public String u;
            public int    w;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.h);
                dest.writeInt(this.s);
                dest.writeString(this.u);
                dest.writeInt(this.w);
            }

            public ImgVoBean() {
            }

            protected ImgVoBean(Parcel in) {
                this.h = in.readInt();
                this.s = in.readInt();
                this.u = in.readString();
                this.w = in.readInt();
            }

            public static final Creator<ImgVoBean> CREATOR = new Creator<ImgVoBean>() {
                @Override
                public ImgVoBean createFromParcel(Parcel source) {
                    return new ImgVoBean(source);
                }

                @Override
                public ImgVoBean[] newArray(int size) {
                    return new ImgVoBean[size];
                }
            };
        }
    }
}
