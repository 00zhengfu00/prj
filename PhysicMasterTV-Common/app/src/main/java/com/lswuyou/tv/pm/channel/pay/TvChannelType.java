package com.lswuyou.tv.pm.channel.pay;

/**
 * Created by wuqiang on 16-5-13.
 * TV App渠道类型
 *
 * @author wuqiang
 *
 * 1：阿里TV；2：大麦；3：当贝；4：沙发；5：海信；6：欢网；7：酷开；8：乐视；9：小米；10：易视腾；
 */
public enum TvChannelType {
    /**
     * 0：自有App，不是某个TV渠道市场，而是走我们自己的登录方式和自己的支付方式的
     */
    none,
    /**
     * 1：阿里TV
     */
    aliplay,
    /**
     * 2：大麦
     */
    damai,
    /**
     * 3：当贝
     */
    dangbei,
    /**
     * 4：沙发
     */
    shafa,
    /**
     * 5：海信
     */
    haixin,
    /**
     * 6：欢网
     */
    huanwang,
    /**
     * 7：酷开
     */
    kukai,
    /**
     * 8：乐视
     */
    leshi,
    /**
     * 9：小米
     */
    xiaomi,
    /**
     * 10：易视腾
     */
    yishiteng;
}
