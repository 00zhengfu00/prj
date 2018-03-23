package com.physicmaster.net.response.discuss;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2016/11/24.
 */
public class QuestionDetailResponse extends Response implements Serializable{


    /**
     * data : {"questionDetail":{"answerCount":0,"content":"啦啦啦啦 我是学霸哈哈哈","gradeSubject":"物理七年级","imgUrls":["http://img.thelper.cn/app/help_pay/2.jpg"],"qid":1217,"releaseTime":"今天 15:07","title":"test"}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * questionDetail : {"answerCount":0,"content":"啦啦啦啦 我是学霸哈哈哈","gradeSubject":"物理七年级","imgUrls":["http://img.thelper.cn/app/help_pay/2.jpg"],"qid":1217,"releaseTime":"今天 15:07","title":"test"}
         */

        public QuestionDetailBean questionDetail;

        public static class QuestionDetailBean {
            /**
             * answerCount : 0
             * content : 啦啦啦啦 我是学霸哈哈哈
             * gradeSubject : 物理七年级
             * imgUrls : ["http://img.thelper.cn/app/help_pay/2.jpg"]
             * qid : 1217
             * releaseTime : 今天 15:07
             * title : test
             */

            public int canRemove;
            public String answerCount;
            public String       content;
            public String       gradeSubject;
            public int          qid;
            public String       releaseTime;
            public String       title;
            public List<ImgVoBean> imgVos;
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
