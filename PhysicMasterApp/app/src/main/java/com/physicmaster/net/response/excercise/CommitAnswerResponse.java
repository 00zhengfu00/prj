package com.physicmaster.net.response.excercise;

import com.physicmaster.net.response.Response;

import java.io.Serializable;

/**
 * Created by songrui on 16/11/29.
 */

public class CommitAnswerResponse extends Response {

    /**
     * data : {"useType":1,"exploreResult":{"correctCount":1,"existExample":5,"starLevel":0,"timeConsuming":"00:13","videoId":1001}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * useType : 1
         * exploreResult : {"correctCount":1,"existExample":5,"starLevel":0,"timeConsuming":"00:13","videoId":1001}
         */

        public int useType;
        public int isFinished;
        public ExploreResultBean exploreResult;

        public static class ExploreResultBean implements Serializable {
            /**
             * correctCount : 1
             * existExample : 5
             * starLevel : 0
             * timeConsuming : 00:13
             * videoId : 1001
             */

            public int correctCount;
            public int    questionCount;
            public int    starLevel;
            public String timeConsuming;
            public int    videoId;
            public int    awardGoldCoin;
            public int    awardPoint;
        }
    }
}
