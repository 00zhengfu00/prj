package com.iask.yiyuanlegou1.network.respose.pay;

/**
 * Created by Administrator on 2016/6/1.
 */
public class WeixinPayBean {
    // 应用ID
    private String appid;
    // 商户号
    private String partnerid;
    // 预支付交易会话ID
    private String prepayid;
    // 扩展字段
    private String packageInfo;
    // 随机字符串
    private String noncestr;
    // 时间戳
    private String timestamp;
    // 签名
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
