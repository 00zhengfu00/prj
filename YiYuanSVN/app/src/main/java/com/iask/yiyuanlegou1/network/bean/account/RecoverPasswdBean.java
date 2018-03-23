/* 
 * 系统名称：lswuyou
 * 类  名  称：RecoverPasswdBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-1 下午5:46:14
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.security.MD5Encryption;

import java.net.URLEncoder;

public class RecoverPasswdBean {
	/** 手机号 */
	public String phone;
	/** 令牌 */
	public String token;
	/** 新密码 */
	public String pwd;

	public RecoverPasswdBean(String phone, String token, String pwd) {
		this.phone = phone;
		this.token = token;
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("phone=" + URLEncoder.encode(phone, Constant.CHARACTER_ENCODING));
			sb.append("&token=" + URLEncoder.encode(token, Constant.CHARACTER_ENCODING));
			sb.append("&pwd=" + MD5Encryption.encodeMD5(URLEncoder.encode(pwd, Constant
					.CHARACTER_ENCODING)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
