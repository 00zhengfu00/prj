package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.MainInfoResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class MainInfoService extends OpenApiDataServiceBase {

    public MainInfoService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.INDEX;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MainInfoResponse.class;
    }
}
