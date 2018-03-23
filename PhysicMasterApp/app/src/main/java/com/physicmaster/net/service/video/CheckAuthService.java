package com.physicmaster.net.service.video;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.video.CheckAuthResponse;

/**
 * Created by huashigen on 2017-07-21.
 */

public class CheckAuthService extends OpenApiDataServiceBase {
    public CheckAuthService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHECK_AUTH;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CheckAuthResponse.class;
    }
}
