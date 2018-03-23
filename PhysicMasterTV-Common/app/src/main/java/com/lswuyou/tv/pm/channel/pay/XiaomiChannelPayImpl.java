//package com.lswuyou.tv.pm.channel.pay;
//
//import android.app.Activity;
//import android.util.Log;
//
//import com.xiaomi.mitv.osspay.sdk.data.PayOrder;
//import com.xiaomi.mitv.osspay.sdk.proxy.PayCallback;
//import com.xiaomi.mitv.osspay.sdk.proxy.ThirdPayProxy;
//
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by Administrator on 2016/10/11.
// */
//public class XiaomiChannelPayImpl implements ChannelPayInterface {
//    private final String TAG = "XiaomiPay";
//    private Activity mContext;
//    private ThirdPayProxy thirdPayProxy;
//
//    public XiaomiChannelPayImpl(Activity context) {
//        mContext = context;
//        thirdPayProxy = ThirdPayProxy.instance(mContext);
//        thirdPayProxy.setUsePreview(true);
//    }
//
//    @Override
//    public void pay(Map<String, String> params) {
//        Set<String> keys = params.keySet();
//        Iterator<String> iterator = keys.iterator();
//        String appId = "";
//        String orderId = "";
//        String price = "";
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            String value = params.get(key);
//            if (key.equals("appId")) {
//                appId = value;
//            } else if (key.equals("cpOrderId")) {
//                orderId = value;
//            } else if (key.equals("amount")) {
//                price = value;
//            }
//        }
//        Float priceF = Float.parseFloat(price);
//        Long priceL = priceF.longValue();
//        thirdPayProxy.createOrderAndPay(Long.parseLong(appId), orderId, "productName", priceL,
//                "orderDesc", "extra_test_data", new PayCallback() {
//
//                    @Override
//                    public void onSuccess(PayOrder payOrder) {
//                        Log.i(TAG, "payorder:" + payOrder.getMiOrderId());
//                    }
//
//                    @Override
//                    public void onError(int code, String message) {
//                        Log.i(TAG, "code:" + code + ",message:" + message);
//                    }
//                });
//    }
//
//    @Override
//    public void pay(String... param) {
//
//    }
//}
