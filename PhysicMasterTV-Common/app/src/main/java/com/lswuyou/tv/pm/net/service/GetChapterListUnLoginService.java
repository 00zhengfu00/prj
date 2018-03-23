package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse;

/**
 * Created by huashigen on 2017-09-06.
 */

public class GetChapterListUnLoginService extends OpenApiDataServiceBase {
    public GetChapterListUnLoginService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_CHAPTER_LIST_UNLOGIN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ChapterListResponse.class;
    }
}
