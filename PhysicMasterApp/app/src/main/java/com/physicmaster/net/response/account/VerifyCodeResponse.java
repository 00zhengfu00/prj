package com.physicmaster.net.response.account;


import com.physicmaster.net.response.Response;

public class VerifyCodeResponse extends Response {
	
	/** 服务器返回数据 */
	public VerifyCodeResponseData data;

	public class VerifyCodeResponseData {

		public String verifyToken;

		public String getVerifyToken() {
			return verifyToken;
		}
	}

}
