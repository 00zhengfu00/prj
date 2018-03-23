//package com.lswuyou.tv.pm.channel.login;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.de.aligame.api.AliTvSdk;
//import com.de.aligame.core.api.AliBaseError;
//import com.de.aligame.core.api.Listeners;
//import com.lswuyou.tv.pm.BaseApplication;
//import com.lswuyou.tv.pm.channel.pay.PayConfig;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Created by Administrator on 2016/11/9.
// */
//public class AliPlayLoginImpl implements ChannelLoginInterface {
//    public String TAG = "AliPlayLoginImpl";
//    private Context context;
//    private ChannelLoginStateListener channelLoginStateListener;
//    private Timer aliplayLoginTimer;
//
//    public AliPlayLoginImpl(Context context) {
//        this.context = context;
//    }
//
//    public ChannelLoginStateListener getChannelLoginStateListener() {
//        return channelLoginStateListener;
//    }
//
//    public void setChannelLoginStateListener(ChannelLoginStateListener channelLoginStateListener) {
//        this.channelLoginStateListener = channelLoginStateListener;
//    }
//
//    @Override
//    public void login() {
//        initTvSDK(context);
//    }
//
//    public void initTvSDK(final Context context) {
//        if (BaseApplication.aliplayInit) {
//            doLoginAuth();
//        } else {
//            // 打开log
//            AliTvSdk.logSwitch(true);
//
//            String appkey = PayConfig.ALIPLAY_APPKEY;
//            String appSecret = PayConfig.ALIPLAY_APP_SECRET;
//            AliTvSdk.init(context, appkey, appSecret, new Listeners.IInitListener() {
//                @Override
//                public void onInitFinish() {
//                    Log.d(TAG, "onInitFinish");
//                    BaseApplication.aliplayInit = true;
//                    //如果用户已登录，则获取用户信息
//                    doLoginAuth();
//                }
//
//                @Override
//                public void onInitError(String s) {
//                }
//
//            }, new AliPlayLoginManager());
//        }
//    }
//
//    /**
//     * 获取用户信息
//     */
//    private void getUserInfo() {
//        AliPlayLoginManager.getUserInfo(new Listeners.IGetUserinfoListener() {
//            @Override
//            public void onSuccess(Listeners.UserInfo userInfo) {
//                Log.d(TAG, "userinfo nick:" + userInfo.getUserNick() + ", id:" + userInfo
//                        .getUserId()
//                        + " headUrl:" + userInfo.getAvatarUrl());
//                channelLoginStateListener.onSuccess(userInfo);
//                Toast.makeText(context, "获取用户信息成功！", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(int errCode) {
//                Log.d(TAG, "get userinfo error:" + errCode + " " + AliBaseError.getErrMsg(errCode));
//                channelLoginStateListener.onFail();
//                Toast.makeText(context, "获取用户信息失败！", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    /**
//     * 登录授权
//     */
//    private void doLoginAuth() {
//        if (AliTvSdk.Account.isAuth()) {
//            getUserInfo();
//        } else {
//            AliPlayLoginManager.checkAuthAndLogin();
//            aliplayLoginTimer = new Timer();
//            aliplayLoginTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    Log.d("AliPlayLoginImpl:", "checkAuthAndLogin");
//                    if (AliTvSdk.Account.isAuth()) {
//                        aliplayLoginTimer.cancel();
//                        getUserInfo();
//                    }
//                }
//            }, 5000, 2000);
//        }
//    }
//
//    /**
//     * 回收资源
//     */
//    public void recycle() {
//        if (aliplayLoginTimer != null) {
//            aliplayLoginTimer.cancel();
//        }
//    }
//}
