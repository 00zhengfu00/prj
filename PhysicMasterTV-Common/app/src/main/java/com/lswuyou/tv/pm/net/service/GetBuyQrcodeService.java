package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.member.GetBuyQrcodeResponse;

/**
 * Created by huashigen on 2017-09-08.
 */

public class GetBuyQrcodeService extends OpenApiDataServiceBase {
    public GetBuyQrcodeService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_BUY_QRCODE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetBuyQrcodeResponse.class;
    }
}
