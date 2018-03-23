package com.physicmaster.modules.study.activity.exercise;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.ExitDialogFragment;
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.modules.discuss.activity.AllQuestionActivity;
import com.physicmaster.modules.explore.activity.MembersActivity;
import com.physicmaster.modules.mine.activity.question.AnswerDetailsActivity;
import com.physicmaster.modules.study.fragment.dialogfragment.CommonDialogFragment;
import com.physicmaster.modules.videoplay.VideoPlayV2Activity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.AuthCodeResponse;
import com.physicmaster.net.response.excercise.GetVideoDetailsResponse;
import com.physicmaster.net.response.excercise.GetVideoDetailsResponse.DataBean.AppVideoStudyVoBean;
import com.physicmaster.net.response.explore.GetQrVideoResponse;
import com.physicmaster.net.security.Base64Decoder;
import com.physicmaster.net.service.account.GetAuthCodeService;
import com.physicmaster.net.service.excercise.GetVideoDetailsService;
import com.physicmaster.net.service.explore.GetQrVideoService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExcerciseDetailActivity extends BaseActivity {

    private String chapterId;
    private TextView tvDesc;
    private int position;
    private AppVideoStudyVoBean videoStudyVoBean;
    private String trySnUrl;
    private String qrData;
    private TitleBuilder titleBuilder;

    @Override
    protected void findViewById() {
        webView = (BridgeWebView) findViewById(R.id.webView);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        initTitle();
    }

    private void initTitle() {
        titleBuilder = new TitleBuilder(this);
        titleBuilder.setLeftImageRes(R.mipmap.fanhui).setLeftText("返回").setLeftTextOrImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        videoId = getIntent().getIntExtra("videoId", 0);
        chapterId = getIntent().getStringExtra("chapterId");
        position = getIntent().getIntExtra("position", -1);
        qrData = getIntent().getStringExtra("qrData");
        findViewById(R.id.ib_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoStudyVoBean != null) {
                    Intent intent = new Intent(ExcerciseDetailActivity.this, VideoPlayV2Activity.class);
                    intent.putExtra("videoId", videoStudyVoBean.videoId + "");
                    intent.putExtra("chapterId", videoStudyVoBean.chapterId + "");
                    startActivityForResult(intent, 1);
                } else {
                    if (BaseApplication.getUserData().isTourist == 1) {
                        Utils.gotoLogin(ExcerciseDetailActivity.this);
                        return;
                    }
                    CommonDialogFragment buyFragment = new CommonDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "购买会员即可观看视频");
                    if (!TextUtils.isEmpty(trySnUrl)) {
                        bundle.putBoolean("showAction2", true);
                        buyFragment.setOnActionBtnClickListener2(new CommonDialogFragment.OnActionBtnClickListener() {
                            @Override
                            public void onLick() {
                                TrySnDialogFragment dialogFragment = new TrySnDialogFragment();
                                dialogFragment.setOnBackListener(new TrySnDialogFragment.OnBackListener() {
                                    @Override
                                    public void onBack() {
                                        if (TextUtils.isEmpty(qrData)) {
                                            getVideoDetail(videoId);
                                        } else {
                                            getVideoDetailByQrData(qrData);
                                        }
                                    }
                                });
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("url", trySnUrl);
                                dialogFragment.setArguments(bundle1);
                                dialogFragment.show(getSupportFragmentManager(), "trySn");
                            }
                        });
                    }
                    buyFragment.setOnActionBtnClickListener(new CommonDialogFragment.OnActionBtnClickListener() {
                        @Override
                        public void onLick() {
                            startActivity(new Intent(ExcerciseDetailActivity.this, MembersActivity.class));
                        }
                    });
                    buyFragment.setArguments(bundle);
                    buyFragment.show(getSupportFragmentManager(), "buyMember");
                }
            }
        });
        if (TextUtils.isEmpty(qrData)) {
            getVideoDetail(videoId);
        } else {
            getVideoDetailByQrData(qrData);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_excercise_detail;
    }

    private void getVideoDetailByQrData(String s) {
        final GetQrVideoService service = new GetQrVideoService(ExcerciseDetailActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GetQrVideoResponse>() {
            @Override
            public void onGetData(GetQrVideoResponse data) {
                initUi(data.data);
                trySnUrl = data.data.trySnUrl;
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(ExcerciseDetailActivity.this, errorMsg);
            }
        });
        service.postLogined("qrCode=" + s, false);
    }

    private void getVideoDetail(int videoId) {
        GetVideoDetailsService service = new GetVideoDetailsService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetVideoDetailsResponse>() {
            @Override
            public void onGetData(GetVideoDetailsResponse data) {
                String replacement1 = "";
                String replacement2 = "";
                if (!TextUtils.isEmpty(data.data.replacement1)) {
                    replacement1 = Base64Decoder.decode(data.data.replacement1);
                }
                if (!TextUtils.isEmpty(data.data.replacement2)) {
                    replacement2 = Base64Decoder.decode(data.data.replacement2);
                }
                InputStream inputStream = null;
                StringBuilder quPreview = new StringBuilder();
                try {
                    inputStream = getAssets().open("qu_review.html");
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String tempStr = "";
                    while ((tempStr = bufferedReader.readLine()) != null) {
                        quPreview.append(tempStr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String question = quPreview.toString().replace
                        ("###REPLACEMENT1###",
                                replacement1).replace("###REPLACEMENT2###", replacement2);
                webView.loadDataWithBaseURL("file:///android_asset/", question, "text/html",
                        "UTF-8", "");
                String desc = "视频讲解包含题目，可直接观看\'";
                if (data.data.appVideoStudyVo != null) {
                    desc += (data.data.appVideoStudyVo.timeLengthStr + "\'");
                }
                tvDesc.setText(desc);
                titleBuilder.setMiddleTitleText(data.data.appVideoStudyVo.videoTitle);
                trySnUrl = data.data.trySnUrl;
                videoStudyVoBean = data.data.appVideoStudyVo;
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.postLogined("videoId=" + videoId + "&chapterId=" + chapterId, false);
    }

    private void initUi(GetQrVideoResponse.DataBean studyData) {
        videoStudyVoBean = studyData.appVideoStudyVo;
        if (videoStudyVoBean != null) {
            String title = videoStudyVoBean.videoTitle;
            if (!TextUtils.isEmpty(title)) {
                titleBuilder.setMiddleTitleText(title);
            }
        }
        String replacement1 = "";
        String replacement2 = "";
        if (!TextUtils.isEmpty(studyData.replacement1)) {
            replacement1 = Base64Decoder.decode(studyData.replacement1);
        }
        if (!TextUtils.isEmpty(studyData.replacement2)) {
            replacement2 = Base64Decoder.decode(studyData.replacement2);
        }
        InputStream inputStream = null;
        StringBuilder quPreview = new StringBuilder();
        try {
            inputStream = getAssets().open("qu_review.html");
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String tempStr = "";
            while ((tempStr = bufferedReader.readLine()) != null) {
                quPreview.append(tempStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String question = quPreview.toString().replace
                ("###REPLACEMENT1###",
                        replacement1).replace("###REPLACEMENT2###", replacement2);
        webView.loadDataWithBaseURL("file:///android_asset/", question, "text/html",
                "UTF-8", "");
        String desc = "视频讲解包含题目，可直接观看\'";
        if (studyData.appVideoStudyVo != null) {
            desc += (studyData.appVideoStudyVo.timeLengthStr + "\'");
        }
        tvDesc.setText(desc);
    }

    private BridgeWebView webView;
    private int videoId;
    private static final String TAG = "ExcerciseActivity2";
    private ProgressLoadingDialog progressLoadingDialog;
    ValueCallback<Uri> mUploadMessage;

    @Override
    protected void initView() {
        WebSettings settings = webView.getSettings();
        StringBuilder builder = new StringBuilder();
        String userAgent = webView.getSettings().getUserAgentString();
        builder.append(userAgent);
        builder.append(" PhysicMaster/");
        builder.append(getVersion());
        builder.append(" Network/");
        builder.append(getNetworkState());
        builder.append(" DevicePixelRatio/" + BaseApplication.getDensity());
        settings.setUserAgentString(builder.toString());
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setDefaultHandler(new DefaultHandler());
        webView.getSettings().setUserAgentString(builder.toString());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new ExcerciseDetailActivity.CreateJsbridgeInterface(),
                "__wj_bridge_loaded__");
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
            }
        });
        webView.setWebViewClient(new BridgeWebViewClient(webView) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
        videoId = getIntent().getIntExtra("videoId", 0);
    }

    private int RESULT_CODE = 0;

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    //创建businessJsBridge的jsBridge
    public class CreateJsbridgeInterface {
        @JavascriptInterface
        public void setup() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
                    webView.registerHandler("showLoading", new BridgeHandler() {

                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = showLoading, data from web = " + data);
                        }
                    });
                    webView.registerHandler("hideLoading", new BridgeHandler() {

                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = hideLoading, data from web = " + data);
                            if (progressLoadingDialog != null) {
                                progressLoadingDialog.dismissDialog();
                            }
                        }
                    });
                    webView.registerHandler("getLoginAuthCode", new BridgeHandler() {

                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                            getAuthCode(function);
                        }

                    });
                    webView.registerHandler("quit", new BridgeHandler() {

                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = quit, data from web = " + data);
                            JSONObject jsonObject = JSONObject.parseObject(data);
                            String type = jsonObject.getString("type");
                            if (type.equals("0")) {
                                ExcerciseDetailActivity.this.finish();
                            } else if (type.equals("1")) {
                                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                                    @Override
                                    public void ok() {
                                        ExcerciseDetailActivity.this.finish();
                                    }
                                });
                                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                            }
                        }
                    });
                }
            });
        }
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
                UIUtils.showToast(ExcerciseDetailActivity.this, "获取授权码失败：" + errorMsg);
            }
        });
        service.postLogined("", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (1 == requestCode) {
            boolean playFinish = data.getBooleanExtra("playFinish", false);
            Intent intent = new Intent();
            if (playFinish) {
                intent.putExtra("position", position);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
