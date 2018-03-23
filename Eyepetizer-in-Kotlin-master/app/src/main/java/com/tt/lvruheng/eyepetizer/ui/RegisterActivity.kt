package com.tt.lvruheng.eyepetizer.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.common.cache.CacheKeys
import com.tt.lvruheng.eyepetizer.common.cache.CacheManager
import com.tt.lvruheng.eyepetizer.network.IOpenApiDataServiceCallback
import com.tt.lvruheng.eyepetizer.network.ServiceURL
import com.tt.lvruheng.eyepetizer.network.response.RegisterResponse
import com.tt.lvruheng.eyepetizer.network.service.RegisterService
import com.tt.lvruheng.eyepetizer.ui.dialog.VerifyFragment
import com.tt.lvruheng.eyepetizer.utils.showToast
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.title_style_1.*

class RegisterActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        var mobileNum = intent.getStringExtra("mobileNum")
        tvTitle.text = "注册"
        ivBack.setOnClickListener({
            finish()
        })
        btnNext.setOnClickListener({
            val pwd = etPwd.text.toString()
            val code = etOldPwd.text.toString()
            val registerService = RegisterService(this)
            registerService.callback = object : IOpenApiDataServiceCallback<RegisterResponse> {

                override fun onGetData(data: RegisterResponse?) {
                    if (data!!.data.login.equals("0")) {
                        showToast("注册成功")
                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, data!!.data)
                        var intent = Intent()
                        intent.setClass(this@RegisterActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        showToast(data.retMsg)
                    }
                }

                override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
                    showToast(errorMsg!!)
                }

            }
            registerService.postLogined("mobileNo=${mobileNum}&passwd=${pwd}&verifyCode=${code}", true)
        })
        btnGetCode.setOnClickListener({
            val dialog = VerifyFragment()
            dialog.show(supportFragmentManager, "veryCode")
            val data = Bundle()
            data.putString("mobileNum", mobileNum)
            dialog.arguments = data
            dialog.setOnBackListener(object : VerifyFragment.OnBackListener {
                override fun onBack(code: Int) {
                    if (code == 0) {
                        btnGetCode.startTimer(60 * 1000)
                    }
                }
            })
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
        tvProtocal.setOnClickListener({
            val intent = Intent()
            intent.setClass(this@RegisterActivity, WebViewActivity::class.java)
            intent.putExtra("url", "${ServiceURL.URL}#/protocol/register")
            intent.putExtra("showTitle", true)
            intent.putExtra("title", "注册协议")
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
