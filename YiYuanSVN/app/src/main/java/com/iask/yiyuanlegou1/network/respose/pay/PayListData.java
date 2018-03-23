package com.iask.yiyuanlegou1.network.respose.pay;

import com.iask.yiyuanlegou1.network.respose.account.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class PayListData {
    // UserInfo
    public UserInfo userInfo;
    // 支付列表
    public List<GoAppPayVo> pay;
}
