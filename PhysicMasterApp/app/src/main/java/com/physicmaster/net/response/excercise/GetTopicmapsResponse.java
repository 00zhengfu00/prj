package com.physicmaster.net.response.excercise;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GetTopicmapsResponse extends Response {


    /**
     * data : {"appQuestionWrongVoList":[{"chapterIcon":"http://img.thelper.cn/icon/chapter/102.png","chapterName":"声现象","defaultChapterid":102,"wrongTotal":1}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<AppQuestionWrongVoListBean> wrongChapterList;

        public static class AppQuestionWrongVoListBean {
            /**
             * chapterIcon : http://img.thelper.cn/icon/chapter/102.png
             * chapterName : 声现象
             * defaultChapterid : 102
             * wrongTotal : 1
             */

            public String chapterIcon;
            public String chapterName;
            public int    chapterId;
            public int    wrongTotal;
        }
    }
}
