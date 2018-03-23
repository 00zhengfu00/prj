package com.iask.yiyuanlegou1.network.bean.account;

import com.iask.yiyuanlegou1.common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class UnbindOpenPlatformBean {
	
	private String uid;
	private String type;

	public UnbindOpenPlatformBean(String uid, String type) {
		// TODO Auto-generated constructor stub
		this.uid = uid;
		this.type = type;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("btype=" + URLEncoder.encode(type, Constant.CHARACTER_ENCODING));
			sb.append("&uid=" + URLEncoder.encode(uid, Constant.CHARACTER_ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
