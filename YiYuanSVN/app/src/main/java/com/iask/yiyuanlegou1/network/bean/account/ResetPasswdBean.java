/* 
 * 系统名称：lswuyou
 * 类  名  称：ResetPasswdBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-2 下午3:59:48
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.security.MD5Encryption;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ResetPasswdBean {
	/** 旧密码 */
	public String opwd;
	/** 新密码 */
	public String pwd;

	public ResetPasswdBean(String opwd, String pwd) {
		this.opwd = opwd;
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("opwd=" + MD5Encryption.encodeMD5(URLEncoder.encode(opwd, Constant
					.CHARACTER_ENCODING)));
			sb.append("&pwd=" + MD5Encryption.encodeMD5(URLEncoder.encode(pwd, Constant.CHARACTER_ENCODING)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
