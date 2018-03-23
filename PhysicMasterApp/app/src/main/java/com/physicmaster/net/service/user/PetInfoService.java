package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.PetInfoResponse;

/**
 * Created by huashigen on 2017-07-10.
 */

public class PetInfoService extends OpenApiDataServiceBase {
    public PetInfoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.PET_INFO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return PetInfoResponse.class;
    }
}
