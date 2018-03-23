package com.physicmaster.net.response.excercise;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GetCollectionResponse extends Response {


    /**
     * data : {"videoList":{"xtlist":[{"posterurl":"http://img.thelper.cn/dt/c/14737578552393tj9ulq4.jpg","title":"信息的传递1-基础经典习题","videoId":1158,"videoType":2}],"zsdlist":[{"posterurl":"http://img.thelper.cn/dt/c/1472627926802kqpd3ypg.jpg","title":"电磁波的海洋","videoId":1028,"videoType":1},{"posterurl":"http://img.thelper.cn/dt/c/1472627937042n321bsgx.jpg","title":"信息之路","videoId":1157,"videoType":1},{"posterurl":"http://img.thelper.cn/dt/c/14726258483888mmbkqmp.jpg","title":"温度","videoId":1226,"videoType":1},{"posterurl":"http://img.thelper.cn/dt/c/14726258714012arhbcpr.jpg","title":"熔化和凝固","videoId":1227,"videoType":1}]}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * videoList : {"xtlist":[{"posterurl":"http://img.thelper.cn/dt/c/14737578552393tj9ulq4.jpg","title":"信息的传递1-基础经典习题","videoId":1158,"videoType":2}],"zsdlist":[{"posterurl":"http://img.thelper.cn/dt/c/1472627926802kqpd3ypg.jpg","title":"电磁波的海洋","videoId":1028,"videoType":1},{"posterurl":"http://img.thelper.cn/dt/c/1472627937042n321bsgx.jpg","title":"信息之路","videoId":1157,"videoType":1},{"posterurl":"http://img.thelper.cn/dt/c/14726258483888mmbkqmp.jpg","title":"温度","videoId":1226,"videoType":1},{"posterurl":"http://img.thelper.cn/dt/c/14726258714012arhbcpr.jpg","title":"熔化和凝固","videoId":1227,"videoType":1}]}
         */

        public VideoListBean videoList;

        public static class VideoListBean {
            public List<ItemlistBean>  xtlist;
            public List<ItemlistBean> zsdlist;
            public List<ItemlistBean> deepList;

            public static class ItemlistBean {
                /**
                 * posterurl : http://img.thelper.cn/dt/c/14737578552393tj9ulq4.jpg
                 * title : 信息的传递1-基础经典习题
                 * videoId : 1158
                 * videoType : 2
                 */

                public String posterurl;
                public String title;
                public int    videoId;
                public int    videoType;
            }


        }
    }
}
