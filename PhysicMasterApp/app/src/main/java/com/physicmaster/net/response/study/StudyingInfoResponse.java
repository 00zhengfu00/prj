package com.physicmaster.net.response.study;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2017-10-16.
 */

public class StudyingInfoResponse extends Response {

    public DataBean data;

    public static class DataBean {
        public int bookId;
        public String selectChapterName;
        public int selectChapterId;
        public String subjectName;
        public String editionName;
        public String bookName;
    }
}
