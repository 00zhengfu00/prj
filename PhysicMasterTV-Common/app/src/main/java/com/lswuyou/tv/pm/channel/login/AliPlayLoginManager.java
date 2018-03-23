//package com.lswuyou.tv.pm.channel.login;
//
//import com.de.aligame.api.AliTvSdk;
//import com.de.aligame.core.api.AliBaseError;
//import com.de.aligame.core.api.Listeners.IAuthListener;
//import com.de.aligame.core.api.Listeners.IGetUserinfoListener;
//import com.de.aligame.core.api.Listeners.UserInfo;
//
//
//import android.util.Log;
//
//public class AliPlayLoginManager implements IAuthListener {
//    private static String TAG = AliPlayLoginManager.class.getSimpleName();
//
//    /**
//     * 检查是否已经登录，不会弹出登录框
//     */
//    public static void checkAuthWithoutLogin() {
//        boolean isAuth = AliTvSdk.Account.isAuth();
//        if (isAuth) {
//        } else {
//        }
//    }
//
//    /**
//     * 登录授权。如果没有登录，会弹出扫码登录框
//     */
//    public static void checkAuthAndLogin() {
//        boolean isAuth = AliTvSdk.Account.checkAuthAndLogin();
//        //已授权，则获取用户信息
//        if (isAuth) {
//        } else {
//        }
//    }
//
//    public static void getUserInfo(IGetUserinfoListener listener) {
//        AliTvSdk.Account.getUserInfo(listener);
//    }
//
//    public static void changeAccount() {
//        AliTvSdk.Account.changeAccount();
//    }
//
//    @Override
//    public void onAuthSucess(int from) {
//        Log.d(TAG, "onAuthSucess. from=" + from);
//    }
//
//    @Override
//    public void onAuthError(int errCode, String errMsg) {
//        Log.d(TAG, "onAuthError:" + errCode + " errMsg:" + errMsg);
//        if (errCode == AliBaseError.INT_ERROR_AUTH_OTHER) {
//        }
//    }
//
//    @Override
//    public void onLogout() {
//    }
//
//    @Override
//    public void onAuthCancel() {
//    }
//}
