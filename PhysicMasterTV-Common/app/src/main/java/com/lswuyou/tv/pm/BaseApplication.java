/*
 * 系统名称：lswuyou
 * 类  名  称：BaseApplication.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-7-28 上午10:50:36
 * 功能说明： Application 基础类
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.lswuyou.tv.pm;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.dangbei.euthenia.manager.DangbeiAdManager;
import com.lswuyou.tv.pm.activity.BaseActivityManager;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.common.Constant;
import com.lswuyou.tv.pm.common.DeviceUuidFactory;
import com.lswuyou.tv.pm.common.SystemParams;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.account.StartUp0;
import com.lswuyou.tv.pm.utils.Utils;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

import java.util.List;


public class BaseApplication extends DefaultApplicationLike {

    private static int mScreenWidth;
    private static int mScreenHeight;

    private static String channel;
    private static Context mContext = null;
    //app启动初始数据
    private static StartUp0 startUpData;

    private static LoginUserInfo loginUserInfo;
    //    public static MiAppInfo appInfo;
    public static boolean aliplayInit = false;

    public BaseApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent
            tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks
                                                          callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplication();
        initDeviceData();
//        Bugly.init(mContext, Constant.APPKEY, true);
        CrashReport.initCrashReport(mContext, Constant.APPKEY, false);
        //沙发初始化
//        TVPayment.init(this, "57fb4ab5", "74f229a5b360747705c15b50ad74ff7f");
        //使用小米账户初始化
        //xiaomiInit();
        leshiInit();
        //当贝广告
        DangbeiAdManager.init(mContext, "7B132019D2D2C5B1", "6jarfNdtuHRfhxQg2KKAXcABsCsP2jPVKGGyruYHE5MqTTG5", getChannel());
    }

    public static StartUp0 getStartUpData() {
        return startUpData;
    }

    public static void setStartUpData(StartUp0 startUpData) {
        BaseApplication.startUpData = startUpData;
    }

    public static LoginUserInfo getLoginUserInfo() {
        return loginUserInfo;
    }

    public static void setLoginUserInfo(LoginUserInfo loginUserInfo) {
        BaseApplication.loginUserInfo = loginUserInfo;
    }

    public static boolean checkIsLogin() {
        LoginUserInfo info = (LoginUserInfo) CacheManager.getObject(CacheManager.TYPE_USER_INFO,
                CacheKeys.USERINFO_LOGINVO, LoginUserInfo.class);
        if (null != info) {
            return true;
        }
        return false;
    }

    private String getUserId() {
        LoginUserInfo loginInfo = (LoginUserInfo) CacheManager.getObject(CacheManager
                .TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, LoginUserInfo.class);
        if (null != loginInfo) {
            return loginInfo.dtUserId + "";
        }
        return "";
    }


    static public Context getAppContext() {
        return mContext;
    }

    public static void quitApplication() {
        BaseActivityManager.getInstance().popAllActivity();
        CacheManager.reset();
    }

    private void initDeviceData() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getAppContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        SharedPreferences preferences = getAppContext().getSharedPreferences(Constant.ACCOUNT_PF, Context.MODE_PRIVATE);
        String uuid = "";
        if (preferences.getBoolean("first", true)) {
            Editor edit = preferences.edit();
            uuid = new DeviceUuidFactory(getAppContext()).getDeviceUuid().toString();

            edit.putString("deviceID", uuid);
            edit.putString("widthPixels", metrics.widthPixels + "");
            edit.putString("heightPixels", metrics.heightPixels + "");
            edit.putString("density", metrics.density + "");
            edit.putString("deviceName", Build.MODEL);
            edit.putBoolean("first", false);
            edit.commit();
        } else {
            uuid = preferences.getString("deviceID", "");
        }
        SystemParams.setScreenWidth(metrics.widthPixels);
        SystemParams.setScreenHeight(metrics.heightPixels);
        SystemParams.setDesity(metrics.density);
        SystemParams.setDeviceName(Build.MODEL);
        SystemParams.setDeviceID(uuid);

        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        channel = Utils.getChannel(getAppContext());
        LoginUserInfo userDataBean = (LoginUserInfo) CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, LoginUserInfo.class);
        if (userDataBean != null) {
            loginUserInfo = userDataBean;
        }
    }

    static public int getScreenWidth() {
        return mScreenWidth;
    }

    static public int getScreenHeight() {
        return mScreenHeight;
    }

    private boolean shouldXiaoMiInit() {
        ActivityManager am = ((ActivityManager) getAppContext().getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getAppContext().getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static String getChannel() {
        return channel;
    }

    private void xiaomiInit() {
//        appInfo = new MiAppInfo();
//        appInfo.setAppId("2882303761517518622");
//        appInfo.setAppKey("5161751817622");
//        MiCommplatform.Init(this, appInfo);
    }

    /**
     * 乐视sdk初始化
     */
    private void leshiInit() {
        //Android6.0必须动态申请权限,不能在此处初始化,需要权限申请完成之后才能初始化
        //请参考MainActivity中的权限申请
//        if (Build.VERSION.SDK_INT < 23) {
//            //请在应用进程中调用
//            if (AppUitls.isDefaultProcess(this)) {
////              LetvSdk.getLetvSDk().setDebug(false);
//                LetvSdk.getLetvSDk().init(this);
//            }
//        }
    }
}
