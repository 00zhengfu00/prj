package com.iask.yiyuanlegou1.network.bean.account;

import com.iask.yiyuanlegou1.common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class BindOpenPlatformBean {
	
	private String uid;
	private String btype;
	private String nickName;
	private String token;

	public BindOpenPlatformBean(String uid, String btype, String nickName, String token) {
		// TODO Auto-generated constructor stub
		this.uid = uid;
		this.btype = btype;
		this.nickName = nickName;
		this.token = token;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("uid=" + URLEncoder.encode(uid, Constant.CHARACTER_ENCODING));
			sb.append("&btype=" + URLEncoder.encode(btype, Constant.CHARACTER_ENCODING));
			sb.append("&nick=" + URLEncoder.encode(nickName, Constant.CHARACTER_ENCODING));
			sb.append("&token=" + URLEncoder.encode(token, Constant.CHARACTER_ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
