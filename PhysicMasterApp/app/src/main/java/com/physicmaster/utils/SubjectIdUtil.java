package com.physicmaster.utils;

/**
 * Created by huashigen on 2017/5/26.
 */

public class SubjectIdUtil {
    /**
     * 科目ID转科目类型
     *
     * @param subjectId
     * @return
     */
    public static int subjectId2Type(int subjectId) {
        if (1 == subjectId) {
            return 51;
        } else if (2 == subjectId) {
            return 52;
        } else if (4 == subjectId) {
            return 54;
        } else {
            return 0;
        }
    }

    /**
     * 年级ID转学年
     *
     * @param eduGrade
     * @return
     */
    public static int eduGrade2Year(int eduGrade) {
        if (1 == eduGrade || 2 == eduGrade) {
            return 107;
        } else if (3 == eduGrade || 4 == eduGrade) {
            return 108;
        } else if (5 == eduGrade || 6 == eduGrade) {
            return 109;
        } else {
            return 0;
        }
    }
}
