package com.physicmaster.modules.mine.activity.topicmap;

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
import com.google.gson.Gson;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.ExitDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.excercise.GetWrongQuListV2Response;
import com.physicmaster.net.response.excercise.WipeOutTopicmapResponse;
import com.physicmaster.net.security.Base64Decoder;
import com.physicmaster.net.service.excercise.GetWrongQuListV2Service;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TopicmapV2Activity extends BaseActivity {

    private BridgeWebView webView;
    private int chapterId;
    private static final String TAG = "TopicmapV2Activity";
    ValueCallback<Uri> mUploadMessage;
    private WipeOutTopicmapResponse.DataBean quBatchIdObj;
    private ProgressLoadingDialog progressLoadingDialog;

    @Override
    protected void findViewById() {
    }

    @Override
    protected void initView() {
        webView = (BridgeWebView) findViewById(R.id.webView);
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
        webView.addJavascriptInterface(new TopicmapV2Activity.CreateJsbridgeInterface(),
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
        chapterId = getIntent().getIntExtra("chapterId", 0);
        getQuestionInfo(chapterId);
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
    private void getQuestionInfo(int chapterId) {
        progressLoadingDialog = new ProgressLoadingDialog(this);
        progressLoadingDialog.showDialog(null);
        GetWrongQuListV2Service service = new GetWrongQuListV2Service(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetWrongQuListV2Response>() {
            @Override
            public void onGetData(GetWrongQuListV2Response data) {
                String replacement1 = "";
                String replacement2 = "";
                if (!TextUtils.isEmpty(data.data.replacement1)) {
                    replacement1 = Base64Decoder.decode(data.data.replacement1);
                }
                if (!TextUtils.isEmpty(data.data.replacement2)) {
                    replacement2 = Base64Decoder.decode(data.data.replacement2);
                }
                InputStream inputStream = null;
                StringBuilder quWrong = new StringBuilder();
                try {
                    inputStream = getAssets().open("qu_wrong.html");
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String tempStr = "";
                    while ((tempStr = bufferedReader.readLine()) != null) {
                        quWrong.append(tempStr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String question = quWrong.toString().replace("###REPLACEMENT1###", replacement1).replace("###REPLACEMENT2###", replacement2);
                webView.loadDataWithBaseURL("file:///android_asset/", question, "text/html", "UTF-8", "");
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(TopicmapV2Activity.this, errorMsg);
            }
        });
        service.postLogined("chapterId=" + chapterId, true);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_topicmap_v2;
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
                                TopicmapV2Activity.this.finish();
                            } else if (type.equals("1")) {
                                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                                    @Override
                                    public void ok() {
                                        TopicmapV2Activity.this.finish();
                                    }
                                });
                                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                            }
                        }
                    });
                    webView.registerHandler("afterShowAnalysisForWrong", new BridgeHandler() {

                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = afterShowAnalysisForWrong, data from web = " +
                                    data);
                            Intent intent = new Intent(TopicmapV2Activity.this,
                                    WipeOutTopicmapActivity.class);
                            intent.putExtra("chapterId", chapterId);
                            startActivityForResult(intent, 1);
                        }
                    });
                    webView.registerHandler("submitWrongQuBatch", new BridgeHandler() {

                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = submitWrongQuBatch, data from web = " +
                                    data);
                            JSONObject jsonObject = JSONObject.parseObject(data);
                            String answerJsonStr = jsonObject.getString("answerJson");
                            int quCount = jsonObject.getInteger("quCount");
                            int correctCount = jsonObject.getInteger("correctCount");
                            Intent intent = new Intent(TopicmapV2Activity.this,
                                    TopicmapFinishActivity.class);
                            intent.putExtra("answerJson", answerJsonStr);
                            intent.putExtra("chapterId", chapterId);
                            intent.putExtra("quCount", quCount);
                            intent.putExtra("correctCount", correctCount);
                            startActivityForResult(intent, 2);
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (1 == requestCode) {
                int action = data.getIntExtra("action", -1);
                if (1 == action) {
                    quBatchIdObj = (WipeOutTopicmapResponse
                            .DataBean) data.getSerializableExtra("quBatchId");
                    String jsonString = new Gson().toJson(quBatchIdObj);
                    webView.callHandler("onExerciseWrong", jsonString, new
                            CallBackFunction() {
                                @Override
                                public void onCallBack(String data) {
                                    Log.i(TAG, "handler = onExerciseWrong, data from web = " +
                                            data);
                                }
                            });
                } else if (2 == action) {
                    webView.callHandler("onShowAnalysis", "", new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            Log.i(TAG, "handler = onShowAnalysis, data from web = " + data);
                        }
                    });
                } else if (3 == action) {
                    finish();
                }
            } else if (2 == requestCode) {
                int action = data.getIntExtra("action", -1);
                //用户点击消灭错题或者退出，销毁activity
                if (1 == action || 3 == action) {
                    finish();
                }
                //用户点击重新挑战
                else if (2 == action) {
                    quBatchIdObj = (WipeOutTopicmapResponse
                            .DataBean) data.getSerializableExtra("quBatchId");
                    String jsonString = new Gson().toJson(quBatchIdObj);
                    webView.callHandler("onExerciseWrong", jsonString, new
                            CallBackFunction() {
                                @Override
                                public void onCallBack(String data) {
                                    Log.i(TAG, "handler = onExerciseWrong, data from web = " +
                                            data);
                                }
                            });
                }
            }
        }
    }
}
