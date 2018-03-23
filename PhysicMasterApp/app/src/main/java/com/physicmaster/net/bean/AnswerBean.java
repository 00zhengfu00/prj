package com.physicmaster.net.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by songrui on 16/11/29.
 */

public class AnswerBean implements Parcelable {
    public String answer = "";
    public int quId;
    public int wrongId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.answer);
        dest.writeInt(this.quId);
        dest.writeInt(this.wrongId);
    }

    public AnswerBean() {
    }

    protected AnswerBean(Parcel in) {
        this.answer = in.readString();
        this.quId = in.readInt();
        this.wrongId = in.readInt();
    }

    public static final Parcelable.Creator<AnswerBean> CREATOR = new Parcelable
            .Creator<AnswerBean>() {
        @Override
        public AnswerBean createFromParcel(Parcel source) {
            return new AnswerBean(source);
        }

        @Override
        public AnswerBean[] newArray(int size) {
            return new AnswerBean[size];
        }
    };
}
