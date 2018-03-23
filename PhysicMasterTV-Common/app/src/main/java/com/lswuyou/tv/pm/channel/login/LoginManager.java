package com.lswuyou.tv.pm.channel.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.lswuyou.tv.pm.channel.pay.TvChannelType;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.CommonResponse;
import com.lswuyou.tv.pm.net.response.account.LoginByChannelResponse;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.account.RegisterByQdResponse;
import com.lswuyou.tv.pm.net.service.BindQdService;
import com.lswuyou.tv.pm.net.service.LoginByChannelService;
import com.lswuyou.tv.pm.net.service.RegisterByQdService;
import com.lswuyou.tv.pm.view.BindWeixinDialog;

/**
 * Created by Administrator on 2016/10/14.
 */
public class LoginManager {
    private static Activity mContext;
    private static BindWeixinDialog bindWeixinDialog;

    //渠道开始登录
    public static void login(final Activity context, String channel) {
        mContext = context;
        if(channel.equals("aliplay")) {
            Intent intent = new Intent(mContext, ChannelLoginActivity.class);
            intent.putExtra("channel", channel);
            mContext.startActivity(intent);
        }else {
            Intent intent = new Intent(mContext, ChannelLoginActivity.class);
            intent.putExtra("channel", channel);
            mContext.startActivity(intent);
        }
    }

    //渠道登录
    private static void loginByChannel(final String nickName, final String portrait, final String
            openId) {
        LoginByChannelService service = new LoginByChannelService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<LoginByChannelResponse>() {
            @Override
            public void onGetData(LoginByChannelResponse data) {
                try {
                    //登录过
                    if (data.data.isLoginByQdSuccess == 1) {
                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                                .USERINFO_LOGINVO, data.data.loginInfo);
                        Toast.makeText(mContext, "登录成功！", Toast.LENGTH_SHORT).show();
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent
                                (UserFragment.USERINFO_UPDATE));
                    }
                    //第一次登录
                    else {
                        bindWeixinDialog = new BindWeixinDialog(mContext, new
                                BindWeixinDialog.OnLoginSuccessListener() {
                                    @Override
                                    public void onLoginSuccess() {
                                        bindQd(nickName, portrait, openId);
                                    }
                                }, data.data.cfg);
                        bindWeixinDialog.setOnCancelListener(new DialogInterface.OnCancelListener
                                () {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                registerByQd(nickName, portrait, openId);
                            }
                        });
                        bindWeixinDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        StringBuilder builder = new StringBuilder();
        builder.append("openId=" + openId);
        builder.append("&nickname=" + nickName);
        builder.append("&portrait=" + portrait);
        service.post(builder.toString(), false);
    }

    /**
     * 用户第一次渠道登录取消了微信绑定，那就只能用渠道账号去注册
     */
    private static void registerByQd(String nickName, String portrait, String openId) {
        RegisterByQdService service = new RegisterByQdService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<RegisterByQdResponse>() {
            @Override
            public void onGetData(RegisterByQdResponse data) {
                try {
                    loginSuccessAction(data.data.loginInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        StringBuilder builder = new StringBuilder();
        builder.append("openId=" + openId);
        builder.append("&nickname=" + nickName);
        builder.append("&portrait=" + portrait);
        service.post(builder.toString(), false);
    }

    /**
     * 登录成功操作
     */
    private static void loginSuccessAction(LoginUserInfo userInfo) {
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, userInfo);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent
                (UserFragment.USERINFO_UPDATE));
    }

    /**
     * 用户扫描了二维码，绑定微信
     *
     * @param nickName
     * @param portrait
     * @param openId
     */
    private static void bindQd(String nickName, String portrait, String openId) {
        BindQdService service = new BindQdService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        StringBuilder builder = new StringBuilder();
        builder.append("openId=" + openId);
        builder.append("&nickname=" + nickName);
        builder.append("&portrait=" + portrait);
        service.postAES(builder.toString(), false);
    }
}

