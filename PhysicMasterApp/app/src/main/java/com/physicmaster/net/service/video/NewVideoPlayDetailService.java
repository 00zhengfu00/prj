package com.physicmaster.net.service.video;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.NewVideoPlayResponse;

/**
 * Created by huashigen on 2016/11/26.
 */
public class NewVideoPlayDetailService extends OpenApiDataServiceBase {
    public NewVideoPlayDetailService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.NEW_VIDEO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return NewVideoPlayResponse.class;
    }
}
