//package com.lswuyou.tv.pm.channel.pay;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.util.Log;
//
//import com.lswuyou.tv.pm.common.MD5;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by Administrator on 2016/10/11.
// */
//public class DamaiChannelPayImpl implements ChannelPayInterface {
//    private final String TAG = "DamaiPay";
//    private Activity mContext;
//
//    public DamaiChannelPayImpl(Activity context) {
//        mContext = context;
//    }
//
//    @Override
//    public void pay(Map<String, String> params) {
//        Intent intent = new Intent();
//
//        intent.putExtra("cashAmt", "0.01");// 支付金额（单位元，注意是String类型）
//        intent.putExtra("productName", "大吉找不同");// 产品名称
//        intent.putExtra("chargingName", "魔力操场");// 计费名称
//        intent.putExtra("chargingDuration", -1);// 计费时长（单位秒，注意是int型）：-1表示永久有效，比如有效期是1天则传86400
//        intent.putExtra("appendAttr", "{\"callback\":\"testNotifyUrl\"," +
//                "\"out_trade_no\":\"test_trade_no\"}");
//        /*callback和out_trade_no为必填， testNotifyUrl 为回调地址，test_trade_no为贵方自己的订单号，这里如此填写时是为了兼容老版本支付
//		 * 如果想传递自己的参数可如此填写 intent.putExtra("appendAttr", "{\"callback\":\"testNotifyUrl\",
//		 * \"out_trade_no\":\"test_trade_no\",\"test\":\"abctest\"}");
//		 * 当然下方生成key的时候appendAttr记得要一致
//		 */
//        intent.putExtra("partnerId", "testPartnerId");// 此处请填写贵方商户id
//        intent.putExtra("packageName", "packageName");// 此处请填写贵方apk的包名
//
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("cashAmt", "0.01");
//        map.put("chargingDuration", "-1");
//        map.put("partnerId", "testPartnerId");// 此处请填写贵方商户id
//        map.put("appendAttr", "{\"callback\":\"testNotifyUrl\"," +
//                "\"out_trade_no\":\"test_trade_no\"}");//与上面intent里面的appendAttr要一样
//        String token = genToken(map, "testKey");// 此处请填写贵方商户key
//        intent.putExtra("token", token);// 秘钥
//        intent.setAction("com.hiveview.activity_huanwang_pay.cashpay");// 设置action
//        intent.addCategory("android.intent.category.DEFAULT");
//
//        mContext.startActivityForResult(intent, 6);
//    }
//
//    @Override
//    public void pay(String... param) {
//
//    }
//
//    private String genToken(Map<String, String> packMap, String paternerKey) {
//        try {
//            /*** 生成pack签名时，参数不判空，且使用md5加密算法 ***/
//            Set<String> keySet = packMap.keySet();
//            List<String> list = new ArrayList<String>(keySet);
//            Collections.sort(list);
//            StringBuffer sb = new StringBuffer();
//            for (String key : list) {
//                String val = packMap.get(key);
//                sb.append("&" + key + "=" + val);
//            }
//            sb.append(paternerKey);
//            String parm = sb.substring(1, sb.length());
//            Log.i("test", "parm=" + parm);
//            String sign = MD5.hexdigest(parm);
//            return sign;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//}
