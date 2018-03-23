package com.lswuyou.tv.pm.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.common.Constant;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.log.AndroidLogger;
import com.lswuyou.tv.pm.log.Logger;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.account.LoginCfgVo;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.account.WeixinLoginResponse;
import com.lswuyou.tv.pm.net.response.account.WeixinLoginVerifyResponse;
import com.lswuyou.tv.pm.net.service.WeixinLoginService;
import com.lswuyou.tv.pm.net.service.WeixinLoginVerifyService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用户扫码微信关注
 * Created by Administrator on 2016/8/15.
 */
public class WeixinFocusDialog extends Dialog implements DialogInterface.OnDismissListener {
    private WebView wbLogin;
    private ImageView ivQrcode;
    private Context mContext;
    private String htmlSource;
    private Logger logger = AndroidLogger.getLogger(WeixinFocusDialog.class);
    private OnLoginSuccessListener listener;
    private Timer mTimer;
    private String token;
    private int expireSeconds;
    private Handler handler = new Handler();
    private LoginCfgVo loginCfgVo;

    public WeixinFocusDialog(Context context, OnLoginSuccessListener listener, LoginCfgVo loginCfgVo) {
        super(context, R.style.CustomStyle);
        mContext = context;
        this.listener = listener;
        this.loginCfgVo = loginCfgVo;
        init();
    }

    public WeixinFocusDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    protected WeixinFocusDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_login);
        setOnDismissListener(this);
        wbLogin = (WebView) findViewById(R.id.wb_login);
        ivQrcode = (ImageView) findViewById(R.id.iv_qrcode);
        WebSettings webSettings = wbLogin.getSettings();
        webSettings.setJavaScriptEnabled(true);
        getLoginUrl();
        wbLogin.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         view.loadUrl("javascript:window.gethtmlsource.getHtml" +
                                                 "('<head>'+" + "document.getElementsByTagName" +
                                                 "('html')[0].innerHTML+'</head>');");
                                     }

                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String
                                             url) {
                                         if (url.contains(Constant.CALLBACK_URL)) {
                                             Uri uri = Uri.parse(url);
                                             String code = uri.getQueryParameter("code");
                                             doWeinxinLogin(code);
                                             logger.debug(url);
                                             return true;
                                         } else {
                                             view.loadUrl(url);
                                             return true;
                                         }
                                     }
                                 }
        );
        wbLogin.addJavascriptInterface(new JavaInterface(), "gethtmlsource");
    }

    @Override
    public void show() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        super.show();
    }

    /**
     * 获取微信二维码登录Url
     */
    private void getLoginUrl() {
        Map<String, String> params = loginCfgVo.cfg;
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        String qrcodeImgUrl = null;
        String useWebLogin = null;
        String wxLoginUrl = null;
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);

            if (key.equals("useWebLogin")) {
                useWebLogin = value;
            }
            if (key.equals("qrcodeImgUrl")) {
                qrcodeImgUrl = value;
            }
            if (key.equals("token")) {
                token = value;
            }
            if (key.equals("expireSeconds")) {
                expireSeconds = Integer.parseInt(value);
            }
            if (key.equals("wxLoginUrl")) {
                wxLoginUrl = value;
            }
        }
        if (useWebLogin.equals("0")) {
            Glide.with(getContext().getApplicationContext()).load(qrcodeImgUrl).placeholder
                    (R.mipmap.loading).error(R.mipmap.loading).into(ivQrcode);
            startCheckLoginState();
            startCountDown();
        } else {
            if (!TextUtils.isEmpty(wxLoginUrl)) {
                wbLogin.loadUrl(wxLoginUrl);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    class JavaInterface {
        @JavascriptInterface
        public void getHtml(String html) {
            htmlSource = html;
            if (!TextUtils.isEmpty(html)) {
                Document document = Jsoup.parseBodyFragment(html);
                if (null != document) {
                    Elements descElement = document.getElementsByClass("wrp_code");
                    Element qrElement = descElement.get(0);
                    if (null != qrElement) {
                        final String qrImgUrl = "https://open.weixin.qq.com" + qrElement
                                .childNode(0).attr("src");
                        logger.debug(qrImgUrl);
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(getContext().getApplicationContext()).load(qrImgUrl).placeholder(R.mipmap.loading)
                                        .error(R.mipmap.loading).into(ivQrcode);
                            }
                        });
                    }
                }
            }
        }
    }

    //微信登录与后台通信
    private void doWeinxinLogin(String code) {
        WeixinLoginService service = new WeixinLoginService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<WeixinLoginResponse>() {
            @Override
            public void onGetData(WeixinLoginResponse data) {
                try {
                    loginSuccessAction(data.data.loginInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "关注异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        try {
            code = URLEncoder.encode(code, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.post("code=" + code, true);
    }

    public interface OnLoginSuccessListener {
        public void onLoginSuccess();
    }

    /**
     * 向服务端轮询登录状态
     */
    private void startCheckLoginState() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doCheckLoginState();
                    }
                });
            }
        }, 10000, 2000);
    }

    /**
     * 向服务端查询登录结果
     */
    private void doCheckLoginState() {
        WeixinLoginVerifyService service = new WeixinLoginVerifyService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<WeixinLoginVerifyResponse>() {
            @Override
            public void onGetData(WeixinLoginVerifyResponse data) {
                try {
                    if (data.data.verifyTokenResultVo.status == 1) {
                        loginSuccessAction(data.data.verifyTokenResultVo.loginInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "登录异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("token=" + token, false);
    }

    /**
     * 登录成功操作
     */
    private void loginSuccessAction(LoginUserInfo userInfo) {
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, userInfo);
        Toast.makeText(mContext, "登录成功！", Toast.LENGTH_SHORT).show();
        WeixinFocusDialog.this.dismiss();
        listener.onLoginSuccess();
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent
                (UserFragment.USERINFO_UPDATE));
    }

    /**
     * 二维码有效期倒计时
     */
    private void startCountDown() {
        CountDownTimer countDownTimer = new CountDownTimer(expireSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                expireSeconds = 0;
                mTimer.cancel();
                getLoginUrl();
            }
        };
        countDownTimer.start();
    }
}
