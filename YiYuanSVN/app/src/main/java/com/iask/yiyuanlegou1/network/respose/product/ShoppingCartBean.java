package com.iask.yiyuanlegou1.network.respose.product;

import com.iask.yiyuanlegou1.network.respose.home.HomePageProductBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ShoppingCartBean extends HomePageProductBean implements Serializable{
    // 用户购买次数
    protected Integer buyCount;

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }
}
