package com.physicmaster.common;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.physicmaster.base.BaseApplication;

import java.io.File;

public class StorageConfigure {
    public static final String TAG = "StorageConfigure";

    private static Context mContext = BaseApplication.getAppContext();


    public static String getAvatorFilePath() {
        return mContext.getExternalFilesDir(null).toString() + "/avator.jpg";
    }

    public static File getPicFilsPath() {
        if (!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            return null;
        }
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static String generatePicFilePath() {
        String dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        return dir + File.separator + System.currentTimeMillis() + ".jpg";
    }

}
