package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.GetMemebersResponse;

/**
 * Created by huashigen on 2016/11/26.
 */
public class GetMembersService extends OpenApiDataServiceBase {
    public GetMembersService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MEMBERS_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetMemebersResponse.class;
    }
}
