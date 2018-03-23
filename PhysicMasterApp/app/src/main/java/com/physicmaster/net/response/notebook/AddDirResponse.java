package com.physicmaster.net.response.notebook;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2018-01-25.
 */

public class AddDirResponse extends Response {

    /**
     * code : 200
     * data : {"quWrongDirs":[{"appUserQuWrongDirDtoList":[{"dirId":10161,"dirName":"huh"},{"dirId":10172,"dirName":"hhah"},{"dirId":10175,"dirName":"hjj"}],"subjectId":2,
     * "subjectName":"初中化学"}]}
     */
    public DataBean data;

    public static class DataBean {
        public List<QuWrongDirsBean> quWrongDirs;

        public static class QuWrongDirsBean {

            /**
             * appUserQuWrongDirDtoList : [{"dirId":10161,"dirName":"huh"},{"dirId":10172,"dirName":"hhah"},{"dirId":10175,"dirName":"hjj"}]
             * subjectId : 2
             * subjectName : 初中化学
             */

            public int subjectId;
            public String subjectName;
            public List<AppUserQuWrongDirDtoListBean> appUserQuWrongDirDtoList;

            public static class AppUserQuWrongDirDtoListBean {

                /**
                 * dirId : 10161
                 * dirName : huh
                 */
                public int dirId;
                public String dirName;
            }
        }
    }
}
