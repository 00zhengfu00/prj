/* 
 * 系统名称：lswuyou
 * 类  名  称：ResetPasswdService.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-2 下午3:16:43
 * 功能说明： 登录状态重置或者设置密码
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.CommonResponse;


public class ResetPasswdService extends OpenApiDataServiceBase {

	public ResetPasswdService(Context pContext) {
		super(pContext);
	}

	@Override
	protected String getResouceURI() {
		return ServiceURL.RESET_PASSWD;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		return CommonResponse.class;
	}

}