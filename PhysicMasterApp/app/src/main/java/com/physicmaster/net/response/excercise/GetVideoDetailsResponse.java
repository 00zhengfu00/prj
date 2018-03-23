package com.physicmaster.net.response.excercise;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2017-07-05.
 */

public class GetVideoDetailsResponse extends Response {

    public DataBean data;

    public static class DataBean {

        public String replacement2;
        public AppVideoStudyVoBean appVideoStudyVo;
        public int canPlay;
        public String replacement1;
        public String trySnUrl;

        public static class AppVideoStudyVoBean implements Parcelable {
            public int chapterId;
            public int isFav;
            public String m3u8Content;
            public int pointValue;
            public String posterUrl;
            public int questionCount;
            public int timeLength;
            public String timeLengthStr;
            public int videoId;
            public String videoQuality;
            public String videoTitle;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.chapterId);
                dest.writeInt(this.isFav);
                dest.writeString(this.m3u8Content);
                dest.writeInt(this.pointValue);
                dest.writeString(this.posterUrl);
                dest.writeInt(this.questionCount);
                dest.writeInt(this.timeLength);
                dest.writeString(this.timeLengthStr);
                dest.writeInt(this.videoId);
                dest.writeString(this.videoQuality);
                dest.writeString(this.videoTitle);
            }

            public AppVideoStudyVoBean() {
            }

            protected AppVideoStudyVoBean(Parcel in) {
                this.chapterId = in.readInt();
                this.isFav = in.readInt();
                this.m3u8Content = in.readString();
                this.pointValue = in.readInt();
                this.posterUrl = in.readString();
                this.questionCount = in.readInt();
                this.timeLength = in.readInt();
                this.timeLengthStr = in.readString();
                this.videoId = in.readInt();
                this.videoQuality = in.readString();
                this.videoTitle = in.readString();
            }

            public static final Parcelable.Creator<AppVideoStudyVoBean> CREATOR = new Parcelable.Creator<AppVideoStudyVoBean>() {
                @Override
                public AppVideoStudyVoBean createFromParcel(Parcel source) {
                    return new AppVideoStudyVoBean(source);
                }

                @Override
                public AppVideoStudyVoBean[] newArray(int size) {
                    return new AppVideoStudyVoBean[size];
                }
            };
        }
    }
}
