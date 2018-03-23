package com.physicmaster.net.response.notebook;

import com.physicmaster.net.response.Response;

/**
 * Created by huashigen on 2018-01-05.
 */

public class InitWrongPoolResponse extends Response {

    /**
     * data : {"replacement1":"","replacement2":"<script type=\"text/javascript\">window.WRONG_REASON_LIST =                            //错题原因                               [
     * {                                   \"id\":1,                                                                           //原因tagid
     * \"n\":\"概念模糊\"                                                                  //原因名称                                 },...                               ];window
     * .SUBJECT_LIST =                                                                 //科目错题目录列表                               [                                 {
     * \"c\":62,                                                                          // 科目下错题池中错题数量                                    \"dirs\":
     * // 科目下错题目录列表                                       [                                          {                                            \"id\":10059,
     * // 错题目录ID                                             \"n\":\"牛顿第三定律\"                                                    // 错题目录名称
     * },...                                       ],                                    \"id\":1,                                                                          //
     * 科目类型,1为初中物理、2为初中化学、4为初中数学                                    \"n\":\"初中物理\"                                                                 // 科目名称
     * },...                                ];<\/script>"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * replacement1 :
         * replacement2 : <script type="text/javascript">window.WRONG_REASON_LIST =                            //错题原因                               [
         * {                                   "id":1,                                                                           //原因tagid
         * "n":"概念模糊"                                                                  //原因名称                                 },...                               ];window
         * .SUBJECT_LIST =                                                                 //科目错题目录列表                               [                                 {
         * "c":62,                                                                          // 科目下错题池中错题数量                                    "dirs":
         * // 科目下错题目录列表                                       [                                          {                                            "id":10059,
         * // 错题目录ID                                             "n":"牛顿第三定律"                                                    // 错题目录名称
         * },...                                       ],                                    "id":1,                                                                          //
         * 科目类型,1为初中物理、2为初中化学、4为初中数学                                    "n":"初中物理"                                                                 // 科目名称
         * },...                                ];</script>
         */

        public String replacement1;
        public String replacement2;
    }
}
