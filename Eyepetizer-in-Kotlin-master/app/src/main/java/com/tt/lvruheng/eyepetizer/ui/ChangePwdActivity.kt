package com.tt.lvruheng.eyepetizer.ui

import android.content.Intent
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.base.BaseApplication
import com.tt.lvruheng.eyepetizer.common.cache.CacheKeys
import com.tt.lvruheng.eyepetizer.common.cache.CacheManager
import com.tt.lvruheng.eyepetizer.network.IOpenApiDataServiceCallback
import com.tt.lvruheng.eyepetizer.network.Response
import com.tt.lvruheng.eyepetizer.network.response.ChangePwdResponse
import com.tt.lvruheng.eyepetizer.network.service.ChangePwdService
import com.tt.lvruheng.eyepetizer.utils.showToast
import kotlinx.android.synthetic.main.activity_change_pwd.*
import kotlinx.android.synthetic.main.title_style_1.*

class ChangePwdActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_change_pwd
    }

    override fun initView() {
        tvTitle.text = "重设密码"
        ivBack.setOnClickListener({
            finish()
        })
        btnNext.setOnClickListener({
            val passwd = etPwd.text.toString()
            val verifyCode = etOldPwd.text.toString()
            val token = BaseApplication.loginInfo!!.logonTokenKey
            val changePwdService = ChangePwdService(this)
            changePwdService.callback = object : IOpenApiDataServiceCallback<ChangePwdResponse> {
                override fun onGetData(data: ChangePwdResponse?) {
                    if (data!!.data.login.equals("0")) {
                        showToast(data!!.retMsg)
                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, data!!.data)
                        BaseApplication.loginInfo = data.data
                        finish()
                    } else {
                        showToast(data!!.retMsg)
                    }
                }

                override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
                    showToast(errorMsg!!)
                }

            }
            changePwdService.postLogined("oldPasswd=${verifyCode}&newPasswd=${passwd}&token=${token}", true)
        })
        etPwd.setTextChangeListener {
            btnNext.isEnabled = !TextUtils.isEmpty(etOldPwd.text) && !TextUtils.isEmpty(it) && it.length >= 6
            if (!TextUtils.isEmpty(it)) {
                ivClear.visibility = View.VISIBLE
                ivEye.visibility = View.VISIBLE
            } else {
                ivClear.visibility = View.GONE
            }
        }
        ivEye.setOnClickListener({
            if (etPwd.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                etPwd.inputType = (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                ivEye.setImageResource(R.drawable.eye_open)
            } else {
                etPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivEye.setImageResource(R.drawable.eye_close)
            }
        })
        ivClear.setOnClickListener({
            etPwd.setText("")
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