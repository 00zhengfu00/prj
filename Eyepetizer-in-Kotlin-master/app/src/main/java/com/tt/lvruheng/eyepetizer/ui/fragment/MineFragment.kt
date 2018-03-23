package com.tt.lvruheng.eyepetizer.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.TextUtils
import android.view.View
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.base.BaseApplication
import com.tt.lvruheng.eyepetizer.common.cache.CacheKeys
import com.tt.lvruheng.eyepetizer.common.cache.CacheManager
import com.tt.lvruheng.eyepetizer.network.IOpenApiDataServiceCallback
import com.tt.lvruheng.eyepetizer.network.ServiceURL
import com.tt.lvruheng.eyepetizer.network.response.IdentityResponse
import com.tt.lvruheng.eyepetizer.network.service.GetIdentityService
import com.tt.lvruheng.eyepetizer.ui.MainActivity
import com.tt.lvruheng.eyepetizer.ui.SettingsActivity
import com.tt.lvruheng.eyepetizer.ui.WebViewActivity
import com.tt.lvruheng.eyepetizer.utils.Constant.IDENTITY_SUCCESS
import kotlinx.android.synthetic.main.mine_fragment.*

/**
 * Created by huashigen on 2018/1/5.
 */
class MineFragment : BaseFragment() {

    override fun getLayoutResources(): Int {
        return R.layout.mine_fragment
    }

    override fun initView() {
        ll_settings.setOnClickListener({
            val intent = Intent()
            intent.setClass(context, SettingsActivity::class.java)
            startActivity(intent)
        })
        ll_bankcard.setOnClickListener({
            val intent = Intent()
            intent.setClass(context, WebViewActivity::class.java)//http://192.168.1.128:5656/#/bank/bankManager?backReg=1 银行卡管理
            intent.putExtra("url", "${ServiceURL.URL}#/bank/bankManager?backReg=1")
            startActivity(intent)
        })
        ll_idcard.setOnClickListener({
            val intent = Intent()
            intent.setClass(context, WebViewActivity::class.java)//http://192.168.1.128:5656/#/identityAuth?backReg=1 身份认证
            intent.putExtra("url", "${ServiceURL.URL}/#/identityAuth?backReg=1")
            startActivity(intent)
        })
        activity.registerReceiver(identitySuccReceiver, IntentFilter(IDENTITY_SUCCESS))
    }

    val identitySuccReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            getUserInfo()
        }
    }

    /**
     * 获取认证成功后的用户信息
     */
    fun getUserInfo() {
        val getIdentityService = GetIdentityService(context)
        getIdentityService.callback = object : IOpenApiDataServiceCallback<IdentityResponse> {
            override fun onGetData(data: IdentityResponse?) {
                if (!TextUtils.isEmpty(data!!.data.name)) {
                    val loginInfo = BaseApplication.loginInfo
                    loginInfo!!.idNo = data!!.data.idNo
                    loginInfo!!.userName = data!!.data.name
                    tvName.visibility = View.VISIBLE
                    tvName.text = loginInfo.userName
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, loginInfo)
                }
            }

            override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
            }

        }
        val token = BaseApplication.loginInfo!!.logonTokenKey
        getIdentityService.postLogined("token=${token}", true)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (identitySuccReceiver != null) {
            activity.unregisterReceiver(identitySuccReceiver)
        }
    }

    override fun onResume() {
        super.onResume()
        if (BaseApplication.loginInfo != null) {
            val loginInfo = BaseApplication.loginInfo
            val phone = loginInfo!!.mobileNo
            try {
                var secretPhone = phone.replaceRange(3, 7, "****")
                tvPhone.text = secretPhone
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (!TextUtils.isEmpty(loginInfo.userName)) {
                tvName.visibility = View.VISIBLE
                tvName.text = loginInfo.userName
            } else {
                tvName.visibility = View.GONE
            }
        } else {
            (activity as MainActivity).gotoMainPage()
        }
    }
}