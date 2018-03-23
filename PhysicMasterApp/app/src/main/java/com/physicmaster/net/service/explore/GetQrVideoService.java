package com.physicmaster.net.service.explore;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.explore.GetQrVideoResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetQrVideoService extends OpenApiDataServiceBase {

    public GetQrVideoService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.XITI_QR;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetQrVideoResponse.class;
    }
}
