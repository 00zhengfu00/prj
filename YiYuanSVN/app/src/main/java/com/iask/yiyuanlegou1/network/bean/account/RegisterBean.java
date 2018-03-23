package com.iask.yiyuanlegou1.network.bean.account;

import java.net.URLEncoder;

import android.content.Context;

import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.security.MD5Encryption;


public class RegisterBean{

	private String utype = "0";
	private String phone = "";
	private String token = "";
	private String pwd = "";
	private int eduStage;
	private int subjectId;
	private String realName = "";
	
	public RegisterBean(Context context, String phone, String token, String pwd, String utype, int eduStage, int subjectId, String realName) {
		if (null != phone)this.phone = phone;
		if (null != token)this.token = token;
		if (null != pwd)this.pwd = pwd;
		this.eduStage = eduStage;
		this.subjectId = subjectId;
		this.utype = utype;
		this.realName = realName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("utype=" + URLEncoder.encode(utype, Constant.CHARACTER_ENCODING));
			sb.append("&phone=" + URLEncoder.encode(phone, Constant.CHARACTER_ENCODING));
			sb.append("&token=" + URLEncoder.encode(token, Constant.CHARACTER_ENCODING));
			sb.append("&eduStage=" + eduStage);
			sb.append("&subjectId=" + subjectId);
			String md5Pwd = MD5Encryption.encodeMD5(URLEncoder.encode(pwd, Constant
					.CHARACTER_ENCODING));
			sb.append("&pwd=" + md5Pwd);
			sb.append("&realName=" + URLEncoder.encode(realName, Constant.CHARACTER_ENCODING));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString() + super.toString();
	}
}
