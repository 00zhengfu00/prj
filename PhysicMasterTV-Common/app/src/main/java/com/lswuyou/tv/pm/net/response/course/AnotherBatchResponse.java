package com.lswuyou.tv.pm.net.response.course;

import com.lswuyou.tv.pm.net.response.Response;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse.DataBean.BookListBean.ItemListBean;

import java.util.List;

/**
 * Created by huashigen on 2018-02-03.
 */

public class AnotherBatchResponse extends Response {
    public DataBean data;

    public static class DataBean {
        public List<ItemListBean> banner;
    }
}
