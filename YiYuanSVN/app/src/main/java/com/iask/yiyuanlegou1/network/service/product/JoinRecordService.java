package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.ProductJoinRecordReponse;

/**
 * Created by Administrator on 2016/5/24.
 */
public class JoinRecordService extends OpenApiDataServiceBase{
    public JoinRecordService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.PRODUCT_JOIN_RECORD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ProductJoinRecordReponse.class;
    }
}
