package com.physicmaster.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.account.LoginDialogActivity;
import com.physicmaster.modules.study.fragment.dialogfragment.CommonDialogFragment;

/**
 * Created by Administrator on 2016/8/18.
 */
public class Utils {
    public static boolean isUserLogined() {
        return BaseApplication.getUserData() != null;
    }

    /**
     * 判断应用程序是否安装 (检测可能会出现问题，程序的包存在，但是程序不可用)
     */
    public static boolean checkApkExist(Context context, String packageName) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.MATCH_UNINSTALLED_PACKAGES);
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
     * 获取应用名称
     *
     * @return
     */
    public static String getAppName(Context context) {
        String packageName = context.getPackageName();
        if (packageName.equals(Constant.PHYSICMASTER)) {
            return "wulidashi";
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            return "huaxuedashi";
        } else if (packageName.equals(Constant.MATHMASTER)) {
            return "shuxuedashi";
        }
        return "dashifm";
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

    public static void gotoLogin(Activity context) {
//        CommonDialogFragment loginFragment = new CommonDialogFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("title", "请以注册账号登录");
//            bundle.putString("note", "您目前是游客身份");
//            bundle.putString("action", "去登录");
//            loginFragment.setArguments(bundle);
//            loginFragment.setOnActionBtnClickListener(new CommonDialogFragment.OnActionBtnClickListener() {
//                @Override
//                public void onLick() {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//            });
//            loginFragment.show(getFragmentManager(), "login");
        context.startActivity(new Intent(context, LoginDialogActivity.class));
        context.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.fade_out);
    }
}
