//package com.lswuyou.tv.pm.channel.pay;
//
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.Log;
//
//import com.lswuyou.tv.pm.utils.Utils;
//
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by Administrator on 2016/10/10.
// */
//public class HaixinChannelPayImpl implements ChannelPayInterface {
//    private Activity mContext;
//
//    public HaixinChannelPayImpl(Activity context) {
//        mContext = context;
//    }
//
//    @Override
//    public void pay(Map<String, String> params) {
//        if (Utils.checkApkExist(mContext, "com.hisense.hitv.payment")) {
//            Intent intent = new Intent();
//            //intent.setAction("com.hisense.hitv.payment.MAIN");//应用启动的action
//            intent.setAction("com.hisense.hitv.payment.QC");//应用启动的action
//            Set<String> keys = params.keySet();
//            Iterator<String> iterator = keys.iterator();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                String value = params.get(key);
//                intent.putExtra(key, value);
//            }
////                intent.putExtra("platformId", "");//支付平台1-支付宝2-微信（可空）
////                intent.putExtra("appName", "支付测试"); //应用名称
////                intent.putExtra("packageName", packageName);//包名
////                intent.putExtra("paymentMD5Key", paymentMD5);//包名md5签名
////                intent.putExtra("tradeNum", "adhjaddk12313141563464629");//商品流水号，第三方商品唯一编号
////                intent.putExtra("goodsPrice", "0.01");//商品价格单位元，注意请转化成字符串
////                intent.putExtra("goodsName", "支付测试");//商品名称
////                intent.putExtra("alipayUserAmount", "hsyzf@hisense.com");//收款账户
////                intent.putExtra("notifyUrl", "http://10.0.64.107:9080/notify");//第三方后台回调地址
//            try {
//                mContext.startActivityForResult(intent, PayConfig.HAIXIN_REQ_CODE);
//            } catch (ActivityNotFoundException e) {
//                Log.d("TAG", "出现异常版本过低，进入市场升级");
//                e.printStackTrace();
//                Intent it = new Intent(Intent.ACTION_VIEW);
//                it.setData(Uri.parse("himarket://details?id=" + "com.hisense.hitv.payment" +
//                        "&isAutoDownload=" + 1));
//                mContext.startActivity(it);
//            }
//        } else {//如果没有预装海信支付跳转至海信聚好用下载
//            Log.d("TAG", "未找到支付程序");
//            Intent it = new Intent(Intent.ACTION_VIEW);
//            it.setData(Uri.parse("himarket://details?id=" + "com.hisense.hitv.payment"));
//            mContext.startActivity(it);
//        }
//    }
//
//    @Override
//    public void pay(String... param) {
//
//    }
//}
