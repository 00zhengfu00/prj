package com.physicmaster.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.physicmaster.common.Constant;

/**
 * Created by huashigen on 2017/3/20.
 */

public class NetworkUtils {
    /**
     * 获取网络状态
     */
    public static String getNetworkState(Context context) {
        if (!isNetworkConnected(context)) {
            return Constant.NETTYPE_UNCONNECTED;
        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return Constant.NETTYPE_UNKNOWN;
        }
        int nType = info.getType();
        if (ConnectivityManager.TYPE_WIFI == nType) {
            return Constant.NETTYPE_WIFI;
        } else if (ConnectivityManager.TYPE_MOBILE == nType) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                    .TELEPHONY_SERVICE);
            int networkType = telephonyManager.getNetworkType();
            return getNetworkClassByType(networkType);
        }
        return Constant.NETTYPE_UNKNOWN;
    }

    /**
     * 获取移动网络的类型
     */
    public static String getNetworkClassByType(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return Constant.NETTYPE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return Constant.NETTYPE_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return Constant.NETTYPE_4G;
            default:
                return Constant.NETTYPE_UNKNOWN;
        }
    }

    // 检测网络状态
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService
                (Context
                        .CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }
}
