package com.tt.lvruheng.eyepetizer.ui

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.network.IOpenApiDataServiceCallback
import com.tt.lvruheng.eyepetizer.network.response.IsRegisteredResponse
import com.tt.lvruheng.eyepetizer.network.service.IsRegisterdService
import com.tt.lvruheng.eyepetizer.utils.showToast
import kotlinx.android.synthetic.main.activity_input_phone.*
import kotlinx.android.synthetic.main.title_style_1.*

class InputPhoneActivity : BaseActivity() {
    var mobileNum: String? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_input_phone
    }

    override fun initView() {
        tvCancel.visibility = View.VISIBLE
        ivBack.visibility = View.GONE
        tvTitle.text = "登录 | 注册"

        etPhone.setTextChangeListener {
            btnNext.isEnabled = !TextUtils.isEmpty(it) && it.length == 11
        }

        btnNext.setOnClickListener({
            val phone = etPhone.text.toString()
            mobileNum = phone
            val isRegisteredService = IsRegisterdService(this)
            isRegisteredService.callback = object : IOpenApiDataServiceCallback<IsRegisteredResponse> {
                override fun onGetData(data: IsRegisteredResponse?) {
                    if (data!!.data.registered.equals("1")) {
                        val intent = Intent(this@InputPhoneActivity, LoginActivity::class.java)
                        intent.putExtra("mobileNum", mobileNum)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@InputPhoneActivity, RegisterActivity::class.java)
                        intent.putExtra("mobileNum", mobileNum)
                        startActivity(intent)
                    }
                }

                override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
                    this@InputPhoneActivity.showToast(errorMsg!!)
                }
            }
            isRegisteredService.postLogined("mobile=${phone}", true)
        })
        tvCancel.setOnClickListener({
            finish()
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
