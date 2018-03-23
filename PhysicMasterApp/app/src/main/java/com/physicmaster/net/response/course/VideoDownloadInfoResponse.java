package com.physicmaster.net.response.course;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2017/3/23.
 */

public class VideoDownloadInfoResponse extends Response {

    public DataBean data;

    public static class DataBean {

        public VideoDownloadVoBean videoDownloadVo;

        public static class VideoDownloadVoBean {

            public long expiresAtTime;
            public String m3u8Content;
            public String posterUrl;
            public int timeLength;
            public int videoId;
            public String videoQuality;
            public String videoTitle;
            public int videoType;
            public List<String> tsUrls;
        }
    }
}
