package com.iask.yiyuanlegou1.home.main.product.pay;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.utils.RSAHelper;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.home.person.setting.BalanceDetailActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.pay.IAppPayResponse;
import com.iask.yiyuanlegou1.network.respose.pay.MoneyBean;
import com.iask.yiyuanlegou1.network.respose.pay.RechargeBean;
import com.iask.yiyuanlegou1.network.respose.pay.RechargeListResponse;
import com.iask.yiyuanlegou1.network.respose.pay.WeixinPayBean;
import com.iask.yiyuanlegou1.network.respose.pay.WeixinPayResponse;
import com.iask.yiyuanlegou1.network.service.pay.IAppPayService;
import com.iask.yiyuanlegou1.network.service.pay.RechargeListService;
import com.iask.yiyuanlegou1.network.service.pay.WeixinPayService;
import com.iask.yiyuanlegou1.widget.TitleBarView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarView title;
    private LinearLayout rechargeLayout1, rechargeLayout2, rechargeLayout3, rechargeLayout4,
            rechargeLayout5, rechargeList;
    private EditText rechargeLayout6;
    private TextView rechargeTv1, rechargeTv2, rechargeTv3, rechargeTv4, rechargeTv5;
    private TextView rewardTv1, rewardTv2, rewardTv3, rewardTv4, rewardTv5;
    private List<MoneyBean> moneyBeans;
    private List<RechargeBean> rechargeBeans;
    private String payType = "";
    private IWXAPI api;
    private int money = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void findViewById() {
        rechargeLayout1 = (LinearLayout) findViewById(R.id.charge_btn_1);
        rechargeLayout2 = (LinearLayout) findViewById(R.id.charge_btn_2);
        rechargeLayout3 = (LinearLayout) findViewById(R.id.charge_btn_3);
        rechargeLayout4 = (LinearLayout) findViewById(R.id.charge_btn_4);
        rechargeLayout5 = (LinearLayout) findViewById(R.id.charge_btn_5);
        rechargeLayout6 = (EditText) findViewById(R.id.charge_btn_6);
        rechargeList = (LinearLayout) findViewById(R.id.recharge_list);

        rechargeLayout1.setOnClickListener(this);
        rechargeLayout2.setOnClickListener(this);
        rechargeLayout3.setOnClickListener(this);
        rechargeLayout4.setOnClickListener(this);
        rechargeLayout5.setOnClickListener(this);
        rechargeLayout6.setOnClickListener(this);

        rechargeTv1 = (TextView) findViewById(R.id.charge_money1);
        rechargeTv2 = (TextView) findViewById(R.id.charge_money2);
        rechargeTv3 = (TextView) findViewById(R.id.charge_money3);
        rechargeTv4 = (TextView) findViewById(R.id.charge_money4);
        rechargeTv5 = (TextView) findViewById(R.id.charge_money5);

        rewardTv1 = (TextView) findViewById(R.id.reward_money1);
        rewardTv2 = (TextView) findViewById(R.id.reward_money2);
        rewardTv3 = (TextView) findViewById(R.id.reward_money3);
        rewardTv4 = (TextView) findViewById(R.id.reward_money4);
        rewardTv5 = (TextView) findViewById(R.id.reward_money5);

        rechargeLayout1.setSelected(true);

        findViewById(R.id.recharge_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rechargeLayout6.isSelected()) {
                    String textMoney = rechargeLayout6.getText().toString();
                    if (!TextUtils.isEmpty(textMoney) && TextUtils.isDigitsOnly(textMoney)) {
                        money = Integer.parseInt(textMoney);
                    } else {
                        money = 0;
                    }
                }
                if (money == 0) {
                    Toast.makeText(RechargeActivity.this, "请输入有效金额！", Toast.LENGTH_SHORT).show();
                    return;
                }
                doPay(payType);
            }
        });
    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setTitleText(R.string.recharge);
        title.setBtnRight(0, R.string.recharge_record);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargeActivity.this.finish();
            }
        });
        title.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RechargeActivity.this, BalanceDetailActivity.class));
            }
        });
        getData();
    }

    private void getData() {
        RechargeListService service = new RechargeListService(this);
        service.setCallback(new IOpenApiDataServiceCallback<RechargeListResponse>() {
            @Override
            public void onGetData(RechargeListResponse data) {
                try {
                    moneyBeans = data.data.moneyList;
                    rechargeBeans = data.data.rechargeList;
                    runOnUiThread(new Runnable() {
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
            public void onGetError(int errorCode, final String errorMsg, Throwable error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        service.post("", true);
    }

    private void refreshUI() {
        rechargeTv1.setText(moneyBeans.get(0).getChong() + "元");
        rewardTv1.setText("赠送:" + moneyBeans.get(0).getFan() + "元");
        rechargeTv2.setText(moneyBeans.get(1).getChong() + "元");
        rewardTv2.setText("赠送:" + moneyBeans.get(1).getFan() + "元");
        rechargeTv3.setText(moneyBeans.get(2).getChong() + "元");
        rewardTv3.setText("赠送:" + moneyBeans.get(2).getFan() + "元");
        rechargeTv4.setText(moneyBeans.get(3).getChong() + "元");
        rewardTv4.setText("赠送:" + moneyBeans.get(3).getFan() + "元");
        rechargeTv5.setText(moneyBeans.get(4).getChong() + "元");
        rewardTv5.setText("赠送:" + moneyBeans.get(4).getFan() + "元");

        for (final RechargeBean vo : rechargeBeans) {
            if (vo.getType().equals("appMoney")) {
                continue;
            }
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
            title.setText(vo.getName());
            desc.setText(vo.getContent());
            rechargeList.addView(item);
            if (vo.getType().equals(PayConfig.IPAY)) {
                if (TextUtils.isEmpty(payType)) {
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
                setCheckState(box);
                payType = PayConfig.WEIXIN_PAY;
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setCheckState(box);
                        payType = vo.getType();
                    }
                });

            }
        }
        money = moneyBeans.get(0).getChong();
    }

    private void setCheckState(View b) {
        for (int i = 0; i < rechargeList.getChildCount(); i++) {
            View view = rechargeList.getChildAt(i);
            CheckBox box = (CheckBox) view.findViewById(R.id.pay_selected);
            if (box == b) {
                box.setChecked(true);
            } else {
                box.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        setSelected(v.getId());
    }

    private void setSelected(int id) {
        rechargeLayout1.setSelected(false);
        rechargeLayout2.setSelected(false);
        rechargeLayout3.setSelected(false);
        rechargeLayout4.setSelected(false);
        rechargeLayout5.setSelected(false);
        rechargeLayout6.setSelected(false);

        findViewById(id).setSelected(true);
        if (moneyBeans == null || moneyBeans.size() == 0) {
            return;
        }
        switch (id) {
            case R.id.charge_btn_1:
                money = moneyBeans.get(0).getChong();
                break;
            case R.id.charge_btn_2:
                money = moneyBeans.get(1).getChong();
                break;
            case R.id.charge_btn_3:
                money = moneyBeans.get(2).getChong();
                break;
            case R.id.charge_btn_4:
                money = moneyBeans.get(3).getChong();
                break;
            case R.id.charge_btn_5:
                money = moneyBeans.get(4).getChong();
                break;
            case R.id.charge_btn_6:
                break;
            default:
                break;
        }
    }

    private void doPay(String type) {
        if (type.equals(PayConfig.WEIXIN_PAY)) {
            doWeixinPay();
        } else if (type.equals(PayConfig.IPAY)) {
            doIPay();
        }
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
        service.post("money=" + money, true);
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
                    Toast.makeText(RechargeActivity.this, "pay success, but sign value is null",
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
                    Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RechargeActivity.this, "pay success, sign error", Toast
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
                Toast.makeText(RechargeActivity.this,
                        "支付失败:[" + "错误码:" + resultCode + "," + (TextUtils.isEmpty(resultInfo) ?
                                "未知错误" : resultInfo) + "]", Toast.LENGTH_LONG)
                        .show();
            }
        });
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
                Toast.makeText(RechargeActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
//        service.post("money=0.01", true);
        service.post("money=" + money, true);
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
}
