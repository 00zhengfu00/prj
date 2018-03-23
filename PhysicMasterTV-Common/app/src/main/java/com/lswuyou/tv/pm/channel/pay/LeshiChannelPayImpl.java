//package com.lswuyou.tv.pm.channel.pay;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.letv.tvos.paysdk.appmodule.pay.model.BaseParamsModel;
//import com.letv.tvos.paysdk.interfaces.OnLePayListener;
//import com.letv.tvos.sdk.LetvSdk;
//
//import java.util.Iterator;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by Administrator on 2016/10/11.
// */
//public class LeshiChannelPayImpl implements ChannelPayInterface {
//    private final String TAG = "LeshiPay";
//    private Activity mContext;
//
//    public LeshiChannelPayImpl(Activity context) {
//        mContext = context;
//    }
//
//    @Override
//    public void pay(Map<String, String> params) {
//        Intent intent = new Intent(mContext,LeshiCoursePayActivity.class);
//        Bundle bundle = new Bundle();
//        Set<String> keys = params.keySet();
//        Iterator<String> iterator = keys.iterator();
//        String productId = "", productName = "", productPrice = "", quanlity = "", customParams =
//                "";
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            String value = params.get(key);
//            if (key.equals("productId")) {
//                productId = value;
//                bundle.putString("productId",productId);
//            } else if (key.equals("productName")) {
//                productName = value;
//                bundle.putString("productName",productName);
//            } else if (key.equals("productPrice")) {
//                productPrice = value;
//                bundle.putString("productPrice",productPrice);
//            } else if (key.equals("quanlity")) {
//                quanlity = value;
//                bundle.putString("quanlity",quanlity);
//            } else if (key.equals("customParams")) {
//                customParams = value;
//                bundle.putString("customParams",customParams);
//            }
//        }
//        intent.putExtra("data",bundle);
//        mContext.startActivity(intent);
//    }
//
//    @Override
//    public void pay(String... param) {
//
//    }
//
//    private OnLePayListener onLePayListener = new OnLePayListener() {
//        @Override
//        public void onPaySuccess(String orderNumber, BaseParamsModel baseParamsModel) {
//            // 乐视平台订单号: orderNumber;
//            // 用户唯一标识，ssoUid
//            String userId = baseParamsModel.getSSOUidOrMac();
//            // 应用AppId
//            String appId = baseParamsModel.getMaster();
//            // 订单购买的商品数量
//            int quantity = baseParamsModel.getQuantity();
//            // 商品id
//            String productId = baseParamsModel.getExternalProductId();
//            // 商品名
//            String productName = baseParamsModel.getMarketName();
//            // 商品单价/元
//            String price = baseParamsModel.getPrice();
//
//            String message = String.format(Locale.CANADA,
//                    "订单信息>>>订单号：%s,userId:%s,appId:%s,商品数量:%s,商品id:%s,商品名:%s,商品单价/元:%s",
//                    orderNumber, userId, appId,
//                    quantity, productId, productName, price);
//            Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
//            Log.e("result", message);
//        }
//
//        @Override
//        public void onPayFailure(BaseParamsModel baseParamsModel, int errorCode) {
//            String msg = "支付失败：" + errorCode;
//            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
//            Log.e("result", msg);
//        }
//    };
//}
