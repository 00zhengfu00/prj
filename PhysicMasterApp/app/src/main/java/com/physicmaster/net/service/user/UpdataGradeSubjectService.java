package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.UserDataResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class UpdataGradeSubjectService extends OpenApiDataServiceBase {

    public UpdataGradeSubjectService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.UPDATA_GRADE_SUBJECT;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return UserDataResponse.class;
    }
}
