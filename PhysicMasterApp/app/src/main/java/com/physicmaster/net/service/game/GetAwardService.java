package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.GetAwardResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetAwardService extends OpenApiDataServiceBase {

    public GetAwardService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_AWARD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetAwardResponse.class;
    }
}
