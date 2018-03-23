package com.physicmaster.net.response.video;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2017-07-25.
 */

public class LikeVideoPlayResponse extends Response {

    public DataBean data;

    public static class DataBean {

        public VideoBean video;

        public static class VideoBean {

            public String m3u8Content;
            public String posterUrl;
            public int timeLength;
            public int videoId;
            public String videoQuality;
            public String videoTitle;
        }
    }
}
