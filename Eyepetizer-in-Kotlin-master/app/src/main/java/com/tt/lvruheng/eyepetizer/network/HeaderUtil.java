/*
 * 系统名称：lswuyou
 * 类  名  称：HeaderUtil.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-7 下午3:24:41
 * 功能说明： http 头部定义
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.tt.lvruheng.eyepetizer.network;


import java.util.HashMap;

public class HeaderUtil {
	/**
	 * 获取头部信息
	 * 
	 * @return
	 */
	public static HashMap<String, String> getDefaultHeader() {
		HashMap<String, String> defaultHeader = new HashMap<String, String>();
		defaultHeader.put("pf", "1");
		return defaultHeader;
	}

	/**
	 * 获取头部信息
	 * 
	 * @return
	 */
	public static HashMap<String, String> getDefaultNativeHeader() {
		HashMap<String, String> nativeHeader = getDefaultHeader();
		nativeHeader.put("pf", "1");
		nativeHeader.put("Accept-Encoding", "gzip,deflate");
		nativeHeader.put("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		nativeHeader
				.put("Accept",
						"text/html,text/javascript,application/json,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		return nativeHeader;
	}
}
