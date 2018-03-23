package com.physicmaster.net.response.course;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2016/11/26.
 */
public class NewVideoPlayResponse extends Response {

    /**
     * data : {"newVideo":{"m3u8Content":"#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:20\n#EXT-X-MEDIA-SEQUENCE:0\n#EXTINF:15.440000,\nhttp://app.video.lswuyou.com/9055191c903a802fc094b979feb56f0f/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00001.ts\n#EXTINF:5.680000,\nhttp://app.video.lswuyou.com/5b01cd01eede22f5b0a983e65568a5cb/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00002.ts\n#EXTINF:9.000000,\nhttp://app.video.lswuyou.com/b01ab5634f320e1c1f1ba6c9d772601c/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00003.ts\n#EXTINF:17.320000,\nhttp://app.video.lswuyou.com/f95762f03ad745f6b8ebd9228e04658e/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00004.ts\n#EXTINF:8.920000,\nhttp://app.video.lswuyou.com/93274e5c473b02b12d368eefd72e654b/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00005.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/a9cbafa8abd7edbc72c1b59dd7970914/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00006.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/be37184ca7c26b948cfc912e1193649e/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00007.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/de51f2cfc7ddf1a5f1badaff16c9a255/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00008.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/d6589d0200c381bb9488a7a03d01413f/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00009.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/e89e6f5f581a2724375967b1ff1ad4cc/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00010.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/d497238c2e56d8c102a9d4832ea9ad7b/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00011.ts\n#EXTINF:5.200000,\nhttp://app.video.lswuyou.com/0d78e293dcf40ee4e548a091fe0a4a2d/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00012.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/1b96d4f784b98a84aa015e61bfb313cb/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00013.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/4b6cc9dd849dc9ea1840a62811552be1/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00014.ts\n#EXTINF:15.160000,\nhttp://app.video.lswuyou.com/52826b12cb2c68a20357477aff79d42a/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00015.ts\n#EXTINF:6.880000,\nhttp://app.video.lswuyou.com/feba4147c2068e8f145b83db771de690/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00016.ts\n#EXTINF:6.800000,\nhttp://app.video.lswuyou.com/1b907d41d267298940c42d5ce37f7e38/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00017.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/a0dcff67dd87dccad4ee1875e9fe61e6/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00018.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/e071f61504e3bcabe7382c058d5c6f63/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00019.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/ab90c4c24ba4f9dad41b67b7402a3610/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00020.ts\n#EXTINF:19.240000,\nhttp://app.video.lswuyou.com/88d1ffd90ead8854e39f69a03af67b50/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00021.ts\n#EXTINF:4.280000,\nhttp://app.video.lswuyou.com/57aee9e1eea5b6472d016e269cbe72a1/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00022.ts\n#EXT-X-ENDLIST\n","posterUrl":"http://img.thelper.cn/dt/c/1477639550200locppmk0.jpg","timeLength":224,"videoId":1001,"videoQuality":"HD","videoTitle":"力"}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * newVideo : {"m3u8Content":"#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:20\n#EXT-X-MEDIA-SEQUENCE:0\n#EXTINF:15.440000,\nhttp://app.video.lswuyou.com/9055191c903a802fc094b979feb56f0f/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00001.ts\n#EXTINF:5.680000,\nhttp://app.video.lswuyou.com/5b01cd01eede22f5b0a983e65568a5cb/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00002.ts\n#EXTINF:9.000000,\nhttp://app.video.lswuyou.com/b01ab5634f320e1c1f1ba6c9d772601c/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00003.ts\n#EXTINF:17.320000,\nhttp://app.video.lswuyou.com/f95762f03ad745f6b8ebd9228e04658e/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00004.ts\n#EXTINF:8.920000,\nhttp://app.video.lswuyou.com/93274e5c473b02b12d368eefd72e654b/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00005.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/a9cbafa8abd7edbc72c1b59dd7970914/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00006.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/be37184ca7c26b948cfc912e1193649e/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00007.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/de51f2cfc7ddf1a5f1badaff16c9a255/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00008.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/d6589d0200c381bb9488a7a03d01413f/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00009.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/e89e6f5f581a2724375967b1ff1ad4cc/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00010.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/d497238c2e56d8c102a9d4832ea9ad7b/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00011.ts\n#EXTINF:5.200000,\nhttp://app.video.lswuyou.com/0d78e293dcf40ee4e548a091fe0a4a2d/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00012.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/1b96d4f784b98a84aa015e61bfb313cb/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00013.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/4b6cc9dd849dc9ea1840a62811552be1/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00014.ts\n#EXTINF:15.160000,\nhttp://app.video.lswuyou.com/52826b12cb2c68a20357477aff79d42a/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00015.ts\n#EXTINF:6.880000,\nhttp://app.video.lswuyou.com/feba4147c2068e8f145b83db771de690/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00016.ts\n#EXTINF:6.800000,\nhttp://app.video.lswuyou.com/1b907d41d267298940c42d5ce37f7e38/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00017.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/a0dcff67dd87dccad4ee1875e9fe61e6/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00018.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/e071f61504e3bcabe7382c058d5c6f63/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00019.ts\n#EXTINF:10.000000,\nhttp://app.video.lswuyou.com/ab90c4c24ba4f9dad41b67b7402a3610/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00020.ts\n#EXTINF:19.240000,\nhttp://app.video.lswuyou.com/88d1ffd90ead8854e39f69a03af67b50/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00021.ts\n#EXTINF:4.280000,\nhttp://app.video.lswuyou.com/57aee9e1eea5b6472d016e269cbe72a1/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00022.ts\n#EXT-X-ENDLIST\n","posterUrl":"http://img.thelper.cn/dt/c/1477639550200locppmk0.jpg","timeLength":224,"videoId":1001,"videoQuality":"HD","videoTitle":"力"}
         */

        public NewVideoBean newVideo;

        public static class NewVideoBean {
            /**
             * m3u8Content : #EXTM3U
             #EXT-X-VERSION:3
             #EXT-X-TARGETDURATION:20
             #EXT-X-MEDIA-SEQUENCE:0
             #EXTINF:15.440000,
             http://app.video.lswuyou.com/9055191c903a802fc094b979feb56f0f/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00001.ts
             #EXTINF:5.680000,
             http://app.video.lswuyou.com/5b01cd01eede22f5b0a983e65568a5cb/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00002.ts
             #EXTINF:9.000000,
             http://app.video.lswuyou.com/b01ab5634f320e1c1f1ba6c9d772601c/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00003.ts
             #EXTINF:17.320000,
             http://app.video.lswuyou.com/f95762f03ad745f6b8ebd9228e04658e/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00004.ts
             #EXTINF:8.920000,
             http://app.video.lswuyou.com/93274e5c473b02b12d368eefd72e654b/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00005.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/a9cbafa8abd7edbc72c1b59dd7970914/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00006.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/be37184ca7c26b948cfc912e1193649e/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00007.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/de51f2cfc7ddf1a5f1badaff16c9a255/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00008.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/d6589d0200c381bb9488a7a03d01413f/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00009.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/e89e6f5f581a2724375967b1ff1ad4cc/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00010.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/d497238c2e56d8c102a9d4832ea9ad7b/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00011.ts
             #EXTINF:5.200000,
             http://app.video.lswuyou.com/0d78e293dcf40ee4e548a091fe0a4a2d/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00012.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/1b96d4f784b98a84aa015e61bfb313cb/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00013.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/4b6cc9dd849dc9ea1840a62811552be1/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00014.ts
             #EXTINF:15.160000,
             http://app.video.lswuyou.com/52826b12cb2c68a20357477aff79d42a/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00015.ts
             #EXTINF:6.880000,
             http://app.video.lswuyou.com/feba4147c2068e8f145b83db771de690/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00016.ts
             #EXTINF:6.800000,
             http://app.video.lswuyou.com/1b907d41d267298940c42d5ce37f7e38/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00017.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/a0dcff67dd87dccad4ee1875e9fe61e6/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00018.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/e071f61504e3bcabe7382c058d5c6f63/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00019.ts
             #EXTINF:10.000000,
             http://app.video.lswuyou.com/ab90c4c24ba4f9dad41b67b7402a3610/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00020.ts
             #EXTINF:19.240000,
             http://app.video.lswuyou.com/88d1ffd90ead8854e39f69a03af67b50/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00021.ts
             #EXTINF:4.280000,
             http://app.video.lswuyou.com/57aee9e1eea5b6472d016e269cbe72a1/5962E4C5/1/1001/hd/1458619639542neef2gqenh-00022.ts
             #EXT-X-ENDLIST

             * posterUrl : http://img.thelper.cn/dt/c/1477639550200locppmk0.jpg
             * timeLength : 224
             * videoId : 1001
             * videoQuality : HD
             * videoTitle : 力
             */

            public String m3u8Content;
            public String posterUrl;
            public int    timeLength;
            public int    videoId;
            public String videoQuality;
            public String videoTitle;
        }
    }
}
