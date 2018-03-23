package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.ShowEndeavorResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class ShowEndeavorService extends OpenApiDataServiceBase {

    public ShowEndeavorService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.PET_EFFORT;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ShowEndeavorResponse.class;
    }
}
