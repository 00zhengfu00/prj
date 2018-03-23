/*
 * 系统名称：lswuyou
 * 类  名  称：DataServiceBase.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-7 下午4:25:03
 * 功能说明： 数据请求服务基础封装
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.tt.lvruheng.eyepetizer.network;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tt.lvruheng.eyepetizer.log.AndroidLogger;
import com.tt.lvruheng.eyepetizer.log.Logger;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

public abstract class DataServiceBase extends AsyncHttpResponseHandler {
    protected Context mContext;
    protected static Logger LOGGER = AndroidLogger.getLogger();
    public static AsyncHttpClient httpClient = new AsyncHttpClient();
    public static boolean isHeaderAdded;
    private static int mTimeOut = 15 * 1000;
    protected static String DEVICE_ID;

    public DataServiceBase(Context mContext) {
        this.mContext = mContext;
        if (!isHeaderAdded) {
            addHeaders();
            isHeaderAdded = true;
            httpClient.setTimeout(mTimeOut);
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore
                        .getDefaultType());
                trustStore.load(null, null);
                httpClient.setSSLSocketFactory(new SSLSocketFactoryEx(trustStore));
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
    }

    private void addHeaders() {
        // 默认头部信息(统一塞入)
        HashMap<String, String> defaultHeader = HeaderUtil.getDefaultNativeHeader();
        for (Map.Entry<String, String> entry : defaultHeader.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            httpClient.addHeader(name, value);
        }
        // 塞入设备参数R
//        SharedPreferences preferences = mContext.getSharedPreferences(Constant.ACCOUNT_PF,
//                Context.MODE_PRIVATE);
//        httpClient.addHeader("screen-width", BaseApplication.getScreenWidth() + "");
//        httpClient.addHeader("screen-height", BaseApplication.getScreenHeight() + "");
//        httpClient.addHeader("dpr", BaseApplication.getDensity() + "");
//        httpClient.addHeader("dn", preferences.getString("deviceName", "0"));
//        httpClient.addHeader("network", getNetworkState());
//        httpClient.addHeader("channel", getChannel());
//        httpClient.addHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING, AsyncHttpClient.ENCODING_GZIP);
//        DEVICE_ID = BaseApplication.getDeviceID();
    }

    /**
     * 获取网络状态
     */
    private String getNetworkState() {
        if (!isNetworkConnected()) {
            return "unconnected";
        }
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        int nType = info.getType();
        if (ConnectivityManager.TYPE_WIFI == nType) {
            return "wifi";
        } else if (ConnectivityManager.TYPE_MOBILE == nType) {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context
                    .TELEPHONY_SERVICE);
            int networkType = telephonyManager.getNetworkType();
            return getNetworkClassByType(networkType);
        }
        return "unknown";
    }

    /**
     * 获取移动网络的类型
     */
    private String getNetworkClassByType(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2g";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3g";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4g";
            default:
                return "unknown";
        }
    }

    // 检测网络状态
    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    private String getChannel() {
        String channel = "Android";
        ApplicationInfo info = null;
        try {
            info = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (info.metaData == null) {
                return channel;
            }
            channel = info.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }
}