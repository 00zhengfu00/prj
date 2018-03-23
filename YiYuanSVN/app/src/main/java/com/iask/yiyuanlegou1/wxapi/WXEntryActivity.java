package com.iask.yiyuanlegou1.wxapi;
/*
 * 系统名称：lswuyou
 * 类  名  称：WXEntryActivity.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-7-28 下午2:06:37
 * 功能说明： 导航页面Activity
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.account.UserInfo;
import com.iask.yiyuanlegou1.network.respose.account.WeixinLoginResponse;
import com.iask.yiyuanlegou1.network.service.account.WeixinLoginService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private Logger logger = AndroidLogger.getLogger(this.getClass().getSimpleName());
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    public final static String ACTION_WEIXIN_AUTHENCATION_SUCC = "ACTION_WEIXIN_AUTHENCATION_SUCC";
    public final static String EXTRA_TYPE_USERCLICK = "EXTRA_TYPE_USERCLICK";
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_info);
        Intent intent = getIntent();
        boolean bUserClick = intent.getBooleanExtra(EXTRA_TYPE_USERCLICK, false);
        init();
        if (true == bUserClick) {
            startAuthencation();
        }
    }

    private void startAuthencation() {
        api.registerApp(Constant.WEIXIN_APP_ID);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
    }

    private void init() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_ID, true);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq arg0) {

    }

    /**
     * 微信访问回调方法
     */
    @Override
    public void onResp(BaseResp resp) {

        int result = 0;
        // Bundle bundle = new Bundle();
        // resp.toBundle(bundle);
        // Resp sp = new Resp(bundle);
        String code = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                code = ((SendAuth.Resp) resp).code;
                // 获取访问Token
                getToken(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                WXEntryActivity.this.finish();
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                finish();
                break;
            default:
                result = R.string.errcode_unknown;
                finish();
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取微信用户信息
     */
    private void getUserinfo(final String openid, String token) {
        AsyncHttpClient client = new AsyncHttpClient();
        String userinfo_url = Constant.WEIXIN_USERINFO_URL + "?access_token=" + token +
                "&openid=" + openid;
        client.get(userinfo_url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                logger.debug(response.toString());
                try {
                    String nick = response.getString("nickname");
                    String gender = response.getString("sex");
                    String portrait = response.getString("headimgurl");
                    String location = response.getString("province") + "," + response.getString
                            ("city");
                    doWeixinLogin(openid, portrait, nick);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                logger.error("fail:code=" + statusCode);
                WXEntryActivity.this.finish();
            }
        });
    }

    /**
     * 获取微信访问Token
     */
    private void getToken(String code) {
        AsyncHttpClient client = new AsyncHttpClient();
        String access_url = Constant.TOKEN_URL + "?appid=" + Constant.WEIXIN_APP_ID + "&secret="
                + Constant.WEIXIN_APP_Secret + "&code=" + code
                + "&grant_type=authorization_code";
        client.get(access_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                logger.debug("success:code=" + statusCode);
                try {
                    logger.debug(response.toString());
                    String accessToken = response.getString("access_token");
                    String expires = String.valueOf(response.getInt("expires_in"));
                    String openid = response.getString("openid");

                    // 获取用户信息
                    getUserinfo(openid, accessToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                logger.debug("fail:code=" + statusCode);
            }

        });
    }

    private void doWeixinLogin(String openId, String photo, String nickName) {
        WeixinLoginService service = new WeixinLoginService(this);
        service.setCallback(new IOpenApiDataServiceCallback<WeixinLoginResponse>() {
            @Override
            public void onGetData(WeixinLoginResponse data) {
                try {
                    UserInfo info = data.data.user;
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .USERINFO_LOGINVO, info);
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .OSS_SERVER_INFO, data.data.aliyun);
                    CacheManager.saveString(CacheManager.TYPE_PUBLIC, CacheKeys
                            .APPDATA_CURR_REGIONURL, data.data.aliyun.getRegionUrl());
                    BaseApplication.getInstance().setUserId(info.getUserId() + "");
                    Intent intent = new Intent(ACTION_WEIXIN_AUTHENCATION_SUCC);
                    sendBroadcast(intent);
                    WXEntryActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(WXEntryActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                WXEntryActivity.this.finish();
            }
        });
        JSONObject json;
        try {
            json = new JSONObject();
            json.put("openId", openId);
            json.put("photo", photo);
            nickName = URLEncoder.encode(nickName, Constant.CHARACTER_ENCODING);
            json.put("nickname", nickName);
            service.post("wxlogin=" + json.toString(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
