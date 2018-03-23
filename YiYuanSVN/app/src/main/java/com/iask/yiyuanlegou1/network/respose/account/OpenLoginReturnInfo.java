/* 
 * 系统名称：lswuyou
 * 类  名  称：OpenLoginReturnInfo.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-19 上午11:11:22
 * 功能说明： 登录返回的数据
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.respose.account;

import java.io.Serializable;


public class OpenLoginReturnInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8454869954605429922L;
	/** 登录返回的用户信息 */
	private LoginUserInfo loginVo;
	/** 这个就是用于后续通信的AES密钥，进行Base64 Decode后即可获得 密钥原文 */
	private String userSecret;

	public LoginUserInfo getLoginVo() {
		return loginVo;
	}

	public void setLoginVo(LoginUserInfo loginVo) {
		this.loginVo = loginVo;
	}

	public String getUserSecret() {
		return userSecret;
	}

	public void setUserSecret(String userSecret) {
		this.userSecret = userSecret;
	}

}
