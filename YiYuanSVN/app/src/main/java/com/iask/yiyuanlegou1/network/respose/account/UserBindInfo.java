/* 
 * 系统名称：lswuyou
 * 类  名  称：UserBindInfo.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-19 上午11:01:21
 * 功能说明： 用户绑定第三方账户信息
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.respose.account;

import java.io.Serializable;

public class UserBindInfo implements Serializable {

	private static final long serialVersionUID = -448668542038210091L;
	/** 绑定的具体信息（邮箱/手机时，此项分别是邮箱和手机；其他第三方账号，就是第三方登录的uid参数） */
	private String bindId;
	/** 绑定类型；0：邮箱，1：手机，2：微信，3：QQ，4：微博 */
	private String bindType;
	/** 绑定的第三方账号的昵称（邮箱/手机时，此项为空） */
	private String tpNickname;

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getBindType() {
		return bindType;
	}

	public void setBindType(String bindType) {
		this.bindType = bindType;
	}

	public String getTpNickname() {
		return tpNickname;
	}

	public void setTpNickname(String tpNickname) {
		this.tpNickname = tpNickname;
	}
}
