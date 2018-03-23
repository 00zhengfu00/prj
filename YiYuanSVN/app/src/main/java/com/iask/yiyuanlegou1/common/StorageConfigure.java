package com.iask.yiyuanlegou1.common;

import android.content.Context;
import android.os.Environment;


import com.iask.yiyuanlegou1.base.BaseApplication;

import java.io.File;

public class StorageConfigure {
	private static Context mContext = BaseApplication.getAppContext();

	public static String getAvatorFilePath(){
		return mContext.getExternalFilesDir(null).toString() + "/avator.jpg";
	}
	
	public static String getPicFilsPath() {
		return mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
	}
	
	public static String generatePicFilePath(){
		String dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
		return dir + File.separator + System.currentTimeMillis() + ".jpg";
	}

}
