//package com.lswuyou.tv.pm.channel.pay;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//
//import com.lswuyou.tv.pm.R;
//import com.lswuyou.tv.pm.common.Constant;
//
//import tv.huan.huanpay4.HuanPayView;
//import tv.huan.huanpay4.been.PayInfo;
//import tv.huan.huanpay4.util.BigpadUtil;
//
//
///**
// * Created with IntelliJ IDEA.
// * User: warriorr
// * Mail: warriorr@163.com
// * Date: 2016/6/22
// * Time: 11:01
// * To change this template use File | Settings | File Templates
// */
//public class HuanwangPayActivity extends Activity {
//    HuanPayView webView;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_huanwang_pay);
//        webView = (HuanPayView) findViewById(R.id.webview);
//        PayInfo payInfo = new PayInfo();
//        payInfo.appSerialNo = getIntent().getStringExtra("appSerialNo");
//        payInfo.appPayKey = getIntent().getStringExtra("appPayKey");
//        payInfo.productName = getIntent().getStringExtra("productName");
//        payInfo.productCount = getIntent().getStringExtra("productCount");
//        payInfo.productPrice = getIntent().getStringExtra("productPrice");
//        payInfo.noticeUrl = getIntent().getStringExtra("noticeUrl");
//        payInfo.signType ="md5";
//        //如果设备无欢网用户系统，还需根据文档输入下列参数
//        int huan = getIntent().getIntExtra("huan", 0);
//        if (huan == 1) {
//            //电视终端
//            payInfo.validateType = getIntent().getStringExtra("validateType");
//            payInfo.accountID = getIntent().getStringExtra("accountID");
//            SharedPreferences preferences = getSharedPreferences(Constant.ACCOUNT_PF,
//                    Context.MODE_PRIVATE);
//            payInfo.termUnitParam = preferences.getString("deviceID", "unknown");
//            payInfo.validateParam = getIntent().getStringExtra("accountID");
////            payInfo.validateType = "HUANTEST";
//////            payInfo.termUnitNo = "";
////            payInfo.termUnitParam = "Hisense+Vision-TV";
////            payInfo.accountID = "HUANTEST000000";
////            payInfo.validateParam = "HUANTEST000000";
//        } else if (huan == 2) {
//            //            bigpad
//            BigpadUtil bigpadUtil = new BigpadUtil();
//            payInfo.validateType = "BIGPAD";
////            payInfo.termUnitNo = "";
//            payInfo.termUnitParam = bigpadUtil.getBigpadInfo(this, bigpadUtil.URI_DM);
//            payInfo.accountID = bigpadUtil.getBigpadInfo(this, bigpadUtil.URI_UNAME);
//            payInfo.validateParam = bigpadUtil.getBigpadInfo(this, bigpadUtil.URI_UNAME) + "|" + bigpadUtil.getBigpadInfo(this, bigpadUtil.URI_UTK)
//                    + "|" + bigpadUtil.getBigpadInfo(this, bigpadUtil.URI_APPID) + "|" + bigpadUtil.getBigpadInfo(this, bigpadUtil.URI_APPKEY);
//
//        }
//        //-----------------------------------------------------------------------------------------------
//        webView.load(this, payInfo);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            try {
//                if (webView.getVisibility() == View.VISIBLE) {
//                    webView.loadUrl("javascript:onKeyBack()");
//                    return true;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//}
