package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.BalanceDetailResponse;

/**
 * Created by Administrator on 2016/6/1.
 */
public class BalanceDetailService extends OpenApiDataServiceBase {
    public BalanceDetailService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.SHOWACCOUNT;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return BalanceDetailResponse.class;
    }
}
