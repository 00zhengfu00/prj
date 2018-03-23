package com.physicmaster.net.response.course;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by huashigen on 2016/11/26.
 */
public class CourseExcerciseWraper {
    public CourseStudyVo appCourseStudyVo;
    public ProgressCompletionBean progressCompletion;
    public NeedBuyCourseVoBean needBuyCourseVo;

    public static class ProgressCompletionBean {
        public List<ProgressBean> progressList;
        public int completeCount;//6,　　　　//目前完成数
        public int maxTargetCount;//:6,　　　 //总进度数
    }

    public static class ProgressBean implements Parcelable {
        public int awardGoldCoin;//金币奖励
        public int awardPoint;//积分奖励
        public int awardPropCount;//道具奖励
        public String awardPropUrl;//道具图片
        public int completeCount;//本次关卡完成度　　　completeCount==targetCount时表示可以领奖
        public int isAward;//是否领奖　
        public int progressLevel;//关卡等级
        public int targetCount;//本次关卡目标数
        public int userCourseProgressId;//领取奖品id

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.awardGoldCoin);
            dest.writeInt(this.awardPoint);
            dest.writeInt(this.awardPropCount);
            dest.writeString(this.awardPropUrl);
            dest.writeInt(this.completeCount);
            dest.writeInt(this.isAward);
            dest.writeInt(this.progressLevel);
            dest.writeInt(this.targetCount);
            dest.writeInt(this.userCourseProgressId);
        }

        public ProgressBean() {
        }

        protected ProgressBean(Parcel in) {
            this.awardGoldCoin = in.readInt();
            this.awardPoint = in.readInt();
            this.awardPropCount = in.readInt();
            this.awardPropUrl = in.readString();
            this.completeCount = in.readInt();
            this.isAward = in.readInt();
            this.progressLevel = in.readInt();
            this.targetCount = in.readInt();
            this.userCourseProgressId = in.readInt();
        }

        public static final Parcelable.Creator<ProgressBean> CREATOR = new Parcelable.Creator<ProgressBean>() {
            @Override
            public ProgressBean createFromParcel(Parcel source) {
                return new ProgressBean(source);
            }

            @Override
            public ProgressBean[] newArray(int size) {
                return new ProgressBean[size];
            }
        };
    }
    public static class NeedBuyCourseVoBean {
        /**
         * courseId : 149
         * price : 1
         * priceYuan : 0.01
         * title : 九年级全册
         */

        public int courseId;
        public int price;
        public String priceYuan;
        public String title;
    }
}
