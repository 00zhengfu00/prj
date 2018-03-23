package cn.cloudwalk.libproject.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.widget.Toast;

/**
 * 用于处理用户界面工具类
 * Created by Cuke Pie on 13-6-24.
 */
public class UIUtils {


  //    @TargetApi(11)
  //    public static void enableStrictMode() {
  //        if (UIUtils.hasGingerbread()) {
  //            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
  //                    .detectAll().penaltyLog();
  //            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
  //                    .detectAll().penaltyLog();
  //
  //            if (UIUtils.hasHoneycomb()) {
  //                threadPolicyBuilder.penaltyFlashScreen();
  //                vmPolicyBuilder.setClassInstanceLimit(CameraActivity.class, 1);
  //            }
  //            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
  //            StrictMode.setVmPolicy(vmPolicyBuilder.build());
  //        }
  //    }

  public static boolean hasFroyo() {
    // Can use static final constants like FROYO, declared in later versions
    // of the OS since they are inlined at compile time. This is guaranteed
    // behavior.
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
  }
  public static int getSdkVersion() {
	    return Build.VERSION.SDK_INT;
	}
  public static boolean hasGingerbread() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
  }

  public static boolean hasHoneycomb() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
  }

  public static boolean hasIceCreamSandwich() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
  }

  public static boolean hasHoneycombMR1() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
  }

  public static boolean hasHoneycombMR2() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
  }

  public static boolean hasJellyBean() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
  }

  public static boolean hasJellyBeanMR1() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
  }

  public static boolean hasJellyBeanMR2() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
  }

  public static boolean hasKitkat() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
  }
//  public static boolean hasM() {
//	    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
//	  }
  private UIUtils() {
  }
/**
 * checkDeviceHasNavigationBar:是否有navigation bar <br/>
 * 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar<br/>
 *
 * @author:284891377   Date: 2016/10/18 0018 11:24
 *
 * @since JDK 1.7
 */
  public static boolean checkDeviceHasNavigationBar(Context activity) {

    //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
    boolean hasMenuKey = ViewConfiguration.get(activity)
            .hasPermanentMenuKey();
    boolean hasBackKey = KeyCharacterMap
            .deviceHasKey(KeyEvent.KEYCODE_BACK);

    if (!hasMenuKey && !hasBackKey) {
      // 做任何你需要做的,这个设备有一个导航栏
      return true;
    }
    return false;
  }
/**
 * getNavigationBarHeight:获取navigation bar高度. <br/>
 * @author:284891377   Date: 2016/10/18 0018 11:25
 *
 * @since JDK 1.7
 */
  public static int getNavigationBarHeight(Activity activity) {
    Resources resources = activity.getResources();
    int resourceId = resources.getIdentifier("navigation_bar_height",
            "dimen", "android");
    //获取NavigationBar的高度
    int height = resources.getDimensionPixelSize(resourceId);
    return height;
  }

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
    if (TextUtils.isEmpty(text)) {
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
