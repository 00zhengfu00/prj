package com.lswuyou.tv.pm.net.response.order;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/18.
 */
public class CourseOrder {
    public String orderId;// 订单ID，用于后续查询订单状态的参数
    public Map<String,String> payCfg;//支付参数
    public String payType;//支付渠道名称
}
