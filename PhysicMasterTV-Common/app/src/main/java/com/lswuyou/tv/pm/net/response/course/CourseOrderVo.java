package com.lswuyou.tv.pm.net.response.course;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/17.
 */
public class CourseOrderVo implements Serializable{
    // 订单ID，用于请求验证订单是否支付
    public String orderId;
    public String wxPayQrCodeImgByteWithBase64;
}
