package com.physicmaster.modules.course.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.course.GetCourseDetailReponse;
import com.physicmaster.net.service.course.CourseDetailService;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CourseDetailActivity extends BaseActivity {

    private WebView webView;
    private ProgressBar webLoadingBar;
    private Button btnBuy;
    private int courseId;
    private int screenWidth;
    private TextView tvPrice;
    private LocalBroadcastManager localBroadcastManager;
    public static final String PAY_SUCCSS = "pay_success";

    @Override
    protected void findViewById() {
    }

    @Override
    protected void initView() {
        initTitle();
        webView = (WebView) findViewById(R.id.webView);
        screenWidth = ScreenUtils.getScreenWidth();
        webLoadingBar = ((ProgressBar) findViewById(R.id.web_loading_bar));
        webLoadingBar.setMax(screenWidth);
        webLoadingBar.setProgress(screenWidth * 10 / 100);

        btnBuy = (Button) findViewById(R.id.btn_buy);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        StringBuilder builder = new StringBuilder();
        String userAgent = webView.getSettings().getUserAgentString();
        builder.append(userAgent);
        builder.append(" PhysicMaster/");
        builder.append(getVersion());
        builder.append(" Network/");
        builder.append(getNetworkState());
        builder.append(" DevicePixelRatio/" + BaseApplication.getDensity());
        webView.getSettings().setUserAgentString(builder.toString());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
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
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        courseId = getIntent().getIntExtra("courseId", 0);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(paySuccessReceiver, new IntentFilter(PAY_SUCCSS));
        getData(courseId);

    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("课程详情");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_course_detail;
    }

    private void getData(int itemId) {
        CourseDetailService service = new CourseDetailService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetCourseDetailReponse>() {
            @Override
            public void onGetData(GetCourseDetailReponse data) {
                try {
                    String content = data.data.courseDetailVo.appIntroduction;
                    InputStream inputStream = null;
                    StringBuilder courseDetail = new StringBuilder();
                    inputStream = getAssets().open("course_detail.html");
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String tempStr = "";
                    while ((tempStr = bufferedReader.readLine()) != null) {
                        courseDetail.append(tempStr);
                    }
                    String urlContent = courseDetail.toString().replace
                            ("###REPLACEMENT###",
                                    content);
                    webView.loadDataWithBaseURL("file:///android_asset/", urlContent,
                            "text/html", "utf-8", "");
                    tvPrice.setText("￥" + data.data.courseDetailVo.priceYuan);
                    if (data.data.courseDetailVo.alreadyBuy == 1) {
                        tvPrice.setVisibility(View.GONE);
                        btnBuy.setText("已购买");
                    } else {
                        if (data.data.courseDetailVo.price == 0) {
                            btnBuy.setText("免费");
                        } else {
                            btnBuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MobclickAgent.onEvent(CourseDetailActivity.this,
                                            "course_detail_pay_immediately");
                                    Intent intent = new Intent(CourseDetailActivity.this,
                                            PaymentActivity.class);
                                    intent.putExtra("courseId", courseId);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
            }
        });
        service.postLogined("courseId=" + itemId, true);
    }

    private BroadcastReceiver paySuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tvPrice.setVisibility(View.GONE);
            btnBuy.setText("已购买");
            btnBuy.setEnabled(false);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (paySuccessReceiver != null) {
            localBroadcastManager.unregisterReceiver(paySuccessReceiver);
        }
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
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
        return "2.0.0";
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

}
