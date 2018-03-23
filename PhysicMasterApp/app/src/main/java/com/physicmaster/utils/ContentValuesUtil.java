package com.physicmaster.utils;

import android.content.ContentValues;

/**
 * Util for {@link ContentValues}
 *
 * @author wlf(Andy)
 * @datetime 2015-11-14 17:53 GMT+8
 * @email 411086563@qq.com
 */
public class ContentValuesUtil {

    /**
     * Returns true if the values is null or 0-length.
     *
     * @param values the values to be examined
     * @return true if values is null or zero length
     */
    public static boolean isEmpty(ContentValues values) {
        return values == null || values.size() == 0;
    }
}