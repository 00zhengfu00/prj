package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.CheckAddrListUploadResponse;

/**
 * Created by huashigen on 2017/5/26.
 */

public class CheckAddrListUploadService extends OpenApiDataServiceBase {
    public CheckAddrListUploadService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHECK_ADDRESS_LIST_UPLOAD_STATE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CheckAddrListUploadResponse.class;
    }
}
