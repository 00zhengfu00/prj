package com.lswuyou.tv.pm.channel.pay;

import android.app.Activity;
import android.content.Intent;

import com.dangbei.dangbeipaysdknew.DangBeiPayActivity;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/10/11.
 */
public class DangbeiChannelPayImpl implements ChannelPayInterface {
    private final String TAG = "DangbeiPay";
    private Activity mContext;

    public DangbeiChannelPayImpl(Activity context) {
        mContext = context;
    }

    @Override
    public void pay(Map<String, String> params) {
        Intent intent = new Intent();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);
            intent.putExtra(key, value);
        }
        intent.setClass(mContext, DangBeiPayActivity.class);
        mContext.startActivityForResult(intent, PayConfig.DANGBEI_REQ_CODE);
    }

    @Override
    public void pay(String... param) {

    }
}
