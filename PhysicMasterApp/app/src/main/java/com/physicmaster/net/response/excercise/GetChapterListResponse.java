package com.physicmaster.net.response.excercise;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2017-07-05.
 */

public class GetChapterListResponse extends Response {

    public DataBean data;

    public static class DataBean {

        public int selectChapterId;
        public List<ChapterListBean> chapterList;

        public static class ChapterListBean {

            public int chapterId;
            public String name;
            public String progress;
        }
    }
}
