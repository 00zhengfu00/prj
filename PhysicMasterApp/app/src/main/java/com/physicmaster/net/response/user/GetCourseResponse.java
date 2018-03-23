package com.physicmaster.net.response.user;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GetCourseResponse extends Response  {

    /**
     * data : {"courses":[{"courseId":111,"posterUrl":"http://img.thelper.cn/dt/c/1460599126222xadcgxce.jpg"},{"courseId":112,"posterUrl":"http://img.thelper.cn/dt/c/1460962518263ssdmetoj.jpg"},{"courseId":113,"posterUrl":"http://img.thelper.cn/dt/c/1461137880694gwfny5hq.jpg"},{"courseId":118,"posterUrl":"http://img.thelper.cn/dt/c/1461921152312wpmby4rk.jpg"},{"courseId":119,"posterUrl":"http://img.thelper.cn/dt/c/1461921349780zxjpjrwx.jpg"},{"courseId":122,"posterUrl":"http://img.thelper.cn/dt/c/14633777067777s93d0pb.jpg"},{"courseId":123,"posterUrl":"http://img.thelper.cn/dt/c/1463379653963wcmnbtom.jpg"},{"courseId":124,"posterUrl":"http://img.thelper.cn/dt/c/1463382190803gled2rqw.jpg"},{"courseId":126,"posterUrl":"http://img.thelper.cn/dt/c/1463455653124cf5yakh3.jpg"},{"courseId":127,"posterUrl":"http://img.thelper.cn/dt/c/1463561976196kehreejr.jpg"}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<CoursesBean> courses;

        public static class CoursesBean {
            /**
             * courseId : 111
             * posterUrl : http://img.thelper.cn/dt/c/1460599126222xadcgxce.jpg
             */

            public int courseId;
            public String posterUrl;
            public String title;
        }
    }
}
