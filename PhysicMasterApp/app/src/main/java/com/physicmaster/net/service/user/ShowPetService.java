package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.ShowPetResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class ShowPetService extends OpenApiDataServiceBase {

    public ShowPetService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.SHOW_PET;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ShowPetResponse.class;
    }
}
