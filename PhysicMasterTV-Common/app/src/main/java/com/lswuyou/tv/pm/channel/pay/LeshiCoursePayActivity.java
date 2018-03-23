//package com.lswuyou.tv.pm.channel.pay;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.letv.tvos.paysdk.appmodule.pay.model.BaseParamsModel;
//import com.letv.tvos.paysdk.interfaces.OnLePayListener;
//import com.letv.tvos.sdk.LetvSdk;
//import com.lswuyou.tv.pm.R;
//import com.lswuyou.tv.pm.activity.BaseActivity;
//import com.lswuyou.tv.pm.view.TitleBarView;
//
//import java.util.Locale;
//
//public class LeshiCoursePayActivity extends BaseActivity {
//    private TitleBarView mTitleBarView;
//
//    @Override
//    protected void findViewById() {
//        mTitleBarView = (TitleBarView) findViewById(R.id.title);
//    }
//
//    @Override
//    protected void initView() {
//        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
//        mTitleBarView.setBtnLeft(0, R.string.leshiPay);
//        Bundle data = getIntent().getBundleExtra("data");
//        String productId = data.getString("productId");
//        String productName = data.getString("productName");
//        String productPrice = data.getString("productPrice");
//        String quanlity = data.getString("quanlity");
//        String customParams = data.getString("customParams");
//        int quanlityInt = 1;
//        try {
//            quanlityInt = Integer.parseInt(quanlity);
//        } catch (Exception e) {
//            quanlityInt = 1;
//        }
//        LetvSdk.getLetvSDk().payNoSku(this, onLePayListener, productId, productName,
//                productPrice, quanlityInt, customParams);
//
//    }
//
//    @Override
//    protected int getContentLayout() {
//        return R.layout.activity_leshi_pay;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        LetvSdk.getLetvSDk().onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        LetvSdk.getLetvSDk().quit();
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
//            Toast.makeText(LeshiCoursePayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//            Log.e("result", message);
//            finish();
//        }
//
//        @Override
//        public void onPayFailure(BaseParamsModel baseParamsModel, int errorCode) {
//            String msg = "支付失败：" + errorCode;
//            Toast.makeText(LeshiCoursePayActivity.this, msg, Toast.LENGTH_SHORT).show();
//            Log.e("result", msg);
//            finish();
//        }
//    };
//}
