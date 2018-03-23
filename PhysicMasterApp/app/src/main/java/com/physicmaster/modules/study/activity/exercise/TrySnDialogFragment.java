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
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.AuthCodeResponse;
import com.physicmaster.net.response.user.PetInfoResponse;
import com.physicmaster.net.service.account.GetAuthCodeService;
import com.physicmaster.utils.UIUtils;

/**
 * Created by huashigen on 2017-08-07.
 */

public class TrySnDialogFragment extends DialogFragment {

    private BridgeWebView webView;
    private static final String TAG = "TrySnDialogFragment";
    ValueCallback<Uri> mUploadMessage;
    private String url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
        url = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_trysn, container, false);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        webView = (BridgeWebView) view.findViewById(R.id.webView);
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
        webView.addJavascriptInterface(new CreateJsbridgeInterface(),
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
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    private int RESULT_CODE = 0;

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    // 检测网络状态
    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    // 获取版本信息
    public String getVersion() {
        PackageManager manager = getActivity().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
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
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        int nType = info.getType();
        if (ConnectivityManager.TYPE_WIFI == nType) {
            return Constant.NETTYPE_WIFI;
        } else if (ConnectivityManager.TYPE_MOBILE == nType) {
            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context
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
            getActivity().runOnUiThread(new Runnable() {
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
                        }
                    });
                    webView.registerHandler("getLoginAuthCode", new BridgeHandler() {

                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                            getAuthCode(function);
                        }

                    });
                    webView.registerHandler("close", new BridgeHandler() {
                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Log.i(TAG, "handler = quit, data from web = " + data);
                            if (onBackListener != null) {
                                onBackListener.onBack();
                            }
                            dismiss();
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
        GetAuthCodeService service = new GetAuthCodeService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<AuthCodeResponse>() {
            @Override
            public void onGetData(AuthCodeResponse data) {
                function.onCallBack(JSON.toJSONString(data));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getActivity(), "获取授权码失败：" + errorMsg);
            }
        });
        service.postLogined("", false);
    }

    private OnBackListener onBackListener;

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public interface OnBackListener {
        public void onBack();
    }
}
