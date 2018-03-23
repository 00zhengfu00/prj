package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.MainInfoResponse;
import com.physicmaster.net.response.user.PetInfoResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class PetAdvancedService extends OpenApiDataServiceBase {

    public PetAdvancedService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.UP_STAGE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return PetInfoResponse.class;
    }
}
