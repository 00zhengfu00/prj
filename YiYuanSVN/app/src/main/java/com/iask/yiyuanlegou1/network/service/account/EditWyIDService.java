/* 
 * 系统名称：lswuyou
 * 类  名  称：EditWyIDService.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-7 下午5:22:29
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.UpdateUserInfoResponse;


public class EditWyIDService extends OpenApiDataServiceBase {

	public EditWyIDService(Context pContext) {
		super(pContext);
	}

	@Override
	protected String getResouceURI() {
		return ServiceURL.EDIT_WYID;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		return UpdateUserInfoResponse.class;
	}

}