package com.iask.yiyuanlegou1.network.bean.account;

import com.iask.yiyuanlegou1.common.Constant;

import java.net.URLEncoder;


public class VerifyCodeBean {
	
	private String phone = "";
	private String type = "";// 0 // 0：获取注册验证码；1：获取找回密码验证码
	private String code = "";// 用户输入的验证码

	public VerifyCodeBean(String phone, String type, String code) {
		this.phone = phone;
		this.type = type;
		this.code = code;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("phone=" + URLEncoder.encode(phone, Constant.CHARACTER_ENCODING));
			sb.append("&type=" + URLEncoder.encode(type, Constant.CHARACTER_ENCODING));
			sb.append("&code=" + URLEncoder.encode(code, Constant.CHARACTER_ENCODING));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}

}
