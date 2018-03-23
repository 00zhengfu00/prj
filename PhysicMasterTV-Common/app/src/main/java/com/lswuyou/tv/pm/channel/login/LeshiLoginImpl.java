//package com.lswuyou.tv.pm.channel.login;
//
//import android.app.Activity;
//
//import com.letv.tvos.sdk.LetvSdk;
//import com.letv.tvos.sdk.account.LoginCallBackListener;
//
///**
// * Created by Administrator on 2016/11/9.
// */
//public class LeshiLoginImpl implements ChannelLoginInterface {
//    private Activity context;
//
//    public LeshiLoginImpl(Activity activity) {
//        context = activity;
//    }
//
//    private LoginCallBackListener loginCallBackListener;
//
//    public LoginCallBackListener getLoginCallBackListener() {
//        return loginCallBackListener;
//    }
//
//    public void setLoginCallBackListener(LoginCallBackListener loginCallBackListener) {
//        this.loginCallBackListener = loginCallBackListener;
//    }
//
//    @Override
//
//    public void login() {
//        LetvSdk.getLetvSDk().login(context, loginCallBackListener);
//    }
//}
