package com.physicmaster.modules.study.activity;

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
import com.physicmaster.utils.ScreenUtils;

import static com.physicmaster.R.id.webView;


public class AboutParentsPayActivity extends BaseActivity {
    private ImageView   titleLeftImageview;
    private TextView    titleLeftTextview;
    private TextView    tvHead;
    private ProgressBar webLoadingBar;
    private WebView     mWebView;
    private int         screenWidth;
    private String chpIntroduceUrl;

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
        chpIntroduceUrl = getIntent().getStringExtra("helpPayIntroUrl");

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
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                tvHead.setText(mWebView.getTitle());
            }
        });
        if (!TextUtils.isEmpty(chpIntroduceUrl)) {
            mWebView.loadUrl(chpIntroduceUrl);
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
        return R.layout.activity_parentspay_about;
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
