package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.account.AdsImgResponse;

/**
 * Created by Administrator on 2016/8/9.
 */
public class GetAdsImgService extends OpenApiDataServiceBase {
    public GetAdsImgService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_ADS_IMG;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AdsImgResponse.class;
    }
}
