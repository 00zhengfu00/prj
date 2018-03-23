package com.iask.yiyuanlegou1.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.DeviceUuidFactory;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.network.respose.account.UserInfo;
import com.iask.yiyuanlegou1.network.respose.product.CacheShoppingCarData;
import com.iask.yiyuanlegou1.network.respose.product.ShoppingCartBean;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;

/**
 * Created by huazai on 22/04/16.
 */
public class BaseApplication extends Application {
    private static Context context;
    private static int mScreenWidth;
    private static int mScreenHeight;
    private static String deviceID;
    private String userId;
    private static BaseApplication instance;
    private String packageName;
    public static CacheShoppingCarData shoppingCarData;

    @Override
    public void onCreate() {
        context = this;
        super.onCreate();
        initDeviceData();

        UserInfo info = (UserInfo) CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, UserInfo.class);
        if (info != null) {
            getInstance().setUserId(info.getUserId() + "");
        }
        //bugly 初始化
        CrashReport.initCrashReport(getApplicationContext(), "900033517", false);
        //获取缓存订单
        getCachedShoppingCarData();
    }

    private static void getCachedShoppingCarData() {
        Object list = CacheManager.getObject(CacheManager.TYPE_USER_INFO,
                CacheKeys.SHOPPING_CAR, CacheShoppingCarData.class);
        if (list != null) {
            getInstance().shoppingCarData = (CacheShoppingCarData) list;
        } else {
            getInstance().shoppingCarData = new CacheShoppingCarData();
            getInstance().shoppingCarData.shoppingCartBeans = new ArrayList<ShoppingCartBean>();
        }
    }

    public static void setShoppingCarData(CacheShoppingCarData shoppingCarData) {
//        if (shoppingCarData == null || shoppingCarData.shoppingCartBeans == null) {
//            return;
//        }
//        List<ShoppingCartBean> shoppingCartBeans = shoppingCarData.shoppingCartBeans;
//        getInstance().shoppingCarData.shoppingCartBeans.clear();
        if (shoppingCarData == null) {
            getInstance().shoppingCarData.shoppingCartBeans.clear();
        } else {
            getInstance().shoppingCarData = shoppingCarData;
        }
    }

    public static CacheShoppingCarData getShoppingCarData() {
        CacheShoppingCarData data = getInstance().shoppingCarData;
        if (data == null) {
            getInstance().shoppingCarData = new CacheShoppingCarData();
            getInstance().shoppingCarData.shoppingCartBeans = new ArrayList<ShoppingCartBean>();
        }
        return getInstance().shoppingCarData;
    }

    public static void saveCachedShoppingCarData() {
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.SHOPPING_CAR, getInstance
                ().shoppingCarData);
    }

    public static synchronized BaseApplication getInstance() {
        if (instance == null) {
            instance = new BaseApplication();
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        instance.userId = userId;
    }

    public static Context getAppContext() {
        return context;
    }

    private void initDeviceData() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        SharedPreferences preferences = getSharedPreferences(Constant.ACCOUNT_PF, MODE_PRIVATE);
        String uuid = "";
        if (preferences.getBoolean("first", true)) {
            SharedPreferences.Editor edit = preferences.edit();
            uuid = new DeviceUuidFactory(this).getDeviceUuid().toString();
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
        deviceID = uuid;
    }

    static public int getScreenWidth() {
        return mScreenWidth;
    }

    static public int getScreenHeight() {
        return mScreenHeight;
    }

    static public String getDeviceID() {
        return deviceID;
    }
}
