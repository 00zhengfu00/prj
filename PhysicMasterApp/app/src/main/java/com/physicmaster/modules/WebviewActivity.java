package com.physicmaster.modules;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.MainActivity;
import com.physicmaster.base.SplashActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.modules.course.activity.pay.PayConfig;
import com.physicmaster.modules.course.activity.pay.PayResult;
import com.physicmaster.modules.explore.activity.BuyMemberDialogFragment;
import com.physicmaster.modules.mine.activity.InvitationActivity;
import com.physicmaster.modules.mine.fragment.dialogFragment.ShareDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.AuthCodeResponse;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.course.MemberPayResponse;
import com.physicmaster.net.service.account.GetAuthCodeService;
import com.physicmaster.net.service.member.WebPrepayService;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class WebviewActivity extends BaseActivity implements View.OnClickListener {
    private BridgeWebView webView;
    private boolean enterMain = false;
    private ImageView backIv;
    private TextView backTv;
    public static String html = "";
    ValueCallback<Uri> mUploadMessage;
    private final String TAG = "WebviewActivity";
    private ProgressBar webLoadingBar;
    private int screenWidth;
    private static int weixinShareType = 0;//0-朋友圈,1-微信好友
    private static final int SDK_PAY_FLAG = 1;
    /**
     * Android6.0需要申请权限
     */
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission
            .ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static int LOCATION_REQUEST_CODE = 1;
    private LocalBroadcastManager localBroadcastManager;
    private ShareAction shareAction;
    private boolean allowShare;
    private LinearLayout llShare;
    private BuyMemberDialogFragment fragment;

    @Override
    protected void findViewById() {
        webView = (BridgeWebView) findViewById(R.id.webView);
        backIv = (ImageView) findViewById(R.id.title_left_imageview);
        backTv = (TextView) findViewById(R.id.title_left_textview);
    }

    @Override
    protected void initView() {
        screenWidth = ScreenUtils.getScreenWidth();
        webLoadingBar = ((ProgressBar) findViewById(R.id.web_loading_bar));
        webLoadingBar.setMax(screenWidth);
        webLoadingBar.setProgress(screenWidth * 10 / 100);
        WebSettings settings = webView.getSettings();
        //设置支持JS
        settings.setJavaScriptEnabled(true);
        // 设置支持本地存储
        settings.setDatabaseEnabled(true);
        //取得缓存路径
        String path = getCacheDir().getPath();
        //设置路径
        settings.setDatabasePath(path);
        //设置支持DomStorage
        settings.setDomStorageEnabled(true);
        //设置存储模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        //设置缓存
        settings.setAppCacheEnabled(true);
        webView.requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setDefaultHandler(new DefaultHandler());

        webView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String
                    AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String
                    AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }

            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                if (newProgress == 100) {
                    webLoadingBar.setVisibility(View.GONE);
                } else {
                    if (newProgress > 60) {
                        //closeLoading();
                    }
                    if (newProgress > 10) {
                        webLoadingBar.setProgress(screenWidth * newProgress / 100);
                    } else {
                        webLoadingBar.setProgress(screenWidth * 10 / 100);
                    }
                }
            }
        });
        webView.setWebViewClient(new BridgeWebViewClient(webView) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                TextView tvTitle = (TextView) findViewById(R.id.tv_head);
                tvTitle.setText(webView.getTitle());
                if (allowShare) {
                    llShare.setVisibility(View.VISIBLE);
                }
            }

        });
        StringBuilder builder = new StringBuilder();
        String userAgent = settings.getUserAgentString();
        builder.append(userAgent);
        String packageName = getPackageName();
        if (packageName.equals(Constant.PHYSICMASTER)) {
            builder.append(" PhysicMaster/");
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            builder.append(" ChemistryMaster/");
        } else if (packageName.equals(Constant.MATHMASTER)) {
            builder.append(" MathMaster/");
        }
        builder.append(getVersion());
        builder.append(" Network/");
        builder.append(getNetworkState());
        builder.append(" DevicePixelRatio/" + BaseApplication.getDensity());
        settings.setUserAgentString(builder.toString());
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new CreateJsbridgeInterface(), "__wj_bridge_loaded__");
        webView.addJavascriptInterface(new JavaScriptinterface(WebviewActivity.this), "jsbridge");
        String url = getIntent().getStringExtra("url");
        enterMain = getIntent().getBooleanExtra("enterMain", false);
        webView.loadUrl(url);

        backIv.setOnClickListener(this);
        backTv.setOnClickListener(this);
        processShare();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(weixinSharedReceiver, new IntentFilter(InvitationActivity.WEIXIN_SHARED_SUCC));
        localBroadcastManager.registerReceiver(paySuccessReceiver, new IntentFilter(Constant.WEIXIN_PAY_FINISHED));
    }

    private BroadcastReceiver paySuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPayResultFunction != null) {
                if (intent.getBooleanExtra("fail", false)) {
                    mPayResultFunction.onCallBack("{\"code\":-1,\"msg\":\"用户取消支付\"}");
                } else {
                    mPayResultFunction.onCallBack("{\"code\":200}");
                }
            }
        }
    };

    private BroadcastReceiver weixinSharedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (weixinShareType == 0) {
                webView.callHandler("onMenuShareWeixinTimeline", "", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                        UIUtils.showToast(WebviewActivity.this, data);
                    }
                });
            } else {
                webView.callHandler("onMenuShareWeixinChat", "", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                        UIUtils.showToast(WebviewActivity.this, data);
                    }
                });
            }

        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.activity_webview;
    }

    // 检测网络状态
    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    // 获取版本信息
    public String getVersion() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "2.2.0";
    }

    /**
     * 获取网络状态
     */
    private String getNetworkState() {
        if (!isNetworkConnected()) {
            return "unconnected";
        }
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        int nType = info.getType();
        if (ConnectivityManager.TYPE_WIFI == nType) {
            return Constant.NETTYPE_WIFI;
        } else if (ConnectivityManager.TYPE_MOBILE == nType) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context
                    .TELEPHONY_SERVICE);
            int networkType = telephonyManager.getNetworkType();
            return getNetworkClassByType(networkType);
        }
        return Constant.NETTYPE_UNKNOWN;
    }

    /**
     * 获取移动网络的类型
     */
    private String getNetworkClassByType(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return Constant.NETTYPE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return Constant.NETTYPE_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return Constant.NETTYPE_4G;
            default:
                return Constant.NETTYPE_UNKNOWN;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if (enterMain) {
                if (null == BaseApplication.getStartupDataBean()) {
                    startActivity(new Intent(WebviewActivity.this, SplashActivity.class));
                } else {
                    Object bean = BaseApplication.getUserData();
                    if (bean != null) {
                        startActivity(new Intent(WebviewActivity.this, MainActivity
                                .class));
                    } else {
                        startActivity(new Intent(WebviewActivity.this, LoginActivity
                                .class));
                    }
                    finish();
                }
            } else {
                finish();
            }
        }
    }

    /**
     * 处理分享
     */
    private void processShare() {
        llShare = (LinearLayout) findViewById(R.id.ll_share);
        allowShare = getIntent().getBooleanExtra("allowShare", true);
        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShareDialogFragment fragment = new ShareDialogFragment();
                fragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_weixin_share:
                                if (Utils.checkApkExist(WebviewActivity.this, "com.tencent.mm")) {
                                    sharedToMedia(SHARE_MEDIA.WEIXIN);
                                    weixinShareType = 1;
                                } else {
                                    Toast.makeText(WebviewActivity.this, "您还未安装微信，请先安装~", Toast
                                            .LENGTH_SHORT).show();
                                }
                                fragment.dismiss();
                                break;
                            case R.id.tv_qq_share:
                                if (Utils.checkApkExist(WebviewActivity.this, "com.tencent" +
                                        ".mobileqq")) {
                                    sharedToMedia(SHARE_MEDIA.QQ);
                                } else {
                                    Toast.makeText(WebviewActivity.this, "您还未安装QQ，请先安装~", Toast
                                            .LENGTH_SHORT).show();
                                }
                                fragment.dismiss();
                                break;
                            case R.id.tv_circle_share:
                                if (Utils.checkApkExist(WebviewActivity.this, "com.tencent.mm")) {
                                    sharedToMedia(SHARE_MEDIA.WEIXIN_CIRCLE);
                                    weixinShareType = 0;
                                } else {
                                    Toast.makeText(WebviewActivity.this, "您还未安装微信，请先安装~", Toast
                                            .LENGTH_SHORT).show();
                                }
                                fragment.dismiss();
                                break;
                            case R.id.tv_qzone_share:
                                if (Utils.checkApkExist(WebviewActivity.this, "com.tencent" +
                                        ".mobileqq")) {
                                    sharedToMedia(SHARE_MEDIA.QZONE);
                                } else {
                                    Toast.makeText(WebviewActivity.this, "您还未安装QQ，请先安装~", Toast
                                            .LENGTH_SHORT).show();
                                }
                                fragment.dismiss();
                                break;
                            case R.id.tv_weibo_share:
                                if (Utils.checkApkExist(WebviewActivity.this, "com.sina.weibo")
                                        || Utils.checkApkExist(WebviewActivity.this, "com" +
                                        ".sina.weibog3")) {
                                    sharedToMedia(SHARE_MEDIA.SINA);
                                } else {
                                    Toast.makeText(WebviewActivity.this, "您还未安装微博，请先安装~", Toast
                                            .LENGTH_SHORT).show();
                                }
                                fragment.dismiss();
                                break;
                        }
                    }
                });
                fragment.show(getSupportFragmentManager(), "share");
            }
        });
    }

    private void sharedToMedia(SHARE_MEDIA media) {
        webView.loadUrl("javascript:window.jsbridge.showSource('<head>'+" + "document" +
                ".getElementsByTagName('html')[0].innerHTML+'</head>');");
        String title = webView.getTitle();
        String desc = "";
        String shareImageUrl = "";
        if (TextUtils.isEmpty(title)) {
            title = "网页";
        }
        if (!TextUtils.isEmpty(html)) {
            Document document = Jsoup.parseBodyFragment(html);
            if (null != document) {
                Elements elements = document.getElementsByTag("meta");
                for (int i = 0; i < elements.size(); i++) {
                    Element element = elements.get(i);
                    String metaName = element.attr("name");
                    if (metaName.equalsIgnoreCase("description")) {
                        desc = element.attr("content");
                        break;
                    }
                }
                Element imageElement = document.getElementById("share_img");
                if (null != imageElement) {
                    shareImageUrl = imageElement.attr("src");
                }
            }
        }
        if (TextUtils.isEmpty(desc)) {
            desc = title;
        }
        UMImage image;
        if (!TextUtils.isEmpty(shareImageUrl)) {
            image = new UMImage(WebviewActivity.this, shareImageUrl);
        } else {
            StartupResponse.DataBean dataBean = BaseApplication.getStartupDataBean();
            if (dataBean == null || TextUtils.isEmpty(dataBean.shareLinkImg)) {
                startActivity(new Intent(WebviewActivity.this, SplashActivity.class));
                WebviewActivity.this.finish();
                UIUtils.showToast(WebviewActivity.this, "数据异常");
                return;
            }
            image = new UMImage(WebviewActivity.this, dataBean.shareLinkImg);
        }
        if (null == image) {
            String packageName = getPackageName();
            if (packageName.equals(Constant.PHYSICMASTER)) {
                image = new UMImage(WebviewActivity.this, R.mipmap.icon_physic);
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                image = new UMImage(WebviewActivity.this, R.mipmap.icon_chemistory);
            } else if (packageName.equals(Constant.MATHMASTER)) {
                image = new UMImage(WebviewActivity.this, R.mipmap.icon_math);
            }
        }
        String url = webView.getUrl();
        if (TextUtils.isEmpty(url)) {
            UIUtils.showToast(this, "分享链接地址为空");
            return;
        }
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setDescription(desc);
        web.setThumb(image);
        shareAction = new ShareAction(WebviewActivity.this);
        shareAction.setPlatform(media).setCallback(new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA media) {
                UIUtils.showToast(WebviewActivity.this, "分享成功啦");
                if (media.equals(SHARE_MEDIA.QQ)) {
                    webView.callHandler("onMenuShareQQ", "", new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                            UIUtils.showToast(WebviewActivity.this, data);
                        }
                    });
                } else if (media.equals(SHARE_MEDIA.QZONE)) {
                    webView.callHandler("onMenuShareQZone", "", new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                            UIUtils.showToast(WebviewActivity.this, data);
                        }
                    });
                } else if (media.equals(SHARE_MEDIA.SINA)) {
                    webView.callHandler("onMenuShareWeibo", "", new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                            UIUtils.showToast(WebviewActivity.this, data);
                        }
                    });
                } else if (media.equals(SHARE_MEDIA.WEIXIN)) {
                    webView.callHandler("onMenuShareWeixinChat", "", new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                            UIUtils.showToast(WebviewActivity.this, data);
                        }
                    });
                } else if (media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                    webView.callHandler("onMenuShareWeixinTimeline", "", new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                            UIUtils.showToast(WebviewActivity.this, data);
                        }
                    });
                }
            }

            @Override
            public void onError(SHARE_MEDIA arg0, Throwable t) {
                UIUtils.showToast(WebviewActivity.this, "分享失败啦");
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA arg0) {
                UIUtils.showToast(WebviewActivity.this, "分享取消了");
            }
        }).withMedia(web).share();
    }

    //客户端支持的方法
    private static final String[] supportApis = {"checkApi", "getNetwork", "getLoginAuthCode", "getLocation"};

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_imageview:
            case R.id.title_left_textview:
                if (enterMain) {
                    Object bean = BaseApplication.getUserData();
                    if (bean != null) {
                        startActivity(new Intent(WebviewActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(WebviewActivity.this, LoginActivity.class));
                    }
                    finish();
                } else {
                    finish();
                }
                break;
        }
    }

    //创建businessJsBridge的jsBridge
    public class CreateJsbridgeInterface {
        @JavascriptInterface
        public void setup() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //判断当前域名是否合法,合法才创建业务交互的jsbridge
                    if (BaseApplication.getStartupDataBean() == null) {
                        startActivity(new Intent(WebviewActivity.this, SplashActivity.class));
                        WebviewActivity.this.finish();
                        UIUtils.showToast(WebviewActivity.this, "数据异常");
                        return;
                    }
                    final List<String> jsBridgeSupportDomainList = BaseApplication.getStartupDataBean().jsBridgeWhiteList;
                    StringBuilder legalDomains = new StringBuilder();
                    if (jsBridgeSupportDomainList != null) {
                        for (String s : jsBridgeSupportDomainList) {
                            legalDomains.append(s);
                        }
                    }
                    String url = webView.getUrl();
                    if (TextUtils.isEmpty(url)) {
                        webView.reload();
                        return;
                    }
                    Uri uri = Uri.parse(url);
                    String domain = uri.getHost();
                    if (legalDomains.toString().contains(domain)) {
                        webView.loadUrl("javascript:if (window.WebViewJavascriptBridge) {\n" +
                                "        window.WJWebViewJavascriptBridge = window" +
                                ".WebViewJavascriptBridge;" +
                                "for(var i=0;i<window.WJWVJBCallbacks.length;i++)\n" +
                                "{var callbacks = window.WJWVJBCallbacks[i];\n" +
                                "callbacks();}" +
                                "    } else {\n" +
                                "        document.addEventListener(\n" +
                                "            'WebViewJavascriptBridgeReady'\n" +
                                "            , function() {\n" +
                                "                window.WJWebViewJavascriptBridge = window" +
                                ".WebViewJavascriptBridge;" +
                                "for(var i=0;i<window.WJWVJBCallbacks.length;i++)\n" +
                                "{var callbacks = window.WJWVJBCallbacks[i];\n" +
                                "callbacks();}" +
                                "            },\n" +
                                "            false\n" +
                                "        );\n" +
                                "    }");
                        webView.registerHandler("getLoginAuthCode", new BridgeHandler() {

                            @Override
                            public void handler(String data, CallBackFunction function) {
                                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                                getAuthCode(function);
                            }

                        });
                        webView.registerHandler("getNetwork", new BridgeHandler() {

                            @Override
                            public void handler(String data, CallBackFunction function) {
                                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                                String network = getNetworkState();
                                JSONObject retJson = new JSONObject();
                                retJson.put("code", 200);
                                retJson.put("network", network);
                                function.onCallBack(retJson.toJSONString());
                            }

                        });
                        webView.registerHandler("checkApi", new BridgeHandler() {

                            @Override
                            public void handler(String data, CallBackFunction function) {
                                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                                JSONObject jsonData = JSON.parseObject(data);
                                JSONObject jsonResult = new JSONObject();
                                JSONArray jsonArray = jsonData.getJSONArray("apiList");
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    for (String supportApi : supportApis) {
                                        String api = jsonArray.getString(i);
                                        if (api.equals(supportApi)) {
                                            jsonResult.put(api, true);
                                            break;
                                        }
                                        jsonResult.put(api, false);
                                    }
                                }
                                JSONObject retJson = new JSONObject();
                                retJson.put("code", 200);
                                retJson.put("checkResult", jsonResult);
                                function.onCallBack(retJson.toJSONString());
                            }

                        });
                        webView.registerHandler("getLocation", new BridgeHandler() {

                            @Override
                            public void handler(String data, CallBackFunction function) {
                                Log.i(TAG, "handler = getLocation, data from web = " + data);
                                savedFunction = function;
                                getLocation(function);
                            }

                        });
                        webView.registerHandler("appPay", new BridgeHandler() {
                            @Override
                            public void handler(String data, CallBackFunction function) {
                                Log.i(TAG, "handler = appPay, data from web = " + data);
                                mPayResultFunction = function;
                                JSONObject jsonData = JSON.parseObject(data);
                                if (jsonData.containsKey("orderId") && jsonData.containsKey("orderType")) {
                                    final String orderId = jsonData.getString("orderId");
                                    final int orderType = jsonData.getIntValue("orderType");
                                    fragment = new BuyMemberDialogFragment();
                                    fragment.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            switch (v.getId()) {
                                                // 支付宝支付
                                                case R.id.tv_zhifubao_pay:
                                                    MobclickAgent.onEvent(WebviewActivity.this, "confirm_order_zhifubao_pay");
                                                    fragment.dismiss();
                                                    prePay(orderType, orderId, PayConfig.ALIPAY);
                                                    break;
                                                //微信支付
                                                case R.id.tv_weixin_pay:
                                                    MobclickAgent.onEvent(WebviewActivity.this, "confirm_order_weixin_pay");
                                                    if (!Utils.checkApkExist(WebviewActivity.this, "com.tencent.mm")) {
                                                        UIUtils.showToast(WebviewActivity.this, "请先安装微信");
                                                        webView.callHandler("appPay", "{\"code\":404,\"msg\":\"手机未安装微信\"}", new CallBackFunction() {
                                                            @Override
                                                            public void onCallBack(String data) {
                                                                UIUtils.showToast(WebviewActivity.this, "回调成功");
                                                            }
                                                        });
                                                        return;
                                                    }
                                                    fragment.dismiss();
                                                    prePay(orderType, orderId, PayConfig.WEIXIN_PAY);
                                                    break;
                                                case R.id.btn_cancel:
                                                    if (mPayResultFunction != null) {
                                                        mPayResultFunction.onCallBack("{\"code\":-1,\"msg\":\"用户取消支付\"}");
                                                    }
                                                    fragment.dismiss();
                                                    break;
                                            }
                                        }
                                    });
                                    fragment.show(getSupportFragmentManager(), "pay");
                                } else {
                                    UIUtils.showToast(WebviewActivity.this, "订单信息不完整");
                                }
                            }

                        });
                    }
                }
            });
        }

    }


    private CallBackFunction savedFunction;
    private CallBackFunction mPayResultFunction;
    private int RESULT_CODE = 0;

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    /**
     * 获取授权码
     *
     * @param
     */
    private void getAuthCode(final CallBackFunction function) {
        GetAuthCodeService service = new GetAuthCodeService(this);
        service.setCallback(new IOpenApiDataServiceCallback<AuthCodeResponse>() {
            @Override
            public void onGetData(AuthCodeResponse data) {
                function.onCallBack(JSON.toJSONString(data));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(WebviewActivity.this, "获取授权码失败：" + errorMsg);
            }
        });
        service.postLogined("", false);
    }

    /**
     * 订单预支付
     *
     * @param orderType
     * @param orderId
     */
    private void prePay(int orderType, String orderId, int payType) {
        if (TextUtils.isEmpty(orderId)) {
            UIUtils.showToast(WebviewActivity.this, "订单信息不合法");
            return;
        }
        WebPrepayService service = new WebPrepayService(this);
        service.setCallback(new IOpenApiDataServiceCallback<MemberPayResponse>() {
            @Override
            public void onGetData(MemberPayResponse data) {
                int type = data.data.orderPayVo.payType;
                Map<String, String> payParams = data.data.orderPayVo.payCfg;
                if (type == PayConfig.WEIXIN_PAY) {
                    doWeixinPay(payParams);
                }

//                else if (type == PayConfig.SHARE_PAY) {
//                    if (Utils.checkApkExist(WebviewActivity.this, "com.tencent.mm")) {
//                        sharedToPay(payParams, SHARE_MEDIA.WEIXIN);
//                    } else if (Utils.checkApkExist(WebviewActivity.this, "com.tencent.mobileqq")) {
//                        sharedToPay(payParams, SHARE_MEDIA.QQ);
//                    } else {
//                        if ((payParams != null) && (orderId != null)) {
//                            String shareUrl = payParams.get("shareUrl");
//                            String qrCodeUrl = payParams.get("qrCodeUrl");
//                            if ((!TextUtils.isEmpty(shareUrl)) && (!TextUtils.isEmpty(qrCodeUrl))) {
//                                ParentsPayDialogFragment fragment = new ParentsPayDialogFragment();
//                                Bundle bundle = new Bundle();
//                                bundle.putString("shareUrl", shareUrl);
//                                bundle.putString("orderId", orderId);
//                                bundle.putString("qrCodeUrl", qrCodeUrl);
//                                fragment.setArguments(bundle);
//                                fragment.show(getSupportFragmentManager(), "parentspay");
//                            }
//                        }
//                    }
//                } 

                else if (type == PayConfig.ALIPAY) {
                    doAlipay(payParams);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(WebviewActivity.this, errorMsg);
                if (mPayResultFunction != null) {
                    mPayResultFunction.onCallBack("{\"code\":0,\"msg\":\"请求预支付订单失败\"}");
                }
            }
        });
        service.postLogined("orderType=" + orderType + "&orderId=" + orderId + "&payType=" + payType, false);
    }

    /**
     * 支付宝支付
     *
     * @param payParams
     */
    private void doAlipay(Map<String, String> payParams) {
        final String orderInfo = payParams.get("orderStr");   // 订单信息
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(WebviewActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private IWXAPI api;

    /**
     * 微信支付
     *
     * @param payParams
     */
    private void doWeixinPay(Map<String, String> payParams) {
        PayReq req = new PayReq();
        api = WXAPIFactory.createWXAPI(this, null);
        req.appId = payParams.get("appId");
        req.partnerId = payParams.get("partnerId");
        req.prepayId = payParams.get("prepayId");
        req.nonceStr = payParams.get("nonceStr");
        req.timeStamp = payParams.get("timeStamp");
        req.packageValue = payParams.get("packageStr");
        req.sign = payParams.get("paySign");
        req.extData = "app data";

        String packageName = getPackageName();
        if (packageName.equals(Constant.PHYSICMASTER)) {
            api.registerApp(Constant.PM_WEIXIN_APP_ID);
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            api.registerApp(Constant.CM_WEIXIN_APP_ID);
        } else if (packageName.equals(Constant.MATHMASTER)) {
            api.registerApp(Constant.MM_WEIXIN_APP_ID);
        }
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    /**
     * 微信代付
     */
    private void sharedToPay(Map<String, String> payParams, SHARE_MEDIA media) {
        String shareImageUrl = payParams.get("shareImg");
        String shareTitle = payParams.get("shareTitle");
        String desc = payParams.get("shareDesc");
        String shareUrl = payParams.get("shareUrl");
        UMImage image = null;
        String packageName = getPackageName();
        if (!TextUtils.isEmpty(shareImageUrl)) {
            image = new UMImage(WebviewActivity.this, shareImageUrl);
        } else {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                image = new UMImage(WebviewActivity.this, R.mipmap.icon_physic);
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                image = new UMImage(WebviewActivity.this, R.mipmap.icon_chemistory);
            } else if (packageName.equals(Constant.MATHMASTER)) {
                image = new UMImage(WebviewActivity.this, R.mipmap.icon_math);
            }
        }
        if (TextUtils.isEmpty(desc)) {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                desc = "物理大师，科技、教育、艺术跨界融合，教师必备课堂教学大片。";
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                desc = "化学大师，科技、教育、艺术跨界融合，教师必备课堂教学大片。";
            } else if (packageName.equals(Constant.MATHMASTER)) {
                desc = "数学大师，科技、教育、艺术跨界融合，教师必备课堂教学大片。";
            }
        }
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);
        web.setDescription(desc);
        web.setThumb(image);
        shareAction = new ShareAction(WebviewActivity.this);
        shareAction.setPlatform(media).setCallback(new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);
                UIUtils.showToast(WebviewActivity.this, "分享成功啦");
                if (platform == SHARE_MEDIA.QQ) {
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                UIUtils.showToast(WebviewActivity.this, "分享失败啦");
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                UIUtils.showToast(WebviewActivity.this, "分享取消了");
            }
        }).withMedia(web).share();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                //支付成功,去服务端查询支付结果
                mPayResultFunction.onCallBack("{\"code\":200}");
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                UIUtils.showToast(WebviewActivity.this, "支付失败");
                mPayResultFunction.onCallBack("{\"code\":-1,\"msg\":\"用户取消支付\"}");
            }
            return true;
        }
    });

    /**
     * 获取当前位置信息
     *
     * @return
     */
    private void getLocation(final CallBackFunction function) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationProvider netProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                        .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission
                                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // 申请权限
                    ActivityCompat.requestPermissions(this, DANGEROUS_PERMISSION,
                            LOCATION_REQUEST_CODE);
                } else {
                    locationManager.requestLocationUpdates(netProvider.getName(), 10000L, 10F, new
                            LocationListener() {

                                @Override
                                public void onLocationChanged(Location location) {
                                    requestLocation(location, function);
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle
                                        extras) {

                                }

                                @Override
                                public void onProviderEnabled(String provider) {

                                }

                                @Override
                                public void onProviderDisabled(String provider) {

                                }
                            });
                }
            } else {
                locationManager.requestLocationUpdates(netProvider.getName(), 10000L, 10F, new
                        LocationListener() {

                            @Override
                            public void onLocationChanged(Location location) {
                                requestLocation(location, function);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle
                                    extras) {
                                System.out.println();
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                                System.out.println();
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                System.out.println();
                            }
                        });
            }
        } else {
            UIUtils.showToast(WebviewActivity.this, "为了更好地用户体验，请打开定位服务");
        }
    }

    // 获取当前详细地址
    private void requestLocation(Location location, final CallBackFunction function) {
        StringBuffer sb = new StringBuffer();
        Geocoder gc = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            sb.append(address.getAddressLine(1));
        }
        JSONObject retJson = new JSONObject();
        JSONObject jsonData = new JSONObject();
        jsonData.put("latitude", location.getLatitude());
        jsonData.put("longitude", location.getLongitude());
        jsonData.put("accuracy", location.getAccuracy());
        jsonData.put("speed", location.getSpeed());
        jsonData.put("description", sb.toString());
        retJson.put("data", jsonData);
        retJson.put("code", 200);
        function.onCallBack(retJson.toJSONString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(savedFunction);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (weixinSharedReceiver != null) {
            localBroadcastManager.unregisterReceiver(weixinSharedReceiver);
        }
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        if (shareAction != null) {
            shareAction.close();
        }
    }
}
