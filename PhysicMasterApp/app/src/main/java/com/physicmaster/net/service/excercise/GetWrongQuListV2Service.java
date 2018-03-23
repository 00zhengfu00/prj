package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.GetWrongQuListV2Response;

/**
 * Created by huashigen on 2017/4/18.
 */

public class GetWrongQuListV2Service extends OpenApiDataServiceBase {

    public GetWrongQuListV2Service(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_WRONG_QU_LIST_V2;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetWrongQuListV2Response.class;
    }
}
