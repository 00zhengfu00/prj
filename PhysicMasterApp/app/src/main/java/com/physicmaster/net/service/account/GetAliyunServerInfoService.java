package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.GetAliyunServerInfoResponse;

/**
 * Created by Administrator on 2016/11/21.
 */
public class GetAliyunServerInfoService extends OpenApiDataServiceBase {
    public GetAliyunServerInfoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_ALIYUN_SERVER_INFO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetAliyunServerInfoResponse.class;
    }
}
