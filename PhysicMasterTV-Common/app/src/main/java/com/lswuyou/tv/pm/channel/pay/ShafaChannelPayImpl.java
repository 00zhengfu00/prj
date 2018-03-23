//package com.lswuyou.tv.pm.channel.pay;
//
//import android.app.Activity;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.xmxgame.pay.PayInfo;
//import com.xmxgame.pay.TVPayment;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by Administrator on 2016/10/11.
// */
//public class ShafaChannelPayImpl implements ChannelPayInterface {
//    private final String TAG = "ShafaPay";
//    private Activity mContext;
//
//    public ShafaChannelPayImpl(Activity context) {
//        mContext = context;
//    }
//
//    @Override
//    public void pay(Map<String, String> params) {
//        PayInfo payInfo = new PayInfo();
//        JSONObject object = new JSONObject();
//        try {
//            object.put("name", "me");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Set<String> keys = params.keySet();
//        Iterator<String> iterator = keys.iterator();
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            String value = params.get(key);
//            if (key.equals("customData")) {
//                try {
//                    JSONObject customData = new JSONObject(value);
//                    payInfo.setCustomData(customData);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else if (key.equals("name")) {
//                payInfo.setName(value);
//            } else if (key.equals("price")) {
//                payInfo.setPrice(Double.parseDouble(value));
//            } else if (key.equals("quantity")) {
//                payInfo.setQuantity(Integer.parseInt(value));
//            }
//        }
//        TVPayment.create(payInfo, new TVPayment.Callback() {
//            @Override
//            public void onStatusChanged(int status, PayInfo info) {
//                switch (status) {
//                    // 创建订单的回调
//                    case STATUS_CREATE_ORDER_SUCCESS:
//                        Log.d(TAG, "订单创建成功 " + info.getCallbackOrderID());
//                        break;
//                    case STATUS_CREATE_ORDER_FAILED:
//                        Log.d(TAG, "订单创建失败" );
//                        Toast.makeText(mContext, "订单信息请求出错", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    // 订单支付结果回调
//                    case STATUS_PAYMENT_SUCCESS:
//                        Log.d(TAG, "支付成功 订单号" + info.getCallbackOrderID());
//                        break;
//                    case STATUS_PAYMENT_FAILED:
//                        Log.d(TAG, "支付失败 订单号 " + info.getCallbackOrderID());
//                        break;
//
//                    case STATUS_CANCELED:
//                        Log.d(TAG, "订单取消: 订单号 " + info.getCallbackOrderID());
//                        break;
//                }
//            }
//        });
//    }
//
//    @Override
//    public void pay(String... param) {
//
//    }
//
//    public static  void main(String []args)
//    {
//        System.out.println(System.currentTimeMillis());
//    }
//}
