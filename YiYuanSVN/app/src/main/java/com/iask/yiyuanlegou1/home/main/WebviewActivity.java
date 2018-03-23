package com.iask.yiyuanlegou1.home.main;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.widget.TitleBarView;

public class WebviewActivity extends BaseActivity {
    private WebView webView;
    private TitleBarView title;
    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.this.finish();
            }
        });
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        String url = getIntent().getStringExtra("url");
        int titles = getIntent().getIntExtra("title", 0);
        title.setTitleText(titles);
        webView.loadUrl(url);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_webview;
    }
}
