package com.iask.yiyuanlegou1.network.respose.account;

public class GetOSSTokenResponseData {
	
	public OSSTokenInfo ossToken;

	public GetOSSTokenResponseData() {
		// TODO Auto-generated constructor stub
	}

	public OSSTokenInfo getOSSTokenInfo(){
		return ossToken;
	}
	
	public void setOSSTokenInfo(OSSTokenInfo ossToken){
		this.ossToken = ossToken;
	}
}
