package com.iask.yiyuanlegou1.network.service.account;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/6/1.
 */
public class BalanceDetailBean {
    //账户金额
    private BigDecimal money;
    //资金渠道
    private String content;
    //纪录时间戳
    private String timeStamp;
    //以年-月-日 时:分:秒显示的时间
    private String fmtTime;

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getFmtTime() {
        return fmtTime;
    }

    public void setFmtTime(String fmtTime) {
        this.fmtTime = fmtTime;
    }
}
