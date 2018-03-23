package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.MyBuyRecordReponse;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MyBuyRecordService extends OpenApiDataServiceBase {
    public MyBuyRecordService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MY_BUY_RECORD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MyBuyRecordReponse.class;
    }
}
