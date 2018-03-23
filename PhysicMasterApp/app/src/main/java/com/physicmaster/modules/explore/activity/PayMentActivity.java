package com.physicmaster.modules.explore.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseFragmentActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.course.activity.pay.PayConfig;
import com.physicmaster.modules.course.activity.pay.PayResult;
import com.physicmaster.modules.mine.activity.MymemberActivity;
import com.physicmaster.modules.mine.activity.location.CargoLocationActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.course.CheckOrderResponse;
import com.physicmaster.net.response.course.MemberDetailsResponse;
import com.physicmaster.net.response.course.MemberOrderResponse;
import com.physicmaster.net.response.course.MemberPayResponse;
import com.physicmaster.net.response.user.MemberListBean;
import com.physicmaster.net.service.account.ParentsPayLogService;
import com.physicmaster.net.service.course.CheckMemberOrderService;
import com.physicmaster.net.service.course.MemberPayService;
import com.physicmaster.net.service.course.MembersDetailsService;
import com.physicmaster.net.service.course.MembersOrderService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.MoreGridView;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PayMentActivity extends BaseFragmentActivity {

    private Button btnPay;
    private String orderId;
    private BuyMemberDialogFragment fragment;
    private static final int SDK_PAY_FLAG = 1;
    private Timer mTimer;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver paySuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTimer = new Timer();
            //这里不发布循环任务的原因是:Timer的在run方法里面执行取消必须是当前的任务池里面没有别的任务
            //如果时机巧合run方法执行到cancel的时候刚好又发布了一个任务，那么这个Timer永远都取消不掉
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    PayMentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkOrder();
                        }
                    });
                }
            }, 1000);
        }
    };
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission
            .ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static int LOCATION_REQUEST_CODE = 1;
    private IWXAPI api;
    private ShareAction shareAction;
    private Map<String, String> payParams;
    private TextView tvPrice;
    private TextView tvTime;
    private int isMy;
    private TextView tvLocation;
    private MemberListBean.ItemsBean item;
    private TextView tv1;
    private int topicId;
    private int mSelectedItem = 0;
    private String mName;
    private String phone;
    private String location;
    private String location2;
    private String areaId;
    private TextView tvSubject;
    private RelativeLayout relativeLayout;
    private RelativeLayout relativeLayout2;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvLocation2;
    private int memberItemId;
    private MoreGridView rvTopic2;
    private MembersTopicAdapter topicAdapter;
    private String latitude;
    private String longitude;
    private String description;

    @Override
    protected void findViewById() {
        btnPay = (Button) findViewById(R.id.btn_pay);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvSubject = (TextView) findViewById(R.id.tv_subject);
        tv1 = (TextView) findViewById(R.id.tv1);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvLocation2 = (TextView) findViewById(R.id.tv_location2);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        rvTopic2 = (MoreGridView) findViewById(R.id.rv_topic2);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.rl2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PayMentActivity.this, LinearLayoutManager.HORIZONTAL, false);
        //rvTopic.setLayoutManager(linearLayoutManager);
        initTitle();

        isMy = getIntent().getIntExtra("isMy", -1);
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout2.setVisibility(View.GONE);
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
        int memberItemId = getIntent().getIntExtra("memberItemId", 0);
        getDetails(memberItemId);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(paySuccessReceiver, new IntentFilter(Constant
                .WEIXIN_PAY_FINISHED));

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        rvTopic2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把点击的position传递到adapter里面去
                topicAdapter.changeState(position);
                mSelectedItem = position;

            }
        });
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_pay_ment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data != null) {
            mName = data.getStringExtra("name");
            phone = data.getStringExtra("phone");
            location = data.getStringExtra("location");
            location2 = data.getStringExtra("location2");
            areaId = data.getStringExtra("areaId");
            relativeLayout2.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            relativeLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(PayMentActivity.this, CargoLocationActivity.class), 0);
                }
            });
            final String name1 = "姓名：";
            String uname1 = mName;
            String str1 = name1 + uname1;
            final SpannableStringBuilder sp1 = new SpannableStringBuilder(str1);
            sp1.setSpan(new ForegroundColorSpan(0xFF666666), name1.length(), str1.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvName.setText(sp1);

            final String name2 = "电话：";
            String uname2 = phone;
            String str2 = name2 + uname2;
            final SpannableStringBuilder sp2 = new SpannableStringBuilder(str2);
            sp2.setSpan(new ForegroundColorSpan(0xFF666666), name2.length(), str2.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvPhone.setText(sp2);

            final String name3 = "地址：";
            String uname3 = location + location2;
            String str3 = name3 + uname3;
            final SpannableStringBuilder sp3 = new SpannableStringBuilder(str3);
            sp3.setSpan(new ForegroundColorSpan(0xff666666), name3.length(), str3.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvLocation2.setText(sp3);

        }
    }

    public void showPayMentDialog() {
        fragment = new BuyMemberDialogFragment();
        fragment.setOnClickListener(btnlistener);
        fragment.show(getSupportFragmentManager(), "pay");
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 支付宝支付
                case R.id.tv_zhifubao_pay:
                    MobclickAgent.onEvent(PayMentActivity.this, "member_open_confirm_zhifubao_pay");
                    fragment.dismiss();
                    startPay(orderId, PayConfig.ALIPAY);
                    break;
                //微信支付
                case R.id.tv_weixin_pay:
                    MobclickAgent.onEvent(PayMentActivity.this, "member_open_confirm_weixin_pay");
                    if (!Utils.checkApkExist(PayMentActivity.this, "com.tencent.mm")) {
                        UIUtils.showToast(PayMentActivity.this, "请先安装微信");
                        return;
                    }
                    fragment.dismiss();
                    startPay(orderId, PayConfig.WEIXIN_PAY);
                    break;
                case R.id.btn_cancel:
                    MobclickAgent.onEvent(PayMentActivity.this, "member_open_confirm_cancel");
                    fragment.dismiss();
                    break;
            }
        }
    };

    private void getDetails(int memberItemId) {
        final MembersDetailsService service = new MembersDetailsService(this);
        service.setCallback(new IOpenApiDataServiceCallback<MemberDetailsResponse>() {
            @Override
            public void onGetData(MemberDetailsResponse data) {
                // UIUtils.showToast(PaymentActivity.this, "课程预下单成功！");
                MemberDetailsResponse.DataBean.OrderDetailBean orderDetail = data.data.orderDetail;
                refreshUI(orderDetail);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(PayMentActivity.this, errorMsg);
            }
        });
        service.postLogined("memberItemId=" + memberItemId, false);
    }

    /**
     * 获取课程预下单
     */
    private void getOrder(int memberItemId, int id, String name, String phone, String loaction, String description, String longitude, String latitude, String areaId) {
        final MembersOrderService service = new MembersOrderService(this);
        service.setCallback(new IOpenApiDataServiceCallback<MemberOrderResponse>() {
            @Override
            public void onGetData(MemberOrderResponse data) {
                // UIUtils.showToast(PaymentActivity.this, "课程预下单成功！");
                orderId = data.data.orderId;
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(PayMentActivity.this, errorMsg);
            }
        });
        StringBuilder params = new StringBuilder();
        params.append("memberItemId=" + memberItemId);
        if (id != -1) {
            params.append("&giftItemId=" + id);
            params.append("&areaId=" + areaId);
        }
        if ((!TextUtils.isEmpty(name)) && (!TextUtils.isEmpty(phone)) && (!TextUtils.isEmpty(loaction))) {
            try {
                String userName = URLEncoder.encode(name, Constant.CHARACTER_ENCODING);
                params.append("&username=" + userName);
                String userPhone = URLEncoder.encode(phone, Constant.CHARACTER_ENCODING);
                params.append("&telNumber=" + userPhone);
                String userLocation = URLEncoder.encode(loaction, Constant.CHARACTER_ENCODING);
                params.append("&recipientAddress=" + userLocation);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if ((!TextUtils.isEmpty(description)) && (!TextUtils.isEmpty(longitude)) && (!TextUtils.isEmpty(latitude))) {
            try {
                String loaction3 = URLEncoder.encode(description, Constant.CHARACTER_ENCODING);
                params.append("&location=" + loaction3);
                String longitude2 = URLEncoder.encode(longitude, Constant.CHARACTER_ENCODING);
                params.append("&longitude=" + longitude2);
                String latitude2 = URLEncoder.encode(latitude, Constant.CHARACTER_ENCODING);
                params.append("&latitude=" + latitude2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        service.postLogined(params.toString(), false);
    }

    /**
     * 获取当前位置信息
     *
     * @return
     */
    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context
                .LOCATION_SERVICE);
        LocationProvider netProvider = locationManager.getProvider(LocationManager
                .NETWORK_PROVIDER);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                        .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission
                                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // 申请权限
                    ActivityCompat.requestPermissions(this, DANGEROUS_PERMISSION,
                            LOCATION_REQUEST_CODE);
                } else {
                    locationManager.requestLocationUpdates(netProvider.getName(), 10000L, 10F, new
                            LocationListener() {

                                @Override
                                public void onLocationChanged(Location location) {
                                    requestLocation(location);
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle
                                        extras) {

                                }

                                @Override
                                public void onProviderEnabled(String provider) {

                                }

                                @Override
                                public void onProviderDisabled(String provider) {

                                }
                            });
                }
            } else {
                locationManager.requestLocationUpdates(netProvider.getName(), 10000L, 10F, new
                        LocationListener() {

                            @Override
                            public void onLocationChanged(Location location) {
                                requestLocation(location);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle
                                    extras) {
                                System.out.println();
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                                System.out.println();
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                System.out.println();
                            }
                        });
            }
        } else {
            UIUtils.showToast(PayMentActivity.this, "为了更好地用户体验，请打开定位服务");
        }
    }

    // 获取当前详细地址
    private void requestLocation(Location location) {
        StringBuffer sb = new StringBuffer();
        Geocoder gc = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            sb.append(address.getAddressLine(1));
        }
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        description = sb.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 根据支付类型开始支付
     *
     * @param payType
     */
    private void startPay(final String orderId, int payType) {
        if (TextUtils.isEmpty(orderId)) {
            UIUtils.showToast(PayMentActivity.this, "无下单信息，不能支付！");
            return;
        }
        final MemberPayService service = new MemberPayService(this);
        service.setCallback(new IOpenApiDataServiceCallback<MemberPayResponse>() {
            @Override
            public void onGetData(MemberPayResponse data) {
                int type = data.data.orderPayVo.payType;
                payParams = data.data.orderPayVo.payCfg;
                if (type == PayConfig.WEIXIN_PAY) {
                    doWeixinPay(payParams);
                } else if (type == PayConfig.ALIPAY) {
                    doAlipay(payParams);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(PayMentActivity.this, errorMsg);
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
                PayTask alipay = new PayTask(PayMentActivity.this);
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
            image = new UMImage(PayMentActivity.this, shareImageUrl);
        } else {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                image = new UMImage(PayMentActivity.this, R.mipmap.icon_physic);
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                image = new UMImage(PayMentActivity.this, R.mipmap.icon_chemistory);
            } else if (packageName.equals(Constant.MATHMASTER)) {
                image = new UMImage(PayMentActivity.this, R.mipmap.icon_math);
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
        shareAction = new ShareAction(PayMentActivity.this);
        shareAction.setPlatform(media).setCallback(new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);
                UIUtils.showToast(PayMentActivity.this, "分享成功啦");
                if (platform == SHARE_MEDIA.QQ) {
                    sharedSuccess(orderId, 0);
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                UIUtils.showToast(PayMentActivity.this, "分享失败啦");
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                UIUtils.showToast(PayMentActivity.this, "分享取消了");
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
                        PayMentActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkOrder();
                            }
                        });
                    }
                }, 1000);
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                UIUtils.showToast(PayMentActivity.this, "支付失败");
            }
            return true;
        }
    });

    /**
     * 刷新页面
     */
    private void refreshUI(MemberDetailsResponse.DataBean.OrderDetailBean orderDetail) {
        if (orderDetail != null) {
            if (orderDetail.giftItemList != null && orderDetail.giftItemList.size() != 0) {
                topicId = orderDetail.giftItemList.get(0).giftItemId;
            }
            memberItemId = orderDetail.memberItemId;
            tvPrice.setText(orderDetail.priceDesc + "");
            tvSubject.setText(orderDetail.title + "");
            final String name = "会员到期时间: ";
            String uname = orderDetail.expiryDate;
            String str = name + uname;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFFFCBE01), name.length(), str.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvTime.setText(sp);

            if (orderDetail != null) {
                if (orderDetail.giftItemList != null && orderDetail.giftItemList.size() != 0) {
                    //rvTopic.setVisibility(View.VISIBLE);
                    rvTopic2.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.VISIBLE);
                    //                    TopicAdapter topicAdapter = new TopicAdapter(PayMentActivity.this, orderDetail.giftItemList);
                    //                    topicAdapter.setSelectTopicListen(new TopicAdapter.SelectTopic() {
                    //                        @Override
                    //                        public void select(int id) {
                    //                            topicId = id;
                    //                        }
                    //                    });
                    //rvTopic.setAdapter(topicAdapter);
                    topicAdapter = new MembersTopicAdapter(orderDetail.giftItemList);
                    rvTopic2.setAdapter(topicAdapter);
                    tvLocation.setText("请输入收货地址");
                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getLocation();
                            startActivityForResult(new Intent(PayMentActivity.this, CargoLocationActivity.class), 0);
                        }
                    });
                    btnPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MobclickAgent.onEvent(PayMentActivity.this, "member_open_step4_pay");
                            if (topicId == -1) {
                                UIUtils.showToast(PayMentActivity.this, "请选择赠品！");
                                return;
                            }
                            if (TextUtils.isEmpty(mName)) {
                                UIUtils.showToast(PayMentActivity.this, "请完善收货信息");
                                return;
                            }
                            if (TextUtils.isEmpty(phone)) {
                                UIUtils.showToast(PayMentActivity.this, "请完善收货信息");
                                return;
                            }
                            if (TextUtils.isEmpty(location)) {
                                UIUtils.showToast(PayMentActivity.this, "请完善收货信息");
                                return;
                            }
                            if (TextUtils.isEmpty(location2)) {
                                UIUtils.showToast(PayMentActivity.this, "请完善收货信息");
                                return;
                            }

                            getOrder(memberItemId, topicId, mName, phone, location + location2, description, longitude, latitude, areaId);
                            MobclickAgent.onEvent(PayMentActivity.this, "confirm_order_pay_immediately");
                            showPayMentDialog();
                        }
                    });

                } else {
                    tvLocation.setText("不需要收货地址");
                    tv1.setVisibility(View.GONE);
                    //rvTopic.setVisibility(View.GONE);
                    rvTopic2.setVisibility(View.GONE);
                    btnPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getOrder(memberItemId, -1, null, null, null, null, null, null, null);
                            MobclickAgent.onEvent(PayMentActivity.this, "confirm_order_pay_immediately");
                            showPayMentDialog();
                        }
                    });
                }

            } else {
                UIUtils.showToast(PayMentActivity.this, "网络出问题了，请稍后再试！");
            }
        }
    }

    class MembersTopicAdapter extends BaseAdapter {

        private List<MemberDetailsResponse.DataBean.OrderDetailBean.GiftItemListBean> memberList;

        public MembersTopicAdapter(List<MemberDetailsResponse.DataBean.OrderDetailBean.GiftItemListBean> list) {
            this.memberList = list;
        }

        @Override
        public int getCount() {
            return memberList == null ? 0 : memberList.size();
        }

        @Override
        public MemberDetailsResponse.DataBean.OrderDetailBean.GiftItemListBean getItem(int position) {
            return memberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(PayMentActivity.this,
                        R.layout.layout_topic_item, null);
                holder = new ViewHolder();
                holder.ivContent = (ImageView) convertView.findViewById(R.id.iv_content);
                holder.tvGrade = (TextView) convertView.findViewById(R.id.tv_grade);
                holder.rbTopic = (RadioButton) convertView.findViewById(R.id.rb_topic);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MemberDetailsResponse.DataBean.OrderDetailBean.GiftItemListBean item = getItem(position);

            holder.rbTopic.setChecked(position == mSelectedItem);
            holder.tvGrade.setText(item.title);
            Glide.with(PayMentActivity.this).load(item.imgUrl).into(holder.ivContent);
            if (mSelectedItem == position) {
                holder.rbTopic.setChecked(true);
                topicId = item.giftItemId;
            } else {
                holder.rbTopic.setChecked(false);

            }
            //            holder.rbTopic.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    mSelectedItem = position;
            //                    holder.rbTopic.setChecked(position == mSelectedItem);
            //                    notifyDataSetChanged();
            //                }
            //            });
            //            holder.ivContent.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    mSelectedItem = position;
            //                    holder.rbTopic.setChecked(position == mSelectedItem);
            //                    notifyDataSetChanged();
            //                }
            //            });

            return convertView;
        }

        public void changeState(int pos) {
            mSelectedItem = pos;
            notifyDataSetChanged();

        }
    }

    static class ViewHolder {
        ImageView ivContent;
        RadioButton rbTopic;
        TextView tvGrade;

    }

    /**
     * 查询订单支付状态
     */
    private void checkOrder() {
        final CheckMemberOrderService service = new CheckMemberOrderService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CheckOrderResponse>() {
            @Override
            public void onGetData(CheckOrderResponse data) {
                if (data.data.orderStatus == 1) {
                    //如果获取支付结果为成功支付，那么取消定时器
                    mTimer.cancel();
                    PayMentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UIUtils.showToast(PayMentActivity.this, "支付成功~");
                            PayMentActivity.this.finish();
                            Intent intent = null;
                            if (isMy == 0) {
                                intent = new Intent(PayMentActivity.this, MembersActivity.class);
                            } else if (isMy == 1) {
                                intent = new Intent(PayMentActivity.this, MymemberActivity.class);
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                } else {
                    //如果获取支付结果为未支付，那么继续发布查询任务
                    logger.debug("支付未完成");
                    if (mTimer != null) {
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                PayMentActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        checkOrder();
                                    }
                                });
                            }
                        }, 1000);
                    }
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
        service.postLogined("orderNum=" + orderNum + "&shareType=" + type, false);
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
        if (api != null) {
            api.unregisterApp();
            api = null;
        }
        if (shareAction != null) {
            shareAction.close();
        }
        if (mHandler != null) {
            mHandler = null;
        }
    }

}
