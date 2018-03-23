package com.tt.lvruheng.eyepetizer.ui

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.base.BaseApplication
import com.tt.lvruheng.eyepetizer.common.cache.CacheKeys
import com.tt.lvruheng.eyepetizer.common.cache.CacheManager
import com.tt.lvruheng.eyepetizer.network.IOpenApiDataServiceCallback
import com.tt.lvruheng.eyepetizer.network.LOGIN_SUCCESS
import com.tt.lvruheng.eyepetizer.network.response.LoginResponse
import com.tt.lvruheng.eyepetizer.network.service.LoginService
import com.tt.lvruheng.eyepetizer.utils.showToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.title_style_1.*

class LoginActivity : BaseActivity() {
    var phone: String? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        phone = intent.getStringExtra("mobileNum")
        tvCancel.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvTitle.text = "登录"

        etPwd.setTextChangeListener {
            btnNext.isEnabled = !TextUtils.isEmpty(it) && it.length >= 6
            if (!TextUtils.isEmpty(it)) {
                ivClear.visibility = View.VISIBLE
            } else {
                ivClear.visibility = View.GONE
            }
        }

        btnNext.setOnClickListener({
            val pwd = etPwd.text.toString()
            val loginService = LoginService(this)
            loginService.callback = object : IOpenApiDataServiceCallback<LoginResponse> {
                override fun onGetData(data: LoginResponse?) {
                    if (data!!.data.login.equals("0")) {
                        showToast("登录成功")
                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, data!!.data)
                        BaseApplication.loginInfo = data.data
                        sendBroadcast(Intent(LOGIN_SUCCESS))
                        val intent = Intent()
                        intent.setClass(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        showToast(data.retMsg)
                    }
                }

                override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
                    showToast(errorMsg!!)
                }

            }
            loginService.postLogined("mobileNo=${phone}&passwd=${pwd}", true)
        })

        ivClear.setOnClickListener({
            etPwd.setText("")
        })

        ivBack.setOnClickListener({
            finish()
        })

        tvForgetPwd.setOnClickListener({
            val intent = Intent()
            intent.setClass(this, ResetPwdActivity::class.java)
            intent.putExtra("mobileNum", phone)
            startActivity(intent)
        })
    }

    private fun EditText.setTextChangeListener(body: (key: String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                body(s.toString())
            }
        })
    }
}
