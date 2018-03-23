package com.iask.yiyuanlegou1.network.service.pay;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.pay.RechargeListResponse;

/**
 * Created by Administrator on 2016/6/6.
 */
public class RechargeListService extends OpenApiDataServiceBase {
    public RechargeListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.RECHARGE_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return RechargeListResponse.class;
    }
}
