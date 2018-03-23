//package com.lswuyou.tv.pm.channel.pay;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//
//import com.lswuyou.tv.pm.AppConfigure;
//
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by Administrator on 2016/10/11.
// */
//public class HuanwangChannelPayImpl implements ChannelPayInterface {
//    private final String TAG = "HuanwangPay";
//    private Activity mContext;
//
//    public HuanwangChannelPayImpl(Activity context) {
//        mContext = context;
//    }
//
//    @Override
//    public void pay(Map<String, String> params) {
//        Intent intent = new Intent(mContext, HuanwangPayActivity.class);
//        if (AppConfigure.huanwang == 2) {
//            intent.putExtra("huan", 1);
//        }
//        Set<String> keys = params.keySet();
//        Iterator<String> iterator = keys.iterator();
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            String value = params.get(key);
//            if (key.equals("appSerialNo")) {
//                intent.putExtra("appSerialNo", value);
//            } else if (key.equals("appPayKey")) {
//                intent.putExtra("appPayKey", value);
//            } else if (key.equals("productCount")) {
//                intent.putExtra("productCount", value);
//            } else if (key.equals("productName")) {
//                intent.putExtra("productName", value);
//            } else if (key.equals("productPrice")) {
//                intent.putExtra("productPrice", value);
//            } else if (key.equals("orderType")) {
//                intent.putExtra("orderType", value);
//            } else if (key.equals("noticeUrl")) {
//                intent.putExtra("noticeUrl", value);
//            } else if (key.equals("validateType")) {
//                intent.putExtra("validateType", value);
//            } else if (key.equals("accountID")) {
//                intent.putExtra("accountID", value);
//            } else if (key.equals("validateParam")) {
//                intent.putExtra("validateParam", value);
//            } else if (key.equals("paymentType")) {
//                intent.putExtra("paymentType", value);
//            } else if (key.equals("termUnitParam")) {
//                intent.putExtra("termUnitParam", value);
//            }
//        }
//        mContext.startActivity(intent);
//    }
//
//    @Override
//    public void pay(String... param) {
//
//    }
//}
