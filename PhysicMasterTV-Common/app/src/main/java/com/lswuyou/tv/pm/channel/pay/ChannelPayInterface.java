package com.lswuyou.tv.pm.channel.pay;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/10.
 */
public interface ChannelPayInterface {
    public void pay(Map<String, String> params);
    public void pay(String... param);
}
