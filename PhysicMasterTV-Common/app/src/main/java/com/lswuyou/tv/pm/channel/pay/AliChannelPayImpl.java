//package com.lswuyou.tv.pm.channel.pay;
//
//import android.app.Activity;
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.de.aligame.api.AliTvSdk;
//import com.de.aligame.core.api.Listeners;
//import com.de.aligame.core.api.Listeners.IPayListener;
//import com.lswuyou.tv.pm.BaseApplication;
//import com.lswuyou.tv.pm.channel.login.AliPlayLoginManager;
//
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by Administrator on 2016/10/24.
// */
//public class AliChannelPayImpl implements ChannelPayInterface {
//    private final String TAG = "aliplay";
//    private Activity mContext;
//
//    public AliChannelPayImpl(Activity context) {
//        mContext = context;
//        //initTvSDK(context);
//    }
//
//    @Override
//    public void pay(Map<String, String> params) {
//        Set<String> keys = params.keySet();
//        Iterator<String> iterator = keys.iterator();
//        String title = "", amount = "", orderId = "", notifyUrl = "";
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            String value = params.get(key);
//            if (key.equals("title")) {
//                title = value;
//            } else if (key.equals("amount")) {
//                amount = value;
//            } else if (key.equals("notifyUrl")) {
//                notifyUrl = value;
//            } else if (key.equals("orderId")) {
//                orderId = value;
//            }
//
//        }
//        initTvSDK(mContext, title, amount, orderId, notifyUrl);
//    }
//
//    @Override
//    public void pay(String... param) {
//
//    }
//
//    public void initTvSDK(final Context context, final String title, final String amount, final
//    String orderId, final String notifyUrl) {
//        if (BaseApplication.aliplayInit) {
//            doPay(title, amount, orderId, notifyUrl);
//        } else {
//            // 打开log
//            AliTvSdk.logSwitch(true);
//            String appkey = PayConfig.ALIPLAY_APPKEY;
//            String appSecret = PayConfig.ALIPLAY_APP_SECRET;
//            AliTvSdk.init(context, appkey, appSecret, new Listeners.IInitListener() {
//                @Override
//                public void onInitFinish() {
//                    Log.d(TAG, "onInitFinish");
//                    BaseApplication.aliplayInit = true;
//                    doPay(title, amount, orderId, notifyUrl);
//                }
//
//                @Override
//                public void onInitError(String s) {
//                    Toast.makeText(context, "支付初始化失败！", Toast.LENGTH_SHORT).show();
//                }
//
//            }, new AliPlayLoginManager());
//        }
//    }
//
//    private void doPay(String title, String amount, String orderId, String notifyUrl) {
//        AliTvSdk.payFromServer(title, amount, orderId, notifyUrl, new IPayListener() {
//            @Override
//            public void onSuccess(String title, int amount) {
//                Toast.makeText(mContext, title + " 支付成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel(String title, int amount) {
//                Toast.makeText(mContext, title + " 支付取消", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String title, int amount, String errMsg) {
//                Toast.makeText(mContext, title + " 支付失败", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String errCode, String errMsg) {
//                Toast.makeText(mContext, " 支付失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
