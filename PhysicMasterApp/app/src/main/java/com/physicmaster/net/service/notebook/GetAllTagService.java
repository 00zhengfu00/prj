package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.notebook.AddDirResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetAllTagService extends OpenApiDataServiceBase {

    public GetAllTagService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_ALL_DIR;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AddDirResponse.class;
    }
}
