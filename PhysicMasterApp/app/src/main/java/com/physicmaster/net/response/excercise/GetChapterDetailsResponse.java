package com.physicmaster.net.response.excercise;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2017-07-05.
 */

public class GetChapterDetailsResponse extends Response {

    public DataBean data;

    public static class DataBean {
        public int shipState;
        public int energyValue;
        public int maxEnergyValue;
        public ProgressCompletionBean progressCompletion;
        public ChapterStudyBean chapterStudy;

        public static class ProgressCompletionBean {

            public int completeCount;
            public int maxTargetCount;
            public List<ProgressListBean> progressList;

            public static class ProgressListBean implements Parcelable {

                public int awardGoldCoin;
                public int awardPoint;
                public int awardPropCount;
                public String awardPropUrl;
                public int completeCount;
                public int isAward;
                public int progressLevel;
                public int targetCount;
                public int userChapterProgressId;

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
                    dest.writeInt(this.userChapterProgressId);
                }

                public ProgressListBean() {
                }

                protected ProgressListBean(Parcel in) {
                    this.awardGoldCoin = in.readInt();
                    this.awardPoint = in.readInt();
                    this.awardPropCount = in.readInt();
                    this.awardPropUrl = in.readString();
                    this.completeCount = in.readInt();
                    this.isAward = in.readInt();
                    this.progressLevel = in.readInt();
                    this.targetCount = in.readInt();
                    this.userChapterProgressId = in.readInt();
                }

                public static final Parcelable.Creator<ProgressListBean> CREATOR = new Parcelable.Creator<ProgressListBean>() {
                    @Override
                    public ProgressListBean createFromParcel(Parcel source) {
                        return new ProgressListBean(source);
                    }

                    @Override
                    public ProgressListBean[] newArray(int size) {
                        return new ProgressListBean[size];
                    }
                };
            }
        }

        public static class ChapterStudyBean {

            public int chapterId;
            public String name;
            public int subjectId;
            public List<DeepListBean> deepList;
            public List<PreviewListBean> previewList;
            public List<ReviewListBean> reviewList;
            public int downloadAllow;

            public static class DeepListBean extends BaseInfo {
                /**
                 * hasQu : 0
                 * starStatus : 0
                 * title : 测试干货视频二
                 * videoId : 2492
                 */
                public int hasQu;
                public int videoId;
                public int starLevel;
            }

            public static class BaseInfo {
                public int position = 0;
                public int starStatus;
                public int studyHere;
                public String title;
                public String name;
                public String posterUrl;
                public static final int POSITION_LEFT = 1;
                public static final int POSITION_RIGHT = 0;
                public static final int STAR_STATUS_LOCK = 0;
                public static final int STAR_STATUS_UNLIGHT = 1;
                public static final int STAR_STATUS_LIGHT1 = 2;
                public static final int STAR_STATUS_LIGHT2 = 3;
            }

            public static class PreviewListBean extends BaseInfo {
                /**
                 * hasQu : 1
                 * starLevel : 0
                 * starStatus : 0
                 * title : 科学之旅
                 * videoId : 1491
                 */

                public int hasQu;
                public int starLevel;
                public int videoId;
            }

            public static class ReviewListBean extends BaseInfo {
                /**
                 * name : 基础习题视频
                 * starStatus : 0
                 */
                public List<ExcerciseVideoBean> videoItemList;
            }

            public static class ExcerciseVideoBean implements Serializable {
                public String title;
                public int videoId;
                public int isWatch;
                public String posterUrl;
            }
        }
    }
}
