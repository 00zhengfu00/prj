package com.physicmaster.modules.account;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.GetMyInfoService;

/**
 * Created by huashigen on 2017-08-25.
 */

public class GetUserInfoService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserInfo();
            }
        }, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        GetMyInfoService service = new GetMyInfoService(this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                loginAfterAction(data.data);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
            }
        });
        service.postLogined("", false);
    }

    /**
     * 刷新成功操作
     *
     * @param data
     */
    private void loginAfterAction(UserDataResponse.UserDataBean data) {
        data.loginVo.isLoginByPhone = true;
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_USERKEY, data.keyVo);
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, data.loginVo);
        BaseApplication.setUserData(data.loginVo);
        BaseApplication.setUserKey(data.keyVo);
    }
}
