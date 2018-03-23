package com.physicmaster.modules.mine.activity.notebook;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.notebook.AddDirResponse;
import com.physicmaster.net.response.notebook.GetPoolListRespose;
import com.physicmaster.net.response.notebook.InitWrongPoolResponse;
import com.physicmaster.net.response.notebook.RecordWrongResponse;
import com.physicmaster.net.security.Base64Decoder;
import com.physicmaster.net.service.notebook.AddTagService;
import com.physicmaster.net.service.notebook.ArchiveQuFromDirService;
import com.physicmaster.net.service.notebook.GetDirQuListService;
import com.physicmaster.net.service.notebook.GetPoolQuListService;
import com.physicmaster.net.service.notebook.InitWrongDirService;
import com.physicmaster.net.service.notebook.InitWrongPoolService;
import com.physicmaster.net.service.notebook.RecordSysWrongService;
import com.physicmaster.net.service.notebook.RmQuFromDirService;
import com.physicmaster.net.service.notebook.RmQuFromPoolService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NoteBookWebActivity extends BaseActivity {

    private BridgeWebView webView;
    private String subjectId;
    private static final String TAG = "NoteBookWebActivity";
    private ProgressLoadingDialog progressLoadingDialog;
    ValueCallback<Uri> mUploadMessage;
    private int pageType;
    private String dirId;

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
        webView.getSettings().setUserAgentString(builder.toString());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new NoteBookWebActivity.CreateJsbridgeInterface(),
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
        webView = findViewById(R.id.webView);
        initWebView();
        subjectId = getIntent().getStringExtra("subjectId");
        pageType = getIntent().getIntExtra("pageType", -1);
        if (pageType == 0) {
            getNoteBookPageInfo();
        } else if (pageType == 1) {
            dirId = getIntent().getStringExtra("dirId");
            getNoteBookDirInfo(subjectId, dirId);
        }
    }

    private int RESULT_CODE = 0;

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    /**
     * 获取错题池页面信息
     */
    private void getNoteBookPageInfo() {
        progressLoadingDialog = new ProgressLoadingDialog(this);
        progressLoadingDialog.showDialog(null);
        InitWrongPoolService service = new InitWrongPoolService(this);
        service.setCallback(new IOpenApiDataServiceCallback<InitWrongPoolResponse>() {
            @Override
            public void onGetData(InitWrongPoolResponse data) {
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
                    inputStream = getAssets().open("wrong_pool.html");
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
                UIUtils.showToast(NoteBookWebActivity.this, errorMsg);
                if (errorCode == 502) {
                    new Energy2DialogFragment()
                            .show(getSupportFragmentManager(), "energy_dialog");
                }
            }
        });
        service.postLogined("subjectId=" + subjectId, true);
    }

    /**
     * 获取错题目录页面信息
     */
    private void getNoteBookDirInfo(String subjectId, String dirId) {
        progressLoadingDialog = new ProgressLoadingDialog(this);
        progressLoadingDialog.showDialog(null);
        InitWrongDirService service = new InitWrongDirService(this);
        service.setCallback(new IOpenApiDataServiceCallback<InitWrongPoolResponse>() {
            @Override
            public void onGetData(InitWrongPoolResponse data) {
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
                    inputStream = getAssets().open("wrong.html");
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
                UIUtils.showToast(NoteBookWebActivity.this, errorMsg);
                if (errorCode == 502) {
                    new Energy2DialogFragment()
                            .show(getSupportFragmentManager(), "energy_dialog");
                }
            }
        });
        service.postLogined("subjectId=" + subjectId + "&dirId=" + dirId, true);
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
            runOnUiThread(() -> {
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
                webView.registerHandler("showLoading", (data, function) -> Log.i(TAG, "handler = showLoading, data from web = " + data));
                webView.registerHandler("hideLoading", (data, function) -> {
                    Log.i(TAG, "handler = hideLoading, data from web = " + data);
                    if (progressLoadingDialog != null) {
                        progressLoadingDialog.dismissDialog();
                    }
                });
                webView.registerHandler("quit", (data, function) -> {
                    Log.i(TAG, "handler = quit, data from web = " + data);
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    String type = jsonObject.getString("type");
                    if (type.equals("0")) {
                        setResult(RESULT_OK);
                        NoteBookWebActivity.this.finish();
                    } else if (type.equals("1")) {
                        ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                        exitDialogFragment.setExitListener(() -> NoteBookWebActivity.this.finish());
                        exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                    }
                });
                webView.registerHandler("loadWrongPool", (data, function) -> {
                    Log.i(TAG, "handler = loadWrongPool, data from web = " + data);
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    String subjectId = jsonObject.getString("subjectId");
                    String next = jsonObject.getString("next");
                    getPoolQuList(subjectId, next, function);
                });
                webView.registerHandler("saveQuWrong", (data, function) -> {
                    Log.i(TAG, "handler = loadWrongPool, data from web = " + data);
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    StringBuilder sbParam = new StringBuilder();
                    String id = jsonObject.getString("id");
                    sbParam.append("id=" + id);
                    String difficultyLevel = jsonObject.getString("difficultyLevel");
                    if (!TextUtils.isEmpty(difficultyLevel)) {
                        sbParam.append("&difficultyLevel=" + difficultyLevel);
                    }
                    String masterLevel = jsonObject.getString("masterLevel");
                    if (!TextUtils.isEmpty(masterLevel)) {
                        sbParam.append("&masterLevel=" + masterLevel);
                    }
                    String tagId = jsonObject.getString("tagId");
                    if (!TextUtils.isEmpty(tagId)) {
                        sbParam.append("&tagId=" + tagId);
                    }
                    String newTagName = jsonObject.getString("newTagName");
                    if (!TextUtils.isEmpty(newTagName)) {
                        sbParam.append("&newTagName=" + newTagName);
                    }
                    String dirId = jsonObject.getString("dirId");
                    if (!TextUtils.isEmpty(dirId)) {
                        sbParam.append("&dirId=" + dirId);
                    }
                    String newDirName = jsonObject.getString("newDirName");
                    if (!TextUtils.isEmpty(newDirName)) {
                        sbParam.append("&newDirName=" + newDirName);
                    }
                    recordSysWrong(sbParam.toString());
                });
                //获取错题目录中的错题
                webView.registerHandler("loadWrongByDir", (data, function) -> {
                    Log.i(TAG, "handler = loadWrongByDir, data from web = " + data);
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    String dirId = jsonObject.getString("dirId");
                    String sort = jsonObject.getString("sort");
                    String desc = jsonObject.getString("desc");
                    String next = jsonObject.getString("next");
                    getDirQuList(dirId, next, sort, desc, function);
                });
                //从错题目录中删除错题
                webView.registerHandler("removeWrong", (data, function) -> {
                    Log.i(TAG, "handler = removeWrong, data from web = " + data);
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    String quId = jsonObject.getString("id");
                    rmQuFromDir(subjectId, quId, function);
                });
                //从错题池中删除错题
                webView.registerHandler("removeFromWrongPool", (data, function) -> {
                    Log.i(TAG, "handler = removeFromWrongPool, data from web = " + data);
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    String quId = jsonObject.getString("id");
                    rmQuFromPool(quId, function);
                });
                //归档错题
                webView.registerHandler("archiveWrong", (data, function) -> {
                    Log.i(TAG, "handler = archiveWrong, data from web = " + data);
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    String quId = jsonObject.getString("id");
                    archiveQuFroDir(subjectId, quId, function);
                });
                //新增错题目录
                webView.registerHandler("addWrongDir", (data, function) -> {
                    Log.i(TAG, "handler = addWrongDir, data from web = " + data);
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    String subjectId = jsonObject.getString("subjectId");
                    String dirName = jsonObject.getString("dirName");
                    addTag(subjectId, dirName, function);
                });
            });
        }
    }

    /**
     * 提交目录到服务器
     *
     * @param name
     */
    private void addTag(String subjectId, String name, CallBackFunction callBack) {
        final AddTagService service = new AddTagService(this);
        final ProgressLoadingDialog dialog = new ProgressLoadingDialog(this);
        dialog.showDialog(() -> service.cancel());
        service.setCallback(new IOpenApiDataServiceCallback<AddDirResponse>() {
            @Override
            public void onGetData(AddDirResponse data) {
                dialog.dismissDialog();
                callBack.onCallBack(JSON.toJSONString(data));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                dialog.dismissDialog();
                JSONObject jsData = new JSONObject();
                jsData.put("code", errorCode);
                jsData.put("msg", errorMsg);
                callBack.onCallBack(JSON.toJSONString(jsData));
            }
        });
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(name, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.postLogined("subjectId=" + subjectId + "&dirName=" + encodeName, false);
    }


    /**
     * 系统录入错题
     *
     * @param param
     */
    private void recordSysWrong(String param) {
        RecordSysWrongService service = new RecordSysWrongService(NoteBookWebActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<RecordWrongResponse>() {

            @Override
            public void onGetData(RecordWrongResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                startActivity(new Intent(NoteBookWebActivity.this, NoteBookActivity.class));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());

            }
        });
        service.postLogined(param, false);
    }

    /**
     * 从错题目录中删除错题
     *
     * @param subjectId
     * @param quId
     */
    private void rmQuFromDir(String subjectId, String quId, CallBackFunction function) {
        RmQuFromDirService service = new RmQuFromDirService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                function.onCallBack(JSON.toJSONString(data));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                JSONObject jsData = new JSONObject();
                jsData.put("code", errorCode);
                jsData.put("msg", errorMsg);
                function.onCallBack(JSON.toJSONString(jsData));
            }
        });
        service.postLogined("subjectId=" + subjectId + "&id=" + quId, false);
    }

    /**
     * 从错题池中删除错题
     *
     * @param quId
     */
    private void rmQuFromPool(String quId, CallBackFunction function) {
        RmQuFromPoolService service = new RmQuFromPoolService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                function.onCallBack(JSON.toJSONString(data));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                JSONObject jsData = new JSONObject();
                jsData.put("code", errorCode);
                jsData.put("msg", errorMsg);
                function.onCallBack(JSON.toJSONString(jsData));
            }
        });
        service.postLogined("id=" + quId, false);
    }

    /**
     * 掌握归档错题目录中的错题
     *
     * @param subjectId
     * @param quId
     */
    private void archiveQuFroDir(String subjectId, String quId, CallBackFunction function) {
        ArchiveQuFromDirService service = new ArchiveQuFromDirService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                function.onCallBack(JSON.toJSONString(data));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                JSONObject jsData = new JSONObject();
                jsData.put("code", errorCode);
                jsData.put("msg", errorMsg);
                function.onCallBack(JSON.toJSONString(jsData));
            }
        });
        service.postLogined("subjectId=" + subjectId + "&id=" + quId, false);
    }

    /**
     * 根据科目查询当前用户错题池中的错题数据
     *
     * @param subjectId
     */
    private void getPoolQuList(String subjectId, String next, CallBackFunction function) {
        final GetPoolQuListService service = new GetPoolQuListService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetPoolListRespose>() {
            @Override
            public void onGetData(GetPoolListRespose data) {
                function.onCallBack(JSON.toJSONString(data));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", errorCode);
                jsonObject.put("msg", errorMsg);
                function.onCallBack(jsonObject.toJSONString());
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("subjectId=" + subjectId);
        if (next != null) {
            stringBuilder.append("&next=" + next);
        }
        service.postLogined(stringBuilder.toString(), false);
    }

    /**
     * 查询指定dir目录中的错题数据
     */
    private void getDirQuList(String dirId, String next, String sort, String desc, CallBackFunction function) {
        final GetDirQuListService service = new GetDirQuListService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetPoolListRespose>() {
            @Override
            public void onGetData(GetPoolListRespose data) {
                function.onCallBack(JSON.toJSONString(data));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", errorCode);
                jsonObject.put("msg", errorMsg);
                function.onCallBack(jsonObject.toJSONString());
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dirId=" + dirId);
        if (!TextUtils.isEmpty(next)) {
            stringBuilder.append("&next=" + next);
        }
        if (!TextUtils.isEmpty(sort)) {
            stringBuilder.append("&sort=" + sort);
        }
        if (!TextUtils.isEmpty(desc)) {
            stringBuilder.append("&desc=" + desc);
        }
        service.postLogined(stringBuilder.toString(), false);
    }


    @Override
    public void onBackPressed() {
        webView.callHandler("onGoBack", "", data -> {

        });
    }
}
