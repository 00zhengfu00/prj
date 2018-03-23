package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.UserDataResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GuideFinishService extends OpenApiDataServiceBase {

    public GuideFinishService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.LOGIN_INITIAL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return UserDataResponse.class;
    }
}
