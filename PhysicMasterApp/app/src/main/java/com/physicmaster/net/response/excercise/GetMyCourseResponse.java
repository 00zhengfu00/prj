package com.physicmaster.net.response.excercise;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GetMyCourseResponse extends Response {


    /**
     * data : {"myCourseList":[{"chapterId":108,"posterUrl":"http://img.thelper.cn/dt/c/1459086846811ju528x7a.jpg%40320w_95Q_1e_0c_2o.jpg","title":"运动和力"},{"chapterId":112,"posterUrl":"http://img.thelper.cn/dt/c/1459088848106dlyndobz.jpg%40320w_95Q_1e_0c_2o.jpg","title":"简单机械"},{"chapterId":121,"posterUrl":"http://img.thelper.cn/dt/c/1460599126222xadcgxce.jpg%40320w_95Q_1e_0c_2o.jpg","title":"信息的传递"},{"chapterId":114,"posterUrl":"http://img.thelper.cn/dt/c/1460962518263ssdmetoj.jpg%40320w_95Q_1e_0c_2o.jpg","title":"内能的利用"},{"chapterId":122,"posterUrl":"http://img.thelper.cn/dt/c/1461137880694gwfny5hq.jpg%40320w_95Q_1e_0c_2o.jpg","title":"能源与可持续发展"},{"chapterId":102,"posterUrl":"http://img.thelper.cn/dt/c/14613037291671xrserre.jpg%40320w_95Q_1e_0c_2o.jpg","title":"声现象"},{"chapterId":103,"posterUrl":"http://img.thelper.cn/dt/c/1461305535200sf1w6btp.jpg%40320w_95Q_1e_0c_2o.jpg","title":"物态变化"},{"chapterId":105,"posterUrl":"http://img.thelper.cn/dt/c/1461319381290ov9ykpg7.jpg%40320w_95Q_1e_0c_2o.jpg","title":"透镜及其应用"},{"chapterId":107,"posterUrl":"http://img.thelper.cn/dt/c/14615700433405jh9y2nt.jpg%40320w_95Q_1e_0c_2o.jpg","title":"力"},{"chapterId":115,"posterUrl":"http://img.thelper.cn/dt/c/1461921152312wpmby4rk.jpg%40320w_95Q_1e_0c_2o.jpg","title":"电流和电路"},{"chapterId":116,"posterUrl":"http://img.thelper.cn/dt/c/1461921349780zxjpjrwx.jpg%40320w_95Q_1e_0c_2o.jpg","title":"电压和电阻"},{"chapterId":106,"posterUrl":"http://img.thelper.cn/dt/c/1462504176704ut8rbths.jpg%40320w_95Q_1e_0c_2o.jpg","title":"质量与密度"},{"chapterId":104,"posterUrl":"http://img.thelper.cn/dt/c/1463369994976lxncm3eo.jpg%40320w_95Q_1e_0c_2o.jpg","title":"光现象"},{"chapterId":113,"posterUrl":"http://img.thelper.cn/dt/c/14633777067777s93d0pb.jpg%40320w_95Q_1e_0c_2o.jpg","title":"热和能"},{"chapterId":119,"posterUrl":"http://img.thelper.cn/dt/c/1463379653963wcmnbtom.jpg%40320w_95Q_1e_0c_2o.jpg","title":"生活用电"},{"chapterId":120,"posterUrl":"http://img.thelper.cn/dt/c/1463382190803gled2rqw.jpg%40320w_95Q_1e_0c_2o.jpg","title":"电与磁"},{"chapterId":101,"posterUrl":"http://img.thelper.cn/dt/c/1463455466941gqllozus.jpg%40320w_95Q_1e_0c_2o.jpg","title":"机械运动"},{"chapterId":118,"posterUrl":"http://img.thelper.cn/dt/c/1463455653124cf5yakh3.jpg%40320w_95Q_1e_0c_2o.jpg","title":"电功率"},{"chapterId":117,"posterUrl":"http://img.thelper.cn/dt/c/1463561976196kehreejr.jpg%40320w_95Q_1e_0c_2o.jpg","title":"欧姆定律"},{"chapterId":164,"posterUrl":"http://img.thelper.cn/dt/c/1463648496920m0bfha9d.jpg%40320w_95Q_1e_0c_2o.jpg","title":"中考冲刺"}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<MyCourseListBean> myCourseList;

        public static class MyCourseListBean {
            /**
             * chapterId : 108
             * posterUrl : http://img.thelper.cn/dt/c/1459086846811ju528x7a.jpg%40320w_95Q_1e_0c_2o.jpg
             * title : 运动和力
             */

            public int chapterId;
            public String posterUrl;
            public String title;
        }
    }
}
