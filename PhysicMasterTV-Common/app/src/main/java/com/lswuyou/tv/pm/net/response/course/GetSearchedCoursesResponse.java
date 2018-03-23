package com.lswuyou.tv.pm.net.response.course;

import com.lswuyou.tv.pm.net.response.Response;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse.DataBean.BookListBean.ItemListBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class GetSearchedCoursesResponse extends Response {

    public DataBean data;

    public static class DataBean {
        public List<ItemListBean> chapterList;
        public List<ItemListBean> videoList;
    }
}
