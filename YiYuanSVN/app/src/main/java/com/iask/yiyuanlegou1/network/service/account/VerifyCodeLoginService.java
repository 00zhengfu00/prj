/* 
 * 系统名称：lswuyou
 * 类  名  称：VerifyCodeLoginService.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-6 下午2:23:04
 * 功能说明： 登录状态下验证验证码
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.VerifyCodeResponse;


public class VerifyCodeLoginService extends OpenApiDataServiceBase {

	public VerifyCodeLoginService(Context pContext) {
		super(pContext);
	}

	@Override
	protected String getResouceURI() {
		return ServiceURL.VERIFY_CODE_LOGIN;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		return VerifyCodeResponse.class;
	}

}
