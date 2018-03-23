package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.account.GetFavListResponse;

/**
 * Created by huashigen on 2017-09-08.
 */

public class GetFavListService extends OpenApiDataServiceBase {
    public GetFavListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_FAV_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetFavListResponse.class;
    }
}
