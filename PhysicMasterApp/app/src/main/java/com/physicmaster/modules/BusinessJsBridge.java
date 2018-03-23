package com.physicmaster.modules;

import android.content.Context;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.AuthCodeResponse;
import com.physicmaster.net.service.account.GetAuthCodeService;
import com.physicmaster.utils.UIUtils;

import cn.pedant.SafeWebViewBridge.JsCallback;

/**
 * Created by huashigen on 2017/2/22.
 */

public class BusinessJsBridge {
    private static Context context;

    public BusinessJsBridge(Context context) {
        BusinessJsBridge.context = context;
    }

    public static void callHandler(WebView view, String method, org.json.JSONObject param, final
    JsCallback jsCallback) {
        System.out.println();
        if (method.equals("getLoginAuthCode")) {
            getAuthCode(view.getContext(), jsCallback);
        }
    }

    public static void registerHandler(WebView view, String method, org.json.JSONObject handler) {
        System.out.println();
    }

    /**
     * 获取授权码
     *
     * @param jsCallback
     */
    private static void getAuthCode(final Context context, final JsCallback jsCallback) {
        GetAuthCodeService service = new GetAuthCodeService(context);
        service.setCallback(new IOpenApiDataServiceCallback<AuthCodeResponse>() {
            @Override
            public void onGetData(AuthCodeResponse data) {
                String dataS = JSON.toJSONString(data);
                try {
                    jsCallback.apply(dataS);
                } catch (JsCallback.JsCallbackException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(context, "获取授权码失败：" + errorMsg);
            }
        });
        service.postLogined("", false);
    }
}
