package com.physicmaster;

import java.util.List;

/**
 * Created by huashigen on 2016/12/29.
 */

public class Test {

    /**
     * data : {"bookMenu":[{"e":[{"b":[{"i":6,"n":"七年级上册"},{"i":7,"n":"七年级下册"},{"i":8,"n":"八年级上册"},{"i":9,"n":"八年级下册"},{"i":10,"n":"九年级上册"},{"i":11,"n":"九年级下册"}],"n":"人教版"}],
     * "n":"初中数学"},{"e":[{"b":[{"i":1,"n":"八年级上册"},{"i":2,"n":"八年级下册"},{"i":3,"n":"九年级全册"}],"n":"人教版"}],"n":"初中物理"},{"e":[{"b":[{"i":4,"n":"九年级上册"},{"i":5,"n":"九年级下册"}],
     * "n":"人教版"}],"n":"初中化学"}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<BookMenuBean> bookMenu;

        public static class BookMenuBean {
            /**
             * e : [{"b":[{"i":6,"n":"七年级上册"},{"i":7,"n":"七年级下册"},{"i":8,"n":"八年级上册"},{"i":9,"n":"八年级下册"},{"i":10,"n":"九年级上册"},{"i":11,"n":"九年级下册"}],"n":"人教版"}]
             * n : 初中数学
             */

            public String n;
            public List<EBean> e;

            public static class EBean {
                /**
                 * b : [{"i":6,"n":"七年级上册"},{"i":7,"n":"七年级下册"},{"i":8,"n":"八年级上册"},{"i":9,"n":"八年级下册"},{"i":10,"n":"九年级上册"},{"i":11,"n":"九年级下册"}]
                 * n : 人教版
                 */

                public String n;
                public List<BBean> b;

                public static class BBean {
                    /**
                     * i : 6
                     * n : 七年级上册
                     */

                    public int i;
                    public String n;
                }
            }
        }
    }
}
