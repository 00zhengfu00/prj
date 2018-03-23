package com.physicmaster.net.response.course;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huashigen on 2016/11/26.
 */
public class VideoVo implements Parcelable {
    public int starLevel;//0,
    public String timeLengthStr;//01;39,
    public int videoFinish;//0,
    public int videoId;//1451,
    public String videoTitle;//机械运动1-基础经典习题
    public int isCanPlay;
    public boolean selected = false;
    public int existExample;
    public String posterUrl;


    public VideoVo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.starLevel);
        dest.writeString(this.timeLengthStr);
        dest.writeInt(this.videoFinish);
        dest.writeInt(this.videoId);
        dest.writeString(this.videoTitle);
        dest.writeInt(this.isCanPlay);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.existExample);
    }

    protected VideoVo(Parcel in) {
        this.starLevel = in.readInt();
        this.timeLengthStr = in.readString();
        this.videoFinish = in.readInt();
        this.videoId = in.readInt();
        this.videoTitle = in.readString();
        this.isCanPlay = in.readInt();
        this.selected = in.readByte() != 0;
        this.existExample = in.readInt();
    }

    public static final Creator<VideoVo> CREATOR = new Creator<VideoVo>() {
        @Override
        public VideoVo createFromParcel(Parcel source) {
            return new VideoVo(source);
        }

        @Override
        public VideoVo[] newArray(int size) {
            return new VideoVo[size];
        }
    };
}
