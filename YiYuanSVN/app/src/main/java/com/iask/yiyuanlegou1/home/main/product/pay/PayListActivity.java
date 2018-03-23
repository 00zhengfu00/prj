package com.iask.yiyuanlegou1.home.main.product.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.utils.RSAHelper;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.home.HomeActivity;
import com.iask.yiyuanlegou1.home.shopping.BuyInfo;
import com.iask.yiyuanlegou1.home.shopping.BuyInfoWrapper;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.account.UserInfo;
import com.iask.yiyuanlegou1.network.respose.pay.BalancePayResponse;
import com.iask.yiyuanlegou1.network.respose.pay.GoAppPayVo;
import com.iask.yiyuanlegou1.network.respose.pay.IAppPayResponse;
import com.iask.yiyuanlegou1.network.respose.pay.PayListResponse;
import com.iask.yiyuanlegou1.network.respose.pay.WeixinPayBean;
import com.iask.yiyuanlegou1.network.respose.pay.WeixinPayResponse;
import com.iask.yiyuanlegou1.network.service.pay.BalancePayService;
import com.iask.yiyuanlegou1.network.service.pay.IAppPayService;
import com.iask.yiyuanlegou1.network.service.pay.PayListService;
import com.iask.yiyuanlegou1.network.service.pay.WeixinPayService;
import com.iask.yiyuanlegou1.widget.TitleBarView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.net.URLDecoder;
import java.util.List;

public class PayListActivity extends BaseActivity {
    private LinearLayout layoutPaylist;
    private List<GoAppPayVo> payList;
    private UserInfo userInfo;
    private TitleBarView title;
    private int pPrice;
    private IWXAPI api;
    private LinearLayout balanceLayout;
    private BuyInfo[] buyInfos;
    private LocalBroadcastManager manager;
    private String payType = PayConfig.BALANCE_PAY;

    @Override
    protected void findViewById() {
        layoutPaylist = (LinearLayout) findViewById(R.id.layout_pay_list);
        balanceLayout = (LinearLayout) findViewById(R.id.balance_layout);
    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setTitleText(R.string.pay_order);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayListActivity.this.finish();
            }
        });
        title.setBtnRight(0, R.string.recharge);
        title.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayListActivity.this, RechargeActivity.class));
            }
        });
        pPrice = getIntent().getIntExtra("pPrice", 0);
        initOrder(getIntent().getIntExtra("pNum", 0), pPrice);
        Bundle bundle = getIntent().getBundleExtra("buyInfo");
        BuyInfoWrapper wrapper = (BuyInfoWrapper) bundle.getSerializable("buyInfo");
        buyInfos = wrapper.buyInfos;
        getPayList();

        manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(weixinPayReceiver, new IntentFilter(Constant.WEIXIN_PAY_FINISHED));
    }

    private void initOrder(int pNum, int pPrice) {
        TextView tvPnum = (TextView) findViewById(R.id.tv_pro_count);
        TextView tvPrice = (TextView) findViewById(R.id.tv_pro_price);
        tvPnum.setText(pNum + "");
        tvPrice.setText(pPrice + "");
        findViewById(R.id.pay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPay(payType);
            }
        });
    }

    private void doPay(String type) {
        if (type.equals(PayConfig.WEIXIN_PAY)) {
            doWeixinPay();
        } else if (type.equals(PayConfig.IPAY)) {
            doIPay();
        } else if (type.equals(PayConfig.BALANCE_PAY)) {
            doBalancePay();
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pay_list;
    }

    private void getPayList() {
        PayListService service = new PayListService(this);
        service.setCallback(new IOpenApiDataServiceCallback<PayListResponse>() {
            @Override
            public void onGetData(PayListResponse data) {
                try {
                    payList = data.data.settlement.pay;
                    userInfo = data.data.settlement.userInfo;
                    PayListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(PayListActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("", false);
    }

    private void setCheckState(View b) {
        for (int i = 0; i < layoutPaylist.getChildCount(); i++) {
            View view = layoutPaylist.getChildAt(i);
            CheckBox box = (CheckBox) view.findViewById(R.id.pay_selected);
            if (box == b) {
                box.setChecked(true);
            } else {
                box.setChecked(false);
            }
        }
    }

    private void refreshUI() {
        TextView tvBalance = (TextView) findViewById(R.id.tv_balance);
        int money = 0;
        if (userInfo.getMoney() != null) {
            money = userInfo.getMoney().intValue();
        }
        tvBalance.setText("（余额:" + money + "元）");
        if (money < pPrice) {
            findViewById(R.id.tv_balance_short).setVisibility(View.VISIBLE);
        } else {
            balanceLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doBalancePay();
                }
            });
        }
        for (final GoAppPayVo vo : payList) {
            View item = View.inflate(this, R.layout.layout_recharge_item, null);
            ImageView image = (ImageView) item.findViewById(R.id.iv_pay);
            TextView title = (TextView) item.findViewById(R.id.tv_pay);
            TextView desc = (TextView) item.findViewById(R.id.tv_desc);
            final CheckBox box = (CheckBox) item.findViewById(R.id.pay_selected);
            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCheckState(box);
                    payType = vo.getType();
                }
            });
            Glide.with(this).load(vo.getImg()).into(image);
            desc.setText(vo.getContent());
            layoutPaylist.addView(item);
            if (vo.getType().equals(PayConfig.IPAY)) {
                title.setText(vo.getName());
                if (money < pPrice) {
                    setCheckState(box);
                    payType = PayConfig.IPAY;
                } else {
                    box.setChecked(false);
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setCheckState(box);
                        payType = vo.getType();
                    }
                });
            } else if (vo.getType().equals(PayConfig.WEIXIN_PAY)) {
                title.setText(vo.getName());
                if (money < pPrice) {
                    setCheckState(box);
                    payType = PayConfig.WEIXIN_PAY;
                } else {
                    box.setChecked(false);
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setCheckState(box);
                        payType = vo.getType();
                    }
                });
            } else if (vo.getType().equals(PayConfig.BALANCE_PAY)) {
                title.setText(vo.getName() + "（" + money + "元）");
                if (money < pPrice) {
                    box.setVisibility(View.GONE);
                    desc.setText("余额不足");
                    desc.setTextColor(getResources().getColor(R.color.pink));
                } else {
                    box.setChecked(true);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setCheckState(box);
                            payType = vo.getType();
                        }
                    });
                }
            } else if (vo.getType().equals(PayConfig.YUN_PAY)) {

            }
        }
    }

    /**
     * 余额支付
     */
    private void doBalancePay() {
        BalancePayService service = new BalancePayService(this);
        service.setCallback(new IOpenApiDataServiceCallback<BalancePayResponse>() {
            @Override
            public void onGetData(final BalancePayResponse data) {
                try {
                    userInfo = data.data.userInfo;
                    CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.SHOPPING_CAR);
                    BaseApplication.getInstance().setShoppingCarData(null);
                    PayListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tvBalance = (TextView) findViewById(R.id.tv_balance);
                            tvBalance.setText(userInfo.getMoney() + "");
                            Toast.makeText(PayListActivity.this, data.data.msgInfo.getMainMsg(),
                                    Toast.LENGTH_SHORT).show();
                            LocalBroadcastManager.getInstance(PayListActivity.this).sendBroadcast
                                    (new Intent(HomeActivity.PRODUCT_NUM_CHANGED));
                            PayListActivity.this.finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(PayListActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("cartlist=" + JSON.toJSONString(buyInfos), true);
    }

    /**
     * 微信支付
     */
    private void doWeixinPay() {
        WeixinPayService service = new WeixinPayService(this);
        service.setCallback(new IOpenApiDataServiceCallback<WeixinPayResponse>() {
            @Override
            public void onGetData(WeixinPayResponse data) {
                try {
                    callWeixinPay(data.data.wxPayment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(PayListActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
//        service.post("money=0.01", true);
        service.post("money=" + pPrice, true);
    }

    /**
     * 调起微信支付
     */
    private void callWeixinPay(WeixinPayBean bean) {
        PayReq req = new PayReq();
        api = WXAPIFactory.createWXAPI(this, null);
        req.appId = bean.getAppid();
        req.partnerId = bean.getPartnerid();
        req.prepayId = bean.getPrepayid();
        req.nonceStr = bean.getNoncestr();
        req.timeStamp = bean.getTimestamp();
        req.packageValue = bean.getPackageInfo();
        req.sign = bean.getSign();
        req.extData = "app data";

        api.registerApp(Constant.WEIXIN_APP_ID);
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    /****************************
     * 爱贝支付
     *****************************************/
    private static final String TAG = PayListActivity.class.getSimpleName();

    /**
     * 爱贝支付
     */
    private void doIPay() {
        IAppPayService service = new IAppPayService(this);
        service.setCallback(new IOpenApiDataServiceCallback<IAppPayResponse>() {
            @Override
            public void onGetData(IAppPayResponse data) {
                try {
                    String transId = data.data.transid;
                    startIPay(transId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.post("money=" + pPrice, true);
//        service.post("money=0.01", true);
    }

    private void startIPay(String transId) {
        String param = "transid=" + transId + "&appid=" + PayConfig.appid;
        IAppPay.startPay(this, param, new IPayResultCallback() {

            @Override
            public void onPayResult(int resultCode, String signvalue, String resultInfo) {
                // TODO Auto-generated method stub

                switch (resultCode) {
                    case IAppPay.PAY_SUCCESS:
                        dealPaySuccess(signvalue);
                        break;
                    default:
                        dealPayError(resultCode, resultInfo);
                        break;
                }
            }


            /**
             * 4.支付成功。
             *  需要对应答返回的签名做验证，只有在签名验证通过的情况下，才是真正的支付成功
             */
            private void dealPaySuccess(String signValue) {
                Log.i(TAG, "sign = " + signValue);
                if (TextUtils.isEmpty(signValue)) {
                    /**
                     *  没有签名值
                     */
                    Log.e(TAG, "pay success,but it's signValue is null");
                    Toast.makeText(PayListActivity.this, "pay success, but sign value is null",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                boolean isvalid = false;
                try {
                    isvalid = signCpPaySuccessInfo(signValue);
                } catch (Exception e) {

                    isvalid = false;
                }
                if (isvalid) {
                    Toast.makeText(PayListActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                    doBalancePay();
                } else {
                    Toast.makeText(PayListActivity.this, "pay success, sign error", Toast
                            .LENGTH_LONG).show();
                }

            }

            /**
             * valid cp callback sign
             * @param signValue
             * @return
             * @throws Exception
             *
             * transdata={"cporderid":"1","transid":"2","appid":"3","waresid":31,
             * "feetype":4,"money":5,"count":6,"result":0,"transtype":0,
             * "transtime":"2012-12-12 12:11:10","cpprivate":"7",
             * "paytype":1}&sign=xxxxxx&signtype=RSA
             */
            private boolean signCpPaySuccessInfo(String signValue) throws Exception {
                int transdataLast = signValue.indexOf("&sign=");
                String transdata = URLDecoder.decode(signValue.substring("transdata=".length(),
                        transdataLast));

                int signLast = signValue.indexOf("&signtype=");
                String sign = URLDecoder.decode(signValue.substring(transdataLast + "&sign="
                        .length(), signLast));

                String signtype = signValue.substring(signLast + "&signtype=".length());

                if (signtype.equals("RSA") && RSAHelper.verify(transdata, PayConfig.publicKey,
                        sign)) {
                    return true;
                } else {
                    Log.e(TAG, "wrong type ");
                }
                return false;
            }

            private void dealPayError(int resultCode, String resultInfo) {
                Log.e(TAG, "failure pay, callback cp errorinfo : " + resultCode + "," + resultInfo);
                Toast.makeText(PayListActivity.this,
                        "支付失败:[" + "错误码:" + resultCode + "," + (TextUtils.isEmpty(resultInfo) ?
                                "未知错误" : resultInfo) + "]", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private BroadcastReceiver weixinPayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doBalancePay();
        }
    };

    @Override
    protected void onDestroy() {
        if (weixinPayReceiver != null) {
            manager.unregisterReceiver(weixinPayReceiver);
        }
        super.onDestroy();
    }
}
