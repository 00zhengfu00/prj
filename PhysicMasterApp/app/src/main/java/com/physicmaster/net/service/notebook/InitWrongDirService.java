package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.notebook.InitWrongPoolResponse;

/**
 * Created by huashigen on 2018-01-05.
 */

public class InitWrongDirService extends OpenApiDataServiceBase {
    public InitWrongDirService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.INITWRONGDIR;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return InitWrongPoolResponse.class;
    }
}
