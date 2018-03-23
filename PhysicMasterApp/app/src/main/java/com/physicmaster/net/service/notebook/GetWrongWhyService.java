package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.notebook.GetWrongWhyResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetWrongWhyService extends OpenApiDataServiceBase {

    public GetWrongWhyService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_WRONG_WHY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetWrongWhyResponse.class;
    }
}
