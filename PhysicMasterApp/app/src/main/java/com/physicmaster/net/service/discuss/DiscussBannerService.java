package com.physicmaster.net.service.discuss;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.discuss.DiscussBannerResponse;

/**
 * Created by huashigen on 2017/5/16.
 */

public class DiscussBannerService extends OpenApiDataServiceBase {
    public DiscussBannerService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.DISCUSS_BANNER;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return DiscussBannerResponse.class;
    }
}
