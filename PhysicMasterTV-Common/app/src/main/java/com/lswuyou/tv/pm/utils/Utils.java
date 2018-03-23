package com.lswuyou.tv.pm.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;

import static com.lswuyou.tv.pm.common.CacheKeys.USERINFO_LOGINVO;

/**
 * Created by Administrator on 2016/8/18.
 */
public class Utils {
    public static boolean isUserLogined() {
        return BaseApplication.getLoginUserInfo() != null;
    }

    /**
     * 判断应用程序是否安装 (检测可能会出现问题，程序的包存在，但是程序不可用)
     */
    public static boolean checkApkExist(Context context, String packageName) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info != null)
                return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return false;
    }

    /**
     * 获取渠道名称
     *
     * @param mContext
     * @return
     */
    public static String getChannel(Context mContext) {
        try {
            ApplicationInfo ai = mContext.getPackageManager().getApplicationInfo(mContext
                    .getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                String channel = ai.metaData.getString("UMENG_CHANNEL");
                return channel;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @param mContext
     * @return
     */
    public static String getVersion(Context mContext) {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext
                    .getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0";
    }
    //渠道长度不超过16位
    // 1：阿里TV；2：大麦；3：当贝；4：沙发；5：海信；6：欢网；7：酷开；8：乐视；9：小米；10：易视腾；11 步步高
    public static int getChannelId(Context context) {
        String channel = getChannel(context);
        if (channel.equals("dangbei")) {
            return 3;
        } else if (channel.equals("none")) {
            return 0;
        } else if (channel.equals("bbk")) {
            return 11;
        } else if (channel.equals("damai")) {
            return 2;
        } else if (channel.equals("aliplay")) {
            return 1;
        } else if (channel.equals("shafa")) {
            return 4;
        } else if (channel.equals("haixin")) {
            return 5;
        } else if (channel.equals("huanwang")) {
            return 6;
        } else if (channel.equals("kukai")) {
            return 7;
        } else if (channel.equals("leshi")) {
            return 8;
        } else if (channel.equals("xiaomi")) {
            return 9;
        } else if (channel.equals("yishiteng")) {
            return 10;
        }
        return 0;
    }
}
