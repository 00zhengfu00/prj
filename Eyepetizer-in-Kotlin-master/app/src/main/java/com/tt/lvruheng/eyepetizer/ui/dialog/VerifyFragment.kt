package com.tt.lvruheng.eyepetizer.ui.dialog

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.network.IOpenApiDataServiceCallback
import com.tt.lvruheng.eyepetizer.network.response.CheckImgCodeResponse
import com.tt.lvruheng.eyepetizer.network.response.GetCaptchaResponse
import com.tt.lvruheng.eyepetizer.network.service.CheckImgCodeService
import com.tt.lvruheng.eyepetizer.network.service.GetCaptchaService
import com.tt.lvruheng.eyepetizer.security.Base64Decoder
import com.tt.lvruheng.eyepetizer.utils.showToast
import kotlinx.android.synthetic.main.dialogfragment_verify_code.*

/**
 * Created by huashigen on 2018-01-04.
 */
class VerifyFragment : DialogFragment() {

    var phone: String? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.dialogfragment_verify_code, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        dialog.window!!.setLayout(dm.widthPixels, dialog.window!!.attributes.height)
        getCaptcha()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phone = arguments.getString("mobileNum")
        btnCancel.setOnClickListener({
            dismiss()
        })
        btnOk.setOnClickListener({
            var imgCode = etImgCode.text.toString()
            if (TextUtils.isEmpty(imgCode)) {
                context.showToast("请输入图片验证码！")
                if (onBackListener != null) {
                    onBackListener!!.onBack(1)
                }
            } else {
                imgCode = imgCode.toUpperCase()
                val checkCodeService = CheckImgCodeService(context)
                checkCodeService.callback = object : IOpenApiDataServiceCallback<CheckImgCodeResponse> {
                    override fun onGetData(data: CheckImgCodeResponse?) {
                        if (data!!.data.codeCheck.equals("0")) {
                            if (onBackListener != null) {
                                onBackListener!!.onBack(0)
                            }
                            dismiss()
                        } else {
                            context.showToast(data.retMsg)
                        }
                    }

                    override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
                        context.showToast(errorMsg!!)
                    }
                }
                checkCodeService.postLogined("mobile=${phone}&code=${imgCode}", true)
            }
        })
        if (!TextUtils.isEmpty(phone)) {
            getCaptcha()
        }
        tvFresh.setOnClickListener({
            getCaptcha()
        })
    }

    fun getCaptcha() {
        val getCaptchaService = GetCaptchaService(context)
        getCaptchaService.callback = object : IOpenApiDataServiceCallback<GetCaptchaResponse> {
            override fun onGetData(data: GetCaptchaResponse?) {
                val img = data!!.data.img
                val byteArray = Base64Decoder.decodeToBytes(img)
                var imgBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                iv_img_code.setImageBitmap(imgBitmap)
            }

            override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
            }

        }
        getCaptchaService.postLogined("mobile=${phone}", true)
    }

    private var onBackListener: OnBackListener? = null

    fun setOnBackListener(onBackListener: OnBackListener) {
        this.onBackListener = onBackListener
    }

    interface OnBackListener {
        fun onBack(code: Int)
    }

}