package com.lswuyou.tv.pm.channel.pay;

import android.app.Activity;

import com.lswuyou.tv.pm.view.PayDialog;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/10/12.
 */
public class PayManager {
    public static void pay(Activity context, String channel, Map<String, String> params) {
        ChannelPayInterface channelPay = null;
        if (channel.equals(TvChannelType.dangbei.name())) {
            channelPay = new DangbeiChannelPayImpl(context);
            channelPay.pay(params);
        }
        if (channel.equals(TvChannelType.aliplay.name())) {
//            channelPay = new AliChannelPayImpl(context);
//            channelPay.pay(params);
        }
        if (channel.equals(TvChannelType.damai.name())) {
//            channelPay = new DamaiChannelPayImpl(context);
//            channelPay.pay(params);
        }
        if (channel.equals(TvChannelType.haixin.name())) {
//            channelPay = new HaixinChannelPayImpl(context);
//            channelPay.pay(params);
        }
        if (channel.equals(TvChannelType.huanwang.name())) {
//            channelPay = new HuanwangChannelPayImpl(context);
//            channelPay.pay(params);
        }
        if (channel.equals(TvChannelType.kukai.name())) {

        }
        if (channel.equals(TvChannelType.leshi.name())) {
//            channelPay = new LeshiChannelPayImpl(context);
//            channelPay.pay(params);
        }
        if (channel.equals(TvChannelType.xiaomi.name())) {
//            channelPay = new XiaomiChannelPayImpl(context);
//            channelPay.pay(params);
        }
        if (channel.equals(TvChannelType.yishiteng.name())) {

        }
        if (channel.equals(TvChannelType.shafa.name())) {
//            channelPay = new ShafaChannelPayImpl(context);
//            channelPay.pay(params);
        }
    }
}
