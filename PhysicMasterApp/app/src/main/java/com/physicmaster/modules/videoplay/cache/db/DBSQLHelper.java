package com.physicmaster.modules.videoplay.cache.db;

import android.content.Context;


/**
 * Created by huashigen on 2017/3/28.
 */

public class DBSQLHelper {
    private static DownloadFileDbHelper mInstance = null;

    public synchronized static DownloadFileDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DownloadFileDbHelper(context);
        }
        return mInstance;
    }
}
