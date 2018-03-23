package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.GetTimeResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetTimeService extends OpenApiDataServiceBase {

    public GetTimeService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GETTIME;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetTimeResponse.class;
    }
}
