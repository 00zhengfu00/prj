package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.CalDetailResponse;

/**
 * Created by Administrator on 2016/5/23.
 */
public class CalDetailService extends OpenApiDataServiceBase {
    public CalDetailService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CAL_DETAIL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CalDetailResponse.class;
    }
}
