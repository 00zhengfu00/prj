package com.iask.yiyuanlegou1.network.service.pay;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.pay.BalancePayResponse;

/**
 * Created by Administrator on 2016/6/1.
 */
public class BalancePayService extends OpenApiDataServiceBase{
    public BalancePayService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.BALANCE_PAY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return BalancePayResponse.class;
    }
}
