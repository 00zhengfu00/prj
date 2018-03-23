package com.lswuyou.tv.pm.net.response.homepage;

import com.lswuyou.tv.pm.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2017-09-06.
 */

public class ChapterListResponse extends Response {

    public DataBean data;

    public static class DataBean {
        public List<BookListBean> bookList;

        public static class BookListBean {

            public String name;
            public List<ItemListBean> itemList;

            public static class ItemListBean {
                public int chapterId;
                public int videoId;
                public int tvVideoType;
                public String poster;
                public String title;
                public int postitionType = -1;
                public static final int FIRST_ITEM = 1;
                public static final int LAST_ITEM = 2;
                public static final int FIRSTBOTTEM_ITEM = 3;
                public static final int LASTBOTTEM_ITEM = 4;
                public static final int BOTTOM_ITEM = 5;
            }
        }
    }
}
