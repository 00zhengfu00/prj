package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.GetPlanListResponse;

/**
 * Created by huashigen on 2017/1/18.
 */

public class GetPlanListService extends OpenApiDataServiceBase {
    public GetPlanListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_PLAN_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetPlanListResponse.class;
    }
}
