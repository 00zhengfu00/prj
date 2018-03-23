/* 
 * 系统名称：lswuyou
 * 类  名  称：UpdateSchoolBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-18 下午5:27:53
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import com.iask.yiyuanlegou1.common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class UpdateSchoolBean {
	/** 必填 选择区县的id */
	public String aid;
	/**
	 * 可选 学校名称 如果使用的用户自定义填写的学校则填上用户名 如果是选择服务器返回的学校数据 则没必要填 但需要把sid填上
	 * 通过sid是可以找到关联的学校表
	 */
	public String sname;
	/** 可选 学校id 用户自定义填写的学校是没有sid的 */
	public String sid;

	public UpdateSchoolBean(String aid, String sname, String sid) {
		this.aid = aid;
		this.sname = sname;
		this.sid = sid;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("aid=" + aid);
		try {
			sb.append("&sname=" + URLEncoder.encode(sname, Constant.CHARACTER_ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sb.append("&sid=" + sid);
		return sb.toString();
	}
}
