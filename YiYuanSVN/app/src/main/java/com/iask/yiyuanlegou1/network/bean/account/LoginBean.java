/*
 * 系统名称：lswuyou
 * 类  名  称：LoginBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-10 上午9:43:58
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import java.net.URLEncoder;

import android.content.Context;

import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.security.MD5Encryption;


public class LoginBean{
	
	/** 登录账户名 */
	public String mAccount;
	/** 登录密码 */
	public String mPassword;
	
	
	public LoginBean(Context context, String account, String pwd) {
		mAccount = account;
		mPassword = pwd;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("phone=" + URLEncoder.encode(mAccount, Constant.CHARACTER_ENCODING));
			String md5Pwd = MD5Encryption.encodeMD5(URLEncoder.encode(mPassword, Constant
					.CHARACTER_ENCODING));
			sb.append("&pwd=" + md5Pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString() + super.toString();
	}
}
