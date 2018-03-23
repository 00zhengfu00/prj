package com.tt.lvruheng.eyepetizer.ui

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import cn.cloudwalk.libproject.Bulider
import cn.cloudwalk.libproject.CloudwalkBankCardOCRActivity
import cn.cloudwalk.libproject.Contants
import cn.cloudwalk.libproject.OcrCameraActivity
import cn.cloudwalk.libproject.util.UIUtils
import com.alibaba.fastjson.JSON
import com.github.lzyzsd.jsbridge.BridgeWebViewClient
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.base.BaseApplication
import com.tt.lvruheng.eyepetizer.base.BaseApplication.Companion.context
import com.tt.lvruheng.eyepetizer.common.cache.CacheKeys
import com.tt.lvruheng.eyepetizer.common.cache.CacheManager
import com.tt.lvruheng.eyepetizer.network.IOpenApiDataServiceCallback
import com.tt.lvruheng.eyepetizer.network.LOGIN_SUCCESS
import com.tt.lvruheng.eyepetizer.network.response.IdCardOcrResponse
import com.tt.lvruheng.eyepetizer.network.service.IdCardOcrService
import com.tt.lvruheng.eyepetizer.security.Base64Encoder
import com.tt.lvruheng.eyepetizer.utils.Constant
import com.tt.lvruheng.eyepetizer.utils.Constant.IDENTITY_SUCCESS
import com.tt.lvruheng.eyepetizer.utils.FileUtil
import com.tt.lvruheng.eyepetizer.utils.showToast
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.title_style_2.*
import org.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder

class WebViewActivity : BaseActivity() {

    var data: String? = null
    var callBack: String? = null
    var directionType: Int = 0
    val REQ_CAMERA1 = 100
    val REQ_CAMERA2 = 200

    override fun getLayoutId(): Int {
        return R.layout.activity_web_view
    }

    override fun initView() {
        initWebView()
        registerReceiver(loginReceiver, IntentFilter(LOGIN_SUCCESS))
        val url = intent.getStringExtra("url")
        val showTitle = intent.getBooleanExtra("showTitle", false)
        val title = intent.getStringExtra("title")
        if (showTitle) {
            titleBar.visibility = View.VISIBLE
            tvTitle.text = title
            ivBack.setImageResource(R.drawable.back_black)
            ivBack.setOnClickListener({
                finish()
            })
        }
        webView.loadUrl(url)
    }

    val loginReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            webView.reload()
        }
    }

    private fun initWebView() {
        val settings = webView.settings
        //设置支持JS
        settings.javaScriptEnabled = true
        // 设置支持本地存储
        settings.databaseEnabled = true
        //取得缓存路径
        val path = cacheDir.absolutePath
        //设置路径
        settings.databasePath = path
        //设置支持DomStorage
        settings.domStorageEnabled = true
        //设置存储模式
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        //设置适应屏幕
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        //允许webview对文件的操作
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        //设置缓存
        settings.setAppCacheEnabled(true)
        webView.requestFocus()
        webView.addJavascriptInterface(JsBridge(this), "androidJS")
        var userAgent = webView.settings.userAgentString
        userAgent = "${userAgent};nxph"
        webView.settings.userAgentString = userAgent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView.webViewClient = object : BridgeWebViewClient(webView) {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
        webView.webChromeClient = WebChromeClient()
        val url = intent.getStringExtra("url")
        webView.loadUrl(url)
    }

    /**
     * Android6.0需要申请权限
     */
    private val DANGEROUS_PERMISSION = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    private val CAMERA_REQUEST_CODE = 1

    /**
     * Android6.0权限申请
     */
    private fun permissionRequest() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 申请权限
                ActivityCompat.requestPermissions(this, DANGEROUS_PERMISSION,
                        CAMERA_REQUEST_CODE)
            } else {
                // 权限已经授予,直接初始化
                scanIdCard()
            }
        } else {
            scanIdCard()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults != null) {
                // 权限授予成功,初始化
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("result", "成功获得授权")
                    scanIdCard()
                } else {
                    // 三方处理自己逻辑,这里只做测试用
                    UIUtils.showToast(context, "您拒绝了拍照权限，无法拍摄身份证照片")
                }
            } else {
                UIUtils.showToast(context, "获取系统权限异常")
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * 扫描身份证
     */
    fun scanIdCard() {
        val intent = Intent(context, OcrCameraActivity::class.java)
        intent.putExtra("LICENCE", Constant.LISENCE)
        intent.putExtra("FACEAPPID", Constant.FACEAPPID)
        intent.putExtra("BANKSERVER", Constant.FACESERVER)
        Bulider.licence = Constant.LISENCE
        if (directionType == 0) {
            intent.putExtra(Contants.OCR_FLAG, Contants.OCR_FLAG_IDFRONT)
            startActivityForResult(intent, REQ_CAMERA1)
        } else {
            intent.putExtra(Contants.OCR_FLAG, Contants.OCR_FLAG_IDBACK)
            startActivityForResult(intent, REQ_CAMERA2)
        }
    }

    /**
     * 扫描银行卡
     */
    fun scanBankCard(callBack: String) {
        val mIntent = Intent(context, CloudwalkBankCardOCRActivity::class.java)
        mIntent.putExtra("LICENCE", Constant.LISENCE)
        mIntent.putExtra("FACEAPPID", Constant.FACEAPPID)
        mIntent.putExtra("BANKSERVER", Constant.FACESERVER)
        mIntent.putExtra("BANKCARD_AUTO_RATIO", true)//是否支持竖版银行卡
        startActivity(mIntent)
        if (callBack != null) {
            this.callBack = callBack
        }
    }

    inner class JsBridge {
        /*
        绑卡连连支付接口 bindLianLianPay
        身份证ocr识别   idCardScan
        银行卡ocr识别   bankCardScan
        回到首页         gotoMainPage
        去登录页面       gotoLoginPage
        获取登录信息     getLoginInfo
        清除登录信息     removeUserInfo
        和H5交互还需要一个隐藏/显示底部导航栏的接口，hideToolBar/showToolBar
         */
        var context: Activity

        constructor(context: Activity) {
            this.context = context
        }

        @JavascriptInterface
        fun bindLianLianPay(data: String) {
            println()
        }

        @JavascriptInterface
        fun idCardScan(data: String) {
            var json = JSONObject(data)
            var type = json.getInt("type")
            var callback = json.getString("callback")
            directionType = type
            callBack = callback
            permissionRequest()
        }

        @JavascriptInterface
        fun bankCardScan(data: String) {
        }

        @JavascriptInterface
        fun gotoMainPage(data: String) {
            finish()
        }

        @JavascriptInterface
        fun identitySucc(data: String) {
            sendBroadcast(Intent(IDENTITY_SUCCESS))
        }

        @JavascriptInterface
        fun gotoMine(data: String) {
            val intent = Intent()
            intent.setClass(context, MainActivity::class.java)
            startActivity(intent)
        }

        @JavascriptInterface
        fun gotoLoginPage(data: String) {
            val intent = Intent()
            intent.setClass(context, InputPhoneActivity::class.java)
            startActivity(intent)
        }

        @JavascriptInterface
        fun removeUserInfo(data: String) {
            CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO)
            BaseApplication.loginInfo = null
        }

        @JavascriptInterface
        fun hideToolBar(data: String) {
        }

        @JavascriptInterface
        fun showToolBar(data: String) {
        }

        @JavascriptInterface
        fun getLoginInfo(data: String) {
            var json = JSONObject(data)
            var callback = json.getString("callback")
            val loginInfo = BaseApplication.loginInfo
            runOnUiThread({
                if (loginInfo != null) {
                    webView.loadUrl("javascript:${callback}(${JSON.toJSONString(loginInfo)})")
                } else {
                    webView.loadUrl("javascript:${callback}()")
                }
            })
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loginReceiver != null) {
            unregisterReceiver(loginReceiver)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQ_CAMERA1 || requestCode == REQ_CAMERA2) {// 相机
            if (data != null) {
                val filePath = data.getStringExtra(OcrCameraActivity.FILEPATH_KEY)
                val bytes = FileUtil.File2byte(filePath)
                var bytesString = Base64Encoder.encode(bytes)
                bytesString = URLEncoder.encode(bytesString, Constant.ENCODE)
                idCardOcrFront(bytesString)
            }
        }
    }

    /**
     * 身份证识别
     */
    private fun idCardOcrFront(bytes: String) {
        val ocrService = IdCardOcrService(context)
        ocrService.callback = object : IOpenApiDataServiceCallback<IdCardOcrResponse> {
            override fun onGetData(data: IdCardOcrResponse?) {
                showToast(data!!.retMsg)
                val dataJson = com.alibaba.fastjson.JSONObject()
                val decodeBytes = URLDecoder.decode(bytes, Constant.ENCODE)
                dataJson.put("imgBaseStr", "data:image/jpg;base64,${decodeBytes}")
                if (TextUtils.isEmpty(data.data.validTime)) {
                    dataJson.put("name", data.data.name)
                    dataJson.put("identity", data.data.identity)
                } else {
                    dataJson.put("validTime", data.data.validTime)
                }
                var dataJsonStr = dataJson.toJSONString()
                webView.loadUrl("javascript:${callBack}(${dataJsonStr})")
            }

            override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
                showToast(errorMsg!!)
            }

        }
        var token = BaseApplication.loginInfo!!.logonTokenKey
        ocrService.postLogined("token=${token}&img=${bytes}", false)
    }
}
