package com.iask.yiyuanlegou1.utils;



import android.content.Context;
import android.widget.Toast;

public class UIUtils {
	/** 弹出String类型的文本 */
	private static long lastTimeInMillis;
	private static String lastText;

	public static void showToast(Context mContext, String text) {
		if (!isShowMsg(text)) {
			return;
		}
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	/** 弹出资源ID类型的文本 */
	public static void showToast(Context mContext, int text) {
		if (text <= 0) {
			return;
		}
		String txt = mContext.getResources().getString(text);
		if (!isShowMsg(txt)) {
			return;
		}
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	/** 判断是否显示此toast信息-空信息不显示；重复信息不显示 */
	private static boolean isShowMsg(String text) {
		if (StringUtils.isEmpty(text)) {
			return false;
		}
		long curTimeInMillis = System.currentTimeMillis();
		if (0L != lastTimeInMillis) {
			if ((curTimeInMillis - lastTimeInMillis) < 1000 && lastText.equals(text)) {
				lastTimeInMillis = curTimeInMillis;
				return false;
			}
		}
		lastTimeInMillis = curTimeInMillis;
		lastText = text;
		return true;
	}
}
