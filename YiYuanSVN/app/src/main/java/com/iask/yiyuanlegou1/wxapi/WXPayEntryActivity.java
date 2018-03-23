package com.iask.yiyuanlegou1.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.common.Constant;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private LocalBroadcastManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_ID);
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
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            manager = LocalBroadcastManager.getInstance(this);
            manager.sendBroadcast(intent);
        }
        finish();
    }
}