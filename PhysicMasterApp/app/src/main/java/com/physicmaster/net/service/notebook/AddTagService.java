package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.notebook.AddDirResponse;

/**
 * Created by huashigen on 2017-12-22.
 */

public class AddTagService extends OpenApiDataServiceBase {
    public AddTagService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.ADD_TAG;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AddDirResponse.class;
    }
}
