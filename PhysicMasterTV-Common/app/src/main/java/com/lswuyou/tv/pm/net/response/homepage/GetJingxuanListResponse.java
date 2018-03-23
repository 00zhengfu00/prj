package com.lswuyou.tv.pm.net.response.homepage;

import com.lswuyou.tv.pm.net.response.Response;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse.DataBean.BookListBean.ItemListBean;

import java.util.List;

/**
 * Created by huashigen on 2017-09-06.
 */

public class GetJingxuanListResponse extends Response {

    public DataBean data;

    public static class DataBean {
        public List<ChoiceListBean> choiceList;
        public List<ItemListBean> banner;

        public static class ChoiceListBean {
            public String subjectName;
            public List<ItemListBean> videoList;
        }
    }
}
