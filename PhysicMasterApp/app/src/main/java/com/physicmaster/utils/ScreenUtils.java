
package com.physicmaster.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;


public class ScreenUtils {

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        Context context = BaseApplication.getAppContext();
        DisplayMetrics dm = new DisplayMetrics();
        //		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        Context context = BaseApplication.getAppContext();
        DisplayMetrics dm = new DisplayMetrics();
        //		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕像素密度Dpi
     */
    public static int getScreendensityDpi(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    /**
     * 获取x方向屏幕每英寸像素
     */
    public static float getScreenWidthDpi(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.xdpi;
    }

    /**
     * 获取y方向屏幕每英寸像素
     */
    public static float getScreenHeightDpi(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.ydpi;
    }

    /**
     * 获取屏幕像素密度
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 获取屏幕参数
     */
    public static DisplayMetrics getScreenParam(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    //获取颜色的状态选择器
    public static ColorStateList getColorStateList(int resId) {
        return BaseApplication.getAppContext().getResources().getColorStateList(resId);
    }

    //获取Drawable
    public static Drawable getDrawable(int resId) {
        return BaseApplication.getAppContext().getResources().getDrawable(resId);

    }

    //获取字符串数组
    public static String[] getStringArray(int resId) {
        return BaseApplication.getAppContext().getResources().getStringArray(resId);
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        Context context = BaseApplication.getAppContext();
        int result = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
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
        return (int) Math.ceil(px / getScreenDensity(context));
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
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.scaledDensity;
    }

    public static int px2sp(float scaledDensity, int px) {
        return (int) Math.ceil(px / scaledDensity);
    }

    public static int mm2px(Context context, int mm) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, mm, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * 用在图片尺寸为16:9的ImageView adjustViewBounds 获取最大高度
     *
     * @param context
     * @return
     */
    public static int get16_9ImageMaxHeight(Context context, int marginDp) {
        int screenWidth = getScreenWidth();
        int margin = dp2px(context, marginDp);
        int imageWidth = screenWidth - margin;
        int maxHeight = imageWidth * 9 / 16;
        return maxHeight;
    }

    /**
     * 用在图片尺寸为3:4的ImageView adjustViewBounds 获取最大高度
     *
     * @param context
     * @return
     */
    public static int get3_4ImageMaxHeight(Context context, int marginDp) {
        int screenWidth = getScreenWidth();
        int margin = dp2px(context, marginDp);
        int imageWidth = screenWidth - margin;
        int maxHeight = imageWidth * 4 / 3;
        return maxHeight;
    }
}
