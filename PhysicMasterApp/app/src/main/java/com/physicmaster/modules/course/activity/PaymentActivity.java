package com.physicmaster.modules.course.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseFragmentActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.course.activity.pay.PayConfig;
import com.physicmaster.modules.course.activity.pay.PayResult;
import com.physicmaster.modules.course.dialog.ParentsPayDialogFragment;
import com.physicmaster.modules.course.dialog.PayDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.course.CheckOrderResponse;
import com.physicmaster.net.response.course.CourseOrderResponse;
import com.physicmaster.net.response.course.CourseOrderVo;
import com.physicmaster.net.response.course.PrePayResponse;
import com.physicmaster.net.service.account.ParentsPayLogService;
import com.physicmaster.net.service.course.CheckOrderService;
import com.physicmaster.net.service.course.CourseOrderService;
import com.physicmaster.net.service.course.PrePayService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.TitleBuilder;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.physicmaster.modules.mine.activity.InvitationActivity.WEIXIN_SHARED_SUCC;

public class PaymentActivity extends BaseFragmentActivity {
    private Button btnPay;
    private String orderId;
    private PayDialogFragment fragment;
    private static final int SDK_PAY_FLAG = 1;
    private Timer mTimer;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver paySuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    PaymentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkOrder();
                        }
                    });
                }
            }, 0, 1000);
        }
    };
    private IWXAPI api;
    private ShareAction shareAction;
    private Map<String, String> payParams;
    private ImageView ivImage;

    @Override
    protected void findViewById() {
        btnPay = (Button) findViewById(R.id.btn_pay);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        String packageName = getPackageName();
        if (packageName.equals(Constant.PHYSICMASTER)) {
            ivImage.setImageResource(R.mipmap.wulidashi);
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            ivImage.setImageResource(R.mipmap.huaxuedashi);
        } else if (packageName.equals(Constant.MATHMASTER)) {
            ivImage.setImageResource(R.mipmap.shuxuedashi);
        }
        initTitle();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("确认订单");
    }

    @Override
    protected void initView() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(PaymentActivity.this, "confirm_order_pay_immediately");
                showPayMentDialog();
            }
        });
        int courseId = getIntent().getIntExtra("courseId", 0);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(paySuccessReceiver, new IntentFilter(Constant
                .WEIXIN_PAY_FINISHED));

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(weixinSharedReceiver, new IntentFilter
                (WEIXIN_SHARED_SUCC));

        getOrder(courseId);
    }

    private BroadcastReceiver weixinSharedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!TextUtils.isEmpty(orderId)) {
                sharedSuccess(orderId, 1);
            }
        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.activity_payment;
    }

    public void showPayMentDialog() {
        //        mPayDialog = new Dialog(this, R.style.my_dialog);
        //        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
        //                R.layout.dialog_payment, null);
        //        root.findViewById(R.id.tv_wenxin_daifu).setOnClickListener(btnlistener);
        //        root.findViewById(R.id.tv_zhifubao_pay).setOnClickListener(btnlistener);
        //        root.findViewById(R.id.tv_wenxin_pay).setOnClickListener(btnlistener);
        //        root.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
        //        mPayDialog.setContentView(root);
        //        Window dialogWindow = mPayDialog.getWindow();
        //        dialogWindow.setGravity(Gravity.BOTTOM);
        //        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        //        lp.x = 0; // 新位置X坐标
        //        lp.y = -20; // 新位置Y坐标
        //        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        //        //      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
        //        //      lp.alpha = 9f; // 透明度
        //        root.measure(0, 0);
        //        lp.height = root.getMeasuredHeight();
        //        lp.alpha = 9f; // 透明度
        //        dialogWindow.setAttributes(lp);
        //        mPayDialog.show();
        fragment = new PayDialogFragment();
        fragment.setOnClickListener(btnlistener);
        fragment.show(getSupportFragmentManager(), "pay");
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 支付宝支付
                case R.id.tv_zhifubao_pay:
                    MobclickAgent.onEvent(PaymentActivity.this, "confirm_order_zhifubao_pay");
                    fragment.dismiss();
                    startPay(orderId, PayConfig.ALIPAY);
                    break;
                //微信支付
                case R.id.tv_weixin_pay:
                    MobclickAgent.onEvent(PaymentActivity.this, "confirm_order_weixin_pay");
                    if (!Utils.checkApkExist(PaymentActivity.this, "com.tencent.mm")) {
                        UIUtils.showToast(PaymentActivity.this, "请先安装微信");
                        return;
                    }
                    fragment.dismiss();
                    startPay(orderId, PayConfig.WEIXIN_PAY);
                    break;
                //家长代付
                case R.id.tv_wenxin_daifu:
                    MobclickAgent.onEvent(PaymentActivity.this, "confirm_order_parents_pay");
                    startPay(orderId, PayConfig.SHARE_PAY);
                    fragment.dismiss();
                    break;
            }
        }
    };

    /**
     * 获取课程预下单
     */
    private void getOrder(int courseId) {
        final CourseOrderService service = new CourseOrderService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CourseOrderResponse>() {
            @Override
            public void onGetData(CourseOrderResponse data) {
                // UIUtils.showToast(PaymentActivity.this, "课程预下单成功！");
                refreshUI(data.data.appCourseOrderDetailVo);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(PaymentActivity.this, errorMsg);
            }
        });
        service.postLogined("courseId=" + courseId, false);
    }

    /**
     * 根据支付类型开始支付
     *
     * @param payType
     */
    private void startPay(final String orderId, int payType) {
        if (TextUtils.isEmpty(orderId)) {
            UIUtils.showToast(PaymentActivity.this, "无下单信息，不能支付！");
            return;
        }
        final PrePayService service = new PrePayService(this);
        service.setCallback(new IOpenApiDataServiceCallback<PrePayResponse>() {
            @Override
            public void onGetData(PrePayResponse data) {
                int type = data.data.courseOrderPayVo.payType;
                payParams = data.data.courseOrderPayVo.payCfg;
                if (type == PayConfig.WEIXIN_PAY) {
                    doWeixinPay(payParams);
                } else if (type == PayConfig.SHARE_PAY) {
                    if (Utils.checkApkExist(PaymentActivity.this, "com.tencent.mm")) {
                        sharedToPay(payParams, SHARE_MEDIA.WEIXIN);
                    } else if (Utils.checkApkExist(PaymentActivity.this, "com.tencent.mobileqq")) {
                        sharedToPay(payParams, SHARE_MEDIA.QQ);
                    } else {
                        if ((payParams != null) && (orderId != null)) {
                            String shareUrl = payParams.get("shareUrl");
                            String qrCodeUrl = payParams.get("qrCodeUrl");
                            if ((!TextUtils.isEmpty(shareUrl)) && (!TextUtils.isEmpty(qrCodeUrl))) {
                                ParentsPayDialogFragment fragment = new ParentsPayDialogFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("shareUrl", shareUrl);
                                bundle.putString("orderId", orderId);
                                bundle.putString("qrCodeUrl", qrCodeUrl);
                                fragment.setArguments(bundle);
                                fragment.show(getSupportFragmentManager(), "parentspay");
                            }
                        }
                    }
                } else if (type == PayConfig.ALIPAY) {
                    doAlipay(payParams);
                }

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(PaymentActivity.this, errorMsg);
            }
        });
        service.postLogined("orderId=" + orderId + "&payType=" + payType, false);
    }

    /**
     * 微信支付
     *
     * @param payParams
     */
    private void doWeixinPay(Map<String, String> payParams) {
        PayReq req = new PayReq();
        api = WXAPIFactory.createWXAPI(this, null);
        req.appId = payParams.get("appId");
        req.partnerId = payParams.get("partnerId");
        req.prepayId = payParams.get("prepayId");
        req.nonceStr = payParams.get("nonceStr");
        req.timeStamp = payParams.get("timeStamp");
        req.packageValue = payParams.get("packageStr");
        req.sign = payParams.get("paySign");
        req.extData = "app data";

        String packageName = getPackageName();
        if (packageName.equals(Constant.PHYSICMASTER)) {
            api.registerApp(Constant.PM_WEIXIN_APP_ID);
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            api.registerApp(Constant.CM_WEIXIN_APP_ID);
        } else if (packageName.equals(Constant.MATHMASTER)) {
            api.registerApp(Constant.MM_WEIXIN_APP_ID);
        }
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    /**
     * 支付宝支付
     *
     * @param payParams
     */
    private void doAlipay(Map<String, String> payParams) {
        final String orderInfo = payParams.get("orderStr");   // 订单信息
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PaymentActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信代付
     */
    private void sharedToPay(Map<String, String> payParams, SHARE_MEDIA media) {
        String shareImageUrl = payParams.get("shareImg");
        String shareTitle = payParams.get("shareTitle");
        String desc = payParams.get("shareDesc");
        String shareUrl = payParams.get("shareUrl");
        UMImage image = null;
        String packageName = getPackageName();
        if (!TextUtils.isEmpty(shareImageUrl)) {
            image = new UMImage(PaymentActivity.this, shareImageUrl);
        } else {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                image = new UMImage(PaymentActivity.this, R.mipmap.icon_physic);
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                image = new UMImage(PaymentActivity.this, R.mipmap.icon_chemistory);
            } else if (packageName.equals(Constant.MATHMASTER)) {
                image = new UMImage(PaymentActivity.this, R.mipmap.icon_math);
            }
        }
        if (TextUtils.isEmpty(desc)) {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                desc = "物理大师，科技、教育、艺术跨界融合，教师必备课堂教学大片。";
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                desc = "化学大师，科技、教育、艺术跨界融合，教师必备课堂教学大片。";
            } else if (packageName.equals(Constant.MATHMASTER)) {
                desc = "数学大师，科技、教育、艺术跨界融合，教师必备课堂教学大片。";
            }
        }
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);
        web.setDescription(desc);
        web.setThumb(image);
        shareAction = new ShareAction(PaymentActivity.this);
        shareAction.setPlatform(media).setCallback(new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);
                UIUtils.showToast(PaymentActivity.this, "分享成功啦");
                if (platform == SHARE_MEDIA.QQ) {
                    sharedSuccess(orderId, 0);
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                UIUtils.showToast(PaymentActivity.this, "分享失败啦");
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                UIUtils.showToast(PaymentActivity.this, "分享取消了");
            }
        }).withMedia(web).share();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                //支付成功,去服务端查询支付结果
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PaymentActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkOrder();
                            }
                        });
                    }
                }, 0, 1000);
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                UIUtils.showToast(PaymentActivity.this, "支付失败");
            }
            return true;
        }
    });

    /**
     * 刷新页面
     *
     * @param orderInfo
     */
    private void refreshUI(CourseOrderVo orderInfo) {
        if (orderInfo != null) {
            orderId = orderInfo.orderId;
            TextView tvPrice = (TextView) findViewById(R.id.tv_price);
            TextView tvPrice2 = (TextView) findViewById(R.id.tv_price2);
            TextView tvDiscountPrice = (TextView) findViewById(R.id.tv_discount_price);
            TextView tvPayPrice = (TextView) findViewById(R.id.tv_pay_price);
            TextView tvTitle = (TextView) findViewById(R.id.tv_title);
            TextView tvTitle2 = (TextView) findViewById(R.id.tv_title2);

            tvPrice.setText("￥" + orderInfo.totalFeeYuan);
            tvPrice2.setText("￥" + orderInfo.totalFeeYuan);
            tvDiscountPrice.setText("-￥" + orderInfo.discountFeeYuan);
            tvPayPrice.setText("￥" + orderInfo.paymentYuan);
            tvTitle.setText(orderInfo.courseTitle);
            tvTitle2.setText(orderInfo.courseTitle);

            Glide.with(this).load(orderInfo.coursePosterUrl).into(ivImage);
        }
    }

    /**
     * 查询订单支付状态
     */
    private void checkOrder() {
        final CheckOrderService service = new CheckOrderService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CheckOrderResponse>() {
            @Override
            public void onGetData(CheckOrderResponse data) {
                if (data.data.orderStatus == 1) {
                    mTimer.cancel();
                    PaymentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UIUtils.showToast(PaymentActivity.this, "支付成功~");
                            PaymentActivity.this.finish();
                        }
                    });
                    localBroadcastManager.sendBroadcast(new Intent(CourseDetailActivity
                            .PAY_SUCCSS));
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
            }
        });
        service.postLogined("orderId=" + orderId, false);
    }

    /**
     * 分享日志接口
     */
    private void sharedSuccess(String orderNum, int type) {
        final ParentsPayLogService service = new ParentsPayLogService(this);
        service.setCallback(new IOpenApiDataServiceCallback() {
            @Override
            public void onGetData(Object data) {
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
            }
        });
        service.postLogined("orderId=" + orderNum + "&shareType=" + type, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (paySuccessReceiver != null) {
            localBroadcastManager.unregisterReceiver(paySuccessReceiver);
            paySuccessReceiver = null;
        }

        if (weixinSharedReceiver != null) {
            localBroadcastManager.unregisterReceiver(weixinSharedReceiver);
            weixinSharedReceiver = null;
        }
        if (api != null) {
            api.unregisterApp();
            api = null;
        }
        if (shareAction != null) {
            shareAction.close();
        }
    }
}
