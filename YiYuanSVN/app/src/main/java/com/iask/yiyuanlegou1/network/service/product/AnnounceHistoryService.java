package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.AnnounceHistoryReponse;

/**
 * Created by Administrator on 2016/5/26.
 */
public class AnnounceHistoryService extends OpenApiDataServiceBase{
    public AnnounceHistoryService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.ANNOUNCE_HISTORY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AnnounceHistoryReponse.class;
    }
}
