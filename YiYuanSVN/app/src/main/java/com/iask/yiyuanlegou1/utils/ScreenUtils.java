
package com.iask.yiyuanlegou1.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.iask.yiyuanlegou1.base.BaseApplication;


public class ScreenUtils {

	/** 获取屏幕宽度 */
	public static int getScreenWidth() {
		Context context = BaseApplication.getAppContext();
		DisplayMetrics dm = new DisplayMetrics();
//		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/** 获取屏幕高度 */
	public static int getScreenHeight() {
		Context context = BaseApplication.getAppContext();
		DisplayMetrics dm = new DisplayMetrics();
//		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/** 获取屏幕像素密度Dpi */
	public static int getScreendensityDpi(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.densityDpi;
	}

	/** 获取x方向屏幕每英寸像素 */
	public static float getScreenWidthDpi(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.xdpi;
	}

	/** 获取y方向屏幕每英寸像素 */
	public static float getScreenHeightDpi(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.ydpi;
	}

	/** 获取屏幕像素密度 */
	public static float getScreendensity(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	/** 获取屏幕参数 */
	public static DisplayMetrics getScreenParam(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	/** dp转px */
	public static int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}
	
	public static int getStatusBarHeight() {
		Context context = BaseApplication.getAppContext();
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
		      result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static int dp2px(float density, int dp) {
		if (dp == 0) {
			return 0;
		}
		return (int) (dp * density + 0.5f);

	}

	public static int px2dp(Context context, int px) {
		return (int) Math.ceil(px / getScreendensity(context));
	}

	public static int sp2px(Context context, float sp) {
		if (sp == 0) {
			return 0;
		}
		return (int) (sp * GetScaledDensity(context) + 0.5f);
	}

	private static float GetScaledDensity(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
//		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.scaledDensity;
	}

	public static int px2sp(float scaledDensity, int px) {
		return (int) Math.ceil(px / scaledDensity);
	}

	public static int mm2px(Context context, int mm) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, mm, context.getResources().getDisplayMetrics()) + 0.5f);
	}

}
