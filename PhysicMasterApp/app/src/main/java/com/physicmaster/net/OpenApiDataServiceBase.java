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
package com.physicmaster.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.physicmaster.R;
import com.physicmaster.base.AppConfigure;
import com.physicmaster.base.BaseActivityManager;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.GuideActivity;
import com.physicmaster.base.SystemParams;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.modules.videoplay.cache.service.DownloadService2;
import com.physicmaster.net.response.ClearTextMsg;
import com.physicmaster.net.response.Response;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.security.AESEncryption;
import com.physicmaster.net.security.Base64Encoder;
import com.physicmaster.net.security.RSAEncryption;
import com.physicmaster.net.security.RSAKeyPair;
import com.physicmaster.utils.MD5;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import static com.physicmaster.net.response.Response.CODE_TIME_ILLEGAL;


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
    private ProgressDialog dialog;
    public static boolean openTimeSync = false;

    public OpenApiDataServiceBase(Context pContext) {
        super(pContext);
        mContext = pContext;
    }

    /**
     * get 请求
     *
     * @param
     */

    public void get(Object params, boolean showDialog) {
        if (showDialog) {
            dialog = ProgressDialog.show(mContext, null, "加载中...", true, true, dialog -> {
                LOGGER.debug("service cancel");
                OpenApiDataServiceBase.this.cancel();
            });
            dialog.show();
            // LoadingDialog.showLoading(mContext, "加载中...", new
            // OnCancelListener() {
            // @Override
            // public void onCancel(DialogInterface dialog) {
            // LOGGER.debug("service cancel");
            // OpenApiDataServiceBase.this.cancel();
            // }
            // }, false);
        }
        try {
            StringEntity _Entity = new StringEntity(JSON.toJSONString(params), HTTP.UTF_8);
            httpClient.get(mContext, getRequestURL(), _Entity, "application/json", this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            HandleError(ErrorCode.ON_SUCCESS_ERROR, e.getMessage(), e);
        }
    }

    private void quitDownloading() {
        mContext.sendBroadcast(new Intent(DownloadService2.QUIT_DOWNLOADING));
        AsyncTask<String, Integer, Boolean> deleteDownloadingVideoTask = new
                AsyncTask<String, Integer, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        VideoManager videoManager = new VideoManager(mContext);
                        List<VideoInfo> videoInfoList = videoManager.getDownloadingVideos();
                        if (videoInfoList != null) {
                            for (VideoInfo videoInfo : videoInfoList) {
                                videoManager.deleteVideoByVideoId(videoInfo.getId());
                            }
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean bool) {
                        super.onPostExecute(bool);
                    }
                };
        deleteDownloadingVideoTask.execute("");
    }

    private long getSyncTime() {
        return SystemParams.getServerTime();
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
            // LoadingDialog.showLoading(mContext, "加载中...", new
            // OnCancelListener() {
            // @Override
            // public void onCancel(DialogInterface dialog) {
            // LOGGER.debug("service cancel");
            // OpenApiDataServiceBase.this.cancel();
            // }
            // }, false);
        }
        try {
//            httpClient.addHeader("channel", getChannel());
            userSecret = MD5.hexdigest(UUID.randomUUID().toString());
            long currentTime = System.currentTimeMillis();
            if (openTimeSync) {
                currentTime = getSyncTime();
            }
            StringBuilder builder = new StringBuilder();
            builder.append("t=" + currentTime);
            builder.append("&d=" + URLEncoder.encode(DEVICE_ID, Constant.CHARACTER_ENCODING));
            builder.append("&aesKey=" + URLEncoder.encode(userSecret, Constant.CHARACTER_ENCODING));
            if (!TextUtils.isEmpty(BaseApplication.getIMEI())) {
                builder.append("&e=" + BaseApplication.getIMEI());
            }
            if (!TextUtils.isEmpty(BaseApplication.getIMSI())) {
                builder.append("&s=" + BaseApplication.getIMSI());
            }
            String sysParamStr = builder.toString();
            LOGGER.debug("sysParamStr=" + sysParamStr);
            LOGGER.debug("busiParams=" + busiParams);
            try {
                RSAKeyPair rsakey = RSAEncryption.getRSAKeyPair();
                sysParamStr = Base64Encoder.encode(RSAEncryption.encryptByPublicKey(sysParamStr
                        .getBytes("UTF-8"), rsakey), false);
                String nonceStr = "_nonce=" + MD5.hexdigest(UUID.randomUUID().toString());
                if (TextUtils.isEmpty(busiParams)) {
                    busiParams = nonceStr;
                } else {
                    busiParams = nonceStr + "&" + busiParams;
                }
                busiParams = AESEncryption.encrypt(busiParams.getBytes("UTF-8"), userSecret);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String request = "s=" + URLEncoder.encode(sysParamStr, "UTF-8") + "&data=" +
                    URLEncoder.encode(busiParams, "UTF-8");
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
            dialog = ProgressDialog.show(mContext, null, "加载中...", true, true, new
                    OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            LOGGER.debug("service cancel");
                            OpenApiDataServiceBase.this.cancel();
                        }
                    });
            dialog.show();
            // LoadingDialog.showLoading(mContext, "加载中...", new
            // OnCancelListener() {
            // @Override
            // public void onCancel(DialogInterface dialog) {
            // LOGGER.debug("service cancel");
            // OpenApiDataServiceBase.this.cancel();
            // }
            // }, false);
        }
        //如果userKey为空则重新登录(这种极端情况会在应用崩溃后Application重建并且缓存的userKey为空时出现)
        if (BaseApplication.getUserKey() == null) {
            UserDataResponse.UserDataBean.KeyVoBean keyVoBean = (UserDataResponse.UserDataBean
                    .KeyVoBean) CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                    .USERINFO_USERKEY, UserDataResponse.UserDataBean.KeyVoBean.class);
            if (keyVoBean != null) {
                userSecret = keyVoBean.userSecret;
                userKey = keyVoBean.userKey;
            } else {
                exit();
                return;
            }
        } else {
            userSecret = BaseApplication.getUserKey().userSecret;
            userKey = BaseApplication.getUserKey().userKey;
        }
        httpClient.addHeader("uk", userKey);
        try {
            long currentTime = System.currentTimeMillis();
            if (openTimeSync) {
                currentTime = getSyncTime();
            }
            StringBuilder builder = new StringBuilder();
            builder.append("t=" + currentTime);
            builder.append("&d=" + URLEncoder.encode(DEVICE_ID, Constant.CHARACTER_ENCODING));
            if (!TextUtils.isEmpty(BaseApplication.getIMEI())) {
                builder.append("&e=" + BaseApplication.getIMEI());
            }
            if (!TextUtils.isEmpty(BaseApplication.getIMSI())) {
                builder.append("&s=" + BaseApplication.getIMSI());
            }
            String sysParamStr = builder.toString();
            LOGGER.debug("sysParamStr=" + sysParamStr);
            LOGGER.debug("busiParams=" + busiParams);
            try {
                sysParamStr = AESEncryption.encrypt(sysParamStr.getBytes("UTF-8"), userSecret);
                String nonceStr = "_nonce=" + MD5.hexdigest(UUID.randomUUID().toString());
                if (TextUtils.isEmpty(busiParams)) {
                    busiParams = nonceStr;
                } else {
                    busiParams = nonceStr + "&" + busiParams;
                }
                busiParams = AESEncryption.encrypt(busiParams.getBytes("UTF-8"), userSecret);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String request = "s=" + URLEncoder.encode(sysParamStr, "UTF-8") + "&data=" + URLEncoder.encode(busiParams, "UTF-8");
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
            String request = "&data=" + URLEncoder.encode(busiParams, "UTF-8");
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

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        if (null != dialog) {
            dialog.dismiss();
        }
        try {
            if (callback != null) {
                String responseStr;
                try {
                    responseStr = new String(responseBody, "utf-8");
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
                int number = headers.length;
                for (int i = 0; i < number; i++) {
                    logger.debug(headers[i].getName() + ":" + headers[i].getValue());
                    if (headers[i].getName().equalsIgnoreCase("data-type") && headers[i].getValue
                            ().equals("1")) {
                        isAESOpen = true;
                        break;
                    }
                }
                if (isAESOpen) {
                    String dataStr;
                    try {
                        dataStr = AESEncryption.decrypt(dataJSON.getString("data"), userSecret);
                        logger.debug("dataStr" + dataStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        HandleError(ErrorCode.ON_SUCCESS_ERROR, e.getMessage(), e);
                        return;
                    }

                    Object resultClass = getDataBeanTypeOrClass();
                    Response response = JSON.parseObject(dataStr, (Class<Response>) resultClass);
                    if (response.code == (Response.CODE_SUCCESS)) {
                        /** 当且仅当code=200时返回成功处理 */
                        callback.onGetData(response);
                    } else {
                        /** 返回失败处理 */
                        if (response.code == (Response.CODE_USER_LOGOUT)) {
                            exit();
                        } else {
                            ClearTextMsg msg = JSON.parseObject(dataStr, ClearTextMsg.class);
                            callback.onGetError(msg.code, msg.msg, null);
                        }
                    }
                }
                /** 数据为明文 */
                else {
                    String code = dataJSON.getString("code");
                    if (code.equals(Response.CODE_SUCCESS + "")) {
                        Object resultClass = getDataBeanTypeOrClass();
                        Response response = JSON.parseObject(responseStr, (Class<Response>)
                                resultClass);
                        /** 当且仅当code=200时返回成功处理 */
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
                        } else if ((CODE_TIME_ILLEGAL + "").equals(code)) {

                        } else {
                            callback.onGetError(Integer.parseInt(code), dataJSON.getString("msg"),
                                    null);
                        }
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
        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_USERKEY);
        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.OSS_SERVER_INFO);
        SpUtils.remove(mContext, CacheKeys.SUBJECT_STUDY_INFO);
        SpUtils.remove(mContext, CacheKeys.SUBJECT_ACTION);
        SpUtils.remove(mContext, CacheKeys.GRAGE_ACTION);
        SpUtils.remove(mContext, CacheKeys.SUBJECT_COURSE_INFO);
        SpUtils.remove(mContext, CacheKeys.SUBJECT_COURSE_INFO_STR);
        SpUtils.remove(mContext, CacheKeys.GRAGE_INFO);
        SpUtils.remove(mContext, "isSoundSwitch");
        clearWebViewCache();
        BaseApplication.setUserKey(null);
        BaseApplication.setUserData(null);
        BaseApplication.getStartupDataBean().ossConfig = null;
        BaseActivityManager.getInstance().popAllActivity();
        quitDownloading();
        Intent intent = new Intent(mContext, GuideActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private static final String TAG = "OpenApiDataServiceBase";

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // LoadingDialog.close();
        if (null != dialog) {
            dialog.dismiss();
        }
        int _ErrorCode = statusCode;
        String _ErrorMessage = "";
        try {
            if (statusCode == 404) {
                HandleError(_ErrorCode, error.getMessage(), error);
                _ErrorMessage = ErrorCode.SERVICE_UNAVAILABLE;
                return;
            } else if (statusCode == 402) {
//				BaseApplication.getServerTime();
                // Toast.makeText(BaseApplication.getAppContext(),
                // "Request onFailure statusCode:" + statusCode +
                // ".  Start refresh partakeTime!", Toast.LENGTH_SHORT)
                // .show();
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
        if (true == AppConfigure.officialVersion) {
            if (TextUtils.isEmpty(ServiceURL.DYNAMIC_URL)) {
                return ServiceURL.URL_OFFICIAL;
            } else {
                return ServiceURL.DYNAMIC_URL;
            }
        } else {
            return ServiceURL.URL;
        }
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