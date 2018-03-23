package com.physicmaster.utils;

/**
 * Util for Array
 *
 * @author wlf(Andy)
 * @email 411086563@qq.com
 */
public class ArrayUtil {

    /**
     * Returns true if the array is null or 0-length.
     *
     * @param array the array to be examined
     * @return true if array is null or zero length
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}