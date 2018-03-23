package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.RegisterResponse;

public class RegisterService extends OpenApiDataServiceBase {

	public RegisterService(Context pContext) {
		super(pContext);
	}

	@Override
	protected String getResouceURI() {
		return ServiceURL.REGISTER;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		return RegisterResponse.class;
	}

}
