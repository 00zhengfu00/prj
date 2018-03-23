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
package com.lswuyou.tv.pm.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bumptech.glide.util.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.common.Constant;
import com.lswuyou.tv.pm.log.AndroidLogger;
import com.lswuyou.tv.pm.log.Logger;
import com.lswuyou.tv.pm.utils.Utils;

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
        }
    }

    private void addHeaders() {
        // 默认头部信息(统一塞入)
        HashMap<String, String> defaultHeader = HeaderUtil.getDefaultNativeHeader();
        for (Map.Entry<String, String> entry : defaultHeader.entrySet()) {
            String name = (String) entry.getKey();
            String value = (String) entry.getValue();
            httpClient.addHeader(name, value);
        }
        // 塞入设备参数
        SharedPreferences preferences = mContext.getSharedPreferences(Constant.ACCOUNT_PF,
                Context.MODE_PRIVATE);
        httpClient.addHeader("screen-width", preferences.getString("widthPixels", "0") + "");
        httpClient.addHeader("screen-height", preferences.getString("heightPixels", "0") + "");
        httpClient.addHeader("dn", preferences.getString("deviceName", "0"));
        httpClient.addHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING, AsyncHttpClient.ENCODING_GZIP);
        DEVICE_ID = preferences.getString("deviceID", "");
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            httpClient.addHeader("pgn", packageInfo.packageName);
            httpClient.addHeader("v", packageInfo.versionName);
            httpClient.addHeader("tct", BaseApplication.getChannel());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        LOGGER.debug("getHeader:screen-width:" + preferences.getString("widthPixels", "0") + "");
        LOGGER.debug("getHeader:screen-height:" + preferences.getString("heightPixels", "0") + "");

    }
}