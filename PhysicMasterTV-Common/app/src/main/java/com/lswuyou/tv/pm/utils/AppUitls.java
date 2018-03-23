//package com.lswuyou.tv.pm.utils;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import static com.dataeye.channel.tv.DCChannelAgent.getProcessName;
//
//
///**
// * Created by weizhilei on 16/7/28.
// */
//public class AppUitls {
//    /**
//     * 是否是默认进程
//     *
//     * @param context
//     * @return
//     */
//    public static boolean isDefaultProcess(Context context) {
//        if (null != context) {
//            String packName = context.getPackageName();
//            String processName = getProcessName(context, android.os.Process.myPid());
//            if (!TextUtils.isEmpty(processName) && packName.equals(processName)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
