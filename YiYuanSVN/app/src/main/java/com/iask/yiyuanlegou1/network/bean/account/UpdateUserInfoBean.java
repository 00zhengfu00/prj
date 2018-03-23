/* 
 * 系统名称：lswuyou
 * 类  名  称：UpdateUserInfoBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-8 上午11:17:47
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import com.iask.yiyuanlegou1.common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class UpdateUserInfoBean {
	/** 用户昵称 */
	public String nick;
	/** 性别 */
	public String gender;
	/** 头像 */
	public String portrait;
	/** 生日 */
	public String bday;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("nick=" + URLEncoder.encode(nick, Constant.CHARACTER_ENCODING));
			sb.append("&gender=" + gender);
			sb.append("&portrait=" + URLEncoder.encode(portrait, Constant.CHARACTER_ENCODING));
			sb.append("&bday=" + bday);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
