package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.ExploreV2Response;

/**
 * Created by huashigen on 2017/4/11.
 */

public class ExploreV2Service extends OpenApiDataServiceBase {
    public ExploreV2Service(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.EXPLOREV2;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ExploreV2Response.class;
    }
}
