package com.lswuyou.tv.pm.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.adapter.TvChapterListAdapter;
import com.lswuyou.tv.pm.channel.login.LoginManager;
import com.lswuyou.tv.pm.channel.pay.PayManager;
import com.lswuyou.tv.pm.channel.pay.TvChannelType;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.fragment.BaseIndexFragment;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.response.account.GetLoginCfgResponse;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.course.GetPackCourseResponse;
import com.lswuyou.tv.pm.net.response.course.PackCourseInfo;
import com.lswuyou.tv.pm.net.response.order.CourseOrder;
import com.lswuyou.tv.pm.net.response.order.GetOrderResponse;
import com.lswuyou.tv.pm.net.response.order.GetOrderStatusResponse;
import com.lswuyou.tv.pm.net.service.GetLoginCfgService;
import com.lswuyou.tv.pm.net.service.GetOrderService;
import com.lswuyou.tv.pm.net.service.GetOrderStatusService;
import com.lswuyou.tv.pm.net.service.GetPackCourseLoginedService;
import com.lswuyou.tv.pm.net.service.GetPackCourseUnLoginService;
import com.lswuyou.tv.pm.utils.Utils;
import com.lswuyou.tv.pm.view.PayDialog;
import com.lswuyou.tv.pm.view.TitleBarView;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import reco.frame.tv.view.TvButton;
import reco.frame.tv.view.TvListView;

/**
 * Created by Administrator on 2016/8/8.
 * 课程包详情
 */
public class CoursePackDetailActivity extends BaseActivity {
    private String TAG = "CoursePackDetailActivity";
    private TitleBarView mTitleBarView;
    private TvButton btnWeixinPay, btnZhifbPay;
    private ImageView ivQrCode;
    private TextView tvPrice, tvPrice2, tvOriPrice, title2;
    private TvListView tlv_list;
    private TvChapterListAdapter adapter;
    private Timer timer;
    private int courseId;
    private String orderId;
    private LocalBroadcastManager localBroadcastManager;
    public static final String ON_LOGIN_FAIL = "on_login_fail";

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
        btnWeixinPay = (TvButton) findViewById(R.id.btn_weixin_pay);
        btnZhifbPay = (TvButton) findViewById(R.id.btn_zhifubao_pay);
        ivQrCode = (ImageView) findViewById(R.id.iv_qrcode);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvPrice2 = (TextView) findViewById(R.id.tv_price2);
        tvOriPrice = (TextView) findViewById(R.id.tv_ori_price);
        title2 = (TextView) findViewById(R.id.title2);

        TextView tvPayType = (TextView) findViewById(R.id.tv_pay_type);
        String channel = BaseApplication.getChannel();
        if (channel.equals(TvChannelType.none.name())) {
            tvPayType.setText(R.string.none);
        } else if (channel.equals(TvChannelType.aliplay.name())) {
            tvPayType.setText(R.string.aliplay);
        } else if (channel.equals(TvChannelType.damai.name())) {
            tvPayType.setText(R.string.damai);
        } else if (channel.equals(TvChannelType.dangbei.name())) {
            tvPayType.setText(R.string.dangbei);
        } else if (channel.equals(TvChannelType.haixin.name())) {
            tvPayType.setText(R.string.haixin);
        } else if (channel.equals(TvChannelType.huanwang.name())) {
            tvPayType.setText(R.string.huanwang);
        } else if (channel.equals(TvChannelType.kukai.name())) {
            tvPayType.setText(R.string.kukai);
        } else if (channel.equals(TvChannelType.leshi.name())) {
            tvPayType.setText(R.string.leshi);
        } else if (channel.equals(TvChannelType.xiaomi.name())) {
            tvPayType.setText(R.string.xiaomi);
        } else if (channel.equals(TvChannelType.yishiteng.name())) {
            tvPayType.setText(R.string.yishiteng);
        } else if (channel.equals(TvChannelType.shafa.name())) {
            tvPayType.setText(R.string.shafa);
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(onLoginSuccReceiver, new IntentFilter(UserFragment
                .USERINFO_UPDATE));
        localBroadcastManager.registerReceiver(onLoginFailReceiver, new IntentFilter
                (ON_LOGIN_FAIL));
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        btnWeixinPay.requestFocus();
        btnWeixinPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrder();
            }
        });
        tlv_list = (TvListView) findViewById(R.id.lv_course_chapter);
        courseId = getIntent().getIntExtra("courseId", 0);
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            mTitleBarView.setBtnLeft(0, R.string.courseDetail);
        } else {
            mTitleBarView.setBtnLeftStr(title);
        }
        if (Utils.isUserLogined()) {
            getData();
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        GetLoginCfgService service = new GetLoginCfgService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetLoginCfgResponse>() {
            @Override
            public void onGetData(GetLoginCfgResponse data) {
                try {
                    String loginType = data.data.loginCfgVo.loginType;
                    if (loginType.equals(TvChannelType.none.name())) {
                        Intent intent = new Intent(CoursePackDetailActivity.this, UnLoginActivity.class);
                        startActivity(intent);
                    } else {
                        LoginManager.login(CoursePackDetailActivity.this, loginType);
                    }
                } catch (Exception e) {
                    Toast.makeText(CoursePackDetailActivity.this, "获取登录配置异常！", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(CoursePackDetailActivity.this, "获取登录配置信息失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        service.post("", true);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_course_pack_detail;
    }

    //获取课程包详情
    private void getData() {
        OpenApiDataServiceBase service = null;
        Object obj = CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, LoginUserInfo.class);
        if (obj != null) {
            service = new GetPackCourseLoginedService(this);
        } else {
            service = new GetPackCourseUnLoginService(this);
        }
        service.setCallback(new IOpenApiDataServiceCallback<GetPackCourseResponse>() {
            @Override
            public void onGetData(final GetPackCourseResponse data) {
                try {
                    CoursePackDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI(data.data);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(CoursePackDetailActivity.this, "获取课程包详情失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        if (obj != null) {
            service.postAES("courseId=" + courseId, false);
        } else {
            service.post("courseId=" + courseId, false);
        }
    }

    //刷新页面微信购买二维码及课程列表数据
    private void refreshUI(final PackCourseInfo info) {
        if (info.packCourseVo.alreadyBuy == 1) {
            Toast.makeText(CoursePackDetailActivity.this, "您已购买此课程包，请到年级课程页面查看~", Toast
                    .LENGTH_SHORT).show();
            CoursePackDetailActivity.this.finish();
        }
        /**if (info.courseOrderVo != null) {
         new Thread(new Runnable() {
        @Override public void run() {
        byte[] qrCodeByte = Base64Decoder.decodeToBytes(info.courseOrderVo
        .wxPayQrCodeImgByteWithBase64);
        final Bitmap bitmap = BitmapFactory.decodeByteArray(qrCodeByte, 0, qrCodeByte
        .length);
        CoursePackDetailActivity.this.runOnUiThread(new Runnable() {
        @Override public void run() {
        ivQrCode.setImageBitmap(bitmap);
        }
        });
        }
        }).start();
         orderId = info.courseOrderVo.orderId;
         }**/
        tvPrice.setText("￥" + info.packCourseVo.priceYuan);
        tvPrice2.setText("￥" + info.packCourseVo.priceYuan);
        tvOriPrice.setText("￥" + info.packCourseVo.originalPriceYuan);
        title2.setText(info.packCourseVo.title);
        adapter = new TvChapterListAdapter(this, info.packCourseVo.subCourseList);
        tlv_list.setAdapter(adapter);

        tlv_list.setOnItemSelectListener(new TvListView.OnItemSelectListener() {

            @Override
            public void onItemSelect(View item, int position) {
            }

            @Override
            public void onItemDisSelect(View item, int position) {

            }
        });

        tlv_list.setOnItemClickListener(new TvListView.OnItemClickListener() {

            @Override
            public void onItemClick(View item, int position) {
            }
        });
    }

    //获取订单状态
    private void getOrderStatus() {
        if (TextUtils.isEmpty(orderId)) {
            return;
        }
        GetOrderStatusService service = new GetOrderStatusService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetOrderStatusResponse>() {
            @Override
            public void onGetData(GetOrderStatusResponse data) {
                try {
                    //如果订单状态为已支付，关闭timer，刷新页面
                    if (data.data.orderStatus == 1) {
                        if (timer != null) {
                            timer.cancel();
                        }
                        CoursePackDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshUI();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(CoursePackDetailActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.postAES("orderId=" + orderId, false);
    }

    private void refreshUI() {
        Intent intent = new Intent(BaseIndexFragment.COURSE_LIST_UPDATE);
        intent.putExtra("courseId", courseId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (onLoginSuccReceiver != null) {
            localBroadcastManager.unregisterReceiver(onLoginSuccReceiver);
        }
        if (onLoginFailReceiver != null) {
            localBroadcastManager.unregisterReceiver(onLoginFailReceiver);
        }
    }

    //获取订单信息
    private void getOrder() {
        GetOrderService service = new GetOrderService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetOrderResponse>() {
            @Override
            public void onGetData(GetOrderResponse data) {
                try {
                    CourseOrder courseOrder = data.data.courseOrderVo;
                    orderId = courseOrder.orderId;
                    if (courseOrder.payType.equals(TvChannelType.none.name())) {
                        String qrCode = null;
                        Set<String> keys = courseOrder.payCfg.keySet();
                        Iterator<String> iterator = keys.iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            String value = courseOrder.payCfg.get(key);
                            if (key.equals("wxPayQrCodeImgByteWithBase64")) {
                                qrCode = value;
                                break;
                            }
                        }
                        PayDialog dialog = new PayDialog();
                        dialog.show(getFragmentManager(), "pay");
                    } else {
                        PayManager.pay(CoursePackDetailActivity.this, courseOrder.payType,
                                courseOrder.payCfg);
                    }
                } catch (Exception e) {
                    Toast.makeText(CoursePackDetailActivity.this, "获取订单信息失败！", Toast
                            .LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(CoursePackDetailActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.postAES("courseId=" + courseId, true);
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    CoursePackDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getOrderStatus();
                        }
                    });

                }
            }, 10000, 1000);
        }
    }

    private BroadcastReceiver onLoginSuccReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getData();
        }
    };
    private BroadcastReceiver onLoginFailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CoursePackDetailActivity.this.finish();
        }
    };

}
