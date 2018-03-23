package com.iask.yiyuanlegou1.network.respose.account;

import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MyBuyRecordBean {
    // 购买时间
    private String time;
    // 购买码
    private List<String> code;
    // 购买个数
    private Integer num;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }
}
