package com.iask.yiyuanlegou1.network.respose.account;

public class OpenBindInfo {
	
	String btype;
	private LoginUserInfo loginVo;

	public LoginUserInfo getLoginVo() {
		return loginVo;
	}

	public void setLoginVo(LoginUserInfo loginVo) {
		this.loginVo = loginVo;
	}

	public OpenBindInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public String getBtype() {
		return btype;
	}

	public void setBtype(String btype) {
		this.btype = btype;
	}

}
