package com.tt.lvruheng.eyepetizer.ui

import android.content.Intent
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.base.BaseApplication
import com.tt.lvruheng.eyepetizer.common.cache.CacheKeys
import com.tt.lvruheng.eyepetizer.common.cache.CacheManager
import com.tt.lvruheng.eyepetizer.network.IOpenApiDataServiceCallback
import com.tt.lvruheng.eyepetizer.network.Response
import com.tt.lvruheng.eyepetizer.network.service.LogoutService
import com.tt.lvruheng.eyepetizer.utils.showToast
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.title_style_2.*

class SettingsActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun initView() {
        tvTitle.text = "设置"
        tvChangePwd.setOnClickListener({
            val intent = Intent()
            intent.setClass(this, ChangePwdActivity::class.java)
            startActivity(intent)
        })
        tvAbout.setOnClickListener({
            val intent = Intent()
            intent.setClass(this, AboutActivity::class.java)
            startActivity(intent)
        })
        ivBack.setOnClickListener({
            finish()
        })
        ivBack.setImageResource(R.drawable.back_black)
        btnLogout.setOnClickListener({
            logout()
        })
    }

    /**
     * 退出登录
     */
    private fun logout() {
        val logoutService = LogoutService(this)
        logoutService.callback = object : IOpenApiDataServiceCallback<Response> {
            override fun onGetData(data: Response?) {
                showToast(data!!.retMsg)
                clearLoginInfo()
                finish()
            }

            override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
                showToast(errorMsg!!)
            }

        }
        val token = BaseApplication.loginInfo!!.logonTokenKey
        logoutService.postLogined("token=${token}", true)
    }

    /**
     * 清除登录信息
     */
    private fun clearLoginInfo() {
        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO)
        BaseApplication.loginInfo = null
    }
}
