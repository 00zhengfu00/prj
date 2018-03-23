package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.HelperResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class HelperService extends OpenApiDataServiceBase {

    public HelperService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.FEED_BACK;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return HelperResponse.class;
    }
}
