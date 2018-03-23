package com.physicmaster.net.response.notebook;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GetAllTagResponse extends Response {


    /**
     * data : {"quWrongDirs":[{"appUserQuWrongDirDtoList":[{"dirId":10011,"dirName":"光学"},{"dirId":10010,"dirName":"力学"},{"dirId":10007,"dirName":"自然界的水"}],"subjectId":1,"subjectName":"数学"},{"appUserQuWrongDirDtoList":[{"dirId":10012,"dirName":"硫酸"},{"dirId":10008,"dirName":"盐酸"}],"subjectId":2,"subjectName":"物理"},{"appUserQuWrongDirDtoList":[{"dirId":10009,"dirName":"函数"}],"subjectId":4,"subjectName":"化学"}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<QuWrongDirsBean> quWrongDirs;

        public static class QuWrongDirsBean {
            /**
             * appUserQuWrongDirDtoList : [{"dirId":10011,"dirName":"光学"},{"dirId":10010,"dirName":"力学"},{"dirId":10007,"dirName":"自然界的水"}]
             * subjectId : 1
             * subjectName : 数学
             */

            public int subjectId;
            public String                             subjectName;
            public List<AppUserQuWrongDirDtoListBean> appUserQuWrongDirDtoList;

            public static class AppUserQuWrongDirDtoListBean {
                /**
                 * dirId : 10011
                 * dirName : 光学
                 */

                public int dirId;
                public String dirName;
            }
        }
    }
}
