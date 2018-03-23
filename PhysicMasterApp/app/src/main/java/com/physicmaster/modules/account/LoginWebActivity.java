package com.physicmaster.modules.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.MainActivity;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.account.basics.SelectPetActivity;
import com.physicmaster.modules.discuss.NimLoginService;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.Bind2QQ4WebService;
import com.physicmaster.net.service.account.Login2QQ4WebService;
import com.physicmaster.utils.MD5;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.utils.UIUtils;

import java.util.UUID;

import static com.physicmaster.R.id.webView;


public class LoginWebActivity extends BaseActivity {
    private ImageView   titleLeftImageview;
    private TextView    titleLeftTextview;
    private TextView    tvHead;
    private ProgressBar webLoadingBar;
    private WebView     mWebView;
    private int         screenWidth;
    private String      loginUrl;

    @Override
    protected void findViewById() {

        titleLeftImageview = (ImageView) findViewById(R.id.title_left_imageview);
        titleLeftTextview = (TextView) findViewById(R.id.title_left_textview);
        tvHead = (TextView) findViewById(R.id.tv_head);
        webLoadingBar = (ProgressBar) findViewById(R.id.web_loading_bar);
        mWebView = (WebView) findViewById(webView);


    }

    @Override
    protected void initView() {
        String webQqGetCodeUrl = getIntent().getStringExtra("webQqGetCodeUrl");
        final boolean isbind = getIntent().getBooleanExtra("isbind", false);
        String str = UUID.randomUUID().toString();
        if (!TextUtils.isEmpty(webQqGetCodeUrl)) {
            loginUrl = webQqGetCodeUrl.replace("###STATE###", MD5.hexdigest(str));
        }


        WebSettings settings = mWebView.getSettings();
        screenWidth = ScreenUtils.getScreenWidth();
        mWebView.requestFocusFromTouch();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webLoadingBar.setMax(screenWidth);
        webLoadingBar.setProgress(screenWidth * 10 / 100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                if (newProgress == 100) {
                    webLoadingBar.setVisibility(View.GONE);
                } else {
                    if (newProgress > 60) {

                    }
                    if (newProgress > 10) {
                        webLoadingBar.setProgress(screenWidth * newProgress / 100);
                    } else {
                        webLoadingBar.setProgress(screenWidth * 10 / 100);
                    }
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if ("m.lswuyou.com".equals(uri.getHost())) {
                    String code = uri.getQueryParameter("code");
                    if (isbind) {
                        bind2QQ(code);
                    } else {
                        login2QQ(code);
                    }
                    return true;
                }
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                tvHead.setText(mWebView.getTitle());
            }
        });
        if (!TextUtils.isEmpty(loginUrl)) {
            mWebView.loadUrl(loginUrl);
        }

        titleLeftImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLeftTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_webview_login;
    }

    private void login2QQ(String code) {

        final Login2QQ4WebService service = new Login2QQ4WebService(LoginWebActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                UIUtils.showToast(LoginWebActivity.this, "登录成功");
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                        .USERINFO_USERKEY, data.data.keyVo);
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                        .USERINFO_LOGINVO, data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                BaseApplication.setUserKey(data.data.keyVo);
                if (data.data.loginVo.isInitial == 0) {
                    Intent intent = new Intent(LoginWebActivity.this, SelectPetActivity.class);
                    UIUtils.showToast(LoginWebActivity.this, "请完善基本信息");
                    startActivity(intent);
                    finish();
                } else {
                    if (1 == data.data.loginVo.isImAutoConnect) {
                        Intent loginIntent = new Intent(LoginWebActivity.this, NimLoginService.class);
                        loginIntent.putExtra("account", data.data.loginVo.imUserId);
                        loginIntent.putExtra("token", data.data.loginVo.imToken);
                        startService(loginIntent);
                    }
                    Intent intent = new Intent(LoginWebActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(LoginWebActivity.this, errorMsg);
                finish();

            }
        });
        service.postUnLogin("code=" + code, false);
    }

    private void bind2QQ(String code) {

        final Bind2QQ4WebService service = new Bind2QQ4WebService(LoginWebActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                UIUtils.showToast(LoginWebActivity.this, "绑定成功");
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                        .USERINFO_LOGINVO, data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(LoginWebActivity.this, errorMsg);
                finish();

            }
        });
        service.postLogined("code=" + code, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWebView) {
            mWebView.destroy();
            mWebView = null;
        }
    }
}
