package com.lswuyou.tv.pm.net.response.account;

import com.lswuyou.tv.pm.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2017-09-08.
 */

public class GetFavListResponse extends Response {

    public DataBean data;

    public static class DataBean {
        public List<BookListBean> bookList;

        public static class BookListBean {

            public String name;
            public List<ItemListBean> itemList;

            public static class ItemListBean {
                /**
                 * posterUrl : http://img.thelper.cn/dt/c/1485075072743usrkabso.jpg
                 * title : 长度的单位
                 * videoId : 1362
                 */
                public int postitionType = -1;
                public static final int FIRST_ITEM = 1;
                public static final int LAST_ITEM = 2;
                public static final int FIRSTBOTTEM_ITEM = 3;
                public static final int LASTBOTTEM_ITEM = 4;
                public static final int BOTTOM_ITEM = 5;
                public int tvVideoType;
                public String posterUrl;
                public String title;
                public int videoId;
            }
        }
    }
}
