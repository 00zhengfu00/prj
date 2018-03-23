package com.physicmaster.modules.mine.activity.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2017-07-19.
 */

public class Book implements Serializable {

    public int id;
    public String name;
    public List<BooksBean> books;

    public static class BooksBean {
        /**
         * bookId : 6
         * name : 七年级上册
         */

        public int bookId;
        public String name;
    }
}
