package com.physicmaster.modules.study.activity.exercise;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

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
import com.physicmaster.modules.study.fragment.dialogfragment.Energy2DialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.excercise.CommitAnswerResponse;
import com.physicmaster.net.response.excercise.ExploreV2Response;
import com.physicmaster.net.security.Base64Decoder;
import com.physicmaster.net.service.excercise.CommitAnswerService;
import com.physicmaster.net.service.excercise.ExploreV2Service;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExcerciseV2Activity extends BaseActivity {

    private BridgeWebView webView;
    private int videoId;
    private static final String TAG = "ExcerciseActivity2";
    private ProgressLoadingDialog progressLoadingDialog;
    ValueCallback<Uri> mUploadMessage;
    private String chapterId;

    @Override
    protected void findViewById() {
    }

    private void initWebView() {
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
        webView.addJavascriptInterface(new ExcerciseV2Activity.CreateJsbridgeInterface(),
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
    }

    @Override
    protected void initView() {
        webView = (BridgeWebView) findViewById(R.id.webView);
        initWebView();
        videoId = getIntent().getIntExtra("videoId", 0);
        chapterId = getIntent().getStringExtra("chapterId");
        getQuestionInfo(videoId);
    }

    private int RESULT_CODE = 0;

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    /**
     * 获取闯关习题信息
     */
    private void getQuestionInfo(int videoId) {
        progressLoadingDialog = new ProgressLoadingDialog(this);
        progressLoadingDialog.showDialog(null);
        ExploreV2Service service = new ExploreV2Service(this);
        service.setCallback(new IOpenApiDataServiceCallback<ExploreV2Response>() {
            @Override
            public void onGetData(ExploreV2Response data) {
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
                    inputStream = getAssets().open("qu_preview.html");
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
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(ExcerciseV2Activity.this, errorMsg);
                if (errorCode == 502) {
                    new Energy2DialogFragment()
                            .show(getSupportFragmentManager(), "energy_dialog");
                }
            }
        });
        service.postLogined("videoId=" + videoId + "&chapterId=" + chapterId, true);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_excersise2;
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
                    webView.registerHandler("quit", new BridgeHandler() {

                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = quit, data from web = " + data);
                            JSONObject jsonObject = JSONObject.parseObject(data);
                            String type = jsonObject.getString("type");
                            if (type.equals("0")) {
                                ExcerciseV2Activity.this.finish();
                            } else if (type.equals("1")) {
                                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                                    @Override
                                    public void ok() {
                                        ExcerciseV2Activity.this.finish();
                                    }
                                });
                                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                            }
                        }
                    });
                    webView.registerHandler("submitPreviewQuBatch", new BridgeHandler() {

                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = submitPreviewQuBatch, data from web = " + data);
                            JSONObject jsonObject = JSONObject.parseObject(data);
                            String answerJson = jsonObject.getString("answerJson");
                            summitAnswer2Server(answerJson);
                        }
                    });
                }
            });
        }
    }

    private void summitAnswer2Server(String answerJson) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final CommitAnswerService service = new CommitAnswerService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommitAnswerResponse>() {
            @Override
            public void onGetData(CommitAnswerResponse data) {
                loadingDialog.dismissDialog();
                if (data.data.useType == 1) {
                    Intent intent = new Intent(ExcerciseV2Activity.this,
                            BreakthoughFinishActivity.class);
                    intent.putExtra("preview_result", data.data.exploreResult);
                    intent.putExtra("videoId", videoId);
                    startActivityForResult(intent, 1);
                } else if (data.data.useType == 2) {
                    Intent intent = new Intent(ExcerciseV2Activity.this, FinishActivity.class);
                    intent.putExtra("review_result", data.data.isFinished);
                    intent.putExtra("videoId", videoId);
                    startActivityForResult(intent, 2);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(ExcerciseV2Activity.this, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("answerJson=" + answerJson, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (1 == requestCode) {
                int action = data.getIntExtra("action", -1);
                if (1 == action) {
                    webView.callHandler("onShowAnalysis", "", new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {

                        }
                    });
                } else if (2 == action) {
                    //加载空页面
                    webView.removeJavascriptInterface("__wj_bridge_loaded__");
                    initWebView();
//                    webView.loadDataWithBaseURL("file:///android_asset/", "<html></html>", "text/html", "UTF-8", "");
                    getQuestionInfo(videoId);
                } else if (3 == action) {
                    finish();
                }
            } else if (2 == requestCode) {
                int action = data.getIntExtra("action", -1);
                if (1 == action) {
                    webView.removeJavascriptInterface("__wj_bridge_loaded__");
                    initWebView();
//                    webView.loadDataWithBaseURL("file:///android_asset/", "<html></html>", "text/html", "UTF-8", "");
                    getQuestionInfo(videoId);
                } else if (2 == action) {
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
        exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
            @Override
            public void ok() {
                ExcerciseV2Activity.this.finish();
            }
        });
        exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
    }
}
