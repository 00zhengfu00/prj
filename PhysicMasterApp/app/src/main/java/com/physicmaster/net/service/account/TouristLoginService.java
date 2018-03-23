package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.UserDataResponse;

/**
 * Created by huashigen on 2017-06-28.
 */

public class TouristLoginService extends OpenApiDataServiceBase {
    public TouristLoginService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.TOURIST_LOGIN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return UserDataResponse.class;
    }
}
