package com.tt.lvruheng.eyepetizer.base

import android.app.Application
import android.content.Context
import com.tt.lvruheng.eyepetizer.common.cache.CacheKeys
import com.tt.lvruheng.eyepetizer.common.cache.CacheManager
import com.tt.lvruheng.eyepetizer.network.response.LoginInfo

/**
 * Created by huashigen on 2018-01-25.
 */
class BaseApplication : Application() {

    companion object {
        var context: Context? = null
        var loginInfo: LoginInfo? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        var loginData = CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, LoginInfo::class.java)
        if (loginData != null) {
            loginInfo = loginData as LoginInfo
        }
    }

    fun Companion.getAppContext(): Context {
        return context!!
    }

    fun Companion.getLoginInfo(): LoginInfo {
        return loginInfo!!
    }
}