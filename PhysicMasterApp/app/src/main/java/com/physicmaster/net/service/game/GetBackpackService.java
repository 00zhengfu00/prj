package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.GetBackpackResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetBackpackService extends OpenApiDataServiceBase {

    public GetBackpackService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.BACKPACK;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetBackpackResponse.class;
    }
}
