package com.physicmaster.modules.guide;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huashigen on 2017-07-05.
 * 用于引导页面裸露出来的View
 */

public class ExposureView implements Parcelable {
    private int left;
    private int top;
    private int right;
    private int bottom;
    private Bitmap bitmap;

    public ExposureView(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.left);
        dest.writeInt(this.top);
        dest.writeInt(this.right);
        dest.writeInt(this.bottom);
        dest.writeParcelable(this.bitmap, flags);
    }

    protected ExposureView(Parcel in) {
        this.left = in.readInt();
        this.top = in.readInt();
        this.right = in.readInt();
        this.bottom = in.readInt();
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Parcelable.Creator<ExposureView> CREATOR = new Parcelable
            .Creator<ExposureView>() {
        @Override
        public ExposureView createFromParcel(Parcel source) {
            return new ExposureView(source);
        }

        @Override
        public ExposureView[] newArray(int size) {
            return new ExposureView[size];
        }
    };
}
