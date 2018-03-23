package com.physicmaster.utils;

/**
 * Created by songrui on 16/11/23.
 */

public class SubjectNameUtil {

    public static String gradeSelected(int grade) {
        if (grade == 1) {
            return "八年级上";
        } else if (grade == 2) {
            return "八年级上";
        } else if (grade == 3) {
            return "八年级上";
        } else if (grade == 4) {
            return "八年级下";
        } else if (grade == 5) {
            return "九年级上";
        } else if (grade == 6) {
            return "九年级下";
        } else if (grade == 107) {
            return "七年级";
        } else if (grade == 108) {
            return "八年级";
        } else if (grade == 109) {
            return "九年级";
        } else {
            return "未知";
        }

    }


    public static int updataGrade(String grade) {
        if ("七年级上".equals(grade)) {
            return 1;
        } else if ("七年级下".equals(grade)) {
            return 2;
        } else if ("八年级上".equals(grade)) {
            return 3;
        } else if ("八年级下".equals(grade)) {
            return 4;
        } else if ("九年级上".equals(grade)) {
            return 5;
        } else if ("九年级下".equals(grade)) {
            return 6;
        } else if ("七年级".equals(grade)) {
            return 107;
        } else if ("八年级".equals(grade)) {
            return 108;
        } else if ("九年级".equals(grade)) {
            return 109;
        } else {
            return 0;
        }
    }

    /**
     * 根据subjectType获得学科名称
     * @param subjectType
     * @return
     */
    public static String getSubjectName(int subjectType) {
        if (51 == subjectType) {
            return "物理";
        } else if (52 == subjectType) {
            return "化学";
        } else if (54 == subjectType) {
            return "数学";
        }
        return "其他";
    }

    /**
     * 根据年级id获得年级名称
     * @param eduGradeYear
     * @return
     */
    public static String getEduGradeYearName(int eduGradeYear) {
        if (eduGradeYear == 107) {
            return "七年级";
        } else if (eduGradeYear == 108) {
            return "八年级";
        } else if (eduGradeYear == 109) {
            return "九年级";
        }
        return "其他";
    }
}
