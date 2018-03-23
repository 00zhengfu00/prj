package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.GetMedalListResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class CheckMedalService extends OpenApiDataServiceBase {

    public CheckMedalService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHECK_MEDAL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetMedalListResponse.class;
    }
}
