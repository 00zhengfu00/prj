/* 
 * 系统名称：lswuyou
 * 类  名  称：EditWyIDBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-7 下午5:25:45
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

public class EditWyIDBean {

	/** 无忧ID */
	public String wid;

	public EditWyIDBean(String wid) {
		this.wid = wid;
	}

	@Override
	public String toString() {
		return "wid=" + wid;
	}
}
