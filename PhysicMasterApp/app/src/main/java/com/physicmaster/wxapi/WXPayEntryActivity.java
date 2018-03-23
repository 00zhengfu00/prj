package com.physicmaster.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.physicmaster.R;
import com.physicmaster.common.Constant;
import com.physicmaster.utils.UIUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private LocalBroadcastManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        String packageName = getPackageName();
        if (packageName.equals(Constant.PHYSICMASTER)) {
            api = WXAPIFactory.createWXAPI(this, Constant.PM_WEIXIN_APP_ID);
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            api = WXAPIFactory.createWXAPI(this, Constant.CM_WEIXIN_APP_ID);
        }else if (packageName.equals(Constant.MATHMASTER)) {
            api = WXAPIFactory.createWXAPI(this, Constant.MM_WEIXIN_APP_ID);
        }
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Intent intent = new Intent(Constant.WEIXIN_PAY_FINISHED);
        intent.putExtra("errCode", resp.errCode);
        if (resp.errStr != null) {
            intent.putExtra("errStr", resp.errStr);
        }
        //发送微信支付成功广播
        manager = LocalBroadcastManager.getInstance(this);
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            manager.sendBroadcast(intent);
        } else {
            UIUtils.showToast(this, "支付失败");
            intent.putExtra("fail",true);
            manager.sendBroadcast(intent);
        }
        finish();
    }
}