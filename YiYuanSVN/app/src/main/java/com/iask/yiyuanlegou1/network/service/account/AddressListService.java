package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.AddressListResponse;

/**
 * Created by Administrator on 2016/6/2.
 */
public class AddressListService extends OpenApiDataServiceBase{

    public AddressListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.ADDRESS_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AddressListResponse.class;
    }
}
