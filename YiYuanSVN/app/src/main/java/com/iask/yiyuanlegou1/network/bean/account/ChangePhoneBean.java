/* 
 * 系统名称：lswuyou
 * 类  名  称：ChangePhoneBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-6 下午3:34:26
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import com.iask.yiyuanlegou1.common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ChangePhoneBean {
	/** 手机号 */
	public String phone;
	/** 验证码成功返回的token */
	public String token;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("phone=" + phone);
		try {
			sb.append("&token=" + URLEncoder.encode(token, Constant.CHARACTER_ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
