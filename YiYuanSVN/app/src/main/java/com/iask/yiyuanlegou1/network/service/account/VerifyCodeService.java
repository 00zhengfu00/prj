package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.CommonResponse;


public class VerifyCodeService extends OpenApiDataServiceBase {

	public VerifyCodeService(Context pContext) {
		super(pContext);
	}

	@Override
	protected String getResouceURI() {
		return ServiceURL.VERIFY_CODE;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		return CommonResponse.class;
	}

}
