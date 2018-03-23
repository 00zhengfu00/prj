package com.physicmaster.modules;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.physicmaster.common.Constant;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.utils.Utils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by huashigen on 2016/3/16.
 */
public class JavaScriptinterface {
    private Logger logger = AndroidLogger.getLogger(this.getClass().getSimpleName());
    private Context mContext;
    private IWXAPI api;
    private LocalBroadcastManager manager;

    public JavaScriptinterface(Context c) {
        mContext = c;
    }


    @JavascriptInterface
    public void doWeixinPay(String data) {
        PayReq req = new PayReq();
        JSONObject json = null;
        try {
            api = WXAPIFactory.createWXAPI(mContext, null);
            json = new JSONObject(data);
            req.appId = json.getString("appId");
            req.partnerId = json.getString("partnerId");
            req.prepayId = json.getString("prepayId");
            req.nonceStr = json.getString("nonceStr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
            req.sign = json.getString("paySign");
            req.extData = "app data";
            api.registerApp(Constant.PM_WEIXIN_APP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }


    /**
     * 打开微信客户端
     */
    @JavascriptInterface
    public void openWeixin() {
        if (Utils.checkApkExist(mContext, "com.tencent.mm")) {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "您还未安装微信，请先安装~", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取网页内容
     */
    @JavascriptInterface
    public void showSource(String html) {
        WebviewActivity.html = html;
    }

    @JavascriptInterface
    public void changeTitle(String html) {
        WebviewActivity.html = html;
    }
}
