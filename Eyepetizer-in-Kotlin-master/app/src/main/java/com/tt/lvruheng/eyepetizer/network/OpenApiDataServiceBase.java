/*
 * 系统名称：lswuyou
 * 类  名  称：OpenApiDataServiceBase.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-7 下午4:44:43
 * 功能说明： 将post、get请求对象化
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.tt.lvruheng.eyepetizer.network;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tt.lvruheng.eyepetizer.R;
import com.tt.lvruheng.eyepetizer.base.BaseApplication;
import com.tt.lvruheng.eyepetizer.common.cache.CacheKeys;
import com.tt.lvruheng.eyepetizer.common.cache.CacheManager;
import com.tt.lvruheng.eyepetizer.log.AndroidLogger;
import com.tt.lvruheng.eyepetizer.log.Logger;
import com.tt.lvruheng.eyepetizer.ui.InputPhoneActivity;
import com.tt.lvruheng.eyepetizer.widget.loading.custom.CustomDialogFactory;
import com.tt.lvruheng.eyepetizer.widget.loading.dialog.LoadingDialog;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;

import cn.cloudwalk.libproject.util.UIUtils;


public abstract class OpenApiDataServiceBase extends DataServiceBase {
    private IOpenApiDataServiceCallback callback;
    private Logger logger = AndroidLogger.getLogger();
    /**
     * 是否开启了AES加密
     */
    private boolean isAESOpen;

    /**
     * AES加密解密密钥
     */
    private String userSecret;
    private String userKey;

    private Context mContext;
    /**
     * 加载对话框
     */
    private Dialog dialog;
    public static boolean openTimeSync = false;

    public OpenApiDataServiceBase(Context pContext) {
        super(pContext);
        mContext = pContext;
    }

    /**
     * postUnLogin(RSA and AES) 请求
     *
     * @param busiParams
     */

    public void postUnLogin(String busiParams, boolean showDialog) {
        if (mContext == null) {
            return;
        }
        if (!isNetworkOk()) {
            if (mContext == null) {
                return;
            }
            UIUtils.showToast(mContext, R.string.network_error);
            return;
        }
        if (showDialog) {
            dialog = ProgressDialog.show(mContext, null, "加载中...", true, true, new
                    OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            LOGGER.debug("service cancel");
                            OpenApiDataServiceBase.this.cancel();
                        }
                    });
            dialog.show();
        }
        try {
            String request = busiParams;
            httpPost(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            HandleError(ErrorCode.ON_SUCCESS_ERROR, e.getMessage(), e);
        }
    }

    private String getChannel() {
        String channel = "Android";
        ApplicationInfo info = null;
        try {
            info = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (info.metaData == null) {
                return channel;
            }
            channel = info.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }

    /**
     * postUnLogin(AES) 请求
     *
     * @param busiParams
     */

    public void postLogined(String busiParams, boolean showDialog) {
        if (mContext == null) {
            return;
        }
        if (!isNetworkOk()) {
            if (mContext == null) {
                return;
            }
            UIUtils.showToast(mContext, R.string.network_error);
            return;
        }
        if (showDialog) {
//            dialog = ProgressDialog.show(mContext, null, "加载中...", true, true, new
//                    OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                            LOGGER.debug("service cancel");
//                            OpenApiDataServiceBase.this.cancel();
//                        }
//                    });
//            dialog.show();
            dialog = LoadingDialog.make(mContext, new CustomDialogFactory())
                    .setMessage("正在加载")//提示消息
                    .setCancelable(true)
                    .create();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    LOGGER.debug("service cancel");
                    OpenApiDataServiceBase.this.cancel();
                }
            });
            dialog.show();
        }
        try {
            String request = busiParams;
            httpPost(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            HandleError(ErrorCode.ON_SUCCESS_ERROR, e.getMessage(), e);
        }
    }

    public void postNoEncode(String busiParams) {
        if (mContext == null) {
            return;
        }
        if (!isNetworkOk()) {
            if (mContext == null) {
                return;
            }
            UIUtils.showToast(mContext, R.string.network_error);
            return;
        }
        try {
            String request = "&data=" + URLEncoder.encode(busiParams, HTTP.UTF_8);
            httpPost(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            HandleError(ErrorCode.ON_SUCCESS_ERROR, e.getMessage(), e);
        }
    }

    private void httpPost(String request) throws UnsupportedEncodingException {
        StringEntity _Entity = new StringEntity(request, HTTP.UTF_8);
        httpClient.post(mContext, getRequestURL(), _Entity, "application/json", this);
        LOGGER.debug("请求URL:" + getRequestURL());
    }

    // /**
    // * postUnLogin 请求
    // *
    // * @param pObject
    // */
    // public void postShowLoading(Object params) {
    // try {
    // LOGGER.debug(JSON.toJSONString(params));
    // LoadingDialog.showLoading(mContext, "加载中...",
    // new OnCancelListener() {
    // @Override
    // public void onCancel(DialogInterface dialog) {
    // LOGGER.debug("service cancel");
    // OpenApiDataServiceBase.this.cancel();
    // }
    // });
    // Request request = new Request();
    // request.data = params;
    // httpPost(request);
    //
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // HandleError(ErrorCode.ON_SUCCESS_ERROR, e.getMessage(), e);
    // }
    //
    // }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        if (null != dialog) {
            dialog.dismiss();
        }
        try {
            if (callback != null) {
                String responseStr;
                try {
                    responseStr = new String(responseBody, HTTP.UTF_8);
                    LOGGER.debug("onSuccess:" + responseStr);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    HandleError(ErrorCode.ON_SUCCESS_ERROR, e.getMessage(), e);
                    return;
                }
                /**
                 * 对结果进行json 处理
                 */
                JSONObject dataJSON = JSON.parseObject(responseStr);
                /** data-type=1时,数据采用AES加密 */
                String code = dataJSON.getString("retCode");
                if (code.equals(Response.CODE_SUCCESS)) {
                    Object resultClass = getDataBeanTypeOrClass();
                    Response response = JSON.parseObject(responseStr, (Class<Response>)
                            resultClass);
                    /** 当且仅当code=0000时返回成功处理 */
                    callback.onGetData(response);
                } else {
                    /** 返回失败处理 */
                    if (code.equals(Response.CODE_SERVER_ECEPTION + "")) {
                        callback.onGetError(Integer.parseInt(code), mContext.getResources()
                                .getString(R.string.server_error), null);
                    } else if (code.equals(Response.CODE_USER_LOGOUT + "")) {
                        exit();
                        callback.onGetError(Integer.parseInt(code), "登录状态异常，请重新登录",
                                null);
                    } else {
                        callback.onGetError(Integer.parseInt(code), dataJSON.getString("retMsg"),
                                null);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.debug("数据处理异常", e);
            HandleError(ErrorCode.ON_SUCCESS_ERROR, e.getMessage(), e);
        }

    }

    /**
     * 退出登录
     */
    private void exit() {
        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO);
        BaseApplication.Companion.setLoginInfo(null);
        Intent intent = new Intent(mContext, InputPhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private static final String TAG = "OpenApiDataServiceBase";

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if (null != dialog) {
            dialog.dismiss();
        }
        int _ErrorCode = statusCode;
        String _ErrorMessage = "";
        try {
            if (statusCode == 404) {
                HandleError(_ErrorCode, error.getMessage(), error);
                return;
            } else if (statusCode == 402) {
                return;
            }

            if (error instanceof HttpResponseException) {
                _ErrorCode = ((HttpResponseException) error).getStatusCode();
                _ErrorMessage = ErrorCode.RETURN_ERROR;
            }

            if (error instanceof SocketTimeoutException) {
                _ErrorCode = ErrorCode.SOCKET_TIMEOUT;
                _ErrorMessage = ErrorCode.TIMEOUT_ERROR;
            }

            if (error instanceof ConnectTimeoutException) {
                _ErrorCode = ErrorCode.CONNECT_TIMEOUT;
                _ErrorMessage = ErrorCode.TIMEOUT_ERROR;
            }
            HandleError(_ErrorCode, _ErrorMessage, error);

        } catch (Exception e) {
            HandleError(_ErrorCode, e.getMessage(), error);
        }
    }

    /**
     * 请求的地址
     */
    protected abstract String getResouceURI();

    /**
     * bean class 用于json 转换
     */
    protected abstract Object getDataBeanTypeOrClass();

    public IOpenApiDataServiceCallback getCallback() {
        return callback;
    }

    public void setCallback(IOpenApiDataServiceCallback callback) {
        this.callback = callback;
    }

    public void HandleError(int errorCode, String errorMsg, Throwable errorThrowable) {
        try {
            LOGGER.debug("HandleError:" + errorMsg);
            if (callback != null) {
                // _ErrorCode, "网络连接连接错误", error
                callback.onGetError(errorCode, errorMsg, errorThrowable);
            }
        } catch (Exception e) {
            LOGGER.debug("HandleError", e);
        }
    }

    public void cancel() {
        if (httpClient != null) {
            httpClient.cancelRequests(mContext, true);
        }
    }

    /**
     * 网路请求地址
     *
     * @return
     */
    private String getRequestURL() {
        String _ApiUrl = getApiURL();
        String _ResoureUrl = getResouceURI();
        return _ApiUrl + _ResoureUrl;
    }

    /**
     * 总的请求地址
     *
     * @return
     */
    private String getApiURL() {
        return ServiceURL.URL_API;
    }

    /**
     * 判断网络是否正常
     */
    private boolean isNetworkOk() {
        if (mContext == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            return false;
        } else {
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if (null != infos && infos.length > 0) {
                for (NetworkInfo info : infos) {
                    if (NetworkInfo.State.CONNECTED == info.getState()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void clearWebViewCache() {
        CookieSyncManager.createInstance(mContext);
        CookieManager.getInstance().removeAllCookie();
    }
}