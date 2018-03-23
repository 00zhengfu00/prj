package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.MemebersResponse;

/**
 * Created by huashigen on 2016/11/26.
 */
public class MembersService extends OpenApiDataServiceBase {
    public MembersService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.EXPLORE_MEMBER;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MemebersResponse.class;
    }
}
