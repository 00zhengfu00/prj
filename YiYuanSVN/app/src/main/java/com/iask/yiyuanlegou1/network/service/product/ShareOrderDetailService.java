package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.ShareOrderDetailResponse;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ShareOrderDetailService extends OpenApiDataServiceBase{
    public ShareOrderDetailService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.SHARE_ORDER_DETAIL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ShareOrderDetailResponse.class;
    }
}
