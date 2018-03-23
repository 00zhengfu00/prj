package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.notebook.RecordWrongResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class RecordSysWrongService extends OpenApiDataServiceBase {

    public RecordSysWrongService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.RECORD_SYS_WRONG;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return RecordWrongResponse.class;
    }
}
