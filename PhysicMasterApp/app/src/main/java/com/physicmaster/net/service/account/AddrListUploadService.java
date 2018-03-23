package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.account.CheckAddrListUploadResponse;

/**
 * Created by huashigen on 2017/5/26.
 */

public class AddrListUploadService extends OpenApiDataServiceBase {
    public AddrListUploadService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.ADDRESS_LIST_UPLOAD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CommonResponse.class;
    }
}
