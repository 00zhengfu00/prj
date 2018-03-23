package com.physicmaster.net.service.discuss;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.CommonResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class LoseMessageService extends OpenApiDataServiceBase {

    public LoseMessageService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MESSAGE_NEWS;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CommonResponse.class;
    }
}
