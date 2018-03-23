package com.lswuyou.tv.pm.net.response.course;

import com.lswuyou.tv.pm.net.response.Response;
import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class GetChapterResponse extends Response implements Serializable {

    public DataBean data;

    public static class DataBean {

        public ChapterDetailsBean chapterDetails;

        public static class ChapterDetailsBean implements Serializable{

            public int chapterId;
            public String name;
            public int subjectId;
            public List<VideoInfo> deepList;
            public List<VideoInfo> hangshiList;
            public List<VideoInfo> jichuList;
            public List<VideoInfo> previewList;
            public List<VideoInfo> tigaoList;
        }
    }
}
